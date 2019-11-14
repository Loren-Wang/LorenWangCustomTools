package android.lorenwang.customview.titlebarHeadView;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.lorenwang.customview.R;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;


/**
 * 创建时间： 2018/10/25 0025 下午 14:51:35
 * 创建人：LorenWang
 * 功能作用：标题操作栏
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
public class AvlwTitleBarHeadView extends FrameLayout{
    private final int LAYOUT_TYPE_0 = 0;//（0）自定义view
    private final int LAYOUT_TYPE_1 = 1;//（1）后退、标题
    private final int LAYOUT_TYPE_2 = 2;//（2）后退、标题、右侧按钮
    private final int LAYOUT_TYPE_3 = 3;//（3）标题、右侧按钮
    private final int LAYOUT_TYPE_4 = 4;//（4）左侧按钮、标题
    private final int LAYOUT_TYPE_5 = 5;//（5）左侧按钮、右侧按钮
    private final int LAYOUT_TYPE_6 = 6;//（6）后退、右侧按钮
    private final int LAYOUT_TYPE_7 = 7;//（7）后退、标题、右侧图标
    private final int LAYOUT_TYPE_8 = 8;//（8）标题、右侧图标
    private final int LAYOUT_TYPE_9 = 9;//（9）后退、右侧图标
    private int backColor;//后退按钮颜色
    private int backImgRes;//后退资源图片
    private int backImgWidth;//后退按钮宽高
    private int backImgheight;//后退按钮高
    private int layoutType = LAYOUT_TYPE_1;//布局类型

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
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AvlwTitleBarHeadView);
        layoutType = attributes.getInt(R.styleable.AvlwTitleBarHeadView_layoutType, LAYOUT_TYPE_1);
        int customLayout = attributes.getResourceId(R.styleable.AvlwTitleBarHeadView_customLayout, -1);


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
     * @param context
     * @param customLayout
     * @return
     */
    private View getLayoutView(Context context, int customLayout) {
        View layoutView;
        switch (layoutType) {
            case LAYOUT_TYPE_0:
                layoutView = LayoutInflater.from(context).inflate(customLayout, null);
                break;
            case LAYOUT_TYPE_2:
                layoutView = LayoutInflater.from(context).inflate(R.layout.avlw_title_bar_head_view_type_2, null);
                break;
            case LAYOUT_TYPE_3:
                layoutView = LayoutInflater.from(context).inflate(R.layout.avlw_title_bar_head_view_type_3, null);
                break;
            case LAYOUT_TYPE_4:
                layoutView = LayoutInflater.from(context).inflate(R.layout.avlw_title_bar_head_view_type_4, null);
                break;
            case LAYOUT_TYPE_5:
                layoutView = LayoutInflater.from(context).inflate(R.layout.avlw_title_bar_head_view_type_5, null);
                break;
            case LAYOUT_TYPE_6:
                layoutView = LayoutInflater.from(context).inflate(R.layout.avlw_title_bar_head_view_type_6, null);
                break;
            case LAYOUT_TYPE_7:
                layoutView = LayoutInflater.from(context).inflate(R.layout.avlw_title_bar_head_view_type_7, null);
                break;
            case LAYOUT_TYPE_8:
                layoutView = LayoutInflater.from(context).inflate(R.layout.avlw_title_bar_head_view_type_8, null);
                break;
            case LAYOUT_TYPE_9:
                layoutView = LayoutInflater.from(context).inflate(R.layout.avlw_title_bar_head_view_type_9, null);
                break;
            case LAYOUT_TYPE_1:
            default:
                layoutView = LayoutInflater.from(context).inflate(R.layout.avlw_title_bar_head_view_type_1, null);
                break;
        }
        return layoutView;
    }

    /**
     * 设置组件属性
     *
     * @param context
     * @param attributes
     */
    private void setLayoutChildView(Context context, TypedArray attributes) {
        switch (layoutType) {
            case LAYOUT_TYPE_0:
                break;
            case LAYOUT_TYPE_2:
                //设置后退按钮样式
                setBackImgBtn(attributes);
                //设置标题
                setTitleTextView(attributes);
                //设置右侧按钮
                setRightBtn(attributes);
                break;
            case LAYOUT_TYPE_3:
                break;
            case LAYOUT_TYPE_4:
                break;
            case LAYOUT_TYPE_5:
                break;
            case LAYOUT_TYPE_6:
                break;
            case LAYOUT_TYPE_7:
                break;
            case LAYOUT_TYPE_8:
                break;
            case LAYOUT_TYPE_9:
                break;
            case LAYOUT_TYPE_1:
            default:
                //设置后退按钮样式
                setBackImgBtn(attributes);
                //设置标题
                setTitleTextView(attributes);
                break;
        }
        //设置背景颜色
        int viewBgColor = attributes.getColor(R.styleable.AvlwTitleBarHeadView_viewBgColor, Color.WHITE);
        setBackgroundColor(viewBgColor);
    }

    /**
     * 设置右侧按钮样式
     *
     * @param attributes
     */
    private void setRightBtn(TypedArray attributes) {
        ((Button) findViewById(R.id.btnRight)).setTextColor(attributes.getColor(R.styleable.AvlwTitleBarHeadView_rightBtnTextColor, Color.BLACK));
        ((Button) findViewById(R.id.btnRight)).setTextSize(TypedValue.COMPLEX_UNIT_PX, attributes.getDimensionPixelSize(R.styleable.AvlwTitleBarHeadView_rightBtnTextSize, 50));
        ((Button) findViewById(R.id.btnRight)).setText(attributes.getString(R.styleable.AvlwTitleBarHeadView_rightBtnText));
        //右侧按钮边距
        findViewById(R.id.btnRight).setPadding(
                Float.valueOf(attributes.getDimension(R.styleable.AvlwTitleBarHeadView_rightBtnLeft, 0)).intValue(),
                Float.valueOf(attributes.getDimension(R.styleable.AvlwTitleBarHeadView_rightBtnTop, 0)).intValue(),
                Float.valueOf(attributes.getDimension(R.styleable.AvlwTitleBarHeadView_rightBtnRight, 0)).intValue(),
                Float.valueOf(attributes.getDimension(R.styleable.AvlwTitleBarHeadView_rightBtnBottom, 0)).intValue()
        );
    }

    /**
     * 设置标题样式
     *
     * @param attributes
     */
    private void setTitleTextView(TypedArray attributes) {
        ((TextView) findViewById(R.id.tvTitle)).setTextColor(attributes.getColor(R.styleable.AvlwTitleBarHeadView_titleTextColor, Color.BLACK));
        ((TextView) findViewById(R.id.tvTitle)).setTextSize(TypedValue.COMPLEX_UNIT_PX, attributes.getDimensionPixelSize(R.styleable.AvlwTitleBarHeadView_titleTextSize, 50));
        ((TextView) findViewById(R.id.tvTitle)).setText(attributes.getString(R.styleable.AvlwTitleBarHeadView_titleText));
    }

    /**
     * 设置后退按钮样式
     *
     * @param attributes
     */
    private void setBackImgBtn(TypedArray attributes) {
        //设置后退按钮大小
        backImgWidth = (int) attributes.getDimension(R.styleable.AvlwTitleBarHeadView_backImgWidth, -1);
        backImgheight = (int) attributes.getDimension(R.styleable.AvlwTitleBarHeadView_backImgHeight, -1);
        backImgRes = attributes.getResourceId(R.styleable.AvlwTitleBarHeadView_backImgRes, R.mipmap.avlw_icon_arrow_left);
        if (backImgWidth > 0 && backImgheight > 0) {
            ViewGroup.LayoutParams layoutParams = findViewById(R.id.imgBtnBack).getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new LayoutParams(backImgWidth, backImgheight);
            } else {
                layoutParams.width = backImgWidth;
                layoutParams.height = backImgheight;
            }
            findViewById(R.id.imgBtnBack).setLayoutParams(layoutParams);
        }
        //设置后退按钮颜色
        backColor = attributes.getColor(R.styleable.AvlwTitleBarHeadView_backImgColor, Color.BLACK);
        //设置背景图片
        findViewById(R.id.imgBtnBack).setBackgroundResource(backImgRes);
        //设置背景图片修改颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(R.id.imgBtnBack).getBackground().setTint(backColor);
        }

        //后退按钮边距
        findViewById(R.id.fmBack).setPadding(
                Float.valueOf(attributes.getDimension(R.styleable.AvlwTitleBarHeadView_backImgLeft, 0)).intValue(),
                Float.valueOf(attributes.getDimension(R.styleable.AvlwTitleBarHeadView_backImgTop, 0)).intValue(),
                Float.valueOf(attributes.getDimension(R.styleable.AvlwTitleBarHeadView_backImgRight, 0)).intValue(),
                Float.valueOf(attributes.getDimension(R.styleable.AvlwTitleBarHeadView_backImgBottom, 0)).intValue()
        );
        //设置默认点击事件为后退并销毁当前页面
        setBackClick(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = (Activity) getContext();
                if(activity != null){
                    activity.onBackPressed();
                    activity.finish();
                }
            }
        });
    }

    /**
     * 设置右侧按钮点击事件
     *
     * @param onClickListener
     */
    public void setRightBtnClick(OnClickListener onClickListener) {
        if (findViewById(R.id.btnRight) != null && onClickListener != null) {
            findViewById(R.id.btnRight).setOnClickListener(onClickListener);
        }
    }

    /**
     * 设置标题点击事件
     *
     * @param onClickListener
     */
    public void setTitleViewClick(OnClickListener onClickListener) {
        if (findViewById(R.id.tvTitle) != null && onClickListener != null) {
            findViewById(R.id.tvTitle).setOnClickListener(onClickListener);
        }
    }

    /**
     * 设置后退按钮点击事件
     * @param onClickListener
     */
    public void setBackClick(OnClickListener onClickListener) {
        if (findViewById(R.id.fmBack) != null && findViewById(R.id.imgBtnBack) != null && onClickListener != null) {
            findViewById(R.id.fmBack).setOnClickListener(onClickListener);
            findViewById(R.id.imgBtnBack).setOnClickListener(onClickListener);
        }
    }
}
