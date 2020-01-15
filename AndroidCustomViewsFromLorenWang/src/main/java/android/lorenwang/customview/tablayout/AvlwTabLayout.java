package android.lorenwang.customview.tablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.lorenwang.customview.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;

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

    /** **************************************绘制参数**********************************************/
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

    /** **************************************配置参数**********************************************/
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
        tabSizeMaxY = Float.valueOf(tabTextPaintY.getTextSize()).compareTo(tabTextPaintN.getTextSize()) >= 0;


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
            case SHOW_TYPE_TEXT_BG:
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
                width = getMeasureWidth(this, widthMeasureSpec, tabTextListSize);
            }
            if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                height = getMeasureHeight(this, heightMeasureSpec, tabTextListSize);
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //item部分绘制
        Paint paint;
        String text;
        boolean isCurrent;
        float drawTextX;
        float drawTextY;
        for (int i = 0; i < tabTextList.size(); i++) {
            //当前是否选中
            isCurrent = currentPosition.compareTo(i) == 0;
            paint = isCurrent ? tabTextPaintY : tabTextPaintN;
            text = tabTextList.get(i);
            //获取文本坐标
            drawTextX = getDrawTextX(paint, text, i);
            drawTextY = getDrawTextY(isCurrent);
            canvas.drawText(text, drawTextX, drawTextY, paint);
            //item类型绘制
            drawTypeItem(drawTextX, drawTextY);
        }
        //绘制基础部分
        String currentText = tabTextList.get(currentPosition);
        float currentWidth = tabTextPaintY.measureText(currentText);
        if (currentWidth > tabWidth) {
            currentWidth = tabWidth;
        }
        float startX = getPaddingLeft() + tabWidth * currentPosition + (tabWidth - currentWidth) / 2;
        if (currentPosition.compareTo(scrollToPosition) == 0) {
            drawTabLayout.drawTypeChild(canvas, currentPosition, currentPosition, lineSlipPercent,
                    startX, startX, currentWidth, currentWidth);
        } else {
            String scrollToText = tabTextList.get(scrollToPosition);
            float scrollToWidth = tabTextPaintY.measureText(scrollToText);
            if (scrollToWidth > tabWidth) {
                scrollToWidth = tabWidth;
            }
            float stopX = getPaddingLeft() + tabWidth * scrollToPosition + (tabWidth - scrollToWidth) / 2;
            drawTabLayout.drawTypeChild(canvas, currentPosition,scrollToPosition, lineSlipPercent,
                    startX, stopX, currentWidth, scrollToWidth);
        }
    }

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

    @Override
    public void drawTypeChild(Canvas canvas, Integer currentPosition, Integer scrollToPosition, float lineSlipPercent, float startX, float stopX, float currentWidth, float scrollToWidth) {
        drawTabLayout.drawTypeChild(canvas, currentPosition, this.scrollToPosition, lineSlipPercent, startX, stopX, currentWidth, scrollToWidth);
    }

    @Override
    public void drawTypeItem(float drawTextX, float drawTextY) {
        drawTabLayout.drawTypeItem(drawTextX, drawTextY);
    }

    /**
     * 获取要绘制文本的x轴坐标
     *
     * @param paint 画笔
     * @param text  要绘制的文本
     * @param index 文本在tab中的位置
     * @return x轴坐标
     */
    private float getDrawTextX(Paint paint, String text, int index) {
        float textWidth = paint.measureText(text);
        if (textWidth > tabWidth) {
            textWidth = tabWidth;
        }
        return getPaddingLeft() + tabWidth * index + (tabWidth - textWidth) / 2;
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


}
