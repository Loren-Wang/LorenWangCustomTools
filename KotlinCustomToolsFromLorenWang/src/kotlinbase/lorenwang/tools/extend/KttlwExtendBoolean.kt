package kotlinbase.lorenwang.tools.extend

/**
 * 功能作用：布尔型数据扩展
 * 创建时间：2020-02-24 10:23
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
/**
 *如果是false值情况下处理
 */
fun <R> Boolean?.kttlwIfFalse(exe: () -> R): R? {
    return if (this == null || this.not()) {
        exe()
    } else {
        null
    }
}

/**
 * 如果是true值的情况下处理
 */
fun <R> Boolean?.kttlwIfTrue(exe: () -> R): R? {
    return if (this != null && this) {
        exe()
    } else {
        null
    }
}

/**
 * 值状态处理，分true和false处理
 */
fun <R> Boolean?.kttlwValueStatus(trueFun: () -> R, falseFun: () -> R): R {
    return if (this == null || this.not()) {
        falseFun()
    } else {
        trueFun()
    }
}

/**
 * 布尔值对应的整型数值
 */
fun Boolean?.kttlwToInt(): Int {
    return if (this == null || this.not()) {
        0
    } else {
        1
    }
}

/**
 * 获取非空数据
 */
fun Boolean?.kttlwGetNotEmptyData(defaultData: Boolean = false): Boolean {
    return this ?: defaultData
}

/**
 * 判断数据值是否是确定状态，默认返回非确定状态也就是默认认定数据为false
 */
fun Boolean?.kttlwIfTrue(): Boolean {
    return this == true
}

/**
 * 判断数据值是否是否定状态，默认返回非确定状态也就是默认认定数据为false
 */
fun Boolean?.kttlwIfFalse(): Boolean {
    return this == false
}
