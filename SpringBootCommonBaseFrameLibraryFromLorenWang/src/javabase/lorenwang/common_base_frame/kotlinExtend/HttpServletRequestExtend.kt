package javabase.lorenwang.common_base_frame.kotlinExtend

import javabase.lorenwang.common_base_frame.bean.SbcbflwBaseDataDisposeStatusBean
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseController
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseHttpServletRequestWrapper
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseRequestBean
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseUpDateRankReqBean
import javabase.lorenwang.common_base_frame.database.SbcbflwBaseTableConfig.CommonColumn.RANK
import javabase.lorenwang.common_base_frame.database.SbcbflwDatabaseParams.FIRST_RANK_LIST
import javabase.lorenwang.common_base_frame.database.helper.SbcbflwUserHelper
import javabase.lorenwang.common_base_frame.database.helper.SbcbflwUserRolePermissionHelper
import javabase.lorenwang.common_base_frame.database.repository.SbcbflwUserInfoRepository
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseTb
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserInfoTb
import javabase.lorenwang.common_base_frame.enums.SbcbflwBaseUserPermissionTypeEnum
import javabase.lorenwang.common_base_frame.safe.SbcbflwEncryptDecryptUtils
import javabase.lorenwang.tools.JtlwLogUtils
import kotlinbase.lorenwang.tools.extend.emptyCheck
import kotlinbase.lorenwang.tools.extend.haveEmptyCheck
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
 * 接口检测并操作扩展
 */
