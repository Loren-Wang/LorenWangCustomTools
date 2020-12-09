package com.test.springboot.config

import com.qtoolsbaby.servicemmxs.config.PropertiesConfig
import com.test.springboot.database.repository.DatabaseTableVersionRepository
import com.test.springboot.database.table.DatabaseTableVersionTb
import com.test.springboot.utils.LogUtils
import javabase.lorenwang.common_base_frame.SbcbflwCommon
import kotlinbase.lorenwang.tools.extend.formatConversion
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.interceptor.TransactionAspectSupport

/**
 * 功能作用：数据库表版本控制操作类
 * 创建时间：2019-10-10 下午 15:16:31
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
@Service
open class DataBaseTableVersionUpdateOptions {
    //数据库表版本表操作实例
    private lateinit var databaseTableVersionRepository : DatabaseTableVersionRepository

    @Transactional
    open fun initData(applicationContext : ConfigurableApplicationContext) {

        try {
            val config = SbcbflwCommon.instance.propertiesConfig.formatConversion<PropertiesConfig>()
            LogUtils.instance.logI(javaClass, "开始执行版本${config?.databaseTableVersionName}的更新")
            //获取数据库表版本表操作实例
            databaseTableVersionRepository = applicationContext.getBean(DatabaseTableVersionRepository::class.java)
            val tableVersion = databaseTableVersionRepository.findDatabaseTableVersionTbByVersionCodeAndVersionName(config?.databaseTableVersionCode!!, config.databaseTableVersionName!!)
            if (tableVersion == null) {
                when (config.databaseTableVersionCode) {
                    100L -> {
                    }
                    else -> {

                    }
                }

                //保存版本信息
                val databaseTableVersionTb = DatabaseTableVersionTb()
                databaseTableVersionTb.versionName = config.databaseTableVersionName
                databaseTableVersionTb.versionCode = config.databaseTableVersionCode
                databaseTableVersionRepository.save(databaseTableVersionTb)
            }
        } catch (e : Exception) {
            LogUtils.instance.logE(javaClass, "更新发生异常，手动执行异常回滚，异常信息：${e.message}")
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()
        }
    }
}
