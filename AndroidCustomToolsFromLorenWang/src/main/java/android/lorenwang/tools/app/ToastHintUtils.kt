package android.lorenwang.tools.app

import android.content.Context
import android.lorenwang.tools.common.AndJavaCommonUtils
import android.support.annotation.StringRes
import android.widget.Toast

/**
 * 创建时间： 0001/2018/3/1 下午 2:03
 * 创建人：王亮（Loren wang）
 * 功能作用：提示弹窗单例类
 * 功能方法：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class ToastHintUtils(private val context: Context) {
    private var allToast: Toast? = null//吐司提示弹窗，如果有下一个要弹出则隐藏上一个

    /**
     * 显示提示信息
     * @param msg 提示文字
     * @param toastTime 提示时间,为空则使用默认短时间
     */
    fun toastMsg(msg: String, toastTime: Int?) {
        if (checkMsg(msg)) {
            if (allToast != null) {
                allToast!!.cancel()
            }
            allToast = Toast.makeText(context, msg, toastTime ?: Toast.LENGTH_SHORT)
            allToast!!.show()
        }
    }

    /**
     * 显示吐司提示信息
     * @param msgResId 提示文字资源id
     * @param toastTime 提示时间，为空则使用默认短时间
     */
    fun toastMsg(@StringRes msgResId: Int, toastTime: Int?) {
        if (allToast != null) {
            allToast!!.cancel()
        }
        allToast = Toast.makeText(context, msgResId, toastTime ?: Toast.LENGTH_SHORT)
        allToast!!.show()
    }

    /**
     * 检测msg消息是否为空
     * @param msg
     * @return 不为空则返回true
     */
    private fun checkMsg(msg: String?): Boolean {
        return msg != null && !AndJavaCommonUtils.getInstance().isEmpty(msg)
    }

    companion object {
        private var baseUtils: ToastHintUtils? = null
        fun getInstance(context: Context): ToastHintUtils {
            if (baseUtils == null) {
                baseUtils = ToastHintUtils(context)
            }
            return baseUtils as ToastHintUtils
        }
    }


}
