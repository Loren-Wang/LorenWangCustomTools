package android.lorenwang.commonbaseframe.bean

/**
 * 功能作用：接口数据基础响应体
 * 初始注释时间： 2021/10/14 17:20
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
abstract class AcbflwBaseRepBean<T> {
    /**
     * 请求是否成功
     */
    abstract fun checkResponseSuccess(): Boolean

    /**
     * 用户登录状态是否异常
     */
    abstract fun checkUserLoginStatusError(): Boolean

    /**
     * 获取异常code
     */
    fun getErrorCode(): String {
        return ""
    }

    /**
     * 获取异常消息文本
     */
    fun getErrorMsg(): String {
        return ""
    }

    /**
     * 获取使用的数据
     */
    abstract fun getUseData():T?
}
