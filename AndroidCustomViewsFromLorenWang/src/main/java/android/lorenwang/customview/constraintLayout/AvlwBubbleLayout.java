package android.lorenwang.customview.constraintLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.lorenwang.customview.R;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * 功能作用：气泡内容布局
 * 初始注释时间： 2021/9/28 18:33
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
public class AvlwBubbleLayout extends ConstraintLayout {
    private final int BUBBLE_DIRECTION_NONE = 0;
    private final int BUBBLE_DIRECTION_LEFT = 1;
    private final int BUBBLE_DIRECTION_TOP = 2;
    private final int BUBBLE_DIRECTION_RIGHT = 3;
    private final int BUBBLE_DIRECTION_BOTTOM = 4;
    /**
     * 阴影路径
     */
    protected Path shadowPath;
    /**
     * 阴影画笔
     */
    private final Paint shadowPaint = new Paint();
    /**
     * 边框画笔
     */
    private final Paint strokePaint = new Paint();
    /**
     * 阴影颜色
     */
    protected int shadowColor = Color.TRANSPARENT;
    /**
     * 阴影宽度
     */
    protected int shadowWidth = 0;
    /**
     * 箭头宽度
     */
    private int arrowWidth = 0;
    /**
     * 箭头高度
     */
    private int arrowHeight = 0;
    /**
     * 边框宽度
     */
    private int strokeWidth = 0;
    /**
     * 边框颜色
     */
    private int strokeColor = Color.TRANSPARENT;
    /**
     * 箭头方向
     */
    private int arrowDirection = BUBBLE_DIRECTION_NONE;
    /**
     * 边框圆角角度
     */
    private int strokeRadiusLeftTop = 0;
    private int strokeRadiusRightTop = 0;
    private int strokeRadiusLeftBottom = 0;
    private int strokeRadiusRightBottom = 0;
    /**
     * 箭头偏移数据
     */
    private float arrowOffsetLeftPercent = 0F;
    private float arrowOffsetTopPercent = 0F;
    private float arrowOffsetRightPercent = 0F;
    private float arrowOffsetBottomPercent = 0F;
    private int arrowOffsetLeft = 0;
    private int arrowOffsetTop = 0;
    private int arrowOffsetRight = 0;
    private int arrowOffsetBottom = 0;
    /**
     * 背景图片
     */
    private Bitmap bgBitmap;
    /**
     * 旧的背景drawable
     */
    private Drawable oldBgDrawable;

