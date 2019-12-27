package javabase.lorenwang.common_base_frame

import Setting.REQUEST_SET_USER_INFO_KEY
import org.springframework.data.repository.CrudRepository
import service.qtoolsbaby.official.base.BaseController
import service.qtoolsbaby.official.base.BaseHttpServletRequestWrapper
import service.qtoolsbaby.official.bean.DataProcessingStatusBean
import service.qtoolsbaby.official.bean.request.UpDateRankReqBean
import service.qtoolsbaby.official.config.DatabaseParams.FIRST_RANK_LIST
import service.qtoolsbaby.official.config.NetParams
import service.qtoolsbaby.official.config.NetParams.ResponseStatus.STATUS_CODE_DELETE_DETAIL_FAIL
import service.qtoolsbaby.official.database.helper.UserHelper
import service.qtoolsbaby.official.database.helper.UserRolePermissionHelper
import service.qtoolsbaby.official.database.repository.UserInfoRepository
import service.qtoolsbaby.official.database.table.BaseTb
import service.qtoolsbaby.official.database.table.TableInfoConfig.CommonColumn.RANK
import service.qtoolsbaby.official.database.table.UserInfoTb
import service.qtoolsbaby.official.enums.UserPermissionTypeEnum
import service.qtoolsbaby.official.utils.EncryptDecryptUtils
import service.qtoolsbaby.official.utils.LogUtils
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
inline fun BaseHttpServletRequestWrapper.controllerCheckAndOptions(
        emptyCheckArray: Array<*>?,
        checkPermissionArray: Array<UserPermissionTypeEnum>,
        baseController: BaseController,
        userInfoRepository: UserInfoRepository,
        noinline notLoginFun: (() -> Any)?,
        noinline notPermissionFun: ((userInfoTb: UserInfoTb) -> Any)?,
        crossinline unKnownRepositoryOptionsFun: (userInfoTb: UserInfoTb) -> Any,
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
        val tokenByReqHeader = UserHelper.instance.getAccessTokenByReqHeader(this)
        val userInfo = this.getAttribute(REQUEST_SET_USER_INFO_KEY).emptyCheck({
            null
        }, {
            it as UserInfoTb
        })
        LogUtils.instance.logD(this::class.java, "用户${userInfo?.account}开始登录检测")
        //通过请求中的参数获取登录状态，因为在基础中已经做了登录拦截，所以这里仅仅对相应参数做比较，不再进行数据库查询
        val loginStatus = if (userInfo == null || tokenByReqHeader == null) {
            false
        } else {
            tokenByReqHeader.let {
                //解密token信息
                val decryptToken = EncryptDecryptUtils.instance.decrypt(it)
                decryptToken.emptyCheck({
                    false
                }, { deToken ->
                    //判断token是否是正常的
                    if (UserHelper.instance.checkAccessTokenEffective(deToken)) {
                        //token是正常的，开始验证用户信息，首先获取用户id
                        UserHelper.instance.getUserIdByAccessToken(deToken)!!.let { tokenUserId ->
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
            LogUtils.instance.logI(this::class.java, "用户${userInfo?.account}登录检测通过")

            //权限检测状态，默认为通过
            var permissionCheckStatus = true

            //当前是登录状态，判断用户权限
            LogUtils.instance.logD(this::class.java, "用户${userInfo?.account}开始权限检测")
            for (permission in checkPermissionArray) {
                if (!UserRolePermissionHelper.instance.checkUserHavePermission(this, userInfo!!, permission, userInfoRepository)) {
                    LogUtils.instance.logI(this::class.java, "用户${userInfo.account}没有相关权限")
                    permissionCheckStatus = false
                    break;
                }
            }

            if (!permissionCheckStatus) {
                LogUtils.instance.logI(this::class.java, "用户${userInfo?.account}权限检测未通过")
                //有些权限该用户没有，判断是否要返回无权限结束
                if (notPermissionFun == null) {
                    baseController.responseErrorForParams(NetParams.ResponseStatus.STATUS_CODE_USER_HAVE_NOT_PERMISSION, baseController.getMessage("net_request_status_code_user_have_not_permission", null))
                } else {
                    try {
                        notPermissionFun(userInfo!!)
                    } catch (e: Exception) {
                        //异常拦截，防止出现异常情况单独处理
                        if (unKnownRepositoryErrorFun == null) {
                            baseController.responseFailForUnKnow(null, null)
                        } else {
                            unKnownRepositoryErrorFun(null)
                        }
                    }
                }
            } else {
                LogUtils.instance.logI(this::class.java, "用户${userInfo?.account}权限检测通过")

                //权限检测也通过了，开始进行其他操作
                try {
                    unKnownRepositoryOptionsFun(userInfo!!)
                } catch (e: Exception) {
                    if (unKnownRepositoryErrorFun == null) {
                        baseController.responseFailForUnKnow(null, e.message)
                    } else {
                        unKnownRepositoryErrorFun(null)
                    }
                }
            }
        } else {
            LogUtils.instance.logI(this::class.java, "用户${userInfo?.account}登录检测未通过")
            //当前是非登录状态
            if (notLoginFun == null) {
                LogUtils.instance.logI(this::class.java, "登录验证失败,用户未登录或者token失效")
                baseController.responseErrorForParams(NetParams.ResponseStatus.STATUS_CODE_ERROR_USER_EMPTY_OR_TOKEN_NO_NEFFECTIVE, baseController.getMessage("net_request_status_code_error_user_empty_or_token_no_neffective", null))
            } else {
                try {
                    //执行费登录操作
                    notLoginFun()
                } catch (e: Exception) {
                    //异常拦截，防止出现异常情况单独处理
                    if (unKnownRepositoryErrorFun == null) {
                        baseController.responseFailForUnKnow(null, null)
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
        is DataProcessingStatusBean -> {
            if (!result.statusResult) {
                baseController.responseFailForUnKnow(result.statusCode, baseController.getMessage(result.statusMsgCode, null))
            } else {
                baseController.responseSuccess(result.body)
            }
        }
        //如果是字符串的话则直接返回
        is String -> {
            result
        }
        else -> {
            //如果是其他的则为未知错误
            baseController.responseFailForUnKnow(null, null)
        }
    }
}

/**
 * 接口检测并操作扩展
 */
inline fun <DATA> BaseHttpServletRequestWrapper.controllerCheckAndOptions(
        emptyCheckArray: Array<*>?,
        data: DATA?,
        baseController: BaseController,
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
                baseController.responseFailForUnKnow(null, e.message)
            } else {
                unKnownRepositoryErrorFun(null)
            }
        }
    }

    //返回值逻辑判断
    return when (result) {
        //如果是类型的话则按类型处理
        is DataProcessingStatusBean -> {
            if (!result.statusResult) {
                baseController.responseFailForUnKnow(result.statusCode, baseController.getMessage(result.statusMsgCode, null))
            } else {
                baseController.responseSuccess(result.body)
            }
        }
        //如果是字符串的话则直接返回
        is String -> {
            result
        }
        else -> {
            //如果是其他的则为未知错误
            baseController.responseFailForUnKnow(null, null)
        }
    }
}

/**
 * 接口检测并操作扩展
 */
inline fun BaseHttpServletRequestWrapper.controllerCheckAndOptions(
        emptyCheckArray: Array<*>?,
        checkPermissionArray: Array<UserPermissionTypeEnum>,
        baseController: BaseController,
        userInfoRepository: UserInfoRepository,
        noinline notLoginFun: (() -> Any)?,
        noinline notPermissionFun: ((userInfoTb: UserInfoTb) -> Any)?,
        crossinline unKnownRepositoryOptionsFun: (userInfoTb: UserInfoTb) -> Any): String {
    return this.controllerCheckAndOptions(emptyCheckArray, checkPermissionArray, baseController, userInfoRepository,
            notLoginFun, notPermissionFun, unKnownRepositoryOptionsFun, null)
}

/**
 * 接口检测并操作扩展
 */
inline fun BaseHttpServletRequestWrapper.controllerCheckAndOptions(
        emptyCheckArray: Array<*>?,
        checkPermissionArray: Array<UserPermissionTypeEnum>,
        baseController: BaseController,
        userInfoRepository: UserInfoRepository,
        noinline notLoginAndPermissionFun: () -> Any,
        crossinline unKnownRepositoryOptionsFun: (userInfoTb: UserInfoTb) -> Any): String {
    return this.controllerCheckAndOptions(emptyCheckArray, checkPermissionArray, baseController, userInfoRepository,
            { notLoginAndPermissionFun() }, { notLoginAndPermissionFun() }, unKnownRepositoryOptionsFun, null)
}

/**
 * 接口检测并操作扩展
 */
inline fun BaseHttpServletRequestWrapper.controllerCheckAndOptions(
        emptyCheckArray: Array<*>?,
        checkPermissionArray: Array<UserPermissionTypeEnum>,
        baseController: BaseController,
        userInfoRepository: UserInfoRepository,
        crossinline unKnownRepositoryOptionsFun: (userInfoTb: UserInfoTb) -> Any): String {
    return this.controllerCheckAndOptions(emptyCheckArray, checkPermissionArray, baseController, userInfoRepository,
            null, null, unKnownRepositoryOptionsFun, null)
}


/**
 * 接口检测并操作扩展
 */
inline fun <DATA> BaseHttpServletRequestWrapper.controllerCheckAndOptions(
        emptyCheckArray: Array<*>?,
        data: DATA?,
        baseController: BaseController,
        crossinline unKnownRepositoryOptionsFun: (data: DATA?) -> Any): String {
    return controllerCheckAndOptions(emptyCheckArray, data, baseController, unKnownRepositoryOptionsFun, null)
}

/**
 * 接口检测并操作扩展
 */
inline fun <DATA> BaseHttpServletRequestWrapper.controllerCheckAndOptions(
        data: DATA?, baseController: BaseController,
        crossinline unKnownRepositoryOptionsFun: (data: DATA?) -> Any,
        noinline unKnownRepositoryErrorFun: ((data: Any?) -> Any)?): String {
    return this.controllerCheckAndOptions(null, data, baseController, unKnownRepositoryOptionsFun, unKnownRepositoryErrorFun)
}

/**
 * 接口检测并操作扩展
 */
inline fun <DATA> BaseHttpServletRequestWrapper.controllerCheckAndOptions(
        baseController: BaseController,
        crossinline unKnownRepositoryOptionsFun: (data: DATA?) -> Any,
        noinline unKnownRepositoryErrorFun: ((data: Any?) -> Any)?): String {
    return this.controllerCheckAndOptions(null, null, baseController, unKnownRepositoryOptionsFun, unKnownRepositoryErrorFun)
}

/**
 * 接口检测并操作扩展
 */
inline fun BaseHttpServletRequestWrapper.controllerCheckAndOptions(
        baseController: BaseController,
        crossinline unKnownRepositoryOptionsFun: () -> Any): String {
    return this.controllerCheckAndOptions(null, null, baseController, {
        unKnownRepositoryOptionsFun()
    }, null)
}

/**
 * 删除表信息中的某一条数据
 */
fun <TB : BaseTb, CURD : CrudRepository<TB, Long>> BaseHttpServletRequestWrapper.deleteTbInfo(
        baseController: BaseController,
        curd: CURD, deleteId: Long?,
        checkPermissionArray: Array<UserPermissionTypeEnum>,
        userInfoRepository: UserInfoRepository, entityManager: EntityManager,
        tableName: String, primaryKeyColumn: String): String {
    return this.controllerCheckAndOptions(arrayOf(deleteId), checkPermissionArray, baseController, userInfoRepository) {
        val deleteTbInfo = entityManager.createNativeQuery("select $RANK from $tableName where $primaryKeyColumn=$deleteId").resultList
        curd.deleteById(deleteId!!)
        return@controllerCheckAndOptions if (curd.existsById(deleteId)) {
            baseController.responseFailForUnKnow(STATUS_CODE_DELETE_DETAIL_FAIL, baseController.getMessage("net_request_status_code_delete_detail_fail", null))
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
fun <TB : BaseTb, CURD : CrudRepository<TB, Long>> BaseHttpServletRequestWrapper.upDataTbAllRank(
        baseController: BaseController,
        curd: CURD, rankBean: UpDateRankReqBean,
        checkPermissionArray: Array<UserPermissionTypeEnum>,
        userInfoRepository: UserInfoRepository, checkOldCount: Boolean,
        getNewSaveTbFun: (oldTbInfo: TB, firstRank: Long, newIds: Array<Long>) -> TB): String {
    return this.controllerCheckAndOptions(arrayOf(rankBean.ids), checkPermissionArray, baseController, userInfoRepository) {
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
