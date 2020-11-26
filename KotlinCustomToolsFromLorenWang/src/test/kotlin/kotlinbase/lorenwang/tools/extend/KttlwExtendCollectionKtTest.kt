package kotlinbase.lorenwang.tools.extend

import org.junit.Assert.*
import org.junit.Test

/**
 * 功能作用：
 * 创建时间：2020-11-23 1:43 下午
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
class KttlwExtendCollectionKtTest {
    @Test
    fun kttlwItemUpDownToLeftRightTest() {
        arrayListOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11).kttlwItemUpDownToLeftRight(3)
    }

    @Test
    fun kttlwItemUpDownToHorizontalTest() {
        arrayListOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11).kttlwItemUpDownToHorizontal(7)
    }
}
