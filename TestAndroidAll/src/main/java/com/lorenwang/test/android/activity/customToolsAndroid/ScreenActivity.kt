package com.lorenwang.test.android.activity.customToolsAndroid

import android.lorenwang.tools.app.AtlwScreenUtil
import android.lorenwang.tools.app.AtlwToastHintUtil
import android.view.View
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity

/**
 * 功能作用：屏幕相关页面
 * 初始注释时间： 2021/9/18 13:29
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
class ScreenActivity : BaseActivity() {

    override fun setContentViewConfig(resId: Int?)  {
        super.setContentViewConfig(R.layout.activity_custom_tools_android_screen)
    }
    fun mainClick(view: View?) {
        if (view != null) {
            val size = (Math.random() * 100).toInt()
            when (view.id) {
                R.id.btnDpToPx -> {
                    AtlwToastHintUtil.getInstance().toastMsg("dp大小：${size},转换后px大小：${AtlwScreenUtil.getInstance().dip2px(size.toFloat())}")
                }
                R.id.btnPxToDp -> {
                    AtlwToastHintUtil.getInstance().toastMsg("px大小：${size},转换后dp大小：${AtlwScreenUtil.getInstance().px2dip(size.toFloat())}")
                }
                R.id.btnSpToPx -> {
                    AtlwToastHintUtil.getInstance().toastMsg("sp大小：${size},转换后px大小：${AtlwScreenUtil.getInstance().sp2px(size.toFloat())}")
                }
                R.id.btnPxToSp -> {
                    AtlwToastHintUtil.getInstance().toastMsg("px大小：${size},转换后sp大小：${AtlwScreenUtil.getInstance().px2sp(size.toFloat())}")
                }
                R.id.btnWidth -> {
                    AtlwToastHintUtil.getInstance().toastMsg("当前屏幕宽度：${AtlwScreenUtil.getInstance().screenWidth}")
                }
                R.id.btnHeight -> {
                    AtlwToastHintUtil.getInstance().toastMsg("当前屏幕高度：${AtlwScreenUtil.getInstance().screenHeight}")
                }
                R.id.btnStatusHeight -> {
                    AtlwToastHintUtil.getInstance().toastMsg("当前状态栏高度：${AtlwScreenUtil.getInstance().statusBarHeight}")
                }
                R.id.btnChangeWidth -> {
                    AtlwToastHintUtil.getInstance().toastMsg("根据设计稿控件宽度转换成当前屏幕的宽度：转换前-${size},转换后-${
                        AtlwScreenUtil.getInstance().getShowPixelValueForWidth(size)
                    }")
                }
                R.id.btnSupplementHeight -> {
                    AtlwToastHintUtil.getInstance().toastMsg("小米补充高度：${AtlwScreenUtil.getInstance().miSupplementHeight}")
                }
                else -> {

                }
            }
        }
    }
}
