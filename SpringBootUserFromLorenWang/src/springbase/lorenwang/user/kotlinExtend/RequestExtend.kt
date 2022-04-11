package springbase.lorenwang.user.kotlinExtend

import kotlinbase.lorenwang.tools.extend.kttlwEmptyCheck
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData
import kotlinbase.lorenwang.tools.extend.kttlwHaveEmptyCheck
import org.springframework.boot.util.LambdaSafe.callback
import springbase.lorenwang.base.controller.SpblwBaseController
import springbase.lorenwang.base.controller.SpblwBaseHttpServletRequestWrapper
import springbase.lorenwang.base.spblwConfig
import springbase.lorenwang.user.database.table.SpulwBaseUserInfoTb
import springbase.lorenwang.user.database.table.SpulwRolePermissionTb
import springbase.lorenwang.user.service.SpulwRolePermissionService
import springbase.lorenwang.user.service.SpulwUserService.Companion.REQUEST_SET_USER_INFO_KEY
import springbase.lorenwang.user.spulwConfig

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
fun <R : SpblwBaseHttpServletRequestWrapper, U : SpulwBaseUserInfoTb, BC : SpblwBaseController<R>> spulwControllerCheckAndOptions(
    emptyCheckArray: Array<*>, checkPermissionTypes: List<Int>, request: R, baseController: BC, notLoginFun: (() -> Any)? = null,
    notPermissionFun: ((userInfoTb: U) -> Any)? = null, optionsFun: (() -> Any?)? = null): String {
    return kttlwHaveEmptyCheck({
        baseController.responseErrorForParams(request)
    }, {
        //检测用户是否登录
        val result = spulwCheckLoginStatus<R, U, BC>(baseController, request, notLoginFun)
        return if (result is SpulwBaseUserInfoTb) {
            //用户已登录，检测用户权限
            if (spulwCheckPermission(request, checkPermissionTypes, result as U)) {
                try {
                    if (optionsFun != null) {
                        //有权限，执行指定操作
                        baseController.responseData(request, optionsFun())
                    } else {
                        baseController.responseData(request, result)
                    }
                } catch (e: Exception) {
                    baseController.responseData(request, result)
                }
            } else {
                try {
                    if (notPermissionFun != null) {
                        baseController.responseData(request, notPermissionFun(result))
                    } else {
                        baseController.responseErrorNotPermission(request)
                    }
                } catch (e: Exception) {
                    baseController.responseErrorNotPermission(request)
                }
            }
        } else {
            baseController.responseData(request, result)
        }
    }, emptyCheckArray)
}

/**
 * 检测登录状态
 * @param baseController 基础controller
 * @param request 请求体
 * @param notLoginFun 未登录处理函数
 */
fun <R : SpblwBaseHttpServletRequestWrapper, U : SpulwBaseUserInfoTb, BC : SpblwBaseController<R>> spulwCheckLoginStatus(baseController: BC,
    request: R, notLoginFun: (() -> Any?)? = null): Any? {
    val userInfo = spulwGetUserInfo<R, U>(request)
    return if (userInfo == null || userInfo.accessToken.isNullOrEmpty()) {
        spblwConfig.getLogUtil().logOptions(SpblwBaseController::class.java, "用户登录信息为空，登录状态验证未通过")
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
        spblwConfig.getLogUtil().logD(SpblwBaseController::class.java, "获取到用户信息，开始信息正确性验证")
        userInfo.accessToken?.let {
            //解密token信息
            val decryptToken = spulwConfig.getUserServices().decryptAccessToken(it)
            decryptToken.kttlwEmptyCheck({
                spblwConfig.getLogUtil().logOptions(SpblwBaseController::class.java, "用户信息无法解密，登录状态验证未通过")
                baseController.responseErrorUserLoginEmptyOrTokenNoneffective(request)
            }, { deToken ->
                //判断token是否是正常的
                val tokenEffective = spulwConfig.getUserServices().checkAccessTokenEffective(deToken)
                if (tokenEffective.statusResult.kttlwGetNotEmptyData(false)) {
                    //token是正常的，开始验证用户信息，首先获取用户id
                    spulwConfig.getUserServices().getUserIdByAccessToken(deToken)!!.let { tokenUserId ->
                        //因为token已经验证过了，所以id是可以正常取值的
                        //接下来进行和用户信息的比较
                        val status = userInfo.userId.compareTo(tokenUserId) == 0
                        spblwConfig.getLogUtil().logOptions(SpblwBaseController::class.java, "用户${userInfo.userId}验证结果：${status}")
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
 * 权限检测
 * @param request 请求体
 * @param permissions 要检测的权限id列表
 * @param callback 回调
 */
fun <R : SpblwBaseHttpServletRequestWrapper, U : SpulwBaseUserInfoTb> spulwCheckPermission(request: R, permissions: List<Int>,
    userInfoTb: U? = null): Boolean {
    //获取用户权限
    val rolePermissions = spulwGetUserPermissions(request, userInfoTb)
    //权限遍历
    var havePermissionCount = 0
    permissions.forEachIndexed { index, permission ->
        for (rolePermission in rolePermissions) {
            if (permission == rolePermission.permission) {
                havePermissionCount += 1
                break
            }
        }
        if (havePermissionCount != index + 1) {
            return@forEachIndexed
        }
    }
    //是否有权限判定
    return havePermissionCount > 0 && permissions.size == havePermissionCount
}

/**
 * 获取用户信息
 * @param request 请求体
 */
fun <R : SpblwBaseHttpServletRequestWrapper, U : SpulwBaseUserInfoTb> spulwGetUserInfo(request: R, userInfoTb: U? = null): U? {
    //从request中获取用户信息，该信息是在filter中拦截并添加到attribute中的
    return userInfoTb?.kttlwGetNotEmptyData {
        request.getAttribute(REQUEST_SET_USER_INFO_KEY).kttlwEmptyCheck({
            null
        }, {
            it
        }) as U?
    }
}

/**
 * 获取用户权限
 * @param request 请求体
 */
fun <R : SpblwBaseHttpServletRequestWrapper, U : SpulwBaseUserInfoTb> spulwGetUserPermissions(request: R,
    userInfoTb: U? = null): List<SpulwRolePermissionTb> {
    return spulwGetUserInfo(request, userInfoTb)?.userRole?.type?.let {
        spulwConfig.applicationContext.getBean(SpulwRolePermissionService::class.java).getPermissions(it)
    }.kttlwGetNotEmptyData { listOf() }
}