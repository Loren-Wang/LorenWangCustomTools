package android.lorenwang.tools.mobile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.lorenwang.tools.base.BaseUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;

import java.util.Locale;

/**
 * Created by LorenWang on 2018/8/16 0016.
 * 创建时间：2018/8/16 0016 下午 04:32
 * 创建人：王亮（Loren wang）
 * 功能作用：手机系统信息工具类
 * 思路：
 * 方法：1、获取当前手机系统语言
 *      2、获取当前系统上的语言列表(Locale列表)
 *      3、获取当前手机系统版本号
 *      4、获取手机型号
 *      5、获取手机厂商
 *      6、获取手机系统sdk版本号
 *      7、获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
 *      8、获取当前网络类型  return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class MobileSystemInfoUtils extends BaseUtils {
    private final String TAG = "MobileSystemInfoUtils";
    public static MobileSystemInfoUtils getInstance(){
        if(baseUtils == null){
            baseUtils = new MobileSystemInfoUtils();
        }
        return (MobileSystemInfoUtils) baseUtils;
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
     * @return  语言列表
     */
    public Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    public String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * 获取手机系统sdk版本号
     * @return
     */
    public int getSystemSdkVersion(){
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机品牌信息
     * @return
     */
    public String getMobileBrand(){
        return Build.MANUFACTURER;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return  手机IMEI
     */
    @SuppressLint({"MissingPermission"})
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getDeviceId();
        }
        return null;
    }

    /**
     * 获取当前网络类型
     * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static int getNetworkType(Context context) {
        int netType = 0;
        String netTypeName = null;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }


        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if(extraInfo != null) {
                if (extraInfo.toLowerCase().equals("cmnet")) {
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


}
