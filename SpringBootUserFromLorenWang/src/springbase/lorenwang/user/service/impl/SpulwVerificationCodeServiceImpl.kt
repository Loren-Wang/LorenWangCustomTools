package springbase.lorenwang.user.service.impl

import org.springframework.beans.factory.annotation.Autowired
import springbase.lorenwang.user.database.repository.SpulwVerificationCodeRepository
import springbase.lorenwang.user.database.table.SpulwVerificationCodeTb
import springbase.lorenwang.user.service.SpulwVerificationCodeService
import java.util.*

/**
 * 功能作用：验证码服务实现
 * 初始注释时间： 2022/6/4 18:49
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
open class SpulwVerificationCodeServiceImpl : SpulwVerificationCodeService {
    @Autowired
    private lateinit var verificationCodeRepository: SpulwVerificationCodeRepository

    /**
     * 删除验证码
     */
    override fun deleteVerificationCode(account: String, code: String, type: String) {
        verificationCodeRepository.deleteAllByAccountAndCodeAndType(account, code, type)
    }

    /**
     * 获取验证码
     */
    override fun getVerificationCode(account: String, code: String, type: String): SpulwVerificationCodeTb? {
        verificationCodeRepository.findAllByAccountAndCodeAndTypeOrderByEndTimeDesc(account, code, type).let {
            if (it.isNotEmpty()) {
                return if (it[0].endTime!!.time > System.currentTimeMillis()) {
                    it[0]
                } else {
                    null
                }
            } else {
                return null
            }
        }
    }

    /**
     * 获取验证码列表
     */
    override fun getVerificationCode(account: String, type: String): List<SpulwVerificationCodeTb> {
        return verificationCodeRepository.findAllByAccountAndTypeOrderByEndTimeDesc(account, type)
    }

    /**
     * 保存验证码
     */
    override fun saveVerificationCode(account: String, code: String, type: String, endTime: Date): SpulwVerificationCodeTb {
        //删除旧的
        verificationCodeRepository.deleteAllByAccountAndType(account, type)
        //保存新的
        return verificationCodeRepository.save(SpulwVerificationCodeTb().also {
            it.code = code
            it.account = account
            it.type = type
            it.endTime = endTime
        })
    }
}