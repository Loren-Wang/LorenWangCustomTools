package com.test.springboot.database.table

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.test.springboot.database.TableInfoConfig
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseDatabaseTableVersionTb
import javax.persistence.Entity
import javax.persistence.Table

/**
 * 功能作用：数据库数据表版本记录
 * 初始注释时间： 2020/12/9 10:34 上午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
@Entity
@Table(name = TableInfoConfig.TableName.DATABASE_TABLE_VERSION)
@org.hibernate.annotations.Table(appliesTo = TableInfoConfig.TableName.DATABASE_TABLE_VERSION, comment = "数据库表版本表")
@JsonAutoDetect
class DatabaseTableVersionTb : SbcbflwBaseDatabaseTableVersionTb()