    public AvlwBubbleLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public AvlwBubbleLayout(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AvlwBubbleLayout(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AvlwBubbleLayout);
        shadowWidth = typedArray.getDimensionPixelOffset(R.styleable.AvlwBubbleLayout_avlw_bl_shadowWidth, shadowWidth);
        shadowColor = typedArray.getColor(R.styleable.AvlwBubbleLayout_avlw_bl_shadowColor, shadowColor);
        arrowWidth = typedArray.getDimensionPixelOffset(R.styleable.AvlwBubbleLayout_avlw_bl_arrowWidth, arrowWidth);
        arrowHeight = typedArray.getDimensionPixelOffset(R.styleable.AvlwBubbleLayout_avlw_bl_arrowHeight, arrowHeight);
        strokeWidth = typedArray.getDimensionPixelOffset(R.styleable.AvlwBubbleLayout_avlw_bl_strokeWidth, strokeWidth);
        //圆角角度
        if (typedArray.getDimensionPixelOffset(R.styleable.AvlwBubbleLayout_avlw_bl_strokeRadius, 0) > 0) {
            strokeRadiusLeftTop = strokeRadiusRightTop = strokeRadiusLeftBottom = strokeRadiusRightBottom = typedArray.getDimensionPixelOffset(
                    R.styleable.AvlwBubbleLayout_avlw_bl_strokeRadius, 0);
        } else {
            strokeRadiusLeftTop = typedArray.getDimensionPixelOffset(R.styleable.AvlwBubbleLayout_avlw_bl_strokeRadiusLeftTop, 0);
            strokeRadiusRightTop = typedArray.getDimensionPixelOffset(R.styleable.AvlwBubbleLayout_avlw_bl_strokeRadiusRightTop, 0);
            strokeRadiusLeftBottom = typedArray.getDimensionPixelOffset(R.styleable.AvlwBubbleLayout_avlw_bl_strokeRadiusLeftBottom, 0);
            strokeRadiusRightBottom = typedArray.getDimensionPixelOffset(R.styleable.AvlwBubbleLayout_avlw_bl_strokeRadiusRightBottom, 0);
        }
        strokeColor = typedArray.getColor(R.styleable.AvlwBubbleLayout_avlw_bl_strokeColor, strokeColor);
        arrowDirection = typedArray.getInt(R.styleable.AvlwBubbleLayout_avlw_bl_arrowDirection, arrowDirection);
        //箭头偏移,百分比选项要取余1，保证数据范围在0-1之间
        arrowOffsetLeftPercent = typedArray.getFloat(R.styleable.AvlwBubbleLayout_avlw_bl_arrowOffsetLeftPercent, arrowOffsetLeftPercent) % 1;
        arrowOffsetTopPercent = typedArray.getFloat(R.styleable.AvlwBubbleLayout_avlw_bl_arrowOffsetTopPercent, arrowOffsetTopPercent) % 1;
        arrowOffsetRightPercent = typedArray.getFloat(R.styleable.AvlwBubbleLayout_avlw_bl_arrowOffsetRightPercent, arrowOffsetRightPercent) % 1;
        arrowOffsetBottomPercent = typedArray.getFloat(R.styleable.AvlwBubbleLayout_avlw_bl_arrowOffsetBottomPercent, arrowOffsetBottomPercent) % 1;
        arrowOffsetLeft = typedArray.getDimensionPixelOffset(R.styleable.AvlwBubbleLayout_avlw_bl_arrowOffsetLeft, arrowOffsetLeft);
        arrowOffsetTop = typedArray.getDimensionPixelOffset(R.styleable.AvlwBubbleLayout_avlw_bl_arrowOffsetTop, arrowOffsetTop);
        arrowOffsetRight = typedArray.getDimensionPixelOffset(R.styleable.AvlwBubbleLayout_avlw_bl_arrowOffsetRight, arrowOffsetRight);
        arrowOffsetBottom = typedArray.getDimensionPixelOffset(R.styleable.AvlwBubbleLayout_avlw_bl_arrowOffsetBottom, arrowOffsetBottom);
        typedArray.recycle();
        //重新设置画笔
        resetPaint();
        //设置内间距给阴影留空间
        changePadding();
        setBackgroundColor(Color.RED);
        //默认设置背景是透明的
        super.setBackground(new ColorDrawable(Color.TRANSPARENT));
    }

