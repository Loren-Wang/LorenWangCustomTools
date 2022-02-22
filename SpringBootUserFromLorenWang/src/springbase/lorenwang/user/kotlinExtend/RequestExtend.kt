package springbase.lorenwang.user.kotlinExtend

import kotlinbase.lorenwang.tools.extend.kttlwEmptyCheck
import kotlinbase.lorenwang.tools.extend.kttlwFormatConversion
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData
import kotlinbase.lorenwang.tools.extend.kttlwHaveEmptyCheck
import springbase.lorenwang.base.SpblwConfig
import springbase.lorenwang.base.controller.REQUEST_SET_USER_INFO_KEY
import springbase.lorenwang.base.controller.SpblwBaseController
import springbase.lorenwang.base.controller.SpblwBaseHttpServletRequestWrapper
import springbase.lorenwang.user.SpulwCommon
import springbase.lorenwang.user.database.repository.SpulwUserInfoRepository
import springbase.lorenwang.user.database.repository.SpulwUserPermissionRepository
import springbase.lorenwang.user.database.table.SpulwBaseUserInfoTb
import springbase.lorenwang.user.database.table.SpulwBaseUserPermissionTb
import springbase.lorenwang.user.database.table.SpulwBaseUserRoleTb
import springbase.lorenwang.user.service.SpulwUserPermissionService

/**
 * 功能作用：接口请求处理扩展
 * 创建时间：2020-11-21 10:49 下午
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
 * 接口检测并操作扩展
 * @param emptyCheckArray 空数据检测
 * @param baseController 基础接口控制器
 * @param optionsFun 验证通过操作调用函数
 */
inline fun <PT, P : SpulwBaseUserPermissionTb<ROLE>, ROLE : SpulwBaseUserRoleTb<P>, reified U : SpulwBaseUserInfoTb<P, ROLE>, R : SpblwBaseHttpServletRequestWrapper, BC : SpblwBaseController<R>> spulwControllerCheckAndOptions(
    emptyCheckArray: Array<*>, checkPermissionArray: Array<PT>, request: R, baseController: BC, decryptAccessTokenKey: String,
    decryptAccessTokenIvs: String, noinline notLoginFun: (() -> Any)? = null, noinline notPermissionFun: ((userInfoTb: U) -> Any)? = null,
    noinline optionsFun: (() -> Any?)? = null): String {
    return kttlwHaveEmptyCheck({
        baseController.responseErrorForParams(request)
    }, {
        //检测用户是否登录
        var result = spulwCheckUserLoginStatus<P, ROLE, U, R, BC>(request, baseController, decryptAccessTokenKey, decryptAccessTokenIvs, notLoginFun)
        if (result is U) {
            //用户已登录，检测用户权限
            result = spulwCheckPermissions(request, result, checkPermissionArray, baseController, notPermissionFun)
            if (result is U && optionsFun != null) {
                //有权限，执行指定操作
                baseController.responseData(request, optionsFun())
            } else {
                baseController.responseData(request, result)
            }
        } else {
            baseController.responseData(request, result)
        }
    }, emptyCheckArray)
}

/**
 * 接口检测并操作扩展
 * @param emptyCheckArray 空数据检测
 * @param baseController 基础接口控制器
 * @param optionsFun 验证通过操作调用函数
 */
fun <R : SpblwBaseHttpServletRequestWrapper, BC : SpblwBaseController<R>> spulwControllerCheckAndOptions(request: R, emptyCheckArray: Array<*>,
    baseController: BC, optionsFun: (() -> Any?)): String {
    return kttlwHaveEmptyCheck({
        baseController.responseErrorForParams(request)
    }, {
        baseController.responseData(request, optionsFun())
    }, emptyCheckArray)
}

/**
 * 检测用户登录状态
 * @param request 请求request
 * @param baseController 基础controller
 * @param notLoginFun 未登陆执行操作
 * @return 检测通过返回用户信息，否则返回异常信息或其他函数信息
 */
