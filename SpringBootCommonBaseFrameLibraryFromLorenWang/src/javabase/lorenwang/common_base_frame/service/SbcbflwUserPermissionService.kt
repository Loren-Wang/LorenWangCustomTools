package javabase.lorenwang.common_base_frame.service

import javabase.lorenwang.common_base_frame.bean.SbcbflwBaseDataDisposeStatusBean
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseHttpServletRequestWrapper
import javabase.lorenwang.common_base_frame.database.repository.SbcbflwUserPermissionRepository
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserInfoTb
import javabase.lorenwang.common_base_frame.enums.SbcbflwBaseUserPermissionType
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserPermissionTb
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserRoleTb

/**
 * 功能作用：用户权限service
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
abstract class SbcbflwUserPermissionService : SbcbflwBaseService {
    /*
    * 检测是否有权限
    */
    abstract fun <R : SbcbflwBaseHttpServletRequestWrapper,
            P : SbcbflwBaseUserPermissionTb<ROLE>, ROLE : SbcbflwBaseUserRoleTb<P>,
            U : SbcbflwBaseUserInfoTb<P, ROLE>, PT : SbcbflwBaseUserPermissionType>
            checkUserHavePermission(request : R, userInfo : U, permission : PT) : SbcbflwBaseDataDisposeStatusBean

    /**
     * 保存用户角色对应的权限
     */
    fun <P : SbcbflwBaseUserPermissionTb<ROLE>, ROLE : SbcbflwBaseUserRoleTb<P>, PR : SbcbflwUserPermissionRepository<P, ROLE>> saveUserPermission(bean : P) : P {
        val mutableSetOf = mutableSetOf<ROLE>()
        bean.permissionRole?.forEach {
            it.permission = mutableSetOf()
            mutableSetOf.add(it)
        }
        bean.permissionRole = mutableSetOf
        var repBean = getUserPermissionRepository<P, ROLE, PR>().save(bean)
        if (repBean.permissionId.isNullOrEmpty() && repBean._ID != null) {
            repBean.permissionId = repBean._ID.toString()
            repBean = getUserPermissionRepository<P, ROLE, PR>().save(repBean)
        }
        return repBean
    }

    /**
     * 获取用户权限数据库操作实体
     */
    abstract fun <P : SbcbflwBaseUserPermissionTb<ROLE>, ROLE : SbcbflwBaseUserRoleTb<P>,
            PR : SbcbflwUserPermissionRepository<P, ROLE>> getUserPermissionRepository() : PR
}
