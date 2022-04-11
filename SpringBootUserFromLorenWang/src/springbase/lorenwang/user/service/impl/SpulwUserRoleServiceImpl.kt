package springbase.lorenwang.user.service.impl

import org.springframework.beans.factory.annotation.Autowired
import springbase.lorenwang.base.database.SpblwBaseTableConfig
import springbase.lorenwang.user.database.repository.SpulwUserRoleRepository
import springbase.lorenwang.user.database.table.SpulwUserRoleTb
import springbase.lorenwang.user.service.SpulwUserRoleService

/**
 * 功能作用：用户角色处理
 * 初始注释时间： 2022/4/11 08:50
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
open class SpulwUserRoleServiceImpl : SpulwUserRoleService {
    @Autowired
    protected lateinit var repository: SpulwUserRoleRepository

    /**
     * 保存用户角色
     * @param type 类型
     * @param name 名称
     */
    override fun saveUserRole(type: Int, name: String) {
        repository.findOne { root, _, criteriaBuilder ->
            return@findOne criteriaBuilder.and(criteriaBuilder.equal(root.get<Int>(SpblwBaseTableConfig.CommonColumn.TYPE), type))
        }.let {
            if (it.isPresent) {
                it.get()
            } else {
                SpulwUserRoleTb()
            }
        }.also { it.name = name;it.type = type }.let {
            repository.save(it)
        }
    }
}