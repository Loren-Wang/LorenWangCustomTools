package springbase.lorenwang.user.kotlinExtend

import kotlinbase.lorenwang.tools.extend.kttlwEmptyCheck
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData
import springbase.lorenwang.base.controller.SpblwBaseHttpServletRequestWrapper
import springbase.lorenwang.user.database.table.SpulwRolePermissionTb
import springbase.lorenwang.user.database.table.SpulwUserInfoTb
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
 * 权限检测
 * @param request 请求体
 * @param permissions 要检测的权限id列表
 */
fun <R : SpblwBaseHttpServletRequestWrapper, U : SpulwUserInfoTb> spulwCheckPermission(request: R, permissions: List<Int>,
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
fun <R : SpblwBaseHttpServletRequestWrapper> spulwGetUserInfo(request: R, userInfoTb: SpulwUserInfoTb? = null): SpulwUserInfoTb? {
    //从request中获取用户信息，该信息是在filter中拦截并添加到attribute中的
    return userInfoTb?.kttlwGetNotEmptyData {
        request.getAttribute(REQUEST_SET_USER_INFO_KEY).kttlwEmptyCheck({
            null
        }, {
            it
        }) as SpulwUserInfoTb?
    }
}

/**
 * 获取用户权限
 * @param request 请求体
 */
fun <R : SpblwBaseHttpServletRequestWrapper, U : SpulwUserInfoTb> spulwGetUserPermissions(request: R,
    userInfoTb: U? = null): List<SpulwRolePermissionTb> {
    return spulwGetUserInfo(request, userInfoTb)?.userRole?.let {
        spulwConfig.applicationContext.getBean(SpulwRolePermissionService::class.java).getPermissions(it)
    }.kttlwGetNotEmptyData { listOf() }
}