package android.lorenwang.tools.app;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.lorenwang.tools.messageTransmit.AtlwFlyMessageMsgTypes;
import android.lorenwang.tools.messageTransmit.AtlwFlyMessageUtil;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.WindowManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;

/**
 * 功能作用：安卓端亮度调节工具类
 * 初始注释时间： 2019/4/5 15:07
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 获取当前屏幕亮度(0-1)--getScreenBrightness()
 * 获取当前屏幕亮度(0-1)--getScreenBrightness(activity)
 * 更新手机系统亮度模式--updateMobileSystemBrightnessMode(isAuto)
 * 更新手机系统亮度模式--updateMobileSystemBrightnessMode(activity,isAuto)
 * 设置亮度--setBrightness(brightness,isCallback)
 * 设置亮度--setBrightness(activity, brightness, isCallback)
 * 判断Activity界面亮度是否是自动的--isActivityAutoBrightness()
 * 判断Activity界面亮度是否是自动的--isActivityAutoBrightness(activity)
 * 判断是否开启了自动亮度调节--isMobileSystemAutoBrightness()
 * 判断是否开启了自动亮度调节--isMobileSystemAutoBrightness(activity)
 * 保存亮度设置状态--saveBrightnessToMobileSystem(brightness)
 * 保存亮度设置状态--saveBrightnessToMobileSystem(activity,brightness)
 * 设置屏幕亮度跟随系统--setBrightnessFollowMobileSystem()
 * 设置屏幕亮度跟随系统--setBrightnessFollowMobileSystem(activity)
 * 注册亮度观察者--registerBrightObserver(activity,mBrightObserver)
 * 解注册亮度观察者--unregisterBrightObserver(activity)
 * 获取过滤蓝光后的颜色值--getColor(blueFilterPercent)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：使用时在application通过getActivityLifecycleCallbacks获取生命周期监听，或者对于单个Activity界面进行注册反注册
 *
 * @author 王亮（Loren）
 */
public class AtlwBrightnessChangeUtil {
    private final String TAG = getClass().getName();
    private static volatile AtlwBrightnessChangeUtil optionsInstance;

    public static AtlwBrightnessChangeUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwBrightnessChangeUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwBrightnessChangeUtil();
                }
            }
        }
        return optionsInstance;
    }

    private AtlwBrightnessChangeUtil() {
        //获取Activity生命周期监听的消息接收
        AtlwFlyMessageUtil.FlyMessgeCallback flyMessgeCallback = new AtlwFlyMessageUtil.FlyMessgeCallback() {
            @Override
            public void msg(int msgType, Object... msgs) {
                if (msgType == AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_RESUMED) {
                    optionsActivity = (Activity) msgs[0];
                    AtlwBrightnessChangeContentObserver observer = observerMap.get(optionsActivity);
                    if (observer != null) {
                        registerBrightObserver(optionsActivity, observer);
                    }
                }
                //                switch (msgType) {
                //                    case AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_CREATE:
                //                        break;
                //                    case AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_START:
                //                        break;
                //                    case AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_RESUMED:
                //                        optionsActivity = (Activity) msgs[0];
                //                        AtlwBrightnessChangeContentObserver observer = observerMap.get(optionsActivity);
                //                        if (observer != null) {
                //                            registerBrightObserver(optionsActivity, observer);
                //                        }
                //                        break;
                //                    case AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_PAUSED:
                //                        break;
                //                    case AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_STOPPED:
                //                        break;
                //                    case AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_SAVE_INSTANCE_STATE:
                //                        break;
                //                    case AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_DESTROYED:
                //                        break;
                //                    default:
                //                        break;
                //                }
            }
        };
        AtlwFlyMessageUtil.getInstance().

                registerMsgCallback(this, AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_RESUMED, flyMessgeCallback, false, false);
        AtlwFlyMessageUtil.getInstance().

                registerMsgCallback(this, AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_PAUSED, flyMessgeCallback, false, false);
        //        FlyMessageUtils.getInstance().registMsgCallback(this, FlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_STOPPED, flyMessgeCallback, false, false);
        //        FlyMessageUtils.getInstance().registMsgCallback(this, FlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_SAVE_INSTANCE_STATE, flyMessgeCallback, false, false);
        //        FlyMessageUtils.getInstance().registMsgCallback(this, FlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_DESTROYED, flyMessgeCallback, false, false);


    }

    /**
     * 当前操作的Activity
     */
    private Activity optionsActivity;

    /**
     * 获取当前屏幕亮度(0-1)
     *
     * @return 当前屏幕亮度
     */
    public float getScreenBrightness() {
        return getScreenBrightness(null);
    }

    /**
     * 获取当前屏幕亮度(0-1)
     *
     * @param activity 界面实例
     * @return 屏幕高度
     */
    public float getScreenBrightness(Activity activity) {
        activity = getActivity(activity);
        float screenBrightness = 0;
        if (activity != null) {
            //如果Activity界面不是自动的
            if (!isActivityAutoBrightness(activity)) {
                return activity.getWindow().getAttributes().screenBrightness;
            }

            ContentResolver resolver = activity.getContentResolver();
            try {
                screenBrightness = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);
            } catch (Exception e) {
                AtlwLogUtil.logUtils.logI(TAG, "获取手机系统当前亮度失败");
            }

            AtlwLogUtil.logUtils.logI(TAG, "获取手机系统当前亮度:" + screenBrightness);
        } else {
            AtlwLogUtil.logUtils.logI(TAG, "获取手机系统当前亮度失败");
        }
        screenBrightness = screenBrightness % 255 / 255.0F;
        return screenBrightness;
    }

    /**
     * 更新手机系统亮度模式
     *
     * @param isAuto 是否是自动亮度
     */
    public void updateMobileSystemBrightnessMode(boolean isAuto) {
        updateMobileSystemBrightnessMode(null, isAuto);
    }

    /**
     * 更新手机系统亮度模式
     *
     * @param activity 界面实例
     * @param isAuto   是否是自动亮度
     */
    public void updateMobileSystemBrightnessMode(Activity activity, boolean isAuto) {
        activity = getActivity(activity);
        if (activity != null) {
            if (isAuto) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(activity)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:" + activity.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                } else {
                    //有了权限，具体的动作
                    Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                            Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
                }
                AtlwLogUtil.logUtils.logI(TAG, "修改手机系统亮度为系统自动亮度" + (isMobileSystemAutoBrightness(activity) ? "成功" : "失败"));
            } else {
                //动态申请权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(activity)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:" + activity.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                } else {
                    Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                }
                AtlwLogUtil.logUtils.logI(TAG, "修改手机系统亮度为系统手动调节亮度" + (!isMobileSystemAutoBrightness(activity) ? "成功" : "失败"));
            }
        }
    }

    /**
     * 设置亮度:通过设置 Windows 的 screenBrightness 来修改当前 Windows 的亮度
     * lp.screenBrightness:参数范围为 0~1
     *
     * @param brightness 亮度范围
     * @param isCallback 是否回调
     */
    public void setBrightness(@FloatRange(from = 0,to = 1) float brightness, boolean isCallback) {
        setBrightness(null, brightness, isCallback);
    }

    /**
     * 设置亮度:通过设置 Windows 的 screenBrightness 来修改当前 Windows 的亮度
     * lp.screenBrightness:参数范围为 0~1
     *
     * @param activity   界面实例
     * @param brightness 要设置的亮度
     * @param isCallback 回调
     */
    public void setBrightness(Activity activity,@FloatRange(from = 0,to = 1) float brightness, boolean isCallback) {
        activity = getActivity(activity);
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                if (brightness > 0) {
                    //将 0~255 范围内的数据，转换为 0~1
                    lp.screenBrightness = brightness;
                    if (lp.screenBrightness > 1) {
                        lp.screenBrightness = 1;
                    }
                    activity.getWindow().setAttributes(lp);
                    AtlwLogUtil.logUtils.logI(TAG, "更新屏幕亮度成功：" + lp.screenBrightness);

                    if (isCallback) {
                        //回传亮度改变
                        AtlwBrightnessChangeContentObserver observer = observerMap.get(activity);
                        if (observer != null) {
                            observer.onBrightnessChange(brightness);
                        }
                    }
                } else {
                    lp.screenBrightness = brightness;
                    activity.getWindow().setAttributes(lp);
                    AtlwLogUtil.logUtils.logI(TAG, "更新屏幕亮度成功：使用系统定义的模式，模式值为：" + brightness);
                }
            } catch (Exception ex) {
                AtlwLogUtil.logUtils.logI(TAG, "更新屏幕亮度失败");
            }
        }
    }

    /**
     * 判断Activity界面亮度是否是自动的
     *
     * @return 是否是自动的
     */
    public boolean isActivityAutoBrightness() {
        return isActivityAutoBrightness(null);
    }

    /**
     * 判断Activity界面亮度是否是自动的
     *
     * @param activity 界面实例
     * @return 亮度是否是自动的
     */
    public boolean isActivityAutoBrightness(Activity activity) {
        boolean isAuto = false;
        if (activity != null) {
            //判断系统是否是自动的
            isAuto = activity.getWindow().getAttributes().screenBrightness < 0;
            AtlwLogUtil.logUtils.logI(TAG, "获取到当前系统的亮度模式为：" + (isAuto ? "自动" : "手动修改"));
        } else {
            AtlwLogUtil.logUtils.logI(TAG, "当前系统的亮度模式获取失败");
        }
        return isAuto;
    }

    /**
     * 判断是否开启了自动亮度调节
     *
     * @return 是否开启
     */
    public boolean isMobileSystemAutoBrightness() {
        return isMobileSystemAutoBrightness(null);
    }

    /**
     * 判断是否开启了自动亮度调节
     *
     * @param activity 解码实例
     * @return 是否是自动亮度
     */
    public boolean isMobileSystemAutoBrightness(Activity activity) {
        boolean isAuto = false;
        if (activity != null) {
            //判断系统是否是自动的
            try {
                isAuto = Settings.System.getInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) ==
                        Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
                AtlwLogUtil.logUtils.logI(TAG, "获取到当前系统的亮度模式为：" + (isAuto ? "自动" : "手动修改"));
            } catch (Settings.SettingNotFoundException e) {
                AtlwLogUtil.logUtils.logI(TAG, "当前系统的亮度模式获取失败");
            }
        } else {
            AtlwLogUtil.logUtils.logI(TAG, "当前系统的亮度模式获取失败");
        }
        return isAuto;
    }

    /**
     * 保存亮度设置状态
     *
     * @param brightness 亮度
     */
    public void saveBrightnessToMobileSystem(int brightness) {
        saveBrightnessToMobileSystem(null, brightness);
    }

    /**
     * 保存亮度设置状态
     *
     * @param activity   界面实例
     * @param brightness 亮度
     */
    public void saveBrightnessToMobileSystem(Activity activity, int brightness) {
        activity = getActivity(activity);
        if (activity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(activity)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                AtlwLogUtil.logUtils.logI(TAG, "保存亮度设置到手机系统成功");
            } else {
                try {
                    Uri uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
                    Settings.System.putInt(activity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
                    // resolver.registerContentObserver(uri, true, myContentObserver);
                    activity.getContentResolver().notifyChange(uri, null);
                    AtlwLogUtil.logUtils.logI(TAG, "保存亮度设置到手机系统成功");
                } catch (Exception ex) {
                    AtlwLogUtil.logUtils.logI(TAG, "保存亮度设置到手机系统失败");
                }
            }
        } else {
            AtlwLogUtil.logUtils.logI(TAG, "保存亮度设置到手机系统失败");
        }
    }

    /**
     * 设置屏幕亮度跟随系统
     */
    public void setBrightnessFollowMobileSystem() {
        setBrightnessFollowMobileSystem(null);
    }

    /**
     * 设置屏幕亮度跟随系统
     *
     * @param activity 上下文
     */
    public void setBrightnessFollowMobileSystem(Activity activity) {
        activity = getActivity(activity);
        if (activity != null) {
            //WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE使用设个亮度值会使其自动跟随系统
            setBrightness(activity, WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE, true);
        }
    }

    //注册 Brightness 的 uri
    private final Uri BRIGHTNESS_MODE_URI = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE);
    private final Uri BRIGHTNESS_URI = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
    private final Uri BRIGHTNESS_ADJ_URI = Settings.System.getUriFor("screen_auto_brightness_adj");

    /**
     * 亮度观察者集合
     */
    private final Map<Activity, AtlwBrightnessChangeContentObserver> observerMap = new ConcurrentHashMap<>();

    /**
     * 注册亮度观察者
     *
     * @param activity        界面实例
     * @param mBrightObserver 观察者
     */
    public void registerBrightObserver(Activity activity, AtlwBrightnessChangeContentObserver mBrightObserver) {
        unregisterBrightObserver(activity);
        try {
            if (mBrightObserver != null) {
                if (!mBrightObserver.isRegist()) {
                    final ContentResolver cr = activity.getContentResolver();
                    cr.unregisterContentObserver(mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_MODE_URI, false, mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_URI, false, mBrightObserver);
                    cr.registerContentObserver(BRIGHTNESS_ADJ_URI, false, mBrightObserver);
                    mBrightObserver.setRegist(true);
                }
                observerMap.put(activity, mBrightObserver);
            }
        } catch (Throwable throwable) {
            AtlwLogUtil.logUtils.logE(TAG, "注册亮度观察者异常! " + throwable);
        }
    }

    /**
     * 解注册亮度观察者
     *
     * @param activity 页面实例
     */
    public void unregisterBrightObserver(Activity activity) {
        try {
            AtlwBrightnessChangeContentObserver mBrightObserver = observerMap.get(activity);
            if (mBrightObserver != null) {
                if (mBrightObserver.isRegist()) {
                    activity.getContentResolver().unregisterContentObserver(mBrightObserver);
                    mBrightObserver.setRegist(false);
                    observerMap.remove(activity);
                }
            }
        } catch (Throwable throwable) {
            AtlwLogUtil.logUtils.logE(TAG, "解注册亮度观察者异常! " + throwable);
        }
    }

    /**
     * 获取Activity
     *
     * @param activity 要先处理的Activity
     * @return 有不为空的Activity则返回，否则的话返回空
     */
    private Activity getActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            return activity;
        }
        if (optionsActivity != null && !optionsActivity.isFinishing()) {
            return optionsActivity;
        }
        return null;
    }

    /**
     * 获取过滤蓝光后的颜色值
     *
     * @param blueFilterPercent 蓝光过滤比例[10-80]
     * @return 过滤后的值
     */
    @ColorInt
    public int getColor(int blueFilterPercent) {
        int realFilter = blueFilterPercent;
        if (realFilter < 10) {
            realFilter = 10;
        } else if (realFilter > 80) {
            realFilter = 80;
        }
        int a = (int) (realFilter / 80f * 180);
        int r = (int) (200 - (realFilter / 80f) * 190);
        int g = (int) (180 - (realFilter / 80f) * 170);
        int b = (int) (60 - realFilter / 80f * 60);
        return Color.argb(a, r, g, b);
    }

}