    /**
     * 重新设置画笔
     */
    protected void resetPaint() {
        shadowPaint.setColor(shadowColor);
        shadowPaint.setAntiAlias(true);
        //内部不绘制，外部绘制
        if (shadowWidth > 0) {
            shadowPaint.setMaskFilter(new BlurMaskFilter(shadowWidth, BlurMaskFilter.Blur.OUTER));
        }

        //边框画笔
        strokePaint.setColor(strokeColor);
        strokePaint.setStrokeWidth(strokeWidth);
        strokePaint.setAntiAlias(true);
        strokePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (shadowPath != null) {
            //阴影绘制
            if (shadowWidth > 0) {
                //关闭硬件加速
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                canvas.drawPath(shadowPath, shadowPaint);
            }
            //获取背景绘制
            if (bgBitmap == null) {
                resetBgBitmap(oldBgDrawable);
            }
            if (bgBitmap != null) {
                canvas.drawBitmap(bgBitmap, 0, 0, null);
            }
            //边框绘制
            canvas.drawPath(shadowPath, strokePaint);
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        shadowPath = getStrokePath();
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(null);
        resetBgBitmap(background);
    }

    /**
     * 获取边框路径
     *
     * @return 边框路径
     */
    private Path getStrokePath() {
        if (getWidth() > 0 && getHeight() > 0) {
            Path path = new Path();
            final int offset = strokeWidth / 2;
            float leftX = getPaddingLeft() - offset;
            float rightX = getWidth() - getPaddingRight() + offset;
            float topY = getPaddingTop() - offset;
            float bottomY = getHeight() - getPaddingBottom();
            final float arrowTopOffset = getArrowTopOffset(bottomY - topY);
            final float arrowLeftOffset = getArrowLeftOffset(rightX - leftX);
            switch (arrowDirection) {
                case BUBBLE_DIRECTION_NONE:
                    path.moveTo(leftX + strokeRadiusLeftTop / 2.0F, topY);
                    path.arcTo(rightX - strokeRadiusRightTop, topY, rightX, topY + strokeRadiusRightTop, 270, 90, false);
                    path.arcTo(rightX - strokeRadiusRightBottom, bottomY - strokeRadiusRightBottom, rightX, bottomY, 0, 90, false);
                    path.arcTo(leftX, bottomY - strokeRadiusLeftBottom, leftX + strokeRadiusLeftBottom, bottomY, 90, 90, false);
                    path.arcTo(leftX, topY, leftX + strokeRadiusLeftTop, topY + strokeRadiusLeftTop, 180, 90, false);
                    break;
                case BUBBLE_DIRECTION_LEFT:
                    leftX = leftX - arrowHeight;
                    path.moveTo(leftX + strokeRadiusLeftTop / 2.0F + arrowHeight, topY);
                    path.arcTo(rightX - strokeRadiusRightTop, topY, rightX, topY + strokeRadiusRightTop, 270, 90, false);
                    path.arcTo(rightX - strokeRadiusRightBottom, bottomY - strokeRadiusRightBottom, rightX, bottomY, 0, 90, false);
                    path.arcTo(leftX + arrowHeight, bottomY - strokeRadiusLeftBottom, leftX + strokeRadiusLeftBottom, bottomY, 90, 90, false);
                    path.lineTo(leftX + arrowHeight, topY + arrowTopOffset + arrowWidth);
                    path.lineTo(leftX, topY + arrowTopOffset + arrowWidth / 2.0F);
                    path.lineTo(leftX + arrowHeight, topY + arrowTopOffset);
                    path.lineTo(leftX + arrowHeight, topY + strokeRadiusLeftTop / 2.0F);
                    path.arcTo(leftX + arrowHeight, topY, leftX + arrowHeight + strokeRadiusLeftTop, topY + strokeRadiusLeftTop, 180, 90, false);
                    break;
                case BUBBLE_DIRECTION_RIGHT:
                    rightX = rightX + arrowHeight;
                    path.moveTo(leftX + strokeRadiusLeftTop / 2.0F, topY);
                    path.arcTo(rightX - strokeRadiusRightTop - arrowHeight, topY, rightX - arrowHeight, topY - arrowHeight + strokeRadiusRightTop,
                            270, 90, false);
                    path.lineTo(rightX - arrowHeight, topY + arrowTopOffset);
                    path.lineTo(rightX, topY + arrowTopOffset + arrowWidth / 2.0F);
                    path.lineTo(rightX - arrowHeight, topY + arrowTopOffset + arrowWidth);
                    path.arcTo(rightX - strokeRadiusRightBottom - arrowHeight, bottomY - strokeRadiusRightBottom, rightX - arrowHeight, bottomY, 0,
                            90, false);
                    path.arcTo(leftX, bottomY - strokeRadiusLeftBottom, leftX + strokeRadiusLeftBottom, bottomY, 90, 90, false);
                    path.arcTo(leftX, topY, leftX + strokeRadiusLeftTop, topY + strokeRadiusLeftTop, 180, 90, false);
                    break;
                case BUBBLE_DIRECTION_TOP:
                    topY -= arrowHeight;
                    path.moveTo(leftX + strokeRadiusLeftTop / 2.0F, topY + arrowHeight);
                    path.lineTo(leftX + arrowLeftOffset, topY + arrowHeight);
                    path.lineTo(leftX + arrowLeftOffset + arrowWidth / 2.0F, topY);
                    path.lineTo(leftX + arrowLeftOffset + arrowWidth, topY + arrowHeight);
                    path.arcTo(rightX - strokeRadiusRightTop, topY + arrowHeight, rightX, topY + arrowHeight + strokeRadiusRightTop, 270, 90, false);
                    path.arcTo(rightX - strokeRadiusRightBottom, bottomY - strokeRadiusRightBottom, rightX, bottomY, 0, 90, false);
                    path.arcTo(leftX, bottomY - strokeRadiusLeftBottom, leftX + strokeRadiusLeftBottom, bottomY, 90, 90, false);
                    path.arcTo(leftX, topY + arrowHeight, leftX + strokeRadiusLeftTop, topY + arrowHeight + strokeRadiusLeftTop, 180, 90, false);
                    break;
                case BUBBLE_DIRECTION_BOTTOM:
                    bottomY += arrowHeight;
                    path.moveTo(leftX + strokeRadiusLeftTop / 2.0F, topY);
                    path.arcTo(rightX - strokeRadiusRightTop, topY, rightX, topY + strokeRadiusRightTop, 270, 90, false);
                    path.arcTo(rightX - strokeRadiusRightBottom, bottomY - strokeRadiusRightBottom - arrowHeight, rightX, bottomY - arrowHeight, 0,
                            90, false);
                    path.lineTo(leftX + arrowLeftOffset + arrowWidth, bottomY - arrowHeight);
                    path.lineTo(leftX + arrowLeftOffset + arrowWidth / 2.0F, bottomY);
                    path.lineTo(leftX + arrowLeftOffset, bottomY - arrowHeight);
                    path.arcTo(leftX, bottomY - arrowHeight - strokeRadiusLeftBottom, leftX + strokeRadiusLeftBottom, bottomY - arrowHeight, 90, 90,
                            false);
                    path.arcTo(leftX, topY, leftX + strokeRadiusLeftTop, topY + strokeRadiusLeftTop, 180, 90, false);
                    break;
                default:
                    break;
            }
            return path;
        } else {
            return null;
        }
    }

    /**
     * 获取箭头距顶部偏移，不包含箭头宽度
     *
     * @return 偏移距离
     */
    private float getArrowTopOffset(float height) {
        float top = 0F;
        switch (arrowDirection) {
            case BUBBLE_DIRECTION_LEFT:
                if (arrowOffsetTopPercent != 0) {
                    top = height * arrowOffsetTopPercent;
                } else if (arrowOffsetBottomPercent != 0) {
                    top = height - height * arrowOffsetBottomPercent - arrowWidth;
                } else if (arrowOffsetTop != 0) {
                    top = arrowOffsetTop;
                } else if (arrowOffsetBottom != 0) {
                    top = height - arrowOffsetBottom - arrowWidth;
                }
                if (top < strokeRadiusLeftTop / 2.0F) {
                    arrowWidth = 0;
                    arrowHeight = 0;
                    top = strokeRadiusLeftTop / 2.0F;
                } else if (top > height - strokeRadiusRightTop / 2.0F) {
                    arrowWidth = 0;
                    arrowHeight = 0;
                    top = height - strokeRadiusRightTop / 2.0F;
                }
                break;
            case BUBBLE_DIRECTION_RIGHT:
                if (arrowOffsetTopPercent != 0) {
                    top = height * arrowOffsetTopPercent;
                } else if (arrowOffsetBottomPercent != 0) {
                    top = height - height * arrowOffsetBottomPercent - arrowWidth;
                } else if (arrowOffsetTop != 0) {
                    top = arrowOffsetTop;
                } else if (arrowOffsetBottom != 0) {
                    top = height - arrowOffsetBottom - arrowWidth;
                }
                if (top < strokeRadiusLeftBottom / 2.0F) {
                    arrowWidth = 0;
                    arrowHeight = 0;
                    top = strokeRadiusLeftBottom / 2.0F;
                } else if (top > height - strokeRadiusRightBottom / 2.0F) {
                    arrowWidth = 0;
                    arrowHeight = 0;
                    top = height - strokeRadiusRightBottom / 2.0F;
                }
                break;
            default:
                break;
        }
        return top;
    }

    /**
     * 获取箭头距左侧偏移，不包含箭头宽度
     *
     * @return 偏移距离
     */
    private float getArrowLeftOffset(float width) {
        float left = 0F;
        switch (arrowDirection) {
            case BUBBLE_DIRECTION_TOP:
                if (arrowOffsetLeftPercent != 0) {
                    left = width * arrowOffsetLeftPercent;
                } else if (arrowOffsetRightPercent != 0) {
                    left = width - width * arrowOffsetRightPercent - arrowWidth;
                } else if (arrowOffsetLeft != 0) {
                    left = arrowOffsetLeft;
                } else if (arrowOffsetRight != 0) {
                    left = width - arrowOffsetRight - arrowWidth;
                }
                if (left < (strokeRadiusLeftTop / 2.0F)) {
                    arrowWidth = 0;
                    arrowHeight = 0;
                    left = strokeRadiusLeftTop / 2.0F;
                } else if (left > (width - strokeRadiusRightTop / 2.0F)) {
                    arrowWidth = 0;
                    arrowHeight = 0;
                    left = width - strokeRadiusRightTop / 2.0F;
                }
                break;
            case BUBBLE_DIRECTION_BOTTOM:
                if (arrowOffsetLeftPercent != 0) {
                    left = width * arrowOffsetLeftPercent;
                } else if (arrowOffsetRightPercent != 0) {
                    left = width - width * arrowOffsetRightPercent - arrowWidth;
                } else if (arrowOffsetLeft != 0) {
                    left = arrowOffsetLeft;
                } else if (arrowOffsetRight != 0) {
                    left = width - arrowOffsetRight - arrowWidth;
                }
                if (left < strokeRadiusLeftBottom / 2.0F) {
                    arrowWidth = 0;
                    arrowHeight = 0;
                    left = strokeRadiusLeftBottom / 2.0F;
                } else if (left > width - strokeRadiusRightBottom / 2.0F) {
                    arrowWidth = 0;
                    arrowHeight = 0;
                    left = width - strokeRadiusRightBottom / 2.0F;
                }
                break;
            default:
                break;
        }
        return left;
    }

    /**
     * 修改内边距
     */
    private void changePadding() {
        int left = shadowWidth + strokeWidth + getPaddingLeft();
        int top = shadowWidth + strokeWidth + getPaddingTop();
        int right = shadowWidth + strokeWidth + getPaddingRight();
        int bottom = shadowWidth + strokeWidth + getPaddingBottom();
        switch (arrowDirection) {
            case BUBBLE_DIRECTION_TOP:
                top += arrowHeight;
                break;
            case BUBBLE_DIRECTION_BOTTOM:
                bottom += arrowHeight;
                break;
            case BUBBLE_DIRECTION_LEFT:
                left = left + arrowHeight;
                break;
            case BUBBLE_DIRECTION_RIGHT:
                right = right + arrowHeight;
            default:
                break;
        }
        setPadding(left, top, right, bottom);
    }

    /**
     * 重新设置背景图片
     *
     * @param background 背景图片
     */
    private void resetBgBitmap(Drawable background) {
        oldBgDrawable = background;
        if (background == null || shadowPath == null) {
            bgBitmap = null;
        } else {
            Bitmap.Config config = background.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
            // 建立对应 bitmap
            bgBitmap = Bitmap.createBitmap(getWidth(), getHeight(), config);
            // 建立对应 bitmap 的画布
            Canvas canvas = new Canvas(bgBitmap);
            background.setBounds(0, 0, getWidth(), getHeight());
            // 把 drawable 内容画到画布中
            background.draw(canvas);
            //获取输出的位图
            Bitmap output = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(output);
            //画板背景透明
            canvas.drawARGB(0, 0, 0, 0);
            //初始化画笔
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            //绘制圆角
            canvas.drawPath(shadowPath, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            //绘制位图
            canvas.drawBitmap(bgBitmap, 0, 0, paint);
            bgBitmap.recycle();
            bgBitmap = null;
            bgBitmap = output;
        }
    }
}
