package android.lorenwang.common_base_frame.network

/**
 * 创建时间：2019-08-05 17:23
 * 创建人：王亮（Loren wang）
 * 功能作用：网络接口操作回调
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：该类主要放到model层中，主要是对接口返回的数据的处理，例如转换成本地使用的数据结构类型等
 */
abstract class AcbflwNetOptionsCallback<T> {
    /**
     * 页码
     */
    var pageIndex: Int? = null
    /**
     * 每页请求数量
     */
    var pageCount: Int? = null

    /**
     * 请求成功
     */
    abstract fun success(dto: T?)

    /**
     * 请求发生异常
     */
    abstract fun error(e: Throwable)

    /**
     * 请求结束
     */
    abstract fun onCompleteFinish()

    /**
     * 请求开始
     */
    abstract fun onSubscribeStart()

    /**
     * 用户登陆状态异常
     */
    abstract fun userLoginStatusError( code: Any?,message: String?)
}
