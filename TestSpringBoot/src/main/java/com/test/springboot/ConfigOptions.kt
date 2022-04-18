package com.test.springboot

import com.test.springboot.base.utils.FileOptionsUtils
import com.test.springboot.base.utils.LogUtil
import com.test.springboot.enums.NetRepStatusEnum
import com.test.springboot.enums.UserPermissionTypeEnum
import com.test.springboot.enums.UserRoleTypeEnum
import kotlinbase.lorenwang.tools.common.bean.KttlwBaseNetResponseBean
import kotlinbase.lorenwang.tools.extend.kttlwToJsonData
import springbase.lorenwang.base.controller.SpblwBaseHttpServletRequestWrapper
import springbase.lorenwang.base.utils.SpblwLog
import springbase.lorenwang.tools.plugins.email.SptlwEmailUtil
import springbase.lorenwang.tools.plugins.oss.SptlwOssUtil
import springbase.lorenwang.tools.utils.SptlwFileOptionsUtil
import springbase.lorenwang.user.SpulwConfig
import springbase.lorenwang.user.service.SpulwRolePermissionService
import springbase.lorenwang.user.service.SpulwUserPermissionService
import springbase.lorenwang.user.service.SpulwUserRoleService
import springbase.lorenwang.user.service.SpulwUserService
import springbase.lorenwang.user.spulwConfig

/**
 * 配置管理
 */
class ConfigOptions : SpulwConfig() {
    private val logUtil = LogUtil()
    private val fileOptionsUtil = FileOptionsUtils()

    init {
        spulwConfig = this
        logUtil.initConfig(true)
    }

    /**
     * 初始化用户框架配置
     */
    override fun initUserConfig() {
        //角色处理
        val roleService = applicationContext.getBean(SpulwUserRoleService::class.java)
        roleService.saveUserRole(UserRoleTypeEnum.SUPER_ADMIN.type, UserRoleTypeEnum.SUPER_ADMIN.des)
        //权限处理
        val permissionService = applicationContext.getBean(SpulwUserPermissionService::class.java)
        permissionService.saveUserPermission(UserPermissionTypeEnum.SUPER_ADMIN.type, UserPermissionTypeEnum.SUPER_ADMIN.des)
        permissionService.saveUserPermission(UserPermissionTypeEnum.ADMIN.type, UserPermissionTypeEnum.ADMIN.des)
        //权限角色中间处理
        val rolePermissionService = applicationContext.getBean(SpulwRolePermissionService::class.java)
        rolePermissionService.saveRoleAndPermission(UserRoleTypeEnum.SUPER_ADMIN.type, UserPermissionTypeEnum.SUPER_ADMIN.type)
    }

    override fun currentIsDebug(): Boolean {
        return true
    }

    override fun getUserServices(): SpulwUserService {
        return applicationContext.getBean(SpulwUserService::class.java)
    }

    /**
     * 获取token加解密key
     */
    override fun getDecryptAccessTokenKey(): String {
        return ""
    }

    /**
     * 获取token加解密ivs
     */
    override fun getDecryptAccessTokenIvs(): String {
        return ""
    }

    override fun getAccessControlAllowHeadersUserLoginFrom(): String {
        return "xxx"
    }

    override fun getAccessControlAllowHeadersAccessToken(): String {
        return "sadfasdf"
    }

    override fun getFileOptionsUtil(): SptlwFileOptionsUtil {
        return fileOptionsUtil
    }

    override fun getLogUtil(): SpblwLog {
        return logUtil
    }

    override fun getEmailUtil(): SptlwEmailUtil? {
        return super.getEmailUtil()
    }

    override fun getOssUtil(): SptlwOssUtil {
//        SptlwOssUtil.instance.initOssConfig(SptlwALiYunOssConfig())
        return SptlwOssUtil.instance
    }


    /**
     * 参数异常响应
     */
    override fun <R : SpblwBaseHttpServletRequestWrapper> responseErrorForParams(request: R?): String {
        return getResponseData(NetRepStatusEnum.PARAMS_ERROR)
    }

    /**
     * 数据响应成功
     */
    override fun <R : SpblwBaseHttpServletRequestWrapper> responseSuccess(request: R?, data: Any?): String {
        return getResponseData(NetRepStatusEnum.SUCCESS, data)
    }

    /**
     * 数据删除失败
     */
    override fun <R : SpblwBaseHttpServletRequestWrapper> responseDeleteFail(request: R?): String {
        return getResponseData(NetRepStatusEnum.DATA_DELETE_FAIL)
    }

    /**
     * 未知错误失败
     */
    override fun <R : SpblwBaseHttpServletRequestWrapper> responseFailForUnKnow(request: R?): String {
        return getResponseData(NetRepStatusEnum.FAIL_UN_KNOW)
    }

    /**
     * 登录验证失败,用户未登录或者token失效
     */
    override fun <R : SpblwBaseHttpServletRequestWrapper> responseErrorUserLoginEmptyOrTokenNoneffective(request: R?): String {
        return getResponseData(NetRepStatusEnum.USER_NOT_LOGIN_OR_TOKEN_FAILURE)
    }

    /**
     * 无权限异常
     */
    override fun <R : SpblwBaseHttpServletRequestWrapper> responseErrorNotPermission(request: R?): String {
        return getResponseData(NetRepStatusEnum.USER_HAVE_NOT_PERMISSION)
    }

    /**
     * 获取响应信息
     * @param repStatusEnum 状态枚举
     * @param data data信息
     */
    private fun getResponseData(repStatusEnum: NetRepStatusEnum, data: Any? = null): String {
        val repBean = KttlwBaseNetResponseBean(data)
        repBean.stateMessage = getMessage(repStatusEnum.messageKey)
        repBean.stateCode = repStatusEnum.code
        return repBean.kttlwToJsonData()
    }
}

/**
 * 配置操作实例
 */
val configOptions = ConfigOptions()