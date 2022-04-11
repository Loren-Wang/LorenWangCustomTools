package springbase.lorenwang.user.service

import springbase.lorenwang.base.service.SpblwBaseService
import springbase.lorenwang.user.database.table.SpulwRolePermissionTb

/**
 * 功能作用：角色权限service
 * 初始注释时间： 2022/4/10 17:50
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
interface SpulwRolePermissionService : SpblwBaseService {

    /**
     * 获取角色列表
     */
    fun getRoles(permissionType: Int): List<SpulwRolePermissionTb>

    /**
     * 获取权限列表
     */
    fun getPermissions(roleType: Int): List<SpulwRolePermissionTb>

    /**
     * 保存角色以及权限对应关系
     * @param roleType 角色类型
     * @param permissionType 权限类型
     */
    fun saveRoleAndPermission(roleType: Int, permissionType: Int)
}