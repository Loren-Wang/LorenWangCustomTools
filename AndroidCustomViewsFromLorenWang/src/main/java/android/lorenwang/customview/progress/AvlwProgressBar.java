package android.lorenwang.customview.progress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.lorenwang.customview.R;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;

/**
 * 功能作用：进度条控件
 * 创建时间：2020-09-29 4:07 下午
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
public class AvlwProgressBar extends View implements AvlwProgressBarOptions {
    /**
     * 视频播放器进度条
     */
    private final int PROGRESS_TYPE_1 = 0;
    private final int PROGRESS_TYPE_2 = 1;
    private final int PROGRESS_TYPE_3 = 2;
    /**
     * 进度条实现基类
     */
    private AvlwProgressBarBase avlwProgressBarBase;

    public AvlwProgressBar(Context context) {
        super(context);
        init(context, null, -1);
    }

    public AvlwProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public AvlwProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AvlwProgressBar);
        switch (attributes.getInt(R.styleable.AvlwProgressBar_avlwShowProgressType, -1)) {
            case PROGRESS_TYPE_2:
                avlwProgressBarBase = new AvlwProgressBarScroll();
                break;
            case PROGRESS_TYPE_1:
                avlwProgressBarBase = new AvlwProgressBarVideoPlay();
                break;
            case PROGRESS_TYPE_3:
            default:
                avlwProgressBarBase = new AvlwProgressBarDefault();
                break;
        }
        this.avlwProgressBarBase.init(context, this, attributes);
        attributes.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(avlwProgressBarBase.getMeasureWidth(widthMeasureSpec), avlwProgressBarBase.getMeasureHeight(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        avlwProgressBarBase.onDrawRegion(canvas, getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Boolean touchEvent = avlwProgressBarBase.onTouchEvent(event);
        return touchEvent != null ? touchEvent : super.onTouchEvent(event);
    }

    /**
     * 设置当前进度
     *
     * @param progress 进度
     */
    @Override
    public void setProgress(@FloatRange(from = 0, to = 1) float progress) {
        avlwProgressBarBase.setProgress(progress);
    }

    /**
     * 获取当前进度
     *
     * @return 当前进度
     */
    @Override
    public float getProgress() {
        return avlwProgressBarBase.getProgress();
    }

    /**
     * 设置缓存进度
     *
     * @param progressCache 缓存进度
     */
    public void setProgressCache(@FloatRange(from = 0, to = 1) float progressCache) {
        if (avlwProgressBarBase instanceof AvlwProgressBarVideoPlay) {
            ((AvlwProgressBarVideoPlay) avlwProgressBarBase).setProgressCache(progressCache);
        }
        invalidate();
    }

    /**
     * 获取缓存进度
     *
     * @return 缓存进度
     */
    public float getProgressCache() {
        if (avlwProgressBarBase instanceof AvlwProgressBarVideoPlay) {
            return ((AvlwProgressBarVideoPlay) avlwProgressBarBase).getProgressCache();
        }
        return 0F;
    }

    /**
     * 设置进度条监听
     *
     * @param progressBarListener 进度条监听
     */
    public void setProgressBarListener(AvlwProgressBarListener progressBarListener) {
        avlwProgressBarBase.setProgressBarListener(progressBarListener);
    }
}
