package android.lorenwang.customview.texiview.priceShow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.lorenwang.customview.R;
import android.lorenwang.tools.app.AtlwViewUtil;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

/**
 * 功能作用：金额显示值货币符号和描述
 * 创建时间：2020-01-02 10:51
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：显示金额、货币符号和描述，描述和金额左右对齐，然后在金额底部
 */

class AvlwPriceShowTypeCurrencySymbolAndDescribe extends AvlwPriceShowTypeCurrencySymbol {
    /**
     * 描述文本
     */
    private String describeText = "";
    /**
     * 描述画笔
     */
    private Paint describePaint;
    /**
     * 描述和文本之间的间距
     */
    private float describePriceDistance = 0;

    /*----------------------------------------绘制参数----------------------------------------*/

    /**
     * 描述显示宽度
     */
    private float describeWidth = 0;
    /**
     * 描述显示高度
     */
    private float describeHeight = 0;
    /**
     * 最大宽度
     */
    private float maxWidth = -1;
    /**
     * 最大高度
     */
    private float maxHeight = -1;

    /**
     * 描述内容行数
     */
    private int describeLines = 1;


    @Override
    void init(Context context, AvlwPriceShowTextView avlwPriceShowTextView, TypedArray attributes) {
        super.init(context, avlwPriceShowTextView, attributes);
        describeText = attributes.getString(R.styleable.AvlwPriceShowTextView_avlwPriceShowDescribe);
        describePriceDistance = attributes.getDimension(R.styleable.AvlwPriceShowTextView_avlwPriceShowDescribePriceDistance, describePriceDistance);
        maxWidth = attributes.getDimension(R.styleable.AvlwPriceShowTextView_avlwPriceShowDescribeMaxWidth, maxWidth);
        maxHeight = attributes.getDimension(R.styleable.AvlwPriceShowTextView_avlwPriceShowDescribeMaxHeight, maxHeight);
        describeText = JtlwCheckVariateUtils.getInstance().isEmpty(describeText) ? "" : describeText;
        //初始化画笔
        describePaint = new Paint();
        describePaint.setAntiAlias(true);
        describePaint.setTextSize(attributes.getDimension(R.styleable.AvlwPriceShowTextView_avlwPriceShowDescribeTextSize, priceSize));
        describePaint.setColor(attributes.getColor(R.styleable.AvlwPriceShowTextView_avlwPriceShowDescribeTextColor, priceColor));


        //描述行数,默认1行
        describeLines = 1;
        //获取文本宽度
        float strTextWidth = AtlwViewUtil.getInstance().getStrTextWidth(describePaint, describeText);
        //获取文本高度,一行高度乘以多行
        float strTextHeight = AtlwViewUtil.getInstance().getStrTextHeight(describePaint);
        //文本实际显示高度
        float strTextShowHeight = strTextHeight * describeLines;
        //比较文本宽度以及边界宽度,如果边界宽度大于文本宽度则以边界宽度为准，如果文本宽度大于边界宽度则将文本宽度
        //和最大宽度做比较，来计算行数
        if (maxWidth > 0) {
            //格式化最大宽度
            maxWidth = Math.max(getPriceShowWidth(), maxWidth);
            //和最大宽度做比较
            if (strTextWidth > maxWidth) {
                describeLines = (int) (strTextWidth % maxWidth > 0 ? (strTextWidth / maxWidth + 1) : strTextWidth / maxWidth);
                strTextShowHeight = strTextHeight * describeLines;
                describeWidth = maxWidth;
                describeHeight = strTextShowHeight;
            } else {
                describeWidth = strTextWidth;
            }
        } else {
            describeWidth = strTextWidth;
        }
        //高度处理
        if (maxHeight > 0) {
            if (strTextShowHeight > maxHeight) {
                describeLines = (int) (strTextHeight / maxHeight);
                //大于最大则裁剪显示数据，计算能够显示的行数
                describeHeight = strTextHeight * describeLines;
            } else {
                describeHeight = maxHeight;
            }
        } else {
            describeHeight = strTextShowHeight;
        }

        //设置金额显示偏移
        setPriceOffset();
    }

