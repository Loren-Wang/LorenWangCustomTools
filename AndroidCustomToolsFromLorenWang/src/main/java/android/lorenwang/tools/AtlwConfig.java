package android.lorenwang.tools;

import android.app.Activity;
import android.app.Application;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.lorenwang.tools.messageTransmit.AtlwFlyMessageUtil;
import android.os.Build;
import android.os.Bundle;

import java.util.Vector;

import androidx.annotation.AnimRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;

import static android.lorenwang.tools.messageTransmit.AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_CREATE;
import static android.lorenwang.tools.messageTransmit.AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_DESTROYED;
import static android.lorenwang.tools.messageTransmit.AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_PAUSED;
import static android.lorenwang.tools.messageTransmit.AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_RESUMED;
import static android.lorenwang.tools.messageTransmit.AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_SAVE_INSTANCE_STATE;
import static android.lorenwang.tools.messageTransmit.AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_START;
import static android.lorenwang.tools.messageTransmit.AtlwFlyMessageMsgTypes.ACTIVITY_LIFECYCLE_CALLBACKS_ON_STOPPED;
/**
 * 功能作用：安卓工具类设置
 * 初始注释时间： 2019/1/29 17:18
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AtlwConfig {
    /**
     * 是否是debug模式
     */
    public static boolean isDebug = false;
    /**
     * 是否注册了生命周期监听
     */
    private static boolean isRegistActivityLifecycleCallback = false;
    /**
     * 设计稿或者屏幕宽度分辨率
     */
    public static int SCREEN_LAYOUT_BASE_WIDTH = 750;
    /**
     * 设计稿或者屏幕高度度分辨率
     */
    public static int SCREEN_LAYOUT_BASE_HEIGHT = 1334;
    /**
     * 图片加载框架类型是fresco
     */
    public static final int IMAGE_LOAD_LIBRARY_TYPE_FRESCO = 0;
    /**
     * 图片加载框架类型是glide
     */
    public static final int IMAGE_LOAD_LIBRARY_TYPE_GLIDE = 1;
    /**
     * 图片加载框架类型是ImageLoad
     */
    public static final int IMAGE_LOAD_LIBRARY_TYPE_IMAGE_LOAD = 2;
    /**
     * 图片加载失败图片
     */
    @DrawableRes
    public static int imageLoadingFailResId = android.R.drawable.stat_sys_warning;
    /**
     * 图片加载加载中图片
     */
    @DrawableRes
    public static int imageLoadingLoadResId = android.R.drawable.ic_popup_sync;
    /**
     * 页面跳转默认进入动画
     */
    @AnimRes
    public static Integer ACTIVITY_JUMP_DEFAULT_ENTER_ANIM = null;
    /**
     * 页面跳转默认退出动画
     */
    @AnimRes
    public static Integer ACTIVITY_JUMP_DEFAULT_EXIT_ANIM = null;
    /**
     * 页面后退跳转默认进入动画
     */
    @AnimRes
    public static Integer ACTIVITY_JUMP_DEFAULT_BACK_ENTER_ANIM = null;
    /**
     * 页面后退跳转默认退出动画
     */
    @AnimRes
    public static Integer ACTIVITY_JUMP_DEFAULT_BACK_EXIT_ANIM = null;
    /**
     * activity实例存储
     */
    public static Vector<Activity> activityCollection = new Vector<>();
    /**
     * 当前APP实例
     */
    public static Application nowApplication;

    /**
     * activity生命周期监听
     */
    private static final Application.ActivityLifecycleCallbacks ACTIVITY_LIFECYCLE_CALLBACKS =
            new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle bundle) {
                    activityCollection.add(activity);
                    AtlwFlyMessageUtil.getInstance().sendMsg(ACTIVITY_LIFECYCLE_CALLBACKS_ON_CREATE,
                            true, activity, bundle);
                }

                @Override
                public void onActivityStarted(Activity activity) {
                    AtlwFlyMessageUtil.getInstance().sendMsg(ACTIVITY_LIFECYCLE_CALLBACKS_ON_START, true
                            , activity);
                }

                @Override
                public void onActivityResumed(Activity activity) {
                    AtlwFlyMessageUtil.getInstance().sendMsg(ACTIVITY_LIFECYCLE_CALLBACKS_ON_RESUMED,
                            true, activity);
                }

                @Override
                public void onActivityPaused(Activity activity) {
                    AtlwFlyMessageUtil.getInstance().sendMsg(ACTIVITY_LIFECYCLE_CALLBACKS_ON_PAUSED,
                            true, activity);
                }

                @Override
                public void onActivityStopped(Activity activity) {
                    AtlwFlyMessageUtil.getInstance().sendMsg(ACTIVITY_LIFECYCLE_CALLBACKS_ON_STOPPED,
                            true, activity);
                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                    AtlwFlyMessageUtil.getInstance().sendMsg(ACTIVITY_LIFECYCLE_CALLBACKS_ON_SAVE_INSTANCE_STATE, true, activity, bundle);
                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    activityCollection.remove(activity);
                    AtlwFlyMessageUtil.getInstance().sendMsg(ACTIVITY_LIFECYCLE_CALLBACKS_ON_DESTROYED,
                            true, activity);
                }
            };

    /**
     * application注册生命周期监听
     *
     * @param application App的全局基础上下文实例
     */
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void registerActivityLifecycleCallbacks(Application application) {
        synchronized (AtlwConfig.class) {
            if (application != null && !isRegistActivityLifecycleCallback) {
                isRegistActivityLifecycleCallback = true;
                application.registerActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE_CALLBACKS);
            }
        }
    }

    /**
     * 返回是否注册了生命周期监听
     *
     * @return 是否注册了监听
     */
    public static boolean isRegisterActivityLifecycleCallback() {
        return isRegistActivityLifecycleCallback;
    }

    /**
     * 初始化安卓自定义工具类
     *
     * @param nowApplication          当前app的application
     * @param isDebug                 是否是debug模式
     * @param debugLogFileDirSavePath debug模式下日志存储地址文件夹
     */
    public static void initAndroidCustomTools(Application nowApplication, boolean isDebug, String debugLogFileDirSavePath) {
        AtlwConfig.nowApplication = nowApplication;
        AtlwConfig.isDebug = isDebug;
        AtlwLogUtil.logUtils = new AtlwLogUtil();
        AtlwLogUtil.logUtils.setShowLog(isDebug);
        if (debugLogFileDirSavePath != null) {
            AtlwLogUtil.logUtils.setLogSaveFileDirPath(debugLogFileDirSavePath);
        }
    }
}
