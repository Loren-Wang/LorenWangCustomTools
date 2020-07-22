package android.lorenwang.commonbaseframe.mvp

import android.lorenwang.commonbaseframe.AcbflwBaseApplication
import android.lorenwang.commonbaseframe.R
import android.lorenwang.commonbaseframe.network.AcbflwBaseRetrofitObserver
import android.lorenwang.commonbaseframe.network.callback.AcbflwNetOptionsByModelCallback
import android.lorenwang.tools.base.AtlwLogUtils
import android.lorenwang.tools.mobile.AtlwMobileSystemInfoUtils
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javabase.lorenwang.dataparse.JdplwJsonUtils
import kotlinbase.lorenwang.tools.common.bean.KttlwBaseNetResponseBean
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * 创建时间：2019-07-15 上午 11:11:5
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

open class AcbflwBaseModel {
    protected val TAG = javaClass.name
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
        AtlwLogUtils.logI(TAG, "释放了当前model所有网络请求！")
    }

    /**
     * 获取响应处理Observer
     * @param pageIndex 页码
     * @param pageCount 页面数量
     * @param netOptionsCallback 请求结果回调
     */
    open fun <D, T : KttlwBaseNetResponseBean<D>> getBaseObserver(
            pageIndex: Int?, pageCount: Int?, netOptionsCallback: AcbflwNetOptionsByModelCallback<D, T>): AcbflwBaseRetrofitObserver<D, T> {
        return object : AcbflwBaseRetrofitObserver<D, T> {
            override fun onComplete() {
                setPageInfo()
                netOptionsCallback.onCompleteFinish()
            }

            override fun onSubscribe(d: Disposable) {
                setPageInfo()
                compositeDisposable.add(d)
                netOptionsCallback.onSubscribeStart()
            }

            override fun onNext(t: Response<T>) {
                setPageInfo()
                if (t.code() == 200) {
                    val repCode = t.body().stateCode
                    if (repCode == AcbflwNetRepCode.repCodeSuccess) {
                        //网络请求成功
                        netOptionsCallback.success(t.body())
                    } else {
                        AcbflwNetRepCode.repCodeLoginStatusError.forEach {
                            if (it == repCode) {
                                //用户登陆状态异常，需要跳转到登陆页面
                                netOptionsCallback.userLoginStatusError(it, t.body().stateMessage)
                                return
                            }
                        }
                        AtlwLogUtils.logE(TAG, t.code().toString())
                        netOptionsCallback.error(Exception("${repCode}-${t.body().stateMessage}"))
                    }
                } else {
                    AtlwLogUtils.logE(TAG, t.code().toString())
                    netOptionsCallback.error(Exception("${t.code()}-${t.message()}"))
                }
            }

            override fun onError(e: Throwable) {
                when (e) {
                    is HttpException, is UnknownHostException -> {
                        //判断是无网络还是其他问题
                        try {
                            if (AtlwMobileSystemInfoUtils.getNetworkType() == 0) {
                                AtlwLogUtils.logE(TAG, AcbflwBaseApplication.appContext?.getString(R.string.net_error_net))
                            } else {
                                AtlwLogUtils.logE(TAG, AcbflwBaseApplication.appContext?.getString(R.string.net_error_server))
                            }
                        } catch (e: Exception) {
                            AtlwLogUtils.logE(TAG, AcbflwBaseApplication.appContext?.getString(R.string.net_error_net))
                        }
                    }
                    is SocketTimeoutException -> {
                        //判断是无网络还是其他问题
                        try {
                            if (AtlwMobileSystemInfoUtils.getNetworkType() == 0) {
                                AtlwLogUtils.logE(TAG, AcbflwBaseApplication.appContext?.getString(R.string.net_error_net))
                            } else {
                                AtlwLogUtils.logE(TAG, AcbflwBaseApplication.appContext?.getString(R.string.net_error_timeout))
                            }
                        } catch (e: Exception) {
                            AtlwLogUtils.logE(TAG, AcbflwBaseApplication.appContext?.getString(R.string.net_error_timeout))
                        }
                    }
                    is SSLException -> {
                        //判断是无网络还是其他问题
                        try {
                            if (AtlwMobileSystemInfoUtils.getNetworkType() == 0) {
                                AtlwLogUtils.logE(TAG, AcbflwBaseApplication.appContext?.getString(R.string.net_error_net))
                            } else {
                                AtlwLogUtils.logE(TAG, AcbflwBaseApplication.appContext?.getString(R.string.net_error_sll))
                            }
                        } catch (e: Exception) {
                            AtlwLogUtils.logE(TAG, AcbflwBaseApplication.appContext?.getString(R.string.net_error_sll))
                        }
                    }
                    else -> {

                    }
                }
                setPageInfo()
                netOptionsCallback.error(e)
            }

            /**
             * 设置分页信息
             */
            fun setPageInfo() {
                netOptionsCallback.pageIndex = pageIndex
                netOptionsCallback.pageCount = pageCount
            }
        }
    }
}