    @Override
    public int getMeasureWidth(int widthMeasureSpec) {
        return (int) Math.max(getPriceLeftDistance() + describeWidth
                        + avlwPriceShowTextView.getPaddingRight() + avlwPriceShowTextView.getPaddingLeft()
                , super.getMeasureWidth(widthMeasureSpec));
    }

    @Override
    public int getMeasureHeight(int heightMeasureSpec) {
        return (int) (super.getMeasureHeight(heightMeasureSpec) + describePriceDistance + describeHeight);
    }

    /**
     * 设置描述最大宽度
     *
     * @param maxWidth 最大宽度
     */
    public void setMaxDescribeWidth(float maxWidth) {
        this.maxWidth = maxWidth;
        //设置金额显示偏移
        setPriceOffset();
        avlwPriceShowTextView.postInvalidate();
    }

    /**
     * 设置描述最大高度
     *
     * @param maxHeight 最大高度
     */
    public void setMaxDescribeHeight(float maxHeight) {
        this.maxHeight = maxHeight;
        //设置金额显示偏移
        setPriceOffset();
        avlwPriceShowTextView.postInvalidate();
    }

    /**
     * 设置金额显示偏移
     */
    private void setPriceOffset() {
        //获取金额显示宽度
        float priceShowWidth = getPriceShowWidth();
        //获取新间距
        if (JtlwCheckVariateUtils.getInstance().isEmpty(currencySymbolText)) {
            priceSymbolPriceDistance = Math.max((describeWidth - priceShowWidth) / 2, priceSymbolPriceDistance);
        } else {
            currencySymbolPriceDistance = Math.max((describeWidth - priceShowWidth) / 2, currencySymbolPriceDistance);
        }
    }

    @Override
    void onDrawRegion(Canvas canvas, float left, float top, float right, float bottom) {
        float parentBottom = bottom - describeHeight - describePriceDistance;
        super.onDrawRegion(canvas, left, top, right, parentBottom);
        onDrawText(canvas, left + getPriceLeftDistance(), parentBottom + describePriceDistance, right, bottom, describeText, describeLines);
    }

    /**
     * 绘制文案
     *
     * @param canvas 换班
     * @param left   左边界
     * @param top    上边距
     * @param right  有边界
     * @param bottom 下边界
     * @param text   绘制的文案
     */
    private void onDrawText(Canvas canvas, float left, float top, float right, float bottom, String text, int drawLines) {
        //获取单行文本高度
        float textHeight = AtlwViewUtil.getInstance().getStrTextHeight(describePaint);
        //能够绘制的宽度
        float drawWidth = right - left;
        //获取大致每行显示文本数量
        int count = text.length() / drawLines;
        String drawText = text.substring(0, count);
        //是否允许绘制
        boolean isAllowDraw = false;
        do {
            //获取当前文本宽度
            if (Float.compare(AtlwViewUtil.getInstance().getStrTextWidth(describePaint, drawText), drawWidth) < 0) {
                //当前小于可绘制，判断下一个是否大于可绘制
                if (count == text.length()) {
                    isAllowDraw = true;
                } else if (Float.compare(AtlwViewUtil.getInstance().getStrTextWidth(describePaint, text.substring(0, ++count)), drawWidth) > 0) {
                    drawText = text.substring(0, --count);
                    isAllowDraw = true;
                }
            } else if (drawLines == 1) {
                isAllowDraw = true;
            }

        } while (!isAllowDraw);

        //绘制文本
        canvas.drawText(drawText, left, top - describePaint.getFontMetrics().top, describePaint);
        if (drawLines > 1) {
            onDrawText(canvas, left, top + textHeight, right, bottom, text.substring(count), --drawLines);
        }
    }

}
