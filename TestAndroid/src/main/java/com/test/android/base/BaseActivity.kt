package com.test.android.base

import android.graphics.Color
import android.lorenwang.common_base_frame.AcbflwBaseActivity
import android.lorenwang.customview.dialog.AvlwLoadingDialogType1
import android.os.Bundle
import android.view.ViewStub
import androidx.annotation.LayoutRes
import com.test.android.R

/**
 * 功能作用：基础Activity
 * 初始注释时间： 2020/4/5 2:29 下午
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 *@author LorenWang（王亮）
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
     * 用户登录状态异常处理
     */
    override fun userLoginStatusError(code: Any?, message: String?) {
    }

    /**
     * 网络请求成功，某些情况下会有通用基础响应需要在基础类中处理
     */
    override fun <T> netReqSuccess(netOptionReqCode: Int, data: T?) {
    }
    /**
     * 网络请求失败，某些情况下会有通用基础响应需要在基础类中处理
     */
    override fun netReqFail(netOptionReqCode: Int, code: Any?, message: String?) {
    }

}
