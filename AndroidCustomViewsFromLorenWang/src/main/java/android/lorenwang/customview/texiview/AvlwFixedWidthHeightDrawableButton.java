package android.lorenwang.customview.texiview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.lorenwang.customview.R;
import android.lorenwang.tools.app.AtlwViewUtils;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.drawable.DrawableCompat;

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
    private final int DRAWABLE_POSITION_NONE = 0;
    private final int DRAWABLE_POSITION_LEFT = 1;
    private final int DRAWABLE_POSITION_TOP = 2;
    private final int DRAWABLE_POSITION_RIGHT = 3;
    private final int DRAWABLE_POSITION_BOTTOM = 4;
    /**
     * 图片位置
     */
    private int drawablePosition = DRAWABLE_POSITION_NONE;
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
     * 要绘制的图片
     */
    private Bitmap drawBitmap;
    private Rect drawBitmapSrcRect;
    private RectF drawBitmapDstRect;
    private ColorStateList tint;

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
        drawableResId = attributes.getResourceId(R.styleable.AvlwFixedWidthHeightDrawableButton_avlwDrawableResId, -1);
        if (Integer.MAX_VALUE != attributes.getColor(R.styleable.AvlwFixedWidthHeightDrawableButton_avlwDrawableTint, Integer.MAX_VALUE)) {
            tint = ColorStateList.valueOf(attributes.getColor(R.styleable.AvlwFixedWidthHeightDrawableButton_avlwDrawableTint, 0));
        }
        attributes.recycle();

        //默认属性处理
        setMinWidth(0);
        setMinHeight(0);
        setMinEms(0);
        setMinimumWidth(0);
        setMinimumHeight(0);
        setBackground(null);
        setAllCaps(false);
        setIncludeFontPadding(false);

        //设置资源文件
        setShowDrawableResId(drawableResId);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initViewAndDrawableInfo();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (drawBitmapDstRect == null && getWidth() > 0 && getHeight() > 0) {
            initViewAndDrawableInfo();
        }
        if (drawBitmap != null && drawBitmapDstRect != null) {
            canvas.drawBitmap(drawBitmap, drawBitmapSrcRect, drawBitmapDstRect, null);
        }
        super.onDraw(canvas);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        initViewAndDrawableInfo();
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
                drawBitmapDstRect = null;
            }
        } else {
            if (drawBitmap != null && !drawBitmap.isRecycled()) {
                drawBitmap.recycle();
                drawBitmap = null;
            }
            drawBitmapSrcRect = null;
            drawBitmapDstRect = null;
        }

        initViewAndDrawableInfo();
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
                invalidate();
            }
        }
    }

    /**
     * 初始化视图以及图片信息
     */
    private void initViewAndDrawableInfo() {
        //实际宽高
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        if (viewHeight > 0 && viewWidth > 0) {
            //边距处理
            float left = getPaddingStart();
            float top = getPaddingTop();
            float bottom = getPaddingBottom();
            float right = getPaddingEnd();

            //文本宽高
            Paint paint = new Paint();
            paint.setTextSize(getTextSize());
            //宽度需要一个像素冗余，否则可能会出现英文时自动换行的情况
            float textWidth = AtlwViewUtils.getInstance().getStrTextWidth(paint, getText().toString());
            float textHeight = AtlwViewUtils.getInstance().getStrTextHeight(paint);

            //图片绘制起始点x、y坐标
            float drawableStartX = left;
            float drawableStartY = top;

            //兼容后内边距处理
            switch (drawablePosition) {
                case DRAWABLE_POSITION_LEFT:
                    //左内边距以及图片绘制位置处理
                    if (judgeGravity(Gravity.END)) {
                        left = Math.max(drawableWidth + drawableTextDistance, viewWidth - textWidth - right);
                    } else if (judgeGravity(Gravity.CENTER)) {
                        left = Math.max(drawableWidth + drawableTextDistance, left + (viewWidth - left - right - textWidth) / 2.0f);
                        right = Math.max(viewWidth - left - textWidth, Math.max(right, 0));
                        top = top + (viewHeight - top - bottom - textHeight) / 2.0f;
                        bottom = viewHeight - top - textHeight;
                    } else if (judgeGravity(Gravity.CENTER_HORIZONTAL)) {
                        left = Math.max(drawableWidth + drawableTextDistance, left + (viewWidth - left - right - textWidth) / 2.0f);
                        right = Math.max(viewWidth - left - textWidth, Math.max(right, 0));
                    } else if (judgeGravity(Gravity.START) || judgeGravity(Gravity.TOP)) {
                        left = Math.max(drawableWidth + drawableTextDistance, left);
                    }

                    //上下边距处理
                    if (drawableHeight > viewHeight) {
                        bottom = top = (drawableHeight - viewHeight) / 2.0f;
                    }

                    //图片绘制坐标
                    drawableStartX = left - drawableWidth - drawableTextDistance;
                    if (drawableHeight > textHeight) {
                        drawableStartY = top - (drawableHeight - textHeight) / 2.0f;
                    } else {
                        drawableStartY = top + (textHeight - drawableHeight) / 2.0f;
                    }
                    break;
                case DRAWABLE_POSITION_TOP:
                    //上内边距以及图片绘制位置处理
                    if (judgeGravity(Gravity.BOTTOM)) {
                        top = Math.max(drawableHeight + drawableTextDistance, viewHeight - textHeight - bottom);
                    } else if (judgeGravity(Gravity.CENTER_VERTICAL) || judgeGravity(Gravity.CENTER)) {
                        top = Math.max(drawableHeight + drawableTextDistance, top + (viewHeight - top - bottom - textHeight) / 2.0f);
                        bottom = Math.max(viewHeight - top - textHeight, Math.max(bottom, Math.max(bottom, 0)));
                        left = Math.max(left + (viewWidth - left - right - textWidth) / 2.0f, Math.max(left, 0));
                        right = Math.max(viewWidth - left - textWidth, Math.max(right, 0));
                    } else if (judgeGravity(Gravity.START) || judgeGravity(Gravity.TOP) || judgeGravity(Gravity.END)) {
                        top = Math.max(drawableHeight + drawableTextDistance, top);
                    }

                    //左右边距处理
                    if (drawableWidth > viewWidth) {
                        left = right = (drawableWidth - viewWidth) / 2.0f;
                    }

                    //图片绘制坐标
                    if (drawableWidth > textWidth) {
                        drawableStartX = left - (drawableWidth - textWidth) / 2.0f;
                    } else {
                        drawableStartX = left + (textWidth - drawableWidth) / 2.0f;
                    }
                    drawableStartY = top - drawableHeight - drawableTextDistance;
                    break;
                case DRAWABLE_POSITION_RIGHT:
                    //右边距以及图片绘制位置处理
                    if (judgeGravity(Gravity.END)) {
                        right = Math.max(drawableWidth + drawableTextDistance, right);
                    } else if (judgeGravity(Gravity.CENTER)) {
                        right = Math.max(drawableWidth + drawableTextDistance, right + (viewWidth - left - right - textWidth) / 2.0f);
                        left = Math.max(viewWidth - right - textWidth, Math.max(left, 0));
                        top = top + (viewHeight - top - bottom - textHeight) / 2.0f;
                        bottom = viewHeight - top - textHeight;
                    } else if (judgeGravity(Gravity.CENTER_HORIZONTAL)) {
                        right = Math.max(drawableWidth + drawableTextDistance, right + (viewWidth - left - right - textWidth) / 2.0f);
                        left = Math.max(viewWidth - right - textWidth, Math.max(left, 0));
                    } else if (judgeGravity(Gravity.START) || judgeGravity(Gravity.TOP)) {
                        right = Math.max(drawableWidth + drawableTextDistance, viewWidth - textWidth - left);
                    }

                    //上下边距处理
                    if (drawableHeight > viewHeight) {
                        bottom = top = (drawableHeight - viewHeight) / 2.0f;
                    }

                    //图片绘制坐标
                    drawableStartX = left + textWidth + drawableTextDistance;
                    if (drawableHeight > textHeight) {
                        drawableStartY = top - (drawableHeight - textHeight) / 2.0f;
                    } else {
                        drawableStartY = top + (textHeight - drawableHeight) / 2.0f;
                    }
                    break;
                case DRAWABLE_POSITION_BOTTOM:
                    //下边距以及图片绘制位置处理
                    if (judgeGravity(Gravity.BOTTOM)) {
                        bottom = Math.max(drawableHeight + drawableTextDistance, bottom);
                    } else if (judgeGravity(Gravity.CENTER_VERTICAL) || judgeGravity(Gravity.CENTER)) {
                        bottom = Math.max(drawableHeight + drawableTextDistance, bottom + (viewHeight - top - bottom - textHeight) / 2.0f);
                        top = Math.max(viewHeight - bottom - textHeight, Math.max(top,0));
                        left = Math.max(left + (viewWidth - left - right - textWidth) / 2.0f, Math.max(left,0));
                        right = Math.max(viewWidth - left - textWidth, 0);
                    } else if (judgeGravity(Gravity.START) || judgeGravity(Gravity.TOP) || judgeGravity(Gravity.END)) {
                        bottom = Math.max(drawableHeight + drawableTextDistance, viewHeight - textHeight - top);
                    }
                    //左右边距处理
                    if (drawableWidth > viewWidth) {
                        left = right = (drawableWidth - viewWidth) / 2.0f;
                    }
                    //图片绘制坐标
                    if (drawableWidth > textWidth) {
                        drawableStartX = left - (drawableWidth - textWidth) / 2.0f;
                    } else {
                        drawableStartX = left + (textWidth - drawableWidth) / 2.0f;
                    }
                    drawableStartY = top + textHeight + drawableTextDistance;
                    break;
                default:
                    break;
            }

            //图片绘制区域
            drawBitmapDstRect = new RectF(drawableStartX, drawableStartY, drawableStartX + drawableWidth
                    , drawableStartY + drawableHeight);

            //判断内边距是否需要修改
            if (left != getPaddingStart() || top != getPaddingTop() || right != getPaddingEnd() || bottom != getPaddingBottom()) {
                super.setPadding((int) left, (int) top, (int) right, (int) bottom);
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
            //第一0是距左右边距离，第二0
            drawable.setBounds(0, 0, this.drawableWidth, this.drawableHeight);
            // 是距上下边距离，第三69长度,第四宽度
            final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            if (tint != null) {
                DrawableCompat.setTintList(wrappedDrawable, tint);
            }
            // 取drawable的长宽
            int width = wrappedDrawable.getIntrinsicWidth();
            int height = wrappedDrawable.getIntrinsicHeight();
            // 取drawable的颜色格式
            Bitmap.Config config = wrappedDrawable.getOpacity() != PixelFormat.OPAQUE ?
                    Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
            // 建立对应bitmap
            bitmap = Bitmap.createBitmap(width, height, config);
            // 建立对应bitmap的画布
            Canvas canvas = new Canvas(bitmap);
            wrappedDrawable.setBounds(0, 0, width, height);
            // 把drawable内容画到画布中
            wrappedDrawable.draw(canvas);
        }
        //取图片的区域
        if (drawBitmap != null && !drawBitmap.isRecycled()) {
            drawBitmapSrcRect = new Rect(0, 0, drawBitmap.getWidth(), drawBitmap.getHeight());
        }
        return bitmap;
    }

    /**
     * 判断当前的文本位置
     *
     * @param targetGravity 要比较的位置
     * @return 是的话返回true
     */
    private boolean judgeGravity(int targetGravity) {
        return (getGravity() & targetGravity) == targetGravity;
    }
}
