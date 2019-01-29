package android.lorenwang.tools.app;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.lorenwang.tools.base.BaseUtils;
import android.lorenwang.tools.base.CheckUtils;
import android.lorenwang.tools.base.LogUtils;
import android.lorenwang.tools.common.AndJavaCommonUtils;
import android.os.Build;
import android.support.annotation.NonNull;

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
 *      2、权限请求结果返回
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class ActivityUtils extends BaseUtils {
    private final String TAG = getClass().getName();
    //权限请求键值对
    private Map<Integer,PermissionRequestCallback> permissionRequestCallbackMap = new HashMap<>();

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
     * @param activity Activity实体
     * @param permisstions 权限列表
     * @param permissionsRequestCode 权限请求码
     * @param permissionRequestCallback 权限请求回调
     */
    public void goToRequestPermisstions(Activity activity, @NonNull String[] permisstions, int permissionsRequestCode
            , PermissionRequestCallback permissionRequestCallback){
        //版本判断，小于23的不执行权限请求
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            if(permissionRequestCallback != null) {
                permissionRequestCallback.perissionRequestSuccessCallback(AndJavaCommonUtils.getInstance().paramesArrayToList(permisstions)
                        , permissionsRequestCode);
            }
        }else {
            //检测所有的权限是否都已经拥有
            //判断所有的权限是否是通过的
            if (CheckUtils.getInstance().checkAppPermisstion(activity,permisstions)) {
                if(permissionRequestCallback != null) {
                    permissionRequestCallback.perissionRequestSuccessCallback(AndJavaCommonUtils.getInstance().paramesArrayToList(permisstions)
                            , permissionsRequestCode);
                }
            } else {//请求权限
                //存储键值对
                permissionRequestCallbackMap.put(permissionsRequestCode,permissionRequestCallback);
                activity.requestPermissions(permisstions, permissionsRequestCode);
            }
        }
    }

    /**
     * 接收到权限请求返回，需要在当前Activity或者基类当中的onRequestPermissionsResult方法中调用那个该方法
     *
     * @param activity Activity实体
     * @param requestCode 权限请求码
     * @param permissions 权限列表
     * @param grantResults 权限状态
     */
    public void receivePermisstionsResult(Activity activity,int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        //获取回调
        PermissionRequestCallback permissionRequestCallback = permissionRequestCallbackMap.get(requestCode);
        if(permissionRequestCallback != null) {
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
}
