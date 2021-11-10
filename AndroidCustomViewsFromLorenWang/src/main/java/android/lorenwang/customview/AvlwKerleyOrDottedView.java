package android.lorenwang.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * 功能作用：间隔线或分隔虚线视图
 * 创建时间：2019-03-20 下午 16:00:41
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class AvlwKerleyOrDottedView extends View implements AvlwCustomViewCommon {

    /**
     * 画笔
     */
    private final Paint mPaint = new Paint();

    /**
     * 虚线宽
     */
    private float mDvWidth = 0;

    /**
     * 虚线高
     */
    private float mDvHeight = 0;

    /**
     * 虚线直径
     */
    private float mDvRadius = 0;

    /**
     * 间距
     */
    private float mDvDistance = 0;

    /**
     * 方向，-1不显示,0水平，1垂直，使用LineaLayout方向配置
     */
    private int mDvOrientation = -1;

    public AvlwKerleyOrDottedView(Context context) {
        super(context);
        initConfig(context, null);
    }

    public AvlwKerleyOrDottedView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        initConfig(context, attrs);
    }

    public AvlwKerleyOrDottedView(Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initConfig(context, attrs);
    }

    private void initConfig(Context context, AttributeSet attrs) {
        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AvlwKerleyOrDottedViewAvlw);
        resetPaint(attributes.getColor(R.styleable.AvlwKerleyOrDottedViewAvlw_avlw_kv_color, Color.TRANSPARENT));
        mDvWidth = attributes.getDimension(R.styleable.AvlwKerleyOrDottedViewAvlw_avlw_kv_width, mDvWidth);
        mDvHeight = attributes.getDimension(R.styleable.AvlwKerleyOrDottedViewAvlw_avlw_kv_height, mDvHeight);
        mDvRadius = attributes.getDimension(R.styleable.AvlwKerleyOrDottedViewAvlw_avlw_kv_radius, mDvRadius);
        mDvDistance = attributes.getDimension(R.styleable.AvlwKerleyOrDottedViewAvlw_avlw_kv_distance, mDvDistance);
        mDvOrientation = attributes.getInt(R.styleable.AvlwKerleyOrDottedViewAvlw_avlw_kv_orientation, mDvOrientation);
        attributes.recycle();
    }

    /**
     * 设置颜色
     *
     * @param color 颜色
     */
    public void setColor(int color) {
        resetPaint(color);
    }

    /**
     * 设置虚线高度
     *
     * @param mDvHeight 高度
     */
    public void setDvHeight(float mDvHeight) {
        this.mDvHeight = mDvHeight;
    }

    /**
     * 设置虚线宽度
     *
     * @param mDvWidth 宽度
     */
    public void setDvWidth(float mDvWidth) {
        this.mDvWidth = mDvWidth;
    }

    /**
     * 设置虚线直径
     *
     * @param mDvRadius 直径
     */
    public void setDvRadius(float mDvRadius) {
        this.mDvRadius = mDvRadius;
    }

    /**
     * 设置虚线点间距
     *
     * @param mDvDistance 点间距
     */
    public void setDvDistance(float mDvDistance) {
        this.mDvDistance = mDvDistance;
    }

    /**
     * 设置虚线方向
     *
     * @param mDvOrientation 虚线方向，使用LineaLayout方向配置
     */
    public void setDvOrientation(int mDvOrientation) {
        this.mDvOrientation = mDvOrientation;
    }

    /**
     * 重置画笔
     *
     * @param color 颜色
     */
    private void resetPaint(int color) {
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int useWidth = getWidth();
        final int useHeight = getHeight();
        if (useWidth > 0 && useHeight > 0) {
            float left = getPaddingLeft();
            float top = getPaddingTop();
            float maxRight = useWidth - getPaddingLeft() - getPaddingRight();
            float maxBottom = useHeight - getPaddingTop() - getPaddingBottom();
            float useXY = mDvRadius / 2F;
            switch (mDvOrientation) {
                case LinearLayout.HORIZONTAL:
                    //判断是否有半径和间距
                    float bottom = top + mDvHeight;
                    if (mDvRadius == 0 && mDvDistance == 0) {
                        canvas.drawRect(left, top, maxRight, bottom, mPaint);
                    } else {
                        //存在半径和间距，开始绘制
                        while ((left + mDvWidth) <= maxRight) {
                            canvas.drawRoundRect(left, top, left + mDvWidth, bottom, useXY, useXY, mPaint);
                            left += mDvDistance + mDvWidth;
                        }
                    }
                    break;
                case LinearLayout.VERTICAL:
                    //判断是否有半径和间距
                    float right = left + mDvWidth;
                    if (mDvRadius == 0 && mDvDistance == 0) {
                        canvas.drawRect(left, top, right, maxBottom, mPaint);
                    } else {
                        //存在半径和间距，开始绘制
                        while ((top + mDvHeight) <= maxBottom) {
                            canvas.drawRoundRect(left, top, right, top + mDvHeight, useXY, useXY, mPaint);
                            top += mDvDistance + mDvHeight;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void release() {

    }
}
