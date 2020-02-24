package kotlinbase.lorenwang.tools.extend

import android.view.View

/**
 * 功能作用：view控件扩展
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
 * 设置控件为显示
 */
fun <V : View> V.toVisible(vararg views: V) {
    this.visibility = View.VISIBLE
    views.forEach {
        it.visibility = View.VISIBLE
    }
}

/**
 * 设置控件为隐藏
 */
fun <V : View> V.toGone(vararg views: V) {
    this.visibility = View.GONE
    views.forEach {
        it.visibility = View.GONE
    }
}

/**
 * 设置控件为不显示但是占位
 */
fun <V : View> V.toInvisible(vararg views: V) {
    this.visibility = View.INVISIBLE
    views.forEach {
        it.visibility = View.INVISIBLE
    }
}

/**
 * 设置所有控件为启用
 */
fun <V : View> V.allViewToEnable(vararg views: V) {
    this.isEnabled = true
    views.forEach {
        it.isEnabled = true
    }
}

/**
 * 设置所有控件为禁用
 */
fun <V : View> V.allViewToDisable(vararg views: V) {
    this.isEnabled = false
    views.forEach {
        it.isEnabled = false
    }
}

/**
 * 设置多控件同样的点击事件监听
 */
fun <V : View> V.setOnClickListenerWith(vararg views: View, listener: View.OnClickListener) {
    this.setOnClickListener(listener)
    views.forEach { it ->
        it.setOnClickListener { listener.onClick(it) }
    }
}
