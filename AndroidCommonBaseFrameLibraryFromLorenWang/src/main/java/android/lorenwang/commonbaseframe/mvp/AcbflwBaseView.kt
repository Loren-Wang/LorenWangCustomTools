package android.lorenwang.commonbaseframe.mvp

/**
 * 创建时间：2019-07-15 上午 11:09:20
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

interface AcbflwBaseView {
    /**
     * 显示加载中
     * @param allowLoadingBackFinishPage 是否允许后退结束当前页面
     */
    fun showBaseLoading(allowLoadingBackFinishPage: Boolean)

    /**
     * 隐藏加载中
     */
    fun hideBaseLoading()

    /**
     * 用户登陆状态异常
     */
    fun userLoginStatusError(code: Any?, message: String?)

    /**
     * 网络请求成功
     * @param data 响应数据
     * @param netOptionReqCode 网络操作请求code
     */
    fun <T> netReqSuccess(netOptionReqCode: Int, data: T)

    /**
     * 网络请求失败
     * @param netOptionReqCode 网络操作请求code
     * @param code 错误码
     * @param message 错误信息
     */
    fun netReqFail(netOptionReqCode: Int, code: Any?, message: String?)
}
