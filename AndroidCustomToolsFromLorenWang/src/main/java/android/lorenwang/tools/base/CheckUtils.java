package android.lorenwang.tools.base;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;

import java.io.File;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * 创建时间：2018-11-16 上午 10:19:49
 * 创建人：王亮（Loren wang）
 * 功能作用：检查工具类，用来检查各种，属于基础工具类
 * 思路：
 * 方法：1、检查是否有文件操作权限
 *      2、检查文件是否存在
 *      3、检查是否是图片文件
 *      4、根据传递进来的参数判断是否有该权限
 *      5、判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
 *      6、检测App是否安装
 *      7、检查App是否在运行
 *      8、检测一个服务是否在后台运行
 *      9、检查io操作工具类权限以及传入参数
 *
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class CheckUtils {
    private final String TAG = getClass().getName();
    private static CheckUtils baseUtils;
    public static CheckUtils getInstance() {
        if (baseUtils == null) {
            baseUtils = new CheckUtils();
        }
        return (CheckUtils) baseUtils;
    }


    /**
     * 检查是否拥有文件操作权限
     *
     * @param context
     * @return 有权限返回true，无权限返回false
     */
    public boolean checkFileOptionsPermisstion(Context context) {
        return checkAppPermisstion(context,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    /**
     * 检查文件是否存在
     *
     * @param filePath
     * @return
     */
    public boolean checkFileIsExit(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            LogUtils.logI(TAG, "被检查文件地址为空，不通过检测");
            return false;
        }
        File file = new File(filePath);
        boolean isExit = false;//文件是否存在记录
        if (file == null || file.isDirectory()) {
            LogUtils.logI(TAG, "被检查文件为空或被检测的地址为文件夹，不通过检测");
            return false;
        }
        if (file.exists()) {
            isExit = true;
            LogUtils.logI(TAG, "被检查文件存在");
        } else {
            LogUtils.logI(TAG, "被检查文件不存在");
        }
        file = null;
        return isExit;
    }

    /**
     * 检测文件是否是图片
     *
     * @param filePath
     * @return
     */
    public boolean checkFileIsImage(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            if (filePath.length() > 4 &&
                    (filePath.toLowerCase().substring(filePath.length() - 4).contains(".jpg")
                            || filePath.toLowerCase().substring(filePath.length() - 4).contains(".png")
                            || filePath.toLowerCase().substring(filePath.length() - 4).contains(".bmp")
                            || filePath.toLowerCase().substring(filePath.length() - 4).contains(".gif")
                            || filePath.toLowerCase().substring(filePath.length() - 4).contains(".psd")
                            || filePath.toLowerCase().substring(filePath.length() - 4).contains(".swf")
                            || filePath.toLowerCase().substring(filePath.length() - 4).contains(".svg")
                            || filePath.toLowerCase().substring(filePath.length() - 4).contains(".pcx")
                            || filePath.toLowerCase().substring(filePath.length() - 4).contains(".dxf")
                            || filePath.toLowerCase().substring(filePath.length() - 4).contains(".wmf")
                            || filePath.toLowerCase().substring(filePath.length() - 4).contains(".emf")
                            || filePath.toLowerCase().substring(filePath.length() - 4).contains(".lic")
                            || filePath.toLowerCase().substring(filePath.length() - 4).contains(".eps")
                            || filePath.toLowerCase().substring(filePath.length() - 4).contains(".tga"))) {
                LogUtils.logI(TAG, "被检测地址为图片地址，图片地址后缀：" + filePath.toLowerCase().substring(filePath.length() - 4));
                return true;
            } else if (filePath.length() > 5 &&
                    (filePath.toLowerCase().substring(filePath.length() - 5).contains(".jpeg")
                            || filePath.toLowerCase().substring(filePath.length() - 5).contains(".tiff"))) {
                LogUtils.logI(TAG, "被检测地址为图片地址，图片地址后缀：" + filePath.toLowerCase().substring(filePath.length() - 5));
                return true;
            }else {
                LogUtils.logI(TAG, "被检测地址为空或文件为非图片");
                return false;
            }
        } else {
            LogUtils.logI(TAG, "被检测地址为空或文件为非图片");
            return false;
        }
    }

    /**
     * 检测App权限
     * @param context App上下文
     * @param permisstions 权限列表
     * @return 有权限返回true，无权限返回false
     */
    public boolean checkAppPermisstion(Context context,@PermissionChecker.PermissionResult String... permisstions){
        if (context == null) {
            return false;
        }
        int length = permisstions.length;
        if(length == 0){
            return true;
        }
        context = context.getApplicationContext();
        if (Build.VERSION.SDK_INT >= 23) {
            for(int i = 0 ; i < length ; i++){
                //只要一个没权限则全部返回false
                if(ActivityCompat.checkSelfPermission(context, permisstions[i]) != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    /**
     * 检查io操作工具类权限以及传入参数
     * @param context 一定要传入applicationcontext
     * @param objects
     * @return
     */
    public boolean checkIOUtilsOptionsPermissionAndObjects(Context context,Object... objects){
        if(context == null){
            return false;
        }
        context = context.getApplicationContext();
        for(Object object : objects){
            if(object == null){
                return false;
            }
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(context
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context
                    , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        }else {
            return true;
        }

    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @return true 表示开启
     */
    public boolean checkGpsIsOpen(Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /**
     * 检测App是否安装
     * @param context 上下文
     * @param pkgName 要检测的包名
     * @return
     */
    public boolean checkAppIsInstall(Context context,String pkgName){
        try {
            context.getApplicationContext().getPackageManager().getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断app是否在运行
     * @param context 上下文
     * @param packName 包名
     * @return
     */
    public boolean checkAppIsRunning(Context context,String packName){
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packName) || info.baseActivity.getPackageName().equals(packName)) {
                isAppRunning = true;
                break;
            }
        }
        return isAppRunning;
    }

    /**
     * 判断一个服务是否在后台运行
     * @param context 上下文
     * @param judgeService 服务class
     * @return
     */
    public <T> boolean checkServiceIsRunning(Context context, Class<T> judgeService) {
        if(context == null){
            return false;
        }
        ActivityManager manager = (ActivityManager) context.getApplicationContext().getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (judgeService.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
