package android.lorenwang.customview.switchButton;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.lorenwang.customview.AvlwCustomViewCommon;
import android.lorenwang.customview.R;
import android.lorenwang.tools.app.ThreadUtils;
import android.lorenwang.tools.image.AtlwImageCommonUtils;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * 创建时间：2019-05-07 上午 09:59:8
 * 创建人：王亮（Loren wang）
 * 功能作用：彷iOS切换按钮
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 * <!--背景选中（开关开启）颜色-->
 * <attr name="avlwSwitchButtnBgColorY" format="color"/>
 * <!--背景未选中（开关关闭）颜色-->
 * <attr name="avlwSwitchButtnBgColorN" format="color"/>
 * <!--按钮切换动画时间，因为无法使用long，所以直接使用int做取值，所以设置时间时尽量不要大于int的范围-->
 * <attr name="avlwSwitchButtnChangeAnimMill" format="integer"/>
 * <!--按钮无论开关显示的切换图标（例如左右滑动切换的圆球）-->
 * <attr name="avlwSwitchButtnDrawable" format="reference"/>
 * <!--按钮无论开关显示的切换图标（例如左右滑动切换的圆球）的渲染颜色，当没有设置Drawable时已渲染颜色为主-->
 * <attr name="avlwSwitchButtnDrawableTintColor" format="color"/>
 * <!--按钮无论开关显示的切换图标（例如左右滑动切换的圆球）的边框宽度-->
 * <attr name="avlwSwitchButtnDrawableStorkeWidth" format="dimension"/>
 * <!--按钮无论开关显示的切换图标（例如左右滑动切换的圆球）的边框颜色-->
 * <attr name="avlwSwitchButtnDrawableStorkeColor" format="color"/>
 * <!--按钮无论开关显示的切换图标（例如左右滑动切换的圆球）的外边距-->
 * <attr name="avlwSwitchButtnDrawableMarginWidth" format="dimension"/>
 * <!--按钮无论开关显示的切换图标（例如左右滑动切换的圆球）的宽度-->
 * <attr name="avlwSwitchButtnDrawableWidth" format="dimension"/>
 * <!--按钮无论开关显示的切换图标（例如左右滑动切换的圆球）的高度-->
 * <attr name="avlwSwitchButtnDrawableHeight" format="dimension"/>
 * <!--控件的圆角角度-->
 * <attr name="avlwSwitchButtnRadius" format="dimension"/>
 */

public class AvlwSwitchButton1 extends View implements AvlwCustomViewCommon {
    private final String TAG = "AvlwSwitchButton1";

    /***************************************配置参数************************************************/
    /**
     * 是否打开
     */
    private boolean isOpen = false;
    /**
     * 切换动画时间，默认为200ms
     */
    private long changeAnimMill = 200;
    /**
     * 总共切换动画需要移动修改的次数
     */
    private int changeTimeNum = 100;
    /**
     * 当前切换数量
     */
    private int nowTimeNum = 0;
    /**
     * 状态切换线程
     */
    private Runnable changeStateRunnable = new Runnable() {
        private Integer changeAllDistance = null;

        @Override
        public void run() {
            nowTimeNum++;
            if (changeAllDistance == null) {
                changeAllDistance = getChangeAllDistance();
            }
            if (isOpen) {
                changeDistance += changeAllDistance * 1.0 / changeTimeNum;
            } else {
                changeDistance += -changeAllDistance * 1.0 / changeTimeNum;
            }
            if (nowTimeNum < changeTimeNum) {
                postInvalidate();
                ThreadUtils.getInstance().postOnChildThreadDelayed(this, changeAnimMill / changeTimeNum);
            } else {
                nowTimeNum = 0;
            }
        }
    };
    /**
     * 状态切换背景切换动画
     */
    private ValueAnimator changeStateBgAnimator;