inline fun <P : SbcbflwBaseUserPermissionTypeEnum> SbcbflwBaseHttpServletRequestWrapper.controllerCheckAndOptions(
        emptyCheckArray: Array<*>?,
        checkPermissionArray: Array<P>,
        baseController: SbcbflwBaseController,
        noinline notLoginFun: (() -> Any)?,
        noinline notPermissionFun: ((userInfoTb: SbcbflwBaseUserInfoTb<*, *>) -> Any)?,
        crossinline unKnownRepositoryOptionsFun: (userInfoTb: SbcbflwBaseUserInfoTb<*, *>) -> Any,
        noinline unKnownRepositoryErrorFun: ((data: Any?) -> Any)?): String {

    //先判断数据是否有空数据,返回空检测状态
    val emptyCheckStatus = emptyCheckArray.emptyCheck({
        //空代表着不检查数据，继续执行
        true
    }, {
        //不为空则代表着检测开始
        this.haveEmptyCheck(it, { false }, { true })
    })

    val result = if (!emptyCheckStatus) {
        //空检测没有通过，有异常参数
        baseController.responseErrorForParams()
    } else {
        //空检测通过，进行下一步检测
        //获取token信息
        val tokenByReqHeader = SbcbflwUserHelper.baseInstance?.getAccessTokenByReqHeader(this)
        val userInfo = this.getAttribute(REQUEST_SET_USER_INFO_KEY).emptyCheck({
            null
        }, {
            it as SbcbflwBaseUserInfoTb<*, *>
        })
        JtlwLogUtils.logD(this::class.java, "用户${userInfo?.account}开始登录检测")
        //通过请求中的参数获取登录状态，因为在基础中已经做了登录拦截，所以这里仅仅对相应参数做比较，不再进行数据库查询
        val loginStatus = if (userInfo == null || tokenByReqHeader == null) {
            false
        } else {
            tokenByReqHeader.let {
                //解密token信息
                val decryptToken = SbcbflwEncryptDecryptUtils.instance.decrypt(it)
                decryptToken.emptyCheck({
                    false
                }, { deToken ->
                    //判断token是否是正常的
                    val tokenEffective = SbcbflwUserHelper.baseInstance?.checkAccessTokenEffective(deToken)
                    if (tokenEffective != null && tokenEffective.statusResult) {
                        //token是正常的，开始验证用户信息，首先获取用户id
                        SbcbflwUserHelper.baseInstance?.getUserIdByAccessToken(deToken)!!.let { tokenUserId ->
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

        //判断登录状态图
        if (loginStatus) {
            JtlwLogUtils.logI(this::class.java, "用户${userInfo?.account}登录检测通过")

            //权限检测状态，默认为通过
            var permissionCheckStatus = true

            //当前是登录状态，判断用户权限
            JtlwLogUtils.logD(this::class.java, "用户${userInfo?.account}开始权限检测")
            for (permission in checkPermissionArray) {
                if (SbcbflwUserRolePermissionHelper.baseInstance == null ||
                        !SbcbflwUserRolePermissionHelper.baseInstance?.checkUserHavePermission(this, userInfo!!, permission)?.statusResult!!) {
                    JtlwLogUtils.logI(this::class.java, "用户${userInfo?.account}没有相关权限")
                    permissionCheckStatus = false
                    break;
                }
            }

            if (!permissionCheckStatus) {
                JtlwLogUtils.logI(this::class.java, "用户${userInfo?.account}权限检测未通过")
                //有些权限该用户没有，判断是否要返回无权限结束
                if (notPermissionFun == null) {
                    baseController.responseErrorNotPermission()
                } else {
                    try {
                        notPermissionFun(userInfo!!)
                    } catch (e: Exception) {
                        //异常拦截，防止出现异常情况单独处理
                        if (unKnownRepositoryErrorFun == null) {
                            baseController.responseFailForUnKnow()
                        } else {
                            unKnownRepositoryErrorFun(null)
                        }
                    }
                }
            } else {
                JtlwLogUtils.logI(this::class.java, "用户${userInfo?.account}权限检测通过")

                //权限检测也通过了，开始进行其他操作
                try {
                    unKnownRepositoryOptionsFun(userInfo!!)
                } catch (e: Exception) {
                    if (unKnownRepositoryErrorFun == null) {
                        baseController.responseFailForUnKnow()
                    } else {
                        unKnownRepositoryErrorFun(null)
                    }
                }
            }
        } else {
            JtlwLogUtils.logI(this::class.java, "用户${userInfo?.account}登录检测未通过")
            //当前是非登录状态
            if (notLoginFun == null) {
                JtlwLogUtils.logI(this::class.java, "登录验证失败,用户未登录或者token失效")
                baseController.responseErrorUserLoginEmptyOrTokenNoneffective()
            } else {
                try {
                    //执行费登录操作
                    notLoginFun()
                } catch (e: Exception) {
                    //异常拦截，防止出现异常情况单独处理
                    if (unKnownRepositoryErrorFun == null) {
                        baseController.responseFailForUnKnow()
                    } else {
                        unKnownRepositoryErrorFun(null)
                    }
                }
            }
        }
    }


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

/**
 * 接口检测并操作扩展
 */
inline fun <DATA : SbcbflwBaseRequestBean> SbcbflwBaseHttpServletRequestWrapper.controllerCheckAndOptions(
        emptyCheckArray: Array<*>?,
        data: DATA?,
        baseController: SbcbflwBaseController,
        crossinline unKnownRepositoryOptionsFun: (data: DATA?) -> Any,
        noinline unKnownRepositoryErrorFun: ((data: Any?) -> Any)?): String {

    //先判断数据是否有空数据,返回空检测状态
    val emptyCheckStatus = emptyCheckArray.emptyCheck({
        //空代表着不检查数据，继续执行
        true
    }, {
        //不为空则代表着检测开始
        this.haveEmptyCheck(it, { false }, { true })
    })

    val result = if (!emptyCheckStatus) {
        //空检测没有通过，有异常参数
        baseController.responseErrorForParams()
    } else {
        //权限检测也通过了，开始进行其他操作
        try {
            unKnownRepositoryOptionsFun(data)
        } catch (e: Exception) {
            if (unKnownRepositoryErrorFun == null) {
                baseController.responseFailForUnKnow()
            } else {
                unKnownRepositoryErrorFun(null)
            }
        }
    }

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

/**
 * 接口检测并操作扩展
 */
inline fun <P : SbcbflwBaseUserPermissionTypeEnum> SbcbflwBaseHttpServletRequestWrapper.controllerCheckAndOptions(
        emptyCheckArray: Array<*>?,
        checkPermissionArray: Array<P>,
        baseController: SbcbflwBaseController,
        noinline notLoginFun: (() -> Any)?,
        noinline notPermissionFun: ((userInfoTb: SbcbflwBaseUserInfoTb<*, *>) -> Any)?,
        crossinline unKnownRepositoryOptionsFun: (userInfoTb: SbcbflwBaseUserInfoTb<*, *>) -> Any): String {
    return this.controllerCheckAndOptions(emptyCheckArray, checkPermissionArray, baseController,
            notLoginFun, notPermissionFun, unKnownRepositoryOptionsFun, null)
}

/**
 * 接口检测并操作扩展
 */
inline fun <P : SbcbflwBaseUserPermissionTypeEnum> SbcbflwBaseHttpServletRequestWrapper.controllerCheckAndOptions(
        emptyCheckArray: Array<*>?,
        checkPermissionArray: Array<P>,
        baseController: SbcbflwBaseController,
        noinline notLoginAndPermissionFun: () -> Any,
        crossinline unKnownRepositoryOptionsFun: (userInfoTb: SbcbflwBaseUserInfoTb<*, *>) -> Any): String {
    return this.controllerCheckAndOptions(emptyCheckArray, checkPermissionArray, baseController,
            { notLoginAndPermissionFun() }, { notLoginAndPermissionFun() }, unKnownRepositoryOptionsFun, null)
}

/**
 * 接口检测并操作扩展
 */
inline fun <P : SbcbflwBaseUserPermissionTypeEnum> SbcbflwBaseHttpServletRequestWrapper.controllerCheckAndOptions(
        emptyCheckArray: Array<*>?,
        checkPermissionArray: Array<P>,
        baseController: SbcbflwBaseController,
        crossinline unKnownRepositoryOptionsFun: (userInfoTb: SbcbflwBaseUserInfoTb<*, *>) -> Any): String {
    return this.controllerCheckAndOptions(emptyCheckArray, checkPermissionArray, baseController,
            null, null, unKnownRepositoryOptionsFun, null)
}


/**
 * 接口检测并操作扩展
 */
inline fun <DATA : SbcbflwBaseRequestBean> SbcbflwBaseHttpServletRequestWrapper.controllerCheckAndOptions(
        emptyCheckArray: Array<*>?,
        data: DATA?,
        baseController: SbcbflwBaseController,
        crossinline unKnownRepositoryOptionsFun: (data: DATA?) -> Any): String {
    return controllerCheckAndOptions(emptyCheckArray, data, baseController, unKnownRepositoryOptionsFun, null)
}

/**
 * 接口检测并操作扩展
 */
inline fun <DATA : SbcbflwBaseRequestBean> SbcbflwBaseHttpServletRequestWrapper.controllerCheckAndOptions(
        data: DATA?, baseController: SbcbflwBaseController,
        crossinline unKnownRepositoryOptionsFun: (data: DATA?) -> Any,
        noinline unKnownRepositoryErrorFun: ((data: Any?) -> Any)?): String {
    return this.controllerCheckAndOptions(null, data, baseController, unKnownRepositoryOptionsFun, unKnownRepositoryErrorFun)
}

/**
 * 接口检测并操作扩展
 */
inline fun <DATA : SbcbflwBaseRequestBean> SbcbflwBaseHttpServletRequestWrapper.controllerCheckAndOptions(
        baseController: SbcbflwBaseController,
        crossinline unKnownRepositoryOptionsFun: (data: DATA?) -> Any,
        noinline unKnownRepositoryErrorFun: ((data: Any?) -> Any)?): String {
    return this.controllerCheckAndOptions(null, null, baseController, unKnownRepositoryOptionsFun, unKnownRepositoryErrorFun)
}

/**
 * 接口检测并操作扩展
 */
inline fun SbcbflwBaseHttpServletRequestWrapper.controllerCheckAndOptions(
        baseController: SbcbflwBaseController,
        crossinline unKnownRepositoryOptionsFun: () -> Any): String {
    return this.controllerCheckAndOptions(null, baseController, {
        unKnownRepositoryOptionsFun()
    }, null)
}

/**
 * 删除表信息中的某一条数据
 */
fun <P : SbcbflwBaseUserPermissionTypeEnum, TB : SbcbflwBaseTb, CURD : CrudRepository<TB, Long>> SbcbflwBaseHttpServletRequestWrapper.deleteTbInfo(
        baseController: SbcbflwBaseController,
        curd: CURD, deleteId: Long?,
        checkPermissionArray: Array<P>,
        entityManager: EntityManager,
        tableName: String, primaryKeyColumn: String): String {
    return this.controllerCheckAndOptions(arrayOf(deleteId), checkPermissionArray, baseController) {
        val deleteTbInfo = entityManager.createNativeQuery("select $RANK from $tableName where $primaryKeyColumn=$deleteId").resultList
        curd.deleteById(deleteId!!)
        return@controllerCheckAndOptions if (curd.existsById(deleteId)) {
            baseController.responseDeleteFail()
        } else {
            //移动排行
            if (deleteTbInfo != null && deleteTbInfo.size == 1) {
                entityManager.removeRank(tableName, primaryKeyColumn, (deleteTbInfo[0] as BigInteger).toLong())
            }
            baseController.responseSuccess(null)
        }
    }
}

/**
 * 更新一个表中所有排行信息
 */
fun <P : SbcbflwBaseUserPermissionTypeEnum, TB : SbcbflwBaseTb, CURD : CrudRepository<TB, Long>> SbcbflwBaseHttpServletRequestWrapper.upDataTbAllRank(
        baseController: SbcbflwBaseController,
        curd: CURD, rankBean: SbcbflwBaseUpDateRankReqBean,
        checkPermissionArray: Array<P>,
        checkOldCount: Boolean,
        getNewSaveTbFun: (oldTbInfo: TB, firstRank: Long, newIds: Array<Long>) -> TB): String {
    return this.controllerCheckAndOptions(arrayOf(rankBean.ids), checkPermissionArray, baseController) {
        //需要进行数量判断，请求和数据库数量不一致则禁止更新
        if (checkOldCount && rankBean.ids!!.size.compareTo(curd.count()) != 0) {
            return@controllerCheckAndOptions baseController.responseErrorForParams()
        }
        //更新排行数据
        curd.findAllById(rankBean.ids!!.toMutableList()).forEach {
            curd.save(getNewSaveTbFun(it, FIRST_RANK_LIST, rankBean.ids!!))
        }
        return@controllerCheckAndOptions baseController.responseSuccess(null)
    }
}
