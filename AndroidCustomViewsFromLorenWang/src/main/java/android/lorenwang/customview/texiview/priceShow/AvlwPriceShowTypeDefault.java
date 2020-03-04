package android.lorenwang.customview.texiview.priceShow;

import android.graphics.Canvas;
import android.lorenwang.tools.app.AtlwViewUtils;

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


    @Override
    public int getMeasureWidth(int widthMeasureSpec) {
        //左右边距不要轻易删除，子类要用   宽度值为：金额金额符号宽度加上金额符号间距加上左右内边距
        return (int) (AtlwViewUtils.getInstance().getStrTextWidth(pricePaint, showPrice + priceSymbol)
                + priceSymbolPriceDistance + avlwPriceShowTextView.getPaddingLeft() + avlwPriceShowTextView.getPaddingRight());
    }

    @Override
    public int getMeasureHeight(int heightMeasureSpec) {
        //上下边距不要轻易删除，子类要用
        return (int) AtlwViewUtils.getInstance().getStrTextHeight(pricePaint) + avlwPriceShowTextView.getPaddingTop() + avlwPriceShowTextView.getPaddingBottom();
    }

    @Override
    void onDrawRegion(Canvas canvas, float left, float top, float right, float bottom) {
        onDrawRegion(canvas, left, top, right, bottom, true);
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
            canvas.drawText(priceSymbol, left, top - pricePaint.getFontMetrics().top, pricePaint);
            canvas.drawText(showPrice,
                    left + priceSymbolPriceDistance + AtlwViewUtils.getInstance().getStrTextWidth(pricePaint, priceSymbol)
                    , top - pricePaint.getFontMetrics().top, pricePaint);
        } else {
            canvas.drawText(showPrice, left, top - pricePaint.getFontMetrics().top, pricePaint);
        }
    }

    /**
     * 获取金额实际的显示宽度
     *
     * @return 金额显示宽度，不包含间距什么的
     */
    protected float getPriceShowWidth() {
        return AtlwViewUtils.getInstance().getStrTextWidth(pricePaint, showPrice + priceSymbol);
    }

}
