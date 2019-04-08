package android.lorenwang.tools.app;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Intent;
import android.lorenwang.tools.base.LogUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 创建时间：2019-04-05 下午 21:38:10
 * 创建人：王亮（Loren wang）
 * 功能作用：安卓端亮度调节工具类
 * 思路：
 * 方法：1、返回activity生命周期监听
 *      2、注册亮度观察者
 *      3、解注册亮度观察者
 *      4、判断是否开启了自动亮度调节
 *      5、获取屏幕的亮度   * 系统亮度模式中，自动模式与手动模式获取到的系统亮度的值不同
 *      6、获取手动模式下的屏幕亮度
 *      7、获取自动模式下的屏幕亮度
 *      8、 设置亮度:通过设置 Windows 的 screenBrightness 来修改当前 Windows 的亮度
 *      9、保存亮度设置状态
 *      10、停止自动亮度调节
 *      11、开始自动亮度调节
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：使用时在application通过getActivityLifecycleCallbacks获取生命周期监听，或者对于单个Activity界面进行注册反注册
 */

public class AdBrightnessChangeUtils {
    private final String TAG = "AdBrightnessChangeUtils";
    private static AdBrightnessChangeUtils adBrightnessChangeUtils;
    public static AdBrightnessChangeUtils getInstance(){
        if(adBrightnessChangeUtils == null){
            adBrightnessChangeUtils = new AdBrightnessChangeUtils();
        }
        return adBrightnessChangeUtils;
    }

