package com.test.springboot.enums

/**
 * 功能作用：用户权限
 * 创建时间：2020-12-11 11:49 上午
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
enum class UserPermissionTypeEnum(val type : Int, val des : String?) {
    /**
     * 超级管理员。该权限可以做任何操作，但是无法删除禁用同级的人，基本上是唯一的
     */
    SUPER_ADMIN(-1, "超级管理员权限"),

    /**
     * 管理员权限，该权限可以做任何操作，但是无法删除禁用同级的人
     */
    ADMIN(-2, "管理员权限")
}
