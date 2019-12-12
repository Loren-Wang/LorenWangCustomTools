package android.lorenwang.customview.sudokuSwipeGestures;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.lorenwang.customview.R;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by LorenWang on 2018/4/13.
 * 创建时间：2018/4/13 16:20
 * 创建人：王亮（Loren wang）
 * 功能作用：九宫格手势滑动操作
 * 思路：首先制定好九个圆圈的位置，以及相应的中心坐标，还有滑动的有效半径，显示中心圆半径
 *      1、还需要设置当圆圈被选中、未选中、以及选中错误的情况下的内圈以及外圈的颜色
 *      2、设置显示模式
 *           ①、内圈实心，外圈空心带边框，滑动时内外圈变色，连接线从中心点出来，此模式下需要传入边框宽度
 *           ②、内圈实心，外圈实心，未选中的时候内圈显示，外圈不显示，滑动时外圈显示，连接线从中心点出来
 *           ③、后续模式待设计
 *      3、是否要显示绘制轨迹
 *      4、连接线宽度、颜色
 *      5、完成手势之后需要接收回调值，如果无回调则直接在指定时间之后重置，如果有回调并且有返回值则操作后回调
 *      6、圆圈绘制使用横向排列绘制
 * 方法：1、释放所有变量
 *      2、重置显示到最初显示状态
 *      3、设置有效半径以及中心圆半径
 *      4、设置圆圈被选中、未选中、以及选中错误下的内圈以及外圈的颜色
 *      5、设置显示模式
 *      6、设置是否要显示轨迹
 *      7、设置连接线颜色、宽度
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AvlwSudokuSwipeGesturesView extends View {
    private final String TAG = getClass().getName();
    private final int CIRCLE_SHOW_TYPE_1 = 1;//显示模式1,内圈实心，外圈空心带边框，滑动时内外圈变色，连接线从中心点出来，此模式下需要传入边框宽度
    private final int CIRCLE_SHOW_TYPE_2 = 2;//显示模式2,内圈实心，外圈实心，未选中的时候内圈显示，外圈不显示，滑动时外圈显示，连接线从中心点出来

    private int circleEffectiveRadius = 0;//圆圈的有效半径
    private int circleInnerRingRadius = 0;//圆圈的内圈半径
    private int circleOuterRingRadius = 0;//圆圈的外圈半径
    private int circleOuterRingBorderWidth = 0;//圆圈的外圈的边框宽度
    private int circleInnerRingSelectedColor = Color.WHITE;//圆圈内圈选中颜色
    private int circleInnerRingUnSelectedColor = Color.WHITE;//圆圈内圈未选中颜色
    private int circleInnerRingErrorColor = Color.WHITE;//圆圈内圈错误颜色
    private int circleOuterRingSelectedColor = Color.WHITE;//圆圈外圈选中颜色
    private int circleOuterRingUnSelectedColor = Color.WHITE;//圆圈外圈未选中颜色
    private int circleOuterRingErrorColor = Color.WHITE;//圆圈外圈错误颜色
    private int circleShowType = CIRCLE_SHOW_TYPE_1;//圆圈显示模式
    private boolean isShowTrack = true;//是否显示绘制轨迹
    private int connectingLineColor = Color.WHITE;//连接线链接时颜色
    private int connectingLineErrorColor = Color.WHITE;//连接线错误的时候的颜色
    private int connectingLineWidth = 0;//连接线宽度


    private List<CircleInfo> circleInfoList = new ArrayList<>();//圆圈集合,存储横向排列
    private Paint circleInnerRingSelectedPaint = new Paint();//圆圈被选中的时候的中心圆的画笔
    private Paint circleInnerRingUnSelectedPaint = new Paint();//圆圈未选中的时候的中心圆的画笔
    private Paint circleInnerRingErrorPaint = new Paint();//圆圈选中错误的时候的中心圆的画笔
    private Paint circleOuterRingSelectedPaint = new Paint();//圆圈被选中的时候中心圆的画笔
    private Paint circleOuterRingUnSelectPaint = new Paint();//圆圈未选中的时候中心圆的画笔
    private Paint circleOuterRingErrorPaint = new Paint();//圆圈选中错误的时候中心圆的画笔
    private Paint connectingSelectLinePaint = new Paint();//连接线画笔
    private Paint connectingSelectErrorLinePaint = new Paint();//连接线画笔移除
    private List<CircleInfo> selectCirclePosiList = new ArrayList<>();//选中列表
    private int maxCircleEffectiveRadius = 0;//最大的圆圈有效半径
    private InputStateChangeCallback inputStateChangeCallback;//输入状态改变回传

    private final int DATA_STATE_INIT = 0;//当前的数据状态是初始化
    private final int DATA_STATE_INPUT = 1;//当前的数据状态是输入中
    private final int DATA_STATE_FINISH = 2;//当前的数据状态是结束输入
    private final int DATA_STATE_TRUE = 3;//当前的数据状态是正确输入
    private final int DATA_STATE_FALSE = 4;//当前的数据状态是错误输入
    private final int DATA_STATE_WAIT_FINISH = 5;//当前的数据状态是等待结束
    private int nowDataState = DATA_STATE_INIT;//当前的数据状态
    private boolean allowDraw = true;//是否允许绘制

    private final int RESET_VIEW = 0;//重置视图
    private final int WAIT_FINISH = 1;//延迟结束
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case RESET_VIEW:
                    resetAll();
                    break;
                case WAIT_FINISH:
                    if(nowDataState == DATA_STATE_WAIT_FINISH) {
                        nowDataState = DATA_STATE_FINISH;
                        finishInput();
                        invalidate();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    public AvlwSudokuSwipeGesturesView(Context context) {
        super(context);
        init(context,null,-1);
    }

    public AvlwSudokuSwipeGesturesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,-1);
    }

    public AvlwSudokuSwipeGesturesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }


    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AvlwSudokuSwipeGesturesView);
        circleEffectiveRadius = attributes.getDimensionPixelOffset(R.styleable.AvlwSudokuSwipeGesturesView_circleEffectiveRadius,dip2px(25));
        isShowTrack = attributes.getBoolean(R.styleable.AvlwSudokuSwipeGesturesView_isShowTrack,true);
        connectingLineWidth = attributes.getDimensionPixelOffset(R.styleable.AvlwSudokuSwipeGesturesView_connectingLineWidth,dip2px(2));
        circleShowType = attributes.getInt(R.styleable.AvlwSudokuSwipeGesturesView_circleShowType,CIRCLE_SHOW_TYPE_1);
        circleInnerRingRadius = attributes.getDimensionPixelOffset(R.styleable.AvlwSudokuSwipeGesturesView_circleInnerRingRadius,dip2px(10));//圆圈的内圈半径
        circleOuterRingRadius = attributes.getDimensionPixelOffset(R.styleable.AvlwSudokuSwipeGesturesView_circleOuterRingRadius,dip2px(25));//圆圈的外圈半径
        circleOuterRingBorderWidth = attributes.getDimensionPixelOffset(R.styleable.AvlwSudokuSwipeGesturesView_circleOuterRingBorderWidth,dip2px(1));//圆圈的外圈的边框宽度
        circleInnerRingSelectedColor = attributes.getColor(R.styleable.AvlwSudokuSwipeGesturesView_circleInnerRingSelectedColor, Color.WHITE);//圆圈内圈选中颜色
        circleInnerRingUnSelectedColor = attributes.getColor(R.styleable.AvlwSudokuSwipeGesturesView_circleInnerRingUnSelectedColor, Color.WHITE);//圆圈内圈未选中颜色
        circleInnerRingErrorColor = attributes.getColor(R.styleable.AvlwSudokuSwipeGesturesView_circleInnerRingErrorColor, Color.WHITE);//圆圈内圈错误颜色
        circleOuterRingSelectedColor = attributes.getColor(R.styleable.AvlwSudokuSwipeGesturesView_circleOuterRingSelectedColor, Color.WHITE);//圆圈外圈选中颜色
        circleOuterRingUnSelectedColor = attributes.getColor(R.styleable.AvlwSudokuSwipeGesturesView_circleOuterRingUnSelectedColor, Color.WHITE);//圆圈外圈未选中颜色
        circleOuterRingErrorColor = attributes.getColor(R.styleable.AvlwSudokuSwipeGesturesView_circleOuterRingErrorColor, Color.WHITE);//圆圈外圈错误颜色
        connectingLineColor = attributes.getColor(R.styleable.AvlwSudokuSwipeGesturesView_connectingLineColor, Color.WHITE);//连接线链接时颜色
        connectingLineErrorColor = attributes.getColor(R.styleable.AvlwSudokuSwipeGesturesView_connectingLineErrorColor, Color.WHITE);//连接线错误的时候的颜色
        setCircleShowType(circleShowType);
    }

    /**
     * 设置画笔
     * @param paint
     * @param color
     * @param isHaveBorder
     * @param borderWidth
     */
    private void setPaint(Paint paint,int color,boolean isHaveBorder,int borderWidth){
        paint.setAntiAlias(true);
        paint.setColor(color);
        if(isHaveBorder){
            paint.setStrokeWidth(borderWidth);
            paint.setStyle(Paint.Style.STROKE);
        }else {
            paint.setStyle(Paint.Style.FILL);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(getMeasuredWidth() > 0 && getMeasuredHeight() > 0){
            //init all circles
            int hor = getMeasuredWidth() / 6;
            int ver = getMeasuredHeight() / 6;
            circleInfoList.clear();
            for (int i = 0; i < 9; i++) {
                int tempX = (i % 3 + 1) * 2 * hor - hor;
                int tempY = (i / 3 + 1) * 2 * ver - ver;
                circleInfoList.add(new CircleInfo(i,tempX,tempY,false));
            }

            int min = Math.min(hor, ver);
            maxCircleEffectiveRadius = min;
            if(circleEffectiveRadius > maxCircleEffectiveRadius){
                circleEffectiveRadius = maxCircleEffectiveRadius;
            }
            //判断外圈半径
            if(circleOuterRingRadius > circleEffectiveRadius){
                circleOuterRingRadius = circleEffectiveRadius;
            }
            //判断内圈半径
            if(circleInnerRingRadius > circleEffectiveRadius){
                circleInnerRingRadius = circleEffectiveRadius;
            }
            //判断内外圈大小,内大于外则交换
            if(circleInnerRingRadius > circleOuterRingRadius){
                int x = circleInnerRingRadius;
                circleInnerRingRadius = circleOuterRingRadius;
                circleOuterRingRadius = x;
            }
        }
    }




    /***************************************手势滑动监听以及图像绘制***********************************/
    private int nowX;
    private int nowY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(allowDraw) {
            nowX = (int) event.getX();
            nowY = (int) event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(nowDataState == DATA_STATE_FINISH
                            || nowDataState == DATA_STATE_TRUE
                            || nowDataState == DATA_STATE_FALSE) {
                        resetAll();
                    }else if(nowDataState == DATA_STATE_WAIT_FINISH){
                        nowDataState = DATA_STATE_INPUT;
                    }
                case MotionEvent.ACTION_MOVE:
                    if(nowDataState == DATA_STATE_INIT
                            || nowDataState == DATA_STATE_WAIT_FINISH
                            || nowDataState == DATA_STATE_INPUT) {
                        nowDataState = DATA_STATE_INPUT;
                        addSelectCircle(getOuterCircle(nowX, nowY));
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    nowDataState = DATA_STATE_WAIT_FINISH;
                    handler.removeMessages(WAIT_FINISH);
                    handler.sendEmptyMessageDelayed(WAIT_FINISH,10);
                    break;
                default:
                    break;
            }
            invalidate();
        }
        return true;

    }
    @Override
    protected void onDraw(Canvas canvas) {
        if(circleInfoList.size() > 0){
            switch (circleShowType){
                case CIRCLE_SHOW_TYPE_1://内圈实心，外圈空心带边框，滑动时内外圈变色，连接线从中心点出来，此模式下需要传入边框宽度
                    drawCanvasCircleShowType(canvas,true,true
                            ,true,true
                            ,true,true);
                    break;
                case CIRCLE_SHOW_TYPE_2://内圈实心，外圈实心，未选中的时候内圈显示，外圈不显示，滑动时外圈显示，连接线从中心点出来
                    drawCanvasCircleShowType(canvas,true,false
                            ,true,true
                            ,true,true);
                    break;
                default:
                    break;

            }
            drawLineMove(canvas);
        }
        super.onDraw(canvas);
    }
    /**
     * 添加选中圆
     * @param circleInfo
     */
    private void addSelectCircle(CircleInfo circleInfo){
        if(circleInfo != null){
            circleInfo.isSelect = true;
            selectCirclePosiList.add(circleInfo);
        }
    }
    /**
     * 判断传入坐标是否在圆内
     * @param x
     * @param y
     * @return
     */
    @Nullable
    private CircleInfo getOuterCircle(int x, int y) {
        Iterator<CircleInfo> iterator = circleInfoList.iterator();
        CircleInfo circleInfo;
        int distanceX;
        int distanceY;
        int distanceZ;
        while (iterator.hasNext()){
            circleInfo = iterator.next();
            //点击位置x坐标与圆心的x坐标的距离
            distanceX = Math.abs(circleInfo.x - x);
            //点击位置y坐标与圆心的y坐标的距离
            distanceY = Math.abs(circleInfo.y - y);
            //点击位置与圆心的直线距离
            distanceZ = (int) Math.sqrt(Math.pow(distanceX,2)+Math.pow(distanceY,2));

            //如果点击位置与圆心的距离大于圆的半径，证明点击位置没有在圆内,同时也没有被选中的
            if(distanceZ <= circleEffectiveRadius && !circleInfo.isSelect){
                iterator = null;
                //做是否跳过位置的判定，如果产生位置跳转则要把跳过的位置加入（当跳过的位置时未选中的时候）
                CircleInfo lastSelectCircle;
                CircleInfo passCircle = null;
                if(selectCirclePosiList.size() > 0){
                    lastSelectCircle = selectCirclePosiList.get(selectCirclePosiList.size() - 1);
                    switch (lastSelectCircle.posi){
                        case 0://此位置会被跳的终点位置为位置6、8、2
                            switch (circleInfo.posi){
                                case 2:
                                    //此时被跳过的位置时1，判断如果是未选中的话则加入到列表中
                                    passCircle = circleInfoList.get(1);
                                    break;
                                case 6:
                                    //此时被跳过的位置时3，判断如果是未选中的话则加入到列表中
                                    passCircle = circleInfoList.get(3);
                                    break;
                                case 8:
                                    //此时被跳过的位置时4，判断如果是未选中的话则加入到列表中
                                    passCircle = circleInfoList.get(4);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 1://此位置会被跳的终点位置为位置7
                            switch (circleInfo.posi){
                                case 7:
                                    //此时被跳过的位置时4，判断如果是未选中的话则加入到列表中
                                    passCircle = circleInfoList.get(4);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 3://此位置会被跳的终点位置为位置5
                            switch (circleInfo.posi){
                                case 5:
                                    //此时被跳过的位置时4，判断如果是未选中的话则加入到列表中
                                    passCircle = circleInfoList.get(4);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 5://此位置会被跳的终点位置为位置3
                            switch (circleInfo.posi){
                                case 3:
                                    //此时被跳过的位置时4，判断如果是未选中的话则加入到列表中
                                    passCircle = circleInfoList.get(4);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 7://此位置会被跳的终点位置为位置1
                            switch (circleInfo.posi){
                                case 1:
                                    //此时被跳过的位置时4，判断如果是未选中的话则加入到列表中
                                    passCircle = circleInfoList.get(4);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 2://此位置会被跳的终点位置为位置0、6、8
                            switch (circleInfo.posi){
                                case 0:
                                    //此时被跳过的位置时1，判断如果是未选中的话则加入到列表中
                                    passCircle = circleInfoList.get(1);
                                    break;
                                case 6:
                                    //此时被跳过的位置时4，判断如果是未选中的话则加入到列表中
                                    passCircle = circleInfoList.get(4);
                                    break;
                                case 8:
                                    //此时被跳过的位置时5，判断如果是未选中的话则加入到列表中
                                    passCircle = circleInfoList.get(5);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 6://此位置会被跳的终点位置为位置0、2、8
                            switch (circleInfo.posi){
                                case 0:
                                    //此时被跳过的位置时3，判断如果是未选中的话则加入到列表中
                                    passCircle = circleInfoList.get(3);
                                    break;
                                case 2:
                                    //此时被跳过的位置时4，判断如果是未选中的话则加入到列表中
                                    passCircle = circleInfoList.get(4);
                                    break;
                                case 8:
                                    //此时被跳过的位置时7，判断如果是未选中的话则加入到列表中
                                    passCircle = circleInfoList.get(7);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 8://此位置会被跳的终点位置为位置0、2、6
                            switch (circleInfo.posi){
                                case 0:
                                    //此时被跳过的位置时4，判断如果是未选中的话则加入到列表中
                                    passCircle = circleInfoList.get(4);
                                    break;
                                case 2:
                                    //此时被跳过的位置时5，判断如果是未选中的话则加入到列表中
                                    passCircle = circleInfoList.get(5);
                                    break;
                                case 6:
                                    //此时被跳过的位置时7，判断如果是未选中的话则加入到列表中
                                    passCircle = circleInfoList.get(7);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                    if(passCircle != null && !passCircle.isSelect){
                        addSelectCircle(passCircle);
                    }
                }


                return circleInfo;
            }
        }
        return null;
    }
    /**
     * 绘制移动线条
     * @param canvas
     */
    private void drawLineMove(Canvas canvas){
        int size = selectCirclePosiList.size();
        if(nowDataState == DATA_STATE_INPUT && isShowTrack && size > 0 && size < 9) {
            CircleInfo circleInfo = selectCirclePosiList.get(selectCirclePosiList.size() - 1);
            canvas.drawLine(circleInfo.x, circleInfo.y, nowX, nowY, connectingSelectLinePaint);
        }
    }
    /**
     *
     * @param canvas
     * @param circleInnerRingIsShowForUnSelect 未选中的内圈是否要显示
     * @param circleOuterRingIsShowForUnSelect 未选中的外圈是否要显示
     * @param circleInnerRingIsShowForOptions 正在滑动操作的时候内圈是否要显示
     * @param circleOuterRingIsShowForOptions 正在滑动操作的时候外圈是否要显示
     * @param circleInnerRingIsShowForDataFalse 当数据是错误的时候内圈是否要显示
     * @param circleOuterRingIsShowForDataFalse 当数据是错误的时候外圈是否要显示
     */
    private void drawCanvasCircleShowType(Canvas canvas,boolean circleInnerRingIsShowForUnSelect
            ,boolean circleOuterRingIsShowForUnSelect,boolean circleInnerRingIsShowForOptions
            ,boolean circleOuterRingIsShowForOptions,boolean circleInnerRingIsShowForDataFalse
            ,boolean circleOuterRingIsShowForDataFalse){
        CircleInfo circleInfo;
        if(isShowTrack) {
            //先绘制选中的
            int size = selectCirclePosiList.size();
            CircleInfo circleInfoLast = null;
            //数据状态是输入中的时候绘制选中状态下的状况
            switch (nowDataState){
                case DATA_STATE_INPUT:
                case DATA_STATE_FINISH:
                case DATA_STATE_TRUE:
                    for (int i = 0; i < size; i++) {
                        circleInfo = selectCirclePosiList.get(i);
                        drawItem(canvas,circleInfo,circleInfoLast,connectingSelectLinePaint
                                ,circleInnerRingIsShowForOptions,circleInnerRingRadius,circleInnerRingSelectedPaint
                                ,circleOuterRingIsShowForOptions,circleOuterRingRadius,circleOuterRingSelectedPaint);
                        circleInfoLast = circleInfo;
                    }
                    break;
                case DATA_STATE_FALSE:
                    for (int i = 0; i < size; i++) {
                        circleInfo = selectCirclePosiList.get(i);
                        drawItem(canvas,circleInfo,circleInfoLast,connectingSelectErrorLinePaint
                                ,circleInnerRingIsShowForOptions,circleInnerRingRadius,circleInnerRingErrorPaint
                                ,circleOuterRingIsShowForOptions,circleOuterRingRadius,circleOuterRingErrorPaint);
                        circleInfoLast = circleInfo;
                    }
                    break;
                case DATA_STATE_INIT:
                default:
                    break;
            }
            circleInfo = null;
            circleInfoLast = null;
        }

        //绘制未选中的
        if(circleInnerRingIsShowForUnSelect && !circleOuterRingIsShowForUnSelect){
            for(int i = 0; i < 9 ; i++){
                circleInfo = circleInfoList.get(i);
                if(selectCirclePosiList.contains(circleInfo) && isShowTrack){
                    continue;
                }
                canvas.drawCircle(circleInfo.x, circleInfo.y, circleInnerRingRadius, circleInnerRingUnSelectedPaint);
            }
        }else if(!circleInnerRingIsShowForUnSelect && circleOuterRingIsShowForUnSelect){
            for(int i = 0; i < 9 ; i++){
                circleInfo = circleInfoList.get(i);
                if(selectCirclePosiList.contains(circleInfo) && isShowTrack){
                    continue;
                }
                canvas.drawCircle(circleInfo.x, circleInfo.y, circleOuterRingRadius, circleOuterRingUnSelectPaint);
            }
        }else {
            for(int i = 0; i < 9 ; i++){
                circleInfo = circleInfoList.get(i);
                if(selectCirclePosiList.contains(circleInfo) && isShowTrack){
                    continue;
                }
                canvas.drawCircle(circleInfo.x, circleInfo.y, circleInnerRingRadius, circleInnerRingUnSelectedPaint);
                canvas.drawCircle(circleInfo.x, circleInfo.y, circleOuterRingRadius, circleOuterRingUnSelectPaint);
            }
        }

    }
    /**
     * 绘制每一个位置的圆圈
     * @param canvas 画布
     * @param circleInfo 要绘制的圆对象
     * @param circleInnerRingRadius 内圈半径
     * @param circleInnerRingPaint 内圈画笔
     * @param circleInnerRingIsShow 内圈是否显示
     * @param circleOuterRingRadius 外圈半径
     * @param circleOuterRingPaint 外圈画笔
     * @param circleOuterRingIsShow 外圈是否显示
     * @param circleInfoLast 要连接线的上一个实体圆对象，为空的时候不连接
     * @param linePaint 连接线画笔
     */
    private void drawItem(Canvas canvas,CircleInfo circleInfo,CircleInfo circleInfoLast,Paint linePaint
            ,boolean circleInnerRingIsShow,int circleInnerRingRadius,Paint circleInnerRingPaint
            ,boolean circleOuterRingIsShow,int circleOuterRingRadius,Paint circleOuterRingPaint){
        //绘制外圈
        if(circleOuterRingIsShow) {
            canvas.drawCircle(circleInfo.x, circleInfo.y, circleOuterRingRadius, circleOuterRingPaint);
        }
        //绘制中心圆
        if(circleInnerRingIsShow){
            canvas.drawCircle(circleInfo.x, circleInfo.y, circleInnerRingRadius, circleInnerRingPaint);
        }
        //绘制两个圆中间的连接线
        if(circleInfoLast != null && linePaint != null){
            canvas.drawLine(circleInfoLast.x, circleInfoLast.y, circleInfo.x, circleInfo.y, linePaint);
        }

    }





    /******************************************内部私有方法******************************************/

    /**
     * 结束输入并回传数据
     */
    private void finishInput(){
        if(inputStateChangeCallback == null){
            handler.sendEmptyMessageDelayed(RESET_VIEW,1500);
        }else {
            int size = selectCirclePosiList.size();
            int[] posis = new int[size];
            for(int i = 0 ; i < size ; i++){
                posis[i] = selectCirclePosiList.get(i).posi;
            }
            inputStateChangeCallback.finishInput(posis);
        }
    }
    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @return
     */
    private int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }



    /*******************************************外部开放方法*****************************************/
    public AvlwSudokuSwipeGesturesView setCircleShowType(@AvlwSudokuSwipeGesturesViewShowType int circleShowType) {
        this.circleShowType = circleShowType;
        switch (circleShowType){
            case CIRCLE_SHOW_TYPE_2://内圈实心，外圈实心，未选中的时候内圈显示，外圈不显示，滑动时外圈显示，连接线从中心点出来
                setPaint(circleInnerRingSelectedPaint,circleInnerRingSelectedColor,false,0);
                setPaint(circleInnerRingUnSelectedPaint,circleInnerRingUnSelectedColor,false,0);
                setPaint(circleInnerRingErrorPaint,circleInnerRingErrorColor,false,0);
                setPaint(circleOuterRingSelectedPaint,circleOuterRingSelectedColor,false,0);
                setPaint(circleOuterRingUnSelectPaint,circleOuterRingUnSelectedColor,false,0);
                setPaint(circleOuterRingErrorPaint,circleOuterRingErrorColor,false,0);
                setPaint(connectingSelectLinePaint,connectingLineColor,true,connectingLineWidth);
                setPaint(connectingSelectErrorLinePaint,connectingLineErrorColor,true,connectingLineWidth);
                break;
            case CIRCLE_SHOW_TYPE_1://内圈实心，外圈空心带边框，滑动时内外圈变色，连接线从中心点出来，此模式下需要传入边框宽度
                setPaint(circleInnerRingSelectedPaint,circleInnerRingSelectedColor,false,0);
                setPaint(circleInnerRingUnSelectedPaint,circleInnerRingUnSelectedColor,false,0);
                setPaint(circleInnerRingErrorPaint,circleInnerRingErrorColor,false,0);
                setPaint(circleOuterRingSelectedPaint,circleOuterRingSelectedColor,true,circleOuterRingBorderWidth);
                setPaint(circleOuterRingUnSelectPaint,circleOuterRingUnSelectedColor,true,circleOuterRingBorderWidth);
                setPaint(circleOuterRingErrorPaint,circleOuterRingErrorColor,true,circleOuterRingBorderWidth);
                setPaint(connectingSelectLinePaint,connectingLineColor,true,connectingLineWidth);
                setPaint(connectingSelectErrorLinePaint,connectingLineErrorColor,true,connectingLineWidth);
            default:
                break;
        }
        return this;
    }
    /**
     * 重置所有状态
     */
    public void resetAll() {
        nowDataState = DATA_STATE_INIT;
        Iterator<CircleInfo> iterator = selectCirclePosiList.iterator();
        CircleInfo circleInfo;
        while (iterator.hasNext()){
            circleInfo = iterator.next();
            circleInfo.isSelect = false;
            iterator.remove();
            selectCirclePosiList.remove(circleInfo);
        }
        invalidate();
    }
    /**
     * 手势错误调用（一般会交由外部方法调用）
     */
    public void gestureError(){
        nowDataState = DATA_STATE_FALSE;
        invalidate();
        //指定时间之后重置
        handler.sendEmptyMessageDelayed(RESET_VIEW,1500);
    }
    /**
     * 设置回调
     * @param inputStateChangeCallback
     * @return
     */
    public AvlwSudokuSwipeGesturesView setInputStateChangeCallback(InputStateChangeCallback inputStateChangeCallback) {
        this.inputStateChangeCallback = inputStateChangeCallback;
        return this;
    }

    public AvlwSudokuSwipeGesturesView setAllowDraw(boolean allowDraw) {
        this.allowDraw = allowDraw;
        return this;
    }

    /**
     * 圆圈的部分属性
     */
    private class CircleInfo{
        private int x;
        private int y;
        private int posi;
        private boolean isSelect;//是否选中了

        public CircleInfo(int posi,int x, int y, boolean isSelect) {
            this.posi = posi;
            this.x = x;
            this.y = y;
            this.isSelect = isSelect;
        }

        @Override
        public String toString() {
            return "CircleInfo{" +
                    "x=" + x +
                    ", y=" + y +
                    ", isSelect=" + isSelect +
                    '}';
        }
    }

    /**
     * 回调方法
     */
    public interface InputStateChangeCallback{
        void finishInput(int[] posis);//回传位置数组
    }
}
