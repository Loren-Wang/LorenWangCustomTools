package android.lorenwang.customview.imageview;

import android.content.Context;
import android.lorenwang.tools.base.AtlwLogUtil;
import android.util.AttributeSet;

import com.facebook.drawee.view.SimpleDraweeView;

import javabase.lorenwang.tools.thread.JtlwTimingTaskUtils;

/**
 * 功能作用：悬浮贴边图片
 * 创建时间：2020-11-16 11:00 上午
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
public class AvlwFrescoFloatingWaltImageView extends SimpleDraweeView {
    private final String TAG = getClass().getName();
    /**
     * 目标是否是要显示的
     */
    private boolean viewToShow = true;
    /**
     * 动画时间
     */
    private final long ANIMATION_TIME = 200L;
    /**
     * 动画时间间隔
     */
    private final long ANIMATION_TIME_PERIOD = 10;
    /**
     * 可移动总次数
     */
    private final int moveSumCount = (int) (ANIMATION_TIME / ANIMATION_TIME_PERIOD);
    /**
     * 当前移动次数
     */
    private int moveCurrentCount = moveSumCount;
    /**
     * 任务id
     */
    private final int TASK_ID = hashCode() % 10000;
    /**
     * 自动去显示的任务id
     */
    private final int TASK_ID_AUTO_TO_SHOW = TASK_ID * 10;
    /**
     * 修改线程
     */
    private final Runnable changeRunnable = this::changeLayoutPosition;
    /**
     * 显示线程
     */
    private final Runnable showRunnable = () -> {
        if (!viewToShow) {
            //取消隐藏线程
            JtlwTimingTaskUtils.getInstance().cancelTimingTask(TASK_ID);
            viewToShow = true;
            moveCurrentCount = moveSumCount - moveCurrentCount;
            //移除当前显示线程
            JtlwTimingTaskUtils.getInstance().cancelTimingTask(TASK_ID_AUTO_TO_SHOW);
            //开始任务
            JtlwTimingTaskUtils.getInstance().schedule(TASK_ID_AUTO_TO_SHOW, changeRunnable, 0);
        }
    };

    /**
     * 当前左侧坐标
     */
    private Integer currentLeft = null;

    public AvlwFrescoFloatingWaltImageView(Context context) {
        super(context);
    }

    public AvlwFrescoFloatingWaltImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AvlwFrescoFloatingWaltImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 显示视图
     */
    public void show() {
        show(500L);
    }

    /**
     * 显示视图
     *
     * @param delay 延迟判断时间
     */
    public void show(long delay) {
        //自动开启去显示任务(延迟一定时间启用)
        JtlwTimingTaskUtils.getInstance().schedule(TASK_ID_AUTO_TO_SHOW, showRunnable, delay);
    }

    /**
     * 隐藏显示
     *
     * @param useAutoShow 是否使用自动显示
     */
    public void hide(boolean useAutoShow) {
        //不管如何要先移除修改去显示的线程
        JtlwTimingTaskUtils.getInstance().cancelTimingTask(TASK_ID_AUTO_TO_SHOW);
        //使用自动显示的话要延迟500ml后显示
        if (useAutoShow) {
            show();
        }
        if (this.viewToShow) {
            this.viewToShow = false;
            moveCurrentCount = moveSumCount - moveCurrentCount;
            //取消上一个任务
            JtlwTimingTaskUtils.getInstance().cancelTimingTask(TASK_ID);
            //开始新任务
            JtlwTimingTaskUtils.getInstance().schedule(TASK_ID, changeRunnable, 0);
        }
    }

    /**
     * 获取单次移动位移
     *
     * @return 单次移动位移
     */
    private int getOneChangeDistance() {
        return getWidth() / 2 / moveSumCount;
    }

    /**
     * 修改布局位置
     */
    private void changeLayoutPosition() {
        if (moveCurrentCount < moveSumCount) {
            moveCurrentCount++;
            if (viewToShow) {
                AtlwLogUtil.logUtils.logI(TAG, "当前是向显示移动，已移动百分比：" + moveCurrentCount * 1.0F / moveSumCount);
                currentLeft = getLeft() - getOneChangeDistance();
                layout(currentLeft, getTop(), currentLeft + getWidth(), getBottom());
                //开始新任务
                JtlwTimingTaskUtils.getInstance().cancelTimingTask(TASK_ID_AUTO_TO_SHOW);
                JtlwTimingTaskUtils.getInstance().schedule(TASK_ID_AUTO_TO_SHOW, changeRunnable, ANIMATION_TIME_PERIOD);
            } else {
                AtlwLogUtil.logUtils.logI(TAG, "当前是向隐藏移动，已移动百分比：" + moveCurrentCount * 1.0F / moveSumCount);
                currentLeft = getLeft() + getOneChangeDistance();
                layout(currentLeft, getTop(), currentLeft + getWidth(), getBottom());
                //开始新任务
                JtlwTimingTaskUtils.getInstance().cancelTimingTask(TASK_ID);
                JtlwTimingTaskUtils.getInstance().schedule(TASK_ID, changeRunnable, ANIMATION_TIME_PERIOD);
            }
        }
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        //必须覆盖重新该方法，因为在一些特殊的布局容器当中会出现你修改坐标后又被重置的情况
        int left = currentLeft != null ? currentLeft : l;
        int right = currentLeft != null ? currentLeft + getWidth() : r;
        super.layout(left, t, right, b);
        AtlwLogUtil.logUtils.logI(TAG, "当前布局位置正在变更，左侧坐标为：" + left);
    }
}
