package javabase.lorenwang.common_base_frame.database.helper

import javabase.lorenwang.common_base_frame.bean.SbcbflwBaseDataDisposeStatusBean
import javabase.lorenwang.common_base_frame.controller.SbcbflwBaseHttpServletRequestWrapper
import javabase.lorenwang.common_base_frame.utils.SbcbflwRandomStringUtils

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
public abstract class SbcbflwUserHelper {
    /**
     * 密码长度，默认10位
     */
    protected var passwordLength: Int = 10

    companion object {
        var baseInstance: SbcbflwUserHelper? = null
    }

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
}