    //注册 Brightness 的 uri
    private final Uri BRIGHTNESS_MODE_URI =
            Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE);
    private final Uri BRIGHTNESS_URI =
            Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
    private final Uri BRIGHTNESS_ADJ_URI =
            Settings.System.getUriFor("screen_auto_brightness_adj");

    /**
     * 亮度观察者集合
     */
    private Map<Activity,AdBrightnessChangeContentObserver> observerMap = new ConcurrentHashMap<>();
    //activity生命周期监听
    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            registerBrightObserver(activity);
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            unregisterBrightObserver(activity);
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    };

    /**
     * 返回生命周期监听用于在application当中注册，这个可以对全部界面进行亮度改变的注册以及反注册监听，当然可以不再application当中注册，在单个页面去注册以及反注册
     * @return
     */
    public Application.ActivityLifecycleCallbacks getActivityLifecycleCallbacks() {
        return activityLifecycleCallbacks;
    }

    /**
     * 注册亮度观察者
     */
    public void registerBrightObserver(Activity activity) {
        try {
            AdBrightnessChangeContentObserver mBrightObserver = getActivityContentResolver(activity);
            if (mBrightObserver != null) {
                if (!mBrightObserver.isRegist()) {
                    final ContentResolver cr = activity.getContentResolver();
                    cr.unregisterContentObserver(mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_MODE_URI, false, mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_URI, false, mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_ADJ_URI, false, mBrightObserver);
                    mBrightObserver.setRegist(true);
                }
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "注册亮度观察者异常! " + throwable);
        }
    }

    /**
     * 解注册亮度观察者
     */
    public void unregisterBrightObserver(Activity activity) {
        try {
            AdBrightnessChangeContentObserver mBrightObserver = getActivityContentResolver(activity);
            if (mBrightObserver != null) {
                if (!mBrightObserver.isRegist()) {
                    activity.getContentResolver().unregisterContentObserver(mBrightObserver);
                    mBrightObserver.setRegist(false);
                    observerMap.remove(activity);
                }
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "解注册亮度观察者异常! " + throwable);
        }
    }

    /**
     * 判断是否开启了自动亮度调节
     */
    public boolean isAutoBrightness(Activity activity) {
        boolean isAuto = false;
        try {
            isAuto = Settings.System.getInt(activity.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Settings.SettingNotFoundException e){
            e.printStackTrace();
        }
        return isAuto;
    }

    /**
     * 获取屏幕的亮度
     * 系统亮度模式中，自动模式与手动模式获取到的系统亮度的值不同
     */
    public int getScreenBrightness(Activity activity) {
        if(isAutoBrightness(activity)){
            return getAutoScreenBrightness(activity);
        }else{
            return getManualScreenBrightness(activity);
        }
    }

    /**
     * 获取手动模式下的屏幕亮度
     * @return value:0~255
     */
    public int getManualScreenBrightness(Activity activity) {
        int nowBrightnessValue = 0;
        ContentResolver resolver = activity.getContentResolver();
        try {
            nowBrightnessValue = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }

    /**
     * 获取自动模式下的屏幕亮度
     * @return value:0~255
     */
    public int getAutoScreenBrightness(Activity activity) {
        float nowBrightnessValue = 0;

        //获取自动调节下的亮度范围在 0~1 之间
        ContentResolver resolver = activity.getContentResolver();
        try {
            //TODO:获取到的值与实际的亮度有差异，没有找到能够获得真正亮度值的方法，希望大佬能够告知。

            nowBrightnessValue = Settings.System.getFloat(resolver, "screen_auto_brightness_adj");
            LogUtils.logI(TAG, "getAutoScreenBrightness: " + nowBrightnessValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //转换范围为 (0~255)
        float fValue = nowBrightnessValue * 225.0f;
        LogUtils.logI(TAG,"brightness: " + fValue);
        return (int)fValue;
    }

    /**
     * 设置亮度:通过设置 Windows 的 screenBrightness 来修改当前 Windows 的亮度
     * lp.screenBrightness:参数范围为 0~1
     */
    public void setBrightness(Activity activity, int brightness) {
        try{
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            //将 0~255 范围内的数据，转换为 0~1
            lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
            LogUtils.logI(TAG, "lp.screenBrightness == " + lp.screenBrightness);
            activity.getWindow().setAttributes(lp);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 保存亮度设置状态
     */
    public void saveBrightness(Activity activity, int brightness) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(activity)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
        else {
            try{
                Uri uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
                Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
                // resolver.registerContentObserver(uri, true, myContentObserver);
                activity.getContentResolver().notifyChange(uri, null);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    /**
     * 停止自动亮度调节
     *
     * @param activity
     */
    public void stopAutoBrightness(Activity activity) {
        //动态申请权限
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(activity)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
        else {
            Settings.System.putInt(activity.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        }

    }

    /**
     * 开启亮度自动调节
     *
     * @param activity
     */
    public void startAutoBrightness(Activity activity) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(activity)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        }
        else {
            //有了权限，具体的动作
            Settings.System.putInt(activity.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        }
    }

    /**
     * 获取亮度观察者，如果有初始化则直接返回，否则不返回
     * @param activity Activity实例
     * @return 返回观察者
     */
    private AdBrightnessChangeContentObserver getActivityContentResolver(final Activity activity){
        AdBrightnessChangeContentObserver observer = observerMap.get(activity);
        if(observer == null){
            observer = new AdBrightnessChangeContentObserver(new Handler()) {
                @Override
                public void onChange(boolean selfChange) {
                    onChange(selfChange, null);
                }

                @Override
                public void onChange(boolean selfChange, Uri uri) {
                    super.onChange(selfChange);

                    //判断当前是否跟随屏幕亮度，如果不是则返回
                    if (selfChange) return;

                    //如果系统亮度改变，则修改当前 Activity 亮度
                    if (BRIGHTNESS_MODE_URI.equals(uri)) {
                        LogUtils.logI(TAG, "亮度模式改变");
                    } else if (BRIGHTNESS_URI.equals(uri) && !isAutoBrightness(activity)) {
                        LogUtils.logI(TAG, "亮度模式为手动模式 值改变");
                        setBrightness(activity, getScreenBrightness(activity));
                    } else if (BRIGHTNESS_ADJ_URI.equals(uri) && isAutoBrightness(activity)) {
                        LogUtils.logI(TAG, "亮度模式为自动模式 值改变");
                        setBrightness(activity, getScreenBrightness(activity));
                    } else {
                        LogUtils.logI(TAG, "亮度调整 其他");
                    }
                }
            };
            observerMap.put(activity,observer);
        }
        return observer;
    }
}
