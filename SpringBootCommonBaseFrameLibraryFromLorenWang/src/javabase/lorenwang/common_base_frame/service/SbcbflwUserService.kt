package javabase.lorenwang.common_base_frame.service

import javabase.lorenwang.common_base_frame.bean.SbcbflwBaseDataDisposeStatusBean
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseHttpServletRequestWrapper
import javabase.lorenwang.common_base_frame.safe.SbcbflwEncryptDecryptUtils
import javabase.lorenwang.common_base_frame.utils.SbcbflwRandomStringUtils

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
abstract class SbcbflwUserService : SbcbflwBaseService {
    /**
     * 密码长度，默认10位
     */
    protected var passwordLength: Int = 10

    /**
     * 是否加密了token，自动调用，当执行过加密方法之后会被自动设置为true
     */
    var encryptAccessToken = false

    /**
     * 通过请求头获取用户token
     */
    abstract fun getAccessTokenByReqHeader(request: SbcbflwBaseHttpServletRequestWrapper): String?

    /**
     * 检查token是否有效
     */
    abstract fun checkAccessTokenEffective(token: String?): SbcbflwBaseDataDisposeStatusBean

    /**
     * 根据用户token获取用户id
     */
    abstract fun getUserIdByAccessToken(token: String?): Long?

    /**
     * 检测用户是否已经登录
     */
    abstract fun checkUserLogin(request: SbcbflwBaseHttpServletRequestWrapper): SbcbflwBaseDataDisposeStatusBean

    /**
     * 刷新用户token
     */
    abstract fun refreshAccessToken(token: String): String

    /**
     * 生成密码,可能为空
     */
    fun generatePassword(): String? {
        return try {
            SbcbflwRandomStringUtils.randomAlphanumeric(passwordLength)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 加密token
     */
    fun encryptAccessToken(token: String): String? {
        encryptAccessToken = true
        return SbcbflwEncryptDecryptUtils.instance.encrypt(token)
    }

    /**
     * 解密token
     */
    fun decryptAccessToken(token: String): String? {
        return if (encryptAccessToken) {
            SbcbflwEncryptDecryptUtils.instance.decrypt(token)
        } else {
            token
        }
    }
}
