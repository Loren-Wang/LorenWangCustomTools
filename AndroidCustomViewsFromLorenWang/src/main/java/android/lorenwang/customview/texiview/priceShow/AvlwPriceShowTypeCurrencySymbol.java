package android.lorenwang.customview.texiview.priceShow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.lorenwang.customview.R;
import android.lorenwang.tools.app.AtlwViewUtils;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

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
    private final int CURRENCY_SYMBOL_LOCATION_LT = 0;
    /**
     * 左下,底部齐平
     */
    private final int CURRENCY_SYMBOL_LOCATION_LB = 1;
    /**
     * 上左，左侧齐平
     */
    private final int CURRENCY_SYMBOL_LOCATION_TL = 2;
    /**
     * 下左，左侧齐平
     */
    private final int CURRENCY_SYMBOL_LOCATION_BL = 3;
    /**
     * 右上，顶部齐平
     */
    private final int CURRENCY_SYMBOL_LOCATION_RT = 4;
    /**
     * 右下，底部齐平
     */
    private final int CURRENCY_SYMBOL_LOCATION_RB = 5;
    /**
     * 上右，右侧齐平
     */
    private final int CURRENCY_SYMBOL_LOCATION_TR = 6;
    /**
     * 下右，右侧齐平
     */
    private final int CURRENCY_SYMBOL_LOCATION_BR = 7;
    /**
     * 符号位置，默认左下，底部齐平
     */
    private int currencySymbolLocation = CURRENCY_SYMBOL_LOCATION_LB;

    /**
     * 金额符号文本
     */
    protected String currencySymbolText = "¥";
    /**
     * 金额符号画笔
     */
    protected Paint currencySymbolPaint;
    /**
     * 金额符号和文本之间的间距
     */
    protected float currencySymbolPriceDistance = 0;

    @Override
    void init(Context context, AvlwPriceShowTextView avlwPriceShowTextView, TypedArray attributes) {
        super.init(context, avlwPriceShowTextView, attributes);
        currencySymbolText = attributes.getString(R.styleable.AvlwPriceShowTextView_avlwPriceShowCurrencySymbol);
        currencySymbolLocation = attributes.getInt(R.styleable.AvlwPriceShowTextView_avlwPriceShowCurrencySymbolLocation, currencySymbolLocation);
        currencySymbolPriceDistance = attributes.getDimension(R.styleable.AvlwPriceShowTextView_avlwPriceShowCurrencySymbolPriceDistance, currencySymbolPriceDistance);
        currencySymbolText = JtlwCheckVariateUtils.getInstance().isEmpty(currencySymbolText) ? "¥" : currencySymbolText;
        //初始化画笔
        currencySymbolPaint = new Paint();
        currencySymbolPaint.setAntiAlias(true);
        currencySymbolPaint.setTextSize(attributes.getDimension(R.styleable.AvlwPriceShowTextView_avlwPriceShowCurrencySymbolTextSize, priceSize));
        currencySymbolPaint.setColor(attributes.getColor(R.styleable.AvlwPriceShowTextView_avlwPriceShowCurrencySymbolTextColor, priceColor));
    }

    @Override
    public int getMeasureWidth(int widthMeasureSpec) {
        float strTextWidth = AtlwViewUtils.getInstance().getStrTextWidth(currencySymbolPaint, currencySymbolText);
        //顶部的，则为符号和数据的最大值
        if (currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_TL ||
                currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_TR ||
                currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_BL ||
                currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_BR) {
            return (int) Math.max(
                    super.getMeasureWidth(widthMeasureSpec),
                    avlwPriceShowTextView.getPaddingLeft() + avlwPriceShowTextView.getPaddingRight() + strTextWidth
            );
        } else {
            //super中本身已经加了左右内边距，所以此时直接加上宽度以及间距
            return (int) (super.getMeasureWidth(widthMeasureSpec) + strTextWidth + currencySymbolPriceDistance);
        }
    }

    @Override
    public int getMeasureHeight(int heightMeasureSpec) {
        float strTextHeight = AtlwViewUtils.getInstance().getStrTextHeight(currencySymbolPaint);
        //顶部的，则为符号、金额、间距的集合，因为super中已经包含了上下内边距，所以这里不加
        if (currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_TL ||
                currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_TR ||
                currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_BL ||
                currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_BR) {
            return (int) (super.getMeasureHeight(heightMeasureSpec) + strTextHeight + currencySymbolPriceDistance);
        } else {
            return (int) Math.max(
                    super.getMeasureHeight(heightMeasureSpec),
                    avlwPriceShowTextView.getPaddingTop() + avlwPriceShowTextView.getPaddingBottom() + strTextHeight
            );
        }
    }

    @Override
    void onDrawRegion(Canvas canvas, float left, float top, float right, float bottom) {
        //货币符号高度
        float strTextHeight = AtlwViewUtils.getInstance().getStrTextHeight(currencySymbolPaint);
        //货币符号宽度
        float strTextWidth = AtlwViewUtils.getInstance().getStrTextWidth(currencySymbolPaint, currencySymbolText);
        //左侧已被使用距离
        float useLeft;
        Paint.FontMetrics pricePaintFm = pricePaint.getFontMetrics();
        Paint.FontMetrics currencySymbolFm = currencySymbolPaint.getFontMetrics();
        //绘制符号
        switch (currencySymbolLocation) {
            case CURRENCY_SYMBOL_LOCATION_BL://下左，左侧对齐
                canvas.drawText(currencySymbolText, left, bottom - currencySymbolFm.bottom, currencySymbolPaint);
                super.onDrawRegion(canvas, left, top, right, bottom - strTextHeight);
                break;
            case CURRENCY_SYMBOL_LOCATION_BR://下右，右侧对齐
                canvas.drawText(currencySymbolText, right - strTextWidth, bottom - currencySymbolFm.bottom, currencySymbolPaint);
                super.onDrawRegion(canvas, left, top, right, bottom - strTextHeight);
                break;
            case CURRENCY_SYMBOL_LOCATION_LB://左下，底部对齐，金额符号在货币符号左侧，相应数据右移
                //线绘制金额符号
                canvas.drawText(priceSymbol, left, top - pricePaintFm.top, pricePaint);
                //计算左侧已使用距离
                useLeft = left + priceSymbolPriceDistance + AtlwViewUtils.getInstance().getStrTextWidth(pricePaint, priceSymbol);
                //带间距的绘制货币符号
                canvas.drawText(currencySymbolText, useLeft, bottom - pricePaintFm.bottom, currencySymbolPaint);
                //绘制父级
                super.onDrawRegion(canvas, useLeft + currencySymbolPriceDistance + strTextWidth,
                        top, right, bottom, false);
                break;
            case CURRENCY_SYMBOL_LOCATION_LT://左上，顶部对齐，金额符号在货币符号左侧，相应数据右移
                //线绘制金额符号
                canvas.drawText(priceSymbol, left, top - pricePaintFm.top, pricePaint);
                //计算左侧已使用距离
                useLeft = left + priceSymbolPriceDistance + AtlwViewUtils.getInstance().getStrTextWidth(pricePaint, priceSymbol);
                //带间距的绘制货币符号,顶部计算为金额top-内容高于基线顶部的负值为距离顶部的距离，在加上货币符号高于其基线的实际距离
                canvas.drawText(currencySymbolText, useLeft, -(pricePaintFm.top - pricePaintFm.ascent + currencySymbolFm.top), currencySymbolPaint);
                //绘制父级
                super.onDrawRegion(canvas, useLeft + currencySymbolPriceDistance + strTextWidth,
                        top, right, bottom, false);
                break;
            case CURRENCY_SYMBOL_LOCATION_RB:
                //线绘制金额符号
                canvas.drawText(priceSymbol, left, top - pricePaintFm.top, pricePaint);
                //带间距的绘制货币符号,顶部计算为金额top-内容高于基线顶部的负值为距离顶部的距离，在加上货币符号高于其基线的实际距离
                canvas.drawText(currencySymbolText, right - strTextWidth, bottom - pricePaintFm.bottom, currencySymbolPaint);
                //绘制父级
                super.onDrawRegion(canvas, left, top, right, bottom);
                break;
            case CURRENCY_SYMBOL_LOCATION_RT:
                //线绘制金额符号
                canvas.drawText(priceSymbol, left, top - pricePaintFm.top, pricePaint);
                //带间距的绘制货币符号,顶部计算为金额top-内容高于基线顶部的负值为距离顶部的距离，在加上货币符号高于其基线的实际距离
                canvas.drawText(currencySymbolText, right - strTextWidth, -(pricePaintFm.top - pricePaintFm.ascent + currencySymbolFm.top), currencySymbolPaint);
                //绘制父级
                super.onDrawRegion(canvas, left, top, right, bottom);
                break;
            case CURRENCY_SYMBOL_LOCATION_TL://上左，左侧对齐
                canvas.drawText(currencySymbolText, left, top - currencySymbolFm.top, currencySymbolPaint);
                super.onDrawRegion(canvas, left, top + strTextHeight, right, bottom);
                break;
            case CURRENCY_SYMBOL_LOCATION_TR://上右，右侧对齐
                canvas.drawText(currencySymbolText, right - strTextWidth, top - currencySymbolFm.top, currencySymbolPaint);
                super.onDrawRegion(canvas, left, top + strTextHeight, right, bottom);
                break;
            default:
                break;
        }
    }


    /**
     * 获取金额左侧距离
     *
     * @return 距离
     */
    protected float getPriceLeftDistance() {
        if (currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_LB || currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_LT
                || currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_RB || currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_RT) {
            return priceSymbolPriceDistance + AtlwViewUtils.getInstance().getStrTextWidth(pricePaint, priceSymbol)
                    + AtlwViewUtils.getInstance().getStrTextWidth(currencySymbolPaint, currencySymbolText);
        } else {
            return priceSymbolPriceDistance + AtlwViewUtils.getInstance().getStrTextWidth(pricePaint, priceSymbol);
        }
    }

    /**
     * 获取金额显示的实际宽度
     *
     * @return 金额显示实际宽度
     */
    @Override
    protected float getPriceShowWidth() {
        return AtlwViewUtils.getInstance().getStrTextWidth(pricePaint, showPrice);
    }
}
