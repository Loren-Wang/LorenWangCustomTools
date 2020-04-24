package javabase.lorenwang.tools.thread;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import javabase.lorenwang.tools.common.JtlwDateTimeUtils;

/**
 * 功能作用：定时器单例类
 * 初始注释时间： 2018/8/2 0002 下午 05:42
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 1、开启一个定时器，在制定时间之后执行runnable
 * 2、开启一个定时器，在等待delay后执行第一次任务，第二次（含）之后间隔period时间后再次执行
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author LorenWang（王亮）
 */
public class JtlwTimerUtils {
    private final String TAG = getClass().getName();
    private static volatile JtlwTimerUtils optionUtils;

    /**
     * 私有构造
     */
    private JtlwTimerUtils() {
    }

    public static JtlwTimerUtils getInstance() {
        if (optionUtils == null) {
            synchronized (JtlwTimerUtils.class) {
                if (optionUtils == null) {
                    optionUtils = new JtlwTimerUtils();
                }
            }
        }
        return optionUtils;
    }

    private final Timer TIMER = new Timer();

    /**
     * 任务map集合记录
     */
    private final Map<Integer, TimerTask> TIMER_TASK_MAP = new ConcurrentHashMap<>();

    /**
     * 开启一个定时器，在指定时间之后执行runnable
     *
     * @param runnable 线程
     * @param delay    等待时间
     * @return 定时器
     */
    public TimerTask schedule(int taskId, final Runnable runnable, long delay) {
        if (runnable == null) {
            return null;
        }
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        };
        cancelTimerTask(taskId);
        TIMER_TASK_MAP.put(taskId, timerTask);
        TIMER.schedule(timerTask, delay);
        return timerTask;
    }

    /**
     * 开启一个定时器，在等待delay后执行第一次任务，第二次（含）之后间隔period时间后再次执行
     *
     * @param runnable 定时器内容任务代码块
     * @param delay    等待时间
     * @param period   间隔时间
     * @return 被开启的定时器
     */
    public TimerTask schedule(int taskId, final Runnable runnable, long delay, long period) {
        if (runnable == null) {
            return null;
        }
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        };
        cancelTimerTask(taskId);
        TIMER_TASK_MAP.put(taskId, timerTask);
        TIMER.schedule(timerTask, delay, period);
        return timerTask;
    }

    /**
     * 取消计时器
     *
     * @param taskId 计时器任务id
     */
    public void cancelTimerTask(int taskId) {
        TimerTask timerTask = TIMER_TASK_MAP.get(taskId);
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    /**
     * 倒计时任务
     *
     * @param taskId            倒计时任务id
     * @param countDownCallback 倒计时回调
     * @param sumTime           总时间（必须大于0）
     * @param period            间隔时间（必须大于0）
     */
    public void countDownTask(int taskId, final CountDownCallback countDownCallback
            , long sumTime, long period) {
        if (countDownCallback == null) {
            return;
        }
        TimerTask timerTask = new TimerTask() {
            private Long nowTime = null;
            /**
             * 任务开始时间
             */
            private final Long START_TIME = JtlwDateTimeUtils.getInstance().getMillisecond();

            @Override
            public void run() {
                nowTime = sumTime - (JtlwDateTimeUtils.getInstance().getMillisecond()
                        - START_TIME);
                if (nowTime <= 0) {
                    //结束了就取消任务
                    cancelTimerTask(taskId);
                    countDownCallback.finish();
                } else {
                    countDownCallback.countDownTime(sumTime, nowTime);
                }
            }
        };
        cancelTimerTask(taskId);
        TIMER_TASK_MAP.put(taskId, timerTask);
        TIMER.schedule(timerTask, 0, period);
    }


}
