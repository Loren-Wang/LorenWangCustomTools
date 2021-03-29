package android.lorenwang.customview.texiview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.lorenwang.customview.R;
import android.lorenwang.tools.app.AtlwViewUtil;
import android.lorenwang.tools.image.AtlwImageCommonUtil;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatButton;

/**
 * 功能作用：固定宽高按钮
 * 创建时间：2020-12-23 1:54 下午
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
public class AvlwFixedWidthHeightDrawableButton extends AppCompatButton {
    /**
     * 取值为none的时候代表着不显示图片
     */
    public final int DRAWABLE_POSITION_NONE = 0;
    public final int DRAWABLE_POSITION_LEFT = 1;
    public final int DRAWABLE_POSITION_TOP = 2;
    public final int DRAWABLE_POSITION_RIGHT = 3;
    public final int DRAWABLE_POSITION_BOTTOM = 4;

    /**
     * 文本显示区域类型
     */
    private final int TEXT_GRAVITY_TOP = 1;
    private final int TEXT_GRAVITY_BOTTOM = 2;
    private final int TEXT_GRAVITY_LEFT = 3;
    private final int TEXT_GRAVITY_START = 4;
    private final int TEXT_GRAVITY_RIGHT = 5;
    private final int TEXT_GRAVITY_END = 6;
    private final int TEXT_GRAVITY_CENTER_VERTICAL_LEFT = 7;
    private final int TEXT_GRAVITY_CENTER_VERTICAL_RIGHT = 8;
    private final int TEXT_GRAVITY_CENTER_HORIZONTAL_TOP = 9;
    private final int TEXT_GRAVITY_CENTER_HORIZONTAL_BOTTOM = 10;
    private final int TEXT_GRAVITY_CENTER = 11;
    private final int TEXT_GRAVITY_RIGHT_BOTTOM = 12;

    /**
     * 文本显示位置，使用属性传递，不读取上层控件的，上层控件的读取太费劲
     */
    private int textShowGravity = 7;
    /**
     * 图片位置
     */
    private int drawablePosition = 0;
    /**
     * 图片宽度
     */
    private int drawableWidth = 0;
    /**
     * 图片高度
     */
    private int drawableHeight = 0;
    /**
     * 图片文字间距
     */
    private int drawableTextDistance = 0;
    /**
     * 图片资源id
     */
    private int drawableResId;
    /**
     * 图片是否占用内边距，默认占用
     */
    private boolean drawableUsePadding = true;
    /**
     * 要绘制的图片
     */
    private Bitmap drawBitmap;
    private Rect drawBitmapSrcRect;
    private RectF drawBitmapDstRect;
    private ColorStateList tint;

    private int paddingLeft = 0;
    private int paddingTop = 0;
    private int paddingRight = 0;
    private int paddingBottom = 0;

    /**
     * 控件宽度
     */
    private int viewWidth = 0;

    /**
     * 控件高度
     */
    private int viewHeight = 0;

    public AvlwFixedWidthHeightDrawableButton(Context context) {
        super(context);
        init(context, null, -1);
    }

    public AvlwFixedWidthHeightDrawableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public AvlwFixedWidthHeightDrawableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        //资源处理
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AvlwFixedWidthHeightDrawableButton);
        drawablePosition = attributes.getInt(R.styleable.AvlwFixedWidthHeightDrawableButton_avlwDrawablePosition, drawablePosition);
        drawableWidth = attributes.getDimensionPixelOffset(R.styleable.AvlwFixedWidthHeightDrawableButton_avlwDrawableWidth, drawableWidth);
        drawableHeight = attributes.getDimensionPixelOffset(R.styleable.AvlwFixedWidthHeightDrawableButton_avlwDrawableHeight, drawableHeight);
        drawableTextDistance = attributes.getDimensionPixelOffset(R.styleable.AvlwFixedWidthHeightDrawableButton_avlwDrawableTextDistance,
                drawableTextDistance);
        drawableUsePadding = attributes.getBoolean(R.styleable.AvlwFixedWidthHeightDrawableButton_avlwDrawableUsePadding, drawableUsePadding);
        drawableResId = attributes.getResourceId(R.styleable.AvlwFixedWidthHeightDrawableButton_avlwDrawableResId, -1);
        textShowGravity = attributes.getInt(R.styleable.AvlwFixedWidthHeightDrawableButton_avlwTextGravity, textShowGravity);
        if (Integer.MAX_VALUE != attributes.getColor(R.styleable.AvlwFixedWidthHeightDrawableButton_avlwDrawableTint, Integer.MAX_VALUE)) {
            tint = ColorStateList.valueOf(attributes.getColor(R.styleable.AvlwFixedWidthHeightDrawableButton_avlwDrawableTint, 0));
        }
        attributes.recycle();

        //控件背景设置
        attributes = context.obtainStyledAttributes(attrs,
                new int[]{android.R.attr.background, android.R.attr.paddingTop, android.R.attr.paddingBottom, android.R.attr.paddingStart,
                        android.R.attr.paddingEnd, android.R.attr.layout_width});
        Drawable drawable = attributes.getDrawable(0);
        if (drawable == null) {
            setBackground(null);
        }
        paddingLeft = attributes.getDimensionPixelOffset(3, 0);
        paddingTop = attributes.getDimensionPixelOffset(1, 0);
        paddingRight = attributes.getDimensionPixelOffset(4, 0);
        paddingBottom = attributes.getDimensionPixelOffset(2, 0);

        //默认属性处理
        if (drawablePosition == DRAWABLE_POSITION_LEFT || drawablePosition == DRAWABLE_POSITION_RIGHT) {
            setMinWidth(drawableWidth + drawableTextDistance);
        } else {
            setMinWidth(drawableWidth);
        }
        if (drawablePosition == DRAWABLE_POSITION_TOP || drawablePosition == DRAWABLE_POSITION_BOTTOM) {
            setMinHeight(drawableHeight + drawableTextDistance);
        } else {
            setMinHeight(drawableHeight);
        }
        setMinEms(0);
        setMinimumWidth(0);
        setMinimumHeight(0);
        setAllCaps(false);
        setMaxLines(1);
        setIncludeFontPadding(false);
        //设置资源文件
        setShowDrawableResId(drawableResId);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                drawBitmapDstRect = null;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        paddingLeft = left;
        paddingRight = right;
        paddingTop = top;
        paddingBottom = bottom;
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawBitmapDstRect == null) {
            changePadding();
        }
        drawBitmapDstRect = getBitmapShowRect();
        if (drawBitmap != null && drawBitmapDstRect != null) {
            if (drawBitmapDstRect.width() > 0 && drawBitmapDstRect.height() > 0) {
                canvas.drawBitmap(drawBitmap, drawBitmapSrcRect, drawBitmapDstRect, getPaint());
            }
        }
    }

    /**
     * 获取图片显示位置
     *
     * @return 图片显示位置
     */
    private RectF getBitmapShowRect() {
        if (viewWidth <= 0 || viewHeight <= 0) {
            return null;
        }
        //文本显示区域
        RectF textShowRect = getTextShowRect();
        //位图显示区域
        RectF bitmapShowRect = new RectF();
        //区域处理
        switch (drawablePosition) {
            case DRAWABLE_POSITION_LEFT:
                bitmapShowRect.left = Math.max(textShowRect.left, drawableWidth + drawableTextDistance) - drawableWidth - drawableTextDistance;
                if (drawableHeight > textShowRect.height()) {
                    bitmapShowRect.top = textShowRect.top - (drawableHeight - textShowRect.height()) / 2;
                } else {
                    bitmapShowRect.top = textShowRect.top + (textShowRect.height() - drawableHeight) / 2;
                }
                break;
            case DRAWABLE_POSITION_RIGHT:
                bitmapShowRect.left = textShowRect.right + drawableTextDistance;
                if (drawableHeight > textShowRect.height()) {
                    bitmapShowRect.top = textShowRect.top - (drawableHeight - textShowRect.height()) / 2;
                } else {
                    bitmapShowRect.top = textShowRect.top + (textShowRect.height() - drawableHeight) / 2;
                }
                break;
            case DRAWABLE_POSITION_TOP:
                bitmapShowRect.top = Math.max(textShowRect.top, drawableHeight + drawableTextDistance) - drawableHeight - drawableTextDistance;
                if (drawableWidth > textShowRect.width()) {
                    bitmapShowRect.left = textShowRect.left - (drawableWidth - textShowRect.width()) / 2;
                } else {
                    bitmapShowRect.left = textShowRect.left + (textShowRect.width() - drawableWidth) / 2;
                }
                break;
            case DRAWABLE_POSITION_BOTTOM:
                bitmapShowRect.top = viewHeight - Math.max(viewHeight - textShowRect.bottom, drawableHeight + drawableTextDistance) +
                        drawableTextDistance;
                if (drawableWidth > textShowRect.width()) {
                    bitmapShowRect.left = textShowRect.left - (drawableWidth - textShowRect.width()) / 2;
                } else {
                    bitmapShowRect.left = textShowRect.left + (textShowRect.width() - drawableWidth) / 2;
                }
                break;
            default:
                break;
        }

        bitmapShowRect.left = Math.max(bitmapShowRect.left, 0);
        bitmapShowRect.top = Math.max(bitmapShowRect.top, 0);
        bitmapShowRect.right = bitmapShowRect.left + drawableWidth;
        bitmapShowRect.bottom = bitmapShowRect.top + drawableHeight;
        switch (drawablePosition) {
            case DRAWABLE_POSITION_LEFT:
                if (bitmapShowRect.bottom > viewHeight) {
                    bitmapShowRect.bottom = viewHeight;
                    if (drawableHeight <= viewHeight) {
                        bitmapShowRect.top = bitmapShowRect.bottom - drawableHeight;
                    }
                }
                if (bitmapShowRect.right > viewWidth) {
                    bitmapShowRect.right = viewWidth;
                    if (drawableWidth <= viewWidth) {
                        bitmapShowRect.left = bitmapShowRect.right - drawableWidth;
                    }
                }
                break;
            case DRAWABLE_POSITION_RIGHT:
                if (bitmapShowRect.bottom > viewHeight) {
                    bitmapShowRect.bottom = viewHeight;
                    if (drawableHeight <= viewHeight) {
                        bitmapShowRect.top = bitmapShowRect.bottom - drawableHeight;
                    }
                }
                //自适应不需要左侧偏移处理
                if (!(getLayoutParams() != null && getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT)) {
                    if (bitmapShowRect.right > viewWidth) {
                        bitmapShowRect.right = viewWidth;
                        if (drawableWidth <= viewWidth) {
                            bitmapShowRect.left = bitmapShowRect.right - drawableWidth;
                        }
                    }
                }
                break;
            case DRAWABLE_POSITION_TOP:
            case DRAWABLE_POSITION_BOTTOM:
            default:
                break;
        }
        return bitmapShowRect;
    }

    /**
     * 修改内边距
     */
    private void changePadding() {
        RectF bitmapShowRect = getBitmapShowRect();
        if (bitmapShowRect != null && bitmapShowRect.width() > 0 && bitmapShowRect.height() > 0) {
            RectF textShowRect = getTextShowRect();
            float left = paddingLeft;
            float right = paddingRight;
            float top = paddingTop;
            float bottom = paddingBottom;
            switch (drawablePosition) {
                case DRAWABLE_POSITION_LEFT:
                    if (textShowRect.left < drawableWidth + drawableTextDistance) {
                        left = getPaddingValue(left, drawableWidth + drawableTextDistance);
                    }
                    if (textShowRect.height() < drawableHeight) {
                        float more = Math.max((drawableHeight - textShowRect.height()) / 2, 0);
                        if (bitmapShowRect.top < more) {
                            top = getPaddingValue(top, more);
                        }
                        if (bitmapShowRect.bottom > viewHeight - more) {
                            bottom = getPaddingValue(bottom, more);
                        }
                    }
                    break;
                case DRAWABLE_POSITION_RIGHT:
                    if ((viewWidth - textShowRect.right) < drawableWidth + drawableTextDistance) {
                        right = getPaddingValue(right, drawableWidth + drawableTextDistance);
                    }
                    if (textShowRect.height() < drawableHeight) {
                        float more = Math.max((drawableHeight - textShowRect.height()) / 2, 0);
                        if (bitmapShowRect.top < more) {
                            top = getPaddingValue(top, more);
                        }
                        if (bitmapShowRect.bottom > viewHeight - more) {
                            bottom = getPaddingValue(bottom, more);
                        }
                    }
                    break;
                case DRAWABLE_POSITION_TOP:
                    top = getPaddingValue(top, bitmapShowRect.bottom + drawableTextDistance);
                    bottom = getPaddingValue(bottom, viewHeight - top - textShowRect.height());
                    if (textShowRect.width() < drawableWidth) {
                        float more = Math.max((drawableWidth - textShowRect.width()) / 2, 0);
                        if (bitmapShowRect.left < more) {
                            left = getPaddingValue(left, more);
                        }
                        if (bitmapShowRect.right > viewWidth - more) {
                            right = getPaddingValue(right, more);
                        }
                    }
                    break;
                case DRAWABLE_POSITION_BOTTOM:
                    top = getPaddingValue(top, bitmapShowRect.top - drawableTextDistance - textShowRect.height());
                    bottom = getPaddingValue(bottom, viewHeight - top - textShowRect.height());
                    if (textShowRect.width() < drawableWidth) {
                        float more = Math.max((drawableWidth - textShowRect.width()) / 2, 0);
                        if (bitmapShowRect.left < more) {
                            left = getPaddingValue(left, more);
                        }
                        if (bitmapShowRect.right > viewWidth - more) {
                            right = getPaddingValue(right, more);
                        }
                    }
                    break;
                default:
                    super.setPadding((int) left, (int) top, (int) right, (int) bottom);
                    break;
            }

            if (left != paddingLeft || right != paddingRight || top != paddingTop || bottom != paddingBottom) {
                super.setPadding((int) left, (int) top, (int) right, (int) bottom);
            }
        }
        //边距修改后一定要重新计算位图数据
        drawBitmapDstRect = getBitmapShowRect();
    }

    /**
     * 获取内边距不同状态值
     */
    private float getPaddingValue(float origin, float change) {
        if (drawableUsePadding) {
            return change;
        } else {
            return origin + change;
        }
    }

    /**
     * 设置图片显示位置
     *
     * @param drawablePosition 图片要展示的位置
     * @return 当前实例
     */
    public synchronized AvlwFixedWidthHeightDrawableButton setDrawable(Integer drawablePosition, Integer drawableWidth, Integer drawableHeight,
            Integer drawableTextDistance, @DrawableRes Integer drawableResId) {
        if (drawablePosition != null) {
            this.drawablePosition = drawablePosition;
        }
        if (drawableWidth != null) {
            this.drawableWidth = drawableWidth;
        }
        if (drawableHeight != null) {
            this.drawableHeight = drawableHeight;
        }
        if (drawableTextDistance != null) {
            this.drawableTextDistance = drawableTextDistance;
        }
        if (drawableResId != null) {
            this.drawableResId = drawableResId;
        } else {
            this.drawableResId = drawableResId = -1;
        }

        if (drawableResId > 0) {
            drawBitmap = getDrawBitmap(drawableResId);
            if (drawBitmap != null) {
                drawBitmapSrcRect = new Rect(0, 0, drawBitmap.getWidth(), drawBitmap.getHeight());
            } else {
                drawBitmapSrcRect = null;
            }
        } else {
            if (drawBitmap != null && !drawBitmap.isRecycled()) {
                drawBitmap.recycle();
                drawBitmap = null;
            }
            drawBitmapSrcRect = null;
        }

        changePadding();
        postInvalidate();
        return this;
    }

    /**
     * 设置资源id
     *
     * @param drawableResId 资源id
     */
    public void setShowDrawableResId(@DrawableRes int drawableResId) {
        if (drawableResId > 0) {
            drawBitmap = getDrawBitmap(drawableResId);
            if (drawBitmap != null) {
                setDrawable(drawablePosition, drawableWidth, drawableHeight, drawableTextDistance, drawableResId);
            }
        }
    }

    /**
     * 获取drawable的bitmap
     *
     * @param drawableResId 资源id
     * @return 位图
     */
    private Bitmap getDrawBitmap(int drawableResId) {
        Bitmap bitmap = null;
        Drawable drawable = getContext().getDrawable(drawableResId);
        if (drawable != null) {
            drawable = drawable.mutate();
            bitmap = AtlwImageCommonUtil.getInstance().drawableToBitmap(AtlwViewUtil.getInstance().tintDrawable(drawable, tint), drawableWidth,
                    drawableHeight);
        }
        //取图片的区域
        if (drawBitmap != null && !drawBitmap.isRecycled()) {
            drawBitmapSrcRect = new Rect(0, 0, drawBitmap.getWidth(), drawBitmap.getHeight());
        }
        return bitmap;
    }

    /**
     * 获取文本显示区域
     *
     * @return 文本显示区域
     */
    private RectF getTextShowRect() {
        float textWidth = AtlwViewUtil.getInstance().getStrTextWidth(getPaint(), getText().toString());
        float textHeight = Math.max(AtlwViewUtil.getInstance().getStrTextHeight(getPaint()), getLineHeight());
        int lineCount;
        int paddingLeft = drawableUsePadding ? this.paddingLeft : getPaddingLeft();
        int paddingRight = drawableUsePadding ? this.paddingRight : getPaddingRight();
        int paddingTop = drawableUsePadding ? this.paddingTop : getPaddingTop();
        int paddingBottom = drawableUsePadding ? this.paddingBottom : getPaddingBottom();
        float more = textWidth % (viewWidth - paddingRight - paddingLeft);
        if (more > 0) {
            lineCount = (int) (textWidth / (viewWidth - paddingRight - paddingLeft) + 1);
        } else {
            lineCount = (int) (textWidth / (viewWidth - paddingRight - paddingLeft));
        }
        //文本内容显示宽高
        float showHeight = textHeight * lineCount;
        float showWidth = Math.min(textWidth, viewWidth - paddingRight - paddingLeft);
        //空白区域
        float heightEmptyHalf = Math.max((viewHeight - paddingTop - paddingBottom - showHeight) / 2, 0);
        float widthEmptyHalf = Math.max((viewWidth - paddingLeft - paddingRight - showWidth) / 2, 0);

        float left;
        float top;
        switch (textShowGravity) {
            case TEXT_GRAVITY_CENTER_VERTICAL_LEFT:
                left = paddingLeft;
                top = heightEmptyHalf + paddingTop;
                break;
            case TEXT_GRAVITY_BOTTOM:
                left = paddingLeft;
                top = viewHeight - paddingBottom - showHeight;
                break;
            case TEXT_GRAVITY_CENTER_HORIZONTAL_TOP:
                left = paddingLeft + widthEmptyHalf;
                top = paddingTop;
                break;
            case TEXT_GRAVITY_CENTER_HORIZONTAL_BOTTOM:
                left = paddingLeft + widthEmptyHalf;
                top = viewHeight - paddingBottom - showHeight;
                break;
            case TEXT_GRAVITY_RIGHT:
            case TEXT_GRAVITY_END:
                left = viewWidth - showWidth - paddingRight;
                top = paddingTop;
                break;
            case TEXT_GRAVITY_CENTER_VERTICAL_RIGHT:
                left = viewWidth - showWidth - paddingRight;
                top = heightEmptyHalf + paddingTop;
                break;
            case TEXT_GRAVITY_CENTER:
                left = widthEmptyHalf + paddingLeft;
                top = heightEmptyHalf + paddingTop;
                break;
            case TEXT_GRAVITY_RIGHT_BOTTOM:
                left = viewWidth - showWidth - paddingRight;
                top = viewHeight - paddingBottom - showHeight;
                break;
            case TEXT_GRAVITY_LEFT:
            case TEXT_GRAVITY_START:
            case TEXT_GRAVITY_TOP:
            default:
                left = paddingLeft;
                top = paddingTop;
                break;
        }
        return new RectF(left, top, left + showWidth, top + showHeight);
    }

}
