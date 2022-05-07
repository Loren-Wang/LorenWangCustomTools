package springbase.lorenwang.tools.utils

import kotlinbase.lorenwang.tools.extend.kttlwFormatConversion
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData
import kotlinbase.lorenwang.tools.extend.kttlwIsNotNullOrEmpty
import kotlinbase.lorenwang.tools.extend.kttlwToJsonData
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.util.StringUtils
import springbase.lorenwang.tools.sptlwConfig
import java.util.*


/**
 * 功能作用：同步工具类
 * 初始注释时间： 2022/5/5 21:27
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
class SptlwSyncTableUtils private constructor() {
    /**
     * 数据库连接实例
     */
    private val jdbcTemplate by lazy { sptlwConfig.applicationContext.getBean(JdbcTemplate::class.java) }

    companion object {
        private var optionsInstance: SptlwSyncTableUtils? = null
        val instance: SptlwSyncTableUtils
            get() {
                if (optionsInstance == null) {
                    synchronized(this::class.java) {
                        if (optionsInstance == null) {
                            optionsInstance = SptlwSyncTableUtils()
                        }
                    }
                }
                return optionsInstance!!
            }
    }

    /**
     * 复制表结构
     * @params fromName 源表名
     * @params toName 目标表名
     */
    fun copyTable(fromName: String, toName: String) {
        sptlwConfig.getLogUtil().logI(javaClass, "复制表结构--源：${fromName},目标：${toName}")
        getCreateTableSql(fromName, toName)?.let { sql ->
            sptlwConfig.getLogUtil().logI(javaClass, "复制表结构--获取到表结构创建语句：${sql}")
            jdbcTemplate.execute(sql)
        }

    }

    /**
     * 同步表格列字段
     */
    fun syncTableStructure(fromName: String, toName: String) {
        sptlwConfig.getLogUtil().logI(javaClass, "同步表结构--源：${fromName},目标：${toName}")
        val fromTable = TableInfo(fromName).also {
            getTableColumns(fromName).forEach { item ->
                it.columns[item.name] = item
            }
            getTableIndex(fromName).forEach { item ->
                it.indexes[item.indexName] = item
            }
        }
        val toTable = TableInfo(toName).also {
            it.createTable = getCreateTableSql(fromName, toName)
            getTableColumns(toName).forEach { item ->
                it.columns[item.name] = item
            }
            getTableIndex(toName).forEach { item ->
                it.indexes[item.indexName] = item
            }
        }
        //要执行的sql列表
        val changeSql = arrayListOf<String>()
        toTable.createTable?.let { it -> changeSql.add(it) }
        changeSql.addAll(compareColumns(fromTable, toTable))
        changeSql.addAll(compareSingleKeys(fromTable, toTable))
        //执行修改sql
        sptlwConfig.getLogUtil().logI(javaClass, "同步表结构--要执行的sql语句：${changeSql.kttlwToJsonData()}")
        changeSql.forEach {
            ddl(it)
        }
    }

    /**
     * 获取创建表的sql语句
     */
    private fun getCreateTableSql(fromName: String, toName: String): String? {
        jdbcTemplate.queryForMap("show create table $fromName;").let {
            return it["Create Table"].kttlwFormatConversion<String>()?.toLowerCase(Locale.getDefault())?.let { createSql ->
                if (createSql.contains("create table if not exists")) {
                    createSql
                } else {
                    createSql.replace("create table", "create table if not exists")
                }.replace(fromName, toName)
            }
        }
    }

    /**
     * 获取表字段
     */
    private fun getTableColumns(tableName: String): List<TableColumn> {
        try {
            jdbcTemplate.queryForList(
                "select COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_DEFAULT,COLUMN_COMMENT,EXTRA from information_schema.columns where TABLE_NAME=${
                    getDbString(tableName)
                } order by ORDINAL_POSITION ").let {
                val list = arrayListOf<TableColumn>()
                var column: TableColumn
                for (map in it) {
                    column = TableColumn()
                    column.name = map["COLUMN_NAME"].kttlwFormatConversion()
                    column.type = map["COLUMN_TYPE"].kttlwFormatConversion()
                    column.defaultValue = map["COLUMN_DEFAULT"].kttlwFormatConversion()
                    column.isNull = map["IS_NULLABLE"].kttlwFormatConversion()
                    column.extra = map["EXTRA"].kttlwFormatConversion<String>().kttlwGetNotEmptyData { "" }
                    column.comment = map["COLUMN_COMMENT"].kttlwFormatConversion()
                    list.add(column)
                }
                return list
            }
        } catch (e: Exception) {
            return arrayListOf()
        }
    }

    /**
     * 获取表索引
     */
    private fun getTableIndex(tableName: String): List<TableIndex> {
        try {
            jdbcTemplate.queryForList("show keys from $tableName").let {
                val list = arrayListOf<TableIndex>()
                var last: TableIndex? = null
                var keyName: String?
                for (map in it) {
                    keyName = map["Key_name"].kttlwFormatConversion()
                    if (last == null || keyName.kttlwIsNotNullOrEmpty() && keyName != last.indexName) {
                        last = TableIndex()
                        last.indexName = keyName
                        last.columns.add(map["Column_name"].kttlwFormatConversion<String>().kttlwGetNotEmptyData { "" })
                        last.notUnique = map["Non_unique"].kttlwFormatConversion()
                        list.add(last)
                    } else {
                        // 表明这两个key在同一索引中
                        last.columns.add(map["Column_name"].kttlwFormatConversion<String>().kttlwGetNotEmptyData { "" })
                    }
                }
                return list
            }
        } catch (e: Exception) {
            return arrayListOf()
        }
    }

    /**
     * 执行sql语句
     */
    private fun ddl(sql: String) {
        try {
            jdbcTemplate.execute(sql)
        } catch (e: Exception) {
            println("ddl fail")
            e.printStackTrace()
        }
    }

    private fun getDbString(s: String?): String {
        return "'${s.kttlwGetNotEmptyData { "" }}'"
    }

    /**
     * 比较字段
     */
    private fun compareColumns(sourceTable: TableInfo, targetTable: TableInfo): ArrayList<String> {
        val changeSql = arrayListOf<String>()
        // 记录最后一个比较的column
        var after: String? = null
        for (column in sourceTable.columns.values) {
            if (targetTable.columns[column.name] == null) {
                // 如果对应的target没有这个字段,直接alter
                var sql = "alter table " + targetTable.tableName + " add " + column.name + " "
                sql += column.type + " "
                sql += if (column.isNull == "NO") {
                    "NOT NULL "
                } else {
                    "NULL "
                }
                if (column.defaultValue != null) {
                    sql += "DEFAULT " + getDbString(column.defaultValue) + " "
                }
                if (column.comment != null) {
                    sql += "COMMENT " + getDbString(column.comment) + " "
                }
                if (after != null) {
                    sql += "after $after"
                }
                changeSql.add("$sql;")
            } else {
                // 检查对应的source 和 target的属性
                val sql = "alter table " + targetTable.tableName + " change " + column.name + " "
                val targetColumn = targetTable.columns[column.name]
                // 比较两者字段,如果返回null,表明一致
                val sqlExtend = compareSingleColumn(column, targetColumn)
                if (sqlExtend != null) {
                    changeSql.add("$sql$sqlExtend;")
                }
            }
            after = column.name
        }
        // remove the target redundancy columns
        for (column in targetTable.columns.values) {
            if (sourceTable.columns[column.name] == null) {
                // redundancy , so drop it
                val sql = "alter table " + targetTable.tableName + " drop " + column.name + " "
                changeSql.add("$sql;")
            }
        }
        return changeSql
    }

    /**
     * 比较字段
     */
    private fun compareSingleColumn(sourceColumn: TableColumn, targetColumn: TableColumn?): String? {
        if (sourceColumn == targetColumn) {
            return null
        }
        var changeSql = ""
        if (sourceColumn.name != targetColumn!!.name) {
            // never reach here
            throw RuntimeException("the bug in this tool")
        }
        changeSql += sourceColumn.name + " "
        changeSql += sourceColumn.type + " "
        changeSql += if (sourceColumn.isNull == "NO") {
            "NOT NULL "
        } else {
            "NULL "
        }
        if (sourceColumn.extra.toUpperCase(Locale.getDefault()).indexOf("AUTO_INCREMENT") != -1) {
            changeSql += "AUTO_INCREMENT "
        }
        if (!StringUtils.isEmpty(sourceColumn.defaultValue)) {
            changeSql += "DEFAULT " + sourceColumn.defaultValue + " "
        }
        if (sourceColumn.comment != null) {
            changeSql += "COMMENT " + getDbString(sourceColumn.comment) + " "
        }
        return changeSql
    }

    /**
     * 比较索引
     */
    private fun compareSingleKeys(sourceTable: TableInfo, targetTable: TableInfo): ArrayList<String> {
        val changeSql = arrayListOf<String>()
        for (index in sourceTable.indexes.values) {
            var sql = "alter table " + targetTable.tableName + " "
            if (targetTable.indexes[index.indexName] == null) {
                sql += if (index.indexName == "PRIMARY") {
                    "add primary key "
                } else {
                    if (index.notUnique == "0") {
                        "add unique " + index.indexName + " "
                    } else {
                        "add index " + index.indexName + " "
                    }
                }
                sql += "(`"
                for (key in index.columns) {
                    sql += key.trim { it <= ' ' } + "`,`"
                }
                // 去掉最后一个,`
                sql = sql.substring(0, sql.length - 2) + ")"
                changeSql.add("$sql;")
            }
        }
        for (index in targetTable.indexes.values) {
            if (sourceTable.indexes[index.indexName] == null) {
                // 表明此索引多余
                var sql = "alter table " + targetTable.tableName + " "
                sql += if (index.indexName == "PRIMARY") {
                    "drop primary key "
                } else {
                    "drop index " + index.indexName
                }
                changeSql.add("$sql;")
            }
        }
        return changeSql
    }

    /**
     * 表格信息
     */
    private class TableInfo(var tableName: String) {
        var createTable: String? = null
        val columns: HashMap<String?, TableColumn> = hashMapOf()
        val indexes: HashMap<String?, TableIndex> = hashMapOf()
        override fun toString(): String {
            return "Table{tableName='$tableName', columns=$columns, indexes=$indexes}"
        }
    }

    /**
     * 表格字段信息
     */
    private class TableColumn {
        //$sql = 'select COLUMN_NAME,COLUMN_TYPE,IS_NULLABLE,COLUMN_DEFAULT,COLUMN_COMMENT,EXTRA from information_schema.columns ';
        //$sql.= 'where TABLE_SCHEMA=\''.$db.'\' and TABLE_NAME=\''.$table.'\' order by ORDINAL_POSITION asc';
        var name: String? = null
        var type: String? = null
        var isNull: String? = null
        var defaultValue: String? = null
            get() = field.kttlwGetNotEmptyData { "" }
        var comment: String? = null
        var extra: String = ""

        override fun equals(other: Any?): Boolean {
            if (this === other) {
                return true
            }
            if (other == null || javaClass != other.javaClass) {
                return false
            }
            val column = other as TableColumn
            if (if (name != null) name != column.name else column.name != null) {
                return false
            }
            if (if (type != null) type != column.type else column.type != null) {
                return false
            }
            if (if (isNull != null) isNull != column.isNull else column.isNull != null) {
                return false
            }
            if (if (defaultValue != null) defaultValue != column.defaultValue else column.defaultValue != null) {
                return false
            }
            // todo comment remove
            /*if (comment != null ? !comment.equals(column.comment) : column.comment != null) {
            return false;
        }*/return extra == column.extra
        }

        override fun hashCode(): Int {
            var result = if (name != null) name.hashCode() else 0
            result = 31 * result + if (type != null) type.hashCode() else 0
            result = 31 * result + if (isNull != null) isNull.hashCode() else 0
            result = 31 * result + if (defaultValue != null) defaultValue.hashCode() else 0
            result = 31 * result + if (comment != null) comment.hashCode() else 0
            result = 31 * result + extra.hashCode()
            return result
        }

        override fun toString(): String {
            return "Column{name='$name', type='$type', isNull='$isNull', defaultValue='$defaultValue', comment='$comment', extra='$extra'}"
        }
    }

    /**
     * 表格索引信息
     */
    private class TableIndex {
        var columns: ArrayList<String> = ArrayList()
        var indexName: String? = null

        // 0表示unique,1表示普通索引
        var notUnique: String? = null

        override fun toString(): String {
            return """
               TableIndex{columns=$columns, indexName='$indexName', notUnique='$notUnique'}
               """.trimIndent()
        }
    }

}