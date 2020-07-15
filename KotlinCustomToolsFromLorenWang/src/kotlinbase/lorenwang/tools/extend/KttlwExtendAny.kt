package kotlinbase.lorenwang.tools.extend

import javabase.lorenwang.dataparse.JdplwJsonUtils
import javabase.lorenwang.tools.common.JtlwCheckVariateUtils

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
fun <T> T.toJsonData(): String {
    return try {
        JdplwJsonUtils.toJson(this) ?: ""
    } catch (e: Exception) {
        ""
    }
}

/**
 * 检测基础数据是否为空，包括空字符串
 */
fun <T> T?.isEmpty(): Boolean {
    return JtlwCheckVariateUtils.getInstance().isEmpty(this)
}

/**
 * 数据检测，根据检测结果调用不同方法，调用结束后返回数据，包括空字符串检测
 */
inline fun <T, R> T?.emptyCheck(emptyFun: () -> R, notEmptyFun: (T) -> R): R {
    return if (this.isEmpty()) {
        emptyFun()
    } else {
        notEmptyFun(this!!)
    }
}

/**
 * 空检测，如果为空调用空方法，包括空字符串检测
 */
inline fun <T, R> T?.emptyCheck(emptyFun: () -> R) {
    if (this.isEmpty()) {
        emptyFun()
    }
}

/**
 * 待检测参数中是否包含空数据，包括空字符串检测
 */
inline fun <T, P, R> T.haveEmptyCheck(emptyFun: () -> R, notEmptyFun: () -> R, vararg params: P): R {
    params.forEach {
        if (it.isEmpty()) {
            return emptyFun()
        }
    }
    return notEmptyFun()
}

/**
 * 待检测参数中是否包含空数据，包括空字符串检测
 * @return 有返回true，否则返回false
 */
fun <T, P> T.haveEmptyCheck(vararg params: P): Boolean {
    params.forEach {
        if (it.isEmpty()) {
            return true
        }
    }
    return false
}

/**
 * 待检测参数中是否全部是空数据，包括空字符串检测
 * @return 有返回true，否则返回false
 */
fun <T, P> T.allEmptyCheck(vararg params: P): Boolean {
    params.forEach {
        if (!it.isEmpty()) {
            return false
        }
    }
    return true
}

/**
 * 检测数据是否为空，仅仅是空，仅仅是null判断
 */
fun <T> T?.isNull(): Boolean {
    return this == null
}

/**
 * 检测数据是否不为空，仅仅是空，仅仅是null判断
 */
fun <T> T?.isNotNull(): Boolean {
    return this != null
}

/**
 * 数据为null检测，为null时执行fun
 */
inline fun <T> T?.nullCheck(exec: () -> Unit) {
    if (this.isNull()) {
        exec()
    }
}

/**
 * 数据不为null检测，不为null时执行fun
 */
inline fun <T> T?.notNullCheck(exec: (T) -> Unit) {
    if (this.isNotNull()) {
        exec(this!!)
    }
}

/**
 * 数据null检测，分情况执行fun
 */
inline fun <T, R> T?.nullCheck(nullFun: () -> R, notNullFun: () -> R): R {
    return if ((this.isNull())) {
        nullFun()
    } else {
        notNullFun()
    }
}

/**
 * 待检测参数中是否有null数据
 */
inline fun <T, P, R> T?.haveNullCheck(vararg params: P, nullFun: () -> R, notNullFun: () -> R): R {
    params.forEach {
        if (it.isNull()) {
            return nullFun()
        }
    }
    return notNullFun()
}

/**
 * 待检测参数中是否包含null
 * @return 有返回true，否则返回false
 */
fun <T, P> T.haveNullCheck(vararg params: P): Boolean {
    params.forEach {
        if (it.isNull()) {
            return true
        }
    }
    return false
}

/**
 * 待检测参数中是否全部是null
 * @return 全是返回true，否则返回false
 */
fun <T, P> T.allNullCheck(vararg params: P): Boolean {
    params.forEach {
        if (!it.isNull()) {
            return false
        }
    }
    return true
}

/**
 * 获取非空数据
 */
fun <T> T?.getNotEmptyData(defaultData: T): T {
    return if (this.isEmpty()) {
        defaultData
    } else {
        this!!
    }
}

/**
 * 数据转换成指定的数据格式类型
 */
inline fun <reified T> Any.formatConversion(): T? {
    return if (this is T) {
        this
    } else {
        null
    }
}
