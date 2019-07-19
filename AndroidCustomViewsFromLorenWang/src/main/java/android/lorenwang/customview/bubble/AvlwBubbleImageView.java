package android.lorenwang.customview.bubble;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.lorenwang.customview.R;
import android.lorenwang.tools.app.AtlwScreenUtils;
import android.lorenwang.tools.image.AtlwImageCommonUtils;
import android.util.AttributeSet;



/**
 * Created by wangliang on 0027/2017/3/27.
 * 创建时间： 0027/2017/3/27 14:06
 * 创建人：王亮（Loren wang）
 * 功能作用：气泡对话框
 * 思路：使用三层覆盖的方法，最底层使用path绘制出来一个带有箭头的圆角黑图，然后使用图片相交模式中的LIGHTEN方式进行
 *      显示截取（其他方法不知道为什么不行反而有问题），然后再使用path再绘制一个带有箭头的圆角边框，然后不要使用paint绘制到画布上
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AvlwBubbleImageView extends android.support.v7.widget.AppCompatImageView {


    private Context context;
    private Paint paint;//基础画笔

    private int bubbleArrowWidth;//气泡箭头的宽度
    private int bubbleArrowHeight;//气泡箭头的高度
    private boolean bubbleArrowIsCenterVertical;//气泡箭头是否居中
    private int bubbleRadius;//气泡的圆角角度
    private String bubbleDirection;//气泡的方向
    private int bubbleMarginTop;//气泡的如果不居中的话距离顶部的距离
    private int bubbleMarginLeft;//气泡的如果不居中的话距离左边的距离
    private int bubbleStrokeWidth;//气泡边框的宽度
    private int bubbleStrokeColor;//气泡边框的颜色
    private int bubbleBackGround;//气泡背景颜色

    private final String BUBBLE_DIRECTION_LEFT = "0";
    private final String BUBBLE_DIRECTION_TOP = "1";
    private final String BUBBLE_DIRECTION_RIGHT = "2";
    private final String BUBBLE_DIRECTION_BOTTOM = "3";

    private Bitmap backgroundBitmap;
    private Bitmap strokeBitmap;
    private Bitmap mBitmap;


    public AvlwBubbleImageView(Context context) {
        this(context,null);
    }

    public AvlwBubbleImageView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public AvlwBubbleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AvlwBubble);
        this.bubbleArrowWidth = attributes.getDimensionPixelOffset(R.styleable.AvlwBubble_bubbleArrowWidth,dip2px(10));
        this.bubbleArrowHeight = attributes.getDimensionPixelOffset(R.styleable.AvlwBubble_bubbleArrowHeight,dip2px(10));
        this.bubbleArrowIsCenterVertical = attributes.getBoolean(R.styleable.AvlwBubble_bubbleArrowIsCenterVertical,true);
        this.bubbleRadius = attributes.getDimensionPixelOffset(R.styleable.AvlwBubble_bubbleRadius,dip2px(8));
        this.bubbleMarginTop = attributes.getDimensionPixelOffset(R.styleable.AvlwBubble_bubbleMarginTop,-1);
        this.bubbleMarginLeft = attributes.getDimensionPixelOffset(R.styleable.AvlwBubble_bubbleMarginLeft,-1);
        this.bubbleStrokeWidth = attributes.getDimensionPixelOffset(R.styleable.AvlwBubble_bubbleStrokeWidth,0);
        this.bubbleStrokeColor = attributes.getColor(R.styleable.AvlwBubble_bubbleStrokeColor, Color.WHITE);
        this.bubbleDirection = attributes.getString(R.styleable.AvlwBubble_bubbleDirection);
        this.bubbleBackGround = attributes.getColor(R.styleable.AvlwBubble_bubbleBackGround,Color.WHITE);
        if(this.bubbleDirection == null || "".equals(this.bubbleDirection)){
            this.bubbleDirection = BUBBLE_DIRECTION_LEFT;
        }

        //百分比和数值都有的话以数值为准
        String bubbleMarginTopPercent = attributes.getString(R.styleable.AvlwBubble_bubbleMarginTopPercent);
        String bubbleMarginLeftPercent = attributes.getString(R.styleable.AvlwBubble_bubbleMarginLeftPercent);
        if(bubbleMarginLeft == -1){
            if(bubbleMarginLeftPercent != null) {
                int percentStrValue = getPercentStrValue(context, bubbleMarginLeftPercent);
                if (percentStrValue > 0) {
                    bubbleMarginLeft = percentStrValue;
                } else {
                    bubbleMarginLeft = dip2px(10);
                }
            } else {
                bubbleMarginLeft = dip2px(10);
            }
        }
        if(bubbleMarginTop == -1){
            if(bubbleMarginTopPercent != null) {
                int percentStrValue = getPercentStrValue(context, bubbleMarginTopPercent);
                if (percentStrValue > 0) {
                    bubbleMarginTop = percentStrValue;
                } else {
                    bubbleMarginTop = dip2px(10);
                }
            } else {
                bubbleMarginTop = dip2px(10);
            }
        }




        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(bubbleStrokeWidth);
        paint.setColor(bubbleStrokeColor);

    }


    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */
    public int dip2px(float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //当控件大小改变的时候这两个位图也要改变
        strokeBitmap = null;
        backgroundBitmap = null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(getMeasuredHeight() != 0 && getMeasuredWidth() != 0){
            //将绘制操作保存到新的图层（更官方的说法应该是离屏缓存，不使用可能会导致混合是出现黑块）,绘制完成之后还要还原画布，进行修改的保存
            int sc = canvas.saveLayer(0, 0, getMeasuredWidth(), getMeasuredHeight(), null, Canvas.ALL_SAVE_FLAG);
            //绘制底图
            if(backgroundBitmap == null){
                backgroundBitmap = initBubbleStrokeBitmap();
            }
            if(backgroundBitmap != null){
                canvas.drawBitmap(initBubbleBackGroundBitmap(),0,0,paint);
            }

            //绘制真实图片
            if(mBitmap != null){
                //设置图形相交模式
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                //画覆盖层（蓝色矩形）SRC
                canvas.drawBitmap(mBitmap
                        , new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight())
                        , new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight())
                        , paint);
            }

            //绘制边框图
            if(bubbleStrokeWidth > 0){
                if(strokeBitmap == null){
                    strokeBitmap = initBubbleStrokeBitmap();
                }
                if(strokeBitmap != null){
                    canvas.drawBitmap(strokeBitmap, 0, 0, null);
                }
            }
            // 还原混合模式
            paint.setXfermode(null);

            // 还原画布
            canvas.restoreToCount(sc);
        }
    }

    /**
     * 初始化底图位图
     * @return
     */
    private Bitmap initBubbleBackGroundBitmap(){
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottopm = getPaddingBottom();
        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();
        Bitmap bubbleBackGroundBitmap = Bitmap.createBitmap(viewWidth ,viewHeight,Bitmap.Config.ARGB_8888);
        Canvas bubbleBackGroundCanvas = new Canvas(bubbleBackGroundBitmap);
        Paint bubbleBackGroundPaint = new Paint();
        bubbleBackGroundPaint.setAntiAlias(true);
        bubbleBackGroundPaint.setStyle(Paint.Style.FILL);
        bubbleBackGroundPaint.setColor(bubbleBackGround);
        Path bubbleBackGroundPath = new Path();

        int nowBubbleArrowX = 0;//箭头的尖的坐标
        int nowBubbleArrowY = 0;//箭头的尖的坐标
        switch (this.bubbleDirection) {
            case BUBBLE_DIRECTION_LEFT:
                if(bubbleArrowIsCenterVertical){
                    nowBubbleArrowX = paddingLeft + bubbleStrokeWidth / 2;
                    nowBubbleArrowY = (viewHeight - paddingTop - paddingBottopm) / 2 + paddingTop;
                }else {
                    nowBubbleArrowX = paddingLeft + bubbleStrokeWidth / 2;
                    nowBubbleArrowY = bubbleMarginTop + paddingTop;
                }
                //绘制实心箭头
                bubbleBackGroundPath.moveTo(nowBubbleArrowX + bubbleArrowWidth,nowBubbleArrowY - bubbleArrowHeight / 2);
                bubbleBackGroundPath.lineTo(nowBubbleArrowX,nowBubbleArrowY);
                bubbleBackGroundPath.lineTo(nowBubbleArrowX + bubbleArrowWidth,nowBubbleArrowY + bubbleArrowHeight / 2);
                //绘制圆角矩形
                bubbleBackGroundPath.addRoundRect(new RectF(nowBubbleArrowX + bubbleArrowWidth
                                ,bubbleStrokeWidth / 2 + paddingTop
                                ,viewWidth - bubbleStrokeWidth / 2 - paddingRight
                                ,viewHeight - bubbleStrokeWidth / 2 - paddingBottopm)
                        ,bubbleRadius,bubbleRadius, Path.Direction.CCW);
                //绘制到画布
                bubbleBackGroundCanvas.drawPath(bubbleBackGroundPath,bubbleBackGroundPaint);
                break;
            case BUBBLE_DIRECTION_TOP:
                if(bubbleArrowIsCenterVertical){
                    nowBubbleArrowX = (viewWidth - paddingLeft - paddingRight) / 2 + paddingLeft;
                    nowBubbleArrowY = paddingTop + bubbleStrokeWidth / 2;
                }else {
                    nowBubbleArrowX = paddingLeft + bubbleMarginLeft;
                    nowBubbleArrowY = paddingTop + bubbleStrokeWidth / 2;
                }
                //绘制实心箭头
                bubbleBackGroundPath.moveTo(nowBubbleArrowX + bubbleArrowWidth / 2,nowBubbleArrowY + bubbleArrowHeight);
                bubbleBackGroundPath.lineTo(nowBubbleArrowX,nowBubbleArrowY);
                bubbleBackGroundPath.lineTo(nowBubbleArrowX - bubbleArrowWidth / 2,nowBubbleArrowY + bubbleArrowHeight);
                //绘制圆角矩形
                bubbleBackGroundPath.addRoundRect(new RectF(paddingLeft + bubbleStrokeWidth / 2
                                ,nowBubbleArrowY + bubbleArrowHeight
                                ,viewWidth - bubbleStrokeWidth / 2 - paddingRight
                                ,viewHeight - bubbleStrokeWidth / 2 - paddingBottopm)
                        ,bubbleRadius,bubbleRadius, Path.Direction.CCW);
                //绘制到画布
                bubbleBackGroundCanvas.drawPath(bubbleBackGroundPath,bubbleBackGroundPaint);
                break;
            case BUBBLE_DIRECTION_RIGHT:
                if(bubbleArrowIsCenterVertical){
                    nowBubbleArrowX = viewWidth - paddingRight - bubbleStrokeWidth / 2;
                    nowBubbleArrowY = (viewHeight - paddingTop - paddingBottopm) / 2 + paddingTop;
                }else {
                    nowBubbleArrowX = viewWidth - paddingRight - bubbleStrokeWidth / 2;
                    nowBubbleArrowY = bubbleMarginTop + paddingTop;
                }
                //绘制实心箭头
                bubbleBackGroundPath.moveTo(nowBubbleArrowX - bubbleArrowWidth,nowBubbleArrowY - bubbleArrowHeight / 2);
                bubbleBackGroundPath.lineTo(nowBubbleArrowX,nowBubbleArrowY);
                bubbleBackGroundPath.lineTo(nowBubbleArrowX - bubbleArrowWidth,nowBubbleArrowY + bubbleArrowHeight / 2);
                //绘制圆角矩形
                bubbleBackGroundPath.addRoundRect(new RectF(paddingLeft + bubbleStrokeWidth / 2
                                ,bubbleStrokeWidth / 2 + paddingTop
                                ,nowBubbleArrowX - bubbleArrowWidth
                                ,viewHeight - bubbleStrokeWidth / 2 - paddingBottopm)
                        ,bubbleRadius,bubbleRadius, Path.Direction.CCW);
                //绘制到画布
                bubbleBackGroundCanvas.drawPath(bubbleBackGroundPath,bubbleBackGroundPaint);
                break;
            case BUBBLE_DIRECTION_BOTTOM:
                if(bubbleArrowIsCenterVertical){
                    nowBubbleArrowX = (viewWidth - paddingLeft - paddingRight) / 2 + paddingLeft;
                    nowBubbleArrowY = viewHeight - paddingBottopm - bubbleStrokeWidth / 2;
                }else {
                    nowBubbleArrowX = paddingLeft + bubbleMarginLeft;
                    nowBubbleArrowY = viewHeight - paddingBottopm - bubbleStrokeWidth / 2;
                }
                //绘制实心箭头
                bubbleBackGroundPath.moveTo(nowBubbleArrowX + bubbleArrowWidth / 2,nowBubbleArrowY - bubbleArrowHeight);
                bubbleBackGroundPath.lineTo(nowBubbleArrowX,nowBubbleArrowY);
                bubbleBackGroundPath.lineTo(nowBubbleArrowX - bubbleArrowWidth / 2,nowBubbleArrowY - bubbleArrowHeight);
                //绘制圆角矩形
                bubbleBackGroundPath.addRoundRect(new RectF(paddingLeft + bubbleStrokeWidth / 2
                                ,paddingTop + bubbleStrokeWidth / 2
                                ,viewWidth - bubbleStrokeWidth / 2 - paddingRight
                                ,nowBubbleArrowY - bubbleArrowHeight)
                        ,bubbleRadius,bubbleRadius, Path.Direction.CCW);
                //绘制到画布
                bubbleBackGroundCanvas.drawPath(bubbleBackGroundPath,bubbleBackGroundPaint);
                break;
            default:
                break;
        }

        return bubbleBackGroundBitmap;

    }

    /**
     * 流程：先确定方向--》确定箭头尖部的坐标--》确定箭头三个点的位置坐标---》绘制圆角矩形
     * @return
     */
    private Bitmap initBubbleStrokeBitmap(){
        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();
        if(viewWidth <= 0 || viewHeight <= 0){
            return null;
        }
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottopm = getPaddingBottom();
        Bitmap bubbleStrokeBitmap = Bitmap.createBitmap(viewWidth ,viewHeight,Bitmap.Config.ARGB_8888);
        Canvas bubbleStrokeCanvas = new Canvas(bubbleStrokeBitmap);
        Paint bubbleStrokePaint = new Paint();
        bubbleStrokePaint.setAntiAlias(true);
        bubbleStrokePaint.setStyle(Paint.Style.STROKE);
        bubbleStrokePaint.setColor(bubbleStrokeColor);
        bubbleStrokePaint.setStrokeWidth(bubbleStrokeWidth);
        Path bubbleStrokePath = new Path();
        Paint bubbleStrokeOverlayPaint;
        Path bubbleStrokeOverlayPath;

        int nowBubbleArrowX = 0;//箭头的尖的坐标
        int nowBubbleArrowY = 0;//箭头的尖的坐标
        switch (this.bubbleDirection) {
            case BUBBLE_DIRECTION_LEFT:
                if(bubbleArrowIsCenterVertical){
                    nowBubbleArrowX = paddingLeft + bubbleStrokeWidth / 2;
                    nowBubbleArrowY = (viewHeight - paddingTop - paddingBottopm) / 2 + paddingTop;
                }else {
                    nowBubbleArrowX = paddingLeft + bubbleStrokeWidth / 2;
                    nowBubbleArrowY = bubbleMarginTop + paddingTop;
                }
                //绘制实心箭头
                bubbleStrokePath.moveTo(nowBubbleArrowX + bubbleArrowWidth,nowBubbleArrowY - bubbleArrowHeight / 2);
                bubbleStrokePath.lineTo(nowBubbleArrowX,nowBubbleArrowY);
                bubbleStrokePath.lineTo(nowBubbleArrowX + bubbleArrowWidth,nowBubbleArrowY + bubbleArrowHeight / 2);
                bubbleStrokePaint.setStyle(Paint.Style.FILL);
                bubbleStrokeCanvas.drawPath(bubbleStrokePath,bubbleStrokePaint);
                //绘制空心圆角矩形
                bubbleStrokePaint.setStyle(Paint.Style.STROKE);
                bubbleStrokePath.addRoundRect(new RectF(nowBubbleArrowX + bubbleArrowWidth
                        ,bubbleStrokeWidth / 2 + paddingTop
                        ,viewWidth - bubbleStrokeWidth / 2 - paddingRight
                        ,viewHeight - bubbleStrokeWidth / 2 - paddingBottopm)
                        ,bubbleRadius,bubbleRadius, Path.Direction.CCW);
                //绘制到画布
                bubbleStrokeCanvas.drawPath(bubbleStrokePath,bubbleStrokePaint);
                bubbleStrokeCanvas.save();
                //绘制覆盖掉箭头的封闭线的实心三角
                bubbleStrokeOverlayPaint = new Paint(bubbleStrokePaint);
                bubbleStrokeOverlayPaint.setStyle(Paint.Style.FILL);
                bubbleStrokeOverlayPaint.setColor(Color.parseColor("#00ffffff"));//使用透明的颜色可以使范围设置的稍微大一些，防止出现2.5dp这种情况
                bubbleStrokeOverlayPaint.setStrokeWidth(1);
                bubbleStrokeOverlayPaint.setAntiAlias(true);
                bubbleStrokeOverlayPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));//设置覆盖物的显示方式
                bubbleStrokeOverlayPath = new Path();
                bubbleStrokeOverlayPath.moveTo(nowBubbleArrowX + bubbleArrowWidth + bubbleStrokeWidth,nowBubbleArrowY - bubbleArrowHeight / 2);
                bubbleStrokeOverlayPath.lineTo(nowBubbleArrowX + bubbleStrokeWidth,nowBubbleArrowY);
                bubbleStrokeOverlayPath.lineTo(nowBubbleArrowX + bubbleArrowWidth + bubbleStrokeWidth,nowBubbleArrowY + bubbleArrowHeight / 2);
                bubbleStrokeOverlayPath.close();
                //绘制进行覆盖显示
                bubbleStrokeCanvas.drawPath(bubbleStrokeOverlayPath,bubbleStrokeOverlayPaint);
                bubbleStrokeCanvas.restore();
                break;
            case BUBBLE_DIRECTION_TOP:
                if(bubbleArrowIsCenterVertical){
                    nowBubbleArrowX = (viewWidth - paddingLeft - paddingRight) / 2 + paddingLeft;
                    nowBubbleArrowY = paddingTop + bubbleStrokeWidth / 2;
                }else {
                    nowBubbleArrowX = paddingLeft + bubbleMarginLeft;
                    nowBubbleArrowY = paddingTop + bubbleStrokeWidth / 2;
                }
                //绘制实心箭头
                bubbleStrokePath.moveTo(nowBubbleArrowX + bubbleArrowWidth / 2,nowBubbleArrowY + bubbleArrowHeight);
                bubbleStrokePath.lineTo(nowBubbleArrowX,nowBubbleArrowY);
                bubbleStrokePath.lineTo(nowBubbleArrowX - bubbleArrowWidth / 2,nowBubbleArrowY + bubbleArrowHeight);
                bubbleStrokePaint.setStyle(Paint.Style.FILL);
                bubbleStrokeCanvas.drawPath(bubbleStrokePath,bubbleStrokePaint);
                //绘制空心圆角矩形
                bubbleStrokePaint.setStyle(Paint.Style.STROKE);
                bubbleStrokePath.addRoundRect(new RectF(paddingLeft + bubbleStrokeWidth / 2
                                ,nowBubbleArrowY + bubbleArrowHeight
                                ,viewWidth - bubbleStrokeWidth / 2 - paddingRight
                                ,viewHeight - bubbleStrokeWidth / 2 - paddingBottopm)
                        ,bubbleRadius,bubbleRadius, Path.Direction.CCW);
                //绘制到画布
                bubbleStrokeCanvas.drawPath(bubbleStrokePath,bubbleStrokePaint);
                bubbleStrokeCanvas.save();
                //绘制覆盖掉箭头的封闭线的实心三角
                bubbleStrokeOverlayPaint = new Paint(bubbleStrokePaint);
                bubbleStrokeOverlayPaint.setStyle(Paint.Style.FILL);
                bubbleStrokeOverlayPaint.setColor(Color.parseColor("#00ffffff"));//使用透明的颜色可以使范围设置的稍微大一些，防止出现2.5dp这种情况
                bubbleStrokeOverlayPaint.setStrokeWidth(1);
                bubbleStrokeOverlayPaint.setAntiAlias(true);
                bubbleStrokeOverlayPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));//设置覆盖物的显示方式
                bubbleStrokeOverlayPath = new Path();
                bubbleStrokeOverlayPath.moveTo(nowBubbleArrowX + bubbleArrowWidth / 2,nowBubbleArrowY + bubbleArrowHeight + bubbleStrokeWidth);
                bubbleStrokeOverlayPath.lineTo(nowBubbleArrowX,nowBubbleArrowY + bubbleStrokeWidth);
                bubbleStrokeOverlayPath.lineTo(nowBubbleArrowX - bubbleArrowWidth / 2,nowBubbleArrowY + bubbleArrowHeight + bubbleStrokeWidth);
                bubbleStrokeOverlayPath.close();
                //绘制进行覆盖显示
                bubbleStrokeCanvas.drawPath(bubbleStrokeOverlayPath,bubbleStrokeOverlayPaint);
                bubbleStrokeCanvas.restore();
                break;
            case BUBBLE_DIRECTION_RIGHT:
                if(bubbleArrowIsCenterVertical){
                    nowBubbleArrowX = viewWidth - paddingRight - bubbleStrokeWidth / 2;
                    nowBubbleArrowY = (viewHeight - paddingTop - paddingBottopm) / 2 + paddingTop;
                }else {
                    nowBubbleArrowX = viewWidth - paddingRight - bubbleStrokeWidth / 2;
                    nowBubbleArrowY = bubbleMarginTop + paddingTop;
                }
                //绘制实心箭头
                bubbleStrokePath.moveTo(nowBubbleArrowX - bubbleArrowWidth,nowBubbleArrowY - bubbleArrowHeight / 2);
                bubbleStrokePath.lineTo(nowBubbleArrowX,nowBubbleArrowY);
                bubbleStrokePath.lineTo(nowBubbleArrowX - bubbleArrowWidth,nowBubbleArrowY + bubbleArrowHeight / 2);
                bubbleStrokePaint.setStyle(Paint.Style.FILL);
                bubbleStrokeCanvas.drawPath(bubbleStrokePath,bubbleStrokePaint);
                //绘制空心圆角矩形
                bubbleStrokePaint.setStyle(Paint.Style.STROKE);
                bubbleStrokePath.addRoundRect(new RectF(paddingLeft + bubbleStrokeWidth / 2
                                ,bubbleStrokeWidth / 2 + paddingTop
                                ,nowBubbleArrowX - bubbleArrowWidth
                                ,viewHeight - bubbleStrokeWidth / 2 - paddingBottopm)
                        ,bubbleRadius,bubbleRadius, Path.Direction.CCW);
                //绘制到画布
                bubbleStrokeCanvas.drawPath(bubbleStrokePath,bubbleStrokePaint);
                bubbleStrokeCanvas.save();
                //绘制覆盖掉箭头的封闭线的实心三角
                bubbleStrokeOverlayPaint = new Paint(bubbleStrokePaint);
                bubbleStrokeOverlayPaint.setStyle(Paint.Style.FILL);
                bubbleStrokeOverlayPaint.setColor(Color.parseColor("#00ffffff"));//使用透明的颜色可以使范围设置的稍微大一些，防止出现2.5dp这种情况
                bubbleStrokeOverlayPaint.setStrokeWidth(1);
                bubbleStrokeOverlayPaint.setAntiAlias(true);
                bubbleStrokeOverlayPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));//设置覆盖物的显示方式
                bubbleStrokeOverlayPath = new Path();
                bubbleStrokeOverlayPath.moveTo(nowBubbleArrowX - bubbleArrowWidth - bubbleStrokeWidth ,nowBubbleArrowY - bubbleArrowHeight / 2);
                bubbleStrokeOverlayPath.lineTo(nowBubbleArrowX - bubbleStrokeWidth,nowBubbleArrowY);
                bubbleStrokeOverlayPath.lineTo(nowBubbleArrowX - bubbleArrowWidth - bubbleStrokeWidth,nowBubbleArrowY + bubbleArrowHeight / 2);
                bubbleStrokeOverlayPath.close();
                //绘制进行覆盖显示
                bubbleStrokeCanvas.drawPath(bubbleStrokeOverlayPath,bubbleStrokeOverlayPaint);
                bubbleStrokeCanvas.restore();
                break;
            case BUBBLE_DIRECTION_BOTTOM:
                if(bubbleArrowIsCenterVertical){
                    nowBubbleArrowX = (viewWidth - paddingLeft - paddingRight) / 2 + paddingLeft;
                    nowBubbleArrowY = viewHeight - paddingBottopm - bubbleStrokeWidth / 2;
                }else {
                    nowBubbleArrowX = paddingLeft + bubbleMarginLeft;
                    nowBubbleArrowY = viewHeight - paddingBottopm - bubbleStrokeWidth / 2;
                }
                //绘制实心箭头
                bubbleStrokePath.moveTo(nowBubbleArrowX + bubbleArrowWidth / 2,nowBubbleArrowY - bubbleArrowHeight);
                bubbleStrokePath.lineTo(nowBubbleArrowX,nowBubbleArrowY);
                bubbleStrokePath.lineTo(nowBubbleArrowX - bubbleArrowWidth / 2,nowBubbleArrowY - bubbleArrowHeight);
                bubbleStrokePaint.setStyle(Paint.Style.FILL);
                bubbleStrokeCanvas.drawPath(bubbleStrokePath,bubbleStrokePaint);
                //绘制空心圆角矩形
                bubbleStrokePaint.setStyle(Paint.Style.STROKE);
                bubbleStrokePath.addRoundRect(new RectF(paddingLeft + bubbleStrokeWidth / 2
                                ,paddingTop + bubbleStrokeWidth / 2
                                ,viewWidth - bubbleStrokeWidth / 2 - paddingRight
                                ,nowBubbleArrowY - bubbleArrowHeight)
                        ,bubbleRadius,bubbleRadius, Path.Direction.CCW);
                //绘制到画布
                bubbleStrokeCanvas.drawPath(bubbleStrokePath,bubbleStrokePaint);
                bubbleStrokeCanvas.save();
                //绘制覆盖掉箭头的封闭线的实心三角
                bubbleStrokeOverlayPaint = new Paint(bubbleStrokePaint);
                bubbleStrokeOverlayPaint.setStyle(Paint.Style.FILL);
                bubbleStrokeOverlayPaint.setColor(Color.parseColor("#00ffffff"));//使用透明的颜色可以使范围设置的稍微大一些，防止出现2.5dp这种情况
                bubbleStrokeOverlayPaint.setStrokeWidth(1);
                bubbleStrokeOverlayPaint.setAntiAlias(true);
                bubbleStrokeOverlayPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));//设置覆盖物的显示方式
                bubbleStrokeOverlayPath = new Path();
                bubbleStrokeOverlayPath.moveTo(nowBubbleArrowX + bubbleArrowWidth / 2,nowBubbleArrowY - bubbleArrowHeight - bubbleStrokeWidth);
                bubbleStrokeOverlayPath.lineTo(nowBubbleArrowX,nowBubbleArrowY - bubbleStrokeWidth);
                bubbleStrokeOverlayPath.lineTo(nowBubbleArrowX - bubbleArrowWidth / 2,nowBubbleArrowY - bubbleArrowHeight - bubbleStrokeWidth);
                bubbleStrokeOverlayPath.close();
                //绘制进行覆盖显示
                bubbleStrokeCanvas.drawPath(bubbleStrokeOverlayPath,bubbleStrokeOverlayPaint);
                bubbleStrokeCanvas.restore();
                break;
            default:
                break;
        }

        return bubbleStrokeBitmap;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        try {
            super.setImageBitmap(bm);
            mBitmap = AtlwImageCommonUtils.getInstance().zoomImage(bm,getMeasuredWidth(),getMeasuredHeight());
            postInvalidate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        try {
            super.setImageDrawable(drawable);
            mBitmap = AtlwImageCommonUtils.getInstance().zoomImage(AtlwImageCommonUtils.getInstance().drawableToBitmap(drawable),getMeasuredWidth(),getMeasuredHeight());
            postInvalidate();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void setImageResource(int resId) {
        try {
            super.setImageResource(resId);
            mBitmap = AtlwImageCommonUtils.getInstance().zoomImage(AtlwImageCommonUtils.getInstance().drawableToBitmap(getDrawable()),getMeasuredWidth(),getMeasuredHeight());
            postInvalidate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getPercentStrValue(Context context,String percent){
        double itemHeight = 0;
        if(percent != null) {
            if (percent.contains("w")) {
                itemHeight = Double.valueOf(percent.substring(0, percent.indexOf("%")).toString()) * 0.01 * AtlwScreenUtils.getInstance().getScreenWidth(context);
            } else if (percent.contains("h")) {
                itemHeight = Double.valueOf(percent.substring(0, percent.indexOf("%")).toString()) * 0.01 * AtlwScreenUtils.getInstance().getScreenHeight(context);
            }
        }
        return (int)itemHeight;
    }


}
