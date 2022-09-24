package android.lorenwang.commonbaseframe.network.callback

import android.lorenwang.commonbaseframe.AcbflwBaseConfig
import android.lorenwang.commonbaseframe.network.AcbflwNetManager.Companion.clearEndZeroAndParamsForDouble
import android.lorenwang.commonbaseframe.network.AcbflwRequestBean
import android.lorenwang.commonbaseframe.network.HTTP_ERROR_SSL
import android.lorenwang.commonbaseframe.network.HTTP_ERROR_SYSTEM
import android.lorenwang.commonbaseframe.network.HTTP_ERROR_TIMEOUT
import android.lorenwang.tools.base.AtlwLogUtil
import android.os.Handler
import android.os.Looper
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import javabase.lorenwang.dataparse.JdplwJsonUtil
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLException

/**
 * 功能作用：网关接口响应处理
 * 创建时间：2022/7/22 19:39
 * 创建者：王亮（Loren）
 * 备注：
 */
abstract class AcbflwNetCallback<T> {
    protected val mHandler: Handler by lazy { Handler(Looper.getMainLooper()) }

    /**
     * AppModule分开的数据处理
     */
    private val mAppModuleNetDataDispose = AcbflwBaseConfig.mAppModuleNetDataDispose

    /**
     * 发起的请求
     */
    var mObservable: Call<Any>? = null

    /**
     * 网关版本
     */
    var retrofitKey: String? = null

    /**
     * 取消请求
     */
    fun cancelRequest() {
        if (mObservable != null && !mObservable!!.isCanceled) {
            mObservable!!.cancel()
        }
    }

    /**
     * 请求准备发起
     */
    open fun onBefore() {
        mHandler.post { mAppModuleNetDataDispose?.onBefore(mObservable, retrofitKey) }
    }

    /**
     * 请求结束
     */
    open fun onAfter(showLoading: Boolean) {
        mHandler.post { mAppModuleNetDataDispose?.onAfter(mObservable, retrofitKey, showLoading) }
    }

    /**
     * 请求开始显示加载中
     */
    open fun onLoading(showLoading: Boolean) {
        mHandler.post { mAppModuleNetDataDispose?.onLoading(mObservable, retrofitKey, showLoading) }
    }

    /**
     * 请求异常
     */
    open fun onError(code: String, message: String?) {
        mHandler.post { mAppModuleNetDataDispose?.onError(mObservable, retrofitKey, code, message) }
    }

    /**
     * 成功数据处理，当自己的服务器返回基础解析成功之后调用该方法将数据转换成需要的格式，也可以直接使用onSuccess()返回,或者特殊的回调继承该类并重写该方法处理数据
     * @param request 请求参数集合
     * @param result 返回数据的字符串
     * @param reqClass 解析class
     * @param callback 回调调用实例
     */
    open fun <B> successDataDispose(request: AcbflwRequestBean, result: String, reqClass: Class<T>, callback: AcbflwNetCallback<B>) {
        mHandler.post { onSuccess(JdplwJsonUtil.fromJson(result, reqClass), result) }
    }

    /**
     * 请求成功
     * @param data 解析后实体，可能为空
     * @param result 接口返回的请求字符串
     */
    abstract fun onSuccess(data: T?, result: String?)

    /**
     * 网关处理成功，准备丢给配置文件中各个项目实现的各自服务器基础相关处理
     */
    fun onGatewaySuccess(request: AcbflwRequestBean, result: String) {
        mAppModuleNetDataDispose?.onGatewaySuccess(mObservable, retrofitKey, request, result, callback = this)
    }

    /**
     * 网关响应异常处理
     */
    fun gatewayResponseError(requestBean: AcbflwRequestBean, code: String?, message: String?): Boolean {
        return false == mAppModuleNetDataDispose?.gatewayResponseError(mObservable, retrofitKey, requestBean, code, message)
    }

    /**
     * 网络接口回调
     */
    internal fun onResponse(requestBean: AcbflwRequestBean, response: Response<Any>, showLoading: Boolean) {
        if (response.isSuccessful) {
            val responseData = JdplwJsonUtil.toJson(response.body())
            if (responseData.isEmpty()) {
                AtlwLogUtil.logUtils.logE(javaClass, "响应数据体body为空")
                onError(response.code().toString(), "body is null")
            } else {
                onGatewaySuccess(requestBean, responseData)
            }
        } else {
            val body = response.body() ?: response.errorBody()?.string()
            if (body != null) {
                try {
                    val content = JSONObject(body as String)
                    val code = clearEndZeroAndParamsForDouble(content.optString("code", content.optString("errorcode", "")))
                    if (code.isNullOrEmpty()) {
                        gatewayResponseError(requestBean, response.code().toString(), "Unexpected code $response")
                    } else {
                        gatewayResponseError(requestBean, code, content.optString("msg", ""))
                    }
                } catch (ignore: Exception) {
                    gatewayResponseError(requestBean, response.code().toString(), "Unexpected code $response")
                }
            } else {
                gatewayResponseError(requestBean, response.code().toString(), "Unexpected code $response")
            }
        }
        onAfter(showLoading)
    }

    /**
     * 网络响应异常
     */
    internal fun onResponseError(requestBean: AcbflwRequestBean, e: Throwable, showLoading: Boolean) {
        when (e) {
            is HttpException, is UnknownHostException -> {
                onError(HTTP_ERROR_SYSTEM, e.message)
            }
            is SocketTimeoutException, is TimeoutException -> {
                onError(HTTP_ERROR_TIMEOUT, e.message)
            }
            is SSLException -> {
                onError(HTTP_ERROR_SSL, e.message)
            }
            else -> {

            }
        }
        onAfter(showLoading)
    }
}