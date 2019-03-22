package android.lorenwang.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 创建时间： 2018-07-10 下午 2:27
 * 创建人：王亮（Loren wang）
 * 功能作用：进度条
 * 功能方法：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：1、进度条类型，外层有圈，进度居中，内圈随进度改变，扇形圆满代表着下载完成（逆时针）
 *      2、进度条类型，外层有圈，进度居中，内圈随进度改变，扇形归零代表着下载完成（顺时针）
 *      3、进度条类型，直线进度条，不带操作圆圈
 *
 *
 *
 * 类型1、2进度条：①.进度条宽、高；
 *            ②.进度条外圈宽度
 *            ③.进度条内外圈间距
 *            ④.进度条内圈颜色
 *            ⑤.进度条外圈颜色
 * 类型3进度条：①.进度条宽、高；
 *            ②.进度条背景颜色
 *            ③.进度条颜色
 *            ④.进度条圆角半径
 */
public class CustomProgressTypeView extends View {

    private final int PROGRESS_TYPE_1 = 0;//第一种进度条
    private final int PROGRESS_TYPE_2 = 1;//第二种进度条
    private final int PROGRESS_TYPE_3 = 2;//第三种进度条
    private int showProgressType = PROGRESS_TYPE_1;//要显示的进度条类型

    /***************************************进度条通用变量*******************************************/
    private int progressWidth = 0;//进度条宽度
    private int progressHeight = 0;//进度条高度
    private double progress = 0;//当前进度

    /************************************第一、二种进度条变量*****************************************/
    private int progressOuterRingWidth = 0;//外圈宽度
    private int progressOuterInnerRingDistance = 0;//内外圈间距
    private int progressOuterRingColor = 0;//外圈颜色
    private int progressInnerRingColor = 0;//外圈颜色
    private int progressInnerRingWidth = 0;//内圈宽度
    private int outerRingRadius = 0;//外圈半径
    private int innerRingRadius = 0;//内圈半径
    private Paint outerRingPaint;//外圈画笔
    private Paint innerRingPaint;//内圈画笔

    /**************************************第三种进度条变量******************************************/
    private Paint thirdProgressPaint;//第三中进度条画笔
    private float thirdProgressRadius = 0f;//第三种进度条圆角角度
    private int thirdProgressBgColor;//第三种进度条背景颜色
    private int thirdProgressColor;//第三种进度条颜色
    private RectF thirdProgressBgRectf;//第三种进度条位置

    public CustomProgressTypeView(Context context) {
        super(context);
        init(context,null,-1);
    }

