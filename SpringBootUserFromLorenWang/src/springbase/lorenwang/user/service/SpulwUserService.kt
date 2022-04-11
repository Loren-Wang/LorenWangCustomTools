package springbase.lorenwang.user.service

import springbase.lorenwang.base.bean.SpblwBaseDataDisposeStatusBean
import springbase.lorenwang.base.controller.SpblwBaseHttpServletRequestWrapper
import springbase.lorenwang.base.service.SpblwBaseService
import springbase.lorenwang.tools.safe.SptlwEncryptDecryptUtil
import springbase.lorenwang.tools.utils.SptlwRandomStringUtil
import springbase.lorenwang.user.spulwConfig

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
abstract class SpulwUserService : SpblwBaseService {
    companion object {
        /**
         * 内部请求头中使用临时的用户信息key
         */
        const val REQUEST_SET_USER_INFO_KEY = "userInfoSave"
    }

    /**
     * 密码长度，默认10位
     */
    var passwordLength: Int = 10

    /**
     * 是否加密了token，自动调用，当执行过加密方法之后会被自动设置为true
     */
    var encryptAccessToken = false


    /**
     * 通过请求头获取用户token
     */
    abstract fun getAccessTokenByReqHeader(request: SpblwBaseHttpServletRequestWrapper): String?

    /**
     * 检查token是否有效
     */
    abstract fun checkAccessTokenEffective(token: String?): SpblwBaseDataDisposeStatusBean

    /**
     * 根据用户token获取用户id
     */
    abstract fun getUserIdByAccessToken(token: String?): String?

    /**
     * 检测用户是否已经登录
     */
    abstract fun checkUserLogin(request: SpblwBaseHttpServletRequestWrapper): SpblwBaseDataDisposeStatusBean

    /**
     * 刷新用户token
     */
    abstract fun refreshAccessToken(token: String): String

    /**
     * 登录验证失败,用户未登录或者token失效
     *
     * @param errorInfo 错误信息
     * @return 返回登录验证失败响应字符串
     */
    abstract fun responseErrorUser(errorInfo: SpblwBaseDataDisposeStatusBean?): String

    /**
     * 生成密码,可能为空
     */
    fun generatePassword(): String? {
        return try {
            SptlwRandomStringUtil.randomAlphanumeric(passwordLength)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 加密token
    //     * @param key 加密的key
    //     * @param ivs 加密解密的算法参数
     */
    fun encryptAccessToken(token: String): String? {
        encryptAccessToken = true
        return SptlwEncryptDecryptUtil.instance.encrypt(spulwConfig.getDecryptAccessTokenKey(), spulwConfig.getDecryptAccessTokenIvs(), token)
    }

    /**
     * 解密token
    //     * @param key 加密的key
    //     * @param ivs 加密解密的算法参数
     */
    fun decryptAccessToken(token: String): String? {
        return if (encryptAccessToken) {
            SptlwEncryptDecryptUtil.instance.decrypt(spulwConfig.getDecryptAccessTokenKey(), spulwConfig.getDecryptAccessTokenIvs(), token)
        } else {
            token
        }
    }

    /**
     * 新增新用户
     * @param account 用户名称
     * @param roleType 角色类型
     */
    abstract fun addNewUser(account: String, phoneNum: String, roleType: Int)

}