    /**************************************绘制参数*************************************************/
    /**
     * 背景画笔
     */
    private Paint paintBg = new Paint();
    /**
     * 切换图标边框画笔
     */
    private Paint changeDrawableStorkePaint = new Paint();
    /**
     * 开启时背景颜色
     */
    private int bgColorY = Color.GREEN;
    /**
     * 关闭时背景颜色
     */
    private int bgColorN = Color.GRAY;
    /**
     * 切换图标的图片
     */
    private Bitmap changeDrawableBitmap = null;
    /**
     * 切换图标的渲染颜色
     */
    private int changeDrawableTintColor = Color.WHITE;
    /**
     * 切换图标的边框宽度
     */
    private int changeDrawableStorkeWidth = 10;
    /**
     * 切换图标的边框颜色
     */
    private int changeDrawableStorkeColor = Color.WHITE;
    /**
     * 切换图标的外边距
     */
    private int changeDrawableMarginWidth = 5;
    /**
     * 切换图标的宽度
     */
    private int changeDrawableWidth = 30;
    /**
     * 切换图标的高度
     */
    private int changeDrawableHeight = 30;
    /**
     * 圆角角度
     */
    private int radius = 50;
    /**
     * 移动距离
     */
    private Double changeDistance = 0d;


    public AvlwSwitchButton1(Context context) {
        super(context);
        init(context, null, -1);
    }

