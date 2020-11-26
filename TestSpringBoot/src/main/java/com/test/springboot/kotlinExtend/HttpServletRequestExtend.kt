package com.test.springboot.kotlinExtend

import com.test.springboot.database.table.UserInfoTb
import com.test.springboot.service.UserRolePermissionService
import com.test.springboot.service.UserService
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseController
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseHttpServletRequestWrapper
import javabase.lorenwang.common_base_frame.database.SbcbflwBaseTableConfig.CommonColumn.RANK
import javabase.lorenwang.common_base_frame.database.SbcbflwDatabaseParams.FIRST_RANK_LIST
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseTb
import javabase.lorenwang.common_base_frame.enums.SbcbflwBaseUserPermissionType
import javabase.lorenwang.common_base_frame.kotlinExtend.sbcbflwRemoveRank
import kotlinbase.lorenwang.tools.common.bean.KttlwBaseNetUpDateRankReqBean
import kotlinbase.lorenwang.tools.extend.isEmpty
import org.springframework.data.repository.CrudRepository
import java.math.BigInteger
import javax.persistence.EntityManager

/**
 * 功能作用：HttpServletRequest请求扩展
 * 创建时间：2019-10-29 下午 14:59:4
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

/**
 * 接口检测和操作
 * @param userService 用户服务
 * @param emptyCheckArray 要检测空的数组
 * @param checkPermissionArray 要检测的权限列表
 * @param checkPermission 是否检测权限
 * @param baseController 接口实例
 * @param notLoginFun 未登录操作
 * @param notPermissionFun 无权限操作
 * @param unKnownRepositoryOptionsFun 未知数据库操作
 * @param unKnownRepositoryOptionsErrorFun 未知数据库操作异常操作
 * @param unKnownOptionsFun 未知操作，一切都未知，只要做异常手机
 * @param unKnownOptionsErrorFun 未知操作异常操作
 * @return 处理结果字符串
 */
fun SbcbflwBaseHttpServletRequestWrapper.controllerCheckAndOptions(
        userService: UserService?,
        userRolePermissionService: UserRolePermissionService?,
        emptyCheckArray: Array<*>?,
        checkPermissionArray: Array<SbcbflwBaseUserPermissionType>?,
        checkPermission: Boolean,
        baseController: SbcbflwBaseController,
        notLoginFun: (() -> Any)?,
        notPermissionFun: ((userInfoTb: UserInfoTb) -> Any)?,
        unKnownRepositoryOptionsFun: ((userInfoTb: UserInfoTb) -> Any)?,
        unKnownRepositoryOptionsErrorFun: ((data: UserInfoTb?) -> Any)?,
        unKnownOptionsFun: (() -> Any?)?,
        unKnownOptionsErrorFun: (() -> Any?)?): String {
    return this.controllerCheckAndOptions(userService, userRolePermissionService, emptyCheckArray, checkPermissionArray, checkPermission, baseController, notLoginFun, notPermissionFun, unKnownRepositoryOptionsFun, unKnownRepositoryOptionsErrorFun, unKnownOptionsFun, unKnownOptionsErrorFun)
}


/**
 * 接口检测并操作扩展
 */
fun SbcbflwBaseHttpServletRequestWrapper.controllerCheckAndOptions(
        emptyCheckArray: Array<*>?,
        baseController: SbcbflwBaseController,
        unKnownOptionsFun: () -> Any,
        unKnownOptionsErrorFun: () -> Any): String {
    return this.controllerCheckAndOptions(emptyCheckArray,baseController, unKnownOptionsFun, unKnownOptionsErrorFun)
}

/**
 * 接口检测并操作扩展
 */
fun SbcbflwBaseHttpServletRequestWrapper.controllerCheckAndOptions(
        userService: UserService,
        userRolePermissionService: UserRolePermissionService?,
        emptyCheckArray: Array<*>?,
        checkPermissionArray: Array<SbcbflwBaseUserPermissionType>,
        baseController: SbcbflwBaseController,
        notLoginFun: (() -> Any)?,
        notPermissionFun: ((userInfoTb: UserInfoTb) -> Any)?,
        unKnownRepositoryOptionsFun: (userInfoTb: UserInfoTb) -> Any): String {
    return this.controllerCheckAndOptions(
            userService, userRolePermissionService, emptyCheckArray, checkPermissionArray, true,
            baseController, notLoginFun, notPermissionFun, unKnownRepositoryOptionsFun,
            null, null, null)
}

/**
 * 接口检测并操作扩展
 */
fun SbcbflwBaseHttpServletRequestWrapper.controllerCheckAndOptions(
        userService: UserService,
        userRolePermissionService: UserRolePermissionService?,
        emptyCheckArray: Array<*>?,
        checkPermissionArray: Array<SbcbflwBaseUserPermissionType>,
        baseController: SbcbflwBaseController,
        notLoginAndPermissionFun: () -> Any,
        unKnownRepositoryOptionsFun: (userInfoTb: UserInfoTb) -> Any): String {
    return this.controllerCheckAndOptions(
            userService, userRolePermissionService, emptyCheckArray, checkPermissionArray, true,
            baseController, notLoginAndPermissionFun, { notLoginAndPermissionFun() }, unKnownRepositoryOptionsFun,
            null, null, null)
}

