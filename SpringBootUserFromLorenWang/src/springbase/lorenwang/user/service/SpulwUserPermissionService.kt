package springbase.lorenwang.user.service

import springbase.lorenwang.base.bean.SpblwBaseDataDisposeStatusBean
import springbase.lorenwang.base.controller.SpblwBaseHttpServletRequestWrapper
import springbase.lorenwang.base.service.SpblwBaseService
import springbase.lorenwang.user.database.repository.SpulwUserPermissionRepository
import springbase.lorenwang.user.database.table.SpulwBaseUserInfoTb
import springbase.lorenwang.user.database.table.SpulwBaseUserPermissionTb
import springbase.lorenwang.user.database.table.SpulwBaseUserRoleTb

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
abstract class SpulwUserPermissionService<R : SpblwBaseHttpServletRequestWrapper, P : SpulwBaseUserPermissionTb<ROLE>, ROLE : SpulwBaseUserRoleTb<P>, U : SpulwBaseUserInfoTb<P, ROLE>, PT, PR : SpulwUserPermissionRepository<P, ROLE>> :
    SpblwBaseService {
    /*
    * 检测是否有权限
    */
    abstract fun checkUserHavePermission(request: R, userInfo: U, permission: PT): SpblwBaseDataDisposeStatusBean

    /**
     * 保存用户角色对应的权限
     */
    fun saveUserPermission(bean: P): P {
        val mutableSetOf = mutableSetOf<ROLE>()
        bean.permissionRole?.forEach {
            it.permission = mutableSetOf()
            mutableSetOf.add(it)
        }
        bean.permissionRole = mutableSetOf
        var repBean = getUserPermissionRepository().save(bean)
        if (repBean.permissionId.isEmpty()) {
            repBean.permissionId = repBean.permissionId
            repBean = getUserPermissionRepository().save(repBean)
        }
        return repBean
    }

    /**
     * 获取用户权限数据库操作实体
     */
    abstract fun getUserPermissionRepository(): PR
}
