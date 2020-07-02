package javabase.lorenwang.common_base_frame.kotlinExtend

import javabase.lorenwang.common_base_frame.bean.SbcbflwBaseDataDisposeStatusBean
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseController
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseHttpServletRequestWrapper
import javabase.lorenwang.common_base_frame.database.SbcbflwBaseTableConfig.CommonColumn.RANK
import javabase.lorenwang.common_base_frame.database.SbcbflwDatabaseParams.FIRST_RANK_LIST
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseTb
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserInfoTb
import javabase.lorenwang.common_base_frame.enums.SbcbflwBaseUserPermissionType
import javabase.lorenwang.common_base_frame.service.SbcbflwUserRolePermissionService
import javabase.lorenwang.common_base_frame.service.SbcbflwUserService
import javabase.lorenwang.tools.JtlwLogUtils
import kotlinbase.lorenwang.tools.common.bean.KttlwBaseNetUpDateRankReqBean
import kotlinbase.lorenwang.tools.extend.emptyCheck
import kotlinbase.lorenwang.tools.extend.haveEmptyCheck
import kotlinbase.lorenwang.tools.extend.isEmpty
import org.springframework.data.repository.CrudRepository
import service.qtoolsbaby.official.database.table.SbcbflwBaseUserPermissionTb
import service.qtoolsbaby.official.database.table.SbcbflwBaseUserRoleTb
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
fun <US : SbcbflwUserService, R : SbcbflwBaseHttpServletRequestWrapper, P : SbcbflwBaseUserPermissionTb<ROLE>,
        ROLE : SbcbflwBaseUserRoleTb<P>, U : SbcbflwBaseUserInfoTb<P, ROLE>, PT : SbcbflwBaseUserPermissionType,
        U_RP_S : SbcbflwUserRolePermissionService<R, P, ROLE, U, PT>> R.controllerCheckAndOptions(
        userService: US?,
        userRolePermissionService: U_RP_S?,
        emptyCheckArray: Array<*>?,
        checkPermissionArray: Array<PT>?,
        checkPermission: Boolean,
        baseController: SbcbflwBaseController,
        notLoginFun: (() -> Any)?,
        notPermissionFun: ((userInfoTb: U) -> Any)?,
        unKnownRepositoryOptionsFun: ((userInfoTb: U) -> Any)?,
        unKnownRepositoryOptionsErrorFun: ((data: U?) -> Any)?,
        unKnownOptionsFun: (() -> Any?)?,
        unKnownOptionsErrorFun: (() -> Any?)?): String {
    //空数据检测
    emptyCheckArray.emptyCheck({
        //空代表着不检查数据，继续执行
        true
    }, {
        //不为空则代表着检测开始
        this.haveEmptyCheck({ false }, { true }, it)
    }).let { emptyCheckStatus ->
        if (!emptyCheckStatus) {
            //空检测没有通过，有异常参数
            return@let baseController.responseErrorForParams()
        } else {
            //开始权限检测，如果需要检测权限或者要被检测的权限不为空
            if (checkPermission || !checkPermissionArray.isNullOrEmpty()) {
                JtlwLogUtils.logD(this::class.java, "开始权限检测，首先检测登录信息")
                //检测权限先获取token信息
                val tokenByReqHeader = userService?.getAccessTokenByReqHeader(this)
                //获取用户信息
                val userInfo = this.getAttribute(REQUEST_SET_USER_INFO_KEY).emptyCheck({
                    null
                }, {
                    it as U
                })
                JtlwLogUtils.logD(this::class.java, "用户${userInfo?.account}登录信息获取成功，开始检测用户信息")
                //通过请求中的参数获取登录状态，因为在基础中已经做了登录拦截，所以这里仅仅对相应参数做比较，不再进行数据库查询
                val loginStatus = if (userInfo == null || tokenByReqHeader == null) {
                    false
                } else {
                    tokenByReqHeader.let {
                        //解密token信息
                        val decryptToken = userService.decryptAccessToken(it)
                        decryptToken.emptyCheck({
                            false
                        }, { deToken ->
                            //判断token是否是正常的
                            val tokenEffective = userService.checkAccessTokenEffective(deToken)
                            if (tokenEffective.statusResult) {
                                //token是正常的，开始验证用户信息，首先获取用户id
                                userService.getUserIdByAccessToken(deToken)!!.let { tokenUserId ->
                                    //因为token已经验证过了，所以id是可以正常取值的
                                    //接下来进行和用户信息的比较
                                    userInfo.userId?.compareTo(tokenUserId) == 0
                                }
                            } else {
                                false
                            }
                        })
                    }
                }
                //开始处理登录状态
                if (loginStatus) {
                    //权限检测并返回数据
                    checkPermissions(userRolePermissionService,
                            userInfo!!, checkPermissionArray, notPermissionFun,
                            baseController, unKnownRepositoryOptionsErrorFun, unKnownRepositoryOptionsFun)
                } else {
                    JtlwLogUtils.logI(this::class.java, "用户${userInfo?.account}登录检测未通过")
                    //当前是非登录状态
                    if (notLoginFun == null) {
                        JtlwLogUtils.logI(this::class.java, "登录验证失败,用户未登录或者token失效")
                        baseController.responseErrorUserLoginEmptyOrTokenNoneffective()
                    } else {
                        try {
                            //执行费登录操作
                            notLoginFun.emptyCheck({
                                baseController.responseFailForUnKnow()
                            }, {
                                it()
                            })
                        } catch (e: Exception) {
                            //异常拦截，防止出现异常情况单独处理
                            unKnownRepositoryOptionsErrorFun.emptyCheck({
                                baseController.responseFailForUnKnow()
                            }, {
                                it(null)
                            })
                        }
                    }
                }
            } else {
                JtlwLogUtils.logD(this::class.java, "不进行权限检测，开始进行数据处理")
                //权限检测也通过了，开始进行其他操作
                try {
                    unKnownOptionsFun.emptyCheck({
                        baseController.responseFailForUnKnow()
                    }, {
                        it()
                    })
                } catch (e: Exception) {
                    unKnownOptionsErrorFun.emptyCheck({
                        baseController.responseFailForUnKnow()
                    }, {
                        it()
                    })
                }
            }
        }
    }.also { result ->
        //返回值逻辑判断
        return when (result) {
            //如果是类型的话则按类型处理
            is SbcbflwBaseDataDisposeStatusBean -> {
                baseController.responseDataDisposeStatus(result)
            }
            //如果是字符串的话则直接返回
            is String -> {
                result
            }
            else -> {
                //如果是其他的则为未知错误
                baseController.responseFailForUnKnow()
            }
        }
    }
}


