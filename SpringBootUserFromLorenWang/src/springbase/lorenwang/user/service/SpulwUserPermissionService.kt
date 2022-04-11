package springbase.lorenwang.user.service

import springbase.lorenwang.base.service.SpblwBaseService
import springbase.lorenwang.user.database.repository.SpulwUserPermissionRepository

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
interface SpulwUserPermissionService : SpblwBaseService {
    /**
     * 保存用户权限
     * @param type 类型
     * @param name 名称
     */
    fun saveUserPermission(type: Int, name: String)
}
