package com.example.testapp.base

import android.graphics.Color
import android.lorenwang.commonbaseframe.AcbflwBaseFragment
import android.lorenwang.customview.dialog.AvlwLoadingDialogType1
import android.lorenwang.tools.app.AtlwToastHintUtil
import javabase.lorenwang.dataparse.JdplwJsonUtils
import javabase.lorenwang.tools.common.JtlwCheckVariateUtils
import kotlinbase.lorenwang.tools.common.bean.KttlwBaseNetResponseBean

/**
 * 功能作用：
 * 创建时间：2020-09-27 10:49 上午
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
abstract class BaseFragment : AcbflwBaseFragment() {
    private var loadingDialog: AvlwLoadingDialogType1? = null//加载中弹窗

    override fun showBaseLoading(allowLoadingBackFinishPage: Boolean) {
        if (loadingDialog == null) {
            loadingDialog = AvlwLoadingDialogType1(activity)
            loadingDialog?.setProgressBarColor(Color.BLUE);
        }
        if (!loadingDialog!!.isShowing()) {
            loadingDialog?.show()
        }
    }

    override fun hideBaseLoading() {
        if (loadingDialog != null && loadingDialog!!.isShowing()) {
            loadingDialog?.dismiss()
        }
    }

    /**
     * 用户登陆状态异常
     */
    override fun userLoginStatusError(code: Any?, message: String?) {
        hideBaseLoading()
    }

    /**
     * 网络请求成功
     * @param data 响应数据
     * @param netOptionReqCode 网络操作请求code
     */
    override fun <T> netReqSuccess(netOptionReqCode: Int, data: T) {
    }

    /**
     * 网络请求失败
     * @param netOptionReqCode 网络操作请求code
     * @param message 错误信息
     */
    override fun netReqFail(netOptionReqCode: Int, message: String?) {
        if (JtlwCheckVariateUtils.getInstance().isNotEmpty(message)) {
            val bean = JdplwJsonUtils.fromJson(message,
                    KttlwBaseNetResponseBean::class.java)
            if (bean != null) {
                if (JtlwCheckVariateUtils.getInstance().isNotEmpty(bean.stateMessage)) {
                    AtlwToastHintUtil.getInstance().toastMsg(bean.stateMessage)
                }
            } else {
                AtlwToastHintUtil.getInstance().toastMsg(message)
            }
        }
    }
}
