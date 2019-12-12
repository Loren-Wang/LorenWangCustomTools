package com.example.testapp

import android.app.Activity
import android.graphics.Color
import android.lorenwang.customview.dialog.AvlwLoadingDialogType1
import android.os.Bundle
import android.view.ViewStub
import androidx.annotation.LayoutRes

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
abstract class BaseActivity : Activity() {
    private var loadingDialog: AvlwLoadingDialogType1? = null//加载中弹窗
    private lateinit var vsbContent: ViewStub

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        vsbContent = findViewById(R.id.vsbContent)
        onChildCreate(savedInstanceState)
    }

    /**
     * 子类create
     */
    abstract fun onChildCreate(savedInstanceState: Bundle?)

    /**
     * 添加子视图
     */
    fun addChildView(@LayoutRes resId: Int) {
        vsbContent.layoutResource = resId
        vsbContent.inflate()
    }

    fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = AvlwLoadingDialogType1(this)
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
