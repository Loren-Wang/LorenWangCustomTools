package javabase.lorenwang.tools.thread;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import javabase.lorenwang.tools.base.BaseUtils;

/**
 * Created by LorenWang on 2018/8/2 0002.
 * 创建时间：2018/8/2 0002 下午 05:42
 * 创建人：王亮（Loren wang）
 * 功能作用：定时器单例类
 * 思路：
 * 方法：1、开启一个定时器，在制定时间之后执行runnable
 *      2、开启一个定时器，在等待delay后执行第一次任务，第二次（含）之后间隔period时间后再次执行
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class TimerUtils extends BaseUtils {
    private final String TAG = getClass().getName();

    public static TimerUtils getInstance() {
        if (baseUtils == null) {
            baseUtils = new TimerUtils();
        }
        return (TimerUtils) baseUtils;
    }

    private Timer timer = new Timer();
    private Map<Integer, TimerTask> timerTaskMap = new ConcurrentHashMap<>();

    /**
     * 开启一个定时器，在制定时间之后执行runnable
     *
     * @param runnable
     * @param delay    等待时间
     * @return
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
        timerTaskMap.put(taskId,timerTask);
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
        timerTaskMap.put(taskId,timerTask);
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


}
