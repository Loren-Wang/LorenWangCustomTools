package javabase.lorenwang.common_base_frame.kotlinExtend

import com.sun.xml.internal.bind.v2.model.core.ID
import javabase.lorenwang.common_base_frame.database.SbcbflwBaseTableConfig.CommonColumn.RANK
import javabase.lorenwang.common_base_frame.utils.SbcbfBaseAllUtils
import kotlinbase.lorenwang.tools.extend.emptyCheck
import kotlinbase.lorenwang.tools.extend.isNotNullOrEmpty
import org.springframework.transaction.annotation.Transactional
import java.math.BigInteger
import javax.persistence.EntityManager

/**
 * 功能作用：jdbc数据库操作扩展
 * 创建时间：2019-10-30 下午 12:18:30
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */


/**
 * 更新移动所有的排行信息，将from到to范围的所有排行从from开始处理重新设置
 * @param tableName 表名
 * @param primaryKeyColumn 主键字段名
 * @param rankColumn 排行字段名
 * @param fromRank 起始排行
 * @param toRank 目标排行
 * @return 更新异常返回null，否则返回新的目标排行
 */
@Transactional
fun EntityManager.sbcbflwUpDataAllRankMove(tableName : String, primaryKeyColumn : String, rankColumn : String? = RANK, fromRank : BigInteger, toRank : BigInteger) : BigInteger? {
    SbcbfBaseAllUtils.logUtils.logI(EntityManager::class.java, "开始重新按顺序设置排行数据")
    //处理后的排行信息
    val newRankOptions = sbcbflwGetTableMaxRank(tableName, rankColumn!!).emptyCheck({ toRank }, {
        if (it < toRank) {
            it
        } else {
            toRank
        }
    })

    //每条数据的前移后移记录
    val changeRank : BigInteger
    //起始排行位置
    val startRank : BigInteger
    //根据排行变动获取要修改的数据
    val list : MutableList<Any?> = when {
        fromRank == BigInteger.ZERO && newRankOptions > BigInteger.ZERO -> {
            changeRank = BigInteger.ONE
            startRank = newRankOptions
            this.createNativeQuery("select $primaryKeyColumn from $tableName where $rankColumn>=$newRankOptions order by $rankColumn").resultList
        }
        fromRank > BigInteger.ZERO && newRankOptions == BigInteger.ZERO -> {
            changeRank = BigInteger.valueOf(-1)
            startRank = fromRank + BigInteger.ONE
            this.createNativeQuery("select $primaryKeyColumn from $tableName where $rankColumn>$fromRank order by $rankColumn").resultList
        }
        fromRank > newRankOptions -> {
            changeRank = BigInteger.ONE
            startRank = newRankOptions
            this.createNativeQuery("select $primaryKeyColumn from $tableName where $rankColumn>=$newRankOptions and $rankColumn<$fromRank order by $rankColumn").resultList
        }
        fromRank < newRankOptions -> {
            changeRank = BigInteger.valueOf(-1)
            startRank = fromRank + BigInteger.ONE
            this.createNativeQuery("select $primaryKeyColumn from $tableName where $rankColumn>$fromRank and $rankColumn<=$newRankOptions order by $rankColumn").resultList
        }
        else -> {
            changeRank = BigInteger.ZERO
            startRank = BigInteger.ZERO
            mutableListOf()
        }
    }

    val size = list.size
    var primaryKey : Any?
    for (index in 0 until size) {
        primaryKey = list[index]
        primaryKey?.let {
            if (this.createNativeQuery("update $tableName set $rankColumn=${startRank.add(changeRank).add(BigInteger.valueOf(index.toLong()))}  where $primaryKeyColumn=$primaryKey").executeUpdate() <= 0) {
                SbcbfBaseAllUtils.logUtils.logI(EntityManager::class.java, "中止更新排行相关数据")
                return null
            }
        }
    }
    SbcbfBaseAllUtils.logUtils.logI(EntityManager::class.java, "完成更新排行相关数据")
    return newRankOptions
}

/**
 * 移除指定排行数据
 * @param tableName 表名
 * @param primaryKeyColumn 主键字段名
 * @param rankColumn 排行字段名
 * @param deleteRank 要删除的排行
 */
fun EntityManager.sbcbflwRemoveRank(tableName : String, primaryKeyColumn : String, rankColumn : String? = RANK, deleteRank : BigInteger) : BigInteger? {
    return sbcbflwUpDataAllRankMove(tableName, primaryKeyColumn, rankColumn, deleteRank, BigInteger.ZERO)
}

/**
 * 删除指定表的一条数据
 * @param tableName 表名
 * @param primaryKeyColumn 主键字段名
 * @param id 要删除的id
 * @param rankColumn 该字段不为空则代表着要移动指定的排行信息
 */
fun <Id> EntityManager.sbcbflwDeleteTableInfo(tableName : String, primaryKeyColumn : String, rankColumn : String?, id : ID) : Boolean {
    var deleteRank : BigInteger? = null
    if (rankColumn.isNotNullOrEmpty()) {
        //需要更新排行则先查询原始数据
        this.createNativeQuery("select $rankColumn from $tableName where $primaryKeyColumn=$id").resultList?.let {
            if (it.size == 1 && it[0] is BigInteger) {
                deleteRank = it[0] as BigInteger
            }
        }
    }
    //删除数据
    return (this.createQuery("delete $tableName  where $primaryKeyColumn=$id").executeUpdate() <= 0).let {
        if (it) {
            //删除失败
            false
        } else {
            //删除成功，判断是否需要移除排行
            if (deleteRank != null) {
                sbcbflwRemoveRank(tableName, primaryKeyColumn, rankColumn, deleteRank!!)
            }
            true
        }
    }
}

/**
 * 获取表中最大的排行
 * @param tableName 表名
 * @param rankColumn 排行使用的字段
 */
fun EntityManager.sbcbflwGetTableMaxRank(tableName : String, rankColumn : String) : BigInteger? {
    this.createNativeQuery("select max($rankColumn) from $tableName").resultList?.let {
        if (it.size == 1 && it[0] is BigInteger) {
            return it[0] as BigInteger
        }
    }
    return null
}
