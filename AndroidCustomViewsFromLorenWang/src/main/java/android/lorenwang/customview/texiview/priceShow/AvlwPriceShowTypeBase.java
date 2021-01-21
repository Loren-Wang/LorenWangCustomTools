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
 */

abstract class AvlwPriceShowTypeBase {
    protected final String TAG = getClass().getName();
    protected Context context;
    /**
     * 金额格式化类型
     */
    private DecimalFormat decimalFormat;
    /**
     * 金额符号类型
     */
    private int priceSymbolType;
    /**
     * 金额格式化精度
     */
    private String priceFormatPattern;
    /**
     * 控件
     */
    private AvlwPriceShowTextView qtPriceShowTextView;


    /**
     * 金额画笔
     */
    protected Paint pricePaint;
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
     * 金额符号和金额之间的间距
     */
    protected float priceSymbolPriceDistance = 0;
    /**
     * 金额的处理模式
     */
    protected int priceRoundingModeType;
    /**
     * 价格基线
     */
    protected int priceBaseLine = -1;
    /**
     * 文本基线
     */
    protected int textBaseLine = -1;


    /**
     * 初始化
     *
     * @param context             上下文
     * @param qtPriceShowTextView 文本显示控件
     * @param attributes          配置参数
     */
    void init(Context context, AvlwPriceShowTextView qtPriceShowTextView, TypedArray attributes) {
        this.context = context;
        this.qtPriceShowTextView = qtPriceShowTextView;
        //金额符号显示的类型
        priceSymbolType = attributes.getInt(R.styleable.AvlwPriceShowTextView_avlwPriceSymbol, 0);
        //金额格式化精度
        priceFormatPattern = attributes
                .getString(R.styleable.AvlwPriceShowTextView_avlwPriceFormatPattern);
        //金额的处理模式,即四舍五入或其他模式
        priceRoundingModeType = attributes
                .getInt(R.styleable.AvlwPriceShowTextView_avlwPriceRoundingMode, -1);
        //格式化金额显示大小
        this.priceSize = attributes
                .getDimension(R.styleable.AvlwPriceShowTextView_avlwPriceTextSize, this.priceSize);
        //金额符号和金额之间的间距
        this.priceSymbolPriceDistance = attributes
                .getDimension(R.styleable.AvlwPriceShowTextView_avlwPriceSymbolPriceDistance,
                        this.priceSymbolPriceDistance);
        //格式化显示颜色
        this.priceColor = attributes
                .getColor(R.styleable.AvlwPriceShowTextView_avlwPriceTextColor, this.priceColor);

        pricePaint = new Paint();
        pricePaint.setColor(this.priceColor);
        pricePaint.setTextSize(this.priceSize);
        pricePaint.setAntiAlias(true);
        //是否加粗
        if (attributes.getBoolean(R.styleable.AvlwPriceShowTextView_avlwPriceTextBold, false)) {
            pricePaint.setTypeface(Typeface.DEFAULT_BOLD);
        }
        //是否划线处理
        if (attributes.getBoolean(R.styleable.AvlwPriceShowTextView_avlwPriceShowLine, false)) {
            setPriceTypeLine();
        }

    }

    /**
     * 获取布局绘制宽度
     *
     * @param widthMeasureSpec 原始宽度
     * @return 绘制宽度
     */
    abstract int getMeasureWidth(int widthMeasureSpec);

    /**
     * 获取布局绘制高度
     *
     * @param heightMeasureSpec 原始高度
     * @return 绘制高度
     */
    abstract int getMeasureHeight(int heightMeasureSpec);

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
     * 设置金额
     *
     * @param price 金额
     */
    abstract void setPrice(BigDecimal price);

    /**
     * 获取金额基线
     *
     * @return 基线
     */
    public int getPriceBaseLine() {
        return priceBaseLine;
    }

    /**
     * 获取文本基线
     *
     * @return 基线
     */
    public int getTextBaseLine() {
        return textBaseLine;
    }

    /**
     * 设置中划线金额
     */
    public void setPriceTypeLine() {
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
     * 设置价格颜色
     *
     * @param color 价格颜色
     */
    public void setPriceColor(Integer color) {
        if (color != null) {
            this.priceColor = color;
            pricePaint.setColor(this.priceColor);
        }
    }

    /**
     * 格式化金额显示
     *
     * @param price 金额数据
     * @return 格式化后金额
     */
    protected String formatPrice(BigDecimal price) {
        String showPrice;
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
            String formatPattern = JtlwCheckVariateUtils.getInstance()
                    .isEmpty(priceFormatPattern) ? "0.00" : priceFormatPattern;
            //格式化金额处理模式
            if (priceRoundingModeType != -1) {
                try {
                    price = price.setScale(
                            formatPattern.substring(formatPattern.indexOf(".") + 1).length(),
                            RoundingMode.valueOf(priceRoundingModeType));
                } catch (Exception ignore) {
                }
            }
            //处理金额数据,要做异常捕获防止其传递异常参数后导致数据崩溃
            try {
                decimalFormat = new DecimalFormat(formatPattern);
                //开始处理数据
                showPrice = decimalFormat.format(price);
                //判断是否是仅仅只显示浮点经度
                if (formatPattern.indexOf(".") == 0) {
                    showPrice = showPrice.substring(showPrice.indexOf("."));
                }
            } catch (Exception e) {
                decimalFormat = new DecimalFormat("0.00");
                //开始处理数据
                showPrice = decimalFormat.format(price);
            }
        }
        return showPrice;
    }
}
