package com.example.testapp.dialog

import android.lorenwang.customview.dialog.AvlwBaseBottomDialog
import android.lorenwang.customview.dialog.AvlwBaseCenterDialog
import android.lorenwang.customview.dialog.AvlwBaseDialog
import android.lorenwang.customview.dialog.AvlwBaseWebViewDialog
import android.os.Bundle
import android.view.View
import com.example.testapp.BaseActivity
import com.example.testapp.R


/**
 * 功能作用：所有弹窗样例页面
 * 初始注释时间： 2020/3/5 14:45
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class DialogsActivity : BaseActivity() {
    private lateinit var bottomDialog: AvlwBaseBottomDialog
    private lateinit var centerDialog: AvlwBaseCenterDialog
    private lateinit var bottomWebViewDialog: AvlwBaseWebViewDialog
    private lateinit var centerWebViewDialog: AvlwBaseWebViewDialog

    override fun onChildCreate(savedInstanceState: Bundle?) {
        addChildView(R.layout.activity_dialogs)
        bottomDialog = AvlwBaseBottomDialog(this, R.layout.dialog_content, true)
        centerDialog = AvlwBaseCenterDialog(this, R.layout.dialog_content, true)
        bottomWebViewDialog = AvlwBaseWebViewDialog(this, R.layout.dialog_webview, R.style.avlw_dialog_bottom_list_options_type_1,
                R.style.avlw_dialog_anim_for_bottom, true, true, false)
        centerWebViewDialog = AvlwBaseWebViewDialog(this, R.layout.dialog_webview, R.style.avlw_dialog_confirm_cancel_1,
                R.style.avlw_dialog_anim_for_center, true, false, false)
        bottomWebViewDialog.initWebView(R.id.webView, "https://www.jianshu.com/p/56e2b0bf9ab2")
        centerWebViewDialog.initWebView(R.id.webView, "https://www.jianshu.com/p/56e2b0bf9ab2")
    }

    /**
     * 弹窗点击事件
     */
    fun dialogOnClick(view: View) {
        when (view.id) {
            R.id.btnDialogBottom -> {
                showDialog(bottomDialog)
            }
            R.id.btnDialogCenter -> {
                showDialog(centerDialog)
            }
            R.id.btnDialogBottomWebView -> {
                showDialog(bottomWebViewDialog)
            }
            R.id.btnDialogCenterWebView -> {
                showDialog(centerWebViewDialog)
            }
            else -> {

            }
        }
    }

    fun showDialog(dialog: AvlwBaseDialog) {
        if (!dialog.isShowing) {
            dialog.show()
        }
    }
}
