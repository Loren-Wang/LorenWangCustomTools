package springbase.lorenwang.user.service.impl

import org.springframework.beans.factory.annotation.Autowired
import springbase.lorenwang.base.database.SpblwBaseTableConfig
import springbase.lorenwang.user.database.repository.SpulwUserPermissionRepository
import springbase.lorenwang.user.database.table.SpulwUserPermissionTb
import springbase.lorenwang.user.service.SpulwUserPermissionService

/**
 * 功能作用：用户权限服务实现
 * 初始注释时间： 2022/4/11 08:43
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
open class SpulwUserPermissionServiceImpl : SpulwUserPermissionService {
    @Autowired
    protected lateinit var repository: SpulwUserPermissionRepository

    /**
     * 保存用户权限
     * @param type 类型
     * @param name 名称
     */
    override fun saveUserPermission(type: Int, name: String) {
        repository.findOne { root, _, criteriaBuilder ->
            return@findOne criteriaBuilder.and(criteriaBuilder.equal(root.get<Int>(SpblwBaseTableConfig.CommonColumn.TYPE), type))
        }.let {
            if (it.isPresent) {
                it.get()
            } else {
                SpulwUserPermissionTb()
            }
        }.also { it.permissionName = name;it.type = type }.let {
            repository.save(it)
        }
    }
}