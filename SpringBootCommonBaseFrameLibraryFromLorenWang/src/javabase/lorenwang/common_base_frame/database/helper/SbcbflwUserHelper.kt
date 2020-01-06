package javabase.lorenwang.common_base_frame.database.helper

import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseHttpServletRequestWrapper
import javabase.lorenwang.common_base_frame.database.repository.SbcbflwUserInfoRepository
import javabase.lorenwang.common_base_frame.database.table.SbcbflwBaseUserInfoTb
import org.apache.commons.lang.RandomStringUtils

/**
 * 功能作用：用户帮助类
 * 创建时间：2020-01-06 17:08
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
abstract class SbcbflwUserHelper {
    /**
     * 密码长度，默认10位
     */
    protected var passwordLength: Int = 10

    companion object {
        lateinit var instance: SbcbflwUserHelper
    }

    /**
     * 通过请求头获取用户token
     */
    abstract fun getAccessTokenByReqHeader(sbcbflwBaseHttpServletRequestWrapper: SbcbflwBaseHttpServletRequestWrapper): String?

    /**
     * 检查token是否有效
     */
    abstract fun checkAccessTokenEffective(deToken: String): Boolean

    /**
     * 根据用户token获取用户id
     */
    abstract fun getUserIdByAccessToken(deToken: String): Long?

    /**
     * 检测用户是否已经登录
     */
    abstract fun checkUserLogin(req: SbcbflwBaseHttpServletRequestWrapper, userInfoRepository: SbcbflwUserInfoRepository): SbcbflwBaseUserInfoTb

    /**
     * 刷新用户token
     */
    abstract fun refreshAccessToken(userInfoRepository: SbcbflwUserInfoRepository, accessToken: String): String

    /**
     * 生成密码,可能为空
     */
    fun generatePassword(): String? {
        return try {
            RandomStringUtils.randomAlphanumeric(passwordLength)
        } catch (e: Exception) {
            null
        }
    }
}
