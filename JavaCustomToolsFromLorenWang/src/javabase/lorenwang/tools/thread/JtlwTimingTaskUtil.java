package javabase.lorenwang.tools.thread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javabase.lorenwang.tools.common.JtlwDateTimeUtil;

/**
 * 功能作用：定时任务工具类
 * 创建时间：2020-06-02 5:31 下午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 开启一个定时器，在制定时间之后执行runnable--schedule(taskId,runnable,delay)
 * 开启一个定时器，在等待delay后执行第一次任务，第二次（含）之后间隔period时间后再次执行--schedule(taskId,runnable,delay,period)
 * 开启一个倒计时任务--countDownTask(taskId,countDownCallback,sumTime,period)
 * 取消计时器--cancelTimingTask(taskId)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：所有的时间单位都是毫秒值
 *
 * @author 王亮（Loren wang）
 */

public class JtlwTimingTaskUtil {
    private final String TAG = getClass().getName();
    private static volatile JtlwTimingTaskUtil optionsInstance;
    private final ScheduledThreadPoolExecutor threadPoolExecutor;
    /**
     * 任务map集合记录
     */
    private final Map<Integer, Runnable> TIMING_TASK_MAP = new ConcurrentHashMap<>();
    private final Map<Integer, ScheduledFuture> TIMING_TASK_MAP_SCHEDULED = new ConcurrentHashMap<>();

    private JtlwTimingTaskUtil() {
        threadPoolExecutor = new ScheduledThreadPoolExecutor(1, (ThreadFactory) Thread::new);
    }

    public static JtlwTimingTaskUtil getInstance() {
        if (optionsInstance == null) {
            synchronized (JtlwTimingTaskUtil.class) {
                if (optionsInstance == null) {
                    optionsInstance = new JtlwTimingTaskUtil();
                }
            }
        }
        return optionsInstance;
    }

    /**
     * 开启一个定时器，在指定时间之后执行runnable
     *
     * @param taskId   任务id
     * @param runnable 线程
     * @param delay    等待时间
     */
    public void schedule(int taskId, final Runnable runnable, long delay) {
        if (runnable == null) {
            return;
        }
        //先取消旧任务
        cancelTimingTask(taskId);
        //启动新任务
        ScheduledFuture<?> schedule = threadPoolExecutor.schedule(runnable, delay, TimeUnit.MILLISECONDS);
        //存储记录
        TIMING_TASK_MAP.put(taskId, runnable);
        TIMING_TASK_MAP_SCHEDULED.put(taskId, schedule);
    }

    /**
     * 开启一个定时器，在等待delay后执行第一次任务，第二次（含）之后间隔period时间后再次执行
     * ps：如果上一次任务的执行时间，超过了定义的延迟周期（period）,则后续的任务可能会延迟执行：
     *
     * @param taskId   任务id
     * @param runnable 定时器内容任务代码块
     * @param delay    等待时间
     * @param period   间隔时间
     */
    public void schedule(int taskId, final Runnable runnable, long delay, long period) {
        if (runnable == null) {
            return;
        }
        //先取消旧任务
        cancelTimingTask(taskId);
        //启动新任务
        ScheduledFuture<?> schedule = threadPoolExecutor.scheduleAtFixedRate(runnable, delay, period, TimeUnit.MILLISECONDS);
        //存储记录
        TIMING_TASK_MAP.put(taskId, runnable);
        TIMING_TASK_MAP_SCHEDULED.put(taskId, schedule);
    }

    /**
     * 倒计时任务
     *
     * @param taskId            倒计时任务id
     * @param countDownCallback 倒计时回调
     * @param sumTime           总时间（必须大于0）
     * @param period            间隔时间（必须大于0）
     */
    public void countDownTask(int taskId, final CountDownCallback countDownCallback, long sumTime, long period) {
        if (countDownCallback == null) {
            return;
        }
        schedule(taskId, new Runnable() {
            private Long nowTime = null;
            /**
             * 任务开始时间
             */
            private final Long START_TIME = JtlwDateTimeUtil.getInstance().getMillisecond();

            @Override
            public void run() {
                nowTime = sumTime - (JtlwDateTimeUtil.getInstance().getMillisecond() - START_TIME);
                if (nowTime <= 0) {
                    //结束了就取消任务
                    cancelTimingTask(taskId);
                    countDownCallback.finish();
                } else {
                    countDownCallback.countDownTime(sumTime, nowTime);
                }
            }
        }, 0, period);
    }

    /**
     * 取消计时器
     *
     * @param taskId 计时器任务id
     */
    public void cancelTimingTask(int taskId) {
        Runnable task = TIMING_TASK_MAP.get(taskId);
        if (task != null) {
            threadPoolExecutor.remove(task);
            threadPoolExecutor.purge();
            TIMING_TASK_MAP.remove(taskId);
            task = null;
        }
        ScheduledFuture scheduledFuture = TIMING_TASK_MAP_SCHEDULED.get(taskId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            TIMING_TASK_MAP_SCHEDULED.remove(taskId);
        }
    }
}
