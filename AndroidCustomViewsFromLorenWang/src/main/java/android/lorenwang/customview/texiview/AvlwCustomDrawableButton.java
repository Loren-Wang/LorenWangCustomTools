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
import android.lorenwang.tools.app.AtlwViewUtils;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * 功能作用：自定义上下左右drawable的按钮控件
 * 初始注释时间： 2020/8/17 7:31 下午
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
public class AvlwCustomDrawableButton extends AppCompatButton {
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
    private Rect drawBitmapDstRect;
    private final Paint drawBitmapPaint = new Paint();
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
        drawablePosition = attributes.getInt(R.styleable.AvlwCustomDrawableButton_drawablePosi,
                drawablePosition);
        drawableWidth =
                attributes.getDimensionPixelOffset(R.styleable.AvlwCustomDrawableButton_drawableWidth, drawableWidth);
        drawableHeight =
                attributes.getDimensionPixelOffset(R.styleable.AvlwCustomDrawableButton_drawableHeight, drawableHeight);
        drawableTextDistance =
                attributes.getDimensionPixelOffset(R.styleable.AvlwCustomDrawableButton_drawableTextDistance, drawableTextDistance);
        drawableResId =
                attributes.getResourceId(R.styleable.AvlwCustomDrawableButton_drawableResId, -1);
        if (Integer.MAX_VALUE != attributes.getColor(R.styleable.AvlwCustomDrawableButton_drawableTint,
                Integer.MAX_VALUE)) {
            tint = ColorStateList.valueOf(attributes.getColor(R.styleable.AvlwCustomDrawableButton_drawableTint, 0));
        }


        setMinWidth(0);
        setMinHeight(0);