/**
 * 接口检测并操作扩展
 */
fun <US : SbcbflwUserService, R : SbcbflwBaseHttpServletRequestWrapper, P : SbcbflwBaseUserPermissionTb<ROLE>,
        ROLE : SbcbflwBaseUserRoleTb<P>, U : SbcbflwBaseUserInfoTb<P, ROLE>, PT : SbcbflwBaseUserPermissionType,
        U_RP_S : SbcbflwUserRolePermissionService<R, P, ROLE, U, PT>> R.controllerCheckAndOptions(
        emptyCheckArray: Array<*>?,
        baseController: SbcbflwBaseController,
        unKnownOptionsFun: () -> Any,
        unKnownOptionsErrorFun: () -> Any): String {
    return this.controllerCheckAndOptions<US, R, P, ROLE, U, PT, U_RP_S>(
            null, null, emptyCheckArray, null, false,
            baseController, null, null, null,
            null, unKnownOptionsFun, unKnownOptionsErrorFun)
}

/**
 * 接口检测并操作扩展
 */
fun <US : SbcbflwUserService, R : SbcbflwBaseHttpServletRequestWrapper, P : SbcbflwBaseUserPermissionTb<ROLE>,
        ROLE : SbcbflwBaseUserRoleTb<P>, U : SbcbflwBaseUserInfoTb<P, ROLE>, PT : SbcbflwBaseUserPermissionType,
        U_RP_S : SbcbflwUserRolePermissionService<R, P, ROLE, U, PT>> R.controllerCheckAndOptions(
        userService: US,
        userRolePermissionService: U_RP_S?,
        emptyCheckArray: Array<*>?,
        checkPermissionArray: Array<PT>,
        baseController: SbcbflwBaseController,
        notLoginFun: (() -> Any)?,
        notPermissionFun: ((userInfoTb: U) -> Any)?,
        unKnownRepositoryOptionsFun: (userInfoTb: U) -> Any): String {
    return this.controllerCheckAndOptions(
            userService, userRolePermissionService, emptyCheckArray, checkPermissionArray, true,
            baseController, notLoginFun, notPermissionFun, unKnownRepositoryOptionsFun,
            null, null, null)
}

