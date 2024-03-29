package android.lorenwang.tools.app;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.lorenwang.tools.AtlwConfig;
import android.lorenwang.tools.base.AtlwAPKPackageNameList;
import android.lorenwang.tools.base.AtlwCheckUtil;
import android.lorenwang.tools.mobile.AtlwMobilePhoneBrandUtil;
import android.net.Uri;
import android.os.Bundle;

import java.lang.reflect.Method;
import java.util.HashMap;

import androidx.annotation.AnimRes;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtil;

/**
 * 功能作用：activity页面跳转工具类
 * 初始注释时间： 2019/10/28 14:20
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 普通页面跳转--jump(old,cls)
 * 带参数页面跳转--jump(old,cls,bundle)
 * 带参数跳转页面并是否清空栈--jump(old,cls,bundle,clearTop)
 * 无动画跳转页面--jumpNoAnim(old,cls)
 * 带参数无动画跳转页面--jumpNoAnim(old,cls,bundle,clearTop)
 * 后退跳转页面--jumpBack(old,cls)
 * 带参数后退跳转页面--jumpBack(old,cls,bundle)
 * 带参数后退跳转页面并是否清空栈--jumpBack(old,cls,bundle,clearTop)
 * 需要进行返回的跳转--jumpForResult(old,cls)
 * 带请求码跳转并需要返回--jumpForResult(old,cls,requestCode)
 * 带参数跳转并需要返回--jumpForResult(old,cls,bundle)
 * 带参数以及请求码跳转并需要返回--jumpForResult(old,cls,requestCode,bundle)
 * 带参数以及请求码跳转并需要返回并决定是否需要清空栈--jumpForResult(old,cls,requestCode,bundle,clearTop)
 * 无动画需要返回跳转页面--jumpForResultNoAnim(old,cls)
 * 无动画带请求码需要返回的跳转页面--jumpForResultNoAnim(old,cls,requestCode)
 * 带参数请求码无动画跳转页面并决定是否需要清空栈--jumpForResultNoAnim(old,cls,requestCode,bundle,clearTop)
 * 通过地址跳转到网页--jumpToWeb(activity,url)
 * 通用方法-带参数跳转页面并是否清空栈并设置进入退出动画--jump(old,cls,bundle,clearTop, enterAnim, exitAnim)
 * 通用方法-带参数跳转页面并设置flag以及设置进入退出动画--jump(old,cls,bundle,flag,enterAnim,exitAnim)
 * 通用方法-带参数跳转页面并设置flag以及设置进入退出动画以及请求码是否存在--jump(old,cls,bundle,flag,requestCode,enterAnim,exitAnim)
 * 初始化所有Activity的唯一代码--initActivityCode()
 * 返回Activity的唯一代码--getActivityCode(cls)
 * 调用Activity的overridePendingTransition方法--overridePendingTransition(activity,enterAnim,exitAnim)
 * 跳转到应用市场--jumpApplicationMarket(activity,marketPkg,appPkg)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AtlwActivityJumpUtil {
    private final String TAG = getClass().getName();
    private static volatile AtlwActivityJumpUtil optionsInstance;

    private AtlwActivityJumpUtil() {
    }

    public static AtlwActivityJumpUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwActivityJumpUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwActivityJumpUtil();
                }
            }
        }
        return optionsInstance;
    }

    // 以Activity的名称为键，存储Activity的代码
    private final HashMap<String, Integer> mActivityCodeMap = new HashMap<>();
    private final String BUNDLE_LAST_ACTIVITY = "BUNDLE_LAST_ACTIVITY";

    /*--------------------------------------普通页面跳转---------------------------------------*/

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
    public void jump(Context old, Class<?> cls, Bundle bundle, boolean clearTop) {
        jump(old, cls, bundle, clearTop, AtlwConfig.ACTIVITY_JUMP_DEFAULT_ENTER_ANIM, AtlwConfig.ACTIVITY_JUMP_DEFAULT_EXIT_ANIM);
    }

    /*--------------------------------------*无动画跳转******************************************/

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


    /*--------------------------------------后退页面跳转*****************************************/

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
    public void jumpBack(Context old, Class<?> cls, Bundle bundle, boolean clearTop) {
        jump(old, cls, bundle, clearTop, AtlwConfig.ACTIVITY_JUMP_DEFAULT_BACK_ENTER_ANIM, AtlwConfig.ACTIVITY_JUMP_DEFAULT_BACK_EXIT_ANIM);
    }

    /*--------------------------------------带参数返回跳转---------------------------------------*/

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
    public void jumpForResult(Context old, Class<?> cls, Integer requestCode, Bundle bundle) {
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
    public void jumpForResult(Context old, Class<?> cls, Integer requestCode, Bundle bundle, boolean clearTop) {
        jump(old, cls, bundle, clearTop ? Intent.FLAG_ACTIVITY_CLEAR_TOP : null, requestCode, AtlwConfig.ACTIVITY_JUMP_DEFAULT_ENTER_ANIM,
                AtlwConfig.ACTIVITY_JUMP_DEFAULT_EXIT_ANIM);
    }

    /*--------------------------------------无动画需要返回跳转--------------------------------------*/

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
    public void jumpForResultNoAnim(Context old, Class<?> cls, Integer requestCode, Bundle bundle, boolean clearTop) {
        jump(old, cls, bundle, clearTop ? Intent.FLAG_ACTIVITY_CLEAR_TOP : null, requestCode, 0, 0);
    }


    /*--------------------------------------跳转到网页方法****************************************/

    /**
     * 通过地址跳转到网页
     *
     * @param activity 上下文实例
     * @param url      网址
     */
    public void jumpToWeb(Activity activity, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            overridePendingTransition(activity, AtlwConfig.ACTIVITY_JUMP_DEFAULT_ENTER_ANIM, AtlwConfig.ACTIVITY_JUMP_DEFAULT_EXIT_ANIM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*--------------------------------------**通用方法*******************************************/

    /**
     * 通用方法-带参数跳转页面并是否清空栈并设置进入退出动画
     *
     * @param old       当前页面或者旧页面
     * @param cls       要跳转的页面
     * @param bundle    跳转携带的参数
     * @param clearTop  是否在跳转的时候清空栈
     * @param enterAnim 进入新Activity页面动画
     * @param exitAnim  旧的Activity退回动画
     */
    public void jump(Context old, Class<?> cls, Bundle bundle, boolean clearTop, @AnimRes Integer enterAnim, @AnimRes Integer exitAnim) {
        jump(old, cls, bundle, clearTop ? Intent.FLAG_ACTIVITY_CLEAR_TOP : null, enterAnim, exitAnim);
    }

    /**
     * 通用方法-带参数跳转页面并设置flag以及设置进入退出动画
     *
     * @param old       当前页面或者旧页面
     * @param cls       要跳转的页面
     * @param bundle    跳转携带的参数
     * @param flag      页面跳转flag
     * @param enterAnim 进入新Activity页面动画
     * @param exitAnim  旧的Activity退回动画
     */
    public void jump(Context old, Class<?> cls, Bundle bundle, Integer flag, Integer enterAnim, Integer exitAnim) {
        jump(old, cls, bundle, flag, null, enterAnim, exitAnim);
    }

    /**
     * 通用方法-带参数跳转页面并设置flag以及设置进入退出动画以及请求码是否存在
     *
     * @param old       当前页面或者旧页面
     * @param cls       要跳转的页面
     * @param bundle    跳转携带的参数
     * @param flag      页面跳转flag
     * @param enterAnim 进入新Activity页面动画
     * @param exitAnim  旧的Activity退回动画
     */
    public void jump(Context old, Class<?> cls, Bundle bundle, Integer flag, Integer requestCode, Integer enterAnim, Integer exitAnim) {
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


    /*--------------------------------------**其他方法*******************************************/

    /**
     * 初始化所有Activity的唯一代码
     */
    public void initActivityCode() {
        try {
            PackageManager manager = AtlwConfig.nowApplication.getPackageManager();
            PackageInfo info = manager.getPackageInfo(AtlwConfig.nowApplication.getPackageName(), PackageManager.GET_ACTIVITIES);
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
     * @param enterAnim 新的Activity进入动画
     * @param exitAnim  旧的Activity退出动画
     */
    public void overridePendingTransition(Activity activity, Integer enterAnim, Integer exitAnim) {
        try {
            Method method = Activity.class.getMethod("overridePendingTransition", int.class, int.class);
            method.invoke(activity, enterAnim, exitAnim);
        } catch (Exception ignored) {
        }
    }

    /**
     * 跳转到应用市场
     *
     * @param marketPkg 应用市场包名
     * @param appPkg    要查找的App包名
     */
    public void jumpApplicationMarket(Activity activity, String marketPkg, String appPkg) {
        if (JtlwCheckVariateUtil.getInstance().isEmpty(appPkg)) {
            return;
        }
        //构造uri
        Uri uri = Uri.parse("market://details?id=" + appPkg);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //如果有传应用市场包则指定包，否则自动选择
        if (JtlwCheckVariateUtil.getInstance().isEmpty(marketPkg)) {
            if (AtlwMobilePhoneBrandUtil.getInstance().isXiaoMiMobile() && AtlwCheckUtil.getInstance().checkAppIsInstall(
                    AtlwAPKPackageNameList.MARKET_XIAO_MI)) {
                intent.setPackage(AtlwAPKPackageNameList.MARKET_XIAO_MI);
            } else if (AtlwMobilePhoneBrandUtil.getInstance().isVivoMobile() && AtlwCheckUtil.getInstance().checkAppIsInstall(
                    AtlwAPKPackageNameList.MARKET_VIVO)) {
                intent.setPackage(AtlwAPKPackageNameList.MARKET_VIVO);
            } else if (AtlwMobilePhoneBrandUtil.getInstance().isCoolpadMobile() && AtlwCheckUtil.getInstance().checkAppIsInstall(
                    AtlwAPKPackageNameList.MARKET_COOLPAD)) {
                intent.setPackage(AtlwAPKPackageNameList.MARKET_COOLPAD);
            } else if (AtlwMobilePhoneBrandUtil.getInstance().isHuaWeiMobile() && AtlwCheckUtil.getInstance().checkAppIsInstall(
                    AtlwAPKPackageNameList.MARKET_HUA_WEI)) {
                intent.setPackage(AtlwAPKPackageNameList.MARKET_HUA_WEI);
            } else if (AtlwMobilePhoneBrandUtil.getInstance().isMeiZuMobile() && AtlwCheckUtil.getInstance().checkAppIsInstall(
                    AtlwAPKPackageNameList.MARKET_FLY_ME)) {
                intent.setPackage(AtlwAPKPackageNameList.MARKET_FLY_ME);
            } else if (AtlwMobilePhoneBrandUtil.getInstance().isOPPOMobile() && AtlwCheckUtil.getInstance().checkAppIsInstall(
                    AtlwAPKPackageNameList.MARKET_OPPO)) {
                intent.setPackage(AtlwAPKPackageNameList.MARKET_OPPO);
            } else if (AtlwMobilePhoneBrandUtil.getInstance().isSamsungMobile() && AtlwCheckUtil.getInstance().checkAppIsInstall(
                    AtlwAPKPackageNameList.MARKET_SAMSUNG)) {
                intent.setPackage(AtlwAPKPackageNameList.MARKET_SAMSUNG);
            } else {
                if (AtlwCheckUtil.getInstance().checkAppIsInstall(AtlwAPKPackageNameList.MARKET_APPLICATION_OF_TREASURE)) {
                    intent.setPackage(AtlwAPKPackageNameList.MARKET_APPLICATION_OF_TREASURE);
                } else if (AtlwCheckUtil.getInstance().checkAppIsInstall(AtlwAPKPackageNameList.MARKET_BAIDU)) {
                    intent.setPackage(AtlwAPKPackageNameList.MARKET_BAIDU);
                }
            }
        } else {
            intent.setPackage(marketPkg);
        }
        activity.startActivity(intent);
    }

}