inline fun <P : SpulwBaseUserPermissionTb<ROLE>, ROLE : SpulwBaseUserRoleTb<P>, reified U : SpulwBaseUserInfoTb<P, ROLE>, R : SpblwBaseHttpServletRequestWrapper, BC : SpblwBaseController<R>> spulwCheckUserLoginStatus(
    request: R, baseController: BC, decryptAccessTokenKey: String, decryptAccessTokenIvs: String, noinline notLoginFun: (() -> Any?)? = null): Any? {
    //从request中获取token信息
    val tokenByReqHeader = SpulwCommon.instance.userService?.getAccessTokenByReqHeader(request)
    //从request中获取用户信息，该信息是在filter中拦截并添加到attribute中的
    val userInfo = request.getAttribute(REQUEST_SET_USER_INFO_KEY).kttlwEmptyCheck({
        null
    }, {
        it
    })
    return if (userInfo == null || userInfo !is U || tokenByReqHeader == null) {
        SpblwConfig.logUtils.logOptions(SpblwBaseController::class.java, "用户登录信息为空，登录状态验证未通过")
        try {
            notLoginFun.kttlwEmptyCheck({
                baseController.responseErrorUserLoginEmptyOrTokenNoneffective(request)
            }, {
                it()
            })
        } catch (ignore: Exception) {
            baseController.responseErrorUserLoginEmptyOrTokenNoneffective(request)
        }
    } else {
        SpblwConfig.logUtils.logD(SpblwBaseController::class.java, "获取到用户信息，开始信息正确性验证")
        return tokenByReqHeader.let {
            //解密token信息
            val decryptToken = SpulwCommon.instance.userService?.decryptAccessToken(decryptAccessTokenKey, decryptAccessTokenIvs, it)
            decryptToken.kttlwEmptyCheck({
                SpblwConfig.logUtils.logOptions(SpblwBaseController::class.java, "用户信息无法解密，登录状态验证未通过")
                return baseController.responseErrorUserLoginEmptyOrTokenNoneffective(request)
            }, { deToken ->
                //判断token是否是正常的
                val tokenEffective = SpulwCommon.instance.userService?.checkAccessTokenEffective(deToken)
                if (tokenEffective?.statusResult.kttlwGetNotEmptyData(false)) {
                    //token是正常的，开始验证用户信息，首先获取用户id
                    SpulwCommon.instance.userService?.getUserIdByAccessToken(deToken)!!.let { tokenUserId ->
                        //因为token已经验证过了，所以id是可以正常取值的
                        //接下来进行和用户信息的比较
                        val status = userInfo.userId.compareTo(tokenUserId) == 0
                        SpblwConfig.logUtils.logOptions(SpblwBaseController::class.java, "用户${userInfo.userId}验证结果：${status}")
                        if (status) {
                            userInfo
                        } else {
                            try {
                                notLoginFun.kttlwEmptyCheck({
                                    baseController.responseErrorUserLoginEmptyOrTokenNoneffective(request)
                                }, {
                                    it()
                                })
                            } catch (ignore: Exception) {
                                baseController.responseErrorUserLoginEmptyOrTokenNoneffective(request)
                            }
                        }
                    }
                } else {
                    try {
                        notLoginFun.kttlwEmptyCheck({
                            baseController.responseErrorUserLoginEmptyOrTokenNoneffective(request)
                        }, {
                            it()
                        })
                    } catch (ignore: Exception) {
                        baseController.responseErrorUserLoginEmptyOrTokenNoneffective(request)
                    }
                }
            })
        }
    }
}

/**
 * 检测用户权限
 * @param request 请求request
 * @param userInfo 用户信息
 * @param permissionCheckTypes 要检测的权限类型
 * @param baseController 基础controller
 * @param notPermissionFun 无权限操作
 * @return 检测通过返回用户信息，否则返回异常信息或其他函数信息
 */
fun <P : SpulwBaseUserPermissionTb<ROLE>, ROLE : SpulwBaseUserRoleTb<P>, U : SpulwBaseUserInfoTb<P, ROLE>, PT, R : SpblwBaseHttpServletRequestWrapper, PR : SpulwUserPermissionRepository<P, ROLE>, BC : SpblwBaseController<R>> spulwCheckPermissions(
    request: R, userInfo: U, permissionCheckTypes: Array<PT>?, baseController: BC, notPermissionFun: ((userInfoTb: U) -> Any)? = null): Any {
    SpblwConfig.logUtils.logOptions(SpulwUserInfoRepository::class.java, "用户${userInfo.userId}开始进行权限检测")
    for (permission in permissionCheckTypes!!) {
        if (!SpulwCommon.instance.userRolePermission?.kttlwFormatConversion<SpulwUserPermissionService<R, P, ROLE, U, PT, PR>>()
                ?.checkUserHavePermission(request, userInfo, permission)?.statusResult.kttlwGetNotEmptyData(true)
        ) {
            SpblwConfig.logUtils.logI(SpulwUserInfoRepository::class.java, "权限检测，用户${userInfo.userId}没有相关权限")
            return try {
                notPermissionFun.kttlwEmptyCheck({
                    baseController.responseErrorUserLoginEmptyOrTokenNoneffective(request)
                }, {
                    it(userInfo)
                })
            } catch (ignore: Exception) {
                baseController.responseErrorNotPermission(request)
            }
        }
    }
    SpblwConfig.logUtils.logOptions(SpulwUserInfoRepository::class.java, "权限检测，用户${userInfo.userId}权限检测通过")
    return userInfo
}
