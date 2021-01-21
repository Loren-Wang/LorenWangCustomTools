package com.example.testapp.base

import android.graphics.Color
import android.lorenwang.commonbaseframe.AcbflwBaseActivity
import android.lorenwang.customview.dialog.AvlwLoadingDialogType1
import android.lorenwang.tools.app.AtlwToastHintUtil
import android.view.ViewStub
import javabase.lorenwang.dataparse.JdplwJsonUtils
import javabase.lorenwang.tools.common.JtlwCheckVariateUtils
import kotlinbase.lorenwang.tools.common.bean.KttlwBaseNetResponseBean

/**
 * 创建时间：2019-04-15 下午 15:15:55
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
abstract class BaseActivity : AcbflwBaseActivity() {
    private var loadingDialog: AvlwLoadingDialogType1? = null//加载中弹窗
    private lateinit var vsbContent: ViewStub

    override fun showBaseLoading(allowLoadingBackFinishPage: Boolean) {
        if (loadingDialog == null) {
            loadingDialog = AvlwLoadingDialogType1(this)
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
