package android.lorenwang.common_base_frame.network.bean

/**
 * 功能作用：基础响应实体
 * 创建时间：2019-12-10 17:15
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class AcbflwBaseRepBean<T> {
    var code: Any? = null
    var message: String? = null
    var data: T? = null
}
