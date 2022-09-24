package android.lorenwang.commonbaseframe.network

import android.lorenwang.commonbaseframe.network.callback.AcbflwNetCallback
import retrofit2.Call

/**
 * 功能作用：App中对于属于自己的网络数据的处理
 * 创建者：王亮（Loren）
 * 备注：
 */
interface AppModuleNetDataDispose {
    /**
     * 请求准备发起
     * @param call 当前请求实例
     * @param retrofitKey 当前请求使用的网关版本
     */
    fun onBefore(call: Call<*>?, retrofitKey: String?)

    /**
     * 请求结束
     * @param call 当前请求实例
     * @param retrofitKey 当前请求使用的网关版本
     * @param showLoading 是否要显示加载中
     */
    fun onAfter(call: Call<*>?, retrofitKey: String?, showLoading: Boolean)

    /**
     * 请求开始显示加载中
     * @param call 当前请求实例
     * @param retrofitKey 当前请求使用的网关版本
     * @param showLoading 是否要显示加载中
     */
    fun onLoading(call: Call<*>?, retrofitKey: String?, showLoading: Boolean)

    /**
     * 请求异常
     * @param call 当前请求实例
     * @param retrofitKey 当前请求使用的网关版本
     * @param code 各自服务器返回的异常code
     * @param message 各自的服务器返回的异常消息内容
     */
    fun onError(call: Call<*>?, retrofitKey: String?, code: String, message: String?)

    /**
     * 网关处理成功，准备丢给配置文件中各个项目实现的各自服务器基础相关处理
     * @param call 当前请求实例
     * @param retrofitKey 当前请求使用的网关版本
     * @param request 当前请求数据实体
     * @param result 返回的内容字符串
     * @param callback 请求返回使用的回调
     */
    fun <T> onGatewaySuccess(call: Call<*>?, retrofitKey: String?, request: AcbflwRequestBean, result: String, callback: AcbflwNetCallback<T>)

    /**
     * 网关响应异常处理，如果处理了返回true，没有处理返回false，默认会有些简单处理
     * @param call 当前请求实例
     * @param retrofitKey 当前请求使用的网关版本
     * @param requestBean 请求实体
     * @param code 网关返回的异常code
     * @param message 异常内容
     */
    fun gatewayResponseError(call: Call<Any>?, retrofitKey: String?, requestBean: AcbflwRequestBean, code: String?, message: String?): Boolean
}