    public AvlwSwitchButton1(Context context, @android.support.annotation.Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public AvlwSwitchButton1(Context context, @android.support.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * view初始化
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.AvlwSwitchButton, defStyleAttr, 0);
        this.isOpen = attr.getBoolean(R.styleable.AvlwSwitchButton_avlwSwitchButtnIsOpen, this.isOpen);
        this.bgColorY = attr.getColor(R.styleable.AvlwSwitchButton_avlwSwitchButtnBgColorY, this.bgColorY);
        this.bgColorN = attr.getColor(R.styleable.AvlwSwitchButton_avlwSwitchButtnBgColorN, this.bgColorN);
        this.changeDrawableStorkeColor = attr.getColor(R.styleable.AvlwSwitchButton_avlwSwitchButtnDrawableStorkeColor, this.changeDrawableStorkeColor);
        this.changeDrawableStorkeWidth = attr.getDimensionPixelOffset(R.styleable.AvlwSwitchButton_avlwSwitchButtnDrawableStorkeWidth, this.changeDrawableStorkeWidth);
        Drawable changeDrawable = attr.getDrawable(R.styleable.AvlwSwitchButton_avlwSwitchButtnDrawable);
        this.changeDrawableTintColor = attr.getColor(R.styleable.AvlwSwitchButton_avlwSwitchButtnDrawableTintColor, this.changeDrawableTintColor);
        this.changeDrawableMarginWidth = attr.getDimensionPixelOffset(R.styleable.AvlwSwitchButton_avlwSwitchButtnDrawableMarginWidth, this.changeDrawableMarginWidth);
        this.radius = attr.getDimensionPixelOffset(R.styleable.AvlwSwitchButton_avlwSwitchButtnRadius, this.radius);
        this.changeDrawableWidth = attr.getDimensionPixelOffset(R.styleable.AvlwSwitchButton_avlwSwitchButtnDrawableWidth, this.changeDrawableWidth);
        this.changeDrawableHeight = attr.getDimensionPixelOffset(R.styleable.AvlwSwitchButton_avlwSwitchButtnDrawableHeight, this.changeDrawableHeight);


        //背景画笔
        paintBg.reset();
        paintBg.setAntiAlias(true);
        if (isOpen) {
            paintBg.setColor(bgColorY);
        } else {
            paintBg.setColor(bgColorN);
        }
        //切换图标边框画笔
        changeDrawableStorkePaint.reset();
        changeDrawableStorkePaint.setAntiAlias(true);
        changeDrawableStorkePaint.setStyle(Paint.Style.STROKE);
        changeDrawableStorkePaint.setColor(changeDrawableStorkeColor);
        changeDrawableStorkePaint.setStrokeWidth(changeDrawableStorkeWidth);
        //切换图标
        if (changeDrawable == null) {
            changeDrawable = new ColorDrawable(changeDrawableTintColor);
        } else {
            changeDrawable = AtlwImageCommonUtils.getInstance().tintDrawable(changeDrawable, ColorStateList.valueOf(changeDrawableTintColor));
        }
        changeDrawable.setBounds(0, 0, changeDrawableWidth, changeDrawableHeight);
        changeDrawableBitmap = AtlwImageCommonUtils.getInstance().getRoundedCornerBitmap(changeDrawable, changeDrawableWidth, changeDrawableHeight, radius);
        //设置点击事件
        setOnClickListener(null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (isOpen) {
            changeDistance = getChangeAllDistance().doubleValue();
        } else {
            changeDistance = 0d;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(0, 0, getWidth(), getHeight(), radius, radius, paintBg);
        } else {
            canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), radius, radius, paintBg);
        }
        //绘制按钮
        if (changeDrawableBitmap != null) {
            //绘制切换图片
            canvas.drawBitmap(changeDrawableBitmap, changeDrawableMarginWidth + changeDistance.intValue()
                    , changeDrawableMarginWidth, changeDrawableStorkePaint);
            //绘制圆形边框
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.drawRoundRect(changeDrawableMarginWidth + changeDistance.intValue()
                        , changeDrawableMarginWidth
                        , changeDrawableMarginWidth + changeDrawableWidth + changeDistance.intValue()
                        , changeDrawableMarginWidth + changeDrawableHeight, radius, radius, changeDrawableStorkePaint);
            } else {
                canvas.drawRoundRect(new RectF(changeDrawableMarginWidth + changeDrawableStorkeWidth + changeDistance.intValue()
                        , changeDrawableMarginWidth + changeDrawableStorkeWidth
                        , changeDrawableMarginWidth + changeDrawableStorkeWidth + changeDrawableWidth + changeDistance.intValue()
                        , changeDrawableMarginWidth + changeDrawableStorkeWidth + changeDrawableHeight), radius, radius, changeDrawableStorkePaint);
            }
        }
    }

    @Override
    public void setOnClickListener(@Nullable final View.OnClickListener l) {
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l != null) {
                    l.onClick(v);
                }
                if (nowTimeNum == 0) {
                    isOpen = !isOpen;
                    ThreadUtils.getInstance().runOnChildThread(changeStateRunnable);

                    //初始化切换动画
                    if (changeStateBgAnimator == null) {
                        if (isOpen) {
                            changeStateBgAnimator = ObjectAnimator.ofObject(1, TAG, new ArgbEvaluator(), bgColorN, bgColorY);
                        } else {
                            changeStateBgAnimator = ObjectAnimator.ofObject(1, TAG, new ArgbEvaluator(), bgColorY, bgColorN);
                        }
                        changeStateBgAnimator.setDuration(changeAnimMill);
                        changeStateBgAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                        changeStateBgAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                paintBg.setColor((int) valueAnimator.getAnimatedValue());
                                postInvalidate();
                            }
                        });
                    }
                    //根据状态设置切换动画的颜色变更
                    if (isOpen) {
                        changeStateBgAnimator.setObjectValues(bgColorN, bgColorY);
                    } else {
                        changeStateBgAnimator.setObjectValues(bgColorY, bgColorN);
                    }
                    //开启动画
                    changeStateBgAnimator.start();
                }
            }
        });
    }

    /**
     * 是否是开启的
     *
     * @return
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * 设置开启状态
     *
     * @param open
     */
    public void setOpen(boolean open) {
        isOpen = open;
        postInvalidate();
    }

    /**
     * 获取总的要移动的距离
     *
     * @return
     */
    private Integer getChangeAllDistance() {
        return getWidth() - changeDrawableMarginWidth * 2 - changeDrawableWidth;
    }

    @Override
    public void release() {

    }
}
