package com.test.springboot.service

import com.test.springboot.bean.local.BaseDataDisposeStatusBean
import com.test.springboot.database.table.UserRoleTb
import com.test.springboot.enums.UserLoginFromEnum
import springbase.lorenwang.user.service.SpulwUserService

/**
 * 功能作用：用户服务
 * 创建时间：2020-07-02 9:48 上午
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
abstract class UserService : SpulwUserService() {
    /**
     * 用户登录
     */
    abstract fun loginUser(account: String, password: String, loginFrom: UserLoginFromEnum): BaseDataDisposeStatusBean

    /**
     * 生成用户token
     */
    abstract fun generateAccessToken(userId: String, loginFrom: UserLoginFromEnum): String?

    /**
     * 新增新用户
     * @param account 用户名称
     * @param roleTb 用户角色信息
     */
    abstract fun addNewUser(account: String, phoneNum: String, roleTb: UserRoleTb)
}
