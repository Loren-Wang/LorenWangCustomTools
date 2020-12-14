package com.test.springboot.service.impl

import com.test.springboot.bean.local.BaseDataDisposeStatusBean
import com.test.springboot.database.repository.UserInfoRepository
import com.test.springboot.database.repository.UserRoleRepository
import com.test.springboot.database.table.UserInfoTb
import com.test.springboot.database.table.UserRoleTb
import com.test.springboot.enums.UserLoginFromEnum
import com.test.springboot.enums.UserStatusEnum
import com.test.springboot.service.UserService
import javabase.lorenwang.common_base_frame.bean.SbcbflwBaseDataDisposeStatusBean
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseHttpServletRequestWrapper
import javabase.lorenwang.tools.common.JtlwCommonUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.test.springboot.plugins.UserPassword

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

    @Autowired
    private lateinit var userInfoRepository : UserInfoRepository

    @Autowired
    private lateinit var userRoleRepository : UserRoleRepository

    /**
     * 密码相关
     */
    @Autowired
    private lateinit var passwordEncoder : UserPassword

    /**
     * 用户登录
     */
    override fun loginUser(account : String, password : String, loginFrom : UserLoginFromEnum) : BaseDataDisposeStatusBean {
        TODO("Not yet implemented")
    }

    /**
     * 生成用户token
     */
    override fun generateAccessToken(userId : String, loginFrom : UserLoginFromEnum) : String? {
        TODO("Not yet implemented")
    }

    /**
     * 检查token是否有效
     */
    override fun checkAccessTokenEffective(token : String?) : SbcbflwBaseDataDisposeStatusBean {
        TODO("Not yet implemented")
    }

    /**
     * 检测用户是否已经登录
     */
    override fun checkUserLogin(request : SbcbflwBaseHttpServletRequestWrapper) : SbcbflwBaseDataDisposeStatusBean {
        TODO("Not yet implemented")
    }

    /**
     * 通过请求头获取用户token
     */
    override fun getAccessTokenByReqHeader(request : SbcbflwBaseHttpServletRequestWrapper) : String? {
        TODO("\n" +
                "        return request.getHeader(Setting.ACCESS_TOKEN_KEY)?.let { JtlwCodeConversionUtil.getInstance().unicodeToChinese(it) }")
    }

    /**
     * 根据用户token获取用户id
     */
    override fun getUserIdByAccessToken(token : String?) : String? {
        TODO("Not yet implemented")
    }

    /**
     * 刷新用户token
     */
    override fun refreshAccessToken(token : String) : String {
        TODO("Not yet implemented")
    }

    /**
     * 新增新用户
     * @param account 用户名称
     * @param roleType 角色类型
     */
    override fun addNewUser(account : String, phoneNum : String, roleType : Int) {
        userRoleRepository.findByRoleType(roleType)?.let {
            addNewUser(account, phoneNum, it)
        }
    }

    /**
     * 新增新用户
     * @param account 用户名称
     * @param roleType 角色类型
     */
    override fun addNewUser(account : String, phoneNum : String, roleTb : UserRoleTb) {
        val userInfoTb = UserInfoTb()
        userInfoTb.account = account
        userInfoTb.securitySalt = JtlwCommonUtils.getInstance().generateUuid(true).substring(0, 10)
        userInfoTb.nickName = JtlwCommonUtils.getInstance().generateUuid(true)
        userInfoTb.password = passwordEncoder.encodePassword(generatePassword(), userInfoTb.securitySalt)
        userInfoTb.phoneNum = phoneNum
        userInfoTb.accessToken = ""
        userInfoTb.userRole = roleTb
        userInfoTb.status = UserStatusEnum.ENABLE.status
        userInfoRepository.save(userInfoTb)
    }
}
