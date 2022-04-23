package kotlinbase.lorenwang.tools.extend

import javabase.lorenwang.dataparse.JdplwJsonUtil
import javabase.lorenwang.tools.common.JtlwCheckVariateUtil

/**
 * 功能作用：基础函数扩展
 * 创建时间：2019-11-14 下午 23:17:43
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

/**
 * 获取实例的json数据
 */
fun <T> T.kttlwToJsonData(): String {
    return try {
        JdplwJsonUtil.toJson(this) ?: ""
    } catch (e: Exception) {
        ""
    }
}

/**
 * 检测基础数据是否为空，包括空字符串
 */
fun <T> T?.kttlwIsEmpty(): Boolean {
    return JtlwCheckVariateUtil.getInstance().isEmpty(this)
}

/**
 * 数据检测，根据检测结果调用不同方法，调用结束后返回数据，包括空字符串检测
 */
inline fun <T, R> T?.kttlwEmptyCheck(emptyFun: () -> R, notEmptyFun: (T) -> R): R {
    return if (this.kttlwIsEmpty()) {
        emptyFun()
    } else {
        notEmptyFun(this!!)
    }
}

/**
 * 空检测，如果为空调用空方法，包括空字符串检测
 */
inline fun <T, R> T?.kttlwEmptyCheck(emptyFun: () -> R) {
    if (this.kttlwIsEmpty()) {
        emptyFun()
    }
}

/**
 * 检测数据是否为空，仅仅是空，仅仅是null判断
 */
fun <T> T?.kttlwIsNull(): Boolean {
    return this == null
}

/**
 * 检测数据是否不为空，仅仅是空，仅仅是null判断
 */
fun <T> T?.kttlwIsNotNull(): Boolean {
    return this != null
}

/**
 * 是否为非null或者空
 */
fun <T> T?.kttlwIsNotNullOrEmpty(): Boolean {
    if (this == null) {
        return false
    }
    if (this is CharSequence) {
        return this.isNotEmpty()
    }
    if (this is Iterable<*>) {
        return !this.none()
    }
    return true
}

/**
 * 数据为null检测，为null时执行fun
 */
inline fun <T> T?.kttlwNullCheck(exec: () -> Unit) {
    if (this.kttlwIsNull()) {
        exec()
    }
}

/**
 * 数据不为null检测，不为null时执行fun
 */
inline fun <T> T?.kttlwNotNullCheck(exec: (T) -> Unit) {
    if (this.kttlwIsNotNull()) {
        exec(this!!)
    }
}

/**
 * 数据null检测，分情况执行fun
 */
inline fun <T, R> T?.kttlwNullCheck(nullFun: () -> R, notNullFun: () -> R): R {
    return if ((this.kttlwIsNull())) {
        nullFun()
    } else {
        notNullFun()
    }
}

/**
 * 数据转换成指定的数据格式类型
 */
inline fun <reified T> Any?.kttlwFormatConversion(): T? {
    return if (this != null && this is T) {
        this
    } else {
        null
    }
}

/**
 * 判断数据值是否是确定状态，默认返回非确定状态也就是默认认定数据为false
 */
fun Any?.kttlwIfTrue(): Boolean {
    if (this != null && this.toString().matches(Regex("\\d+"))) {
        return this.toString() != "0"
    }
    if (this is Boolean) {
        return this
    }
    return false
}

/**
 * 判断数据值是否是否定状态，默认返回非确定状态也就是默认认定数据为false
 */
fun Any?.kttlwIfFalse(): Boolean {
    if (this != null && this.toString().matches(Regex("\\d+"))) {
        return this.toString() == "0"
    }
    if (this is Boolean) {
        return this
    }
    return true
}


/**
 * 待检测参数中是否包含空数据，包括空字符串检测
 */
inline fun <R> kttlwHaveEmptyCheck(emptyFun: () -> R, notEmptyFun: () -> R, params: Array<*>): R {
    params.forEach {
        if (it.kttlwIsEmpty()) {
            return emptyFun()
        }
    }
    return notEmptyFun()
}

/**
 * 待检测参数中是否包含空数据，包括空字符串检测
 * @return 有返回true，否则返回false
 */
fun kttlwHaveEmptyCheck(params: Array<*>): Boolean {
    params.forEach {
        if (it.kttlwIsEmpty()) {
            return true
        }
    }
    return false
}

/**
 * 待检测参数中是否全部是空数据，包括空字符串检测
 * @return 有返回true，否则返回false
 */
fun kttlwAllEmptyCheck(params: Array<*>): Boolean {
    params.forEach {
        if (!it.kttlwIsEmpty()) {
            return false
        }
    }
    return true
}

/**
 * 待检测参数中是否有null数据
 */
inline fun <R> kttlwHaveNullCheck(params: Array<*>, nullFun: () -> R, notNullFun: () -> R): R {
    params.forEach {
        if (it.kttlwIsNull()) {
            return nullFun()
        }
    }
    return notNullFun()
}

/**
 * 待检测参数中是否包含null
 * @return 有返回true，否则返回false
 */
fun kttlwHaveNullCheck(params: Array<*>): Boolean {
    params.forEach {
        if (it.kttlwIsNull()) {
            return true
        }
    }
    return false
}

/**
 * 待检测参数中是否全部是null
 * @return 全是返回true，否则返回false
 */
fun kttlwAllNullCheck(params: Array<*>): Boolean {
    params.forEach {
        if (!it.kttlwIsNull()) {
            return false
        }
    }
    return true
}

/**
 * 获取非空数据
 */
fun <T> T?.kttlwGetNotEmptyData(getDefaultData: () -> T): T {
    return if (this.kttlwIsEmpty()) {
        getDefaultData()
    } else {
        this!!
    }
}
