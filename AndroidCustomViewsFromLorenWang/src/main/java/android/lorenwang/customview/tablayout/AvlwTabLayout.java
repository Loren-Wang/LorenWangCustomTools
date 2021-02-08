package android.lorenwang.customview.tablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.lorenwang.customview.R;
import android.lorenwang.tools.app.AtlwThreadUtil;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

/**
 * 功能作用：tablayout自定义控件
 * 创建时间：2020-01-15 14:32
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AvlwTabLayout extends View implements AvlwBaseTabLayout {
    private final String TAG = getClass().getName();
    /**
     * 文本和下划线类型
     */
    private final int SHOW_TYPE_TEXT_LINE = 0;
    /**
     * 文本和背景类型
     */
    private final int SHOW_TYPE_TEXT_BG = 1;
    /**
     * 文本、下划线、下划线容器类型
     */
    private final int SHOW_TYPE_TEXT_LINE_CONTAINER = 2;
    private AvlwBaseTabLayoutChangeListener changeListener;

    /*-------------------------------------绘制参数-------------------------------------*/
    /**
     * tab宽度
     */
    private float tabWidth = 0;
    /**
     * tab宽度
     */
    private float tabHeight = 0;
    /**
     * tab选中文本画笔
     */
    private Paint tabTextPaintY;
    /**
     * tab未选中文本画笔
     */
    private Paint tabTextPaintN;
    /**
     * 下划线滑动百分比
     */
    private float lineSlipPercent = 0f;
    /**
     * 布局偏移，用来做左右滑动使用的
     */
    private float leftOffset = 0f;
    /**
     * 手指按下时的偏移
     */
    private float downLeftOffset = leftOffset;

    /*-------------------------------------配置参数-------------------------------------*/
    /**
     * 显示类型
     */
    private int showType = SHOW_TYPE_TEXT_LINE;
    /**
     * 要实际绘制的部分
     */
    private AvlwBaseTabLayout drawTabLayout;
    /**
     * tab文本列表
     */
    private List<String> tabTextList = new ArrayList<>();
    /**
     * 当前位置
     */
    private Integer currentPosition = 0;
    /**
     * 滑动目标位置
     */
    private Integer scrollToPosition = 0;
    /**
     * tab文本列表大小
     */
    private int tabTextListSize = 0;
    /**
     * tab选中文本的文本大小是否是最大的
     */
    private boolean tabSizeMaxY = true;
    /**
     * 文本是否使用基线对齐方式
     */
    private boolean lineTextBaselineAligning = true;
    /**
     * 是否允许触摸改变位置
     */
    private boolean allowTouchChange = true;
    /**
     * 是否允许滑动切换
     */
    private boolean allowScrollChange = true;
    /**
     * 按下时x坐标
     */
    private float downX;
    /**
     * 按下时y坐标
     */
    private float downY;
    /**
     * 滑动跳转到指定位置线程
     */
    private final AvlwTabScrollToPositionRunnable scrollToPositionRunnable = new AvlwTabScrollToPositionRunnable() {
        @Override
        public void run() {
            float percent = 0f;
            while (true) {
                percent += 0.05f;
                //修改滑动进度
                changeScrollPercent(percent, onTouchChange);
                if ((int) percent == 1) {
                    break;
                }
                try {
                    if (percent < 0.75) {
                        Thread.sleep(15);
                    } else {
                        Thread.sleep(30);
                    }
                } catch (Exception e) {
                    AtlwLogUtil.logUtils.logE(TAG, "滑动异常");
                }
            }
        }
    };

    public AvlwTabLayout(Context context) {
        super(context);
        init(context, null, -1);
    }

    public AvlwTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public AvlwTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AvlwTabLayout);
        showType = attributes.getInt(R.styleable.AvlwTabLayout_avlw_tl_showType, showType);
        tabWidth = attributes.getDimension(R.styleable.AvlwTabLayout_avlw_tl_tabWidth, tabWidth);
        tabHeight = attributes.getDimension(R.styleable.AvlwTabLayout_avlw_tl_tabHeight, tabHeight);
        currentPosition = attributes.getInt(R.styleable.AvlwTabLayout_avlw_tl_tabDefaultPosition, currentPosition);
        lineTextBaselineAligning = attributes.getBoolean(R.styleable.AvlwTabLayout_avlw_tl_lineTextBaselineAligning, lineTextBaselineAligning);

        //初始化tab画笔
        tabTextPaintY = new Paint();
        tabTextPaintY.setAntiAlias(true);
        tabTextPaintY.setTextSize(attributes.getDimension(R.styleable.AvlwTabLayout_avlw_tl_tabTextSizeY, 50f));
        tabTextPaintY.setColor(attributes.getColor(R.styleable.AvlwTabLayout_avlw_tl_tabTextColorY, Color.BLUE));
        //加粗配置
        if (attributes.getBoolean(R.styleable.AvlwTabLayout_avlw_tl_tabTextBoldY, false)) {
            tabTextPaintY.setTypeface(Typeface.DEFAULT_BOLD);
        }
        tabTextPaintN = new Paint();
        tabTextPaintN.setAntiAlias(true);
        tabTextPaintN.setTextSize(attributes.getDimension(R.styleable.AvlwTabLayout_avlw_tl_tabTextSizeN, 50f));
        tabTextPaintN.setColor(attributes.getColor(R.styleable.AvlwTabLayout_avlw_tl_tabTextColorN, Color.BLACK));
        //加粗配置
        if (attributes.getBoolean(R.styleable.AvlwTabLayout_avlw_tl_tabTextBoldN, false)) {
            tabTextPaintN.setTypeface(Typeface.DEFAULT_BOLD);
        }
        //判断选中文本大小是否是最大的
        tabSizeMaxY = Float.compare(tabTextPaintY.getTextSize(), tabTextPaintN.getTextSize()) >= 0;


        //获取相对于屏幕百分比，大于0情况下安照百分比来显示宽度
        float tabWidthPercent = attributes.getFloat(R.styleable.AvlwTabLayout_avlw_tl_tabWidthPercent, -1.0F);
        if (tabWidthPercent > (float) 0) {
            if (tabWidthPercent > (float) 1) {
                tabWidthPercent = 1.0F;
            }
            this.tabWidth = (float) getResources().getDisplayMetrics().widthPixels * tabWidthPercent;
        }

        //读取tab文本列表tl_tabTextList
        String texts = attributes.getString(R.styleable.AvlwTabLayout_avlw_tl_tabTextList);
        texts = texts == null ? "" : texts;
        //格式化处理文本列表
        String[] split = texts.split("~~");
        tabTextList = Arrays.asList(split);
        tabTextListSize = tabTextList.size();

        //初始化实际的配置部分
        switch (showType) {
            case SHOW_TYPE_TEXT_LINE_CONTAINER:
                drawTabLayout = new AvlwTabLayoutTypeTextLineContainer(context, this, attributes, tabWidth, tabHeight);
                break;
            case SHOW_TYPE_TEXT_BG:
                drawTabLayout = new AvlwTabLayoutTypeTextBg(context, this, attributes, tabWidth, tabHeight);
                break;
            case SHOW_TYPE_TEXT_LINE:
            default:
                drawTabLayout = new AvlwTabLayoutTypeTextLine(context, this, attributes, tabWidth, tabHeight);
                break;
        }

        attributes.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = widthMeasureSpec;
        int height = heightMeasureSpec;
        //wrap的时候读取自定义的宽高设置
        if (getLayoutParams() != null) {
            if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
                width = Math.min(getMeasureWidth(this, widthMeasureSpec, tabTextListSize), getResources().getDisplayMetrics().widthPixels);
            }
            if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                height = Math.min(getMeasureHeight(this, heightMeasureSpec, tabTextListSize), getResources().getDisplayMetrics().heightPixels);
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制基础部分
        String currentText = tabTextList.get(currentPosition);
        float currentWidth = getTextWidth(tabTextPaintY, currentText);
        float scrollToWidth = currentWidth;
        Float startX = getDrawTextX(currentWidth, currentPosition);
        float stopX = startX;
        if (currentPosition.compareTo(scrollToPosition) == 0) {
            drawTabLayout.drawTypeChild(canvas, currentPosition, currentPosition, lineSlipPercent, startX, startX, currentWidth, currentWidth);
        } else {
            String scrollToText = tabTextList.get(scrollToPosition);
            scrollToWidth = getTextWidth(tabTextPaintY, scrollToText);
            stopX = getDrawTextX(scrollToWidth, scrollToPosition);
            drawTabLayout.drawTypeChild(canvas, currentPosition, scrollToPosition, lineSlipPercent, startX, stopX, currentWidth, scrollToWidth);
        }

        List<Float> drawTextLocation = new ArrayList<>();

        //item部分绘制
        Paint paint;
        String text;
        boolean isCurrent;
        float drawTextX;
        float drawTextY;
        float textWidth;
        //绘制子view的item的type
        for (int i = 0; i < tabTextList.size(); i++) {
            //当前是否选中
            isCurrent = currentPosition.compareTo(i) == 0;
            //获取要使用的哪个画笔
            paint = isCurrent ? tabTextPaintY : tabTextPaintN;
            //要绘制的文本
            text = tabTextList.get(i);
            //要绘制的文本宽度
            textWidth = getTextWidth(paint, text);
            //获取文本坐标
            drawTextX = getDrawTextX(textWidth, i);
            drawTextY = getDrawTextY(isCurrent);
            drawTextLocation.add(drawTextX);
            drawTextLocation.add(drawTextY);
            //item类型绘制，为了子view的动态
            if (currentPosition.compareTo(scrollToPosition) != 0) {
                //当前和要滑动的不是一个，为了子view的动态
                if (currentPosition.compareTo(i) == 0) {
                    //当前遍历到的位置是当前选中的，为了子view的动态
                    drawTypeItem(canvas, paint, drawTextX, drawTextY, stopX, scrollToWidth,
                            startX.compareTo(stopX) > 0 ? -lineSlipPercent : lineSlipPercent, isCurrent, textWidth, paint.descent() - paint.ascent());
                } else if (scrollToPosition.compareTo(i) == 0) {
                    //当前遍历到的是目标位置的，为了子view的动态
                    drawTypeItem(canvas, paint, drawTextX, drawTextY, startX, currentWidth,
                            startX.compareTo(stopX) > 0 ? lineSlipPercent : -lineSlipPercent, isCurrent, textWidth, paint.descent() - paint.ascent());
                } else {
                    //其余情况正常绘制
                    drawTypeItem(canvas, paint, drawTextX, drawTextY, drawTextX, textWidth, 0, isCurrent, textWidth,
                            paint.descent() - paint.ascent());
                }
            } else {
                drawTypeItem(canvas, paint, drawTextX, drawTextY, drawTextX, textWidth, 0, isCurrent, textWidth, paint.descent() - paint.ascent());
            }
        }
        //绘制文本
        for (int i = 0; i < tabTextList.size(); i++) {
            //当前是否选中
            isCurrent = currentPosition.compareTo(i) == 0;
            //获取要使用的哪个画笔
            paint = isCurrent ? tabTextPaintY : tabTextPaintN;
            //要绘制的文本
            text = tabTextList.get(i);
            //绘制文本
            canvas.drawText(text, drawTextLocation.get(i * 2), drawTextLocation.get(i * 2 + 1), paint);
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                downLeftOffset = leftOffset;
                break;
            case MotionEvent.ACTION_MOVE:
                leftOffset = downLeftOffset + event.getX() - downX;
                if (leftOffset > 0) {
                    //一旦大于0代表着已经滑动到了左边界
                    leftOffset = 0;
                } else if (Math.abs(leftOffset) > getMeasureWidth(this, 0, tabTextListSize) - getWidth()) {
                    //绝对值大于差值则代表着已经到了右边界
                    leftOffset = -Math.abs(getMeasureWidth(this, 0, tabTextListSize) - getWidth());
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (allowTouchChange) {
                    checkChangePosition(event.getX(), event.getY());
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 检测是否是要修改位置,如果要修改则直接切换位置
     *
     * @param nowX 当前x坐标
     * @param nowY 当前点击y坐标
     */
    private void checkChangePosition(float nowX, float nowY) {
        if (Math.abs(nowX - downX) < 25 && Math.abs(nowY - downY) < 25) {
            int clickPosition = (int) ((nowX - getPaddingLeft() - leftOffset) / tabWidth);
            setCurrentPosition(clickPosition, false, true);
        }
    }

    /**
     * 无用，给其他子控件使用的
     */
    @Override
    public int getMeasureWidth(AvlwTabLayout avlwTabLayout, int widthMeasureSpec, int tabTextListSize) {
        return drawTabLayout.getMeasureWidth(avlwTabLayout, widthMeasureSpec, tabTextListSize);
    }

    /**
     * 无用，给其他子控件使用的
     */
    @Override
    public int getMeasureHeight(AvlwTabLayout avlwTabLayout, int heightMeasureSpec, int tabTextListSize) {
        return drawTabLayout.getMeasureHeight(avlwTabLayout, heightMeasureSpec, tabTextListSize);
    }

    /**
     * 绘制子view
     *
     * @param canvas           画板
     * @param currentPosition  当前位置
     * @param scrollToPosition 目标位置
     * @param lineSlipPercent  移动百分比
     * @param startX           起始点x坐标
     * @param stopX            结束点x坐标
     * @param currentWidth     当前位置宽度
     * @param scrollToWidth    滑动到目标的宽度
     */
    @Override
    public void drawTypeChild(Canvas canvas, Integer currentPosition, Integer scrollToPosition, float lineSlipPercent, float startX, float stopX,
            float currentWidth, float scrollToWidth) {
        drawTabLayout.drawTypeChild(canvas, currentPosition, this.scrollToPosition, lineSlipPercent, startX, stopX, currentWidth, scrollToWidth);
    }

    /**
     * 绘制子view的item
     *
     * @param canvas            画板
     * @param textPaint         文本画笔
     * @param drawTextX         文本x坐标
     * @param drawTextY         文本y坐标
     * @param scrollToTextX     滑动目标的文本x坐标
     * @param scrollToTextWidth 滑动到目标的文本宽度
     * @param lineScrollPercent 滑动到目标的滑动百分比
     * @param isCurrent         当前是否是选中的
     * @param textWidth         文本宽度
     * @param textHeight        文本高度
     */
    @Override
    public void drawTypeItem(Canvas canvas, Paint textPaint, float drawTextX, float drawTextY, float scrollToTextX, float scrollToTextWidth,
            float lineScrollPercent, boolean isCurrent, float textWidth, float textHeight) {
        drawTabLayout.drawTypeItem(canvas, textPaint, drawTextX, drawTextY, scrollToTextX, scrollToTextWidth, lineScrollPercent, isCurrent, textWidth,
                textHeight);
    }

    /**
     * 切换位置
     *
     * @param currentPosition 当前位置
     */
    public void setCurrentPosition(int currentPosition) {
        setCurrentPosition(currentPosition, false);
    }

    /**
     * 切换位置
     *
     * @param currentPosition 目标位置
     * @param percent         目标百分比
     */
    public void setCurrentPosition(int currentPosition, @FloatRange(from = 0, to = 1) float percent) {
        setCurrentPosition(currentPosition, false, percent);
    }

    /**
     * 切换位置
     *
     * @param currentPosition 目标位置
     * @param percent         目标百分比
     * @param isOnTouchChange 是否是触摸修改位置的
     */
    private void setCurrentPosition(int currentPosition, boolean isOnTouchChange, @FloatRange(from = 0, to = 1) float percent) {
        if (tabTextList == null || currentPosition >= tabTextList.size()) {
            return;
        }
        allowScrollChange = false;
        this.scrollToPosition = currentPosition;
        changeScrollPercent(percent, isOnTouchChange);
        allowScrollChange = true;
    }

    /**
     * 切换位置
     *
     * @param currentPosition 当前位置
     * @param isScroll        是否滑动切换
     */
    public void setCurrentPosition(int currentPosition, boolean isScroll) {
        setCurrentPosition(currentPosition, isScroll, false);
    }

    /**
     * 切换位置
     *
     * @param currentPosition 当前位置
     * @param isScroll        是否滑动切换
     * @param isOnTouchChange 是否是触摸切换的
     */
    private void setCurrentPosition(int currentPosition, boolean isScroll, boolean isOnTouchChange) {
        if (tabTextList == null || currentPosition >= tabTextList.size()) {
            return;
        }
        if (isScroll) {
            if (allowScrollChange) {
                allowScrollChange = false;
                this.scrollToPosition = currentPosition;
                scrollToPositionRunnable.setOnTouchChange(isOnTouchChange);
                AtlwThreadUtil.getInstance().postOnChildThread(scrollToPositionRunnable);
            }
        } else {
            this.currentPosition = currentPosition;
            //切换结束之后位置处理
            currentPositionChangeFinish(isOnTouchChange);
            invalidate();
        }
    }

    /**
     * 获取当前位置
     *
     * @return 当前位置
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    /**
     * 设置是否允许触摸改变位置
     *
     * @param allowTouchChange 允许触摸改变位置
     */
    public void setAllowTouchChange(boolean allowTouchChange) {
        this.allowTouchChange = allowTouchChange;
    }

    /**
     * 获取文本宽度
     *
     * @param paint 文本画笔
     * @param text  文本
     * @return 文本宽度
     */
    private float getTextWidth(Paint paint, String text) {
        float textWidth = paint.measureText(text);
        if (textWidth > tabWidth) {
            textWidth = tabWidth;
        }
        return textWidth;
    }

    /**
     * 获取要绘制文本的x轴坐标
     *
     * @param textWidth 文本宽度
     * @param index     文本在tab中的位置
     * @return x轴坐标
     */
    private float getDrawTextX(float textWidth, int index) {
        return getPaddingLeft() + tabWidth * index + (tabWidth - textWidth) / 2 + leftOffset;
    }

    /**
     * 设置改变监听
     *
     * @param changeListener 改变监听
     */
    public void setChangeListener(AvlwBaseTabLayoutChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    /**
     * 设置tab数据列表
     *
     * @param tabTextList tab数据列表
     * @return 当前实例
     */
    public AvlwTabLayout setTabTextList(List<String> tabTextList) {
        if (tabTextList != null) {
            this.tabTextList = tabTextList;
            tabTextListSize = tabTextList.size();
        }
        return this;
    }

    /**
     * 获取文本列表
     *
     * @return 文本列表
     */
    public List<String> getTabTextList() {
        return tabTextList;
    }

    /**
     * 获取文本所在文本列表位置
     *
     * @param text 文本
     * @return 位置信息，如果查找不到返回null
     */
    public Integer getTextPosition(String text) {
        if (JtlwCheckVariateUtils.getInstance().isNotEmpty(text)) {
            int indexOf = tabTextList.indexOf(text);
            if (indexOf >= 0) {
                return indexOf;
            }
        }
        return null;
    }

    /**
     * 设置tab宽度
     *
     * @param tabWidth tab宽度
     * @return 当前view实例
     */
    @Override
    public AvlwTabLayout setTabWidth(float tabWidth) {
        this.tabWidth = tabWidth;
        drawTabLayout.setTabWidth(tabWidth);
        return this;
    }

    /**
     * 设置屏幕宽度百分比
     *
     * @param percent 宽度百分比0-1
     * @return 当前实例
     */
    public AvlwTabLayout setTabWidthPercent(@FloatRange(from = 0, to = 1) float percent) {
        //获取相对于屏幕百分比，大于0情况下安照百分比来显示宽度
        if (percent > (float) 0) {
            if (percent > (float) 1) {
                percent = 1.0F;
            }
            this.tabWidth = (float) getResources().getDisplayMetrics().widthPixels * percent;
            drawTabLayout.setTabWidth(tabWidth);
        }
        return this;
    }

    /**
     * 设置文本配置
     *
     * @param textSizeY   选中文本大小
     * @param textSizeN   非选中文本大小
     * @param textColorY  选中文本颜色
     * @param textColorN  非选中文本颜色
     * @param isTextYBold 选中文本是否加粗
     * @param isTextNBold 非选中文本是否加粗
     * @return 当前实例
     */
    public AvlwTabLayout setTabTextConfig(Integer textSizeY, Integer textSizeN, Integer textColorY, Integer textColorN, Boolean isTextYBold,
            Boolean isTextNBold, Boolean isAllowLineTextBaselineAligning) {
        if (textSizeY != null) {
            tabTextPaintY.setTextSize(textSizeY);
        }
        if (textSizeN != null) {
            tabTextPaintN.setTextSize(textSizeN);
        }
        if (textColorY != null) {
            tabTextPaintY.setColor(textColorY);
        }
        if (textColorN != null) {
            tabTextPaintN.setColor(textColorN);
        }
        //加粗配置
        if (isTextYBold != null) {
            if (isTextYBold) {
                tabTextPaintY.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                tabTextPaintY.setTypeface(Typeface.DEFAULT);
            }
        }
        //加粗配置
        if (isTextNBold != null) {
            if (isTextNBold) {
                tabTextPaintN.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                tabTextPaintN.setTypeface(Typeface.DEFAULT);
            }
        }
        //是否允许基线对齐
        if (isAllowLineTextBaselineAligning != null) {
            this.lineTextBaselineAligning = isAllowLineTextBaselineAligning;
        }
        return this;
    }

    /**
     * 下划线数据配置
     *
     * @param lineWidth        下划线宽度,默认和文本宽度一致
     * @param lineWidthPercent 下划线宽度百分比
     * @param lineHeight       下划线高度
     * @param lineBgDrawable   下划线颜色
     * @param lineTextSpace    下划线和文本之间的距离
     * @return 当前实例
     */
    public AvlwTabLayout setLineConfig(Float lineWidth, @FloatRange(from = 0, to = 1) Float lineWidthPercent, Float lineHeight,
            Drawable lineBgDrawable, Float lineTextSpace) {
        if (drawTabLayout instanceof AvlwTabLayoutTypeTextLine) {
            ((AvlwTabLayoutTypeTextLine) drawTabLayout).setLineConfig(lineWidth, lineWidthPercent, lineHeight, lineBgDrawable, lineTextSpace);
        }
        return this;
    }

    /**
     * 下划线容器数据配置
     *
     * @param lineContainerBg        下划线所属容器图片
     * @param lineContainerHeight    下划线容器高度
     * @param lineContainerTextSpace 下划线容器和文本之间的间距
     * @return 当前实例
     */
    public AvlwTabLayout setLineContainerConfig(Drawable lineContainerBg, Float lineContainerHeight, Float lineContainerTextSpace) {
        if (drawTabLayout instanceof AvlwTabLayoutTypeTextLineContainer) {
            ((AvlwTabLayoutTypeTextLineContainer) drawTabLayout).setLineContainerConfig(lineContainerBg, lineContainerHeight, lineContainerTextSpace);
        }
        return this;
    }

    /**
     * 设置文本背景配置
     *
     * @param tabBgWidth             tab背景宽度，默认是文本宽度,但是宽度不能大于tab宽度
     * @param tabBgWidthPercent      tab背景宽度百分比，默认是文本宽度
     * @param tabBgHeight            tab背景高度，默认是文本高度，但是不能小于文字高度
     * @param tabBgTextWidthPadding  tab背景宽度距离文本间距
     * @param tabBgTextHeightPadding tab背景高度距离文本间距
     * @param tabBgY                 tab选中背景
     * @param tabBgN                 tab未选中背景
     * @return 当前实例
     */
    public AvlwTabLayout setTextBgConfig(Float tabBgWidth, @FloatRange(from = 0, to = 1) Float tabBgWidthPercent, Float tabBgHeight,
            Float tabBgTextWidthPadding, Float tabBgTextHeightPadding, Drawable tabBgY, Drawable tabBgN) {
        if (drawTabLayout instanceof AvlwTabLayoutTypeTextBg) {
            ((AvlwTabLayoutTypeTextBg) drawTabLayout).setTextBgConfig(tabBgWidth, tabBgWidthPercent, tabBgHeight, tabBgTextWidthPadding,
                    tabBgTextHeightPadding, tabBgY, tabBgN);
        }
        return this;
    }

    /**
     * 获取绘制的文本y坐标
     *
     * @param isCurrent 是否是当前选中
     * @return 坐标
     */
    private float getDrawTextY(boolean isCurrent) {
        float offset;
        Paint.FontMetrics yFontMetrics = tabTextPaintY.getFontMetrics();
        Paint.FontMetrics nFontMetrics = tabTextPaintN.getFontMetrics();
        float yHeight = yFontMetrics.descent - yFontMetrics.ascent;
        float nHeight = nFontMetrics.descent - nFontMetrics.ascent;
        //如果基线对齐则直接使用最大画笔的偏移
        if (lineTextBaselineAligning) {
            if (tabSizeMaxY) {
                offset = -yFontMetrics.top + (tabHeight - yHeight) / 2;
            } else {
                offset = -nFontMetrics.top + (tabHeight - nHeight) / 2;
            }
        } else {
            //非基线对齐，先判断选中是否是最大的，然后再判断当前是否是选中的
            if (tabSizeMaxY) {
                //选中时最大的，当前如果是选中的话则正常返回否则的话返回计算之后的
                if (isCurrent) {
                    offset = -yFontMetrics.top + (tabHeight - yHeight) / 2;
                } else {
                    offset = -nFontMetrics.top + (yHeight - nHeight) / 2 + (tabHeight - yHeight) / 2;
                }
            } else {
                //选中时最小的，当前如果是选中的话则返回计算的，当前是非选中的话正常返回
                if (isCurrent) {
                    offset = -yFontMetrics.top + (nHeight - yHeight) / 2 + (tabHeight - nHeight) / 2;
                } else {
                    offset = -nFontMetrics.top + (tabHeight - nHeight) / 2;
                }
            }
        }
        return getPaddingTop() + offset;
    }

    /**
     * 修改滑动进度
     *
     * @param isOnTouchChange 是否是触摸修改位置的
     */
    private void changeScrollPercent(Float percent, boolean isOnTouchChange) {
        if (percent.intValue() == 1) {
            this.currentPosition = scrollToPosition;
            //切换结束之后位置处理
            currentPositionChangeFinish(isOnTouchChange);
            lineSlipPercent = 0f;
            allowScrollChange = true;
        } else {
            lineSlipPercent = percent;
        }
        postInvalidate();
    }

    /**
     * 当前位置切换结束处理
     *
     * @param isOnTouchChange 是否是触摸修改位置的
     */
    private void currentPositionChangeFinish(boolean isOnTouchChange) {
        //当前item左边界
        float currentX = currentPosition * tabWidth;
        if (currentX < Math.abs(leftOffset) + tabWidth) {
            //当前目标切换位置在左边界外则需要切换回来
            leftOffset = -currentX;
        } else if (Math.abs(leftOffset) + getWidth() < currentX + tabWidth) {
            //当前目标切换位置在右边界外则需要切换回来
            leftOffset = -(currentX + tabWidth - getWidth());
        }
        if (changeListener != null) {
            changeListener.onChangePosition(isOnTouchChange, tabTextList.size() > currentPosition ? tabTextList.get(currentPosition) : "",
                    currentPosition);
        }
    }

}
