package kotlinbase.lorenwang.tools.extend

import android.lorenwang.tools.app.AtlwScreenUtil

/**
 * 功能作用：其他扩展
 * 初始注释时间： 2020/12/3 10:36 上午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 获取屏幕宽度(kttlwGetScreenWidth)
 * 获取屏幕高度(kttlwGetScreenHeight)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */

/**
 * 获取屏幕宽度
 */
fun kttlwGetScreenWidth(): Int {
    return AtlwScreenUtil.getInstance().screenWidth
}

/**
 * 获取屏幕高度
 */
fun kttlwGetScreenHeight(): Int {
    return AtlwScreenUtil.getInstance().screenHeight
}
