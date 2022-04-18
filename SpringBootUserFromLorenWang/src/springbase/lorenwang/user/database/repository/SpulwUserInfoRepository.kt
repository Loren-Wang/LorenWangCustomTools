package springbase.lorenwang.user.database.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import springbase.lorenwang.base.database.repository.SpblwBaseRepository
import springbase.lorenwang.user.database.SpulwBaseTableConfig
import springbase.lorenwang.user.database.SpulwBaseTableConfig.TableName.USER_INFO
import springbase.lorenwang.user.database.table.SpulwUserInfoTb

/**
 * 功能作用：用户数据库表操作
 * 创建时间：2019-09-19 下午 15:10:49
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
interface SpulwUserInfoRepository : SpblwBaseRepository<SpulwUserInfoTb, String> {
    /**
     * 根据用户名以查询用户数据
     */
    @Query(value = "select * from $USER_INFO where ${SpulwBaseTableConfig.UserInfoColumn.ACCOUNT}=:#{#account}", nativeQuery = true)
    fun getUserInfoTbByAccount(@Param("account") account: String): List<SpulwUserInfoTb>?

}
