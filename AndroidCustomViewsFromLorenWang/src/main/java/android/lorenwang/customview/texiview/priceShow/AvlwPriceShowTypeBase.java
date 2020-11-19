package android.lorenwang.customview.texiview.priceShow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.lorenwang.customview.R;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

/**
 * 功能作用：金额显示类型基础
 * 创建时间：2020-01-02 10:06
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 * <!--金额符号-->
 * <attr name="priceSymbol" format="enum">
 * <enum name="DEFAULT" value="0" />
 * <!--空，什么都不加，原有的符号也会被移除-->
 * <enum name="NONE" value="1" />
 * <!--正数，会在货币符号前加“+”-->
 * <enum name="POSITIVE" value="2" />
 * <!--负数，会在货币符号前加“-”-->
 * <enum name="MINUS" value="3" />
 * <!--相反的，也就是值为负的时候显示正，之为正则显示负-->
 * <enum name="OPPOSITE" value="4" />
 * <!--相反的，也就是值为负的时候显示正，之为正则显示负,同时转换后是正值的情况下不显示符号-->
 * <enum name="OPPOSITE_AND_POSITIVE_NONE" value="5" />
 * </attr>
 * <!--金额格式化格式-->
 * <attr name="priceFormatPattern" format="string" />
 * <!--货币符号-->
 * <attr name="currencySymbol" format="string" />
 * <!--金额文本大小-->
 * <attr name="priceTextSize" format="dimension" />
 * <!--金额文本颜色-->
 * <attr name="priceTextColor" format="color" />
 * <!--金额文本-->
 * <attr name="priceText" format="string" />
 */

abstract class AvlwPriceShowTypeBase {
    protected final String TAG = getClass().getName();
    protected Context context;
    /**
     * 显示的金额
     */
    protected String showPrice;
    /**
     * 金额显示颜色
     */
    protected int priceColor = Color.TRANSPARENT;
    /**
     * 金额显示大小
     */
    protected float priceSize = 0;
    /**
     * 金额符号
     */
    protected String priceSymbol = "";
    /**
     * 金额格式化类型
     */
    protected DecimalFormat decimalFormat;
    /**
     * 金额文本是否加粗
     */
    protected boolean priceTextBold = false;
    /**
     * 金额文本处理模式
     */
    protected RoundingMode priceRoundingMode = null;
    /**
     * 金额符号和金额之间的间距
     */
    protected float priceSymbolPriceDistance = 0;
    /**
     * 控件
     */
    protected AvlwPriceShowTextView avlwPriceShowTextView;

    /**
     * 金额画笔
     */
    protected Paint pricePaint;

    /**
     * 金额符号类型
     */
    protected int priceSymbolType;

    /**
     * 金额格式化精度
     */
    protected String priceFormatPattern;

    /**
     * 金额的处理模式
     */
    protected int priceRoundingModeType;


