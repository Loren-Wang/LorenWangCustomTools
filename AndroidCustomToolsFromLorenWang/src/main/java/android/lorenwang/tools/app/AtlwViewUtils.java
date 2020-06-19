package android.lorenwang.tools.app;

import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.lorenwang.tools.base.AtlwLogUtils;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

/**
 * 创建时间：2019-04-30 下午 15:47:15
 * 创建人：王亮（Loren wang）
 * 功能作用：控件相关工具类
 * 思路：
 * 方法：
 * 1、获取控件的LayoutParams---getViewLayoutParams（view，width，height）
 * 2、设置控件的宽高---setViewWidthHeight（view，width，height）
 * 3、设置控件宽高以及margin属性---setViewWidthHeightMargin（view，width，height，l,t,r,b）
 * 4、对Drawable着色---tintDrawable(drawable,ColorStateList)
 * 5、设置背景图片着色---setBackgroundTint(view,ColorStateList)
 * 6、设置图片控件的src资源的着色---setImageSrcTint(imageView,ColorStateList)
 * 7、设置文本控件的Drawable左上右下图片着色---setTextViewDrawableLRTBTint(textView,ColorStateList)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AtlwViewUtils {
    private final String TAG = getClass().getName();
    private static volatile AtlwViewUtils optionsInstance;

    private AtlwViewUtils() {
    }

    public static AtlwViewUtils getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwViewUtils.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwViewUtils();
                }
            }
        }
        return optionsInstance;
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
            //当前params
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams != null) {
                if (width == null) {
                    width = ViewGroup.LayoutParams.WRAP_CONTENT;
                }
                if (height == null) {
                    height = ViewGroup.LayoutParams.WRAP_CONTENT;
                }
                layoutParams.width = width;
                layoutParams.height = height;
            } else {
                //params为空，获取view的params类型进行更新
                try {
                    //通过反射获取父级的素有属性，然后取出父类中定义的静态params类型然后转换并设置给子view
                    Class<?>[] classes = view.getClass().getClasses();
                    for (Class<?> item : classes) {
                        if (ViewGroup.LayoutParams.class.isAssignableFrom(item)) {
                            return getViewLayoutParams(item, width, height);
                        }
                    }
                } catch (Exception e) {
                    AtlwLogUtils.logE(TAG, "当前ViewParams获取异常");
                }
                //当前的params获取失败，获取父级的
                if (view.getParent() != null) {
                    try {
                        //通过反射获取父级的素有属性，然后取出父类中定义的静态params类型然后转换并设置给子view
                        Class<?>[] classes = view.getParent().getClass().getClasses();
                        for (Class<?> item : classes) {
                            if (ViewGroup.LayoutParams.class.isAssignableFrom(item)) {
                                return getViewLayoutParams(item, width, height);
                            }
                        }
                    } catch (Exception e) {
                        AtlwLogUtils.logE(TAG, "当前View父级Params获取异常");
                    }
                }
                if (view instanceof LinearLayout) {
                    return getViewLayoutParams(LinearLayout.LayoutParams.class, width, height);
                } else if (view instanceof FrameLayout) {
                    return getViewLayoutParams(FrameLayout.LayoutParams.class, width, height);
                } else if (view instanceof RelativeLayout) {
                    return getViewLayoutParams(RelativeLayout.LayoutParams.class, width, height);
                } else if (view instanceof ConstraintLayout) {
                    return getViewLayoutParams(ConstraintLayout.LayoutParams.class, width, height);
                } else if (view instanceof ViewGroup) {
                    return getViewLayoutParams(ViewGroup.LayoutParams.class, width, height);
                }
            }
        }
        return null;
    }

    /**
     * 获取控件的LayoutParams
     *
     * @param paramsClass params 的class
     * @param width       控件显示的宽度
     * @param height      控件显示的高度
     * @return 控件的LayoutParams
     */
    public <T extends ViewGroup.LayoutParams> T getViewLayoutParams(Class<?> paramsClass,
                                                                    Integer width, Integer height) {
        if (paramsClass != null) {
            try {
                if (width == null) {
                    width = ViewGroup.LayoutParams.WRAP_CONTENT;
                }
                if (height == null) {
                    height = ViewGroup.LayoutParams.WRAP_CONTENT;
                }
                T params =
                        (T) paramsClass.getDeclaredConstructor(int.class, int.class).newInstance(width, height);
                params.width = width;
                params.height = height;
                return params;
            } catch (Exception e) {
                AtlwLogUtils.logE(TAG, "Params获取异常");
            }
        }
        return null;
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
    public void setViewWidthHeightMargin(View view, int width, int height, Integer left,
                                         Integer top, Integer right, Integer bottom) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = getViewLayoutParams(view, width, height);
            setViewMarginParams(view, layoutParams, left, top, right, bottom);
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
    public void setViewWidthHeightMargin(View view, Class<?> paramsClass, int width, int height,
                                         Integer left, Integer top, Integer right, Integer bottom) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = getViewLayoutParams(paramsClass, width, height);
            setViewMarginParams(view, layoutParams, left, top, right, bottom);
        }
    }

    /**
     * 设置view的外边距params
     *
     * @param layoutParams 布局params
     * @param view         要设置的控件
     * @param left         左外边距
     * @param top          上外边距
     * @param right        右外边距
     * @param bottom       下外边距
     */
    public void setViewMarginParams(View view, ViewGroup.LayoutParams layoutParams, Integer left,
                                    Integer top, Integer right, Integer bottom) {
        if (layoutParams != null) {
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams params = (LinearLayout.LayoutParams) layoutParams;
                params.setMargins(left == null ? params.leftMargin : left
                        , top == null ? params.topMargin : top
                        , right == null ? params.rightMargin : right
                        , bottom == null ? params.bottomMargin : bottom);
                view.setLayoutParams(params);
            } else {
                view.setLayoutParams(layoutParams);
            }

        }
    }

    /**
     * 对Drawable着色
     *
     * @param drawable 要着色的Drawable
     * @param colors   着色的颜色
     * @return f返回着色后的Drawable
     */
    public Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    /**
     * 设置背景图片着色
     *
     * @param view           控件
     * @param colorStateList 颜色
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setBackgroundTint(View view, ColorStateList colorStateList) {
        if (view != null && colorStateList != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.setBackgroundTintList(colorStateList);
            } else {
                Drawable background = view.getBackground();
                if (background != null) {
                    background = tintDrawable(background, colorStateList);
                    view.setBackground(background);
                }
            }
        }
    }

    /**
     * 设置图片控件的src资源的着色
     *
     * @param imageView      图片控件
     * @param colorStateList 颜色
     */
    public void setImageSrcTint(ImageView imageView, ColorStateList colorStateList) {
        if (imageView != null && colorStateList != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imageView.setImageTintList(colorStateList);
            } else {
                Drawable background = imageView.getDrawable();
                if (background != null) {
                    background = tintDrawable(background, colorStateList);
                    imageView.setImageDrawable(background);
                }
            }
        }
    }

    /**
     * 设置文本控件的Drawable左上右下图片着色
     *
     * @param textView       控件
     * @param colorStateList 颜色
     */
    public void setTextViewDrawableLRTBTint(TextView textView, ColorStateList colorStateList) {
        if (textView != null && colorStateList != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setCompoundDrawableTintList(colorStateList);
            } else {
                //获取图片进行着色
                Drawable[] compoundDrawables = textView.getCompoundDrawables();
                for (int i = 0; i < compoundDrawables.length; i++) {
                    if (compoundDrawables[i] != null)
                        compoundDrawables[i] = tintDrawable(compoundDrawables[i], colorStateList);
                }
                //设置Drawable
                textView.setCompoundDrawables(compoundDrawables[0], compoundDrawables[1],
                        compoundDrawables[2], compoundDrawables[3]);
            }
        }
    }

    /**
     * 获取文本字符串宽度
     *
     * @param paint 画笔
     * @param text  文本
     * @param start 要计算文本的起始位置
     * @param end   要计算文本的结束为止
     * @return 字符串宽度
     */
    public float getStrTextWidth(Paint paint, String text, Integer start, Integer end) {
        if (!JtlwCheckVariateUtils.getInstance().isHaveEmpty(paint, text)) {
            if (start != null) {
                if (text.length() < start) {
                    start = text.length() - 1;
                }
            } else {
                start = 0;
            }
            if (end != null) {
                if (text.length() < end) {
                    end = text.length() - 1;
                }
            } else {
                end = 0;
            }
            if (start == 0 && end == 0) {
                return 0f;
            } else {
                return paint.measureText(text, start, end);
            }
        }
        return 0f;
    }

    /**
     * 获取文本字符串宽度
     *
     * @param paint 画笔
     * @param text  文本
     * @return 字符串宽度
     */
    public float getStrTextWidth(Paint paint, String text) {
        if (JtlwCheckVariateUtils.getInstance().isEmpty(text)) {
            return 0f;
        }
        return getStrTextWidth(paint, text, 0, text.length());
    }

    /**
     * 获取文本字符串宽度
     *
     * @param textSize 文本大小
     * @param text     文本
     * @param start    要计算文本的起始位置
     * @param end      要计算文本的结束为止
     * @return 字符串宽度
     */
    public float getStrTextWidth(int textSize, String text, Integer start, Integer end) {
        if (JtlwCheckVariateUtils.getInstance().isEmpty(text)) {
            return 0f;
        }
        Paint paint;
        paint = new Paint(textSize);
        paint.setTextSize(textSize);
        return getStrTextWidth(paint, text, start, end);
    }

    /**
     * 获取文本字符串宽度
     *
     * @param textSize 文本大小
     * @param text     文本
     * @return 字符串宽度
     */
    public float getStrTextWidth(int textSize, String text) {
        if (JtlwCheckVariateUtils.getInstance().isEmpty(text)) {
            return 0f;
        }
        Paint paint;
        paint = new Paint(textSize);
        paint.setTextSize(textSize);
        return getStrTextWidth(paint, text, 0, text.length() - 1);
    }

    /**
     * 获取文本高度
     *
     * @param paint 画笔
     * @return 文本高度
     */
    public float getStrTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics;
        if (JtlwCheckVariateUtils.getInstance().isEmpty(paint)) {
            return 0f;
        }
        fontMetrics = paint.getFontMetrics();
        return fontMetrics.bottom - fontMetrics.top + paint.getStrokeWidth();

    }

    /**
     * 获取文本高度
     *
     * @param textSize 文本大小
     * @return 文本高度
     */
    public float getStrTextHeight(int textSize) {
        Paint paint;
        paint = new Paint();
        paint.setTextSize(textSize);
        return getStrTextHeight(paint);
    }
}