/**
 * 接口检测并操作扩展
 */
fun <US : SbcbflwUserService, R : SbcbflwBaseHttpServletRequestWrapper, P : SbcbflwBaseUserPermissionTb<ROLE>,
        ROLE : SbcbflwBaseUserRoleTb<P>, U : SbcbflwBaseUserInfoTb<P, ROLE>, PT : SbcbflwBaseUserPermissionType,
        U_RP_S : SbcbflwUserRolePermissionService<R, P, ROLE, U, PT>> R.controllerCheckAndOptions(
        userService: US,
        userRolePermissionService: U_RP_S?,
        emptyCheckArray: Array<*>?,
        checkPermissionArray: Array<PT>,
        baseController: SbcbflwBaseController,
        notLoginAndPermissionFun: () -> Any,
        unKnownRepositoryOptionsFun: (userInfoTb: U) -> Any): String {
    return this.controllerCheckAndOptions(
            userService, userRolePermissionService, emptyCheckArray, checkPermissionArray, true,
            baseController, notLoginAndPermissionFun, { notLoginAndPermissionFun() }, unKnownRepositoryOptionsFun,
            null, null, null)
}

/**
 * 接口检测并操作扩展
 */
fun <US : SbcbflwUserService, R : SbcbflwBaseHttpServletRequestWrapper, P : SbcbflwBaseUserPermissionTb<ROLE>,
        ROLE : SbcbflwBaseUserRoleTb<P>, U : SbcbflwBaseUserInfoTb<P, ROLE>, PT : SbcbflwBaseUserPermissionType,
        U_RP_S : SbcbflwUserRolePermissionService<R, P, ROLE, U, PT>> R.controllerCheckAndOptions(
        userService: US,
        userRolePermissionService: U_RP_S?,
        emptyCheckArray: Array<*>?,
        checkPermissionArray: Array<PT>,
        baseController: SbcbflwBaseController,
        unKnownRepositoryOptionsFun: (userInfoTb: U) -> Any): String {
    return this.controllerCheckAndOptions(
            userService, userRolePermissionService, emptyCheckArray, checkPermissionArray, true,
            baseController, null, null, unKnownRepositoryOptionsFun,
            null, null, null)
}


/**
 * 接口检测并操作扩展
 */
fun <US : SbcbflwUserService, R : SbcbflwBaseHttpServletRequestWrapper, P : SbcbflwBaseUserPermissionTb<ROLE>,
        ROLE : SbcbflwBaseUserRoleTb<P>, U : SbcbflwBaseUserInfoTb<P, ROLE>, PT : SbcbflwBaseUserPermissionType,
        U_RP_S : SbcbflwUserRolePermissionService<R, P, ROLE, U, PT>> R.controllerCheckAndOptions(
        emptyCheckArray: Array<*>?,
        baseController: SbcbflwBaseController,
        unKnownOptionsFun: (() -> Any?)): String {
    return this.controllerCheckAndOptions<US, R, P, ROLE, U, PT, U_RP_S>(
            null, null, emptyCheckArray, null, false,
            baseController, null, null, null,
            null, unKnownOptionsFun, null)
}

/**
 * 接口检测并操作扩展
 */
