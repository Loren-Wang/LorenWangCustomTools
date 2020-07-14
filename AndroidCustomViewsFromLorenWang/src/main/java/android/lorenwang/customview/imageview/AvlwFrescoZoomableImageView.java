package android.lorenwang.customview.imageview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * 功能作用：fresco缩放图片
 * 创建时间：2020-07-14 11:05 上午
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren wang）
 */

public class AvlwFrescoZoomableImageView extends AppCompatImageView {
    private ScaleGestureDetector mScaleDetector;
    private GestureDetector mGestureDetector;

    private float mCurrentScale = 1f;
    private Matrix mCurrentMatrix;
    private float mMidX;
    private float mMidY;
    private OnClickListener mClickListener;

    /**
     * 是否使用缩放，默认使用缩放
     */
    private boolean useZoomable = true;

    public AvlwFrescoZoomableImageView(Context context) {
        super(context);
        init();
    }

    public AvlwFrescoZoomableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AvlwFrescoZoomableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mCurrentMatrix = new Matrix();

        ScaleGestureDetector.OnScaleGestureListener scaleListener = new ScaleGestureDetector
                .SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                if (useZoomable) {
                    float scaleFactor = detector.getScaleFactor();

                    mCurrentScale *= scaleFactor;
                    if (Float.compare(mMidX, 0) == 0) {
                        mMidX = getWidth() / 2f;
                    }
                    if (Float.compare(mMidY, 0) == 0) {
                        mMidY = getHeight() / 2f;
                    }
                    mCurrentMatrix.postScale(scaleFactor, scaleFactor, mMidX, mMidY);
                    invalidate();

                    return true;
                } else {
                    return super.onScale(detector);
                }
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                if (useZoomable) {
                    super.onScaleEnd(detector);
                    if (mCurrentScale < 1f) {
                        resetZoomable();
                    }
                    checkBorder();
                }
            }
        };
        mScaleDetector = new ScaleGestureDetector(getContext(), scaleListener);

        GestureDetector.SimpleOnGestureListener gestureListener =
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        if (mClickListener != null) {
                            mClickListener.onClick(AvlwFrescoZoomableImageView.this);
                        }
                        return true;
                    }

                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                            float distanceY) {
                        if (useZoomable) {
                            if (mCurrentScale > 1f) {
                                mCurrentMatrix.postTranslate(-distanceX, -distanceY);
                                invalidate();
                                checkBorder();
                            }
                            return true;
                        } else {
                            return super.onScroll(e1, e2, distanceX, distanceY);
                        }
                    }
                };
        mGestureDetector = new GestureDetector(getContext(), gestureListener);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        resetZoomable();
        super.setImageDrawable(drawable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (useZoomable) {
            int saveCount = canvas.save();
            canvas.concat(mCurrentMatrix);
            super.onDraw(canvas);
            canvas.restoreToCount(saveCount);
        } else {
            super.onDraw(canvas);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (useZoomable) {
            mScaleDetector.onTouchEvent(event);
            if (!mScaleDetector.isInProgress()) {
                mGestureDetector.onTouchEvent(event);
            }
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener listener) {
        mClickListener = listener;
    }

    /**
     * 重置缩放
     */
    public void resetZoomable() {
        mCurrentMatrix.reset();
        mCurrentScale = 1f;
        invalidate();
    }

    public void setUseZoomable(boolean useZoomable) {
        resetZoomable();
        this.useZoomable = useZoomable;
    }

    public boolean isUseZoomable() {
        return useZoomable;
    }

    /**
     * 检查图片边界是否移到view以内
     * 目的是让图片边缘不要移动到view里面
     */
    private void checkBorder() {
        RectF rectF = new RectF(getLeft(), getTop(), getRight(), getBottom());
        mCurrentMatrix.mapRect(rectF);
        boolean reset = false;
        float dx = 0;
        float dy = 0;

        if (rectF.left > 0) {
            dx = getLeft() - rectF.left;
            reset = true;
        }
        if (rectF.top > 0) {
            dy = getTop() - rectF.top;
            reset = true;
        }
        if (rectF.right < getRight()) {
            dx = getRight() - rectF.right;
            reset = true;
        }
        if (rectF.bottom < getHeight()) {
            dy = getHeight() - rectF.bottom;
            reset = true;
        }
        if (reset) {
            mCurrentMatrix.postTranslate(dx, dy);
            invalidate();
        }
    }
}
