package com.test.springboot.enums

/**
 * 功能作用：用户状态
 * 创建时间：2020-07-13 11:42 上午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：基类以及子类必须使用java文件格式，否则会导致无法使用父类变量
 *
 * @author 王亮（Loren）
 */
enum class UserStatusEnum(val status : Int) {
    /**
     * 启用
     */
    ENABLE(-1),

    /**
     * 禁用
     */
    DISABLE(-2),

    /**
     * 删除
     */
    DELETE(-3)
}
