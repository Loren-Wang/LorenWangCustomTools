package android.lorenwang.tools.app;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtil;

/**
 * 功能作用：控件相关工具类
 * 初始注释时间： 2021/9/17 11:08
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 获取控件的LayoutParams--getViewLayoutParams(view,width,height)
 * 获取控件的LayoutParams--getViewLayoutParams(paramsClass,view,width,height)
 * 设置控件的宽高--setViewWidthHeight(view,width,height)
 * 设置控件宽高以及margin属性--setViewWidthHeightMargin(view,width,height,left,top,right,bottom)
 * 设置控件宽高以及margin属性--setViewWidthHeightMargin(view,paramsClass,width,height,left,top,right,bottom)
 * 设置view的外边距params--setViewMarginParams(view,layoutParams,left,top,right,bottom)
 * 对Drawable着色--tintDrawable(drawable,colors)
 * 设置背景图片着色--setBackgroundTint(view,colorStateList)
 * 设置图片控件的src资源的着色--setImageSrcTint(imageView,colorStateList)
 * 设置文本控件的Drawable左上右下图片着色--setTextViewDrawableLRTBTint(textView,colorStateList)
 * 获取文本字符串宽度--getStrTextWidth(paint,text,start,end)
 * 获取文本字符串宽度--getStrTextWidth(paint,text)
 * 获取文本字符串宽度--getStrTextWidth(textSize,text,start,end)
 * 获取文本字符串宽度--getStrTextWidth(textSize,text)
 * 获取文本高度--getStrTextHeight(paint)
 * 获取文本高度--getStrTextHeight(textSize)
 * RecycleView 是否在顶部未向下滑动过--recycleViewIsTheTop(recyclerView)
 * 获取控件位图--getViewBitmap(optionsView)
 * 获取控件位图--getViewBitmap(optionsView,hideIds)
 * 获取控件位图--getViewBitmap(optionsView,useWidth,useHeight,hideIds)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AtlwViewUtil {
    private final String TAG = getClass().getName();
    private static volatile AtlwViewUtil optionsInstance;

    private AtlwViewUtil() {
    }

    public static AtlwViewUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwViewUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwViewUtil();
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
                return layoutParams;
            } else {
                //params为空，获取view的params类型进行更新
                try {
                    //通过反射获取父级的素有属性，然后取出父类中定义的静态params类型然后转换并设置给子view
                    Class<?>[] classes = view.getClass().getClasses();
                    for (Class<?> item : classes) {
                        if (ViewGroup.LayoutParams.class.isAssignableFrom(item)) {
                            return getViewLayoutParams(item, view, width, height);
                        }
                    }
                } catch (Exception e) {
                    AtlwLogUtil.logUtils.logE(TAG, "当前ViewParams获取异常");
                }
                //当前的params获取失败，获取父级的
                if (view.getParent() != null) {
                    try {
                        //通过反射获取父级的素有属性，然后取出父类中定义的静态params类型然后转换并设置给子view
                        Class<?>[] classes = view.getParent().getClass().getClasses();
                        for (Class<?> item : classes) {
                            if (ViewGroup.LayoutParams.class.isAssignableFrom(item)) {
                                return getViewLayoutParams(item, view, width, height);
                            }
                        }
                    } catch (Exception e) {
                        AtlwLogUtil.logUtils.logE(TAG, "当前View父级Params获取异常");
                    }
                }
                if (view instanceof LinearLayout) {
                    return getViewLayoutParams(LinearLayout.LayoutParams.class, view, width, height);
                } else if (view instanceof FrameLayout) {
                    return getViewLayoutParams(FrameLayout.LayoutParams.class, view, width, height);
                } else if (view instanceof RelativeLayout) {
                    return getViewLayoutParams(RelativeLayout.LayoutParams.class, view, width, height);
                } else if (view instanceof ConstraintLayout) {
                    return getViewLayoutParams(ConstraintLayout.LayoutParams.class, view, width, height);
                } else if (view instanceof ViewGroup) {
                    return getViewLayoutParams(ViewGroup.LayoutParams.class, view, width, height);
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
    public <T extends ViewGroup.LayoutParams> T getViewLayoutParams(Class<?> paramsClass, View view, Integer width, Integer height) {
        if (paramsClass != null) {
            try {
                if (width == null) {
                    width = ViewGroup.LayoutParams.WRAP_CONTENT;
                }
                if (height == null) {
                    height = ViewGroup.LayoutParams.WRAP_CONTENT;
                }
                T params = (T) view.getLayoutParams();
                if (params == null) {
                    params = (T) paramsClass.getDeclaredConstructor(int.class, int.class).newInstance(width, height);
                }
                params.width = width;
                params.height = height;
                return params;
            } catch (Exception e) {
                AtlwLogUtil.logUtils.logE(TAG, "Params获取异常");
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
    public void setViewWidthHeightMargin(View view, int width, int height, Integer left, Integer top, Integer right, Integer bottom) {
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
    public void setViewWidthHeightMargin(View view, Class<?> paramsClass, int width, int height, Integer left, Integer top, Integer right,
            Integer bottom) {
        if (view != null) {
            ViewGroup.LayoutParams layoutParams = getViewLayoutParams(paramsClass, view, width, height);
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
    public void setViewMarginParams(View view, ViewGroup.LayoutParams layoutParams, Integer left, Integer top, Integer right, Integer bottom) {
        if (layoutParams != null) {
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layoutParams;
                params.setMargins(left == null ? params.leftMargin : left, top == null ? params.topMargin : top,
                        right == null ? params.rightMargin : right, bottom == null ? params.bottomMargin : bottom);
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
                    if (compoundDrawables[i] != null) {
                        compoundDrawables[i] = tintDrawable(compoundDrawables[i], colorStateList);
                    }
                }
                //设置Drawable
                textView.setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]);
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
        if (!JtlwCheckVariateUtil.getInstance().isHaveEmpty(paint, text)) {
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
        if (JtlwCheckVariateUtil.getInstance().isEmpty(text)) {
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
        if (JtlwCheckVariateUtil.getInstance().isEmpty(text)) {
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
        if (JtlwCheckVariateUtil.getInstance().isEmpty(text)) {
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
        if (JtlwCheckVariateUtil.getInstance().isEmpty(paint)) {
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

    /**
     * RecycleView 是否在顶部未向下滑动过
     *
     * @param recyclerView RecycleView列表控件
     * @return 未向下或者空或者没有布局管理器均返回true，其他情况返回false
     */
    public boolean recycleViewIsTheTop(RecyclerView recyclerView) {
        if (recyclerView != null && recyclerView.getLayoutManager() != null && recyclerView.getLayoutManager().getChildCount() > 0) {
            View view = recyclerView.getLayoutManager().getChildAt(0);
            if (recyclerView.getLayoutManager().getPosition(view) > 0) {
                //第一个显示的view不是最顶部的view
                return false;
            }
            //间隔线偏移
            int offset = 0;
            Rect rect;
            RecyclerView.State state = new RecyclerView.State();
            for (int i = 0; i < recyclerView.getItemDecorationCount(); i++) {
                rect = new Rect(0, 0, 0, 0);
                recyclerView.getItemDecorationAt(i).getItemOffsets(rect, view, recyclerView, state);
                offset = Math.max(offset, rect.top);
            }

            //第一个显示的view是首尾的话获取当前view的偏移量
            return view.getTop() == offset;
        }
        return true;
    }

    /**
     * 获取控件位图
     *
     * @param optionsView 控件
     * @return 控件位图
     */
    public Bitmap getViewBitmap(View optionsView) {
        return getViewBitmap(optionsView, null, null, null);
    }

    /**
     * 获取控件位图
     *
     * @param optionsView 控件
     * @param hideIds     要隐藏的id列表
     * @return 控件位图
     */
    public Bitmap getViewBitmap(View optionsView, Integer[] hideIds) {
        return getViewBitmap(optionsView, null, null, hideIds);
    }

    /**
     * 获取控件位图
     *
     * @param optionsView 控件
     * @param useWidth    要使用的宽度
     * @param useHeight   要使用的高度
     * @param hideIds     要隐藏的id列表
     * @return 控件位图
     */
    public Bitmap getViewBitmap(View optionsView, Integer useWidth, Integer useHeight, Integer[] hideIds) {
        if (optionsView == null) {
            return null;
        }
        List<Integer> showViews = new ArrayList<>();
        //处理布局(隐藏不需要显示的，同时记录改变显示状态的)
        View view;
        if (hideIds != null) {
            for (int id : hideIds) {
                view = optionsView.findViewById(id);
                if (view.getVisibility() == View.VISIBLE) {
                    showViews.add(id);
                    view.setVisibility(View.GONE);
                }
            }
        }
        //宽高处理
        int width;
        int height;
        if (optionsView instanceof ScrollView) {
            View container = ((ViewGroup) optionsView).getChildAt(0);
            width = container.getWidth();
            height = container.getHeight();
        } else {
            width = optionsView.getWidth();
            height = optionsView.getHeight();
        }
        if (useWidth != null && useWidth > 0) {
            width = useWidth;
        }
        if (useHeight != null && useHeight > 0) {
            height = useHeight;
        }
        if (width <= 0 || height <= 0) {
            //恢复布局
            for (Integer id : showViews) {
                optionsView.findViewById(id).setVisibility(View.VISIBLE);
            }
            return null;
        }

        //获取位图
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        optionsView.draw(canvas);

        //恢复布局
        for (Integer id : showViews) {
            optionsView.findViewById(id).setVisibility(View.VISIBLE);
        }
        return bitmap;
    }
}
