package springbase.lorenwang.user.service.impl

import org.springframework.beans.factory.annotation.Autowired
import springbase.lorenwang.base.database.SpblwBaseTableConfig
import springbase.lorenwang.user.database.SpulwBaseTableConfig
import springbase.lorenwang.user.database.repository.SpulwRolePermissionRepository
import springbase.lorenwang.user.database.table.SpulwRolePermissionTb
import springbase.lorenwang.user.service.SpulwRolePermissionService

/**
 * 功能作用：角色权限服务
 * 初始注释时间： 2022/4/10 17:52
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

open class SpulwRolePermissionServiceImpl : SpulwRolePermissionService {
    @Autowired
    protected lateinit var repository: SpulwRolePermissionRepository

    /**
     * 获取角色列表
     */
    override fun getRoles(permissionType: Int): List<SpulwRolePermissionTb> {
        return repository.findAll { root, _, criteriaBuilder ->
            return@findAll criteriaBuilder.and(criteriaBuilder.equal(root.get<Int>(SpblwBaseTableConfig.CommonColumn.TYPE), permissionType))
        }
    }

    /**
     * 获取权限列表
     */
    override fun getPermissions(roleType: Int): List<SpulwRolePermissionTb> {
        return repository.findAll { root, _, criteriaBuilder ->
            return@findAll criteriaBuilder.and(criteriaBuilder.equal(root.get<Int>(SpblwBaseTableConfig.CommonColumn.TYPE), roleType))
        }
    }

    /**
     * 保存角色以及权限对应关系
     * @param roleType 角色类型
     * @param permissionType 权限类型
     */
    override fun saveRoleAndPermission(roleType: Int, permissionType: Int) {
        repository.findOne { root, _, criteriaBuilder ->
            return@findOne criteriaBuilder.and(
                criteriaBuilder.equal(root.get<Int>(SpulwBaseTableConfig.IntermediateUserRolePermissionColumn.ROLE), roleType),
                criteriaBuilder.equal(root.get<Int>(SpulwBaseTableConfig.IntermediateUserRolePermissionColumn.PERMISSION), permissionType))
        }.let {
            if (!it.isPresent) {
                repository.save(SpulwRolePermissionTb().also { tb ->
                    tb.permission = permissionType
                    tb.role = roleType
                })
            }
        }
    }
}