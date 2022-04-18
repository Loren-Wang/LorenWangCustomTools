package springbase.lorenwang.user.database.repository

import org.springframework.stereotype.Repository
import springbase.lorenwang.base.database.repository.SpblwBaseRepository
import springbase.lorenwang.user.database.table.SpulwPlatformTokenTb

/**
 * 功能作用：第三方平台token数据库表操作
 * 创建时间：2019-09-19 下午 15:10:49
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
interface SpulwPlatformTokenRepository : SpblwBaseRepository<SpulwPlatformTokenTb, String> {
    /**
     * 根据平台id获取平台相关信息
     */
    fun findByPlatformId(id: String): SpulwPlatformTokenTb?
}
