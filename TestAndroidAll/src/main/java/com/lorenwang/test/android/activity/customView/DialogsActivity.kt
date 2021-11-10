package com.lorenwang.test.android.activity.customView

import android.lorenwang.customview.dialog.*
import android.lorenwang.tools.app.AtlwScreenUtil
import android.lorenwang.tools.app.AtlwToastHintUtil
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import com.lorenwang.test.android.bean.dialog.TextSelectBean
import com.lorenwang.test.android.activity.customView.dialog.TextSelectDialog
import com.lorenwang.test.android.activity.customView.dialog.LoadingDialog
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData


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
    private lateinit var confirmCancelDialog: AvlwConfirmCancelDialog1
    private lateinit var onlyButtonDialog: AvlwConfirmCancelDialog1
    private lateinit var rightButtonDialog: AvlwBaseRightDialog

    /**
     * 文本选择弹窗
     */
    private var textSelectDialog: TextSelectDialog? = null
        get() {
            field = field.kttlwGetNotEmptyData { TextSelectDialog(this) }
            return field
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
            R.id.btnDialogConfirmCancel -> {
                showDialog(confirmCancelDialog)
            }
            R.id.btnDialogOnlyButton -> {
                showDialog(onlyButtonDialog)
            }
            R.id.btnDialogRight -> {
                showDialog(rightButtonDialog)
            }
            R.id.btn_text_select -> {
                textSelectDialog?.showTextListSelect("你猜猜", "不知道", 1,
                    arrayListOf(TextSelectBean().also { it.text = "1" }, TextSelectBean().also { it.text = "2" },
                        TextSelectBean().also { it.text = "3" }, TextSelectBean().also { it.text = "4" }, TextSelectBean().also { it.text = "5" }),
                    { type, item ->
                        if (type == 1) {
                            AtlwToastHintUtil.getInstance().toastMsg(item?.text)
                        }
                    }, "3", R.style.RadioButtonStyle)
            }
            R.id.btn_loading_true -> {
                LoadingDialog(this).show(true)
            }
            R.id.btn_loading_false -> {
                LoadingDialog(this).show(false)
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

    /**
     * 初始化view
     */
    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_custom_view_dialogs)
        bottomDialog = AvlwBaseBottomDialog(this, R.layout.dialog_content, true)

        centerDialog = AvlwBaseCenterDialog(this, R.layout.dialog_content, true, (AtlwScreenUtil.getInstance().screenWidth * 0.6).toInt(), null)



        bottomWebViewDialog = AvlwBaseWebViewDialog(this, R.layout.dialog_webview, R.style.AvlwLayoutDialogBottom, R.style.AvlwAnimDialogBottom, true,
            ViewGroup.LayoutParams.MATCH_PARENT, null, Gravity.BOTTOM)
        bottomWebViewDialog.initWebView(R.id.webView, "https://www.jianshu.com/p/56e2b0bf9ab2")



        centerWebViewDialog = AvlwBaseWebViewDialog(this, R.layout.dialog_webview, R.style.AvlwLayoutDialogCenter, R.style.AvlwAnimDialogCenter, true,
            ViewGroup.LayoutParams.MATCH_PARENT, null, Gravity.CENTER)
        centerWebViewDialog.initWebView(R.id.webView, "https://www.jianshu.com/p/56e2b0bf9ab2")



        confirmCancelDialog = AvlwConfirmCancelDialog1(this)
        confirmCancelDialog.setBtnLeft("取消", null, null) { confirmCancelDialog.dismiss() }
        confirmCancelDialog.setBtnRight("确定", null, null) { confirmCancelDialog.dismiss() }
        confirmCancelDialog.setContent("确定||取消 确定||取消 确定||取消 确定||取消", null, null)



        onlyButtonDialog = AvlwConfirmCancelDialog1(this)
        onlyButtonDialog.setOptionsState(true, false, 100)
        onlyButtonDialog.setBtnLeft("确定", null, null) { onlyButtonDialog.dismiss() }
        onlyButtonDialog.setContent("确定||取消 确定||取消 确定||取消 确定||取消", null, null)

        rightButtonDialog = AvlwBaseRightDialog(this, R.layout.dialog_content, true, (AtlwScreenUtil.getInstance().screenWidth).toInt(),
            ViewGroup.LayoutParams.MATCH_PARENT)
    }
}
