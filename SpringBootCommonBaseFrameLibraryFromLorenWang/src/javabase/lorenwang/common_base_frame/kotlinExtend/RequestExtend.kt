package javabase.lorenwang.common_base_frame.kotlinExtend

import javabase.lorenwang.common_base_frame.SbcbflwCommon
import javabase.lorenwang.common_base_frame.controller.REQUEST_SET_USER_INFO_KEY
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseController
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseHttpServletRequestWrapper
import javabase.lorenwang.common_base_frame.database.repository.SbcbflwUserPermissionRepository
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserInfoTb
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserPermissionTb
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserRoleTb
import javabase.lorenwang.common_base_frame.enums.SbcbflwBaseUserPermissionType
import javabase.lorenwang.common_base_frame.service.SbcbflwUserPermissionService
import javabase.lorenwang.common_base_frame.utils.SbcbfBaseAllUtils
import kotlinbase.lorenwang.tools.extend.emptyCheck
import kotlinbase.lorenwang.tools.extend.formatConversion
import kotlinbase.lorenwang.tools.extend.getNotEmptyData
import kotlinbase.lorenwang.tools.extend.kttlwHaveEmptyCheck

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
inline fun <PT : SbcbflwBaseUserPermissionType, P : SbcbflwBaseUserPermissionTb<ROLE>, ROLE : SbcbflwBaseUserRoleTb<P>, reified U : SbcbflwBaseUserInfoTb<P, ROLE>>
        sbcbflwControllerCheckAndOptions(emptyCheckArray : Array<*>,
                                         checkPermissionArray : Array<PT>,
                                         request : SbcbflwBaseHttpServletRequestWrapper,
                                         baseController : SbcbflwBaseController,
                                         noinline notLoginFun : (() -> Any)? = null,
                                         noinline notPermissionFun : ((userInfoTb : U) -> Any)? = null,
                                         noinline optionsFun : (() -> Any?)? = null) : String {
    return kttlwHaveEmptyCheck({
        baseController.responseErrorForParams()
    }, {
        //检测用户是否登录
        var result = sbcbflwCheckUserLoginStatus<P, ROLE, U>(request, baseController, notLoginFun)
        if (result is U) {
            //用户已登录，检测用户权限
            result = sbcbflwCheckPermissions(request, result, checkPermissionArray, baseController, notPermissionFun)
            if (result is U && optionsFun != null) {
                //有权限，执行指定操作
                baseController.responseData(optionsFun())
            } else {
                baseController.responseData(result)
            }
        } else {
            baseController.responseData(result)
        }
    }, emptyCheckArray)
}

/**
 * 接口检测并操作扩展
 * @param emptyCheckArray 空数据检测
 * @param baseController 基础接口控制器
 * @param optionsFun 验证通过操作调用函数
 */
fun sbcbflwControllerCheckAndOptions(emptyCheckArray : Array<*>, baseController : SbcbflwBaseController, optionsFun : (() -> Any?)) : String {
    return kttlwHaveEmptyCheck({
        baseController.responseErrorForParams()
    }, {
        baseController.responseData(optionsFun())
    }, emptyCheckArray)
}

/**
 * 检测用户登录状态
 * @param request 请求request
 * @param baseController 基础controller
 * @param notLoginFun 未登陆执行操作
 * @return 检测通过返回用户信息，否则返回异常信息或其他函数信息
 */
