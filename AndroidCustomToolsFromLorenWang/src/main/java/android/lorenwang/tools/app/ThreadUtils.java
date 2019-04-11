package android.lorenwang.tools.app;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.VisibleForTesting;

import java.util.concurrent.FutureTask;


public class ThreadUtils {
    private static ThreadUtils threadUtils;
    /**
     * ui线程handler
     */
    private Handler sUiThreadHandler;
    /**
     * 子线程handler
     */
    private Handler childThreadHandler;
    private Boolean sWillOverride = false;
    /**
     * 同步锁使用
     */
    private Object sLockUI = new Object();
    /**
     * 同步锁使用
     */
    private Object sLockChild = new Object();

    private ThreadUtils() {
        //初始化主线程
        sUiThreadHandler = new Handler(Looper.getMainLooper());
        //初始化子线程
        HandlerThread handlerThread = new HandlerThread("child_thread");
        handlerThread.start();
        childThreadHandler = new Handler(handlerThread.getLooper());
    }


    public static ThreadUtils getInstance() {
        if (threadUtils == null) {
            threadUtils = new ThreadUtils();
        }
        return threadUtils;
    }

    /**
     * 获取UI线程handler
     */
    private Handler getUiThreadHandler() {
        synchronized (sLockUI) {
            if (sUiThreadHandler == null) {
                if (sWillOverride) {
                    throw new RuntimeException("Did not yet override the UI thread");
                }
                sUiThreadHandler = new Handler(Looper.getMainLooper());
            }
            return sUiThreadHandler;
        }
    }

    /**
     * 获取子线程handler
     */
    private Handler getChildThreadHandler() {
        synchronized (sLockChild) {
            if (childThreadHandler == null) {
                childThreadHandler = new Handler();
            }
            return childThreadHandler;
        }
    }

    /**
     * 当前线程是否是主线程--oldname：runningOnUiThread
     *
     * @return true iff the current thread is the main (UI) thread.
     */
    private Boolean isRunningOnUiThread() {
        return getUiThreadHandler().getLooper() == Looper.myLooper();
    }

    /**
     * 当前线程是否是子线程--oldname：runningOnUiThread
     *
     * @return true iff the current thread is the main (UI) thread.
     */
    private Boolean isRunningOnChildThread() {
        return getChildThreadHandler().getLooper() == Looper.myLooper();
    }

    /**
     * 发送到主线程运行
     *
     * @param task The Runnable to run
     */
    public void postOnUiThread(Runnable task) {
        getUiThreadHandler().post(task);
    }

    /**
     * 间隔指定时间后再主线程运行
     *
     * @param task        The Runnable to run
     * @param delayMillis The delay in milliseconds until the Runnable will be run
     */
    @VisibleForTesting
    public void postOnUiThreadDelayed(Runnable task, Long delayMillis) {
        getUiThreadHandler().postDelayed(task, delayMillis);
    }

    /**
     *
     * @param task The FutureTask to run
     * @return The queried task (to aid inline construction)
     */
    public <T> FutureTask<T> postOnUiThread(FutureTask<T> task) {
        getUiThreadHandler().post(task);
        return task;
    }

    /**
     * 如果当前是主线程则直接运行，否则发送到主线程去运行
     *
     * @param r The Runnable to run
     */
    public void runOnUiThread(Runnable r) {
        if (isRunningOnUiThread()) {
            r.run();
        } else {
            getUiThreadHandler().post(r);
        }
    }


    /**
     * 发送到子线程运行
     *
     * @param task The Runnable to run
     */
    public void postOnChildThread(Runnable task) {
        getChildThreadHandler().post(task);
    }

    /**
     * 间隔指定时间后再子线程运行
     *
     * @param task        The Runnable to run
     * @param delayMillis The delay in milliseconds until the Runnable will be run
     */
    @VisibleForTesting
    public void postOnChildThreadDelayed(Runnable task, Long delayMillis) {
        getChildThreadHandler().postDelayed(task, delayMillis);
    }

    /**
     * Post the supplied FutureTask to run on the child thread. The method will not block, even if
     * called on the UI thread.
     *
     * @param task The FutureTask to run
     * @return The queried task (to aid inline construction)
     */
    public <T> FutureTask<T> postOnChildThread(FutureTask<T> task) {
        getChildThreadHandler().post(task);
        return task;
    }

    /**
     * 如果当前是子线程则直接运行，否则发送到子线程去运行
     *
     * @param r The Runnable to run
     */
    public void runOnChildThread(Runnable r) {
        if (isRunningOnChildThread()) {
            r.run();
        } else {
            getChildThreadHandler().post(r);
        }
    }
}
