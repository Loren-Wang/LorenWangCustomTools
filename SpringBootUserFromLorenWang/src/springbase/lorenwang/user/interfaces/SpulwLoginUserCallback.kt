package springbase.lorenwang.user.interfaces

import springbase.lorenwang.user.database.table.SpulwUserInfoTb

/**
 * 功能作用：用户登录回调
 * 创建时间：2022/4/16 11:15
 * 创建者：王亮（Loren）
 * 备注：
 */
interface SpulwLoginUserCallback {
    /**
     * 登录成功
     */
    fun loginUserSuccess(info: SpulwUserInfoTb): String

    /**
     * 未知原因登录失败
     */
    fun loginUserFailUnKnow(msg: String?): String
}