package springbase.lorenwang.user.service

import springbase.lorenwang.base.controller.SpblwBaseHttpServletRequestWrapper
import springbase.lorenwang.base.service.SpblwBaseService
import springbase.lorenwang.user.database.repository.SpulwUserRoleRepository
import springbase.lorenwang.user.database.table.SpulwBaseUserInfoTb
import springbase.lorenwang.user.database.table.SpulwBaseUserPermissionTb
import springbase.lorenwang.user.database.table.SpulwBaseUserRoleTb

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
abstract class SpulwUserRoleService<R : SpblwBaseHttpServletRequestWrapper, P : SpulwBaseUserPermissionTb<ROLE>, ROLE : SpulwBaseUserRoleTb<P>, U : SpulwBaseUserInfoTb<P, ROLE>, PR : SpulwUserRoleRepository<P, ROLE>> :
    SpblwBaseService {

    /**
     * 保存用户角色
     */
    fun saveUserRole(bean: ROLE): ROLE {
        val mutableSetOf = mutableSetOf<P>()
        bean.permission?.forEach {
            it.permissionRole = mutableSetOf()
            mutableSetOf.add(it)
        }
        bean.permission = mutableSetOf()
        var repBean = getUserRoleRepository().save(bean)
        if (repBean.roleId.isEmpty()) {
            repBean.roleId = repBean.roleId
            repBean = getUserRoleRepository().save(repBean)
        }
        return repBean
    }

    /**
     * 获取用户权限数据库操作实体
     */
    abstract fun getUserRoleRepository(): PR
}
