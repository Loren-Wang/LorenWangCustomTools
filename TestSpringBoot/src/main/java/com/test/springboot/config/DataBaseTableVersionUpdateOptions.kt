package com.test.springboot.config

import com.test.springboot.database.TableInfoConfig
import com.test.springboot.database.repository.DatabaseTableVersionRepository
import com.test.springboot.database.repository.UserInfoRepository
import com.test.springboot.database.repository.UserPermissionRepository
import com.test.springboot.database.repository.UserRoleRepository
import com.test.springboot.database.table.DatabaseTableVersionTb
import com.test.springboot.database.table.UserInfoTb
import com.test.springboot.database.table.UserPermissionTb
import com.test.springboot.database.table.UserRoleTb
import com.test.springboot.enums.UserPermissionTypeEnum
import com.test.springboot.enums.UserRoleTypeEnum
import com.test.springboot.service.UserService
import com.test.springboot.utils.LogUtil
import javabase.lorenwang.common_base_frame.SbcbflwCommon
import javabase.lorenwang.common_base_frame.database.SbcbflwBaseTableConfig
import kotlinbase.lorenwang.tools.extend.formatConversion
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.jdbc.core.JdbcTemplate
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
            //获取数据库表版本表操作实例
            databaseTableVersionRepository = applicationContext.getBean(DatabaseTableVersionRepository::class.java)
            val tableVersion = databaseTableVersionRepository.findDatabaseTableVersionTbByVersionCodeAndVersionName(config?.databaseTableVersionCode!!, config.databaseTableVersionName!!)
            if (tableVersion == null) {
                LogUtil.instance.logI(javaClass, "开始执行版本${config.databaseTableVersionName}的更新")
                when (config.databaseTableVersionCode) {
                    100L -> {
                        ChangeTableV100(applicationContext).initTable()
                    }
                    else -> {

                    }
                }

                //保存版本信息
                val databaseTableVersionTb = DatabaseTableVersionTb()
                databaseTableVersionTb.versionName = config.databaseTableVersionName
                databaseTableVersionTb.versionCode = config.databaseTableVersionCode
                databaseTableVersionRepository.save(databaseTableVersionTb)
                LogUtil.instance.logI(javaClass, "版本${config.databaseTableVersionName}更新成功")
            }
        } catch (e : Exception) {
            LogUtil.instance.logE(javaClass, "更新发生异常，手动执行异常回滚，异常信息：${e.message}")
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()
        }
    }

    /**
     * 功能作用：v1.0.0版本更新
     * 初始注释时间： 2019/10/10 0010 下午 18:10:26
     * 注释创建人：Loren（王亮）
     * 方法介绍：
     * 思路：
     * 修改人：
     * 修改时间：
     * 备注：
     */
    class ChangeTableV100(private var applicationContext : ConfigurableApplicationContext) {
        /**
         * 超级管理员角色
         */
        private lateinit var userRoleSuperAdmin : UserRoleTb

        //获取jdbc操作实例
        private val jdbcTemplate = applicationContext.getBean(JdbcTemplate::class.java)

        @Throws(java.lang.Exception::class)
        fun initTable() {
            jdbcTemplate.execute("SET foreign_key_checks = 0;")
            //初始化用户角色和权限信息
            initUserRoleAndPermissionInfo()
            jdbcTemplate.execute("SET foreign_key_checks = 1;")
        }

        /**
         * 初始化用户角色和权限信息
         */
        @Throws(java.lang.Exception::class)
        private fun initUserRoleAndPermissionInfo() {
            //清理旧数据表
            clearTable(SbcbflwBaseTableConfig.TableName.INTERMEDIATE_USER_ROLE_PERMISSION)
            clearTable(TableInfoConfig.TableName.USER_ROLE)
            clearTable(TableInfoConfig.TableName.USER_PERMISSION)

            val userRoleRepository = applicationContext.getBean(UserRoleRepository::class.java)
            val userPermissionRepository = applicationContext.getBean(UserPermissionRepository::class.java)
            val userService = applicationContext.getBean(UserService::class.java)

            val userRoleTb = UserRoleTb()
            userRoleTb.roleName = UserRoleTypeEnum.SUPER_ADMIN.des
            userRoleTb.roleType = UserRoleTypeEnum.SUPER_ADMIN.type
            userRoleTb.permission = mutableSetOf()
            val userPermissionTb = savePermission(userPermissionRepository, "超级管理员权限", UserPermissionTypeEnum.SUPER_ADMIN)
            userRoleTb.permission?.add(userPermissionTb)
            userRoleSuperAdmin = userRoleRepository.save(userRoleTb)

            //添加初始超级管理员
            userService.addNewUser("wangliang", "18321634834", userRoleSuperAdmin)

        }

        /**
         *
         */
        @Throws(java.lang.Exception::class)
        private fun savePermission(userPermissionRepository : UserPermissionRepository, permissionName : String, permissionType : UserPermissionTypeEnum) : UserPermissionTb {
            val userPermissionTb = UserPermissionTb()
            userPermissionTb.permissionName = permissionName
            userPermissionTb.type = permissionType.type
            return userPermissionRepository.save(userPermissionTb)
        }

        /**
         * 完全清理表，流程为复制表结构，删除表，修改表名称三步
         */
        @Throws(java.lang.Exception::class)
        private fun clearTable(tableName : String) {
            val copyTableName = "${tableName}_c"
            //复制表结构
            jdbcTemplate.execute("create table if not exists $copyTableName like $tableName")
            //删除表
            jdbcTemplate.execute("drop table $tableName")
            //修改表名称
            jdbcTemplate.execute("alter table $copyTableName rename to $tableName")
        }

    }
}
