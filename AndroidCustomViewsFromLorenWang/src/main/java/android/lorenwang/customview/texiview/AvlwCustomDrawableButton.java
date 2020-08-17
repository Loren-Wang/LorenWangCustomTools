package android.lorenwang.customview.texiview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.lorenwang.customview.R;
import android.lorenwang.tools.app.AtlwViewUtils;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.drawable.DrawableCompat;

public class AvlwCustomDrawableButton extends AppCompatButton {

    private final int DRAWABLE_POSI_NONE = 0;//取值为none的时候代表着不显示图片
    private final int DRAWABLE_POSI_LEFT = 1;
    private final int DRAWABLE_POSI_TOP = 2;
    private final int DRAWABLE_POSI_RIGHT = 3;
    private final int DRAWABLE_POSI_BOTTOM = 4;

    private int drawablePosi = DRAWABLE_POSI_NONE;//图片位置
    private int drawableWidth = 0;//图片宽度
    private int drawableHeight = 0;//图片高度
    private int drawableTextDistance = 0;//图片文字间距
    private int drawableResId;//图片资源id

    //要绘制的图片
    private Bitmap drawBitmap;
    private Rect drawBitmapSrcRect;
    private Rect drawBitmapDstRect;
    private Paint drawBitmapPaint = new Paint();
    private ColorStateList tint;