fun <US : SbcbflwUserService, R : SbcbflwBaseHttpServletRequestWrapper, P : SbcbflwBaseUserPermissionTb<ROLE>,
        ROLE : SbcbflwBaseUserRoleTb<P>, U : SbcbflwBaseUserInfoTb<P, ROLE>, PT : SbcbflwBaseUserPermissionType,
        U_RP_S : SbcbflwUserRolePermissionService<R, P, ROLE, U, PT>> R.controllerCheckAndOptions(
        baseController: SbcbflwBaseController,
        unKnownRepositoryOptionsFun: ((userInfoTb: U) -> Any)?,
        unKnownRepositoryOptionsErrorFun: ((data: U?) -> Any)?): String {
    return this.controllerCheckAndOptions<US, R, P, ROLE, U, PT, U_RP_S>(
            null, null, null, null, false,
            baseController, null, null, unKnownRepositoryOptionsFun,
            unKnownRepositoryOptionsErrorFun, null, null)
}

/**
 * 接口检测并操作扩展
 */
fun <US : SbcbflwUserService, R : SbcbflwBaseHttpServletRequestWrapper, P : SbcbflwBaseUserPermissionTb<ROLE>,
        ROLE : SbcbflwBaseUserRoleTb<P>, U : SbcbflwBaseUserInfoTb<P, ROLE>, PT : SbcbflwBaseUserPermissionType,
        U_RP_S : SbcbflwUserRolePermissionService<R, P, ROLE, U, PT>> R.controllerCheckAndOptions(
        baseController: SbcbflwBaseController,
        unKnownOptionsFun: () -> Any): String {
    return this.controllerCheckAndOptions<US, R, P, ROLE, U, PT, U_RP_S>(
            null, null, null, null, false,
            baseController, null, null, null,
            null, unKnownOptionsFun, null)
}

/**
 * 删除表信息中的某一条数据
 */
fun <US : SbcbflwUserService, R : SbcbflwBaseHttpServletRequestWrapper, P : SbcbflwBaseUserPermissionTb<ROLE>,
        ROLE : SbcbflwBaseUserRoleTb<P>, U : SbcbflwBaseUserInfoTb<P, ROLE>, PT : SbcbflwBaseUserPermissionType,
        U_RP_S : SbcbflwUserRolePermissionService<R, P, ROLE, U, PT>,
        TB : SbcbflwBaseTb, ID, CURD : CrudRepository<TB, ID>> R.deleteTbInfo(
        userService: US,
        userRolePermissionService: U_RP_S?,
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
fun <US : SbcbflwUserService, R : SbcbflwBaseHttpServletRequestWrapper, P : SbcbflwBaseUserPermissionTb<ROLE>,
        ROLE : SbcbflwBaseUserRoleTb<P>, U : SbcbflwBaseUserInfoTb<P, ROLE>, PT : SbcbflwBaseUserPermissionType,
        U_RP_S : SbcbflwUserRolePermissionService<R, P, ROLE, U, PT>,
        TB : SbcbflwBaseTb, ID, CURD : CrudRepository<TB, ID>> R.deleteTbInfo(
        userService: US,
        userRolePermissionService: U_RP_S?,
        baseController: SbcbflwBaseController,
        curd: CURD, deleteId: ID?,
        checkPermissionArray: Array<PT>?,
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
            this.controllerCheckAndOptions<US, R, P, ROLE, U, PT, U_RP_S>(baseController) {
                deleteTableInfo(entityManager, tableName, primaryKeyColumn, deleteId, curd, baseController)
            }
        }
    }
}

/**
 * 更新一个表中所有排行信息
 */
fun <US : SbcbflwUserService, R : SbcbflwBaseHttpServletRequestWrapper, P : SbcbflwBaseUserPermissionTb<ROLE>,
        ROLE : SbcbflwBaseUserRoleTb<P>, U : SbcbflwBaseUserInfoTb<P, ROLE>, PT : SbcbflwBaseUserPermissionType,
        U_RP_S : SbcbflwUserRolePermissionService<R, P, ROLE, U, PT>,
        TB : SbcbflwBaseTb, ID, CURD : CrudRepository<TB, ID>> R.upDataTbAllRank(
        baseController: SbcbflwBaseController,
        curd: CURD, rankBean: KttlwBaseNetUpDateRankReqBean<ID>,
        checkOldCount: Boolean,
        getNewSaveTbFun: (oldTbInfo: TB, firstRank: Long, newIds: Array<ID>) -> TB): String {
    return this.upDataTbAllRank<US, R, P, ROLE, U, PT, U_RP_S, TB, ID, CURD>(baseController, curd, rankBean,
            checkOldCount, getNewSaveTbFun)
}

