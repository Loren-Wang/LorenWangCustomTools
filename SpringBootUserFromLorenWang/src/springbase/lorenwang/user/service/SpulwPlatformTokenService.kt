package springbase.lorenwang.user.service

import springbase.lorenwang.base.service.SpblwBaseService

/**
 * 功能作用：平台token服务
 * 初始注释时间： 2022/4/17 11:30
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
interface SpulwPlatformTokenService : SpblwBaseService {
    /**
     * 保存微信token信息
     */
    fun saveWeChatTokenInfo(openid: String?, accessToken: String?, refreshToken: String?, timeOut: Long?): Boolean

    /**
     * 更新用户token信息
     */
    fun updateUserTokenInfo(userId: String, token: String, timeOut: Long): Boolean
}