package com.test.springboot

import com.test.springboot.base.utils.FileOptionsUtils
import com.test.springboot.base.utils.LogUtil
import com.test.springboot.enums.NetRepStatusEnum
import com.test.springboot.enums.UserPermissionTypeEnum
import com.test.springboot.enums.UserRoleTypeEnum
import kotlinbase.lorenwang.tools.common.bean.KttlwBaseNetResponseBean
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData
import kotlinbase.lorenwang.tools.extend.kttlwToJsonData
import springbase.lorenwang.base.utils.SpblwLog
import springbase.lorenwang.tools.plugins.email.SptlwEmailUtil
import springbase.lorenwang.tools.plugins.oss.SptlwOssUtil
import springbase.lorenwang.tools.plugins.sms.SptlwSmsUtil
import springbase.lorenwang.tools.plugins.sms.aliyun.SptlwALiYunSmsConfig
import springbase.lorenwang.tools.utils.SptlwFileOptionsUtil
import springbase.lorenwang.user.SpulwConfig
import springbase.lorenwang.user.service.SpulwRolePermissionService
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
        //权限角色中间处理
        val rolePermissionService = applicationContext.getBean(SpulwRolePermissionService::class.java)
        rolePermissionService.saveRoleAndPermission(UserRoleTypeEnum.SUPER_ADMIN.type, UserPermissionTypeEnum.SUPER_ADMIN.type)
    }

    override fun currentIsDebug(): Boolean {
        return true
    }

    override fun encryptRequestContent(data: String?): String {
        return data.kttlwGetNotEmptyData { "" }
    }

    override fun decryptResponseContent(data: String?): String {
        return data.kttlwGetNotEmptyData { "" }

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

    override fun getOssUtil(): SptlwOssUtil {
        return SptlwOssUtil.instance
    }

    override fun getSmsUtil(): SptlwSmsUtil {
        return SptlwSmsUtil.instance
    }

    override fun getEmailUtil(): SptlwEmailUtil {
        return email
    }

    /**
     * 获取响应信息
     * @param repStatusEnum 状态枚举
     * @param data data信息
     */
    private fun getResponseData(repStatusEnum: NetRepStatusEnum, data: Any? = null): String {
        val repBean = KttlwBaseNetResponseBean(data)
        repBean.stateMessage = messageMap[repStatusEnum.messageKey].toString()
        repBean.stateCode = repStatusEnum.code
        return repBean.kttlwToJsonData()
    }
}

/**
 * 配置操作实例
 */
val configOptions = ConfigOptions()