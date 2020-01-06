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
enum class SbcbflwBaseUserStatusEnum constructor(var status: Int) {
    /**
     * 启用
     */
    ENABLE(0),
    /**
     * 禁用
     */
    DISABLE(1),
    /**
     * 删除
     */
    DELETE(2)

}
