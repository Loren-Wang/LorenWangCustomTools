package android.lorenwang.tools.mobile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.lorenwang.tools.AtlwConfig;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;

import androidx.annotation.RequiresPermission;

/**
 * 功能作用：手机系统信息工具类
 * 初始注释时间： 2018/8/16 0016 下午 04:32
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 获取当前手机系统语言--getSystemLanguage()（返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”）
 * 获取当前系统上的语言列表(Locale列表)--getSystemLanguageList()
 * 获取当前手机系统版本号--getSystemVersion()
 * 获取手机型号--getSystemModel()
 * 获取手机厂商--getDeviceBrand()
 * 获取手机系统sdk版本号--getSystemSdkVersion()
 * 获取手机品牌信息--getMobileBrand()
 * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)--getIMEIInfo()
 * 获取wifi的mac地址，适配到android Q--getMac()
 * <p>
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AtlwMobileSystemInfoUtil {
    private final String TAG = getClass().getName();
    private static volatile AtlwMobileSystemInfoUtil optionsInstance;

    private AtlwMobileSystemInfoUtil() {
    }

    public static AtlwMobileSystemInfoUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwMobileSystemInfoUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwMobileSystemInfoUtil();
                }
            }
        }
        return optionsInstance;
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * 获取手机系统sdk版本号
     *
     * @return 手机系统版本号
     */
    public int getSystemSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机品牌信息
     *
     * @return 返回手机品牌信息
     */
    public String getMobileBrand() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
    @SuppressLint({"MissingPermission"})
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public String getIMEIInfo() {
        TelephonyManager tm = (TelephonyManager) AtlwConfig.nowApplication.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getDeviceId();
        }
        return null;
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static int getNetworkType() {
        int netType = 0;
        String netTypeName = null;
        ConnectivityManager connectivityManager = (ConnectivityManager) AtlwConfig.nowApplication.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }


        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (extraInfo != null) {
                if (extraInfo.equalsIgnoreCase("cmnet")) {
                    netType = 3;
                    netTypeName = "cmNet";
                } else {
                    netType = 2;
                    netTypeName = "cmWap";
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;
            netTypeName = "wifi";
        }

        return netType;
    }


    /**
     * 获取wifi的mac地址，适配到android Q
     */
    @SuppressLint("HardwareIds")
    public static String getMac() {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                String str = getMacMoreThanM();
                if (!TextUtils.isEmpty(str)) {
                    return str;
                }
            }
            // 6.0以下手机直接获取wifi的mac地址即可
            WifiManager wifiManager = (WifiManager) AtlwConfig.nowApplication.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                return wifiInfo.getMacAddress();
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    /**
     * android 6.0+获取wifi的mac地址
     */
    private static String getMacMoreThanM() {
        try {
            //获取本机器所有的网络接口
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = enumeration.nextElement();
                //获取硬件地址，一般是MAC
                byte[] arrayOfByte = networkInterface.getHardwareAddress();
                if (arrayOfByte == null || arrayOfByte.length == 0) {
                    continue;
                }

                StringBuilder stringBuilder = new StringBuilder();
                for (byte b : arrayOfByte) {
                    //格式化为：两位十六进制加冒号的格式，若是不足两位，补0
                    stringBuilder.append(String.format("%02X:", b));
                }
                if (stringBuilder.length() > 0) {
                    //删除后面多余的冒号
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }
                String str = stringBuilder.toString();
                // wlan0:无线网卡 eth0：以太网卡
                if (networkInterface.getName().equals("wlan0")) {
                    return str;
                }
            }
        } catch (SocketException socketException) {
            return null;
        }
        return null;
    }

}
