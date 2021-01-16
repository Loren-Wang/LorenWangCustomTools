package kotlinbase.lorenwang.tools.extend

import java.math.BigDecimal

/**
 * 功能作用：BigDecimal扩展
 * 创建时间：2020-06-28 10:32 上午
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren wang）
 */
/**
 * 获取非空数据
 */
fun BigDecimal?.kttlwGetNotEmptyData(defaultData: BigDecimal = BigDecimal.ZERO): BigDecimal {
    return if (this.kttlwIsEmpty()) {
        defaultData
    } else {
        this!!
    }
}
