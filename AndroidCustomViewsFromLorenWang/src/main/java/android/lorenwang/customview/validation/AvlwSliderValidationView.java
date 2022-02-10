package android.lorenwang.customview.validation;

import android.content.Context;
import android.content.res.TypedArray;
import android.lorenwang.customview.R;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * 功能作用：滑块验证界面
 * 创建时间：2021-03-10 11:21
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
public class AvlwSliderValidationView extends ConstraintLayout {
    private final String TAG = "AvlwSliderValidationView";
    /**
     * 触摸控件相关
     */
    private View touchView;
    private int touchViewId;

    /**
     * 移动控件相关
     */
    private View moveView;
    private int moveViewId;

    /**
     * 偏移
     */
    private int offset = 0;

    /**
     * 移动最大距离，-1_代表着不限制
     */
    private int moveMaxWidth = -1;
    private float moveMaxWidthPercent = -1;

    /**
     * 移动的确认距离,-1为不进行确认
     */
    private int moveConfirmWidth = -1;
    private float moveConfirmWidthPercent = -1;

    /**
     * 是否在重置
     */
    private boolean isReset = false;

    /**
     * 结果回调
     */
    private OnResultCallback onResultCallback;

    /**
     * 重置需要的时间
     */
    private long resetMaxSumDuration = 500;
    /**
     * 重置时间监听
     */
    private final Interpolator resetInterpolator = input -> {
        moveAllView((int) (offset * (1 - input)));
        if (input == 1) {
            isReset = false;
            offset = 0;
        }
        return input;
    };

    public AvlwSliderValidationView(@NonNull Context context) {
        super(context);
        init(context, null, -1);
    }

    public AvlwSliderValidationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public AvlwSliderValidationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AvlwSliderValidationView);
        touchViewId = attributes.getResourceId(R.styleable.AvlwSliderValidationView_avlw_svv_touchView, -1);
        moveViewId = attributes.getResourceId(R.styleable.AvlwSliderValidationView_avlw_svv_moveView, -1);
        moveMaxWidthPercent = attributes.getFloat(R.styleable.AvlwSliderValidationView_avlw_svv_moveMaxWidthPercent, moveMaxWidthPercent);
        moveConfirmWidthPercent = attributes.getFloat(R.styleable.AvlwSliderValidationView_avlw_svv_moveConfirmWidthPercent, moveConfirmWidthPercent);
        attributes.recycle();
    }

    /**
     * 子控件加载完成
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View view;
        for (int i = 0; i < getChildCount(); i++) {
            view = getChildAt(i);
            if (view.getId() != -1) {
                if (view.getId() == touchViewId) {
                    touchView = view;
                } else if (view.getId() == moveViewId) {
                    moveView = view;
                }
            }
        }
    }

    /**
     * 宽高处理结束
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //设置确认距离百分比
        setMoveConfirmWidthPercent(moveConfirmWidthPercent);
        //确认位移距离
        setMoveMaxWidthPercent(moveMaxWidthPercent);
    }

    private float downX;
    private float downY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (touchView != null) {
            if (isReset) {
                return true;
            }
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = ev.getX();
                    downY = ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (offset != 0 || (touchView.getX() < downX && touchView.getX() + touchView.getWidth() > downX && touchView.getY() < downY &&
                            touchView.getY() + touchView.getHeight() > downY)) {
                        offset = (int) (ev.getX() - downX);
                        //最大区间判断
                        if (moveMaxWidth >= 0 && moveMaxWidth <= offset) {
                            offset = moveMaxWidth;
                        }
                        //最小区间判断
                        if (offset < 0) {
                            offset = 0;
                        }
                        moveAllView(null);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (onResultCallback == null) {
                        reset();
                    } else {
                        if (moveConfirmWidth > 0 && Math.abs(moveConfirmWidth - offset) < getWidth() * 0.02) {
                            AtlwLogUtil.logUtils.logD(TAG, "区域成功");
                            onResultCallback.success(this);
                        } else {
                            AtlwLogUtil.logUtils.logD(TAG, "区域失败");
                            onResultCallback.fail(this);
                        }
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 设置确认距离百分比
     *
     * @param moveConfirmWidthPercent 确认距离百分比
     */
    public void setMoveConfirmWidthPercent(float moveConfirmWidthPercent) {
        this.moveConfirmWidthPercent = moveConfirmWidthPercent;
        if (moveMaxWidthPercent >= 0) {
            moveMaxWidth = (int) (getWidth() * moveMaxWidthPercent);
        }
    }

    /**
     * 设置移动最大距离百分比
     *
     * @param moveMaxWidthPercent 移动距离百分比
     */
    public void setMoveMaxWidthPercent(float moveMaxWidthPercent) {
        this.moveMaxWidthPercent = moveMaxWidthPercent;
        if (moveConfirmWidthPercent >= 0) {
            moveConfirmWidth = (int) (getWidth() * moveConfirmWidthPercent);
        }
        if (touchView != null) {
            moveMaxWidth -= touchView.getWidth();
        }
        moveMaxWidth = Math.max(0, moveMaxWidth);
    }

    /**
     * 设置结果回调
     *
     * @param onResultCallback 结果回调
     */
    public void setOnResultCallback(OnResultCallback onResultCallback) {
        this.onResultCallback = onResultCallback;
    }

    /**
     * 设置重置动画时间,当前时间是针对于滑动到最大值的时候所需要的时间，当没有滑动到头的时候会按比例缩减
     *
     * @param resetMaxSumDuration 重置动画时间
     */
    public void setResetMaxSumDuration(long resetMaxSumDuration) {
        this.resetMaxSumDuration = resetMaxSumDuration;
    }

    /**
     * 重置
     */
    public void reset() {
        if (!isReset) {
            isReset = true;
            TranslateAnimation animation = new TranslateAnimation(0, 0, 0, 0);
            if (moveMaxWidth > 0) {
                animation.setDuration((long) (resetMaxSumDuration * offset * 1.0f / moveMaxWidth));
            } else {
                animation.setDuration((long) (resetMaxSumDuration * offset * 1.0f / getWidth()));
            }
            animation.setInterpolator(resetInterpolator);
            this.startAnimation(animation);
        }
    }

    /**
     * 移动所有控件
     *
     * @param move 移动位移
     */
    private void moveAllView(Integer move) {
        if (move == null) {
            move = offset;
        }
        touchView.setTranslationX(move);
        if (moveView != null) {
            moveView.setTranslationX(move);
        }
    }

    /**
     * 结果回调
     */
    public interface OnResultCallback {
        /**
         * 成功
         *
         * @param view 当前view
         */
        void success(AvlwSliderValidationView view);

        /**
         * 失败
         *
         * @param view 当前view
         */
        void fail(AvlwSliderValidationView view);
    }
}
