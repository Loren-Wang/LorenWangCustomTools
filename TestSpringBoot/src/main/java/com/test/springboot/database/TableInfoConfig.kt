package com.test.springboot.database

/**
 * 功能作用：数据库表配置类
 * 创建时间：2019-09-12 上午 10:05:24
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
object TableInfoConfig {
    /*********************************表名部分*********************************/

    object TableName {
        /**
         * 数据库表版本
         */
        const val DATABASE_TABLE_VERSION = "db_table_version"

        /**
         * 用户信息表
         */
        const val USER_INFO = "user_info"

        /**
         * 用户角色表
         */
        const val USER_ROLE = "user_role"

        /**
         * 用户角色权限表
         */
        const val USER_PERMISSION = "user_permission"

    }
}
