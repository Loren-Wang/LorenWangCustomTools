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
 * 功能作用：自定义tablayout文本以及其背景
 * 创建时间：2020-01-16 12:05
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

class AvlwTabLayoutTypeTextBg implements AvlwBaseTabLayout {
    private final String TAG = getClass().getName();
    private Context context;
    private AvlwTabLayout avlwTabLayout;
    private float tabWidth;
    private float tabHeight;
    /**
     * tab背景宽度，默认是文本宽度,但是宽度不能大于tab宽度
     */
    private float tabBgWidth = 0f;
    /**
     * tab背景高度，默认是文本高度，但是不能小于文字高度,也不能大于tab高度
     */
    private float tabBgHeight = 0f;
    /**
     * tab背景宽度距离文本间距
     */
    private float tabBgTextWidthPadding = 0f;
    /**
     * tab背景高度距离文本间距
     */
    private float tabBgTextHeightPadding = 0f;
    /**
     * tab选中图片
     */
    private Bitmap tabBgY;
    /**
     * tab未选中背景
     */
    private Bitmap tabBgN;
    /**
     * tab选中图片
     */
    private Drawable tabBgDrawableY;
    /**
     * tab未选中背景
     */
    private Drawable tabBgDrawableN;

    public AvlwTabLayoutTypeTextBg(Context context, AvlwTabLayout avlwTabLayout, TypedArray attributes, float tabWidth, float tabHeight) {
        this.context = context;
        this.avlwTabLayout = avlwTabLayout;
        this.tabWidth = tabWidth;
        this.tabHeight = tabHeight;
        this.tabBgWidth = attributes.getDimension(R.styleable.AvlwTabLayout_avlw_tl_tabBgWidth, this.tabBgWidth);
        this.tabBgHeight = attributes.getDimension(R.styleable.AvlwTabLayout_avlw_tl_tabBgHeight, this.tabBgHeight);
        this.tabBgTextWidthPadding = attributes.getDimension(R.styleable.AvlwTabLayout_avlw_tl_tabBgTextWidthPadding, this.tabBgTextWidthPadding);
        this.tabBgTextHeightPadding = attributes.getDimension(R.styleable.AvlwTabLayout_avlw_tl_tabBgTextHeightPadding, this.tabBgTextHeightPadding);

        //获取相对于屏幕百分比，大于0情况下安照百分比来显示宽度
        float widthPercent = attributes.getFloat(R.styleable.AvlwTabLayout_avlw_tl_tabBgWidthPercent, -1.0F);
        if (widthPercent > (float) 0) {
            if (widthPercent > (float) 1) {
                widthPercent = 1.0F;
            }
            this.tabBgWidth = (float) context.getResources().getDisplayMetrics().widthPixels * widthPercent;
        }

        //读取bg图片
        tabBgDrawableY = attributes.getDrawable(R.styleable.AvlwTabLayout_avlw_tl_tabBgY);
        if (tabBgDrawableY == null) {
            tabBgDrawableY = new ColorDrawable(Color.TRANSPARENT);
        }
        //读取bg图片
        tabBgDrawableN = attributes.getDrawable(R.styleable.AvlwTabLayout_avlw_tl_tabBgN);
        if (tabBgDrawableN == null) {
            tabBgDrawableN = new ColorDrawable(Color.TRANSPARENT);
        }

    }

    @Override
    public int getMeasureWidth(AvlwTabLayout avlwTabLayout, int widthMeasureSpec, int tabTextListSize) {
        return (int) (avlwTabLayout.getPaddingLeft() + avlwTabLayout.getPaddingRight() + tabTextListSize * tabWidth);
    }

    @Override
    public int getMeasureHeight(AvlwTabLayout avlwTabLayout, int heightMeasureSpec, int tabTextListSize) {
        return (int) (avlwTabLayout.getPaddingTop() + avlwTabLayout.getPaddingBottom() + tabHeight);
    }

    @Override
    public void drawTypeChild(Canvas canvas, Integer currentPosition, Integer scrollToPosition, float lineSlipPercent, float startX, float stopX, float currentWidth, float scrollToWidth) {

    }

    /**
     * 绘制背景区域
     *
     * @param canvas            画板
     * @param textPaint         文本画笔
     * @param drawTextX         文本x坐标
     * @param drawTextY         文本y坐标
     * @param scrollToTextX     滑动目标的文本x坐标
     * @param scrollToTextWidth 滑动到目标的文本宽度
     * @param lineScrollPercent 滑动到目标的滑动百分比
     * @param isCurrent         当前是否是选中的
     * @param textWidth         文本宽度
     * @param textHeight        文本高度
     */
    @Override
    public void drawTypeItem(Canvas canvas, Paint textPaint, float drawTextX, float drawTextY, float scrollToTextX, float scrollToTextWidth, float lineScrollPercent, boolean isCurrent, float textWidth, float textHeight) {
        float width = this.tabBgWidth;
        float height = this.tabBgHeight;
        //获取处理后的宽度
        if (width == 0) {
            width = textWidth + tabBgTextWidthPadding;
        }
        width = Math.min(width, tabWidth);
        //获取处理后的高度
        if (height == 0) {
            height = textHeight + tabBgTextHeightPadding;
        }
        height = Math.min(height, tabHeight);
        //宽高均不为0
        if (width > 0 && height > 0) {
            //获取偏移
            float offset = 0f;
            if (lineScrollPercent != 0) {
                offset = lineScrollPercent * Math.abs(drawTextX - scrollToTextX);
            }

            Bitmap bitmap;
            //获取不同的位图
            if (isCurrent) {
                bitmap = getBgY((int) width, (int) height);
            } else {
                bitmap = getBgN((int) width, (int) height);
            }
            //绘制位图，因为不管是哪种模式左右偏移肯定是一致的，所以计算好除以2即可
            float widthOffset = (width - textWidth) / 2;
            float heightOffset = (height - textHeight) / 2;
            canvas.drawBitmap(bitmap, null,
                    new RectF(drawTextX - widthOffset + offset,
                            drawTextY + textPaint.ascent() - heightOffset,
                            drawTextX + textWidth + widthOffset + offset,
                            drawTextY + textPaint.descent() + heightOffset), null);
        }
    }

    /**
     * 获取选中背景位图
     *
     * @return 位图
     */
    private Bitmap getBgY(Integer width, Integer height) {
        if (tabBgY != null && width.compareTo(tabBgY.getWidth()) != 0) {
            AtlwImageCommonUtils.getInstance().releaseBitmap(tabBgY);
            tabBgY = null;
        }
        if (tabBgY == null) {
            synchronized (AtlwImageCommonUtils.class) {
                if (tabBgY == null) {
                    if (width > 0 && height > 0) {
                        tabBgY = AtlwImageCommonUtils.getInstance().drawableToBitmap(tabBgDrawableY, width, height);
                    }
                }
            }
        }
        return tabBgY;
    }

    /**
     * 获取非选中背景位图
     *
     * @return 位图
     */
    private Bitmap getBgN(Integer width, Integer height) {
        if (tabBgN != null && width.compareTo(tabBgN.getWidth()) != 0) {
            AtlwImageCommonUtils.getInstance().releaseBitmap(tabBgN);
            tabBgN = null;
        }
        if (tabBgN == null) {
            synchronized (AtlwImageCommonUtils.class) {
                if (tabBgN == null) {
                    if (width > 0 && height > 0) {
                        tabBgN = AtlwImageCommonUtils.getInstance().drawableToBitmap(tabBgDrawableN, width, height);
                    }
                }
            }
        }
        return tabBgN;
    }
}
