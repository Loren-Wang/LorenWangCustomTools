package android.lorenwang.tools.app;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 创建时间：2019-04-30 下午 15:47:15
 * 创建人：王亮（Loren wang）
 * 功能作用：控件相关工具类
 * 思路：
 * 方法：1、获取控件的LayoutParams
 * 2、设置控件的宽高
 * 3、设置控件宽高以及margin属性
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AtlwViewUtils {
    private final String TAG = "AtlwViewUtils";
    private static AtlwViewUtils utils;

    private AtlwViewUtils() {
    }

    public static AtlwViewUtils getInstance() {
        synchronized (AtlwViewUtils.class) {
            if (utils == null) {
                utils = new AtlwViewUtils();
            }
        }
        return utils;
    }

    /**
     * 获取控件的LayoutParams
     *
     * @param view   要设置的控件
     * @param width  控件显示的宽度
     * @param height 控件显示的高度
     * @return 控件的LayoutParams
     */
    public ViewGroup.LayoutParams getViewLayoutParams(View view, int width, int height) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams == null) {
                if (layoutParams instanceof LinearLayout.LayoutParams) {
                    layoutParams = new LinearLayout.LayoutParams(width, height);
                } else if (layoutParams instanceof FrameLayout.LayoutParams) {
                    layoutParams = new FrameLayout.LayoutParams(width, height);
                } else if (layoutParams instanceof RelativeLayout.LayoutParams) {
                    layoutParams = new RelativeLayout.LayoutParams(width, height);
                }
            }
            return layoutParams;
        } else {
            return null;
        }
    }

    /**
     * 设置控件的宽高
     *
     * @param view   要设置的控件
     * @param width  控件显示的宽度
     * @param height 控件显示的高度
     */
    public void setViewWidthHeight(View view, int width, int height) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = getViewLayoutParams(view, width, height);
            if (layoutParams != null) {
                layoutParams.width = width;
                layoutParams.height = height;
                view.setLayoutParams(layoutParams);
            }
        }
    }

    /**
     * 设置控件宽高以及margin属性
     *
     * @param view   要设置的控件
     * @param width  控件显示的宽度
     * @param height 控件显示的高度
     * @param left   左外边距
     * @param top    上外边距
     * @param right  右外边距
     * @param bottom 下外边距
     */
    public void setViewWidthHeightMargin(View view, int width, int height, Integer left, Integer top, Integer right, Integer bottom) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = getViewLayoutParams(view, width, height);
            if (layoutParams != null) {
                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layoutParams;
                    params.setMargins(left == null ? params.leftMargin : left
                            , top == null ? params.topMargin : top
                            , right == null ? params.rightMargin : right
                            , bottom == null ? params.bottomMargin : bottom);
                    params.width = width;
                    params.height = height;
                    view.setLayoutParams(params);
                } else {
                    layoutParams.width = width;
                    layoutParams.height = height;
                    view.setLayoutParams(layoutParams);
                }
            }
        }
    }
}