        setDrawable(drawablePosition, drawableWidth, drawableHeight, drawableTextDistance,
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
        setDrawable(drawablePosition, drawableWidth, drawableHeight, drawableTextDistance,
                drawableResId);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (drawBitmap != null && drawBitmapDstRect != null) {
            canvas.drawBitmap(drawBitmap, drawBitmapSrcRect, drawBitmapDstRect, drawBitmapPaint);
        }
        super.onDraw(canvas);
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        setDrawable(drawablePosition, drawableWidth, drawableHeight, drawableTextDistance,
                drawableResId);
        requestLayout();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        setDrawable(drawablePosition, drawableWidth, drawableHeight, drawableTextDistance,
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
        setDrawable(drawablePosition, drawableWidth, drawableHeight, drawableTextDistance,
                drawableResId);
        postInvalidate();
    }

    /**
     * 设置图片显示位置
     *
     * @param drawablePosition 图片要展示的位置
     * @return 当前实例
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceType")
    public synchronized AvlwCustomDrawableButton setDrawable(Integer drawablePosition
            , Integer drawableWidth, Integer drawableHeight, Integer drawableTextDistance,
                                                             @DrawableRes Integer drawableResId) {

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
           if(drawBitmap != null){
               drawBitmapSrcRect = new Rect(0, 0, drawBitmap.getWidth(), drawBitmap.getHeight());
           }else {
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
     * @param drawableResId 资源id
     * @return 位图
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

    /**
     * padding内容更新
     *
     * @param left   左内边距
     * @param top    上内边距
     * @param right  右内边距
     * @param bottom 下内边距
     */
    public void paddingChanged(int left, int top, int right, int bottom) {

        //中心点坐标
        int centerX = getMeasuredWidth() / 2;
        int centerY = getMeasuredHeight() / 2;
        if (centerX <= 0 || centerY <= 0) {
            return;
        }

        //图片坐标
        int drawableStartX = left;
        int drawableStartY = top;
        //文本左上角坐标
        int textTopStartX = left;
        int textTopStartY = top;

        //获取文字宽高
        String str = getText().toString();
        if (allCaps) {
            str = str.toUpperCase();
        }
        Paint paint = new Paint();
        paint.setTextSize(getTextSize());
        int textWidth = (int) AtlwViewUtils.getInstance().getStrTextWidth(paint, str);
        int textHeight = (int) AtlwViewUtils.getInstance().getStrTextHeight(paint);

        //文本坐标读取
//        if ((getGravity() | Gravity.LEFT) == getGravity()
//                || (getGravity() | Gravity.START) == getGravity()) {
//            textTopStartX = left;
//        }
//        if ((getGravity() | Gravity.TOP) == getGravity()) {
//            textTopStartY = top;
//        }
        if ((getGravity() | Gravity.RIGHT) == getGravity()
                || (getGravity() | Gravity.END) == getGravity()) {
            textTopStartX = getMeasuredWidth() - right - textWidth;
        }
        if ((getGravity() | Gravity.BOTTOM) == getGravity()) {
            textTopStartY = getMeasuredHeight() - bottom - textHeight;
        }
        if ((getGravity() | Gravity.CENTER) == getGravity()) {
            textTopStartX = (getMeasuredWidth() - left - right) / 2 - textWidth / 2 + left;
            textTopStartY = (getMeasuredHeight() - top - bottom) / 2 - textHeight / 2 + top;
        } else {
            if ((getGravity() | Gravity.CENTER_HORIZONTAL) == Gravity.CENTER) {
                textTopStartX = (getMeasuredWidth() - left - right) / 2 - textWidth / 2 + left;
            }
            if ((getGravity() | Gravity.CENTER_VERTICAL) == Gravity.CENTER) {
                textTopStartY = (getMeasuredHeight() - top - bottom) / 2 - textHeight / 2 + top;
            }
        }

        //左右和padding边距
        int leftDistance = textTopStartX - left;
        int topDistance = textTopStartY - top;
        int rightDistance = getMeasuredWidth() - right - textWidth - textTopStartX;
        int bottomDistance = getMeasuredHeight() - bottom - textHeight - textTopStartY;

        int realPaddingLeft = left;
        int realPaddingTop = top;
        int realPaddingBottom = bottom;
        int realPaddingRight = right;

        //做内边距以及图片坐标判定
        switch (this.drawablePosition) {
            case DRAWABLE_POSITION_LEFT:
                //判断左侧空间是否足够
                if (leftDistance > (drawableWidth + drawableTextDistance)) {
                    //左侧空间足够
                    drawableStartX = textTopStartX - drawableWidth - drawableTextDistance;
                } else {
                    //左侧空间不够实际内容右移
                    realPaddingLeft = left + drawableWidth + drawableTextDistance;
                    realPaddingRight = Math.max(getMeasuredWidth() - realPaddingLeft - textWidth,
                            0) + right;
                    drawableStartX = realPaddingLeft - drawableWidth - drawableTextDistance;
                }

                //判断绘图高低是否足够
                if (drawableHeight < textHeight) {
                    //高度足够
                    drawableStartY = textTopStartY + (textHeight - drawableHeight) / 2;
                } else {
                    //高度不够
                    realPaddingTop = textTopStartY + (drawableHeight - textHeight) / 2;
                    realPaddingBottom = bottom + (drawableHeight - textHeight) / 2;
                    drawableStartY = top;
                }
                break;
            case DRAWABLE_POSITION_RIGHT:
                //判断右侧空间是否足够
                if (rightDistance > (drawableWidth + drawableTextDistance)) {
                    //右侧空间足够
                    drawableStartX = textTopStartX + textWidth + drawableTextDistance;
                } else {
                    //左侧空间不够实际内容左移
                    realPaddingRight = right + drawableWidth + drawableTextDistance;
                    realPaddingLeft = Math.max(getMeasuredWidth() - realPaddingRight - textWidth,
                            0) + left;
                    drawableStartX = realPaddingLeft + textWidth + drawableTextDistance;
                }
                //判断绘图高低是否足够
                if (drawableHeight < textHeight) {
                    //高度足够
                    drawableStartY = textTopStartY + (textHeight - drawableHeight) / 2;
                } else {
                    //高度不够
                    realPaddingTop = textTopStartY + (drawableHeight - textHeight) / 2;
                    realPaddingBottom = bottom + (drawableHeight - textHeight) / 2;
                    drawableStartY = top;
                }
                break;
            case DRAWABLE_POSITION_TOP:
                //判断顶部空间是否足够
                if (topDistance > (drawableHeight + drawableTextDistance)) {
                    //顶部空间足够
                    drawableStartY = textTopStartY - drawableHeight - drawableTextDistance;
                } else {
                    //顶部空间不够实际内容下移
                    realPaddingTop = top + drawableHeight + drawableTextDistance;
                    realPaddingBottom = Math.max(getMeasuredHeight() - realPaddingTop - textHeight,
                            0) + bottom;
                    drawableStartY = realPaddingTop - drawableHeight - drawableTextDistance;
                }

                //判断绘图宽度是否足够
                if (drawableWidth < getMeasuredWidth() - left - right) {
                    //宽度足够
                    drawableStartX = textTopStartX + (textWidth - drawableWidth) / 2;
                } else {
                    //宽度不够
                    realPaddingLeft = left + (drawableWidth - textWidth) / 2;
                    realPaddingRight = right + (drawableWidth - textWidth) / 2;
                    drawableStartX = left;
                }
                break;
            case DRAWABLE_POSITION_BOTTOM:
                //判断底部空间是否足够
                if (bottomDistance > (drawableHeight + drawableTextDistance)) {
                    //底部空间足够
                    drawableStartY =
                            textTopStartY + textHeight + drawableHeight + drawableTextDistance;
                } else {
                    //底部空间不够实际内容上移
                    realPaddingBottom = bottom + drawableHeight + drawableTextDistance;
                    realPaddingTop = Math.max(getMeasuredHeight() - realPaddingBottom - textHeight,
                            0) + top;
                    drawableStartY = realPaddingTop + textHeight + drawableTextDistance;
                }

                //判断绘图宽度是否足够
                if (drawableWidth < getMeasuredWidth() - left - right) {
                    //宽度足够
                    drawableStartX = textTopStartX + (textWidth - drawableWidth) / 2;
                } else {
                    //宽度不够
                    realPaddingLeft = left + (drawableWidth - textWidth) / 2;
                    realPaddingRight = right + (drawableWidth - textWidth) / 2;
                    drawableStartX = left;
                }
                break;
            default:
                break;
        }
        super.setPadding(Math.max(realPaddingLeft, 0), Math.max(realPaddingTop, 0),
                Math.max(realPaddingRight, 0), Math.max(realPaddingBottom, 0));
        drawBitmapDstRect = new Rect(drawableStartX, drawableStartY, drawableStartX + drawableWidth
                , drawableStartY + drawableHeight);

    }
}
