package com.example.testapp.base

import android.graphics.Color
import android.lorenwang.commonbaseframe.AcbflwBaseFragment
import android.lorenwang.customview.dialog.AvlwLoadingDialogType1
import android.lorenwang.tools.app.AtlwToastHintUtils
import android.view.ViewStub
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
    private lateinit var vsbContent: ViewStub

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
     * 限流挡板异常数据返回
     *
     * @param netOptionReqCode 网络请求code
     * @param repBean          限流挡板数据
     */
    override fun currentLimitingBaffleError(netOptionReqCode: Int, repBean: KttlwBaseNetResponseBean<Any>) {
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
                    AtlwToastHintUtils.getInstance().toastMsg(bean.stateMessage)
                }
            } else {
                AtlwToastHintUtils.getInstance().toastMsg(message)
            }
        }
    }
}