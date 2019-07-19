package android.lorenwang.customview.texiview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * 创建时间： 2019/7/19 0019 下午 14:04:31
 * 创建人：Loren-Wang
 * 功能作用：自定义的圆形进度按钮 用于录制视频页面的开始和暂停功能
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class AvlwCircleButtonView extends View {

    private Paint mBigCirclePaint;//外圈进度的背景色
    private Paint mSmallCirclePaint;//内圈圆
    private Paint mProgressCirclePaint;//外圈进度的颜色
    private Paint mInnerSquarePaint;//内圈的正方形

    /**
     * 自定义使用的颜色资源id
     */
    private int mBigCircleColorId = 0;//外圈进度的背景色
    private int mSmallCircleId = 0;//内圈圆形的颜色
    private int mProgressCircleId = 0;//外圈进度条的颜色
    private int mInnerSquareId = 0;//内圈的正方形的颜色

    private int mHeight;//当前View的高
    private int mWidth;//当前View的宽
    private float mBigRadius;//外圈大圆的半径
    private float mSmallRadius;//内圈小圆的半径
    private boolean isRecording = false;//录制状态
    private float mCurrentProgress = 0;//当前进度

    private int maxTime = 120;//录制最大时间s
    private float mProgressW = 10f;//圆环宽度

    public OnButtonStatusChangeListener listener;

    public AvlwCircleButtonView(Context context) {
        super(context);
        init(context, null);
    }

    public AvlwCircleButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public AvlwCircleButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //初始画笔抗锯齿、颜色
        mBigCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (mBigCircleColorId != 0) {
            mBigCirclePaint.setColor(getResources().getColor(mBigCircleColorId));
        } else {
            mBigCirclePaint.setColor(Color.parseColor("#dfdfdf"));
        }

        mSmallCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (mSmallCircleId != 0) {
            mSmallCirclePaint.setColor(getResources().getColor(mSmallCircleId));
        } else {
            mSmallCirclePaint.setColor(Color.parseColor("#d0021b"));
        }

        mProgressCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (mProgressCircleId != 0) {
            mProgressCirclePaint.setColor(getResources().getColor(mProgressCircleId));
        } else {
            mProgressCirclePaint.setColor(Color.parseColor("#d0021b"));
        }

        mInnerSquarePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (mInnerSquareId != 0) {
            mInnerSquarePaint.setColor(getResources().getColor(mInnerSquareId));
        } else {
            mInnerSquarePaint.setColor(getResources().getColor(android.R.color.white));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mBigRadius = mWidth / 2;
        mSmallRadius = mBigRadius * 0.75f;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        //绘制外圆的进度条的背景
        drawProgressBackground(canvas);

        //绘制内圆
        canvas.drawCircle(mWidth / 2, mHeight / 2, mSmallRadius, mSmallCirclePaint);

        //绘制里面的正方形
        if (isRecording) {
            canvas.drawRect(mWidth / 3, mHeight / 3, mWidth - mWidth / 3, mHeight - mHeight / 3, mInnerSquarePaint);
        }

        //录制的过程中绘制进度条
        drawProgress(canvas);
    }

    /**
     * 绘制圆形进度
     *
     * @param canvas
     */
    private void drawProgress(Canvas canvas) {
        mProgressCirclePaint.setStrokeWidth(mProgressW);
        mProgressCirclePaint.setStyle(Paint.Style.STROKE);
        //用于定义的圆弧的形状和大小的界限
        RectF oval = new RectF(mWidth / 2 - (mBigRadius - mProgressW / 2), mHeight / 2 - (mBigRadius - mProgressW / 2),
                mWidth / 2 + (mBigRadius - mProgressW / 2), mHeight / 2 + (mBigRadius - mProgressW / 2));
        //根据进度画圆弧
        float sweepAngle = mCurrentProgress / maxTime * 360;
        canvas.drawArc(oval, -180, sweepAngle, false, mProgressCirclePaint);
    }

    /**
     * 绘制圆形进度的底色圈
     *
     * @param canvas
     */
    private void drawProgressBackground(Canvas canvas) {
        mBigCirclePaint.setStrokeWidth(mProgressW);
        mBigCirclePaint.setStyle(Paint.Style.STROKE);
        //用于定义的圆弧的形状和大小的界限
        RectF oval = new RectF(mWidth / 2 - (mBigRadius - mProgressW / 2), mHeight / 2 - (mBigRadius - mProgressW / 2),
                mWidth / 2 + (mBigRadius - mProgressW / 2), mHeight / 2 + (mBigRadius - mProgressW / 2));
        canvas.drawArc(oval, 0, 360, false, mBigCirclePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isRecording = !isRecording;
                invalidate();
                if (isRecording) {//开始播放
                    mHandler.sendEmptyMessageDelayed(0, 1000);
                    if (listener != null) {
                        listener.onStart();
                    }
                } else {//暂停播放
                    mHandler.removeCallbacksAndMessages(null);
                    if (listener != null) {
                        listener.onStop();
                    }
                }
                break;
        }
        return true;

    }

    public void setOnStatusChangeListener(OnButtonStatusChangeListener listener) {
        this.listener = listener;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mCurrentProgress = mCurrentProgress + 1;
            if (mCurrentProgress >= maxTime) {//进度条正在前进
                mCurrentProgress = maxTime;
                if (listener != null) {
                    listener.onFinish();
                }
                isRecording = false;
                invalidate();
            } else {//进度条走到了最大值
                this.sendEmptyMessageDelayed(0, 1000);
                invalidate();
            }
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }


    public interface OnButtonStatusChangeListener {
        void onStart();

        void onStop();

        void onFinish();
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public void setmProgressW(float mProgressW) {
        if (mProgressW > maxTime) {
            return;
        }
        this.mProgressW = mProgressW;
    }

    public float getmCurrentProgress() {
        return mCurrentProgress;
    }

    public void setmCurrentProgress(float mCurrentProgress) {
        this.mCurrentProgress = mCurrentProgress;
    }

    public void setmBigCircleColorId(int mBigCircleColorId) {
        this.mBigCircleColorId = mBigCircleColorId;
    }

    public void setmSmallCircleId(int mSmallCircleId) {
        this.mSmallCircleId = mSmallCircleId;
    }

    public void setmProgressCircleId(int mProgressCircleId) {
        this.mProgressCircleId = mProgressCircleId;
    }

    public void setmInnerSquareId(int mInnerSquareId) {
        this.mInnerSquareId = mInnerSquareId;
    }
}
