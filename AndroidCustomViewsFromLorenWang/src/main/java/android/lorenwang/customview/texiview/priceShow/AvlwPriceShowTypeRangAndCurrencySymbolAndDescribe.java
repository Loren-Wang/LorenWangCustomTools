package android.lorenwang.customview.texiview.priceShow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.lorenwang.customview.R;
import android.lorenwang.tools.app.AtlwViewUtil;

import java.math.BigDecimal;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtil;

/**
 * 功能作用：范围金额、符号、描述
 * 创建时间：2020-12-15 2:23 下午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AvlwPriceShowTypeRangAndCurrencySymbolAndDescribe extends AvlwPriceShowTypeCurrencySymbol {
    /**
     * 描述画笔
     */
    private Paint paintDes;
    /**
     * 金额分隔符画笔
     */
    private Paint paintRangSeparate;

    /**
     * 描述起始
     */
    private String desStart;
    /**
     * 描述结束
     */
    private String desEnd;
    /**
     * 最小金额
     */
    private String priceMin;
    /**
     * 最大金额
     */
    private String priceMax;
    /**
     * 范围金额分隔符
     */
    private String rangSeparate;

    /**
     * 起始描述右侧间距
     */
    private int desStartRightDistance = 0;
    /**
     * 结束描述左侧间距
     */
    private int desEndLeftDistance = 0;
    /**
     * 范围分隔符左右的间距
     */
    private int rangSeparateLeftRightDistance = 0;

    /**
     * 范围时最大金额是否需要金额符号
     */
    private boolean rangMaxNeedCurrencySymbol = false;

    @Override
    void init(Context context, AvlwPriceShowTextView qtPriceShowTextView, TypedArray attributes) {
        super.init(context, qtPriceShowTextView, attributes);
        rangMaxNeedCurrencySymbol = attributes.getBoolean(R.styleable.AvlwPriceShowTextView_avlw_pst_rangMaxNeedCurrencySymbol,
                rangMaxNeedCurrencySymbol);
        rangSeparateLeftRightDistance = attributes.getDimensionPixelOffset(R.styleable.AvlwPriceShowTextView_avlw_pst_rangSeparateLeftRightDistance,
                rangSeparateLeftRightDistance);
        desStartRightDistance = attributes.getDimensionPixelOffset(R.styleable.AvlwPriceShowTextView_avlw_pst_desStartRightDistance,
                desStartRightDistance);
        desEndLeftDistance = attributes.getDimensionPixelOffset(R.styleable.AvlwPriceShowTextView_avlw_pst_desEndLeftDistance, desEndLeftDistance);

        //初始化画笔
        paintDes = new Paint();
        paintDes.setAntiAlias(true);
        paintDes.setTextSize(attributes.getDimension(R.styleable.AvlwPriceShowTextView_avlw_pst_describeTextSize, priceSize));
        paintDes.setColor(attributes.getColor(R.styleable.AvlwPriceShowTextView_avlw_pst_describeTextColor, priceColor));
        if (attributes.getBoolean(R.styleable.AvlwPriceShowTextView_avlw_pst_describeTextBold, false)) {
            paintDes.setTypeface(Typeface.DEFAULT_BOLD);
        }

        rangSeparate = attributes.getString(R.styleable.AvlwPriceShowTextView_avlw_pst_rangSeparate);
        paintRangSeparate = new Paint();
        paintRangSeparate.setTextSize(attributes.getDimension(R.styleable.AvlwPriceShowTextView_avlw_pst_rangSeparateTextSize, priceSize));
        paintRangSeparate.setColor(attributes.getColor(R.styleable.AvlwPriceShowTextView_avlw_pst_rangSeparateTextColor, priceColor));
        if (attributes.getBoolean(R.styleable.AvlwPriceShowTextView_avlw_pst_rangSeparateTextBold, false)) {
            paintRangSeparate.setTypeface(Typeface.DEFAULT_BOLD);
        }

        //初始化设置文本
        setDesInfo(attributes.getString(R.styleable.AvlwPriceShowTextView_avlw_pst_desStart),
                attributes.getString(R.styleable.AvlwPriceShowTextView_avlw_pst_desEnd));
        String priceMin = attributes.getString(R.styleable.AvlwPriceShowTextView_avlw_pst_priceMin);
        String priceMax = attributes.getString(R.styleable.AvlwPriceShowTextView_avlw_pst_priceMax);
        if (JtlwCheckVariateUtil.getInstance().isEmpty(priceMin)) {
            priceMin = attributes.getString(R.styleable.AvlwPriceShowTextView_avlw_pst_priceText);
        }
        setRangPrice(JtlwCheckVariateUtil.getInstance().isNotEmpty(priceMin) ? new BigDecimal(priceMin) : null,
                JtlwCheckVariateUtil.getInstance().isNotEmpty(priceMax) ? new BigDecimal(priceMax) : null);
    }

    /**
     * 设置描述信息
     *
     * @param start 起始文本
     * @param end   结束文本
     */
    public void setDesInfo(String start, String end) {
        desStart = start;
        desEnd = end;
    }

    /**
     * 设置价格区间
     *
     * @param priceMin 最小金额
     * @param priceMax 最大金额
     */
    public void setRangPrice(BigDecimal priceMin, BigDecimal priceMax) {
        if (priceMin == null && priceMax == null) {
            this.priceMin = null;
            this.priceMax = null;
        } else if (priceMin != null && priceMax == null) {
            this.priceMin = formatPrice(priceMin);
            this.priceMax = null;
        } else if (priceMin == null) {
            this.priceMin = formatPrice(priceMax);
            this.priceMax = null;
        } else if (priceMin.compareTo(priceMax) > 0) {
            this.priceMin = formatPrice(priceMax);
            this.priceMax = formatPrice(priceMin);
        } else if (priceMin.compareTo(priceMax) == 0) {
            this.priceMin = formatPrice(priceMin);
            this.priceMax = null;
        } else {
            this.priceMin = formatPrice(priceMin);
            this.priceMax = formatPrice(priceMax);
        }
    }

    /**
     * 设置金额
     *
     * @param price 金额
     */
    @Override
    void setPrice(BigDecimal price) {
        setRangPrice(price, null);
    }

    @Override
    public void setPriceColor(Integer color) {
        super.setPriceColor(color);
       if(color != null){
           paintDes.setColor(color);
           paintRangSeparate.setColor(color);
       }
    }

    /**
     * 获取布局绘制宽度
     *
     * @param widthMeasureSpec 原始宽度
     * @return 绘制宽度
     */
    @Override
    public int getMeasureWidth(int widthMeasureSpec) {
        float showWidth = 0;
        //起始描述
        if (JtlwCheckVariateUtil.getInstance().isNotEmpty(desStart)) {
            showWidth += AtlwViewUtil.getInstance().getStrTextWidth(paintDes, desStart);
        }
        //间距
        showWidth += desStartRightDistance;
        //起始金额
        if (JtlwCheckVariateUtil.getInstance().isNotEmpty(priceMin)) {
            //顶部的，则为符号和数据的最大值
            if (currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_TL ||
                    currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_TR ||
                    currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_BL ||
                    currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_BR) {
                showWidth += Math.max(
                        AtlwViewUtil.getInstance().getStrTextWidth(pricePaint, priceMin),
                        AtlwViewUtil.getInstance().getStrTextWidth(currencySymbolPaint, currencySymbolText)
                );
            } else {
                showWidth += AtlwViewUtil.getInstance().getStrTextWidth(pricePaint, priceMin)
                        + AtlwViewUtil.getInstance().getStrTextWidth(currencySymbolPaint, currencySymbolText)
                        + currencySymbolPriceDistance;
            }
        }
        //结束金额
        if (JtlwCheckVariateUtil.getInstance().isNotEmpty(priceMax)) {
            //分隔符
            showWidth += rangSeparateLeftRightDistance * 2 + AtlwViewUtil.getInstance().getStrTextWidth(paintRangSeparate, rangSeparate);
            if (rangMaxNeedCurrencySymbol) {
                //顶部的，则为符号和数据的最大值
                if (currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_TL ||
                        currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_TR ||
                        currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_BL ||
                        currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_BR) {
                    showWidth += Math.max(
                            AtlwViewUtil.getInstance().getStrTextWidth(pricePaint, priceMax),
                            AtlwViewUtil.getInstance().getStrTextWidth(currencySymbolPaint, currencySymbolText)
                    );
                } else {
                    showWidth += AtlwViewUtil.getInstance().getStrTextWidth(pricePaint, priceMax)
                            + AtlwViewUtil.getInstance().getStrTextWidth(currencySymbolPaint, currencySymbolText)
                            + currencySymbolPriceDistance;
                }
            } else {
                showWidth += AtlwViewUtil.getInstance().getStrTextWidth(pricePaint, priceMax) + currencySymbolPriceDistance;
            }
        }
        //结束描述以及间距
        showWidth += desEndLeftDistance;
        if (JtlwCheckVariateUtil.getInstance().isNotEmpty(desEnd)) {
            showWidth += AtlwViewUtil.getInstance().getStrTextWidth(paintDes, desEnd);
        }

        return (int) showWidth;
    }

    /**
     * 获取布局绘制高度
     *
     * @param heightMeasureSpec 原始高度
     * @return 绘制高度
     */
    @Override
    public int getMeasureHeight(int heightMeasureSpec) {
        float showHeight = 0;
        if (JtlwCheckVariateUtil.getInstance().isNotEmpty(desStart) || JtlwCheckVariateUtil.getInstance().isNotEmpty(desEnd)) {
            showHeight = Math.max(showHeight, AtlwViewUtil.getInstance().getStrTextHeight(paintDes));
        }
        if (JtlwCheckVariateUtil.getInstance().isNotEmpty(priceMin) || JtlwCheckVariateUtil.getInstance().isNotEmpty(priceMax)) {
            showHeight = Math.max(showHeight, Math.abs(pricePaint.getFontMetrics().ascent));
        }
        if (JtlwCheckVariateUtil.getInstance().isNotEmpty(currencySymbolText)) {
            if (currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_TL ||
                    currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_TR ||
                    currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_BL ||
                    currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_BR) {
                showHeight = Math.max(showHeight, AtlwViewUtil.getInstance().getStrTextHeight(currencySymbolPaint) +
                        currencySymbolPriceDistance + AtlwViewUtil.getInstance().getStrTextHeight(pricePaint));
            } else {
                showHeight = Math.max(showHeight, AtlwViewUtil.getInstance().getStrTextHeight(currencySymbolPaint));
            }
        } else {
            showHeight = Math.max(showHeight, AtlwViewUtil.getInstance().getStrTextHeight(currencySymbolPaint));
        }
        if (JtlwCheckVariateUtil.getInstance().isNotEmpty(rangSeparate)) {
            showHeight = Math.max(showHeight, AtlwViewUtil.getInstance().getStrTextHeight(paintRangSeparate));
        }
        return (int) (showHeight);
    }

    /**
     * 指定区域绘制
     *
     * @param canvas 画板
     * @param left   区域左侧坐标
     * @param top    区域顶部坐标
     * @param right  区域右侧坐标
     * @param bottom 区域底部坐标
     */
    @Override
    void onDrawRegion(Canvas canvas, float left, float top, float right, float bottom) {
        float drawLeft = left;
        //绘制开始描述
        if (JtlwCheckVariateUtil.getInstance().isNotEmpty(desStart)) {
            canvas.drawText(desStart, drawLeft, bottom - paintDes.getFontMetrics().bottom, paintDes);
            drawLeft += AtlwViewUtil.getInstance().getStrTextWidth(paintDes, desStart);
        }
        drawLeft += desStartRightDistance;
        //绘制左侧金额符号
        if (JtlwCheckVariateUtil.getInstance().isNotEmpty(currencySymbolText)) {
            drawLeft = drawCurrencySymbol(canvas, drawLeft, bottom, priceMin);
        }
        //绘制最小金额
        if (JtlwCheckVariateUtil.getInstance().isNotEmpty(priceMin)) {
            drawLeft = drawPrice(canvas, drawLeft, bottom - currencySymbolPaint.getFontMetrics().bottom, priceMin);
        }
        //右侧时需要drawLeft加上符号宽度
        if (currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_RB || currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_RT) {
            drawLeft += AtlwViewUtil.getInstance().getStrTextWidth(currencySymbolPaint, currencySymbolText) + currencySymbolPriceDistance;
        }
        //绘制最大金额
        if (JtlwCheckVariateUtil.getInstance().isNotEmpty(priceMax)) {
            //绘制分隔符
            if (JtlwCheckVariateUtil.getInstance().isNotEmpty(rangSeparate)) {
                drawLeft += rangSeparateLeftRightDistance;
                if (currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_BL || currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_BR) {
                    canvas.drawText(rangSeparate, drawLeft, bottom - paintRangSeparate.getFontMetrics().bottom - rangSeparateLeftRightDistance -
                            AtlwViewUtil.getInstance().getStrTextHeight(currencySymbolPaint), paintRangSeparate);
                } else {
                    canvas.drawText(rangSeparate, drawLeft, bottom - paintRangSeparate.getFontMetrics().bottom, paintRangSeparate);
                }

                drawLeft += AtlwViewUtil.getInstance().getStrTextWidth(paintRangSeparate, rangSeparate) + rangSeparateLeftRightDistance;
            }
            //绘制最大金额符号
            if (JtlwCheckVariateUtil.getInstance().isNotEmpty(currencySymbolText) && rangMaxNeedCurrencySymbol) {
                drawLeft = drawCurrencySymbol(canvas, drawLeft, bottom, priceMax);
            }
            //绘制最大金额
            if (JtlwCheckVariateUtil.getInstance().isNotEmpty(priceMax)) {
                drawLeft = drawPrice(canvas, drawLeft, bottom - currencySymbolPaint.getFontMetrics().bottom, priceMax);
            }
        }
        //右侧时需要drawLeft加上符号宽度
        if (currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_RB || currencySymbolLocation == CURRENCY_SYMBOL_LOCATION_RT) {
            if (rangMaxNeedCurrencySymbol) {
                drawLeft += currencySymbolPriceDistance;
            }
        }
        drawLeft += desEndLeftDistance;
        //绘制结束描述
        if (JtlwCheckVariateUtil.getInstance().isNotEmpty(desEnd)) {
            canvas.drawText(desEnd, drawLeft, bottom - paintDes.getFontMetrics().bottom, paintDes);
        }
    }


}
