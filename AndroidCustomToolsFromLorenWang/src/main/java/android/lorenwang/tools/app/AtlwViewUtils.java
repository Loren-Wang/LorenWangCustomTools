package android.lorenwang.tools.app;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.constraintlayout.widget.ConstraintLayout;

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
    private static volatile AtlwViewUtils utils;

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
    public ViewGroup.LayoutParams getViewLayoutParams(View view, Integer width, Integer height) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams == null) {
                if (view.getParent() != null) {
                    if (width == null) {
                        width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    }
                    if (height == null) {
                        height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    }
                    try {
                        //通过反射获取父级的素有属性，然后取出父类中定义的静态params类型然后转换并设置给子view
                        Class<?>[] classes = view.getParent().getClass().getClasses();
                        for (Class<?> item : classes) {
                            if (ViewGroup.LayoutParams.class.isAssignableFrom(item)) {
                                layoutParams = (ViewGroup.LayoutParams) item.getDeclaredConstructor(int.class, int.class).newInstance(width, height);
                                break;
                            }
                        }
                    } catch (Exception e) {
                        if (layoutParams instanceof LinearLayout.LayoutParams) {
                            layoutParams = new LinearLayout.LayoutParams(width, height);
                        } else if (layoutParams instanceof FrameLayout.LayoutParams) {
                            layoutParams = new FrameLayout.LayoutParams(width, height);
                        } else if (layoutParams instanceof RelativeLayout.LayoutParams) {
                            layoutParams = new RelativeLayout.LayoutParams(width, height);
                        } else if (layoutParams instanceof TableLayout.LayoutParams) {
                            layoutParams = new TableLayout.LayoutParams(width, height);
                        } else if (layoutParams instanceof ConstraintLayout.LayoutParams) {
                            layoutParams = new ConstraintLayout.LayoutParams(width, height);
                        } else if (layoutParams instanceof TableRow.LayoutParams) {
                            layoutParams = new TableRow.LayoutParams(width, height);
                        } else if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                            layoutParams = new ViewGroup.MarginLayoutParams(width, height);
                        } else {
                            layoutParams = new ViewGroup.LayoutParams(width, height);
                        }
                    }
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
    public void setViewWidthHeight(View view, Integer width, Integer height) {
        if (view != null && !(width == null && height == null)) {
            ViewGroup.LayoutParams layoutParams = getViewLayoutParams(view, width, height);
            if (layoutParams != null) {
                if (width != null) {
                    layoutParams.width = width;
                }
                if (height != null) {
                    layoutParams.height = height;
                }
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
