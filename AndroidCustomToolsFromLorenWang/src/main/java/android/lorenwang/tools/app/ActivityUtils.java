package android.lorenwang.tools.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.lorenwang.tools.base.CheckUtils;
import android.lorenwang.tools.base.LogUtils;
import android.lorenwang.tools.common.AtlwAndJavaCommonUtils;
import android.lorenwang.tools.file.FileOptionUtils;
import android.os.Build;
import android.support.annotation.NonNull;
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

/**
 * 创建时间：2018-12-21 下午 20:05:50
 * 创建人：王亮（Loren wang）
 * 功能作用：activity工具类
 * 思路：
 * 方法：1、去请求权限
 * 2、权限请求结果返回
 * 3、控制软键盘显示与隐藏
 * 4、通过系统相册选择图片后返回给activiy的实体的处理，用来返回新的图片文件
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class ActivityUtils {
    private final String TAG = getClass().getName();
    private static ActivityUtils baseUtils;
    //权限请求键值对
    private Map<Integer, PermissionRequestCallback> permissionRequestCallbackMap = new HashMap<>();

    private ActivityUtils() {
    }

    public static ActivityUtils getInstance() {
        if (baseUtils == null) {
            baseUtils = new ActivityUtils();
        }
        return (ActivityUtils) baseUtils;
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
                permissionRequestCallback.perissionRequestSuccessCallback(AtlwAndJavaCommonUtils.getInstance().paramesArrayToList(permisstions)
                        , permissionsRequestCode);
            }
        } else {
            //检测所有的权限是否都已经拥有
            //判断所有的权限是否是通过的
            if (CheckUtils.getInstance().checkAppPermisstion(activity, permisstions)) {
                if (permissionRequestCallback != null) {
                    permissionRequestCallback.perissionRequestSuccessCallback(AtlwAndJavaCommonUtils.getInstance().paramesArrayToList(permisstions)
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
                        LogUtils.logI(TAG, "用户同意权限-user granted the permission!" + permissions[i]);
                    } else {
                        LogUtils.logI(TAG, "用户不同意权限-user denied the permission!" + permissions[i]);
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
                LogUtils.logE(TAG, e.getMessage());
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
     * @param view
     * @param visibility
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
                view.setFocusableInTouchMode(false);
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
     * @param activity
     * @param data
     * @param saveFile
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
                boolean state = FileOptionUtils.getInstance().writeToFile(activity, true, new File(saveFile), inputStream,false);
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
}
