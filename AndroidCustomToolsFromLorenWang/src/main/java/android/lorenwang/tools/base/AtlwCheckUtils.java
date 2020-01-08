package android.lorenwang.tools.base;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.lorenwang.tools.AtlwSetting;
import android.os.Build;
import android.text.TextUtils;

import java.io.File;
import java.util.List;

import androidx.core.app.ActivityCompat;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * 创建时间：2018-11-16 上午 10:19:49
 * 创建人：王亮（Loren wang）
 * 功能作用：检查工具类，用来检查各种，属于基础工具类
 * 思路：
 * 方法：1、检查是否有文件操作权限
 * 2、检查文件是否存在
 * 3、检查是否是图片文件
 * 4、根据传递进来的参数判断是否有该权限
 * 5、判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
 * 6、检测App是否安装
 * 7、检查App是否在运行
 * 8、检测一个服务是否在后台运行
 * 9、检查io操作工具类权限以及传入参数
 * <p>
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class AtlwCheckUtils {
    private final String TAG = getClass().getName();
    private static volatile AtlwCheckUtils optionsInstance;

    private AtlwCheckUtils() {
    }

    public static AtlwCheckUtils getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwCheckUtils.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwCheckUtils();
                }
            }
        }
        return optionsInstance;
    }


    /**
     * 检查是否拥有文件操作权限
     *
     * @return 有权限返回true，无权限返回false
     */
    public boolean checkFileOptionsPermission() {
        return checkAppPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    /**
     * 检查文件是否存在
     *
     * @param filePath 文件地址
     * @return 文件是否存在
     */
    public boolean checkFileIsExit(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            AtlwLogUtils.logI(TAG, "被检查文件地址为空，不通过检测");
            return false;
        }
        File file = new File(filePath);
        boolean isExit = false;//文件是否存在记录
        if (file == null || file.isDirectory()) {
            AtlwLogUtils.logI(TAG, "被检查文件为空或被检测的地址为文件夹，不通过检测");
            return false;
        }
        if (file.exists()) {
            isExit = true;
            AtlwLogUtils.logI(TAG, "被检查文件存在");
        } else {
            AtlwLogUtils.logI(TAG, "被检查文件不存在");
        }
        file = null;
        return isExit;
    }

    /**
     * 检查文件是否存在
     *
     * @param filePath 文件地址
     * @return 文件是否存在
     */
    public boolean checkDirectoryIsExit(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            AtlwLogUtils.logI(TAG, "被检查文件地址为空，不通过检测");
            return false;
        }
        File file = new File(filePath);
        boolean isExit = false;//文件是否存在记录
        if (file == null || file.isFile()) {
            AtlwLogUtils.logI(TAG, "被检查文件为空或被检测的地址为文件，不通过检测");
            return false;
        }
        if (file.exists()) {
            isExit = true;
            AtlwLogUtils.logI(TAG, "被检查文件夹存在");
        } else {
            AtlwLogUtils.logI(TAG, "被检查文件夹不存在");
        }
        file = null;
        return isExit;
    }

    /**
     * 检测文件是否是图片
     *
     * @param filePath 文件地址
     * @return 是否是图片
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
                AtlwLogUtils.logI(TAG, "被检测地址为图片地址，图片地址后缀：" + filePath.toLowerCase().substring(filePath.length() - 4));
                return true;
            } else if (filePath.length() > 5 &&
                    (filePath.toLowerCase().substring(filePath.length() - 5).contains(".jpeg")
                            || filePath.toLowerCase().substring(filePath.length() - 5).contains(".tiff"))) {
                AtlwLogUtils.logI(TAG, "被检测地址为图片地址，图片地址后缀：" + filePath.toLowerCase().substring(filePath.length() - 5));
                return true;
            } else {
                AtlwLogUtils.logI(TAG, "被检测地址为空或文件为非图片");
                return false;
            }
        } else {
            AtlwLogUtils.logI(TAG, "被检测地址为空或文件为非图片");
            return false;
        }
    }

    /**
     * 检测App权限
     *
     * @param permissions 权限列表
     * @return 有权限返回true，无权限返回false
     */
    public boolean checkAppPermission(String... permissions) {
        if (AtlwSetting.nowApplication == null) {
            return false;
        }
        int length = permissions.length;
        if (length == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            for (String permission : permissions) {
                //只要一个没权限则全部返回false
                if (ActivityCompat.checkSelfPermission(AtlwSetting.nowApplication, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 检查io操作工具类权限以及传入参数
     *
     * @param objects 传入的参数列表
     * @return 是否有权限
     */
    public boolean checkIOUtilsOptionsPermissionAndObjects(Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                return false;
            }
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(AtlwSetting.nowApplication
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(AtlwSetting.nowApplication
                    , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }

    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @return true 表示开启
     */
    public boolean checkGpsIsOpen() {
        LocationManager locationManager
                = (LocationManager) AtlwSetting.nowApplication.getSystemService(Context.LOCATION_SERVICE);
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
     *
     * @param pkgName 要检测的包名
     * @return app是否安装
     */
    public boolean checkAppIsInstall(String pkgName) {
        try {
            AtlwSetting.nowApplication.getPackageManager().getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断app是否在运行
     *
     * @param packName 包名
     * @return 是否在运行
     */
    public boolean checkAppIsRunning(String packName) {
        ActivityManager am = (ActivityManager) AtlwSetting.nowApplication.getSystemService(Context.ACTIVITY_SERVICE);
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
     *
     * @param <T>          泛型
     * @param judgeService 服务class
     * @return 是否允许
     */
    public <T> boolean checkServiceIsRunning(Class<T> judgeService) {
        ActivityManager manager = (ActivityManager) AtlwSetting.nowApplication.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (judgeService.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
