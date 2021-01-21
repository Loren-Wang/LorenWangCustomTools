package android.lorenwang.customview.texiview.priceShow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.lorenwang.customview.R;
import android.lorenwang.tools.app.AtlwViewUtil;

import org.jetbrains.annotations.NotNull;

/**
 * 功能作用：金额显示类型之货币符号
 * 创建时间：2020-01-02 10:50
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：仅显示金额和货币符号
 * <!--货币符号-->
 * <attr name="currencySymbol" format="string" />
 * <!--金额符号在金额的位置-->
 * <attr name="currencySymbolLocation" format="enum">
 * <!--左上,顶部齐平-->
 * <enum name="leftTop" value="0" />
 * <!--左下,底部齐平-->
 * <enum name="leftBottom" value="1" />
 * <!--上左，左侧齐平-->
 * <enum name="topLeft" value="2" />
 * <!--下左，左侧齐平-->
 * <enum name="bottomLeft" value="3" />
 * <!--右上，顶部齐平-->
 * <enum name="rightTop" value="4" />
 * <!--右下，底部齐平-->
 * <enum name="rightBottom" value="5" />
 * <!--上右，右侧齐平-->
 * <enum name="topRight" value="6" />
 * <!--下右，右侧齐平-->
 * <enum name="bottomRight" value="7" />
 * </attr>
 * <!--金额符号和文本之间的间距-->
 * <attr name="currencySymbolPriceDistance" format="dimension" />
 * <!--金额符号文本大小-->
 * <attr name="currencySymbolTextSize" format="dimension" />
 * <!--金额符号文本颜色-->
 * <attr name="currencySymbolTextColor" format="color" />
 */

class AvlwPriceShowTypeCurrencySymbol extends AvlwPriceShowTypeDefault {
    /**
     * 左上,顶部齐平
     */
    protected final int CURRENCY_SYMBOL_LOCATION_LT = 0;
    /**
     * 左下,底部齐平
     */
    protected final int CURRENCY_SYMBOL_LOCATION_LB = 1;
    /**
     * 上左，左侧齐平
     */
    protected final int CURRENCY_SYMBOL_LOCATION_TL = 2;
    /**
     * 下左，左侧齐平
     */
    protected final int CURRENCY_SYMBOL_LOCATION_BL = 3;
    /**
     * 右上，顶部齐平
     */
    protected final int CURRENCY_SYMBOL_LOCATION_RT = 4;
    /**
     * 右下，底部齐平
     */
    protected final int CURRENCY_SYMBOL_LOCATION_RB = 5;
    /**
     * 上右，右侧齐平
     */
    protected final int CURRENCY_SYMBOL_LOCATION_TR = 6;
    /**
     * 下右，右侧齐平
     */
    protected final int CURRENCY_SYMBOL_LOCATION_BR = 7;
    /**
     * 符号位置，默认左下，底部齐平
     */
    protected int currencySymbolLocation = CURRENCY_SYMBOL_LOCATION_LB;

    /**
     * 金额单位文本
     */
    protected String currencySymbolText;
    /**
     * 金额单位画笔
     */
    protected Paint currencySymbolPaint;
    /**
     * 金额单位和文本之间的间距
     */
    protected float currencySymbolPriceDistance = 0;

