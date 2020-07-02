package javabase.lorenwang.common_base_frame.enums

/**
 * 功能作用：用户权限类型枚举
 * 创建时间：2020-01-06 17:21
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
open class SbcbflwBaseUserPermissionType {
    companion object {
        /**
         * 超级管理员。该权限可以做任何操作，但是无法删除禁用同级的人，基本上是唯一的
         */
        val SUPER_ADMIN = -1

        /**
         * 全部权限，该权限可以做任何操作，但是无法删除禁用同级的人
         */
        val ALL = 0
    }
}
