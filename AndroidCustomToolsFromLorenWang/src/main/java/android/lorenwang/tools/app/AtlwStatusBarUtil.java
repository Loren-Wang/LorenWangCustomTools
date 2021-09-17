package android.lorenwang.tools.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.lorenwang.tools.mobile.AtlwMobilePhoneBrandUtil;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
/**
 * 功能作用：状态栏操作工具类
 * 初始注释时间： 2021/9/17 10:50
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 修改状态栏为全透明--transparencyBar(activity)
 * 修改状态栏颜色，支持4.4以上版本--setStatusBarColor(activity,color)
 * 当状态栏背景为亮色的时候需要把状态栏图标以及文字改成黑色--setStatusBarLightMode(activity,isFullscreen)
 * 当状态栏背景为亮色的时候需要把状态栏图标以及文字改成黑色--setStatusBarDarkMode(activity,isFullscreen)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AtlwStatusBarUtil {
    private final String TAG = getClass().getName();
    private static volatile AtlwStatusBarUtil optionsInstance;

    private AtlwStatusBarUtil() {
    }

    public static AtlwStatusBarUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwStatusBarUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwStatusBarUtil();
                }
            }
        }
        return optionsInstance;
    }

    /**
     * 修改状态栏为全透明,参考链接 https://www.jianshu.com/p/7f5a9969be53
     *
     * @param activity 页面实例
     */
    @TargetApi(19)
    public void transparencyBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本,参考链接 https://www.jianshu.com/p/7f5a9969be53
     *
     * @param activity 页面实例
     * @param color    颜色值
     */
    public void setStatusBarColor(Activity activity, int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使用SystemBarTint库使4.4版本状态栏变色，需要先将状态栏设置为透明
            transparencyBar(activity);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(color);
        }
    }

    /**
     * 当状态栏背景为亮色的时候需要把状态栏图标以及文字改成黑色
     *
     * @param activity     页面实例
     * @param isFullscreen 是否全屏显示
     */
    public void setStatusBarLightMode(Activity activity, boolean isFullscreen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (AtlwMobilePhoneBrandUtil.getInstance().isXiaoMiMobile()) {
                setStatusBarLightModeForXiaoMi(activity, true);
            } else if (AtlwMobilePhoneBrandUtil.getInstance().isMeiZuMobile()) {
                setStatusBarLightModeForMeiZu(activity, true);
            }
        }
        try {
            if (isFullscreen) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } catch (Exception ignore) {

        }
    }

    /**
     * 当状态栏背景为亮色的时候需要把状态栏图标以及文字改成黑色
     *
     * @param activity     页面实例
     * @param isFullscreen 是否全屏显示
     */
    public void setStatusBarDarkMode(Activity activity, boolean isFullscreen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (AtlwMobilePhoneBrandUtil.getInstance().isXiaoMiMobile()) {
                setStatusBarLightModeForXiaoMi(activity, false);
            } else if (AtlwMobilePhoneBrandUtil.getInstance().isMeiZuMobile()) {
                setStatusBarLightModeForMeiZu(activity, false);
            }
        }
        try {
            if (isFullscreen) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_VISIBLE);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        } catch (Exception ignore) {

        }
    }


    /**
     * 需要MIUIV6以上,参考链接 https://www.jianshu.com/p/7f5a9969be53
     *
     * @param activity 页面实例
     * @param dark     是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private void setStatusBarLightModeForXiaoMi(Activity activity, boolean dark) {
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    //状态栏透明且黑色字体
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            } catch (Exception ignore) {

            }
        }
    }

    /*-----------------------------------------魅族设置-----------------------------------------*/

    /**
     * 设置状态栏字体图标颜色
     *
     * @param activity 当前activity
     * @param dark     是否深色 true为深色 false 为白色
     */
    private void setStatusBarLightModeForMeiZu(Activity activity, boolean dark) {
        setStatusBarLightModeForMeiZu(activity, dark, true);
    }

    /**
     * 设置魅族状态栏颜色
     *
     * @param activity 页面实例
     * @param dark     是否是页面模式
     * @param flag     icon
     */
    private void setStatusBarLightModeForMeiZu(Activity activity, boolean dark, boolean flag) {
        try {
            Method mSetStatusBarDarkIcon = Activity.class.getMethod("setStatusBarDarkIcon",
                    boolean.class);
            mSetStatusBarDarkIcon.invoke(activity, dark);
            AtlwLogUtil.logUtils.logD(TAG, "Set StatusBar success for MeiZu");
        } catch (Exception e) {
            AtlwLogUtil.logUtils.logD(TAG, "Set StatusBar fail for MeiZu");
            if (flag) {
                setStatusBarDarkIcon(activity.getWindow(), dark);
            }
        }
    }

    /**
     * 设置状态栏字体图标颜色(只限全屏非activity情况)
     *
     * @param window 当前窗口
     * @param dark   是否深色 true为深色 false 为白色
     */
    private static void setStatusBarDarkIcon(Window window, boolean dark) {
        if (Build.VERSION.SDK_INT < 23) {
            changeMeizuFlag(window.getAttributes(), "MEIZU_FLAG_DARK_STATUS_BAR_ICON", dark);
        } else {
            View decorView = window.getDecorView();
            if (decorView != null) {
                setStatusBarDarkIcon(decorView, dark);
                setStatusBarColor(window, 0);
            }
        }
    }

    /**
     * 改变魅族标签
     *
     * @param winParams 页面参数
     * @param flagName  名称
     * @param on        内外
     * @return 是否成功
     */
    private static boolean changeMeizuFlag(WindowManager.LayoutParams winParams, String flagName,
                                           boolean on) {
        try {
            Field f = winParams.getClass().getDeclaredField(flagName);
            f.setAccessible(true);
            int bits = f.getInt(winParams);
            Field f2 = winParams.getClass().getDeclaredField("meizuFlags");
            f2.setAccessible(true);
            int meizuFlags = f2.getInt(winParams);
            int oldFlags = meizuFlags;
            if (on) {
                meizuFlags |= bits;
            } else {
                meizuFlags &= ~bits;
            }
            if (oldFlags != meizuFlags) {
                f2.setInt(winParams, meizuFlags);
                return true;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置状态栏颜色
     *
     * @param window 窗口
     * @param color  颜色值
     */
    private static void setStatusBarColor(Window window, int color) {
        WindowManager.LayoutParams winParams = window.getAttributes();
        try {
            Field mStatusBarColorFiled = WindowManager.LayoutParams.class.getField(
                    "statusBarColor");
            if (mStatusBarColorFiled != null) {
                int oldColor = mStatusBarColorFiled.getInt(winParams);
                if (oldColor != color) {
                    mStatusBarColorFiled.set(winParams, color);
                    window.setAttributes(winParams);
                }
            }
        } catch (Exception ignore) {
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @param view 视图
     * @param dark 是否是页面模式
     */
    private static void setStatusBarDarkIcon(View view, boolean dark) {
        int oldVis = view.getSystemUiVisibility();
        int newVis = oldVis;
        Field field;
        try {
            field = View.class.getField("SYSTEM_UI_FLAG_LIGHT_STATUS_BAR");
            int SYSTEM_UI_FLAG_LIGHT_STATUS_BAR = field.getInt(null);
            if (dark) {
                newVis |= SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                newVis &= ~SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            if (newVis != oldVis) {
                view.setSystemUiVisibility(newVis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
