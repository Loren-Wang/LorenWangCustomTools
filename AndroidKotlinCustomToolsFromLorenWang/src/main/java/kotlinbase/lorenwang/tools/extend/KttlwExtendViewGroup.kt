package kotlinbase.lorenwang.tools.extend

import android.view.View
import android.view.ViewGroup

/**
 * 功能作用：viewgroup 扩展
 * 创建时间：2020-02-24 10:24
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
/**
 * 子控件遍历
 */
fun <T : ViewGroup> T?.forEachChild(exe: (Int, View) -> Unit) {
    this?.let {
        for (i in 0 until it.childCount) {
            exe(i, it.getChildAt(i))
        }
    }
}
