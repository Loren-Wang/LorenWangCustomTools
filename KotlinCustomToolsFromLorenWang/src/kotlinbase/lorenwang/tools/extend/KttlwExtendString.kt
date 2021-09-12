package kotlinbase.lorenwang.tools.extend

import javabase.lorenwang.dataparse.JdplwJsonUtils
import javabase.lorenwang.tools.common.JtlwCommonUtils

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

/**
 * 将字符串转为驼峰法
 */
fun String?.kttlwToCamelCase(): String? {
    return JtlwCommonUtils.getInstance().toCamelCase(this)
}

/**
 * 将字符串分离(以大写字母为分隔添加位置)
 * @param separated 分离字符
 * @return 分离后字符
 */
fun String?.kttlwToSeparatedCase(separated: String?): String? {
    return JtlwCommonUtils.getInstance().toSeparatedCase(this, separated)
}

/**
 * 清除起始字符串
 * @param str 要清除的字符串
 * @param zero 是否要清除最开始的，当为true的时候，如果最开始是该字符串则清除，否则不进行任何操作
 */
fun String?.kttlwClearFirstString(str: String, zero: Boolean): String? {
    return if (zero) {
        val indexOf = this?.indexOf(str, 0, false)
        if (indexOf == 0) {
            this?.substring(str.length)
        } else {
            this
        }
    } else {
        this?.replaceFirst(str, "", false)
    }
}

/**
 * 清除结束字符串
 * @param str 要清除的字符串
 * @param last 是否要清除最尾部的，当为true的时候，如果最尾部是该字符串则清除，否则不进行任何操作
 */
fun String?.kttlwClearEndString(str: String, last: Boolean): String? {
    val indexOf = this?.lastIndexOf(str).kttlwGetNotEmptyData(-1)
    val length = this?.length.kttlwGetNotEmptyData(0) - 1
    return if (last) {
        if (indexOf == length) {
            this?.substring(0, indexOf)
        } else {
            this
        }
    } else {
        if (indexOf > 0) {
            this?.substring(0, indexOf) + this?.substring(indexOf + str.length)
        } else {
            this
        }
    }
}
