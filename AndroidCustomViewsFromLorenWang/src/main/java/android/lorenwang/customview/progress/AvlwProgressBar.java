package android.lorenwang.customview.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.lorenwang.customview.R;
import android.util.AttributeSet;
import android.view.View;

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
        switch (attributes.getInt(R.styleable.AvlwProgressBar_avlwShowProgressType, PROGRESS_TYPE_1)) {
            case PROGRESS_TYPE_1:
            default:
                avlwProgressBarBase = new AvlwProgressBarVideoPlay();
                break;
        }
        this.avlwProgressBarBase.init(context, this, attributes);
        attributes.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(avlwProgressBarBase.getMeasureWidth(widthMeasureSpec),
                avlwProgressBarBase.getMeasureHeight(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        avlwProgressBarBase.onDrawRegion(canvas, getPaddingLeft(), getPaddingTop(),
                getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
    }

}
