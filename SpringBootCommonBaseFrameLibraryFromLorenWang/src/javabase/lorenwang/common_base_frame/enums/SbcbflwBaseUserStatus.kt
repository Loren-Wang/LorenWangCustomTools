package javabase.lorenwang.common_base_frame.enums

/**
 * 功能作用：用户状态枚举变量
 * 创建时间：2019-10-31 下午 14:16:8
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
open class SbcbflwBaseUserStatus(var status: Int) {
    companion object {
        /**
         * 启用
         */
        val ENABLE = SbcbflwBaseUserStatus(0)

        /**
         * 禁用
         */
        val DISABLE = SbcbflwBaseUserStatus(1)

        /**
         * 删除
         */
        val DELETE = SbcbflwBaseUserStatus(2)
    }
}
