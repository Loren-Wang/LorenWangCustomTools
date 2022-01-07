package kotlinbase.lorenwang.tools.extend

import android.lorenwang.tools.app.AtlwThreadUtil
import android.lorenwang.tools.app.AtlwViewUtil
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.FloatRange

/**
 * 功能作用：view控件扩展
 * 创建时间：2020-02-24 10:24
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 单个控件调用：View，集合控件调用：Collection<View?>
 * 设置单个或集合控件为显示（kttlwToVisible）
 * 设置单个或集合控件为隐藏（kttlwToGone）
 * 设置单个或集合控件为占位不显示（kttlwToInvisible）
 * 设置单个或集合控件为启用状态（kttlwToEnable）
 * 设置单个或集合控件为禁用状态（kttlwToDisable）
 * 设置单个或集合控件的防重点击（kttlwThrottleClick）
 * 设置单个控件宽高（kttlwSetWidthHeight、kttlwSetWidthHeightForWidth、kttlwSetWidthHeightForHeight）
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

/**
 * 设置控件为显示
 */
fun <V : View> V?.kttlwToVisible() {
    this?.visibility = View.VISIBLE
}

/**
 * 设置部分控件为显示状态
 */
fun <V : View> Collection<V?>.kttlwToVisible() {
    this.forEach {
        it?.visibility = View.VISIBLE
    }
}

/**
 * 设置控件为隐藏
 */
fun <V : View> V?.kttlwToGone() {
    this?.visibility = View.GONE
}

/**
 * 设置部分控件为隐藏状态
 */
fun <V : View> Collection<V?>.kttlwToGone() {
    this.forEach {
        it?.visibility = View.GONE
    }
}

/**
 * 设置控件为不显示但是占位
 */
fun <V : View> V?.kttlwToInvisible() {
    this?.visibility = View.INVISIBLE
}

/**
 * 设置部分控件为占位不显示状态
 */
fun <V : View> Collection<V?>.kttlwToInvisible() {
    this.forEach {
        it?.visibility = View.INVISIBLE
    }
}

/**
 * 设置控件为启用
 */
fun <V : View> V?.kttlwToEnable() {
    this?.isEnabled = true
}

/**
 * 设置所有控件为启用
 */
fun <V : View> Collection<V?>.kttlwToEnable() {
    this.forEach {
        it?.isEnabled = true
    }
}

/**
 * 设置控件为禁用
 */
fun <V : View> V?.kttlwToDisable() {
    this?.isEnabled = false
}

/**
 * 设置所有控件为禁用
 */
fun <V : View> Collection<V?>.kttlwToDisable() {
    this.forEach {
        it?.isEnabled = false
    }
}

/**
 * 设置所有控件宽高
 */
fun <V : View> V?.kttlwSetWidthHeight(width : Int, height : Int) {
    AtlwViewUtil.getInstance().setViewWidthHeight(this, width, height)
}

/**
 * 设置所有控件宽高
 * @param widthPercentForScreen 宽度相当于屏幕宽度的比例
 * @param aspectRatio 宽/高比例
 */
fun <V : View> V?.kttlwSetWidthHeightForWidth(@FloatRange(from = 0.0, to = 1.0) widthPercentForScreen : Float, aspectRatio : Float) {
    this.kttlwSetWidthHeightForWidth((widthPercentForScreen * kttlwGetScreenWidth()).toInt(), aspectRatio)
}

/**
 * 设置所有控件宽高
 * @param width 显示的宽度
 * @param aspectRatio 宽/高比例
 */
fun <V : View> V?.kttlwSetWidthHeightForWidth(width : Int, aspectRatio : Float) {
    AtlwViewUtil.getInstance().setViewWidthHeight(this, width, (width / aspectRatio).toInt())
}

/**
 * 设置所有控件宽高
 * @param heightPercentForScreen 高度相当于屏幕高度的比例
 * @param aspectRatio 宽/高比例
 */
fun <V : View> V?.kttlwSetWidthHeightForHeight(@FloatRange(from = 0.0, to = 1.0) heightPercentForScreen : Float, aspectRatio : Float) {
    this.kttlwSetWidthHeightForHeight((heightPercentForScreen * kttlwGetScreenHeight()).toInt(), aspectRatio)
}

/**
 * 设置所有控件宽高
 * @param height 显示的高度
 * @param aspectRatio 宽/高比例
 */
fun <V : View> V?.kttlwSetWidthHeightForHeight(height : Int, aspectRatio : Float) {
    AtlwViewUtil.getInstance().setViewWidthHeight(this, (height * aspectRatio).toInt(), height)
}

/**
 * 设置多控件同样的点击事件监听
 */
fun <V : View> Collection<V?>.kttlwThrottleClick(clickFun : (view : View) -> Unit) {
    this.forEach {
        it?.kttlwThrottleClick(clickFun)
    }
}

/**
 * 防重点击
 * @param clickFun 点击执行函数
 */
fun <V : View> V?.kttlwThrottleClick(clickFun : (view : View) -> Unit) {
    this.kttlwThrottleClick(500, clickFun)
}

/**
 * 防重点击
 * @param timeInterval 两次点击之间的时间间隔
 * @param clickFun 点击执行函数
 */
fun <V : View> V?.kttlwThrottleClick(timeInterval : Long, clickFun : (view : View) -> Unit) {
    this?.setOnClickListener {
        //本地是启用状态下点击才有效
        if (it?.isEnabled.kttlwGetNotEmptyData(false)) {
            it.kttlwToDisable()
            clickFun(it)
            AtlwThreadUtil.getInstance().postOnUiThreadDelayed({
                it.kttlwToEnable()
            }, timeInterval)
        }
    }
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