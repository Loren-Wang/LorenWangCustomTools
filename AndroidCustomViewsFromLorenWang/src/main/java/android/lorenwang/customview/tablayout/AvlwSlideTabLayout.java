package android.lorenwang.customview.tablayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

/**
 * 创建时间： 2018/11/1 0001 上午 10:59:21
 * 创建人：LorenWang
 * 功能作用：滑动tablayout，跟随viewpager进行左右滑动交互的控件，点击控件的每一个item会相应的切换viewpage的位置
 * 同样滑动viewpager也会影响tablayout显示的位置，然后下面的下划线是平滑移动的
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class AvlwSlideTabLayout extends View {

    private String TAG;
    private int tabTextViewColor;//tab文本显示颜色
    private int tabTextViewSize;//tab文本显示大小
    private boolean isTabTextViewBold = false;//tab文本是否加粗
    private int tabSelectColor;//选中的文本颜色
    private boolean isTabSelectBold = false;//tab选中文本是否加粗

    private float lineHeight;//切换线的高度
    private float lineWidth;//切换线宽度,默认同文本宽度相同
    private int lineColor;//切换线颜色

    private Paint tabSelectPaint;//选择文字画笔
    private Paint tabNoSelectPaint;//未选中文字画笔
    private Paint tabLinePaint;//滑动切换线的画笔

    private Context context;
    private LinearLayout lineView;
    private List<String> tabTitleList = new ArrayList<>();
    private ViewPager2 viewPager;
    private boolean isAllowTouchChange = true;//是否允许触摸切换

    private float viewWidth = 0;//控件整体宽度
    private float viewHeight = 0;//控件高度
    private int nowSelectPosi = 0;//当前选择的位置
    private int lastSelectPosi = 0;//上一次选择的位置
    private float everyLastTextViewWidth = 0;//每一个文本控件的宽度，默认是均分总控件宽度
    private float everyLastTextContentWidth = 0;//每一个文本内容的宽度，默认第一个tab文本宽度
    private int textShowHeight;

    private float nowLinePaddingLeft = 0;//当前下滑线左侧的偏移
    private float lastNowLinePaddingLeft = 0;//开始位移之前的左侧位移
    private boolean isFirstSetData = false;//是否是第一次设置数据

    //间隔线相关
    private int intervalLineWidth = 0;//间隔线宽度
    private int intervalLineHeight = 0;//间隔线高度
    private int intervalLineColor = Color.WHITE;//间隔线颜色
    private Paint intervalLinePaint;//间隔线画笔
    private boolean isShowIntervalLine = false;//是否显示间隔线


    private ViewPager2.OnPageChangeCallback onPageChangeListener = new ViewPager2.OnPageChangeCallback() {
        private Float fromPosiTextWidth;//起点文字宽度
        private Float toPosiTextWidth;//位移终点文字宽度
        private Float nowLineAllPaddingLeft;//当前下划线总共需要的位移
        private boolean isFinish = true;// 是否结束位移

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

//            LogUtils.logD(TAG,"当前正在滑动的目标位置:::" + position);
//            LogUtils.logD(TAG,"当前正在滑动的百分比:::" + positionOffset);

            int addLeft = 0;//计算左侧间距
            for (int i = 0; i < position + 1; i++) {
                if (position == i) {//viewpager向左滑动时，position为当前页码-1，享有滑动时为当前页码
                    addLeft += everyLastTextViewWidth * positionOffset;
                } else {
                    addLeft += everyLastTextViewWidth;
                }
            }


            nowLinePaddingLeft = lastNowLinePaddingLeft + addLeft;//动态位移
            //重新布局
            invalidate();

        }

        @Override
        public void onPageSelected(int position) {
//            LogUtils.logD(TAG,"当前选择位置:::" + position);
            nowSelectPosi = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    public AvlwSlideTabLayout(Context context) {
        super(context);
        init(context);
    }

    public AvlwSlideTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AvlwSlideTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 设置默认的初始属性
     *
     * @param context 上下文
     */
    private void init(Context context) {
        this.context = context;
        TAG = getClass().getName();
        tabTextViewColor = Color.BLACK;
        tabTextViewSize = 20;
        tabSelectColor = Color.RED;
        lineHeight = 5;
        lineWidth = 0;
        textShowHeight = (int) (viewHeight - lineHeight);
        setPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (viewWidth != 0 && viewHeight != 0) {
            int size = tabTitleList.size();
            String text;
            float textWidth;
            Paint.FontMetrics fm;
            Rect bounds;
            float baseLine;

            float startx;//间隔线起始点x
            float startY;//间隔线起始点y
            float endx;//间隔线结束点x
            float endy;//间隔线结束点y

            //绘制文字
            for (int i = 0; i < size; i++) {
                text = tabTitleList.get(i);
                if (i == viewPager.getCurrentItem()) {
                    fm = tabSelectPaint.getFontMetrics();
                    textWidth = tabSelectPaint.measureText(text);
                    bounds = new Rect();
                    tabSelectPaint.getTextBounds(text, 0, text.length(), bounds);
                    baseLine = (getMeasuredHeight() - fm.bottom + fm.top) / 2 - fm.top;

                    canvas.drawText(text, getPaddingLeft() + everyLastTextViewWidth * i + (everyLastTextViewWidth - textWidth) / 2, getPaddingTop() + baseLine, tabSelectPaint);
                } else {
                    fm = tabNoSelectPaint.getFontMetrics();
                    textWidth = tabNoSelectPaint.measureText(text);
                    bounds = new Rect();
                    tabSelectPaint.getTextBounds(text, 0, text.length(), bounds);
                    baseLine = (getMeasuredHeight() - fm.bottom + fm.top) / 2 - fm.top;

                    canvas.drawText(text, getPaddingLeft() + everyLastTextViewWidth * i + (everyLastTextViewWidth - textWidth) / 2, getPaddingTop() + baseLine, tabNoSelectPaint);
                }

                //左侧边距为0的情况
                if (isFirstSetData) {
                    isFirstSetData = false;
                    lastNowLinePaddingLeft = nowLinePaddingLeft = (everyLastTextViewWidth - textWidth) / 2;
                }

                if (isShowIntervalLine && i > 0) {
                    if (intervalLinePaint != null) {
                        startx = everyLastTextViewWidth * i;
                        startY = (viewHeight - intervalLineHeight) / 2;
                        endx = startx + intervalLineWidth;
                        endy = startY + intervalLineHeight;
                        canvas.drawRect(startx, startY, endx, endy, intervalLinePaint);
                    }
                }

            }

            //下划线
            float right = getPaddingLeft() + nowLinePaddingLeft + everyLastTextContentWidth - ((everyLastTextContentWidth - lineWidth) / 2);
            right = Math.min(right, (viewWidth - getPaddingLeft() - getPaddingRight()));
            canvas.drawRect(getPaddingLeft() + nowLinePaddingLeft + ((everyLastTextContentWidth - lineWidth) / 2),
                    getPaddingTop() + viewHeight - lineHeight,
                    right,
                    viewHeight - getPaddingBottom(), tabLinePaint);

        } else {
            super.onDraw(canvas);
        }
    }

    private float lastX;
    private float lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isAllowTouchChange) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = event.getX();
                    lastY = event.getY();
                    return true;
                case MotionEvent.ACTION_UP:
                    if (judge(event.getX(), event.getY())) {
                        return true;
                    } else {
                        return super.onTouchEvent(event);
                    }
                default:
                    return true;
            }
        } else {
            return super.onTouchEvent(event);
        }
    }

    /**
     * 判断位置
     *
     * @param nowX 当前x坐标
     * @param nowY 当前点击y坐标
     * @return 点击范围是否一致，在误差范围内
     */
    private boolean judge(float nowX, float nowY) {
        if (Math.abs(nowX - lastX) < 50 && Math.abs(nowY - lastY) < 50) {
            int clickPosi = (int) (nowX / everyLastTextViewWidth);
            viewPager.setCurrentItem(clickPosi);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置默认属性
     *
     * @param tabTextViewColor   tab文本颜色
     * @param tabTextViewSize    tab文本大小
     * @param tabSelectColor     tab选中颜色
     * @param viewWidth        控件宽度
     * @param viewHeight       控件整体高度
     */
    public void setViewRules(
            Integer tabTextViewColor
            , Integer tabTextViewSize
            , boolean isTabTextViewBold
            , Integer tabSelectColor
            , boolean isTabSelectBold
            , Integer lineWidth
            , Integer lineHeight
            , Integer lineColor
            , double viewWidth
            , double viewHeight
            , List<String> tabTitleList
            , ViewPager2 viewPager) {
        this.tabTextViewColor = tabTextViewColor != null ? tabTextViewColor : this.tabTextViewColor;
        this.tabTextViewSize = tabTextViewSize != null ? tabTextViewSize : this.tabTextViewSize;
        this.isTabTextViewBold = isTabTextViewBold;
        this.tabSelectColor = tabSelectColor != null ? tabSelectColor : this.tabSelectColor;
        this.isTabSelectBold = isTabSelectBold;

        this.lineHeight = lineHeight != null ? lineHeight : this.lineHeight;
        this.lineColor = lineColor != null ? lineColor : this.tabSelectColor;
        this.viewWidth = (float) viewWidth;
        this.viewHeight = (float) viewHeight;
        this.tabTitleList = tabTitleList;
        this.viewPager = viewPager;

        //初始化部分常量
        everyLastTextViewWidth = (float) ((viewWidth - getPaddingLeft() - getPaddingRight()) * 1.0 / tabTitleList.size());
        textShowHeight = (int) ((viewHeight - getPaddingTop() - getPaddingBottom()) - this.lineHeight);

        //初始化部分动态变量
        everyLastTextContentWidth = tabSelectPaint.measureText(tabTitleList.get(0));
        this.lineWidth = lineWidth != null ? lineWidth : everyLastTextContentWidth;
        isFirstSetData = true;

        viewPager.unregisterOnPageChangeCallback(onPageChangeListener);
        viewPager.registerOnPageChangeCallback(onPageChangeListener);

        setPaint();
        postInvalidate();
    }

    /**
     * 设置默认属性，带有分割线样式
     *
     * @param tabTextViewColor   tab文本颜色
     * @param tabTextViewSize    tab文本大小
     * @param tabSelectColor     tab选中颜色
     * @param viewWidth          控件宽度
     * @param viewHeight         控件整体高度
     * @param intervalLineColor  分割线颜色
     * @param intervalLineWidth  分割线宽度
     * @param intervalLineHeight 分割线高度
     */
    public void setViewRules(
            Integer tabTextViewColor
            , Integer tabTextViewSize
            , boolean isTabTextViewBold
            , Integer tabSelectColor
            , boolean isTabSelectBold
            , Integer lineWidth
            , Integer lineHeight
            , Integer lineColor
            , double viewWidth
            , double viewHeight
            , List<String> tabTitleList
            , ViewPager2 viewPager, @ColorInt int intervalLineColor
            , int intervalLineWidth, int intervalLineHeight) {
        this.intervalLineColor = intervalLineColor;
        this.intervalLineWidth = intervalLineWidth;
        this.intervalLineHeight = intervalLineHeight;
        this.isShowIntervalLine = true;
        this.intervalLinePaint = new Paint();
        intervalLinePaint.setAntiAlias(true);
        intervalLinePaint.setColor(intervalLineColor);
        setViewRules(tabTextViewColor, tabTextViewSize, isTabTextViewBold
                , tabSelectColor, isTabSelectBold, lineWidth, lineHeight, lineColor
                , viewWidth, viewHeight, tabTitleList, viewPager);
    }

    private void setPaint() {
        //选中画笔
        tabSelectPaint = new Paint();
        tabSelectPaint.setAntiAlias(true);
        tabSelectPaint.setColor(tabSelectColor);
        tabSelectPaint.setTextSize(tabTextViewSize);
        //是否加粗
        if (isTabSelectBold) {
            tabSelectPaint.setTypeface(Typeface.DEFAULT_BOLD);
        }

        //未选中画笔
        tabNoSelectPaint = new Paint(tabSelectPaint);
        tabNoSelectPaint.setColor(tabTextViewColor);
        //是否加粗
        if (isTabTextViewBold) {
            tabNoSelectPaint.setTypeface(Typeface.DEFAULT_BOLD);
        }

        //下滑切换线画笔
        tabLinePaint = new Paint(tabSelectPaint);
        tabLinePaint.setColor(lineColor);
    }

    /**
     * 获取是否允许触摸变动位置
     *
     * @return 是否允许触摸改变位置
     */
    public boolean isAllowTouchChange() {
        return isAllowTouchChange;
    }

    /**
     * 设置是否允许触摸变动位置
     *
     * @param allowTouchChange 是否允许触摸改变位置
     * @return 当前实例
     */
    public AvlwSlideTabLayout setAllowTouchChange(boolean allowTouchChange) {
        isAllowTouchChange = allowTouchChange;
        return this;
    }
}