    private boolean allCaps = false;
    private Integer paddingLeft;
    private Integer paddingTop;
    private Integer paddingRight;
    private Integer paddingBottom;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AvlwCustomDrawableButton(Context context) {
        super(context);
        init(context, null, -1);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AvlwCustomDrawableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AvlwCustomDrawableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray attributes = context.obtainStyledAttributes(attrs,
                R.styleable.AvlwCustomDrawableButton);
        drawablePosi = attributes.getInt(R.styleable.AvlwCustomDrawableButton_drawablePosi,
                drawablePosi);
        drawableWidth =
                attributes.getDimensionPixelOffset(R.styleable.AvlwCustomDrawableButton_drawableWidth, drawableWidth);
        drawableHeight =
                attributes.getDimensionPixelOffset(R.styleable.AvlwCustomDrawableButton_drawableHeight, drawableHeight);
        drawableTextDistance =
                attributes.getDimensionPixelOffset(R.styleable.AvlwCustomDrawableButton_drawableTextDistance, drawableTextDistance);
        drawableResId =
                attributes.getResourceId(R.styleable.AvlwCustomDrawableButton_drawableResId, -1);
        if (Integer.compare(Integer.MAX_VALUE,
                attributes.getColor(R.styleable.AvlwCustomDrawableButton_drawableTint,
                        Integer.MAX_VALUE)) != 0) {
            tint = ColorStateList.valueOf(attributes.getColor(R.styleable.AvlwCustomDrawableButton_drawableTint, 0));
        }

        setMinWidth(0);
        setMinHeight(0);


        setDrawable(drawablePosi, drawableWidth, drawableHeight, drawableTextDistance,
                drawableResId);

        this.paddingLeft = getPaddingLeft();
        this.paddingTop = getPaddingTop();
        this.paddingRight = getPaddingRight();
        this.paddingBottom = getPaddingBottom();

        setIncludeFontPadding(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setDrawable(drawablePosi, drawableWidth, drawableHeight, drawableTextDistance,
                drawableResId);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (drawBitmap != null && drawBitmapDstRect != null) {
            canvas.drawBitmap(drawBitmap, drawBitmapSrcRect, drawBitmapDstRect, drawBitmapPaint);
        }
        super.onDraw(canvas);
    }

    /**
     * 设置资源id
     *
     * @param drawableResId 资源id
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceType")
    public void serShowDrawableResId(@DrawableRes int drawableResId) {
        if (drawableResId > 0) {
            drawBitmap = getDrawBitmap(drawableResId);
            drawBitmapSrcRect = new Rect(0, 0, drawBitmap.getWidth(), drawBitmap.getHeight());
            setDrawable(drawablePosi, drawableWidth, drawableHeight, drawableTextDistance,
                    drawableResId);
            invalidate();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        setDrawable(drawablePosi, drawableWidth, drawableHeight, drawableTextDistance,
                drawableResId);
        requestLayout();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        setDrawable(drawablePosi, drawableWidth, drawableHeight, drawableTextDistance,
                drawableResId);
        requestLayout();
    }

    @Override
    public void setAllCaps(boolean allCaps) {
        super.setAllCaps(allCaps);
        this.allCaps = allCaps;
    }

    @Override
    public void setCompoundDrawableTintList(ColorStateList tint) {
        this.tint = tint;
        setDrawable(drawablePosi, drawableWidth, drawableHeight, drawableTextDistance,
                drawableResId);
        postInvalidate();
    }

    /**
     * 设置图片显示位置
     *
     * @param drawablePosi
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceType")
    public synchronized AvlwCustomDrawableButton setDrawable(Integer drawablePosi
            , Integer drawableWidth, Integer drawableHeight, Integer drawableTextDistance,
                                                             @DrawableRes Integer drawableResId) {

        if (drawablePosi != null) {
            this.drawablePosi = drawablePosi;
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
            drawBitmapSrcRect = new Rect(0, 0, drawBitmap.getWidth(), drawBitmap.getHeight());
        } else {
            if (drawBitmap != null && !drawBitmap.isRecycled()) {
                drawBitmap.recycle();
                drawBitmap = null;
            }
            drawBitmapSrcRect = null;
            drawBitmapDstRect = null;
        }

        if (paddingLeft == null) {
            paddingLeft = getPaddingLeft();
        }
        if (paddingTop == null) {
            paddingTop = getPaddingTop();
        }
        if (paddingRight == null) {
            paddingRight = getPaddingRight();
        }
        if (paddingBottom == null) {
            paddingBottom = getPaddingBottom();
        }

        //padding改变，做变更
        paddingChanged(paddingLeft, paddingTop, paddingRight, paddingBottom);

        return this;
    }

    /**
     * 获取drawable的bitmap
     *
     * @param drawableResId
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Bitmap getDrawBitmap(int drawableResId) {
        Drawable drawable = getContext().getDrawable(drawableResId).mutate();
        drawable.setBounds(0, 0, this.drawableWidth, this.drawableHeight);//第一0是距左右边距离，第二0
        // 是距上下边距离，第三69长度,第四宽度
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        if (tint != null) {
            DrawableCompat.setTintList(wrappedDrawable, tint);
        }

        int width = wrappedDrawable.getIntrinsicWidth();// 取drawable的长宽
        int height = wrappedDrawable.getIntrinsicHeight();
        Bitmap.Config config = wrappedDrawable.getOpacity() != PixelFormat.OPAQUE ?
                Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;// 取drawable的颜色格式
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);// 建立对应bitmap
        Canvas canvas = new Canvas(bitmap);// 建立对应bitmap的画布
        wrappedDrawable.setBounds(0, 0, width, height);
        wrappedDrawable.draw(canvas);// 把drawable内容画到画布中
        return bitmap;
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        this.paddingLeft = left;
        this.paddingTop = top;
        this.paddingRight = right;
        this.paddingBottom = bottom;
        //padding改变，做变更
        paddingChanged(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }


    public void paddingChanged(int left, int top, int right, int bottom) {

        //中心点坐标
        int centerX = getMeasuredWidth() / 2;
        int centerY = getMeasuredHeight() / 2;
        if (centerX <= 0 || centerY <= 0) {
            return;
        }
        //图片宽高的一半
        int drawableWidthHalf = this.drawableWidth / 2;
        int drawableHeightHalf = this.drawableHeight / 2;

        //获取文字宽高
        String str = getText().toString();
        if (allCaps) {
            str = str.toUpperCase();
        }
        Paint paint = new Paint();
        paint.setTextSize(getTextSize());
        int textWidth = (int) AtlwViewUtils.getInstance().getStrTextWidth(paint, str);
        int textHeight = (int) AtlwViewUtils.getInstance().getStrTextHeight(paint);
        paint = null;

        switch (this.drawablePosi) {
            case DRAWABLE_POSI_LEFT:
                if (drawableHeight > textHeight) {
                    super.setPadding(left + drawableWidth + drawableTextDistance,
                            top + (drawableHeight - textHeight) / 2, right,
                            bottom + (drawableHeight - textHeight) / 2);
                    drawBitmapDstRect = new Rect(left, top, left + drawableWidth,
                            top + drawableHeight);
                } else {
                    super.setPadding(left + drawableWidth + drawableTextDistance, top, right,
                            bottom);
                    drawBitmapDstRect = new Rect(left, top + (textHeight - drawableHeight) / 2,
                            left + drawableWidth,
                            top + (textHeight - drawableHeight) / 2 + drawableHeight);
                }
                break;
            case DRAWABLE_POSI_TOP:
                if (drawableWidth > textWidth) {
                    super.setPadding(left + (drawableWidth - textWidth) / 2,
                            top + drawableHeight + drawableTextDistance,
                            right + (drawableWidth - textWidth) / 2,
                            bottom);

                    drawBitmapDstRect = new Rect(left, top,
                            left + drawableWidth, top + drawableHeight);
                } else {
                    super.setPadding(left, top + drawableHeight + drawableTextDistance, right,
                            bottom);
                    drawBitmapDstRect = new Rect(left + (textWidth - drawableWidth) / 2,
                            top, left + (textWidth - drawableWidth) / 2 + drawableWidth,
                            top + drawableHeight);
                }
                break;
            case DRAWABLE_POSI_RIGHT:
                if (drawableHeight > textHeight) {
                    super.setPadding(left,
                            top + (drawableHeight - textHeight) / 2,
                            right + drawableWidth + drawableTextDistance,
                            bottom+ (drawableHeight - textHeight) / 2);
                    drawBitmapDstRect = new Rect(left + textWidth + drawableTextDistance,
                            top, left + textWidth + drawableTextDistance + drawableWidth,
                            top + drawableHeight);
                } else {
                    super.setPadding(left, top, right + drawableWidth + drawableTextDistance,
                            bottom);
                    drawBitmapDstRect = new Rect(left + textWidth + drawableTextDistance,
                            top + (textHeight - drawableHeight) / 2,
                            left + textWidth + drawableTextDistance + drawableWidth,
                            top + (textHeight - drawableHeight) / 2 + drawableHeight);
                }
                break;
            case DRAWABLE_POSI_BOTTOM:
                if (drawableWidth > textWidth) {
                    super.setPadding(left + (drawableWidth - textWidth) / 2,
                            top, right + (drawableWidth - textWidth) / 2,
                            bottom + drawableHeight + drawableTextDistance);

                    drawBitmapDstRect = new Rect(left, top + textHeight + drawableTextDistance,
                            left + drawableWidth, top + textHeight + drawableTextDistance + drawableHeight);
                } else {
                    super.setPadding(left, top, right,
                            bottom + drawableHeight + drawableTextDistance);
                    drawBitmapDstRect = new Rect(left + (textWidth - drawableWidth) / 2,
                            top + textHeight + drawableTextDistance,
                            left + (textWidth - drawableWidth) / 2 + drawableWidth,
                            top + textHeight + drawableTextDistance + drawableHeight);
                }
                break;
            default:
                break;
        }

    }

    /**
     * 获取显示宽度
     *
     * @return 显示宽度
     */
    private int getMeasureWidth() {
        //获取文字宽高
        String str = getText().toString();
        if (allCaps) {
            str = str.toUpperCase();
        }
        Paint paint = new Paint();
        paint.setTextSize(getTextSize());
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        int textWidth = rect.width();

        if (this.drawablePosi == DRAWABLE_POSI_LEFT || this.drawablePosi == DRAWABLE_POSI_RIGHT) {
            return drawableTextDistance + getPaddingLeft() + getPaddingRight() + +Math.max(textWidth, drawableWidth);
        } else {
            return textWidth;
        }
    }

    /**
     * 获取显示高度
     *
     * @return 显示高度
     */
    private int getMeasureHeight() {
        //获取文字宽高
        String str = getText().toString();
        if (allCaps) {
            str = str.toUpperCase();
        }
        Paint paint = new Paint();
        paint.setTextSize(getTextSize());
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        int textWidth = rect.width();
        int textHeight = rect.height();

        if (this.drawablePosi == DRAWABLE_POSI_LEFT || this.drawablePosi == DRAWABLE_POSI_RIGHT) {
            return drawableTextDistance + getPaddingTop() + getPaddingBottom() + Math.max(textHeight, drawableHeight);
        } else {
            return textHeight;
        }
    }
}