inline fun <P : SbcbflwBaseUserPermissionTb<ROLE>, ROLE : SbcbflwBaseUserRoleTb<P>, reified U : SbcbflwBaseUserInfoTb<P, ROLE>>
        sbcbflwCheckUserLoginStatus(request : SbcbflwBaseHttpServletRequestWrapper, baseController : SbcbflwBaseController,
                                    noinline notLoginFun : (() -> Any?)? = null) : Any? {
    //从request中获取token信息
    val tokenByReqHeader = SbcbflwCommon.instance.userService?.getAccessTokenByReqHeader(request)
    //从request中获取用户信息，该信息是在filter中拦截并添加到attribute中的
    val userInfo = request.getAttribute(REQUEST_SET_USER_INFO_KEY).emptyCheck({
        null
    }, {
        it
    })
    return if (userInfo == null || userInfo !is U || tokenByReqHeader == null) {
        SbcbfBaseAllUtils.logUtils.logOptions(SbcbflwBaseController::class.java, "用户登录信息为空，登录状态验证未通过")
        try {
            notLoginFun.emptyCheck({
                baseController.responseErrorUserLoginEmptyOrTokenNoneffective()
            }, {
                it()
            })
        } catch (ignore : Exception) {
            baseController.responseErrorUserLoginEmptyOrTokenNoneffective()
        }
    } else {
        SbcbfBaseAllUtils.logUtils.logD(SbcbflwBaseController::class.java, "获取到用户信息，开始信息正确性验证")
        return tokenByReqHeader.let {
            //解密token信息
            val decryptToken = SbcbflwCommon.instance.userService?.decryptAccessToken(it)
            decryptToken.emptyCheck({
                SbcbfBaseAllUtils.logUtils.logOptions(SbcbflwBaseController::class.java, "用户信息无法解密，登录状态验证未通过")
                return baseController.responseErrorUserLoginEmptyOrTokenNoneffective()
            }, { deToken ->
                //判断token是否是正常的
                val tokenEffective = SbcbflwCommon.instance.userService?.checkAccessTokenEffective(deToken)
                if (tokenEffective?.statusResult.getNotEmptyData(false)) {
                    //token是正常的，开始验证用户信息，首先获取用户id
                    SbcbflwCommon.instance.userService?.getUserIdByAccessToken(deToken)!!.let { tokenUserId ->
                        //因为token已经验证过了，所以id是可以正常取值的
                        //接下来进行和用户信息的比较
                        val status = userInfo.userId.compareTo(tokenUserId) == 0
                        SbcbfBaseAllUtils.logUtils.logOptions(SbcbflwBaseController::class.java, "用户${userInfo.userId}验证结果：${status}")
                        if (status) {
                            userInfo
                        } else {
                            try {
                                notLoginFun.emptyCheck({
                                    baseController.responseErrorUserLoginEmptyOrTokenNoneffective()
                                }, {
                                    it()
                                })
                            } catch (ignore : Exception) {
                                baseController.responseErrorUserLoginEmptyOrTokenNoneffective()
                            }
                        }
                    }
                } else {
                    try {
                        notLoginFun.emptyCheck({
                            baseController.responseErrorUserLoginEmptyOrTokenNoneffective()
                        }, {
                            it()
                        })
                    } catch (ignore : Exception) {
                        baseController.responseErrorUserLoginEmptyOrTokenNoneffective()
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
fun <P : SbcbflwBaseUserPermissionTb<ROLE>, ROLE : SbcbflwBaseUserRoleTb<P>, U : SbcbflwBaseUserInfoTb<P, ROLE>, PT : SbcbflwBaseUserPermissionType,
        R : SbcbflwBaseHttpServletRequestWrapper, PR : SbcbflwUserPermissionRepository<P, ROLE>>
        sbcbflwCheckPermissions(request : R, userInfo : U, permissionCheckTypes : Array<PT>?, baseController : SbcbflwBaseController, notPermissionFun : ((userInfoTb : U) -> Any)? = null) : Any {
    SbcbfBaseAllUtils.logUtils.logOptions(SbcbflwBaseUserPermissionType::class.java, "用户${userInfo.userId}开始进行权限检测")
    for (permission in permissionCheckTypes!!) {
        if (!SbcbflwCommon.instance.userRolePermission?.formatConversion<SbcbflwUserPermissionService<R, P, ROLE, U, PT, PR>>()?.checkUserHavePermission(request, userInfo, permission)?.statusResult.getNotEmptyData(true)) {
            SbcbfBaseAllUtils.logUtils.logI(SbcbflwBaseUserPermissionType::class.java, "权限检测，用户${userInfo.userId}没有相关权限")
            return try {
                notPermissionFun.emptyCheck({
                    baseController.responseErrorUserLoginEmptyOrTokenNoneffective()
                }, {
                    it(userInfo)
                })
            } catch (ignore : Exception) {
                baseController.responseErrorNotPermission()
            }
        }
    }
    SbcbfBaseAllUtils.logUtils.logOptions(SbcbflwBaseUserPermissionType::class.java, "权限检测，用户${userInfo.userId}权限检测通过")
    return userInfo
}