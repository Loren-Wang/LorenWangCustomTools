package javabase.lorenwang.common_base_frame.kotlinExtend

import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseController
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseHttpServletRequestWrapper
import javabase.lorenwang.common_base_frame.database.SbcbflwDatabaseParams.FIRST_RANK_LIST
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseTb
import kotlinbase.lorenwang.tools.common.bean.KttlwBaseNetUpDateRankReqBean
import org.springframework.data.repository.CrudRepository

/**
 * 功能作用：CrudRepository数据库操作扩展
 * 创建时间：2020-11-25 4:58 下午
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


/**
 * 更新一个表中所有排行信息
 * @param baseController 基础控制器
 * @param rankBean 要更新的排行信息
 * @param checkOldCount 是否要检测更新的排行数量和数据库内的总数量一致
 * @param getNewSaveTbFun 获取要新保存的数据库实例,参数1:tb实体数据，参数2：新排行
 */
fun <CURD : CrudRepository<TB, ID>, ID, TB : SbcbflwBaseTb, R : SbcbflwBaseHttpServletRequestWrapper, BC : SbcbflwBaseController<R>> CURD.sbcbflwUpDataTbAllRank(
        baseController : BC, rankBean : KttlwBaseNetUpDateRankReqBean<ID>,
        checkOldCount : Boolean, getNewSaveTbFun : (tbInfo : TB, newRank : Long) -> TB) : String {
    //做数量判断
    if (checkOldCount && rankBean.ids!!.size.compareTo(this.count()) != 0) {
        return baseController.responseErrorForParams()
    }
    //更新排行数据
    val list = arrayListOf<TB>()
    this.findAllById(rankBean.ids!!.toMutableList()).forEachIndexed { index, tb ->
        list.add(getNewSaveTbFun(tb, FIRST_RANK_LIST + index))
    }
    this.saveAll(list)
    //返回成功信息
    return baseController.responseSuccess(null)
}


/**
 * 修改数据库操作扩展
 * @param zeroError 返回值为0是否算是异常
 * @param options 操作内容
 * @param successFun 成功方法
 * @param errorFun 异常方法
 *
 */
fun <CURD : CrudRepository<TB, ID>, ID, TB : SbcbflwBaseTb, R> CURD.sbcbflwModifyOptions(
        zeroError : Boolean, options : (CURD) -> Int, successFun : () -> R, errorFun : (Exception?) -> R) : R {
    return try {
        val result = options(this)
        if (result > 0) {
            successFun()
        } else if (result == 0) {
            if (zeroError) {
                errorFun(null)
            } else {
                successFun()
            }
        } else {
            errorFun(null)
        }
    } catch (e : java.lang.Exception) {
        errorFun(e)
    }
}

/**
 * 查找数据信息操作
 * @param options 操作逻辑
 * @param successFun 查找成功操作
 * @param errorFun 查找异常或者失败操作
 */
fun <CURD : CrudRepository<TB, ID>, ID, TB : SbcbflwBaseTb, R> CURD.sbcbflwFindTbInfoOptions(
        options : (CURD) -> TB?, successFun : (TB) -> R, errorFun : (Exception?) -> R) : R {
    return try {
        val result = options(this)
        if (result != null) {
            successFun(result)
        } else {
            errorFun(null)
        }
    } catch (e : java.lang.Exception) {
        errorFun(e)
    }
}
