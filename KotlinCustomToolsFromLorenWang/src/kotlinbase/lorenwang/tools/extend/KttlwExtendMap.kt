package kotlinbase.lorenwang.tools.extend

/**
 * 功能作用：Map函数扩展
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
fun <R> Map<String, Any?>.kttlwParseJsonData(cls: Class<R>): R? {
    return try {
        JsonUtils.fromJson(this, cls)
    } catch (e: Exception) {
        null
    }
}

/**
 * 获取非空数据
 */
fun <K, V> Map<K, V>?.kttlwGetNotEmptyData(defaultData: Map<K, V> = mapOf()): Map<K, V> {
    return if (this.isNullOrEmpty()) {
        defaultData
    } else {
        this
    }
}
