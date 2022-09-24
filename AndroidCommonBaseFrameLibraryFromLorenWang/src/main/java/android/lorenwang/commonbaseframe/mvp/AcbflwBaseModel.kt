package android.lorenwang.commonbaseframe.mvp

import android.app.Activity
import android.lorenwang.commonbaseframe.AcbflwBaseApplication
import android.lorenwang.commonbaseframe.network.AcbflwNetManager
import android.lorenwang.commonbaseframe.network.AcbflwRequestBean
import android.lorenwang.commonbaseframe.network.callback.AcbflwFileDownLoadCallback
import android.lorenwang.commonbaseframe.network.callback.AcbflwNetCallback
import android.lorenwang.commonbaseframe.network.file.AcbflwFileDownLoadBean
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javabase.lorenwang.tools.JtlwLogUtil
import retrofit2.Response


/**
 * 功能作用：基础model
 * 初始注释时间： 2021/1/17 8:55 下午
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
open class AcbflwBaseModel {

    /**
     *
     * 请求的记录器
     */
    private var compositeDisposable = CompositeDisposable()

    /**
     * 释放
     */
    fun releaseModel() {
        compositeDisposable.clear()
        JtlwLogUtil.logUtils.logI(javaClass, "释放了当前model所有网络请求！")
    }

    /**
     * 获取响应处理Observer
     * @param activity 页面实例
     * @param netOptionsCallback 请求结果回调
     * @param requestBean 请求提
     * @param showLoading 是否显示加载中
     */
    open fun <T> getBaseObserver(activity: Activity?, requestBean: AcbflwRequestBean, netOptionsCallback: AcbflwNetCallback<T>,
        showLoading: Boolean): Observer<Response<Any>> {
        //添加model实例
        AcbflwBaseApplication.application?.addModel(activity, this)
        if (showLoading) {
            netOptionsCallback.onLoading(showLoading)
        }
        return object : Observer<Response<Any>> {
            override fun onComplete() {
                netOptionsCallback.onAfter(showLoading)
            }

            override fun onSubscribe(d: Disposable) {
                compositeDisposable.add(d)
                netOptionsCallback.onBefore()
            }

            override fun onNext(t: Response<Any>) {
                netOptionsCallback.onResponse(requestBean, t, showLoading)
            }

            override fun onError(e: Throwable) {
                netOptionsCallback.onResponseError(requestBean, e, showLoading)
            }
        }
    }

    /**
     * 文件下载
     */
    fun downloadFile(appCompileType: Int,retrofitKey: String, bean: AcbflwFileDownLoadBean, callback: AcbflwFileDownLoadCallback) {
        AcbflwNetManager.downloadFile(appCompileType,retrofitKey, bean, callback)
    }
}
