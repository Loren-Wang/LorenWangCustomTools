package android.lorenwang.common_base_frame.network.callback

/**
 * 功能作用：响应数据操作回调,主要针对于在presenter基类处理后的回调，下一步就是扩展处理数据后给到view层
 * 创建时间：2019-12-11 10:14
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：该类放到presenter中，因为mvp中的p层负责m数据和v的中间连接，而该回调是用来对m数据层从接口获取到的数据之后的
 * 一些处理，例如当activity被结束后不调用该方法给view层展示数据等
 */
interface AcbflwRepOptionsByPresenterCallback<T> {
    /**
     * 返回view操作数据
     */
    fun viewOptionsData(data: T)

    /**
     * 响应数据异常
     * @param code 错误码
     */
    fun repDataError(code: Any?, message: String?)
}
