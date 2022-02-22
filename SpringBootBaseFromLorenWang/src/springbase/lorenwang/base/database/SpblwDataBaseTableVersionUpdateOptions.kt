package springbase.lorenwang.base.database

import org.springframework.context.ConfigurableApplicationContext
import org.springframework.transaction.interceptor.TransactionAspectSupport
import springbase.lorenwang.base.SpblwConfig
import springbase.lorenwang.base.database.repository.SpblwDatabaseTableVersionRepository
import springbase.lorenwang.base.database.table.SpblwBaseDatabaseTableVersionTb

/**
 * 功能作用：数据库表版本控制操作类
 * 创建时间：2019-10-10 下午 15:16:31
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
abstract class SpblwDataBaseTableVersionUpdateOptions<T : SpblwBaseDatabaseTableVersionTb, R : SpblwDatabaseTableVersionRepository<T>> {
    fun initData(applicationContext: ConfigurableApplicationContext, databaseTableVersionRepository: R) {
        try {
            SpblwConfig.logUtils.logI(javaClass, "开始执行版本${getVersionName()}的数据库等更新")
            //获取数据库表版本表操作实例
            val tableVersion =
                databaseTableVersionRepository.findDatabaseTableVersionTbByVersionCodeAndVersionName(getVersionCode(), getVersionName())
            if (tableVersion == null) {
                //开始修改数据库
                startChangeDataBase(applicationContext)

                //保存版本信息
                val databaseTableVersionTb = getDatabaseTableVersionTb()
                databaseTableVersionTb.versionName = getVersionName()
                databaseTableVersionTb.versionCode = getVersionCode()
                databaseTableVersionRepository.save(databaseTableVersionTb)
            }
            SpblwConfig.logUtils.logI(javaClass, "版本${getVersionName()}的数据库等更新完成")
        } catch (e: Exception) {
            try {
                SpblwConfig.logUtils.logE(javaClass, "更新发生异常，手动执行异常回滚，异常信息：${e.message}")
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()
            } catch (e: Exception) {
                SpblwConfig.logUtils.logE(javaClass, "更新发生异常，手动执行异常回滚，异常信息：${e.message}")
            }

        }
    }

    /**
     * 获取数据库存储实例
     */
    abstract fun getDatabaseTableVersionTb(): T

    /**
     * 获取版本名
     * @return 版本名
     */
    abstract fun getVersionName(): String

    /**
     * 获取版本code
     * @return 版本code
     */
    abstract fun getVersionCode(): Long

    /**
     * 开始进行数据库数据修改
     */
    abstract fun startChangeDataBase(applicationContext: ConfigurableApplicationContext)
}
