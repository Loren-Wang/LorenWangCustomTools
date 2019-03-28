package android.lorenwang.customview.tablayout

import java.util.*

/**
 * 创建时间：2019-03-28 上午 11:33:33
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
interface BaseHorizontalSlipTabLayout {
    /**
     * 设置tab列表
     */
    fun setTabList(tabList: ArrayList<String>?, selectPosi: Int?)

    /**
     * 跳转到指定位置
     */
    fun skipToPosi(posi: Int)

    /**
     * 滑动到指定位置，带百分比滑动
     */
    fun slipToPosi(slipToPosi: Int, percent: Float)

    /**
     * 滑动跳转到指定位置
     */
    fun slipSkipToPosi(slipToPosi: Int)
}
