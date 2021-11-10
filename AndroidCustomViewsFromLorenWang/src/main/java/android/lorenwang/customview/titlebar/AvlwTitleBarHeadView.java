package android.lorenwang.customview.titlebar;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.lorenwang.customview.R;
import android.lorenwang.tools.app.AtlwViewUtil;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatTextView;


/**
 * 功能作用：标题操作栏
 * 创建时间： 2018/10/25 0025 下午 14:51:35
 * 创建人：LorenWang
 * 方法介绍：
 * 思路：1、通过type加载不同的布局
 * 2、传递过来titlebar高度后设置给params
 * 3、自定义的几个xml布局文件
 * 4、标题大小以及颜色需要单独设置
 * 5、type样式布局列表
 * （0）自定义view
 * （1）后退、标题
 * （2）后退、标题、右侧按钮
 * （3）标题、右侧按钮
 * （4）左侧按钮、标题
 * （5）左侧按钮、右侧按钮
 * （6）后退、右侧按钮
 * （7）后退、标题、右侧图标
 * （8）标题、右侧图标
 * （9）后退、右侧图标
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class AvlwTitleBarHeadView extends FrameLayout {
    private final int LAYOUT_TYPE_0 = 0;//（0）自定义view
    private final int LAYOUT_TYPE_1 = 1;//（1）后退按钮图片、标题
    private final int LAYOUT_TYPE_2 = 2;//（2）后退按钮图片、标题、右侧按钮
    private final int LAYOUT_TYPE_3 = 3;//（3）标题、右侧按钮
    private final int LAYOUT_TYPE_4 = 4;//（4）左侧按钮、标题
    private final int LAYOUT_TYPE_5 = 5;//（5）左侧按钮、右侧按钮
    private final int LAYOUT_TYPE_6 = 6;//（6）后退按钮图片、右侧按钮
    private final int LAYOUT_TYPE_7 = 7;//（7）后退按钮图片、标题、右侧图标
    private final int LAYOUT_TYPE_8 = 8;//（8）标题、右侧图标
    private final int LAYOUT_TYPE_9 = 9;//（9）后退按钮图片、右侧图标
    private int layoutType = LAYOUT_TYPE_1;//布局类型

    /**
     * 默认文本颜色
     */
    private final int DEFAULT_TEXT_COLOR = Color.BLACK;
    /**
     * 默认文本大小
     */
    private final int DEFAULT_TEXT_SIZE = 50;
    /**
     * 默认操作按钮宽度
     */
    private final int DEFAULT_OPTIONS_WIDTH = ViewGroup.LayoutParams.WRAP_CONTENT;
    /**
     * 默认操作按钮高度
     */
    private final int DEFAULT_OPTIONS_HEIGHT = ViewGroup.LayoutParams.MATCH_PARENT;

    /**
     * 操作控件drawable设置类型为src（使用view的tag做记录）
     */
    private final int OPTION_VIEW_DRAWABLE_TYPE_SRC = 0;

    /**
     * 操作控件drawable设置类型为bg（使用view的tag做记录）
     */
    private final int OPTION_VIEW_DRAWABLE_TYPE_BG = 1;

    public AvlwTitleBarHeadView(Context context) {
        super(context);
        init(context, null, -1);
    }

    public AvlwTitleBarHeadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public AvlwTitleBarHeadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 初始化
     *
     * @param context      上下文
     * @param attrs        属性
     * @param defStyleAttr 属性
     */
    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AvlwTitleBarHeadView);
        layoutType = attributes.getInt(R.styleable.AvlwTitleBarHeadView_avlw_tbh_layoutType, LAYOUT_TYPE_1);
        int customLayout = attributes.getResourceId(R.styleable.AvlwTitleBarHeadView_avlw_tbh_customLayout, -1);


        //根据不同类型，读取不同数据
        View layoutView = getLayoutView(context, customLayout);
        //移除内部所有的view
        removeAllViews();
        //进行view变更
        if (layoutView != null) {
            addView(layoutView);
            //设置view数据
            setLayoutChildView(context, attributes);
        }
    }

    /**
     * 获取布局viwe
     *
     * @param context      上下文
     * @param customLayout 自定义布局
     * @return 布局view
     */
    private View getLayoutView(Context context, int customLayout) {
        View layoutView;
        switch (layoutType) {
            case LAYOUT_TYPE_0:
                layoutView = LayoutInflater.from(context).inflate(customLayout, null);
                break;
            case LAYOUT_TYPE_2:
                layoutView =
                        LayoutInflater.from(context).inflate(R.layout.avlw_title_bar_head_view_type_2, null);
                break;
            case LAYOUT_TYPE_3:
                layoutView =
                        LayoutInflater.from(context).inflate(R.layout.avlw_title_bar_head_view_type_3, null);
                break;
            case LAYOUT_TYPE_4:
                layoutView =
                        LayoutInflater.from(context).inflate(R.layout.avlw_title_bar_head_view_type_4, null);
                break;
            case LAYOUT_TYPE_5:
                layoutView =
                        LayoutInflater.from(context).inflate(R.layout.avlw_title_bar_head_view_type_5, null);
                break;
            case LAYOUT_TYPE_6:
                layoutView =
                        LayoutInflater.from(context).inflate(R.layout.avlw_title_bar_head_view_type_6, null);
                break;
            case LAYOUT_TYPE_7:
                layoutView =
                        LayoutInflater.from(context).inflate(R.layout.avlw_title_bar_head_view_type_7, null);
                break;
            case LAYOUT_TYPE_8:
                layoutView =
                        LayoutInflater.from(context).inflate(R.layout.avlw_title_bar_head_view_type_8, null);
                break;
            case LAYOUT_TYPE_9:
                layoutView =
                        LayoutInflater.from(context).inflate(R.layout.avlw_title_bar_head_view_type_9, null);
                break;
            case LAYOUT_TYPE_1:
            default:
                layoutView =
                        LayoutInflater.from(context).inflate(R.layout.avlw_title_bar_head_view_type_1, null);
                break;
        }
        return layoutView;
    }

    /**
     * 设置组件属性
     *
     * @param context    上下文
     * @param attributes 属性列表
     */
    private void setLayoutChildView(Context context, TypedArray attributes) {
        switch (layoutType) {
            case LAYOUT_TYPE_0:
                break;
            case LAYOUT_TYPE_2:
            case LAYOUT_TYPE_7:
                //设置左侧右侧控件样式
                setLeftOptionsView(attributes);
                setRightOptionsView(attributes);
                //设置标题
                setTitleTextView(attributes);
                break;
            case LAYOUT_TYPE_3:
            case LAYOUT_TYPE_8:
                setRightOptionsView(attributes);
                setTitleTextView(attributes);
                break;
            case LAYOUT_TYPE_5:
            case LAYOUT_TYPE_6:
            case LAYOUT_TYPE_9:
                setLeftOptionsView(attributes);
                setRightOptionsView(attributes);
                break;
            case LAYOUT_TYPE_1:
            case LAYOUT_TYPE_4:
            default:
                //设置后退按钮样式
                setLeftOptionsView(attributes);
                //设置标题
                setTitleTextView(attributes);
                break;
        }
        //设置背景颜色
        int viewBgColor = attributes.getColor(R.styleable.AvlwTitleBarHeadView_avlw_tbh_viewBgColor, Color.WHITE);
        setBackgroundColor(viewBgColor);
    }

    /**
     * 设置左侧操作控件
     *
     * @param attributes 样式
     */
    private void setLeftOptionsView(TypedArray attributes) {
        setOptionsView(findViewById(R.id.optionsLeft),
                attributes.getColor(R.styleable.AvlwTitleBarHeadView_avlw_tbh_leftBtnTextColor, DEFAULT_TEXT_COLOR),
                attributes.getDimensionPixelSize(R.styleable.AvlwTitleBarHeadView_avlw_tbh_leftBtnTextSize, DEFAULT_TEXT_SIZE),
                attributes.getString(R.styleable.AvlwTitleBarHeadView_avlw_tbh_leftBtnText),
                attributes.getDimensionPixelOffset(R.styleable.AvlwTitleBarHeadView_avlw_tbh_leftWidth, DEFAULT_OPTIONS_WIDTH),
                attributes.getDimensionPixelOffset(R.styleable.AvlwTitleBarHeadView_avlw_tbh_leftHeight, DEFAULT_OPTIONS_HEIGHT),
                attributes.getColor(R.styleable.AvlwTitleBarHeadView_avlw_tbh_leftBgColor, -1),
                attributes.getDrawable(R.styleable.AvlwTitleBarHeadView_avlw_tbh_leftBgRes),
                attributes.getDimensionPixelOffset(R.styleable.AvlwTitleBarHeadView_avlw_tbh_leftDistanceLeft, 0),
                attributes.getDimensionPixelOffset(R.styleable.AvlwTitleBarHeadView_avlw_tbh_leftDistanceTop, 0),
                attributes.getDimensionPixelOffset(R.styleable.AvlwTitleBarHeadView_avlw_tbh_leftDistanceRight, 0),
                attributes.getDimensionPixelOffset(R.styleable.AvlwTitleBarHeadView_avlw_tbh_leftDistanceBottom, 0));

        //设置默认点击事件为后退并销毁当前页面
        setOptionsLeftOnClick(v -> {
            Activity activity = (Activity) getContext();
            if (activity != null) {
                activity.onBackPressed();
                activity.finish();
            }
        });
    }

    /**
     * 设置右侧操作控件
     *
     * @param attributes 样式
     */
    private void setRightOptionsView(TypedArray attributes) {
        setOptionsView(findViewById(R.id.optionsRight),
                attributes.getColor(R.styleable.AvlwTitleBarHeadView_avlw_tbh_rightBtnTextColor, DEFAULT_TEXT_COLOR),
                attributes.getDimensionPixelSize(R.styleable.AvlwTitleBarHeadView_avlw_tbh_rightBtnTextSize, DEFAULT_TEXT_SIZE),
                attributes.getString(R.styleable.AvlwTitleBarHeadView_avlw_tbh_rightBtnText),
                attributes.getDimensionPixelOffset(R.styleable.AvlwTitleBarHeadView_avlw_tbh_rightWidth, DEFAULT_OPTIONS_WIDTH),
                attributes.getDimensionPixelOffset(R.styleable.AvlwTitleBarHeadView_avlw_tbh_rightHeight, DEFAULT_OPTIONS_HEIGHT),
                attributes.getColor(R.styleable.AvlwTitleBarHeadView_avlw_tbh_rightBgColor, -1),
                attributes.getDrawable(R.styleable.AvlwTitleBarHeadView_avlw_tbh_rightBgRes),
                attributes.getDimensionPixelOffset(R.styleable.AvlwTitleBarHeadView_avlw_tbh_rightDistanceLeft, 0),
                attributes.getDimensionPixelOffset(R.styleable.AvlwTitleBarHeadView_avlw_tbh_rightDistanceTop, 0),
                attributes.getDimensionPixelOffset(R.styleable.AvlwTitleBarHeadView_avlw_tbh_rightDistanceRight, 0),
                attributes.getDimensionPixelOffset(R.styleable.AvlwTitleBarHeadView_avlw_tbh_rightDistanceBottom, 0));
    }

    /**
     * 设置操作按钮
     *
     * @param view           操作按钮控件
     * @param textColor      当是文本按钮是设置文本按钮颜色
     * @param textSize       当是文本按钮时设置文本按钮文本大小
     * @param text           当是文本按钮是设置文本
     * @param width          控件宽度
     * @param height         控件高度
     * @param bgColor        背景颜色
     * @param bgDrawable     背景图片
     * @param leftDistance   左侧边距
     * @param topDistance    顶部间距
     * @param rightDistance  右侧间距
     * @param bottomDistance 底部间距
     */
    private void setOptionsView(View view, int textColor, int textSize, String text, int width,
                                int height, int bgColor,
                                Drawable bgDrawable, int leftDistance, int topDistance,
                                int rightDistance, int bottomDistance) {
        if (view == null) {
            return;
        }
        //设置图片
        if (view instanceof ImageView) {
            view.setTag(OPTION_VIEW_DRAWABLE_TYPE_SRC);
            if (bgDrawable != null) {
                view.setBackground(null);
                if (bgColor >= 0) {
                    ((ImageView) view).setImageDrawable(AtlwViewUtil.getInstance().tintDrawable(bgDrawable, ColorStateList.valueOf(bgColor)));
                } else {
                    ((ImageView) view).setImageDrawable(bgDrawable);
                }
            } else {
                if (bgColor >= 0) {
                    ((ImageView) view).setImageDrawable(null);
                    view.setBackgroundColor(bgColor);
                }
            }
        } else {
            view.setTag(OPTION_VIEW_DRAWABLE_TYPE_BG);
            if (bgDrawable != null) {
                view.setBackground(bgDrawable);
                if (bgColor >= 0) {
                    view.setBackgroundTintList(ColorStateList.valueOf(bgColor));
                }
            } else {
                if (bgColor >= 0) {
                    view.setBackgroundColor(bgColor);
                }
            }
        }
        //将边距转化为padding内边距
        if (width >= 0) {
            width += leftDistance + rightDistance;
        }
        if (height > 0) {
            width += topDistance + bottomDistance;
        }
        //设置大小以及外边距
        AtlwViewUtil.getInstance().setViewWidthHeight(view, width, height);
        //后退按钮边距
        view.setPadding(leftDistance, topDistance, rightDistance, bottomDistance);

        //设置文本内容
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(textColor);
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            if (text != null) {
                ((TextView) view).setText(text);
            }
        }
    }


    /**
     * 设置标题样式
     *
     * @param attributes 属性列表
     */
    private void setTitleTextView(TypedArray attributes) {
        View view = findViewById(R.id.tvTitle);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(attributes.getColor(R.styleable.AvlwTitleBarHeadView_avlw_tbh_titleTextColor, DEFAULT_TEXT_COLOR));
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    attributes.getDimensionPixelSize(R.styleable.AvlwTitleBarHeadView_avlw_tbh_titleTextSize, DEFAULT_TEXT_SIZE));
            String text = attributes.getString(R.styleable.AvlwTitleBarHeadView_avlw_tbh_titleText);
            if (text != null) {
                ((TextView) view).setText(text);
            }
        }
    }

    /**
     * 设置左侧按钮点击事件，默认为后退按钮
     *
     * @param onClickListener 点击事件
     */
    public void setOptionsLeftOnClick(OnClickListener onClickListener) {
        if (findViewById(R.id.optionsLeft) != null && onClickListener != null) {
            findViewById(R.id.optionsLeft).setOnClickListener(onClickListener);
        }
    }

    /**
     * 设置右侧按钮点击事件
     *
     * @param onClickListener 点击事件
     */
    public void setOptionsRightOnClick(OnClickListener onClickListener) {
        if (findViewById(R.id.optionsRight) != null && onClickListener != null) {
            findViewById(R.id.optionsRight).setOnClickListener(onClickListener);
        }
    }

    /**
     * 设置标题点击事件
     *
     * @param onClickListener 点击事件
     */
    public void setTitleViewClick(OnClickListener onClickListener) {
        if (findViewById(R.id.tvTitle) != null && onClickListener != null) {
            findViewById(R.id.tvTitle).setOnClickListener(onClickListener);
        }
    }

    /**
     * 设置标题文本（如果布局属性中有标题的话）
     *
     * @param text 标题文本
     */
    public void setTitleText(String text) {
        View view = findViewById(R.id.tvTitle);
        if (view instanceof AppCompatTextView && text != null) {
            ((AppCompatTextView) view).setText(text);
        }
    }

    /**
     * 设置标题文本（如果布局属性中有标题的话）
     *
     * @param textResId 标题文本资源
     */
    public void setTitleText(@StringRes int textResId) {
        View view = findViewById(R.id.tvTitle);
        if (view instanceof AppCompatTextView) {
            ((AppCompatTextView) view).setText(textResId);
        }
    }

    /**
     * 设置右侧操作控件文本
     *
     * @param text 文本
     */
    public void setOptionsRightText(String text) {
        View view = findViewById(R.id.optionsRight);
        if (view instanceof TextView && text != null) {
            ((TextView) view).setText(text);
        }
    }

    /**
     * 设置右侧操作控件文本
     *
     * @param textResId 标题文本资源
     */
    public void setOptionsRightText(@StringRes int textResId) {
        View view = findViewById(R.id.optionsRight);
        if (view instanceof TextView) {
            ((TextView) view).setText(textResId);
        }
    }

    /**
     * 设置左侧操作控件文本
     *
     * @param text 文本
     */
    public void setOptionsLeftText(String text) {
        View view = findViewById(R.id.optionsLeft);
        if (view instanceof TextView && text != null) {
            ((TextView) view).setText(text);
        }
    }

    /**
     * 设置左侧操作控件文本
     *
     * @param textResId 标题文本资源
     */
    public void setOptionsLeftText(@StringRes int textResId) {
        View view = findViewById(R.id.optionsLeft);
        if (view instanceof TextView) {
            ((TextView) view).setText(textResId);
        }
    }

    /**
     * 设置左侧控件图片
     *
     * @param resId 图片资源
     */
    public void setOptionsLeftImage(@DrawableRes int resId) {
        View view = findViewById(R.id.optionsLeft);
        Object tag = view.getTag();
        if (tag instanceof Integer) {
            switch ((Integer) tag) {
                case OPTION_VIEW_DRAWABLE_TYPE_SRC:
                    ((ImageView) view).setImageResource(resId);
                    break;
                case OPTION_VIEW_DRAWABLE_TYPE_BG:
                    view.setBackgroundResource(resId);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 设置左侧控件图片
     *
     * @param drawable 图片资源
     */
    public void setOptionsLeftImage(Drawable drawable) {
        if (drawable != null) {
            View view = findViewById(R.id.optionsLeft);
            Object tag = view.getTag();
            if (tag instanceof Integer) {
                switch ((Integer) tag) {
                    case OPTION_VIEW_DRAWABLE_TYPE_SRC:
                        ((ImageView) view).setImageDrawable(drawable);
                        break;
                    case OPTION_VIEW_DRAWABLE_TYPE_BG:
                        view.setBackground(drawable);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 设置右侧控件图片
     *
     * @param resId 图片资源
     */
    public void setOptionsRightImage(@DrawableRes int resId) {
        View view = findViewById(R.id.optionsRight);
        Object tag = view.getTag();
        if (tag instanceof Integer) {
            switch ((Integer) tag) {
                case OPTION_VIEW_DRAWABLE_TYPE_SRC:
                    ((ImageView) view).setImageResource(resId);
                    break;
                case OPTION_VIEW_DRAWABLE_TYPE_BG:
                    view.setBackgroundResource(resId);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 设置右侧控件图片
     *
     * @param drawable 图片资源
     */
    public void setOptionsRightImage(Drawable drawable) {
        if (drawable != null) {
            View view = findViewById(R.id.optionsRight);
            Object tag = view.getTag();
            if (tag instanceof Integer) {
                switch ((Integer) tag) {
                    case OPTION_VIEW_DRAWABLE_TYPE_SRC:
                        ((ImageView) view).setImageDrawable(drawable);
                        break;
                    case OPTION_VIEW_DRAWABLE_TYPE_BG:
                        view.setBackground(drawable);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
