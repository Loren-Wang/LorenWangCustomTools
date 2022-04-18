package springbase.lorenwang.user.service.impl

import kotlinbase.lorenwang.tools.extend.kttlwEmptyCheck
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData
import org.springframework.beans.factory.annotation.Autowired
import springbase.lorenwang.user.database.repository.SpulwPlatformTokenRepository
import springbase.lorenwang.user.database.table.SpulwPlatformTokenTb
import springbase.lorenwang.user.service.SpulwPlatformTokenService

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
abstract class SpulwPlatformTokenServiceImpl : SpulwPlatformTokenService {
    @Autowired
    protected lateinit var repository: SpulwPlatformTokenRepository

    override fun saveWeChatTokenInfo(id: String?, accessToken: String?, refreshToken: String?, timeOut: Long?): Boolean {
        return id.kttlwEmptyCheck({ false }, { platformId ->
            repository.save(repository.findByPlatformId(platformId).kttlwGetNotEmptyData { SpulwPlatformTokenTb() }.also {
                it.platformId = platformId
                it.accessToken = accessToken
                it.refreshToken = refreshToken
                it.failureTime = timeOut
            })
            true
        })
    }

    override fun updateUserTokenInfo(userId: String, token: String, timeOut: Long): Boolean {
        repository.save(repository.findByPlatformId(userId).kttlwGetNotEmptyData { SpulwPlatformTokenTb() }.also {
            it.platformId = userId
            it.accessToken = token
            it.refreshToken = null
            it.failureTime = timeOut
        })
        return true
    }
}