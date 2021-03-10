package android.lorenwang.customview.validation;

import android.content.Context;
import android.content.res.TypedArray;
import android.lorenwang.customview.R;
import android.lorenwang.tools.app.AtlwThreadUtil;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.jetbrains.annotations.NotNull;

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
     * 移动最大距离，-1代表着不限制
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
     * 重置线程每次延迟时间
     */
    private final long resetRunnableDelayMillis = 5;
    /**
     * 重置线程
     */
    private final Runnable resetRunnable = new Runnable() {
        private int change = 0;

        @Override
        public void run() {
            //修改记录
            if (change == 0) {
                change = offset / 50;
                if (change == 0) {
                    change = offset / 5;
                    if (change == 0) {
                        change = offset;
                    }
                }
            }
            if (offset > 0) {
                //开始移动
                offset -= change;
                AtlwThreadUtil.getInstance().postOnChildThreadDelayed(this, resetRunnableDelayMillis);
            } else {
                offset = 0;
                isReset = false;
                AtlwThreadUtil.getInstance().removeRunnable(this);
            }
            moveAllView();
        }
    };

    public AvlwSliderValidationView(@NonNull @NotNull Context context) {
        super(context);
        init(context, null, -1);
    }

    public AvlwSliderValidationView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public AvlwSliderValidationView(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.AvlwSliderValidationView);
        touchViewId = attributes.getResourceId(R.styleable.AvlwSliderValidationView_avlwSliderValidationViewTouchView, -1);
        moveViewId = attributes.getResourceId(R.styleable.AvlwSliderValidationView_avlwSliderValidationViewMoveView, -1);
        moveMaxWidthPercent = attributes.getFloat(R.styleable.AvlwSliderValidationView_avlwSliderValidationViewMoveMaxWidthPercent,
                moveMaxWidthPercent);
        moveConfirmWidthPercent = attributes.getFloat(R.styleable.AvlwSliderValidationView_avlwSliderValidationViewMoveConfirmWidthPercent,
                moveConfirmWidthPercent);
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
                        moveAllView();
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
     * 重置
     */
    public void reset() {
        if (!isReset) {
            isReset = true;
            AtlwThreadUtil.getInstance().postOnChildThreadDelayed(resetRunnable, resetRunnableDelayMillis);
        }
    }

    /**
     * 移动所有控件
     */
    private void moveAllView() {
        touchView.setTranslationX(offset);
        if (moveView != null) {
            moveView.setTranslationX(offset);
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
