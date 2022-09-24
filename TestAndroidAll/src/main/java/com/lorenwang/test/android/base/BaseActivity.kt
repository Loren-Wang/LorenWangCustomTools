package com.lorenwang.test.android.base

import android.graphics.Color
import android.lorenwang.commonbaseframe.mvp.AcbflwBaseActivity
import android.lorenwang.customview.dialog.AvlwLoadingDialogType1
import android.lorenwang.tools.app.AtlwToastHintUtil
import android.os.Bundle
import android.view.ViewStub
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import javabase.lorenwang.dataparse.JdplwJsonUtil
import javabase.lorenwang.tools.common.JtlwCheckVariateUtil
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
    var swipeRefresh: SwipeRefreshLayout? = null

    override fun showBaseLoading(allowLoadingBackFinishPage: Boolean, data: String?) {
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

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initListener(savedInstanceState: Bundle?) {

    }

    override fun onRefreshData() {

    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    fun addShowContentView(b: Boolean, binding: ViewBinding?) {
        mContentView = binding?.root
        super.setContentViewConfig(null)
    }

    /**
     * 网络请求成功
     * @param data 响应数据
     * @param netOptionReqCode 网络操作请求code
     */
    override fun <T> netReqSuccess(netOptionReqCode: Int, data: T, result: String?) {
    }

    /**
     * 网络请求失败
     * @param netOptionReqCode 网络操作请求code
     * @param message 错误信息
     */
    override fun netReqFail(netOptionReqCode: Int, code: String, message: String?) {
        if (JtlwCheckVariateUtil.getInstance().isNotEmpty(message)) {
            val bean = JdplwJsonUtil.fromJson(message, KttlwBaseNetResponseBean::class.java)
            if (bean != null) {
                if (JtlwCheckVariateUtil.getInstance().isNotEmpty(bean.stateMessage)) {
                    AtlwToastHintUtil.getInstance().toastMsg(bean.stateMessage)
                }
            } else {
                AtlwToastHintUtil.getInstance().toastMsg(message)
            }
        }
    }


}
