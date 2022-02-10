package android.lorenwang.customview.texiview.priceShow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.lorenwang.customview.R;
import android.lorenwang.tools.app.AtlwViewUtil;

import java.math.BigDecimal;

import javabase.lorenwang.tools.JtlwMatchesRegularCommon;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtil;

/**
 * 功能作用：默认金额显示类型
 * 创建时间：2020-01-02 10:48
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：仅显示金额，其他均不显示
 */

class AvlwPriceShowTypeDefault extends AvlwPriceShowTypeBase {

    /**
     * 显示的金额
     */
    protected String showPrice = "";

    @Override
    void init(Context context, AvlwPriceShowTextView qtPriceShowTextView, TypedArray attributes) {
        super.init(context, qtPriceShowTextView, attributes);
        //格式化金额显示
        String priceText = attributes.getString(R.styleable.AvlwPriceShowTextView_avlw_pst_priceText);
        if (priceText != null && priceText.matches(JtlwMatchesRegularCommon.EXP_BIGDECIMAL)) {
            setPrice(new BigDecimal(priceText));
        } else {
            setPrice(null);
        }
    }

    @Override
    public int getMeasureWidth(int widthMeasureSpec) {
        return (int) getPriceShowWidth();
    }

    @Override
    public int getMeasureHeight(int heightMeasureSpec) {
        return (int) AtlwViewUtil.getInstance().getStrTextHeight(pricePaint);
    }

    @Override
    void onDrawRegion(Canvas canvas, float left, float top, float right, float bottom) {
        onDrawRegion(canvas, left, top, right, bottom, true);
    }

    /**
     * 设置金额
     *
     * @param price 金额
     */
    @Override
    void setPrice(BigDecimal price) {
        if (price != null) {
            showPrice = formatPrice(price);
        } else {
            showPrice = "";
        }
    }

    /**
     * 绘制数据
     *
     * @param canvas          画板
     * @param left            左侧区域坐标
     * @param top             顶部坐标
     * @param right           右侧坐标
     * @param bottom          底部坐标
     * @param drawPriceSymbol 是否绘制金额符号
     */
    protected void onDrawRegion(Canvas canvas, float left, float top, float right, float bottom, boolean drawPriceSymbol) {
        if (drawPriceSymbol) {
            drawPriceSymbol(canvas, left, bottom - pricePaint.getFontMetricsInt().bottom);
        }
        drawPrice(canvas, left + priceSymbolPriceDistance + AtlwViewUtil.getInstance().getStrTextWidth(pricePaint, priceSymbol),
                bottom - pricePaint.getFontMetricsInt().descent, showPrice);
    }

    /**
     * 获取金额实际的显示宽度
     *
     * @return 金额显示宽度，不包含间距什么的
     */
    protected float getPriceShowWidth() {
        return (int) (AtlwViewUtil.getInstance().getStrTextWidth(pricePaint, showPrice + priceSymbol) + priceSymbolPriceDistance);
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
    protected float drawPrice(Canvas canvas, float left, float baseLine, String showPrice) {
        if (JtlwCheckVariateUtil.getInstance().isNotEmpty(showPrice)) {
            priceBaseLine = (int) baseLine;
            if (priceBaseLine < 0) {
                textBaseLine = priceBaseLine;
            }
            canvas.drawText(showPrice, left, baseLine, pricePaint);
            left += AtlwViewUtil.getInstance().getStrTextWidth(pricePaint, showPrice);
        }
        return left;
    }

    /**
     * 绘制金额符号
     *
     * @param canvas   画板
     * @param left     左侧坐标
     * @param baseLine 绘制基线
     * @return 绘制后左侧坐标
     */
    protected float drawPriceSymbol(Canvas canvas, float left, float baseLine) {
        canvas.drawText(priceSymbol, left, baseLine, pricePaint);
        return left + AtlwViewUtil.getInstance().getStrTextWidth(pricePaint, priceSymbol);
    }

}
