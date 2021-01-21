package android.lorenwang.tools.app;

import android.content.Context;
import android.lorenwang.tools.AtlwConfig;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 创建时间：2019-04-30 下午 14:36:58
 * 创建人：王亮（Loren wang）
 * 功能作用：屏幕相关工具类
 * 思路：
 * 方法：
 * 1、根据手机的分辨率从 dp 的单位 转成为 px(像素)
 * 2、根据手机的分辨率从 px(像素) 的单位 转成为 dp
 * 3、将sp值转换为px值，保证文字大小不变
 * 4、将px值转换为sp值，保证文字大小不变
 * 5、获取屏幕宽度
 * 6、获取屏幕高度
 * 7、根据宽度获取在屏幕上显示的总的像素值
 * 8、根据高度获取在屏幕上显示的总的像素值
 * 9、或者状态栏高度
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
 * DisplayMetrics dm = new DisplayMetrics();
 * wm.getDefaultDisplay().getMetrics(dm);
 * int width = dm.widthPixels;         // 屏幕宽度（像素）
 * int height = dm.heightPixels;       // 屏幕高度（像素）
 * float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
 * int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
 * // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
 * int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
 * int screenHeight = (int) (height / density);// 屏幕高度(dp)
 * Log.d("h_bl", "屏幕宽度（像素）：" + width);
 * Log.d("h_bl", "屏幕高度（像素）：" + height);
 * Log.d("h_bl", "屏幕密度（0.75 / 1.0 / 1.5）：" + density);
 * Log.d("h_bl", "屏幕密度dpi（120 / 160 / 240）：" + densityDpi);
 * Log.d("h_bl", "屏幕宽度（dp）：" + screenWidth);
 * Log.d("h_bl", "屏幕高度（dp）：" + screenHeight);
 */

public class AtlwScreenUtil {
    private final String TAG = getClass().getName();
    private static volatile AtlwScreenUtil optionsInstance;

    private AtlwScreenUtil() {
    }

    public static AtlwScreenUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwScreenUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwScreenUtil();
                }
            }
        }
        return optionsInstance;
    }

    //屏幕宽度
    private Integer screenWidth = null;
    //屏幕高度
    private Integer screenHeight = null;


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dpValue dp值
     * @return px像素值
     */
    public float dip2px(float dpValue) {
        final float scale = getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param pxValue px值
     * @return dp值
     */
    public float px2dip(float pxValue) {
        final float scale = getDisplayMetrics().density;
        return pxValue / scale + 0.5f;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue sp值
     * @return px值
     */
    public float sp2px(float spValue) {
        final float fontScale = getDisplayMetrics().scaledDensity;
        return spValue * fontScale + 0.5f;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue px像素值
     * @return sp值
     */
    public float px2sp(float pxValue) {
        final float fontScale = getDisplayMetrics().scaledDensity;
        return pxValue / fontScale + 0.5f;
    }

    /**
     * 获取屏幕宽度
     *
     * @return 屏幕宽度
     */
    public int getScreenWidth() {
        if (screenWidth == null) {
            screenWidth = getDisplayMetrics().widthPixels;         // 屏幕宽度（像素）
        }
        return screenWidth;
    }

    /**
     * 获取屏幕高度
     *
     * @return 屏幕高度
     */
    public int getScreenHeight() {
        if (screenHeight == null) {
            screenHeight = getDisplayMetrics().heightPixels;         // 屏幕宽度（像素）
        }
        return screenHeight;
    }

    /**
     * 或者状态栏高度
     *
     * @return 获取到返回指定值，无法获取返回0
     */
    public int getStatusBarHeight() {
        int resourceId = AtlwConfig.nowApplication.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return AtlwConfig.nowApplication.getResources().getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    /**
     * 根据宽度获取在屏幕上显示的总的像素值
     *
     * @param layoutShowValue 标注图或设计稿上的要显示的控件或其他的尺寸值
     * @return 要显示的像素值
     */
    public int getShowPixelValueForWidth(int layoutShowValue) {
        return (int) (getScreenWidth() * (layoutShowValue * 1.0 / AtlwConfig.SCREEN_LAYOUT_BASE_WIDTH));
    }

    /**
     * 根据高度获取在屏幕上显示的总的像素值
     *
     * @param layoutShowValue 标注图或设计稿上的要显示的控件或其他的尺寸值
     * @return 要显示的像素值
     */
    public int getShowPixelValueForHeight(int layoutShowValue) {
        return (int) (getScreenHeight() * (layoutShowValue * 1.0 / AtlwConfig.SCREEN_LAYOUT_BASE_HEIGHT));
    }

    /**
     * 获取手机的屏幕管理
     * @return 手机的屏幕管理
     */
    private DisplayMetrics getDisplayMetrics() {
        WindowManager wm = (WindowManager) AtlwConfig.nowApplication.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }
}
