package kotlinbase.lorenwang.tools.extend

import javabase.lorenwang.dataparse.JdplwJsonUtils

/**
 * 功能作用：字符串相关函数扩展
 * 创建时间：2019-11-14 下午 23:28:13
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

/**
 * 格式化json数据
 */
fun <T> String.kttlwParseJsonData(cls: Class<T>): T? {
    return try {
        JdplwJsonUtils.fromJson(this, cls)
    } catch (e: Exception) {
        null
    }
}

/**
 * 获取非空数据
 */
fun String?.kttlwGetNotEmptyData(defaultStr: String = ""): String {
    return if (this.isNullOrEmpty()) {
        defaultStr
    } else {
        this
    }
}

/**
 * 判断这个字符串是否是长整型时间戳
 */
fun String?.kttlwIsLongTime(): Boolean {
    return !(this == null || !this.matches(Regex("[0-9]+")))
}
