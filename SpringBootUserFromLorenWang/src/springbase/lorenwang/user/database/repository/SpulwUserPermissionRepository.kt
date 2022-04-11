package springbase.lorenwang.user.database.repository


import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import springbase.lorenwang.base.database.repository.SpblwBaseRepository
import springbase.lorenwang.user.database.table.SpulwUserPermissionTb


/**
 * 功能作用：用户权限数据库操作
 * 初始注释时间： 2020/1/12 0012 下午 17:00:33
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
interface SpulwUserPermissionRepository : SpblwBaseRepository<SpulwUserPermissionTb, String> {
    /**
     * 根据type获取数据
     */
    fun findByType(type: Int): SpulwUserPermissionTb?
}
