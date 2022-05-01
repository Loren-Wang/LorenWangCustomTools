package com.test.springboot.base.services

import org.springframework.stereotype.Service
import springbase.lorenwang.base.controller.SpblwBaseHttpServletRequestWrapper
import springbase.lorenwang.user.enums.SpulwUserLoginFromEnum
import springbase.lorenwang.user.enums.SpulwUserLoginTypeEnum
import springbase.lorenwang.user.service.impl.SpulwUserServiceImpl

/**
 * 功能作用：用户服务实现
 * 创建时间：2020-07-02 9:50 上午
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren wang）
 */
@Service
open class UserServiceImpl : SpulwUserServiceImpl() {
    override fun getAccessTokenTimeOut(): Long {
        return 7 * 24 * 60 * 60 * 1000
    }

    override fun checkLoginUserValidation(name: String, validation: String?, typeEnum: SpulwUserLoginTypeEnum): Boolean {
        return true
    }

    //
//    /**
//     * 新增新用户
//     * @param account 用户名称
//     * @param roleType 角色类型
//     */
//    override fun addNewUser(account: String, phoneNum: String, roleTb: SpulwUserRoleTb) {
////        val userInfoTb = UserInfoTb()
////        userInfoTb.account = account
////        userInfoTb.securitySalt = JtlwCommonUtil.getInstance().generateUuid(true).substring(0, 10)
////        userInfoTb.nickName = JtlwCommonUtil.getInstance().generateUuid(true)
////        userInfoTb.password = passwordEncoder.encodePassword(generatePassword(), userInfoTb.securitySalt)
////        userInfoTb.phoneNum = phoneNum
////        userInfoTb.accessToken = ""
////        userInfoTb.userRole = roleTb
////        userInfoTb.status = UserStatusEnum.ENABLE.status
////        userInfoRepository.save(userInfoTb)
//    }
    override fun getWeChatSecret(): String {
        TODO("Not yet implemented")
    }

    override fun getWeChatAppId(): String {
        TODO("Not yet implemented")
    }

    override fun getWeChatSmallProgramSecret(): String {
        return ""
    }

    override fun getWeChatSmallProgramAppId(): String {
        return ""

    }

    override fun refreshAccessToken(token: String?, fromEnum: SpulwUserLoginFromEnum): String? {
        return ""
    }

    override fun getAccessTokenByReqHeader(request: SpblwBaseHttpServletRequestWrapper): String? {
        return null
    }

    override fun getUserGroupIdByAccessToken(token: String?): String? {
        return null
    }

    override fun generateAccessToken(userId: String, loginFrom: SpulwUserLoginFromEnum): String? {
        return null
    }
}
