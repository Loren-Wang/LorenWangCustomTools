package android.lorenwang.tools.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.lorenwang.tools.AtlwConfig;
import android.lorenwang.tools.base.AtlwCheckUtil;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import javabase.lorenwang.tools.common.JtlwVariateDataParamUtils;

/**
 * 功能作用：activity工具类
 * 初始注释时间： 2021/1/21 3:17 下午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 发起权限请求--goToRequestPermissions(object,permisstions,permissionsRequestCode,permissionRequestCallback)
 * 接收到权限请求返回--receivePermissionsResult(requestCode,permissions,grantResults)(需要在当前Activity或者基类当中的onRequestPermissionsResult方法中调用那个该方法)
 * 控制软键盘显示与隐藏--setInputMethodVisibility(activity,view,visibility)
 * 返回APP级别的实例--getApplicationContext(context)
 * 允许退出App的判断以及线程--allowExitApp(time)
 * 检测App版本更新，通过versionName比较--checkAppVersionUpdate(oldVersion, newVersion)
 * 退出应用--exitApp(activity)
 * 获得应用是否在前台--isOnForeground()
 * 获取应用程序名称--getAppName()
 * 修改页面旋转方向--changeActivityScreenOrientation(activity)
 * 参数页面当前是否是横屏显示--isPageLandscape(activity)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AtlwActivityUtil {
    private final String TAG = getClass().getName();
    private static volatile AtlwActivityUtil optionsInstance;
    //权限请求键值对
    private final Map<Integer, AtlwPermissionRequestCallback> permissionRequestCallbackMap = new HashMap<>();

    private AtlwActivityUtil() {
    }

    public static AtlwActivityUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwActivityUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwActivityUtil();
                }
            }
        }
        return optionsInstance;
    }

    public Map<Integer, AtlwPermissionRequestCallback> getPermissionRequestCallbackMap() {
        return permissionRequestCallbackMap;
    }

    /**
     * 发起权限请求，和receivePermisstionsResult方法结合使用，如果没有receivePermisstionsResult方法则可能会导致无法产生回调
     *
     * @param context                   上下文实体
     * @param permisstions              权限列表
     * @param permissionsRequestCode    权限请求码
     * @param permissionRequestCallback 权限请求回调
     */
    public void goToRequestPermissions(@NotNull Object context, @NonNull String[] permisstions, int permissionsRequestCode,
            AtlwPermissionRequestCallback permissionRequestCallback) {
        //版本判断，小于23的不执行权限请求
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (permissionRequestCallback != null) {
                permissionRequestCallback.permissionRequestSuccessCallback(JtlwVariateDataParamUtils.getInstance().paramesArrayToList(permisstions),
                        permissionsRequestCode);
            }
        } else {
            //检测所有的权限是否都已经拥有
            if (AtlwCheckUtil.getInstance().checkAppPermission(permisstions)) {
                if (permissionRequestCallback != null) {
                    permissionRequestCallback.permissionRequestSuccessCallback(
                            JtlwVariateDataParamUtils.getInstance().paramesArrayToList(permisstions), permissionsRequestCode);
                }
            } else {
                //存储键值对
                permissionRequestCallbackMap.put(permissionsRequestCode, permissionRequestCallback);
                //请求权限
                if (context instanceof AppCompatActivity) {
                    ActivityCompat.requestPermissions((Activity) context, permisstions, permissionsRequestCode);
                } else if (context instanceof Activity) {
                    ((Activity) context).requestPermissions(permisstions, permissionsRequestCode);
                } else if (context instanceof Fragment) {
                    ((Fragment) context).requestPermissions(permisstions, permissionsRequestCode);
                }
            }
        }
    }

    /**
     * 接收到权限请求返回，需要在当前Activity或者基类当中的onRequestPermissionsResult方法中调用那个该方法
     *
     * @param requestCode  权限请求码
     * @param permissions  权限列表
     * @param grantResults 权限状态
     */
    public void receivePermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //获取回调
        AtlwPermissionRequestCallback permissionRequestCallback = permissionRequestCallbackMap.get(requestCode);
        if (permissionRequestCallback != null) {
            // If request is cancelled, the result arrays are empty.
            List<String> successPermissionList = new ArrayList<>();
            List<String> failPermissionList = new ArrayList<>();

            if (grantResults.length > 0 && grantResults.length == permissions.length) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        successPermissionList.add(permissions[i]);
                        AtlwLogUtil.logUtils.logI(TAG, "用户同意权限-user granted the permission!" + permissions[i]);
                    } else {
                        AtlwLogUtil.logUtils.logI(TAG, "用户不同意权限-user denied the permission!" + permissions[i]);
                        failPermissionList.add(permissions[i]);
                    }
                }
            } else {
                Collections.addAll(failPermissionList, permissions);
            }
            try {//只要有一个权限不通过则都失败
                if (failPermissionList.size() > 0) {
                    permissionRequestCallback.permissionRequestFailCallback(failPermissionList, requestCode);
                } else {
                    permissionRequestCallback.permissionRequestSuccessCallback(successPermissionList, requestCode);
                }
            } catch (Exception e) {
                AtlwLogUtil.logUtils.logE(TAG, e.getMessage());
            } finally {
                successPermissionList.clear();
                failPermissionList.clear();
            }

            //移除回调
            permissionRequestCallbackMap.remove(requestCode);
        }
    }

    /**
     * 控制软键盘显示与隐藏
     *
     * @param view       要显示或者隐藏的view
     * @param visibility 显示状态
     */
    public void setInputMethodVisibility(Activity activity, View view, int visibility) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view == null && visibility == View.GONE) {
            if (activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            return;
        }
        switch (visibility) {
            case View.VISIBLE:
                //显示软键盘 //
                if (view != null) {
                    view.setFocusableInTouchMode(true);
                    view.requestFocus();
                }
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
                break;
            case View.GONE:
                //隐藏软键盘 //
                view.clearFocus();
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                break;
            default:
                break;
        }
    }

    /**
     * 返回APP级别的实例（对于传递的上下文做转换）
     *
     * @param context 上下文
     * @return APP级别的实例，如果传入为空则返回也为空
     */
    public Context getApplicationContext(Context context) {
        if (context != null) {
            Context applicationContext = context.getApplicationContext();
            if (applicationContext != null) {
                context = applicationContext;
            }
            return context;
        } else {
            return null;
        }
    }

    /**
     * 是否允许退出App
     */
    private boolean allowExitApp = false;

    /**
     * 允许退出App的判断以及线程
     *
     * @param time 间隔时间
     * @return 是否允许退出App
     */
    public boolean allowExitApp(long time) {
        if (!allowExitApp) {
            allowExitApp = true;
            AtlwThreadUtil.getInstance().postOnChildThreadDelayed(new Runnable() {
                @Override
                public void run() {
                    allowExitApp = false;
                }
            }, time);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检测App版本更新，通过versionName比较
     *
     * @param oldVersion 旧版本versionName
     * @param newVersion 新版本versionName
     * @return 是否需要更新
     */
    public boolean checkAppVersionUpdate(String oldVersion, String newVersion) {
        if (oldVersion == null || newVersion == null) {
            return false;
        }
        String[] oldVersionSplit = oldVersion.split("\\.");
        String[] newVersionSplit = newVersion.split("\\.");
        int maxLength = Math.max(newVersion.length(), oldVersion.length());
        List<Integer> oldList = new ArrayList<>();
        for (String item : oldVersionSplit) {
            try {
                oldList.add(Integer.parseInt(item));
            } catch (Exception e) {
                return false;
            }
        }
        if (oldVersionSplit.length < maxLength) {
            for (int i = 0; i < maxLength - oldVersionSplit.length; i++) {
                oldList.add(0);
            }
        }

        List<Integer> newList = new ArrayList<>();
        for (String item : newVersionSplit) {
            try {
                newList.add(Integer.parseInt(item));
            } catch (Exception e) {
                return false;
            }
        }
        if (newVersionSplit.length < maxLength) {
            for (int i = 0; i < maxLength - newVersionSplit.length; i++) {
                newList.add(0);
            }
        }

        //判断是否有某一位的值大
        for (int i = 0; i < maxLength; i++) {
            if (newList.get(i).compareTo(oldList.get(i)) < 0) {
                return false;
            } else if (newList.get(i).compareTo(oldList.get(i)) > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 退出应用
     *
     * @param activity activity实例
     */
    public void exitApp(Activity activity) {
        try {
            activity.finish();
            for (Activity value : AtlwConfig.activityCollection) {
                value.finish();
            }
            System.exit(0);
        } catch (Exception ignored) {
        }
    }

    /**
     * 获得应用是否在前台
     */
    public boolean isOnForeground() {
        if (AtlwConfig.nowApplication != null) {
            ActivityManager activityManager = (ActivityManager) AtlwConfig.nowApplication.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
            if (tasksInfo.size() > 0) {
                // 应用程序位于堆栈的顶层
                return AtlwConfig.nowApplication.getPackageName().equals(tasksInfo.get(0).topActivity.getPackageName());
            }
        }
        return false;
    }

    /**
     * 获取应用程序名称
     */
    public synchronized String getAppName() {
        if (AtlwConfig.nowApplication != null) {
            try {
                PackageManager packageManager = AtlwConfig.nowApplication.getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(AtlwConfig.nowApplication.getPackageName(), 0);
                int labelRes = packageInfo.applicationInfo.labelRes;
                String name = AtlwConfig.nowApplication.getString(labelRes);
                AtlwLogUtil.logUtils.logE(TAG, "App名称获取成功:" + name);
                return name;
            } catch (Exception e) {
                AtlwLogUtil.logUtils.logE(TAG, "App名称获取失败");
            }
        }
        return null;

    }

    /**
     * 修改页面旋转方向
     *
     * @param activity 当前页面实例
     */
    public void changeActivityScreenOrientation(@NotNull Activity activity) {
        if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ||
                activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            //当前是竖排，需要修改为横排
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
            //当前是横排，需要修改为横排
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /**
     * 参数页面当前是否是横屏显示
     *
     * @param activity 当前页面
     * @return true代表当前是横屏显示
     */
    public boolean isPageLandscape(@NotNull Activity activity) {
        return activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE ||
                activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED ||
                activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }


}