/**
 * 接口检测并操作扩展
 */
fun SbcbflwBaseHttpServletRequestWrapper.controllerCheckAndOptions(
        userService: UserService,
        userRolePermissionService: UserRolePermissionService?,
        emptyCheckArray: Array<*>?,
        checkPermissionArray: Array<SbcbflwBaseUserPermissionType>,
        baseController: SbcbflwBaseController,
        unKnownRepositoryOptionsFun: (userInfoTb: UserInfoTb) -> Any): String {
    return this.controllerCheckAndOptions(
            userService, userRolePermissionService, emptyCheckArray, checkPermissionArray, true,
            baseController, null, null, unKnownRepositoryOptionsFun,
            null, null, null)
}


/**
 * 接口检测并操作扩展
 */
fun SbcbflwBaseHttpServletRequestWrapper.controllerCheckAndOptions(
        emptyCheckArray: Array<*>?,
        baseController: SbcbflwBaseController,
        unKnownOptionsFun: (() -> Any?)): String {
    return this.controllerCheckAndOptions(
            null, null, emptyCheckArray, null, false,
            baseController, null, null, null,
            null, unKnownOptionsFun, null)
}

/**
 * 接口检测并操作扩展
 */
fun SbcbflwBaseHttpServletRequestWrapper.controllerCheckAndOptions(
        baseController: SbcbflwBaseController,
        unKnownRepositoryOptionsFun: ((userInfoTb: UserInfoTb) -> Any)?,
        unKnownRepositoryOptionsErrorFun: ((data: UserInfoTb?) -> Any)?): String {
    return this.controllerCheckAndOptions(
            null, null, null, null, false,
            baseController, null, null, unKnownRepositoryOptionsFun,
            unKnownRepositoryOptionsErrorFun, null, null)
}

/**
 * 接口检测并操作扩展
 */
fun SbcbflwBaseHttpServletRequestWrapper.controllerCheckAndOptions(
        baseController: SbcbflwBaseController,
        unKnownOptionsFun: () -> Any): String {
    return this.controllerCheckAndOptions(
            null, null, null, null, false,
            baseController, null, null, null,
            null, unKnownOptionsFun, null)
}

/**
 * 删除表信息中的某一条数据
 */
fun <CURD : CrudRepository<TB, ID>, ID, TB : SbcbflwBaseTb> SbcbflwBaseHttpServletRequestWrapper.deleteTbInfo(
        userService: UserService,
        userRolePermissionService: UserRolePermissionService?,
        baseController: SbcbflwBaseController,
        curd: CURD, deleteId: ID?,
        entityManager: EntityManager,
        tableName: String, primaryKeyColumn: String): String {
    return this.deleteTbInfo(
            userService, userRolePermissionService, baseController, curd, deleteId, null,
            entityManager,
            tableName, primaryKeyColumn)
}

/**
 * 删除表信息中的某一条数据
 */
fun <CURD : CrudRepository<TB, ID>, ID, TB : SbcbflwBaseTb> SbcbflwBaseHttpServletRequestWrapper.deleteTbInfo(
        userService: UserService,
        userRolePermissionService: UserRolePermissionService?,
        baseController: SbcbflwBaseController,
        curd: CURD, deleteId: ID?,
        checkPermissionArray: Array<SbcbflwBaseUserPermissionType>?,
        entityManager: EntityManager,
        tableName: String, primaryKeyColumn: String): String {
    if (deleteId.isEmpty()) {
        return baseController.responseErrorForParams()
    }
    return checkPermissionArray.isNullOrEmpty().let {
        if (it) {
            this.controllerCheckAndOptions(
                    userService, userRolePermissionService, null, checkPermissionArray, true,
                    baseController, null, null,
                    {
                        deleteTableInfo(entityManager, tableName, primaryKeyColumn, deleteId, curd, baseController)
                    }, null, null, null)
        } else {
            this.controllerCheckAndOptions(baseController) {
                deleteTableInfo(entityManager, tableName, primaryKeyColumn, deleteId, curd, baseController)
            }
        }
    }
}

/**
 * 更新一个表中所有排行信息
 */
fun <CURD : CrudRepository<TB, ID>, ID, TB : SbcbflwBaseTb> SbcbflwBaseHttpServletRequestWrapper.upDataTbAllRank(
        baseController: SbcbflwBaseController,
        curd: CURD, rankBean: KttlwBaseNetUpDateRankReqBean<ID>,
        checkOldCount: Boolean,
        getNewSaveTbFun: (oldTbInfo: TB, firstRank: Long, newIds: Array<ID>) -> TB): String {
    return this.upDataTbAllRank(baseController, curd, rankBean,
            checkOldCount, getNewSaveTbFun)
}

