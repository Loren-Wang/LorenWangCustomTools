package springbase.lorenwang.user.database.repository

import springbase.lorenwang.base.database.repository.SpblwBaseRepository
import springbase.lorenwang.user.database.table.SpulwVerificationCodeTb

/**
 * 功能作用：验证码数据库操作
 * 初始注释时间： 2022/6/4 18:44
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
interface SpulwVerificationCodeRepository : SpblwBaseRepository<SpulwVerificationCodeTb, String> {
    /**
     * 根据code和type查找数据，并且根据时间倒序处理
     */
    fun findAllByAccountAndCodeAndTypeOrderByEndTimeDesc(account: String, code: String, type: String): List<SpulwVerificationCodeTb>

    /**
     * 根据type查找数据，并且根据时间倒序处理
     */
    fun findAllByAccountAndTypeOrderByEndTimeDesc(account: String, type: String): List<SpulwVerificationCodeTb>

    /**
     * 根据账户验证码以及类型删除验证记录
     */
    fun deleteAllByAccountAndCodeAndType(account: String, code: String, type: String)

    /**
     * 根据账户以及类型删除记录
     */
    fun deleteAllByAccountAndType(account: String, type: String)
}