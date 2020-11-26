package kotlinbase.lorenwang.tools.extend

import java.util.*
import kotlin.collections.ArrayList

/**
 * 功能作用：集合扩展
 * 创建时间：2020-11-23 11:19 上午
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

/**
 * 集合item由原本上下排列转左右排列，以逻辑显示居多
 * 例如：0 3 6 9
 *      1 4 7 10
 *      2 5 8 11
 *
 * 转换为：0 1 2  3
 *        4 5 6  7
 *        8 9 10 11
 *
 * @param rowCount 行数量，转换前后一致
 */
fun <T> Collection<T>.kttlwItemUpDownToLeftRight(rowCount : Int) : ArrayList<T> {
    //浅复制列表，保证有数据，使其指定位置可以直接赋值
    val list = ArrayList<T>(this)
    //获取总列数
    val columns = if (this.size % rowCount > 0) {
        this.size / rowCount + 1
    } else {
        this.size / rowCount
    }
    //开始赋值
    this.forEachIndexed { index, t ->
        list[(index % columns) * rowCount + index / columns] = t
    }
    return list
}

/**
 * 集合item由原本上下排列转为水平排列欢唱显示，为空代表着只需要占位
 * 例如：0 3 6 9
 *      1 4 7 10
 *      2 5 8 11
 *
 * 转换为：0  1  2  3  4  5  6
 *        7  8  9  10 11
 * @param rowCount 行数量
 * @param columnCount 列数量
 */
fun <T> Collection<T>.kttlwItemUpDownToHorizontal(columnCount : Int) : ArrayList<T?> {
    //获取总行数
    val rows = if (this.size % columnCount > 0) {
        this.size / columnCount + 1
    } else {
        this.size / columnCount
    }
    //初始化集合数据，使其指定位置可以直接赋值，并且所有位置都为null，在返回是没有设置为null的显示，null的位置占位隐藏
    val list = ArrayList<T?>(rows * columnCount)
    for (i in 0 until rows * columnCount) {
        list.add(null)
    }
    //开始赋值
    this.forEachIndexed { index, t ->
        list[(index % columnCount) * rows + index / columnCount] = t
    }
    return list
}
