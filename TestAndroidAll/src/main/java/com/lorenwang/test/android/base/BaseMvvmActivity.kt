package com.lorenwang.test.android.base

import android.graphics.Color
import android.lorenwang.commonbaseframe.mvvm.AcbflwMvvmActivity
import android.lorenwang.commonbaseframe.mvvm.AcbflwMvvmVModel
import android.lorenwang.customview.dialog.AvlwLoadingDialogType1
import android.os.Bundle
import androidx.viewbinding.ViewBinding

abstract class BaseMvvmActivity<VM : AcbflwMvvmVModel, VB : ViewBinding> : AcbflwMvvmActivity<VM, VB>() {
    private var loadingDialog: AvlwLoadingDialogType1? = null//加载中弹窗

    override fun initListener(savedInstanceState: Bundle?) {

    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun onRefreshData() {

    }


    override fun showBaseLoading(allowLoadingBackFinishPage: Boolean, data: String?) {
        if (loadingDialog == null) {
            loadingDialog = AvlwLoadingDialogType1(this)
            loadingDialog?.setProgressBarColor(Color.BLUE);
        }
        if (!loadingDialog!!.isShowing) {
            loadingDialog?.show()
        }
    }

    override fun hideBaseLoading() {
        if (loadingDialog != null && loadingDialog!!.isShowing) {
            loadingDialog?.dismiss()
        }
    }

}