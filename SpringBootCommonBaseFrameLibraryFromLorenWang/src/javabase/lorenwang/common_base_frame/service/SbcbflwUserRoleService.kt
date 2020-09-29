package javabase.lorenwang.common_base_frame.service

import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseHttpServletRequestWrapper
import javabase.lorenwang.common_base_frame.database.repository.SbcbflwUserRoleRepository
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserInfoTb
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserPermissionTb
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserRoleTb

/**
 * 功能作用：用户角色service
 * 创建时间：2020-07-01 5:55 下午
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren wang）
 */
abstract class SbcbflwUserRoleService<R : SbcbflwBaseHttpServletRequestWrapper,
        P : SbcbflwBaseUserPermissionTb<ROLE>, ROLE : SbcbflwBaseUserRoleTb<P>,
        U : SbcbflwBaseUserInfoTb<P, ROLE>,
        PR : SbcbflwUserRoleRepository<P, ROLE>> : SbcbflwBaseService {

    /**
     * 保存用户角色
     */
    fun saveUserRole(bean: ROLE): ROLE {
        val mutableSetOf = mutableSetOf<P>()
        bean.permission?.forEach {
            it.permissionRole = mutableSetOf()
            mutableSetOf.add(it)
        }
        bean.permission = mutableSetOf
        var repBean = getUserRoleRepository().save(bean)
        if (repBean.roleId.isNullOrEmpty() && repBean._ID != null) {
            repBean.roleId = repBean._ID.toString()
            repBean = getUserRoleRepository().save(repBean)
        }
        return repBean
    }

    /**
     * 获取用户权限数据库操作实体
     */
    abstract fun getUserRoleRepository(): PR
}
