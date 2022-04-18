package springbase.lorenwang.user.enums

import kotlinbase.lorenwang.tools.extend.kttlwEmptyCheck

/**
 * 功能作用：用户性别枚举
 * 初始注释时间： 2022/4/16 14:10
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
enum class SpulwUserSexEnum(val sex: Int) {
    /**
     * 男性
     */
    MAN(1),

    /**
     * 女性
     */
    WOMAN(2);

    companion object {
        /**
         * 获取用户性别
         */
        @JvmStatic
        fun getUserSex(sex: Int?): SpulwUserSexEnum? {
            return sex.kttlwEmptyCheck({ null }, {
                for (value in values()) {
                    if (value.sex == it) {
                        return value
                    }
                }
                null
            })
        }
    }
}