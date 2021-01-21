package android.lorenwang.customview.constraintLayout;

import android.content.Context;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.constraintlayout.widget.ConstraintLayout;
import javabase.lorenwang.tools.thread.JtlwTimingTaskUtils;

/**
 * 功能作用：为了获取滑动状态的改变的布局
 * 创建时间：2020-11-19 2:15 下午
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
public class AvlwMotionEventView extends ConstraintLayout {
    private final String TAG = getClass().getName();
    /**
     * 判断滑动结束的等待时间
     */
    private final long JUDGE_SCROLL_END_WAIT_TIME = 500L;
    /**
     * 手势是否是抬起状态
     */
    private boolean actionIsUp = true;
    /**
     * 任务id
     */
    private final int TASK_ID = hashCode();
    /**
     * 判断内容滑动x偏移记录使用
     */
    private Integer judgeContentScrollX = null;
    /**
     * 判断内容滑动y偏移记录使用
     */
    private Integer judgeContentScrollY = null;
    /**
     * 自定义的滑动状态改变监听
     */
    private OnScrollStatusChangeListener onScrollStatusChangeListener;
    /**
     * 判断线程
     */
    private final Runnable runnable = () -> {
        if (actionIsUp) {
            AtlwLogUtil.logUtils.logI(TAG, "当前滚动结束");
            if (onScrollStatusChangeListener != null) {
                onScrollStatusChangeListener.endScroll();
            }
        }
    };

    public AvlwMotionEventView(Context context) {
        super(context);
    }

    public AvlwMotionEventView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AvlwMotionEventView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                AtlwLogUtil.logUtils.logI(TAG, "手势抬起");
                actionIsUp = true;
                //做显示判断
                JtlwTimingTaskUtils.getInstance().cancelTimingTask(TASK_ID);
                JtlwTimingTaskUtils.getInstance().schedule(TASK_ID, runnable, JUDGE_SCROLL_END_WAIT_TIME);
                break;
            case MotionEvent.ACTION_DOWN:
                AtlwLogUtil.logUtils.logI(TAG, "手势落下");
                actionIsUp = false;
                contentScroll((int) ev.getRawX(), (int) ev.getRawY());
                break;
            case MotionEvent.ACTION_MOVE:
                AtlwLogUtil.logUtils.logI(TAG, "手势移动");
                actionIsUp = false;
                if (onScrollStatusChangeListener != null) {
                    onScrollStatusChangeListener.startScroll();
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 内容滑动调用
     *
     * @param scrollX x轴位移
     * @param scrollY y轴位移
     */
    public void contentScroll(int scrollX, int scrollY) {
        //判断位置是否有改变
        if (judgeContentScrollX != null && judgeContentScrollY != null &&
                judgeContentScrollX.compareTo(scrollX) == 0 &&
                judgeContentScrollY.compareTo(scrollY) == 0) {
            return;
        }
        AtlwLogUtil.logUtils.logI(TAG, "接收到其子布局内容在滑动");
        this.judgeContentScrollX = scrollX;
        this.judgeContentScrollY = scrollY;
        JtlwTimingTaskUtils.getInstance().cancelTimingTask(TASK_ID);
        JtlwTimingTaskUtils.getInstance().schedule(TASK_ID, runnable, JUDGE_SCROLL_END_WAIT_TIME);
    }

    /**
     * 设置自定义滑动状态改变监听
     *
     * @param onScrollStatusChangeListener 滑动状态改变监听
     */
    public void setOnScrollStatusChangeListener(OnScrollStatusChangeListener onScrollStatusChangeListener) {
        this.onScrollStatusChangeListener = onScrollStatusChangeListener;
    }

    /**
     * 滑动状态改变监听
     */
    public interface OnScrollStatusChangeListener {
        /**
         * 开始滑动
         */
        void startScroll();

        /**
         * 结束滑动
         */
        void endScroll();
    }
}
