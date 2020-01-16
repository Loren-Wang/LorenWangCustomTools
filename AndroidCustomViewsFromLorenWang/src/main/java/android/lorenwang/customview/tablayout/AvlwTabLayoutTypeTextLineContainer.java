package android.lorenwang.customview.tablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.lorenwang.customview.R;
import android.lorenwang.tools.image.AtlwImageCommonUtils;

/**
 * 功能作用：tablayout自定义控件,文本、线、线容器
 * 创建时间：2020-01-16 11:40
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

class AvlwTabLayoutTypeTextLineContainer extends AvlwTabLayoutTypeTextLine {
    /**
     * 下划线容器高度
     */
    private float lineContainerHeight;
    /**
     * 下划线容器和文本之间的间距
     */
    private float lineContainerTextSpace;
    /**
     * 下划线所属容器图片
     */
    private Bitmap lineContainerBg;
    /**
     * 下划线drawable
     */
    private Drawable lingContainerBgDrawable;

    public AvlwTabLayoutTypeTextLineContainer(Context context, AvlwTabLayout avlwTabLayout, TypedArray attributes, float tabWidth, float tabHeight) {
        super(context, avlwTabLayout, attributes, tabWidth, tabHeight);
        this.lineContainerHeight = attributes.getDimension(R.styleable.AvlwTabLayout_avlw_tl_lineContainerHeight, this.lineContainerHeight);
        this.lineContainerTextSpace = attributes.getDimension(R.styleable.AvlwTabLayout_avlw_tl_lineContainerTextSpace, this.lineContainerTextSpace);
        //读取bg图片
        lingContainerBgDrawable = attributes.getDrawable(R.styleable.AvlwTabLayout_avlw_tl_lineContainerBg);
        if (lingContainerBgDrawable == null) {
            lingContainerBgDrawable = new ColorDrawable(Color.TRANSPARENT);
        }
    }

    @Override
    public void drawTypeChild(Canvas canvas, Integer currentPosition, Integer scrollToPosition, float lineSlipPercent, float startX, float stopX, float currentWidth, float scrollToWidth) {
        //读取当前宽度的图片数据
        Bitmap lineContainerBg = getLineContainerBg();
        if (lineContainerBg != null) {
            //绘制下划线
            float top = avlwTabLayout.getPaddingTop() + tabHeight + lineContainerTextSpace;
            canvas.drawBitmap(lineContainerBg, null, new RectF(
                    0, top, lineContainerBg.getWidth(), top + lineContainerBg.getHeight()), null);
        }
        super.drawTypeChild(canvas, currentPosition, scrollToPosition, lineSlipPercent, startX, stopX, currentWidth, scrollToWidth);
    }

    @Override
    public int getMeasureHeight(AvlwTabLayout avlwTabLayout, int heightMeasureSpec, int tabTextListSize) {
        return (int) (super.getMeasureHeight(avlwTabLayout, heightMeasureSpec, tabTextListSize) + (lineContainerHeight - lineHeight) + (lineContainerTextSpace - lineTextSpace));
    }

    /**
     * 下划线容器数据配置
     *
     * @param lineContainerBg        下划线所属容器图片
     * @param lineContainerHeight    下划线容器高度
     * @param lineContainerTextSpace 下划线容器和文本之间的间距
     */
    public void setLineContainerConfig(Drawable lineContainerBg, Float lineContainerHeight,
                                       Float lineContainerTextSpace) {
        if (lineContainerBg != null) {
            this.lingContainerBgDrawable = lineContainerBg;
        }
        if (lineContainerHeight != null) {
            this.lineContainerHeight = lineContainerHeight;
        }
        if (lineContainerTextSpace != null) {
            this.lineContainerTextSpace = lineContainerTextSpace;
        }
    }

    /**
     * 获取下划线位图
     *
     * @return 位图
     */
    private Bitmap getLineContainerBg() {
        if (lineContainerBg == null) {
            synchronized (AtlwImageCommonUtils.class) {
                if (lineContainerBg == null) {
                    if (avlwTabLayout.getWidth() > 0 && lineContainerHeight > 0) {
                        lineContainerBg = AtlwImageCommonUtils.getInstance().drawableToBitmap(lingContainerBgDrawable, avlwTabLayout.getWidth(), (int) lineContainerHeight);
                    }
                }
            }
        }
        return lineContainerBg;
    }
}
