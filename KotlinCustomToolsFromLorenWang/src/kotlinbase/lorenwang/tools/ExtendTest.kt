package kotlinbase.lorenwang.tools

import kotlinbase.lorenwang.tools.extend.kttlwIfFalse
import kotlinbase.lorenwang.tools.extend.kttlwIfTrue


/**
 * 功能作用：扩展测试
 * 创建时间：2020-11-01 2:51 下午
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
open class ExtendTest {
    init {
        println("boolean:" + 0.kttlwIfFalse().toString())
        println("boolean:" + 0.kttlwIfTrue().toString())
    }
}