/**
 * 更新一个表中所有排行信息
 */
fun <CURD : CrudRepository<TB, ID>, ID, TB : SbcbflwBaseTb> SbcbflwBaseHttpServletRequestWrapper.upDataTbAllRank(
        userService: UserService,
        userRolePermissionService: UserRolePermissionService?,
        baseController: SbcbflwBaseController,
        curd: CURD, rankBean: KttlwBaseNetUpDateRankReqBean<ID>,
        checkPermissionArray: Array<SbcbflwBaseUserPermissionType>?,
        checkOldCount: Boolean,
        getNewSaveTbFun: (oldTbInfo: TB, firstRank: Long, newIds: Array<ID>) -> TB): String {
    return checkPermissionArray.isNullOrEmpty().let {
        if (it) {
            this.controllerCheckAndOptions(
                    userService, userRolePermissionService, null, checkPermissionArray, true,
                    baseController, null, null,
                    {
                        upDateAllRank(checkOldCount, rankBean, curd, baseController, getNewSaveTbFun)
                    }, null, null, null)
        } else {
            this.controllerCheckAndOptions(baseController) {
                //需要进行数量判断，请求和数据库数量不一致则禁止更新
                return@controllerCheckAndOptions upDateAllRank(checkOldCount, rankBean, curd, baseController, getNewSaveTbFun)
            }
        }
    }
}

/**
 * 删除表信息
 * @param entityManager 数据库操作管理器
 * @param tableName 表名
 * @param primaryKeyColumn 字段名
 * @param deleteId 删除的主键id
 * @param curd 增加改成的数据表操作器
 * @param baseController 接口实例
 */
private fun <CURD : CrudRepository<TB, ID>, ID, TB : SbcbflwBaseTb> deleteTableInfo(
        entityManager: EntityManager, tableName: String, primaryKeyColumn: String, deleteId: ID?,
        curd: CURD, baseController: SbcbflwBaseController): String {
    val deleteTbInfo = entityManager.createNativeQuery("select $RANK from $tableName where $primaryKeyColumn=$deleteId").resultList
    curd.deleteById(deleteId!!)
    return if (curd.existsById(deleteId)) {
        baseController.responseDeleteFail()
    } else {
        //移动排行
        if (deleteTbInfo != null && deleteTbInfo.size == 1) {
            entityManager.sbcbflwRemoveRank(tableName, primaryKeyColumn,null, deleteTbInfo[0] as BigInteger)
        }
        baseController.responseSuccess(null)
    }
}

/**
 * 更新所有排行
 * @param checkOldCount
 * @param rankBean 更新数据实体
 * @param curd 数据库表操作
 * @param baseController 接口实例
 * @param getNewSaveTbFun 执行方法
 */
private fun <CURD : CrudRepository<TB, ID>, ID, TB : SbcbflwBaseTb> upDateAllRank(
        checkOldCount: Boolean, rankBean: KttlwBaseNetUpDateRankReqBean<ID>, curd: CURD,
        baseController: SbcbflwBaseController, getNewSaveTbFun: (oldTbInfo: TB, firstRank: Long, newIds: Array<ID>) -> TB): String {
    if (checkOldCount && rankBean.ids!!.size.compareTo(curd.count()) != 0) {
        return baseController.responseErrorForParams()
    }
    //更新排行数据
    curd.findAllById(rankBean.ids!!.toMutableList()).forEach {
        curd.save(getNewSaveTbFun(it, FIRST_RANK_LIST, rankBean.ids!!))
    }
    return baseController.responseSuccess(null)
}

/**
 * 权限检测
 * @param userInfo 用户信息
 * @param checkPermissionArray 权限检测列表
 * @param notPermissionFun 无权限操作
 * @param baseController 接口实例
 * @param unKnownRepositoryOptionsFun 未知数据库操作
 * @param unKnownRepositoryOptionsErrorFun 未知数据库异常操作
 */
fun SbcbflwBaseHttpServletRequestWrapper.checkPermissions(
        userRolePermissionService: UserRolePermissionService?,
        userInfo: UserInfoTb,
        checkPermissionArray: Array<SbcbflwBaseUserPermissionType>?,
        notPermissionFun: ((userInfoTb: UserInfoTb) -> Any)?,
        baseController: SbcbflwBaseController,
        unKnownRepositoryOptionsErrorFun: ((data: UserInfoTb?) -> Any)?,
        unKnownRepositoryOptionsFun: ((userInfoTb: UserInfoTb) -> Any)?): Any {
    return this.checkPermissions(userRolePermissionService, userInfo, checkPermissionArray, notPermissionFun, baseController, unKnownRepositoryOptionsErrorFun, unKnownRepositoryOptionsFun)
}
