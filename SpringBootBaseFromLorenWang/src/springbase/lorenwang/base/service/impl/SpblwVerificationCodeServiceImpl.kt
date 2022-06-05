package springbase.lorenwang.base.service.impl

import org.springframework.beans.factory.annotation.Autowired
import springbase.lorenwang.base.database.repository.SpblwVerificationCodeRepository
import springbase.lorenwang.base.database.table.SpblwVerificationCodeTb
import springbase.lorenwang.base.service.SpblwVerificationCodeService
import java.util.*
import javax.transaction.Transactional

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
open class SpblwVerificationCodeServiceImpl : SpblwVerificationCodeService {
    @Autowired
    private lateinit var verificationCodeRepository: SpblwVerificationCodeRepository

    /**
     * 删除验证码
     */
    override fun deleteVerificationCode(account: String, code: String, type: String) {
        verificationCodeRepository.deleteAllByAccountAndCodeAndType(account, code, type)
    }

    /**
     * 获取验证码
     */
    override fun getVerificationCode(account: String, code: String, type: String): SpblwVerificationCodeTb? {
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
    override fun getVerificationCode(account: String, type: String): List<SpblwVerificationCodeTb> {
        return verificationCodeRepository.findAllByAccountAndTypeOrderByEndTimeDesc(account, type)
    }

    /**
     * 保存验证码
     */
    override fun saveVerificationCode(account: String, code: String, type: String, endTime: Date): SpblwVerificationCodeTb {
        //删除旧的
        verificationCodeRepository.deleteAllByAccountAndType(account, type)
        //保存新的
        return verificationCodeRepository.save(SpblwVerificationCodeTb().also {
            it.code = code
            it.account = account
            it.type = type
            it.endTime = endTime
        })
    }
}