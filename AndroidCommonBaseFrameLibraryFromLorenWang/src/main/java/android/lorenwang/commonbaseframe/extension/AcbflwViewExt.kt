package android.lorenwang.commonbaseframe.extension

import android.lorenwang.commonbaseframe.AcbflwNoDoubleClickListener
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import kotlinbase.lorenwang.tools.extend.kttlwEmptyCheck
import kotlinbase.lorenwang.tools.extend.kttlwFormatConversion

/**
 * 防重点击
 */
inline fun View.setOnNoDoubleClick(delayTime: Long, msg: String = "", crossinline block: View.() -> Unit) {
    setOnClickListener(object : AcbflwNoDoubleClickListener(delayTime, msg) {
        override fun onNoDoubleClick(view: View?) {
            block()
        }
    })
}

/**
 * 防重点击
 */
inline fun View.setOnNoDoubleClick(crossinline block: View.() -> Unit) {
    this.setOnNoDoubleClick(500L, "", block)
}

/**
 * 设置控件显示
 */
fun View?.setViewToVisible() {
    this?.visibility = View.VISIBLE
}

/**
 * 设置控件隐藏
 */
fun View?.setViewToGone() {
    this?.visibility = View.GONE
}

/**
 * 设置控件占位
 */
fun View?.setViewToInvisible() {
    this?.visibility = View.INVISIBLE
}

/**
 * 添加输入法action操作处理监听
 */
fun EditText?.addInputManagerActionsListener(actionId: Int, listener: (v: TextView?, actionId: Int, event: KeyEvent?) -> Boolean) {
    //hashCode可能会被其他的使用的的，所以再随便加个数
    this?.getTag(this.hashCode() + 1000023121).kttlwFormatConversion<ListOnEditorActionListener>().kttlwEmptyCheck({
        this?.setOnEditorActionListener(ListOnEditorActionListener().also {
            it.addAction(actionId, listener)
            this.setTag(this.hashCode() + 1000023121, it)
        })
    }, {
        it.addAction(actionId, listener)
    })
}

/**
 * action操作监听
 */
private class ListOnEditorActionListener : TextView.OnEditorActionListener {
    /**
     * actionId和触发事件处理
     */
    private val mMapAction = hashMapOf<Int, (v: TextView?, actionId: Int, event: KeyEvent?) -> Boolean>()

    /**
     * 添加行为监听
     */
    fun addAction(actionId: Int, listener: (v: TextView?, actionId: Int, event: KeyEvent?) -> Boolean) {
        mMapAction[actionId] = listener
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        mMapAction[actionId]?.let {
            return it(v, actionId, event)
        }
        return false
    }
}