/**
 * 更新一个表中所有排行信息
 */
fun <US : SbcbflwUserService, R : SbcbflwBaseHttpServletRequestWrapper, P : SbcbflwBaseUserPermissionTb<ROLE>,
        ROLE : SbcbflwBaseUserRoleTb<P>, U : SbcbflwBaseUserInfoTb<P, ROLE>, PT : SbcbflwBaseUserPermissionType,
        U_RP_S : SbcbflwUserRolePermissionService<R, P, ROLE, U, PT>,
        TB : SbcbflwBaseTb, ID, CURD : CrudRepository<TB, ID>> R.upDataTbAllRank(
        userService: US,
        userRolePermissionService: U_RP_S?,
        baseController: SbcbflwBaseController,
        curd: CURD, rankBean: KttlwBaseNetUpDateRankReqBean<ID>,
        checkPermissionArray: Array<PT>?,
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
            this.controllerCheckAndOptions<US, R, P, ROLE, U, PT, U_RP_S>(baseController) {
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
            entityManager.removeRank(tableName, primaryKeyColumn, (deleteTbInfo[0] as BigInteger).toLong())
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
fun <R : SbcbflwBaseHttpServletRequestWrapper, P : SbcbflwBaseUserPermissionTb<ROLE>,
        ROLE : SbcbflwBaseUserRoleTb<P>, U : SbcbflwBaseUserInfoTb<P, ROLE>, PT : SbcbflwBaseUserPermissionType,
        U_RP_S : SbcbflwUserRolePermissionService<R, P, ROLE, U, PT>> R.checkPermissions(
        userRolePermissionService: U_RP_S?,
        userInfo: U,
        checkPermissionArray: Array<PT>?,
        notPermissionFun: ((userInfoTb: U) -> Any)?,
        baseController: SbcbflwBaseController,
        unKnownRepositoryOptionsErrorFun: ((data: U?) -> Any)?,
        unKnownRepositoryOptionsFun: ((userInfoTb: U) -> Any)?): Any {
    JtlwLogUtils.logI(this::class.java, "用户${userInfo.account}权限检测，登录信息状态处理成功")
    //开始检测权限
    //权限检测状态，默认为通过
    var permissionCheckStatus = true

    //当前是登录状态，判断用户权限
    JtlwLogUtils.logD(this::class.java, "用户${userInfo.account}权限检测，开始权限检测")
    for (permission in checkPermissionArray!!) {
        if (userRolePermissionService == null ||
                userRolePermissionService.checkUserHavePermission(this,
                        userInfo, permission)
                        .statusResult) {
            JtlwLogUtils.logI(this::class.java, "权限检测，用户${userInfo.account}没有相关权限")
            permissionCheckStatus = false
            break
        }
    }
    //权限检测结果处理
    return if (!permissionCheckStatus) {
        JtlwLogUtils.logI(this::class.java, "权限检测，用户${userInfo.account}权限检测未通过")
        //有些权限该用户没有，判断是否要返回无权限结束
        try {
            notPermissionFun.emptyCheck({
                baseController.responseErrorNotPermission()
            }, {
                it(userInfo)
            })
        } catch (e: Exception) {
            //异常拦截，防止出现异常情况单独处理
            unKnownRepositoryOptionsErrorFun.emptyCheck({
                baseController.responseFailForUnKnow()
            }, {
                it(null)
            })
        }
    } else {
        JtlwLogUtils.logI(this::class.java, "权限检测，用户${userInfo.account}权限检测通过")

        //权限检测也通过了，开始进行其他操作
        try {
            unKnownRepositoryOptionsFun.emptyCheck({
                baseController.responseFailForUnKnow()
            }, {
                it(userInfo)
            })
        } catch (e: Exception) {
            if (unKnownRepositoryOptionsErrorFun == null) {
                baseController.responseFailForUnKnow()
            } else {
                unKnownRepositoryOptionsErrorFun(null)
            }
        }
    }
}
