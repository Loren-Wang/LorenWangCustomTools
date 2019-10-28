package android.lorenwang.tools.app;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.lorenwang.tools.AtlwSetting;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.AnimRes;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 功能作用：activity页面跳转工具类
 * 初始注释时间： 2019/10/28 0028 下午 21:51:35
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 1.1、普通页面跳转
 * 1.2、带参数页面跳转
 * 1.3、带参数跳转页面并是否清空栈
 * 2.1、无动画跳转页面
 * 2.2、带参数无动画跳转页面
 * 3.1、后退跳转页面
 * 3.2、带参数后退跳转页面
 * 3.3、带参数后退跳转页面并是否清空栈
 * 4.1、需要进行返回的跳转
 * 4.2、带请求码跳转并需要返回
 * 4.3、带参数跳转并需要返回
 * 4.4、带参数以及请求码跳转并需要返回
 * 4.5、带参数以及请求码跳转并需要返回并决定是否需要清空栈
 * 5.1、无动画需要返回跳转页面
 * 5.2、无动画带请求码需要返回的跳转页面
 * 5.3、带参数请求码无动画跳转页面并决定是否需要清空栈
 * 6.1、通用方法-带参数跳转页面并是否清空栈并设置进入退出动画
 * 6.2、通用方法-带参数跳转页面并设置flag以及设置进入退出动画
 * 6.3、通用方法-带参数跳转页面并设置flag以及设置进入退出动画以及请求码是否存在
 * 7.1、初始化所有Activity的唯一代码
 * 7.2、返回Activity的唯一代码
 * 7.3、调用Activity的overridePendingTransition方法
 * 8.1、通过地址跳转到网页
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class AtlwActivityJumpUtils {
    private AtlwActivityJumpUtils() {
    }

    private final String TAG = "AtlwActivityJumpUtils";
    private static volatile AtlwActivityJumpUtils instanceUtils;

    public static AtlwActivityJumpUtils getInstance() {
        if (instanceUtils == null) {
            synchronized (AtlwActivityJumpUtils.class) {
                if (instanceUtils == null) {
                    instanceUtils = new AtlwActivityJumpUtils();
                }
            }
        }
        return instanceUtils;
    }

    // 以Activity的名称为键，存储Activity的代码
    private final HashMap<String, Integer> mActivityCodeMap = new HashMap<String, Integer>();
    private final String BUNDLE_LAST_ACTIVITY = "BUNDLE_LAST_ACTIVITY";

    /******************************************普通页面跳转*****************************************/

    /**
     * 普通页面跳转
     *
     * @param old 当前页面或者旧页面
     * @param cls 要跳转的页面
     */
    public void jump(Context old, Class<?> cls) {
        jump(old, cls, new Bundle());
    }

    /**
     * 带参数页面跳转
     *
     * @param old    当前页面或者旧页面
     * @param cls    要跳转的页面
     * @param bundle 跳转携带的参数
     */
    public void jump(Context old, Class<?> cls, Bundle bundle) {
        jump(old, cls, bundle, false);
    }

    /**
     * 带参数跳转页面并是否清空栈
     *
     * @param old      当前页面或者旧页面
     * @param cls      要跳转的页面
     * @param bundle   跳转携带的参数
     * @param clearTop 是否在跳转的时候清空栈
     */
    public void jump(Context old, Class<?> cls, Bundle bundle,
                     boolean clearTop) {
        jump(old, cls, bundle, clearTop,
                AtlwSetting.ACTIVITY_JUMP_DEFAULT_ENTER_ANIM,
                AtlwSetting.ACTIVITY_JUMP_DEFAULT_EXIT_ANIM);
    }

    /*******************************************无动画跳转******************************************/

    /**
     * 无动画跳转页面
     *
     * @param old 当前页面或者旧页面
     * @param cls 要跳转的页面
     */
    public void jumpNoAnim(Context old, Class<?> cls) {
        jumpNoAnim(old, cls, new Bundle(), false);
    }

    /**
     * 带参数无动画跳转页面
     *
     * @param old      当前页面或者旧页面
     * @param cls      要跳转的页面
     * @param bundle   跳转携带的参数
     * @param clearTop 是否在跳转的时候清空栈
     */
    public void jumpNoAnim(Context old, Class<?> cls, Bundle bundle, boolean clearTop) {
        jump(old, cls, bundle, clearTop, 0, 0);
    }


    /******************************************后退页面跳转*****************************************/

    /**
     * 后退跳转页面
     *
     * @param old 当前页面或者旧页面
     * @param cls 要跳转的页面
     */
    public void jumpBack(Context old, Class<?> cls) {
        jumpBack(old, cls, new Bundle());
    }

    /**
     * 带参数后退跳转页面
     *
     * @param old    当前页面或者旧页面
     * @param cls    要跳转的页面
     * @param bundle 跳转携带的参数
     */
    public void jumpBack(Context old, Class<?> cls, Bundle bundle) {
        jumpBack(old, cls, bundle, false);
    }

    /**
     * 带参数后退跳转页面并是否清空栈
     *
     * @param old      当前页面或者旧页面
     * @param cls      要跳转的页面
     * @param bundle   跳转携带的参数
     * @param clearTop 是否在跳转的时候清空栈
     */
    public void jumpBack(Context old, Class<?> cls, Bundle bundle,
                         boolean clearTop) {
        jump(old, cls, bundle, clearTop,
                AtlwSetting.ACTIVITY_JUMP_DEFAULT_BACK_ENTER_ANIM,
                AtlwSetting.ACTIVITY_JUMP_DEFAULT_BACK_EXIT_ANIM);
    }

    /*****************************************带参数返回跳转****************************************/

    /**
     * 需要进行返回的跳转
     *
     * @param old 当前页面或者旧页面
     * @param cls 要跳转的页面
     */
    public void jumpForResult(Context old, Class<?> cls) {
        jumpForResult(old, cls, getActivityCode(old.getClass()), new Bundle());
    }

    /**
     * 带请求码跳转并需要返回
     *
     * @param old         当前页面或者旧页面
     * @param cls         要跳转的页面
     * @param requestCode 请求码
     */
    public void jumpForResult(Context old, Class<?> cls, Integer requestCode) {
        jumpForResult(old, cls, requestCode, new Bundle());
    }

    /**
     * 带参数跳转并需要返回
     *
     * @param old    当前页面或者旧页面
     * @param cls    要跳转的页面
     * @param bundle 携带的参数
     */
    public void jumpForResult(Context old, Class<?> cls, Bundle bundle) {
        jumpForResult(old, cls, getActivityCode(old.getClass()), bundle);
    }

    /**
     * 带参数以及请求码跳转并需要返回
     *
     * @param old         当前页面或者旧页面
     * @param cls         要跳转的页面
     * @param requestCode 请求码
     * @param bundle      携带的参数
     */
    public void jumpForResult(Context old, Class<?> cls,
                              Integer requestCode, Bundle bundle) {
        jumpForResult(old, cls, requestCode, bundle, false);
    }

    /**
     * 带参数以及请求码跳转并需要返回并决定是否需要清空栈
     *
     * @param old         当前页面或者旧页面
     * @param cls         要跳转的页面
     * @param requestCode 请求码
     * @param bundle      携带的参数
     * @param clearTop    是否要清空栈
     */
    public void jumpForResult(Context old, Class<?> cls,
                              Integer requestCode, Bundle bundle, boolean clearTop) {
        jump(old, cls, bundle, clearTop ? Intent.FLAG_ACTIVITY_CLEAR_TOP : null, requestCode,
                AtlwSetting.ACTIVITY_JUMP_DEFAULT_ENTER_ANIM,
                AtlwSetting.ACTIVITY_JUMP_DEFAULT_EXIT_ANIM);
    }

    /***************************************无动画需要返回跳转**************************************/

    /**
     * 无动画需要返回跳转页面
     *
     * @param old 当前页面或者旧页面
     * @param cls 要跳转的页面
     */
    public void jumpForResultNoAnim(Context old, Class<?> cls) {
        jumpForResultNoAnim(old, cls, getActivityCode(old.getClass()));
    }

    /**
     * 无动画带请求码需要返回的跳转页面
     *
     * @param old         当前页面或者旧页面
     * @param cls         要跳转的页面
     * @param requestCode 请求码
     */
    public void jumpForResultNoAnim(Context old, Class<?> cls, Integer requestCode) {
        jumpForResultNoAnim(old, cls, requestCode, new Bundle(), false);
    }

    /**
     * 带参数请求码无动画跳转页面并决定是否需要清空栈
     *
     * @param old      当前页面或者旧页面
     * @param cls      要跳转的页面
     * @param bundle   跳转携带的参数
     * @param clearTop 是否在跳转的时候清空栈
     */
    public void jumpForResultNoAnim(Context old, Class<?> cls,
                                    Integer requestCode, Bundle bundle, boolean clearTop) {
        jump(old, cls, bundle, clearTop ? Intent.FLAG_ACTIVITY_CLEAR_TOP : null, requestCode, 0, 0);
    }


    /*****************************************跳转到网页方法****************************************/

    /**
     * 通过地址跳转到网页
     *
     * @param activity 上下文实例
     * @param url      网址
     */
    public void jumpToWeb(Activity activity, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);
            overridePendingTransition(activity,
                    AtlwSetting.ACTIVITY_JUMP_DEFAULT_ENTER_ANIM,
                    AtlwSetting.ACTIVITY_JUMP_DEFAULT_EXIT_ANIM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过地址跳转到网页
     *
     * @param url 网址
     */
    public void jumpToWeb(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (AtlwSetting.nowApplication != null) {
                AtlwSetting.nowApplication.startActivity(intent);
            }
        } catch (Exception e) {
        }
    }


    /********************************************通用方法*******************************************/

    /**
     * 通用方法-带参数跳转页面并是否清空栈并设置进入退出动画
     *
     * @param old       当前页面或者旧页面
     * @param cls       要跳转的页面
     * @param bundle    跳转携带的参数
     * @param clearTop  是否在跳转的时候清空栈
     * @param enterAnim 进入新页面动画
     * @param exitAnim  从新页面退回动画
     */
    public void jump(Context old, Class<?> cls, Bundle bundle,
                     boolean clearTop, @AnimRes Integer enterAnim, @AnimRes Integer exitAnim) {
        jump(old, cls, bundle, clearTop ? Intent.FLAG_ACTIVITY_CLEAR_TOP : null, enterAnim, exitAnim);
    }

    /**
     * 通用方法-带参数跳转页面并设置flag以及设置进入退出动画
     *
     * @param old       当前页面或者旧页面
     * @param cls       要跳转的页面
     * @param bundle    跳转携带的参数
     * @param flag      页面跳转flag
     * @param enterAnim 进入新页面动画
     * @param exitAnim  从新页面退回动画
     */
    public void jump(Context old, Class<?> cls, Bundle bundle, Integer flag,
                     Integer enterAnim, Integer exitAnim) {
        jump(old, cls, bundle, flag, null, enterAnim, exitAnim);
    }

    /**
     * 通用方法-带参数跳转页面并设置flag以及设置进入退出动画以及请求码是否存在
     *
     * @param old       当前页面或者旧页面
     * @param cls       要跳转的页面
     * @param bundle    跳转携带的参数
     * @param flag      页面跳转flag
     * @param enterAnim 进入新页面动画
     * @param exitAnim  从新页面退回动画
     */
    public void jump(Context old, Class<?> cls, Bundle bundle, Integer flag, Integer requestCode,
                     Integer enterAnim, Integer exitAnim) {
        try {
            Intent intent = new Intent(old, cls);
            //设置参数
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            //设置携带参数中的唯一码
            if (old instanceof Activity) {
                Activity activity = (Activity) old;
                intent.putExtra(BUNDLE_LAST_ACTIVITY, getActivityCode(activity.getClass()));
            }
            //设置跳转flag
            if (flag != null) {
                intent.setFlags(flag);
            }
            //判断请求码是否为空以及请求上下文是否是activity
            if (requestCode != null && old instanceof Activity) {
                ((Activity) old).startActivityForResult(intent, requestCode);
            } else {
                old.startActivity(intent);
            }
            if (old instanceof Activity) {
                Activity activity = (Activity) old;
                overridePendingTransition(activity, enterAnim, exitAnim);
            }
        } catch (Exception ignored) {
        }
    }


    /********************************************其他方法*******************************************/

    /**
     * 初始化所有Activity的唯一代码
     *
     * @param context 上下文实例
     */
    public void initActivityCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (info.activities != null && info.activities.length > 0) {
                for (ActivityInfo actInfo : info.activities) {
                    mActivityCodeMap.put(actInfo.name, actInfo.name.hashCode());
                }
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * 返回Activity的唯一代码
     *
     * @param cls 类
     * @return Activity的唯一代码
     */
    public Integer getActivityCode(Class<?> cls) {
        try {
            if (!mActivityCodeMap.containsKey(cls.getName())) {
                return -1;
            }
            return mActivityCodeMap.get(cls.getName());
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 调用Activity的overridePendingTransition方法
     *
     * @param activity  activity实例
     * @param enterAnim 进入动画
     * @param exitAnim  退出动画
     */
    public void overridePendingTransition(Activity activity, Integer enterAnim, Integer exitAnim) {
        try {
            Method method = Activity.class.getMethod(
                    "overridePendingTransition", int.class, int.class);
            method.invoke(activity, enterAnim, exitAnim);
        } catch (Exception ignored) {
        }
    }

}