package javabase.lorenwang.common_base_frame.database.repository


import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserPermissionTb
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserRoleTb


/**
 * 功能作用：用户角色数据库操作
 * 初始注释时间： 2020/1/12 0012 下午 17:00:33
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
interface SbcbflwUserRoleRepository<P : SbcbflwBaseUserPermissionTb<R>, R : SbcbflwBaseUserRoleTb<P>> : SbcbflwBaseRepository<R, String> {
    /**
     * 获取角色信息
     */
    fun findByRoleType(type: Int): R?
}

