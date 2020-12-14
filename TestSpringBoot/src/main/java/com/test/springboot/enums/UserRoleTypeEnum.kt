package com.test.springboot.enums

/**
 * 功能作用：用户角色枚举
 * 创建时间：2020-12-11 11:42 上午
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
enum class UserRoleTypeEnum(val type : Int, val des : String) {
    /**
     * 超级管理员
     */
    SUPER_ADMIN(-1, "超级管理员")
}
