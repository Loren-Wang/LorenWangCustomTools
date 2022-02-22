package springbase.lorenwang.user.database.repository

import org.springframework.data.repository.NoRepositoryBean
import springbase.lorenwang.base.database.repository.SpblwBaseRepository
import springbase.lorenwang.user.database.table.SpulwBaseUserInfoTb
import springbase.lorenwang.user.database.table.SpulwBaseUserRoleTb

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
@NoRepositoryBean
interface SpulwUserInfoRepository<P, ROLE_TB : SpulwBaseUserRoleTb<P>, T : SpulwBaseUserInfoTb<P, ROLE_TB>> : SpblwBaseRepository<T, String>
