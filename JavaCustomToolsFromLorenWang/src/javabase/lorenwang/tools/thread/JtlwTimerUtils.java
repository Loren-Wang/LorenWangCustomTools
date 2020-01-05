package javabase.lorenwang.tools.thread;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by LorenWang on 2018/8/2 0002.
 * 创建时间：2018/8/2 0002 下午 05:42
 * 创建人：王亮（Loren wang）
 * 功能作用：定时器单例类
 * 思路：
 * 方法：1、开启一个定时器，在制定时间之后执行runnable
 * 2、开启一个定时器，在等待delay后执行第一次任务，第二次（含）之后间隔period时间后再次执行
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
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

    private Timer timer = new Timer();
    private Map<Integer, TimerTask> timerTaskMap = new ConcurrentHashMap<>();

    /**
     * 开启一个定时器，在制定时间之后执行runnable
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
        timerTaskMap.put(taskId, timerTask);
        timer.schedule(timerTask, delay);
        return timerTask;
    }

    /**
     * 开启一个定时器，在等待delay后执行第一次任务，第二次（含）之后间隔period时间后再次执行
     *
     * @param runnable
     * @param delay    等待时间
     * @param period   间隔时间
     * @return
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
        timerTaskMap.put(taskId, timerTask);
        timer.schedule(timerTask, delay, period);
        return timerTask;
    }

    /**
     * 取消计时器
     *
     * @param taskId
     */
    public void cancelTimerTask(int taskId) {
        TimerTask timerTask = timerTaskMap.get(taskId);
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    /**
     * 倒计时任务
     *
     * @param taskId
     * @param countDownCallback 倒计时回调
     * @param sumTime           总时间（必须大于0）
     * @param period            间隔时间（必须大于0）
     * @return
     */
    public TimerTask countDownTask(int taskId, final CountDownCallback countDownCallback
            , long sumTime, long period) {
        if (countDownCallback == null) {
            return null;
        }
        TimerTask timerTask = new TimerTask() {
            private Long nowTime = null;

            @Override
            public void run() {
                if (nowTime == null) {
                    nowTime = sumTime;
                }
                if (nowTime <= 0) {
                    //结束了就取消任务
                    cancelTimerTask(taskId);
                    countDownCallback.finish();
                } else {
                    countDownCallback.countDownTime(sumTime, nowTime);
                }
                nowTime -= period;
            }
        };
        cancelTimerTask(taskId);
        timerTaskMap.put(taskId, timerTask);
        timer.schedule(timerTask, 0, period);
        return timerTask;
    }


}