    @Override
    void init(Context context, AvlwPriceShowTextView qtPriceShowTextView, TypedArray attributes) {
        super.init(context, qtPriceShowTextView, attributes);
        //符号文本
        currencySymbolText = attributes.getString(R.styleable.AvlwPriceShowTextView_avlwCurrencySymbol);
        currencySymbolText = currencySymbolText == null ? "￥" : currencySymbolText;
        //符号要显示的位置
        currencySymbolLocation = attributes.getInt(R.styleable.AvlwPriceShowTextView_avlwCurrencySymbolLocation, currencySymbolLocation);
        //符号和价格的间距
        currencySymbolPriceDistance = attributes.getDimension(R.styleable.AvlwPriceShowTextView_avlwCurrencySymbolPriceDistance,
                currencySymbolPriceDistance);

        //初始化画笔
        currencySymbolPaint = new Paint();
        currencySymbolPaint.setAntiAlias(true);
        currencySymbolPaint.setTextSize(attributes.getDimension(R.styleable.AvlwPriceShowTextView_avlwCurrencySymbolTextSize, priceSize));
        currencySymbolPaint.setColor(attributes.getColor(R.styleable.AvlwPriceShowTextView_avlwCurrencySymbolTextColor, priceColor));
        if (attributes.getBoolean(R.styleable.AvlwPriceShowTextView_avlwCurrencySymbolTextBold, false)) {
            currencySymbolPaint.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    @Override
    public int getMeasureWidth(int widthMeasureSpec) {
        //符号文本宽度
        float strTextWidth = AtlwViewUtil.getInstance().getStrTextWidth(currencySymbolPaint, currencySymbolText);
        //顶部的，则为符号和数据的最大值
        if (currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_TL || currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_TR ||
                currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_BL || currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_BR) {
            return (int) Math.max(super.getMeasureWidth(widthMeasureSpec), strTextWidth);
        } else {
            //super中本身已经加了左右内边距，所以此时直接加上宽度以及间距
            return (int) (super.getMeasureWidth(widthMeasureSpec) + strTextWidth + currencySymbolPriceDistance);
        }
    }

    @Override
    public int getMeasureHeight(int heightMeasureSpec) {
        //符号文本高度
        float strTextHeight = AtlwViewUtil.getInstance().getStrTextHeight(currencySymbolPaint);
        if (currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_TL || currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_TR ||
                currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_BL || currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_BR) {
            return (int) (super.getMeasureHeight(heightMeasureSpec) + strTextHeight + currencySymbolPriceDistance);
        } else {
            return (int) Math.max(super.getMeasureHeight(heightMeasureSpec), strTextHeight);
        }
    }

    @Override
    void onDrawRegion(Canvas canvas, float left, float top, float right, float bottom) {
        //绘制金额符号
        left = drawPriceSymbol(canvas, left, bottom - pricePaint.getFontMetricsInt().bottom);
        //绘制金额单位
        left = drawCurrencySymbol(canvas, left, bottom, showPrice);
        //绘制金额
        drawPrice(canvas, left, bottom - currencySymbolPaint.getFontMetricsInt().bottom, showPrice);
    }

    /**
     * 设置金额单位颜色
     *
     * @param color 颜色
     */
    protected void setSymbolTextColor(Integer color) {
        if (color != null) {
            currencySymbolPaint.setColor(color);
        }
    }

    /**
     * 绘制金额单位
     *
     * @param canvas 画板
     * @param left   左侧坐标
     * @param bottom 底部坐标
     * @param price  要绘制的金额，可能为空
     * @return 绘制后下一个位置的左侧坐标
     */
    protected float drawCurrencySymbol(Canvas canvas, float left, float bottom, String price) {
        float currencySymbolWidth = AtlwViewUtil.getInstance().getStrTextWidth(currencySymbolPaint, currencySymbolText);
        float currencySymbolHeight = AtlwViewUtil.getInstance().getStrTextHeight(currencySymbolPaint);
        float priceWidth = AtlwViewUtil.getInstance().getStrTextWidth(pricePaint, price);
        float priceHeight = AtlwViewUtil.getInstance().getStrTextHeight(pricePaint);
        switch (currencySymbolLocation) {
            case CURRENCY_SYMBOL_LOCATION_TL:
                canvas.drawText(currencySymbolText, left,
                        bottom - priceHeight - currencySymbolPriceDistance - currencySymbolPaint.getFontMetrics().bottom, currencySymbolPaint);
                break;
            case CURRENCY_SYMBOL_LOCATION_TR:
                canvas.drawText(currencySymbolText, left + priceWidth - currencySymbolWidth,
                        bottom - priceHeight - currencySymbolPriceDistance - currencySymbolPaint.getFontMetrics().bottom, currencySymbolPaint);
                break;
            case CURRENCY_SYMBOL_LOCATION_BL:
                canvas.drawText(currencySymbolText, left, bottom - currencySymbolPaint.getFontMetrics().bottom, currencySymbolPaint);
                break;
            case CURRENCY_SYMBOL_LOCATION_BR:
                canvas.drawText(currencySymbolText, left + priceWidth - currencySymbolWidth, bottom - currencySymbolPaint.getFontMetrics().bottom,
                        currencySymbolPaint);
                break;
            case CURRENCY_SYMBOL_LOCATION_LB:
                canvas.drawText(currencySymbolText, left, bottom - currencySymbolPaint.getFontMetrics().bottom, currencySymbolPaint);
                left += AtlwViewUtil.getInstance().getStrTextWidth(currencySymbolPaint, currencySymbolText);
                break;
            case CURRENCY_SYMBOL_LOCATION_LT:
                canvas.drawText(currencySymbolText, left,
                        bottom - AtlwViewUtil.getInstance().getStrTextHeight(pricePaint) - currencySymbolPaint.getFontMetrics().top,
                        currencySymbolPaint);
                left += AtlwViewUtil.getInstance().getStrTextWidth(currencySymbolPaint, currencySymbolText);
                break;
            case CURRENCY_SYMBOL_LOCATION_RB:
                canvas.drawText(currencySymbolText,
                        left + AtlwViewUtil.getInstance().getStrTextWidth(pricePaint, price) + currencySymbolPriceDistance,
                        bottom - currencySymbolPaint.getFontMetrics().bottom, currencySymbolPaint);
                break;
            case CURRENCY_SYMBOL_LOCATION_RT:
                canvas.drawText(currencySymbolText,
                        left + AtlwViewUtil.getInstance().getStrTextWidth(pricePaint, price) + currencySymbolPriceDistance,
                        bottom - AtlwViewUtil.getInstance().getStrTextHeight(pricePaint) - currencySymbolPaint.getFontMetrics().top,
                        currencySymbolPaint);
                break;
            default:
                break;
        }
        return left;
    }

    @Override
    public void setPriceColor(Integer color) {
        super.setPriceColor(color);
        setSymbolTextColor(color);
    }

    /**
     * 绘制金额价格
     *
     * @param canvas    画板
     * @param left      左侧
     * @param baseLine  绘制基线
     * @param showPrice 显示价格
     * @return 绘制后左侧坐标
     */
    @Override
    protected float drawPrice(Canvas canvas, float left, float baseLine, @NotNull String showPrice) {
        switch (currencySymbolLocation) {
            case CURRENCY_SYMBOL_LOCATION_BL:
            case CURRENCY_SYMBOL_LOCATION_BR:
                baseLine -= AtlwViewUtil.getInstance().getStrTextHeight(currencySymbolPaint) + currencySymbolPriceDistance;
                break;
            case CURRENCY_SYMBOL_LOCATION_LB:
            case CURRENCY_SYMBOL_LOCATION_LT:
                left += currencySymbolPriceDistance;
                break;
            case CURRENCY_SYMBOL_LOCATION_RB:
            case CURRENCY_SYMBOL_LOCATION_RT:
            case CURRENCY_SYMBOL_LOCATION_TL:
            case CURRENCY_SYMBOL_LOCATION_TR:
            default:
                break;
        }
        return super.drawPrice(canvas, left, baseLine, showPrice);
    }

    @Override
    protected float drawPriceSymbol(Canvas canvas, float left, float baseLine) {
        switch (currencySymbolLocation) {
            case CURRENCY_SYMBOL_LOCATION_BL:
            case CURRENCY_SYMBOL_LOCATION_BR:
                baseLine -= AtlwViewUtil.getInstance().getStrTextHeight(currencySymbolPaint) + currencySymbolPriceDistance;
                break;
            case CURRENCY_SYMBOL_LOCATION_LB:
            case CURRENCY_SYMBOL_LOCATION_LT:
            case CURRENCY_SYMBOL_LOCATION_RB:
            case CURRENCY_SYMBOL_LOCATION_RT:
            case CURRENCY_SYMBOL_LOCATION_TL:
            case CURRENCY_SYMBOL_LOCATION_TR:
            default:
                break;
        }
        return super.drawPriceSymbol(canvas, left, baseLine);
    }
}
