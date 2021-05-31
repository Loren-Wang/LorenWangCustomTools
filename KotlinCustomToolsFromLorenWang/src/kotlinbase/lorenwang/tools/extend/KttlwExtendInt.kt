package kotlinbase.lorenwang.tools.extend

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
 * 获取非空数据
 */
fun Int?.kttlwGetNotEmptyData(defaultStr: Int): Int {
    return if (this.kttlwIsNull()) {
        defaultStr
    } else {
        this!!
    }
}