    public CustomProgressTypeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,-1);
    }

    public CustomProgressTypeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }
    private void init(Context context, AttributeSet attrs, int defStyleAttr){

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressTypeView);
        showProgressType = attributes.getInt(R.styleable.CustomProgressTypeView_showProgressType,showProgressType);
        progressWidth = attributes.getDimensionPixelOffset(R.styleable.CustomProgressTypeView_progressWidth,progressWidth);
        progressHeight = attributes.getDimensionPixelOffset(R.styleable.CustomProgressTypeView_progressHeight,progressHeight);
        switch (showProgressType){
            case PROGRESS_TYPE_1:
            case PROGRESS_TYPE_2:
                progressOuterRingWidth = attributes.getDimensionPixelOffset(R.styleable.CustomProgressTypeView_progressOuterRingWidth,progressOuterRingWidth);
                progressOuterInnerRingDistance = attributes.getDimensionPixelOffset(R.styleable.CustomProgressTypeView_progressOuterInnerRingDistance,progressOuterInnerRingDistance);
                progressOuterRingColor = attributes.getColor(R.styleable.CustomProgressTypeView_progressOuterRingColor,progressOuterRingColor);
                progressInnerRingColor = attributes.getColor(R.styleable.CustomProgressTypeView_progressInnerRingColor,progressInnerRingColor);

                //初始化半径信息
                progressInnerRingWidth = progressWidth - progressOuterRingWidth - progressOuterInnerRingDistance;
                outerRingRadius = progressWidth / 2;
                innerRingRadius = progressInnerRingWidth / 2;

                //初始化外圈画笔
                outerRingPaint = new Paint();
                outerRingPaint.setAntiAlias(true);
                outerRingPaint.setColor(progressOuterRingColor);
                outerRingPaint.setStyle(Paint.Style.STROKE);
                outerRingPaint.setStrokeWidth(progressOuterRingWidth);

                //初始化内圈画笔
                innerRingPaint = new Paint();
                innerRingPaint.setAntiAlias(true);
                innerRingPaint.setColor(progressInnerRingColor);

                break;
            case PROGRESS_TYPE_3:
                thirdProgressRadius = attributes.getDimension(R.styleable.CustomProgressTypeView_progressRadius,thirdProgressRadius);
                thirdProgressBgColor = attributes.getColor(R.styleable.CustomProgressTypeView_progressBgColor, Color.WHITE);
                thirdProgressColor = attributes.getColor(R.styleable.CustomProgressTypeView_progressColor,Color.BLACK);
                thirdProgressPaint = new Paint();
                thirdProgressPaint.setAntiAlias(true);
                thirdProgressPaint.setStyle(Paint.Style.FILL);
                thirdProgressBgRectf = new RectF(0,0,progressWidth,progressHeight);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(getMeasuredWidth() > 0 && getMeasuredWidth() > 0){
            int centerX = getMeasuredWidth() / 2;
            int centerY = getMeasuredHeight() / 2;
            switch (showProgressType){
                case PROGRESS_TYPE_1:
                    //绘制外圈
                    canvas.drawCircle(centerX,centerY,outerRingRadius,outerRingPaint);
                    //绘制内圈
                    if(progress == 1){
                       canvas.drawCircle(centerX,centerY,innerRingRadius,innerRingPaint);
                    }else if(progress > 0 && progress < 1){
                        canvas.drawArc(centerX - innerRingRadius,centerY - innerRingRadius
                                ,centerX + innerRingRadius,centerY + innerRingRadius
                                , - 90, (float) (-360 * progress),true,innerRingPaint);
                    }
                    break;
                case PROGRESS_TYPE_2:
                    //绘制外圈
                    canvas.drawCircle(centerX,centerY,outerRingRadius,outerRingPaint);
                    //绘制内圈
                    if(progress == 0){
                        canvas.drawCircle(centerX,centerY,innerRingRadius,innerRingPaint);
                    }else if(progress > 0 && progress < 1){
                        canvas.drawArc(centerX - innerRingRadius,centerY - innerRingRadius
                                ,centerX + innerRingRadius,centerY + innerRingRadius
                                , - 90,(float) (-360 * (1 - progress)),true,innerRingPaint);
                    }
                    break;
                case PROGRESS_TYPE_3:
                    //绘制背景
                    thirdProgressPaint.setColor(thirdProgressBgColor);
                    canvas.drawRoundRect(thirdProgressBgRectf,thirdProgressRadius,thirdProgressRadius,thirdProgressPaint);
                    //绘制进度
                    thirdProgressPaint.setColor(thirdProgressColor);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        canvas.drawRoundRect(0,0, (float) (progressWidth * progress),progressHeight,thirdProgressRadius,thirdProgressRadius,thirdProgressPaint);
                    }else {
                        canvas.drawRoundRect(new RectF(0,0, (float) (progressWidth * progress),progressHeight),thirdProgressRadius,thirdProgressRadius,thirdProgressPaint);
                    }
                    break;
                default:
                    break;
            }
        }
        super.onDraw(canvas);
    }

    public CustomProgressTypeView setProgress(@FloatRange(from = 0,to = 1) float progress) {
        this.progress = progress;
        invalidate();
        return this;
    }

    public double getProgress() {
        return progress;
    }
}
