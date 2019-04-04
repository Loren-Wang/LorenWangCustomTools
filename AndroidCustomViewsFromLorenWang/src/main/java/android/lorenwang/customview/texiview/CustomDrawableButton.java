package android.lorenwang.customview.texiview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.lorenwang.customview.R;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.Gravity;

public class CustomDrawableButton extends android.support.v7.widget.AppCompatButton {

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
    public CustomDrawableButton(Context context) {
        super(context);
        init(context, null, -1);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomDrawableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomDrawableButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomDrawableButton);
        drawablePosi = attributes.getInt(R.styleable.CustomDrawableButton_drawablePosi, drawablePosi);
        drawableWidth = attributes.getDimensionPixelOffset(R.styleable.CustomDrawableButton_drawableWidth, drawableWidth);
        drawableHeight = attributes.getDimensionPixelOffset(R.styleable.CustomDrawableButton_drawableHeight, drawableHeight);
        drawableTextDistance = attributes.getDimensionPixelOffset(R.styleable.CustomDrawableButton_drawableTextDistance, drawableTextDistance);
        drawableResId = attributes.getResourceId(R.styleable.CustomDrawableButton_drawableResId, -1);

        setDrawable(drawablePosi, drawableWidth, drawableHeight, drawableTextDistance, drawableResId);

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
        setDrawable(drawablePosi, drawableWidth, drawableHeight, drawableTextDistance, drawableResId);
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
     * @param drawableResId
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceType")
    public void serShowDrawableResId(@DrawableRes int drawableResId) {
        if (drawableResId > 0) {
            drawBitmap = getDrawBitmap(drawableResId);
            drawBitmapSrcRect = new Rect(0, 0, drawBitmap.getWidth(), drawBitmap.getHeight());
            setDrawable(drawablePosi, drawableWidth, drawableHeight, drawableTextDistance, drawableResId);
            invalidate();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        setDrawable(drawablePosi, drawableWidth, drawableHeight, drawableTextDistance, drawableResId);
        invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        setDrawable(drawablePosi, drawableWidth, drawableHeight, drawableTextDistance, drawableResId);
        invalidate();
    }

    @Override
    public void setAllCaps(boolean allCaps) {
        super.setAllCaps(allCaps);
        this.allCaps = allCaps;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setBackgroundTintList(@Nullable ColorStateList tint) {
        this.tint = tint;
        setDrawable(drawablePosi, drawableWidth, drawableHeight, drawableTextDistance, drawableResId);
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
    public synchronized CustomDrawableButton setDrawable(Integer drawablePosi
            , Integer drawableWidth, Integer drawableHeight, Integer drawableTextDistance, @DrawableRes Integer drawableResId) {

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
     * @param drawableResId
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Bitmap getDrawBitmap(int drawableResId){
        Drawable drawable = getContext().getDrawable(drawableResId).mutate();
        drawable.setBounds(0, 0, this.drawableWidth, this.drawableHeight);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        if(tint != null) {
            DrawableCompat.setTintList(wrappedDrawable, tint);
        }

        int width = wrappedDrawable.getIntrinsicWidth();// 取drawable的长宽
        int height = wrappedDrawable.getIntrinsicHeight();
        Bitmap.Config config = wrappedDrawable.getOpacity() != PixelFormat.OPAQUE ?Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;// 取drawable的颜色格式
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);// 建立对应bitmap
        Canvas canvas = new Canvas(bitmap);// 建立对应bitmap的画布
        wrappedDrawable.setBounds(0, 0, width, height);
        wrappedDrawable.draw(canvas);// 把drawable内容画到画布中
        return  bitmap;
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
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        int textWidth = rect.width();
        int textHeight = rect.height();
        int textWidthHalf = textWidth / 2;//文本的宽度
        int textHeightHalf = textHeight / 2;//文本的高度
        rect = null;
        paint = null;


        //如果文字方式是在左上的话那么需要重新设置padding
        if (getGravity() == Gravity.LEFT) {
            //如果位置是在左侧的话
            if (this.drawablePosi == DRAWABLE_POSI_LEFT) {
                if (drawableHeight > rect.height()) {
                    super.setPadding(left + drawableWidth + drawableTextDistance, top + (drawableHeight - rect.height()), right, bottom);
                } else {
                    super.setPadding(left + drawableWidth + drawableTextDistance, top, right, bottom);
                }
                drawBitmapDstRect = new Rect(left, top, left + drawableWidth, top + drawableHeight);
            }
        } else if (getGravity() == (Gravity.LEFT | Gravity.CENTER_VERTICAL)) {
            //如果文字位置是在左侧中间的话同时图片也在左侧
            if (this.drawablePosi == DRAWABLE_POSI_LEFT) {
                super.setPadding(left + drawableWidth + drawableTextDistance, top, right, bottom);
                drawBitmapDstRect = new Rect(left, centerY - drawableHeightHalf, left + drawableWidth, centerY + drawableHeightHalf);
            }
        } else if (getGravity() == (Gravity.RIGHT | Gravity.CENTER_VERTICAL)) {
            //如果文字位置是在左侧中间的话同时图片也在左侧
            if (this.drawablePosi == DRAWABLE_POSI_LEFT) {
                drawBitmapDstRect = new Rect(getMeasuredWidth() - right - textWidth - drawableTextDistance - drawableWidth
                        , centerY - drawableHeightHalf, getMeasuredWidth() - right - textWidth - drawableTextDistance
                        , centerY + drawableHeightHalf);
            }
        } else {
            //其他的先都按默认走
            setGravity(Gravity.CENTER);
            super.setPadding(left, top, right, bottom);
            switch (this.drawablePosi) {
                case DRAWABLE_POSI_LEFT:
                    drawBitmapDstRect = new Rect(centerX - textWidthHalf - this.drawableTextDistance - this.drawableWidth
                            , centerY - drawableHeightHalf
                            , centerX - textWidthHalf - this.drawableTextDistance
                            , centerY + drawableHeightHalf);
                    break;
                case DRAWABLE_POSI_TOP:
                    drawBitmapDstRect = new Rect(centerX - drawableWidthHalf
                            , centerY - textHeightHalf - this.drawableTextDistance - this.drawableHeight
                            , centerX + drawableWidthHalf
                            , centerY - textHeightHalf - this.drawableTextDistance);
                    break;
                case DRAWABLE_POSI_RIGHT:
                    drawBitmapDstRect = new Rect(centerX + textWidthHalf + this.drawableTextDistance
                            , centerY - drawableHeightHalf
                            , centerX + textWidthHalf + this.drawableTextDistance + this.drawableWidth
                            , centerY + drawableHeightHalf);
                    break;
                case DRAWABLE_POSI_BOTTOM:
                    drawBitmapDstRect = new Rect(centerX - drawableWidthHalf
                            , centerY + textHeightHalf + this.drawableTextDistance
                            , centerX + drawableWidthHalf
                            , centerY + textHeightHalf + this.drawableTextDistance + this.drawableHeight);
                    break;
                case DRAWABLE_POSI_NONE:
                default:
                    drawBitmapDstRect = null;
                    break;
            }
        }
    }
}
