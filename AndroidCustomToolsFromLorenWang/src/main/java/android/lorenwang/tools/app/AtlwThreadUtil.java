package android.lorenwang.tools.app;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.util.concurrent.FutureTask;

/**
 * 功能作用：安卓线程处理单例
 * 初始注释时间： 2021/9/17 10:53
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 获取UI线程handler--getUiThreadHandler()
 * 获取子线程handler--getChildThreadHandler()
 * 当前线程是否是主线程--isRunningOnUiThread()
 * 当前线程是否是子线程--isRunningOnChildThread()
 * 发送到主线程运行--postOnUiThread(task)
 * 间隔指定时间后再主线程运行--postOnUiThreadDelayed(task,delayMillis)
 * 发送到主线程运行--postOnUiThread(task)
 * 如果当前是主线程则直接运行，否则发送到主线程去运行--runOnUiThread(r)
 * 发送到子线程运行--postOnChildThread(task)
 * 间隔指定时间后再子线程运行--postOnChildThreadDelayed(task,delayMillis)
 * 发送到子线程运行--postOnChildThread(task)
 * 如果当前是子线程则直接运行，否则发送到子线程去运行--runOnChildThread(r)
 * 在主、子线程中移除指定的runnable--removeRunnable(runnable)
 * 在主、子线程中移除指定的runnable--removeRunnableForChild(runnable)
 * 在主、子线程中移除指定的runnable--removeRunnableForUi(runnable)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class AtlwThreadUtil {
    private static volatile AtlwThreadUtil atlwThreadUtils;
    /**
     * ui线程handler
     */
    private Handler sUiThreadHandler;
    /**
     * 子线程handler
     */
    private Handler childThreadHandler;
    private final Boolean sWillOverride = false;
    /**
     * 同步锁使用
     */
    private final Object sLockUI = new Object();
    /**
     * 同步锁使用
     */
    private final Object sLockChild = new Object();

    private AtlwThreadUtil() {
        //初始化主线程
        sUiThreadHandler = new Handler(Looper.getMainLooper());
        //初始化子线程
        HandlerThread handlerThread = new HandlerThread("child_thread");
        handlerThread.start();
        childThreadHandler = new Handler(handlerThread.getLooper());
    }

    public static AtlwThreadUtil getInstance() {
        synchronized (AtlwThreadUtil.class) {
            if (atlwThreadUtils == null) {
                atlwThreadUtils = new AtlwThreadUtil();
            }
        }
        return atlwThreadUtils;
    }

    /**
     * 获取UI线程handler
     *
     * @return 返回主线程handler
     */
    public Handler getUiThreadHandler() {
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
     *
     * @return 返回子线程handler
     */
    public Handler getChildThreadHandler() {
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
    private boolean isRunningOnUiThread() {
        return getUiThreadHandler().getLooper() == Looper.myLooper();
    }

    /**
     * 当前线程是否是子线程--oldname：runningOnUiThread
     *
     * @return true iff the current thread is the main (UI) thread.
     */
    private boolean isRunningOnChildThread() {
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
    public void postOnUiThreadDelayed(Runnable task, Long delayMillis) {
        getUiThreadHandler().postDelayed(task, delayMillis);
    }

    /**
     * 发送到主线程运行
     * @param <T>  泛型
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
    public void postOnChildThreadDelayed(Runnable task, Long delayMillis) {
        getChildThreadHandler().postDelayed(task, delayMillis);
    }

    /**
     * 发送到子线程运行
     * Post the supplied FutureTask to run on the child thread. The method will not block, even if
     * called on the UI thread.
     *
     * @param <T>  泛型
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

    /**
     * 在主、子线程中移除指定的runnable
     *
     * @param runnable 要移除的runnable
     */
    public void removeRunnable(Runnable runnable) {
        if (runnable != null) {
            getChildThreadHandler().removeCallbacks(runnable);
            getUiThreadHandler().removeCallbacks(runnable);
        }
    }

    /**
     * 在主、子线程中移除指定的runnable
     *
     * @param runnable 要移除的runnable
     */
    public void removeRunnableForChild(Runnable runnable) {
        if (runnable != null) {
            getChildThreadHandler().removeCallbacks(runnable);
        }
    }

    /**
     * 在主、子线程中移除指定的runnable
     *
     * @param runnable 要移除的runnable
     */
    public void removeRunnableForUi(Runnable runnable) {
        if (runnable != null) {
            getUiThreadHandler().removeCallbacks(runnable);
        }
    }
}
