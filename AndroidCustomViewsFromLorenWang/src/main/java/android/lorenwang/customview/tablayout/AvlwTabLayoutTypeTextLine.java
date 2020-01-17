package android.lorenwang.customview.tablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.lorenwang.customview.R;
import android.lorenwang.tools.image.AtlwImageCommonUtils;

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

class AvlwTabLayoutTypeTextLine implements AvlwBaseTabLayout {
    private final String TAG = getClass().getName();
    private Context context;
    protected AvlwTabLayout avlwTabLayout;
    private float tabWidth;
    protected float tabHeight;
    /**
     * 下划线宽度
     */
    private float lineWidth = 0f;
    /**
     * 下划线高度
     */
    protected float lineHeight = 0f;
    /**
     * 下划线和文本之间的间距
     */
    protected float lineTextSpace = 0f;
    /**
     * 下划线背景
     */
    private Bitmap lineBg;
    /**
     * 下划线drawable
     */
    private Drawable lingBgDrawable;


    public AvlwTabLayoutTypeTextLine(Context context, AvlwTabLayout avlwTabLayout, TypedArray attributes, float tabWidth, float tabHeight) {
        this.context = context;
        this.avlwTabLayout = avlwTabLayout;
        this.tabWidth = tabWidth;
        this.tabHeight = tabHeight;
        this.lineWidth = attributes.getDimension(R.styleable.AvlwTabLayout_avlw_tl_lineWidth, this.lineWidth);
        this.lineHeight = attributes.getDimension(R.styleable.AvlwTabLayout_avlw_tl_lineHeight, this.lineHeight);
        this.lineTextSpace = attributes.getDimension(R.styleable.AvlwTabLayout_avlw_tl_lineTextSpace, this.lineTextSpace);
        //读取bg图片
        lingBgDrawable = attributes.getDrawable(R.styleable.AvlwTabLayout_avlw_tl_lineBg);
        if (lingBgDrawable == null) {
            lingBgDrawable = new ColorDrawable(Color.TRANSPARENT);
        }

        //获取相对于屏幕百分比，大于0情况下安照百分比来显示宽度
        float lineWidthPercent = attributes.getFloat(R.styleable.AvlwTabLayout_avlw_tl_lineWidthPercent, -1.0F);
        if (lineWidthPercent > (float) 0) {
            if (lineWidthPercent > (float) 1) {
                lineWidthPercent = 1.0F;
            }
            this.lineWidth = (float) context.getResources().getDisplayMetrics().widthPixels * lineWidthPercent;
        }

        //数据合理性检测，首先检测下划线宽度是否大于tab宽度
        this.lineWidth = Math.min(this.lineWidth, this.tabWidth);
    }

    @Override
    public int getMeasureWidth(AvlwTabLayout avlwTabLayout, int widthMeasureSpec, int tabTextListSize) {
        return (int) (avlwTabLayout.getPaddingLeft() + avlwTabLayout.getPaddingRight() + tabTextListSize * tabWidth);
    }

    @Override
    public int getMeasureHeight(AvlwTabLayout avlwTabLayout, int heightMeasureSpec, int tabTextListSize) {
        return (int) (avlwTabLayout.getPaddingTop() + avlwTabLayout.getPaddingBottom() + tabHeight + lineHeight + lineTextSpace);
    }

    @Override
    public void drawTypeChild(Canvas canvas, Integer currentPosition, Integer scrollToPosition,
                              float lineSlipPercent, float startX, float stopX, float currentWidth, float scrollToWidth) {
        //判断原始宽度是否为空或者为0，如果是的话则直接使用文本宽度绘制
        int showLineWidth;
        if (this.lineWidth > 0) {
            showLineWidth = Float.valueOf(this.lineWidth).intValue();
            //获取差值
            float value = (tabWidth - this.lineWidth) / 2;
            startX = tabWidth * currentPosition + value;
            stopX = tabWidth * scrollToPosition + value;
            currentWidth = this.lineWidth;
            scrollToWidth = this.lineWidth;
        } else {
            showLineWidth = Float.valueOf(currentWidth).intValue();
        }
        //根据百分比获取下划线变化后的值
        if (lineSlipPercent != 0) {
            showLineWidth = (int) (showLineWidth + (scrollToWidth - currentWidth) * lineSlipPercent);
        }
        //读取当前宽度的图片数据
        Bitmap lineBg = getLineBg(showLineWidth);
        if (lineBg != null) {
            float offset = startX;
            if (lineSlipPercent != 0) {
                offset = startX + (stopX - startX) * lineSlipPercent;
            }
            //绘制下划线
            float left = avlwTabLayout.getPaddingLeft() + offset;
            float top = avlwTabLayout.getPaddingTop() + tabHeight + lineTextSpace;
            canvas.drawBitmap(lineBg, null, new RectF(
                    left, top, left + showLineWidth, top + lineHeight), null);
        }

    }

    @Override
    public void drawTypeItem(Canvas canvas, Paint textPaint, float drawTextX, float drawTextY, float scrollToTextX, float scrollToTextWidth, float lineScrollPercent, boolean isCurrent, float textWidth, float textHeight) {

    }

    /**
     * 下划线数据配置
     *
     * @param lineWidth        下划线宽度,默认和文本宽度一致
     * @param lineWidthPercent 下划线宽度百分比
     * @param lineHeight       下划线高度
     * @param lineBgDrawable   下划线颜色
     * @param lineTextSpace    下划线和文本之间的距离
     */
    public void setLineConfig(Float lineWidth, Float lineWidthPercent, Float lineHeight,
                              Drawable lineBgDrawable, Float lineTextSpace) {
        if (lineWidth != null) {
            this.lineWidth = lineWidth;
        }
        if (lineWidthPercent != null) {
            //获取相对于屏幕百分比，大于0情况下安照百分比来显示宽度
            if (lineWidthPercent > (float) 0) {
                if (lineWidthPercent > (float) 1) {
                    lineWidthPercent = 1.0F;
                }
                this.lineWidth = (float) context.getResources().getDisplayMetrics().widthPixels * lineWidthPercent;
            }
        }
        //数据合理性检测，首先检测下划线宽度是否大于tab宽度
        this.lineWidth = Math.min(this.lineWidth, this.tabWidth);
        if (lineHeight != null) {
            this.lineHeight = lineHeight;
        }
        if (lineBgDrawable != null) {
            this.lingBgDrawable = lineBgDrawable;
        }
        if (lineTextSpace != null) {
            this.lineTextSpace = lineTextSpace;
        }
    }

    @Override
    public AvlwBaseTabLayout setTabWidth(float tabWidth) {
        this.tabWidth = tabWidth;
        return this;
    }

    /**
     * 获取下划线位图
     *
     * @return 位图
     */
    private Bitmap getLineBg(Integer lineWidth) {
        if (lineBg != null && lineWidth.compareTo(lineBg.getWidth()) != 0) {
            AtlwImageCommonUtils.getInstance().releaseBitmap(lineBg);
            lineBg = null;
        }
        if (lineBg == null) {
            synchronized (AtlwImageCommonUtils.class) {
                if (lineBg == null) {
                    if (lineWidth > 0 && lineHeight > 0) {
                        lineBg = AtlwImageCommonUtils.getInstance().drawableToBitmap(lingBgDrawable, lineWidth, (int) lineHeight);
                    }
                }
            }
        }
        return lineBg;
    }

}
