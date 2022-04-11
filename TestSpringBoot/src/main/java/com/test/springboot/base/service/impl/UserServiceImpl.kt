package com.test.springboot.base.service.impl

import com.test.springboot.bean.local.BaseDataDisposeStatusBean
import com.test.springboot.enums.UserLoginFromEnum
import com.test.springboot.base.service.UserService
import org.springframework.stereotype.Service
import springbase.lorenwang.base.bean.SpblwBaseDataDisposeStatusBean
import springbase.lorenwang.base.controller.SpblwBaseHttpServletRequestWrapper
import springbase.lorenwang.user.database.table.SpulwUserRoleTb

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
class UserServiceImpl : UserService() {

    /**
     * 用户登录
     */
    override fun loginUser(account: String, password: String, loginFrom: UserLoginFromEnum): BaseDataDisposeStatusBean {
        TODO("Not yet implemented")
    }

    /**
     * 生成用户token
     */
    override fun generateAccessToken(userId: String, loginFrom: UserLoginFromEnum): String? {
        return "safdasdf"
    }

    /**
     * 检查token是否有效
     */
    override fun checkAccessTokenEffective(token: String?): SpblwBaseDataDisposeStatusBean {
        return SpblwBaseDataDisposeStatusBean(true)
    }

    /**
     * 检测用户是否已经登录
     */
    override fun checkUserLogin(request: SpblwBaseHttpServletRequestWrapper): SpblwBaseDataDisposeStatusBean {
        return SpblwBaseDataDisposeStatusBean(true)
    }

    /**
     * 通过请求头获取用户token
     */
    override fun getAccessTokenByReqHeader(request: SpblwBaseHttpServletRequestWrapper): String? {
        return ""
    }

    /**
     * 根据用户token获取用户id
     */
    override fun getUserIdByAccessToken(token: String?): String? {
        return ""
    }

    /**
     * 刷新用户token
     */
    override fun refreshAccessToken(token: String): String {
        return ""
    }

    /**
     * 登录验证失败,用户未登录或者token失效
     *
     * @param errorInfo 错误信息
     * @return 返回登录验证失败响应字符串
     */
    override fun responseErrorUser(errorInfo: SpblwBaseDataDisposeStatusBean?): String {
        return ""
    }

    /**
     * 新增新用户
     * @param account 用户名称
     * @param roleType 角色类型
     */
    override fun addNewUser(account: String, phoneNum: String, roleType: Int) {
//        userRoleRepository.findByType(roleType)?.let {
//            addNewUser(account, phoneNum, it)
//        }
    }

    /**
     * 新增新用户
     * @param account 用户名称
     * @param roleType 角色类型
     */
    override fun addNewUser(account: String, phoneNum: String, roleTb: SpulwUserRoleTb) {
//        val userInfoTb = UserInfoTb()
//        userInfoTb.account = account
//        userInfoTb.securitySalt = JtlwCommonUtil.getInstance().generateUuid(true).substring(0, 10)
//        userInfoTb.nickName = JtlwCommonUtil.getInstance().generateUuid(true)
//        userInfoTb.password = passwordEncoder.encodePassword(generatePassword(), userInfoTb.securitySalt)
//        userInfoTb.phoneNum = phoneNum
//        userInfoTb.accessToken = ""
//        userInfoTb.userRole = roleTb
//        userInfoTb.status = UserStatusEnum.ENABLE.status
//        userInfoRepository.save(userInfoTb)
    }
}