    /**
     * 初始化
     *
     * @param context               上下文
     * @param avlwPriceShowTextView 文本显示控件
     * @param attributes            配置参数
     */
    void init(Context context, AvlwPriceShowTextView avlwPriceShowTextView, TypedArray attributes) {
        this.context = context;
        this.avlwPriceShowTextView = avlwPriceShowTextView;
        priceSymbolType = attributes.getInt(R.styleable.AvlwPriceShowTextView_avlwPriceShowPriceSymbol, 0);
        //金额格式化精度
        priceFormatPattern = attributes.getString(R.styleable.AvlwPriceShowTextView_avlwPriceShowPriceFormatPattern);
        //金额的处理模式
        priceRoundingModeType = attributes.getInt(R.styleable.AvlwPriceShowTextView_avlwPriceShowPriceRoundingMode, -1);
        //格式化金额显示大小
        this.priceSize = attributes.getDimension(R.styleable.AvlwPriceShowTextView_avlwPriceShowPriceTextSize, this.priceSize);
        //金额符号和金额之间的间距
        this.priceSymbolPriceDistance = attributes.getDimension(R.styleable.AvlwPriceShowTextView_avlwPriceShowPriceSymbolPriceDistance,
                this.priceSymbolPriceDistance);
        //格式化显示颜色
        this.priceColor = attributes.getColor(R.styleable.AvlwPriceShowTextView_avlwPriceShowPriceTextColor, this.priceColor);
        //获取货币符号
        this.priceTextBold = attributes.getBoolean(R.styleable.AvlwPriceShowTextView_avlwPriceShowPriceTextBold, this.priceTextBold);
        //格式化金额显示
        formatPrice(new BigDecimal(attributes.getString(R.styleable.AvlwPriceShowTextView_avlwPriceShowPriceText) == null
                ? "" : attributes.getString(R.styleable.AvlwPriceShowTextView_avlwPriceShowPriceText)));

        pricePaint = new Paint();
        pricePaint.setColor(this.priceColor);
        pricePaint.setTextSize(this.priceSize);
        pricePaint.setAntiAlias(true);
        if (priceTextBold) {
            pricePaint.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    /**
     * 获取布局绘制宽度
     *
     * @param widthMeasureSpec 原始宽度
     * @return 绘制宽度
     */
    public abstract int getMeasureWidth(int widthMeasureSpec);

    /**
     * 获取布局绘制高度
     *
     * @param heightMeasureSpec 原始高度
     * @return 绘制高度
     */
    public abstract int getMeasureHeight(int heightMeasureSpec);

    /**
     * 指定区域绘制
     *
     * @param canvas 画板
     * @param left   区域左侧坐标
     * @param top    区域顶部坐标
     * @param right  区域右侧坐标
     * @param bottom 区域底部坐标
     */
    abstract void onDrawRegion(Canvas canvas, float left, float top, float right, float bottom);

    /**
     * 设置金额以及颜色
     *
     * @param price 金额
     * @param color 文本颜色
     */
    public void setPrice(BigDecimal price, Integer color) {
        formatPrice(price);
        if (color != null) {
            this.priceColor = color;
            pricePaint.setColor(this.priceColor);
        }
    }

    /**
     * 设置中划线金额以及颜色
     *
     * @param price 金额
     * @param color 文本颜色
     */
    public void setLinePrice(BigDecimal price, Integer color) {
        setPrice(price, color);
        //设置中划线
        pricePaint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    /**
     * 设置字体类型
     *
     * @param typeface 字体类型
     */
    public void setTypeFace(@NotNull Typeface typeface) {
        pricePaint.setTypeface(typeface);
    }

    /**
     * 格式化金额显示
     *
     * @param price 金额数据
     */
    private void formatPrice(BigDecimal price) {
        if (JtlwCheckVariateUtils.getInstance().isEmpty(price)) {
            showPrice = "";
        } else {
            //格式化符号显示，当值为0时，不做任何符号处理
            if (BigDecimal.ZERO.compareTo(price) == 0) {
                priceSymbol = "";
            } else {
                switch (priceSymbolType) {
                    //空，什么都不加，原有的符号也会被移除
                    case 1:
                        priceSymbol = "";
                        break;
                    //正数，会在货币符号前加“+”,原有的符号也会被移除
                    case 2:
                        priceSymbol = "+";
                        break;
                    //负数，会在货币符号前加“-”,原有的符号也会被移除
                    case 3:
                        priceSymbol = "-";
                        break;
                    //相反的，也就是值为负的时候显示正，之为正则显示负
                    case 4:
                        priceSymbol = BigDecimal.ZERO.compareTo(price) > 0 ? "+" : "-";
                        break;
                    //相反的，也就是值为负的时候显示正，之为正则显示负,同时转换后是正值的情况下不显示符号
                    case 5:
                        priceSymbol = BigDecimal.ZERO.compareTo(price) > 0 ? "" : "-";
                        break;
                    //默认的，按照原有金额符号处理
                    case 0:
                        priceSymbol = BigDecimal.ZERO.compareTo(price) > 0 ? "-" : "+";
                    default:
                        break;
                }
            }
            //获取格式化金额的精度
            String formatPattern = JtlwCheckVariateUtils.getInstance().isEmpty(priceFormatPattern) ? "0.00" : priceFormatPattern;
            //格式化金额处理模式
            if (priceRoundingModeType != -1) {
                try {
                    priceRoundingMode = RoundingMode.valueOf(priceRoundingModeType);
                    price = price.setScale(formatPattern.substring(formatPattern.indexOf(".") + 1).length(), priceRoundingMode);
                } catch (Exception e) {
                    priceRoundingMode = null;
                }
            }
            //处理金额数据,要做异常捕获防止其传递异常参数后导致数据崩溃
            try {
                decimalFormat = new DecimalFormat(formatPattern);
                //开始处理数据
                showPrice = decimalFormat.format(price);
            } catch (Exception e) {
                decimalFormat = new DecimalFormat("0.00");
                //开始处理数据
                showPrice = decimalFormat.format(price);
            }
        }
    }
}
