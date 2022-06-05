package springbase.lorenwang.base.service

import springbase.lorenwang.base.database.table.SpblwVerificationCodeTb
import java.util.*

/**
 * 功能作用：验证码相关服务
 * 初始注释时间： 2022/6/4 18:48
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
interface SpblwVerificationCodeService : SpblwBaseService {
    /**
     * 删除验证码
     */
    fun deleteVerificationCode(account: String, code: String, type: String)

    /**
     * 获取验证码
     */
    fun getVerificationCode(account: String, code: String, type: String): SpblwVerificationCodeTb?

    /**
     * 获取验证码
     */
    fun getVerificationCode(account: String, type: String): List<SpblwVerificationCodeTb>

    /**
     * 保存验证码
     */
    fun saveVerificationCode(account: String, code: String, type: String, endTime: Date): SpblwVerificationCodeTb
}