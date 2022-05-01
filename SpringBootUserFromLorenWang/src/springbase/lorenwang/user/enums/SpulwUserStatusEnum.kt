package springbase.lorenwang.user.enums

import kotlinbase.lorenwang.tools.extend.kttlwEmptyCheck

/**
 * 功能作用：用户状态枚举
 * 初始注释时间： 2022/4/16 14:16
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
enum class SpulwUserStatusEnum(val status: Int) {
    /**
     * 启用
     */
    ENABLE(1),

    /**
     * 禁用
     */
    DISABLE(2),

    /**
     * 删除
     */
    DELETE(3),

    /**
     * 被合并的
     */
    BE_MERGED(4);


    companion object {
        /**
         * 获取用户状态
         */
        @JvmStatic
        fun getUserStatus(status: Int?): SpulwUserStatusEnum? {
            return status.kttlwEmptyCheck({ null }, {
                for (value in SpulwUserStatusEnum.values()) {
                    if (value.status == it) {
                        return value
                    }
                }
                null
            })
        }
    }
}