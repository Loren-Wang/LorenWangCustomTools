package com.example.testapp

import android.app.Activity
import android.graphics.Color
import android.lorenwang.customview.dialog.LoadingDialogType1

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
open class BaseActivity : Activity() {
    private var loadingDialog: LoadingDialogType1? = null//加载中弹窗
    fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialogType1(this)
            loadingDialog?.setProgressBarColor(Color.BLUE);
        }
        if (!loadingDialog!!.isShowing()) {
            loadingDialog?.show()
        }
    }

    fun hideLoading() {
        if (loadingDialog != null && loadingDialog!!.isShowing()) {
            loadingDialog?.dismiss()
        }
    }

}
