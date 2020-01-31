package javabase.lorenwang.common_base_frame.kotlinExtend

import javabase.lorenwang.common_base_frame.database.SbcbflwBaseTableConfig.CommonColumn.RANK
import javabase.lorenwang.tools.JtlwLogUtils
import kotlinbase.lorenwang.tools.extend.isEmpty
import org.springframework.jdbc.core.JdbcTemplate
import java.math.BigInteger
import javax.persistence.EntityManager

/**
 * 功能作用：jdbc数据库操作扩展
 * 创建时间：2019-10-30 下午 12:18:30
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */


/**
 * 更新移动所有的排行信息
 * @param tableName 表名
 * @param primaryKeyColumn 主键字段名
 * @param oldRank 旧的排行
 * @param newRank 新的排行
 */
fun EntityManager.upDataAllRankMove(tableName: String, primaryKeyColumn: String, oldRank: Long, newRank: Long): Long? {
    JtlwLogUtils.logI(JdbcTemplate::class.java, "开始更新排行相关数据")
    var newRankOptions = newRank

    //判断是否大于最大的排行，是的话则返回当前变更后的排行,前提是旧数据是非0开始移动的
    val maxRankList = this.createNativeQuery("select max($RANK) from $tableName").resultList
    maxRankList?.let {
        if (oldRank > 0 && newRankOptions > 0 && it.size == 1 &&
                it[0] is BigInteger && (it[0] as BigInteger) > BigInteger.ZERO &&
                (it[0] as BigInteger).toLong() < newRankOptions) {
            newRankOptions = (it[0] as BigInteger).toLong()
        }
    }

    //初始化修改的排行变动值
    val changeRank: Long
    val startRank: Long
    //根据排行变动获取要修改的数据
    val list: MutableList<Any?> = when {
        oldRank == 0L && newRankOptions > 0 -> {
            changeRank = 1;
            startRank = newRankOptions
            this.createNativeQuery("select $primaryKeyColumn from $tableName where $RANK>=$newRankOptions order by $RANK").resultList
        }
        oldRank > 0 && newRankOptions == 0L -> {
            changeRank = -1;
            startRank = oldRank + 1
            this.createNativeQuery("select $primaryKeyColumn from $tableName where $RANK>$oldRank order by $RANK").resultList
        }
        oldRank > newRankOptions -> {
            changeRank = 1;
            startRank = newRankOptions
            this.createNativeQuery("select $primaryKeyColumn from $tableName where $RANK>=$newRankOptions and $RANK<$oldRank order by $RANK").resultList
        }
        oldRank < newRankOptions -> {
            changeRank = -1;
            startRank = oldRank + 1
            this.createNativeQuery("select $primaryKeyColumn from $tableName where $RANK>$oldRank and $RANK<=$newRankOptions order by $RANK").resultList
        }
        else -> {
            changeRank = 0;
            startRank = 0;
            mutableListOf()
        }
    }

    val size = list.size
    var primaryKey: Any?
    for (index in 0 until size) {
        primaryKey = list[index]
        primaryKey?.let {
            if (this.createNativeQuery("update $tableName set $RANK=${startRank + index + changeRank}  where $primaryKeyColumn=$primaryKey").executeUpdate() <= 0) {
                JtlwLogUtils.logI(JdbcTemplate::class.java, "中止更新排行相关数据")
                return null
            }
        }
    }
    JtlwLogUtils.logI(JdbcTemplate::class.java, "完成更新排行相关数据")
    return newRankOptions
}

/**
 * 更新移动所有的排行信息
 * @param tableName 表名
 * @param primaryKeyColumn 主键字段名
 * @param newRank 新的排行
 * @param isAddLast 是否添加到最后
 */
fun EntityManager.addNewRank(tableName: String, primaryKeyColumn: String, newRank: Long, isAddLast: Boolean): Long {
    JtlwLogUtils.logI(JdbcTemplate::class.java, "开始更新排行相关数据")
    var newRankOptions = newRank
    //判断是否大于最大的排行，是的话则返回当前变更后的排行
    //新排行为0且不添加到最后时不处理，添加到最后时处理，不为0的话则是否添加到最后无效
    if (newRankOptions > 0 || isAddLast) {
        val maxRankList = this.createNativeQuery("select max($RANK) from $tableName").resultList
        maxRankList?.let {
            if (it.size == 1 && it[0] is BigInteger) {
                newRankOptions = when {
                    newRankOptions > 0 && (it[0] as BigInteger).toLong() < newRankOptions -> {
                        (it[0] as BigInteger).toLong() + 1
                    }
                    newRankOptions == 0L && isAddLast -> {
                        (it[0] as BigInteger).toLong() + 1
                    }
                    else -> {
                        newRankOptions
                    }
                }
            }
        }
    }


    //初始化修改的排行变动值
    val changeRank: Long
    val startRank: Long
    //根据排行变动获取要修改的数据
    val list: MutableList<Any?> = if (newRankOptions > 0) {
        changeRank = 1;
        startRank = newRankOptions
        this.createNativeQuery("select $primaryKeyColumn from $tableName where $RANK>=$newRankOptions order by $RANK").resultList
    } else {
        changeRank = 0;
        startRank = 0;
        mutableListOf()
    }

    val size = list.size
    var primaryKey: Any?
    for (index in 0 until size) {
        primaryKey = list[index]
        primaryKey?.let {
            if (this.createNativeQuery("update $tableName set $RANK=${startRank + index + changeRank}  where $primaryKeyColumn=$primaryKey").executeUpdate() <= 0) {
                JtlwLogUtils.logI(JdbcTemplate::class.java, "中止更新排行相关数据")
                return 0L
            }
        }
    }
    JtlwLogUtils.logI(JdbcTemplate::class.java, "完成更新排行相关数据")
    return newRankOptions
}

/**
 * 移除排行
 */
fun EntityManager.removeRank(tableName: String, primaryKeyColumn: String, oldRank: Long): Long? {
    return upDataAllRankMove(tableName, primaryKeyColumn, oldRank, 0L)
}
