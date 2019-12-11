package android.lorenwang.common_base_frame.mvp

import android.lorenwang.common_base_frame.AcbflwBaseApplication
import android.lorenwang.common_base_frame.R
import android.lorenwang.tools.base.AtlwLogUtils
import android.lorenwang.tools.mobile.AtlwMobileSystemInfoUtils
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import android.lorenwang.common_base_frame.network.AcbflwNetOptionsCallback
import android.lorenwang.common_base_frame.network.bean.AcbflwBaseRepBean
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javabase.lorenwang.dataparse.JdplwJsonUtils
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

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
     */
    fun <T> getBaseObserver(pageIndex: Int?, pageCount: Int?, netOptionsCallback: AcbflwNetOptionsCallback<AcbflwBaseRepBean<T>>): Observer<Response<AcbflwBaseRepBean<T>>> {
        return object : Observer<Response<AcbflwBaseRepBean<T>>> {
            override fun onNext(t: Response<AcbflwBaseRepBean<T>>) {
                setPageInfo()
                if (t.code() == 200) {
                    val repCode = t.body().code
                    if (repCode == AcbflwNetRepCode.repCodeSuccess) {
                        //网络请求成功
                        netOptionsCallback.success(t.body())
                    } else {
                        AcbflwNetRepCode.repCodeLoginStatusError.forEach {
                            if (it == repCode) {
                                //用户登陆状态异常，需要跳转到登陆页面
                                netOptionsCallback.userLoginStatusError(it, t.body().message)
                                return
                            }
                        }
                        AtlwLogUtils.logE(TAG, t.code().toString())
                        netOptionsCallback.error(Exception("${repCode}-${t.body().message}"))
                    }
                } else {
                    AtlwLogUtils.logE(TAG, t.code().toString())
                    netOptionsCallback.error(Exception("${t.code()}-${t.message()}"))
                }
            }

            override fun onComplete() {
                setPageInfo()
                netOptionsCallback.onCompleteFinish()
            }

            override fun onSubscribe(d: Disposable) {
                setPageInfo()
                compositeDisposable.add(d)
                netOptionsCallback.onSubscribeStart()
            }

            override fun onError(e: Throwable) {
                when (e) {
                    is HttpException, is UnknownHostException -> {
                        //判断是无网络还是其他问题
                        try {
                            if (AtlwMobileSystemInfoUtils.getNetworkType(AcbflwBaseApplication.appContext) == 0) {
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
                            if (AtlwMobileSystemInfoUtils.getNetworkType(AcbflwBaseApplication.appContext) == 0) {
                                AtlwLogUtils.logE(TAG, AcbflwBaseApplication.appContext?.getString(R.string.net_error_net))
                            } else {
                                AtlwLogUtils.logE(TAG, AcbflwBaseApplication.appContext?.getString(R.string.net_error_timeout))
                            }
                        } catch (e: Exception) {
                            AtlwLogUtils.logE(TAG, AcbflwBaseApplication.appContext?.getString(R.string.net_error_timeout))
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

    /**
     * 获取请求数据
     * @param keyArray 请求参数key数组
     * @param dataArray 请求数据数组
     * @return 返回格式化后字符串
     */
    fun getDataStr(keyArray: Array<String>?, dataArray: Array<Any?>?): String? {
        return getDataStr(null, null, null, keyArray, dataArray)
    }

    /**
     * 获取请求数据
     * @param currentPage 当前页码
     * @param limit
     * @param total 总数
     * @param keyArray 请求参数key数组
     * @param dataArray 请求数据数组
     * @return 返回格式化后字符串
     */
    fun getDataStr(currentPage: Int?, limit: Int?, total: Int?, keyArray: Array<String>?, dataArray: Array<Any?>?): String? {
        //map存储请求数据
        val map = hashMapOf<String, Any?>()
        currentPage?.let {
            map.put("currentPage", it)
        }
        limit?.let {
            map.put("limit", it)
        }
        total?.let {
            map.put("total", it)
        }
        //存储传递的数据列表数据，数据参数数量不同则不进行数据的添加
        if (!(keyArray.isNullOrEmpty() || dataArray.isNullOrEmpty()) && keyArray.size == dataArray.size) {
            keyArray.forEachIndexed { index: Int, key: String ->
                map[key] = dataArray[index]
            }
        }
        return JdplwJsonUtils.toJson(map)
    }
}
