package android.lorenwang.tools.app;

import android.content.Context;
import android.lorenwang.tools.AtlwSetting;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

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
 * <p>
 * <p>
 * Log.d("h_bl", "屏幕宽度（像素）：" + width);
 * Log.d("h_bl", "屏幕高度（像素）：" + height);
 * Log.d("h_bl", "屏幕密度（0.75 / 1.0 / 1.5）：" + density);
 * Log.d("h_bl", "屏幕密度dpi（120 / 160 / 240）：" + densityDpi);
 * Log.d("h_bl", "屏幕宽度（dp）：" + screenWidth);
 * Log.d("h_bl", "屏幕高度（dp）：" + screenHeight);
 */

public class AtlwScreenUtils {
    private final String TAG = "AtlwScreenUtils";
    private static AtlwScreenUtils utils;

    private AtlwScreenUtils() {
    }

    public static AtlwScreenUtils getInstance() {
        synchronized (utils) {
            if (utils == null) {
                utils = new AtlwScreenUtils();
            }
        }
        return utils;
    }

    //屏幕宽度
    private Integer screenWidth = null;
    //屏幕高度
    private Integer screenHeight = null;


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @return
     */
    public float dip2px(Context context, float dpValue) {
        if (JtlwCheckVariateUtils.getInstance().isEmpty(context)) {
            new NullPointerException("Context is not null");
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale + 0.5f;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @return
     */
    public float px2dip(Context context, float pxValue) {
        if (JtlwCheckVariateUtils.getInstance().isEmpty(context)) {
            new NullPointerException("Context is not null");
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return pxValue / scale + 0.5f;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public float sp2px(Context context, float spValue) {
        if (JtlwCheckVariateUtils.getInstance().isEmpty(context)) {
            new NullPointerException("Context is not null");
        }
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return spValue * fontScale + 0.5f;
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public float px2sp(Context context, float pxValue) {
        if (JtlwCheckVariateUtils.getInstance().isEmpty(context)) {
            new NullPointerException("Context is not null");
        }
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return pxValue / fontScale + 0.5f;
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public int getScreenWidth(Context context) {
        if (JtlwCheckVariateUtils.getInstance().isEmpty(context)) {
            new NullPointerException("Context is not null");
        }
        if (screenWidth == null) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            screenWidth = dm.widthPixels;         // 屏幕宽度（像素）
        }
        return screenWidth;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public int getScreenHeight(Context context) {
        if (JtlwCheckVariateUtils.getInstance().isEmpty(context)) {
            new NullPointerException("Context is not null");
        }
        if (screenHeight == null) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            screenHeight = dm.heightPixels;         // 屏幕宽度（像素）
        }
        return screenHeight;
    }

    /**
     * 根据宽度获取在屏幕上显示的总的像素值
     *
     * @param context         上下文
     * @param layoutShowValue 标注图或设计稿上的要显示的控件或其他的尺寸值
     * @return 要显示的像素值
     */
    public int getShowPixelValueForWidth(Context context, int layoutShowValue) {
        return (int) (getScreenWidth(context) * (layoutShowValue * 1.0 / AtlwSetting.SCREEN_LAYOUT_BASE_WIDTH));
    }

    /**
     * 根据高度获取在屏幕上显示的总的像素值
     *
     * @param context         上下文
     * @param layoutShowValue 标注图或设计稿上的要显示的控件或其他的尺寸值
     * @return 要显示的像素值
     */
    public int getShowPixelValueForHeight(Context context, int layoutShowValue) {
        return (int) (getScreenHeight(context) * (layoutShowValue * 1.0 / AtlwSetting.SCREEN_LAYOUT_BASE_HEIGHT));
    }
}
