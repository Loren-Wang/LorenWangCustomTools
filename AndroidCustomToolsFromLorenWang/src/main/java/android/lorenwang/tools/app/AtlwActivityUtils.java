package android.lorenwang.tools.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.lorenwang.tools.AtlwSetting;
import android.lorenwang.tools.base.AtlwCheckUtils;
import android.lorenwang.tools.base.AtlwLogUtils;
import android.lorenwang.tools.file.AtlwFileOptionUtils;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import javabase.lorenwang.tools.common.JtlwVariateDataParamUtils;

/**
 * 创建时间：2018-12-21 下午 20:05:50
 * 创建人：王亮（Loren wang）
 * 功能作用：activity工具类
 * 思路：
 * 方法：
 * 1、去请求权限
 * 2、权限请求结果返回
 * 3、控制软键盘显示与隐藏
 * 4、通过系统相册选择图片后返回给activiy的实体的处理，用来返回新的图片文件
 * 5、返回APP级别的实例（对于传递的上下文做转换）
 * 6、允许退出App的判断以及线程
 * 7、检测App版本更新，通过versionName比较
 * 8、退出应用
 * 9、获得应用是否在前台
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class AtlwActivityUtils {
    private final String TAG = getClass().getName();
    private static volatile AtlwActivityUtils optionsUtils;
    //权限请求键值对
    private Map<Integer, PermissionRequestCallback> permissionRequestCallbackMap = new HashMap<>();

    private AtlwActivityUtils() {
    }

    public static AtlwActivityUtils getInstance() {
        synchronized (AtlwActivityUtils.class) {
            if (optionsUtils == null) {
                optionsUtils = new AtlwActivityUtils();
            }
        }
        return (AtlwActivityUtils) optionsUtils;
    }

    public Map<Integer, PermissionRequestCallback> getPermissionRequestCallbackMap() {
        return permissionRequestCallbackMap;
    }

    /**
     * 发起权限请求，和receivePermisstionsResult方法结合使用，如果没有receivePermisstionsResult方法则可能会导致无法产生回调
     *
     * @param activity                  Activity实体
     * @param permisstions              权限列表
     * @param permissionsRequestCode    权限请求码
     * @param permissionRequestCallback 权限请求回调
     */
    public void goToRequestPermisstions(Activity activity, @NonNull String[] permisstions, int permissionsRequestCode
            , PermissionRequestCallback permissionRequestCallback) {
        //版本判断，小于23的不执行权限请求
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (permissionRequestCallback != null) {
                permissionRequestCallback.perissionRequestSuccessCallback(JtlwVariateDataParamUtils.getInstance().paramesArrayToList(permisstions)
                        , permissionsRequestCode);
            }
        } else {
            //检测所有的权限是否都已经拥有
            //判断所有的权限是否是通过的
            if (AtlwCheckUtils.getInstance().checkAppPermisstion(activity, permisstions)) {
                if (permissionRequestCallback != null) {
                    permissionRequestCallback.perissionRequestSuccessCallback(JtlwVariateDataParamUtils.getInstance().paramesArrayToList(permisstions)
                            , permissionsRequestCode);
                }
            } else {//请求权限
                //存储键值对
                permissionRequestCallbackMap.put(permissionsRequestCode, permissionRequestCallback);
                activity.requestPermissions(permisstions, permissionsRequestCode);
            }
        }
    }

    /**
     * 接收到权限请求返回，需要在当前Activity或者基类当中的onRequestPermissionsResult方法中调用那个该方法
     *
     * @param activity     Activity实体
     * @param requestCode  权限请求码
     * @param permissions  权限列表
     * @param grantResults 权限状态
     */
    public void receivePermisstionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //获取回调
        PermissionRequestCallback permissionRequestCallback = permissionRequestCallbackMap.get(requestCode);
        if (permissionRequestCallback != null) {
            // If request is cancelled, the result arrays are empty.
            List<String> successPermissionList = new ArrayList<>();
            List<String> failPermissionList = new ArrayList<>();

            if (grantResults.length > 0 && grantResults.length == permissions.length) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        successPermissionList.add(permissions[i]);
                        AtlwLogUtils.logI(TAG, "用户同意权限-user granted the permission!" + permissions[i]);
                    } else {
                        AtlwLogUtils.logI(TAG, "用户不同意权限-user denied the permission!" + permissions[i]);
                        failPermissionList.add(permissions[i]);
                    }
                }
            } else {
                for (int i = 0; i < permissions.length; i++) {
                    failPermissionList.add(permissions[i]);
                }
            }
            try {//只要有一个权限不通过则都失败
                if (failPermissionList.size() > 0) {
                    permissionRequestCallback.perissionRequestFailCallback(failPermissionList, requestCode);
                } else {
                    permissionRequestCallback.perissionRequestSuccessCallback(successPermissionList, requestCode);
                }
            } catch (Exception e) {
                AtlwLogUtils.logE(TAG, e.getMessage());
            } finally {
                successPermissionList.clear();
                failPermissionList.clear();
                successPermissionList = null;
                failPermissionList = null;
            }

            //移除回调
            permissionRequestCallbackMap.remove(requestCode);
        }
    }

    /**
     * 控制软键盘显示与隐藏
     *
     * @param activity   界面实例
     * @param view       要显示或者隐藏的view
     * @param visibility 显示状态
     */
    public void setInputMethodVisibility(Activity activity, View view, int visibility) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view == null && visibility == View.GONE) {
            if (activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            return;
        }
        switch (visibility) {
            case View.VISIBLE:
                //显示软键盘 //
                view.setFocusableInTouchMode(true);
                view.requestFocus();
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
        imm = null;
    }

    /**
     * 通过系统相册选择图片后返回给activiy的实体的处理，用来返回新的图片文件
     *
     * @param activity 界面实例
     * @param data     intent
     * @param saveFile 保存地址
     * @return 返回新图片地址
     */
    public String onActivityResultFromPhotoAlbum(Activity activity, Intent data, String saveFile) {
        if (activity == null || data == null || saveFile == null || "".equals(saveFile)) {
            return null;
        }
        if (data != null && data.getData() != null) {
            //目标文件夹
            InputStream inputStream = null;//文件图片输入流
            try {
                inputStream = activity.getContentResolver().openInputStream(data.getData());
                boolean state = AtlwFileOptionUtils.getInstance().writeToFile(activity, true, new File(saveFile), inputStream, false);
                if (state) {
                    return saveFile;
                } else {
                    return null;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        inputStream = null;
                    }
                }
            }
        } else {
            return null;
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
            AtlwThreadUtils.getInstance().postOnChildThreadDelayed(new Runnable() {
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
        String[] oldVersionSplit = oldVersion.split(".");
        String[] newVersionSplit = newVersion.split(".");
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
            if (newList.get(i).compareTo(oldList.get(i)) > 0) {
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
            for (Activity value : AtlwSetting.activityCollection) {
                value.finish();
            }
            System.exit(0);
        } catch (Exception ignored) {
        }
    }

    /**
     * 获得应用是否在前台
     */
    public boolean isOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            // 应用程序位于堆栈的顶层
            return context.getPackageName().equals(
                    tasksInfo.get(0).topActivity.getPackageName());
        }
        return false;
    }

}
