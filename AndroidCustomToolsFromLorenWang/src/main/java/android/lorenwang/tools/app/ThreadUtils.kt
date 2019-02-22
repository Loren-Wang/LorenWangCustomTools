package android.lorenwang.tools.app

import android.os.Handler
import android.os.Looper
import android.support.annotation.VisibleForTesting
import java.util.concurrent.FutureTask

/**
 * 创建时间：2019-02-22 下午 22:24:10
 * 创建人：王亮（Loren wang）
 * 功能作用：线程工具类
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class ThreadUtils {
    private val TAG = "ThreadUtils"
    /**
     * ui线程handler
     */
    private lateinit var sUiThreadHandler: Handler
    /**
     * 子线程handler
     */
    private lateinit var childThreadHandler: Handler
    private var sWillOverride: Boolean = false
    companion object {
        private var threadUtils: ThreadUtils
        /**
         * 同步锁使用
         */
        private val sLockUI:Any = Any()
        /**
         * 同步锁使用
         */
        private val sLockChild:Any = Any()

        init {
            threadUtils = ThreadUtils()
        }

        val instances: ThreadUtils
            get() {
                return threadUtils
            }
    }

    /**
     * 获取UI线程handler
     */
    private fun getUiThreadHandler(): Handler {
        synchronized(sLockUI) {
            if (sUiThreadHandler == null) {
                if (sWillOverride) {
                    throw RuntimeException("Did not yet override the UI thread")
                }
                sUiThreadHandler = Handler(Looper.getMainLooper())
            }
            return sUiThreadHandler
        }
    }

    /**
     * 获取子线程handler
     */
    private fun getChildThreadHandler():Handler{
        synchronized(sLockChild) {
            if (sUiThreadHandler == null) {
                if (sWillOverride) {
                    throw RuntimeException("Did not yet override the UI thread")
                }
                sUiThreadHandler = Handler(Looper.getMainLooper())
            }
            return sUiThreadHandler
        }
    }

    /**
     * 当前线程是否是主线程--oldname：runningOnUiThread
     * @return true iff the current thread is the main (UI) thread.
     */
    private fun isRunningOnUiThread(): Boolean {
        return getUiThreadHandler().looper == Looper.myLooper()
    }
    /**
     * 当前线程是否是子线程--oldname：runningOnUiThread
     * @return true iff the current thread is the main (UI) thread.
     */
    private fun isRunningOnChildThread(): Boolean {
        return getChildThreadHandler().looper == Looper.myLooper()
    }

    /**
     * 发送到主线程运行
     * Post the supplied Runnable to run on the main thread. The method will not block, even if
     * called on the UI thread.
     *
     * @param task The Runnable to run
     */
    fun postOnUiThread(task: Runnable) {
        getUiThreadHandler().post(task)
    }

    /**
     * 间隔指定时间后再主线程运行
     * Post the supplied Runnable to run on the main thread after the given amount of time. The
     * method will not block, even if called on the UI thread.
     *
     * @param task The Runnable to run
     * @param delayMillis The delay in milliseconds until the Runnable will be run
     */
    @VisibleForTesting
    fun postOnUiThreadDelayed(task: Runnable, delayMillis: Long) {
        getUiThreadHandler().postDelayed(task, delayMillis)
    }

    /**
     * Post the supplied FutureTask to run on the main thread. The method will not block, even if
     * called on the UI thread.
     *
     * @param task The FutureTask to run
     * @return The queried task (to aid inline construction)
     */
    fun <T> postOnUiThread(task: FutureTask<T>): FutureTask<T> {
        getUiThreadHandler().post(task)
        return task
    }

    /**
     * 如果当前是主线程则直接运行，否则发送到主线程去运行
     * Run the supplied Runnable on the main thread. The method will block only if the current
     * thread is the main thread.
     *
     * @param r The Runnable to run
     */
    fun runOnUiThread(r: Runnable) {
        if (isRunningOnUiThread()) {
            r.run()
        } else {
            getUiThreadHandler().post(r)
        }
    }


    /**
     * 发送到子线程运行
     * Post the supplied Runnable to run on the child thread. The method will not block, even if
     * called on the UI thread.
     *
     * @param task The Runnable to run
     */
    fun postOnChildThread(task: Runnable) {
        getChildThreadHandler().post(task)
    }

    /**
     * 间隔指定时间后再子线程运行
     * Post the supplied Runnable to run on the child thread after the given amount of time. The
     * method will not block, even if called on the UI thread.
     *
     * @param task The Runnable to run
     * @param delayMillis The delay in milliseconds until the Runnable will be run
     */
    @VisibleForTesting
    fun postOnChildThreadDelayed(task: Runnable, delayMillis: Long) {
        getChildThreadHandler().postDelayed(task, delayMillis)
    }

    /**
     * Post the supplied FutureTask to run on the child thread. The method will not block, even if
     * called on the UI thread.
     *
     * @param task The FutureTask to run
     * @return The queried task (to aid inline construction)
     */
    fun <T> postOnChildThread(task: FutureTask<T>): FutureTask<T> {
        getChildThreadHandler().post(task)
        return task
    }

    /**
     * 如果当前是子线程则直接运行，否则发送到子线程去运行
     * Run the supplied Runnable on the child thread. The method will block only if the current
     * thread is the child thread.
     *
     * @param r The Runnable to run
     */
    fun runOnChildThread(r: Runnable) {
        if (isRunningOnUiThread()) {
            r.run()
        } else {
            getChildThreadHandler().post(r)
        }
    }

}
