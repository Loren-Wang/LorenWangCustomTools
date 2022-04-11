package springbase.lorenwang.user.database.repository

import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Repository
import springbase.lorenwang.base.database.repository.SpblwBaseRepository
import springbase.lorenwang.user.database.table.SpulwBaseUserInfoTb

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
interface SpulwUserInfoRepository<T : SpulwBaseUserInfoTb> : SpblwBaseRepository<T, String>
