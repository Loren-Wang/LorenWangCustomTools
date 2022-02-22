package com.test.springboot.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

/**
 * 功能作用：配置文件,继承实现，为了能够获取bean
 * 创建时间：2020-06-12 2:14 下午
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren wang）
 */
@Service
class PropertiesConfig {
    /**
     * 数据库表版本名称
     */
    @Value("\${database.table.versionName}")
    val databaseTableVersionName: String? = null

    /**
     * 数据库表版本号
     */
    @Value("\${database.table.versionCode}")
    val databaseTableVersionCode: Long? = null
}
