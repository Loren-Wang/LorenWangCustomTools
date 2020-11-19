package android.lorenwang.customview.texiview.priceShow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.lorenwang.customview.R;
import android.util.AttributeSet;
import android.view.View;

import java.math.BigDecimal;

import androidx.annotation.Nullable;

/**
 * 功能作用：金额显示文本控件
 * 创建时间：2020-01-02 9:58
 * 创建人：王亮（Loren wang）
 * 思路：使用四个个text绘制分别取绘制数据：符号文本、前缀、金额文本、描述文本，使用不同type决定用何种类型，
 * 然后根据不同的type类型去调用不同的类，然后将绘制交给不同类去执行，同时不同的类型读取的参数数据也不尽相同，
 * 同时还要有一些间距、文本大小、颜色、是否加粗等参数
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 * <!--显示类型-->
 * <attr name="showType" format="enum">
 * <!--默认只显示货币-->
 * <enum name="DEFAULT" value="1" />
 * <!--货币符号-->
 * <enum name="CURRENCY_SYMBOL" value="2" />
 * <!--货币符号和描述-->
 * <enum name="CURRENCY_SYMBOL_AND_DES" value="3" />
 * </attr>
 * @author wangliang
 */

public class AvlwPriceShowTextView extends View {
    private AvlwPriceShowTypeBase showType;

    public AvlwPriceShowTextView(Context context) {
        super(context);
        init(context, null, -1);
    }

    public AvlwPriceShowTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public AvlwPriceShowTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AvlwPriceShowTextView);
        int type = attributes.getInt(R.styleable.AvlwPriceShowTextView_avlwPriceShowShowType, 0);
        switch (type) {
            case 1:
                this.showType = new AvlwPriceShowTypeCurrencySymbol();
                break;
            case 2:
                this.showType = new AvlwPriceShowTypeCurrencySymbolAndDescribe();
                break;
            case 0:
            default:
                this.showType = new AvlwPriceShowTypeDefault();
                break;
        }
        this.showType.init(context, this, attributes);
        attributes.recycle();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(showType.getMeasureWidth(widthMeasureSpec),
                showType.getMeasureHeight(heightMeasureSpec));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        showType.onDrawRegion(canvas, getPaddingLeft(), getPaddingTop(),
                getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
    }

    /**
     * 设置金额以及颜色
     *
     * @param price 金额
     * @param color 文本颜色
     */
    public void setPrice(BigDecimal price, Integer color) {
        setPrice(price, color,null);
    }

    /**
     * 设置金额以及颜色
     *
     * @param price 金额
     * @param color 文本颜色
     */
    public void setPrice(BigDecimal price, Integer color, Typeface typeface) {
        showType.setPrice(price, color);
        setPriceTypeface(typeface);
    }

    /**
     * 设置字体类型
     *
     * @param typeface 字体类型
     */
    public void setPriceTypeface(Typeface typeface) {
        if(typeface != null){
            showType.setTypeFace(typeface);
        }
        //必须重新绘制，否则会显示不全
        requestLayout();
    }

    /**
     * 设置划线金额以及颜色
     *
     * @param price 金额
     * @param color 文本颜色
     */
    public void setLinePrice(BigDecimal price, Integer color) {
        showType.setLinePrice(price, color);
        setPriceTypeface(null);
    }

    /**
     * 设置金额符号颜色
     *
     * @param color 颜色
     */
    public void setSymbolTextColor(int color) {
        if (showType instanceof AvlwPriceShowTypeCurrencySymbol ||
                showType instanceof AvlwPriceShowTypeCurrencySymbolAndDescribe) {
            ((AvlwPriceShowTypeCurrencySymbol) showType).setSymbolTextColor(color);
        }
    }
}
