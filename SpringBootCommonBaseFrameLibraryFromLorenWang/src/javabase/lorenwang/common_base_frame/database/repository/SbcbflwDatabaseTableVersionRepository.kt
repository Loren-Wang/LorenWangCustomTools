package javabase.lorenwang.common_base_frame.database.repository

import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseDatabaseTableVersionTb
import org.springframework.data.repository.NoRepositoryBean

/**
 * 功能作用：数据库表版本表操作
 * 创建时间：2019-09-15 下午 19:45:57
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
@NoRepositoryBean
interface SbcbflwDatabaseTableVersionRepository<T : SbcbflwBaseDatabaseTableVersionTb> : SbcbflwBaseRepository<T> {
    fun findDatabaseTableVersionTbByVersionCodeAndVersionName(versionCode: Long, versionName: String): T?
}
