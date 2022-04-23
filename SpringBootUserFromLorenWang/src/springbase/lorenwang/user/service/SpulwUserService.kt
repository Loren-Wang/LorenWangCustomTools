package springbase.lorenwang.user.service

import springbase.lorenwang.base.controller.SpblwBaseHttpServletRequestWrapper
import springbase.lorenwang.base.service.SpblwBaseService
import springbase.lorenwang.user.database.table.SpulwUserInfoTb
import springbase.lorenwang.user.enums.SpulwUserLoginFromEnum
import springbase.lorenwang.user.enums.SpulwUserLoginTypeEnum
import springbase.lorenwang.user.interfaces.SpulwLoginUserCallback

/**
 * 功能作用：用户相关服务
 * 创建时间：2020-07-01 5:51 下午
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
interface SpulwUserService : SpblwBaseService {
    companion object {
        /**
         * 内部请求头中使用临时的用户信息key
         */
        const val REQUEST_SET_USER_INFO_KEY = "userInfoSave"
    }

    /**
     * 获取密码长度
     */
    fun getPasswordLength(): Int

    /**
     * accessToken超时时间
     */
    fun getAccessTokenTimeOut(): Long

    /**
     * 登录用户
     * @param name 账户相关，账户account、手机号、第三方登录id
     * @param validation 验证处理，密码、验证码
     * @param fromEnum 登录来源，网页、安卓、iOS、鸿蒙、小程序
     * @param typeEnum 登录类型，账户密码、手机号密码、手机号验证码、微信、微博、QQ、邮箱密码、邮箱验证码
     * @param callback 登录回调
     */
    fun loginUser(name: String, validation: String?, fromEnum: SpulwUserLoginFromEnum, typeEnum: SpulwUserLoginTypeEnum,
        callback: SpulwLoginUserCallback): String

    /**
     * 获取用户信息
     * @param userId 用户id
     * @param account 账户
     * @param email 邮件
     * @param phone 手机号
     * @param wxId 微信id
     * @param qqId qqId
     * @param sinaId 新浪微博id
     * @param token 用户token
     */
    fun getUserInfo(userId: String?, account: String?, email: String?, phone: String?, wxId: String?, qqId: String?,
        sinaId: String?): SpulwUserInfoTb?

    /**
     * 获取微信第三方secret
     */
    fun getWeChatSecret(): String

    /**
     * 获取微信第三方登录的appId
     */
    fun getWeChatAppId(): String

    /**
     * 获取微信小程序第三方secret
     */
    fun getWeChatSmallProgramSecret(): String

    /**
     * 获取微信小程序第三方登录的appId
     */
    fun getWeChatSmallProgramAppId(): String

    /**
     * 刷新用户token并返回新的token信息
     */
    fun refreshAccessToken(token: String?, fromEnum: SpulwUserLoginFromEnum): String?

    /**
     * 加密token
     */
    fun encryptAccessToken(token: String): String?

    /**
     * 解密token
     */
    fun decryptAccessToken(token: String): String?

    /**
     * 通过请求头获取用户token
     */
    fun getAccessTokenByReqHeader(request: SpblwBaseHttpServletRequestWrapper): String?

    /**
     * 根据用户token获取用户id
     */
    fun getUserIdByAccessToken(token: String?): String?

    /**
     * 生成用户token
     */
    fun generateAccessToken(userId: String, loginFrom: SpulwUserLoginFromEnum): String?

    /**
     * 生成密码,可能为空
     */
    fun generatePassword(): String?
//
//
//    /**
//     * 新增新用户
//     * @param account 用户名称
//     * @param roleType 角色类型
//     */
//    abstract fun addNewUser(account: String, phoneNum: String, roleType: Int)

}
