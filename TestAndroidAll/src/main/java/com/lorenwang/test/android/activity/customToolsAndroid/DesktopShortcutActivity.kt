package com.lorenwang.test.android.activity.customToolsAndroid

import android.graphics.BitmapFactory
import android.lorenwang.tools.app.AtlwToastHintUtil
import android.lorenwang.tools.desktopShortcut.AtlwDesktopShortcutUtil
import android.lorenwang.tools.desktopShortcut.DesktopShortcutOptionsCallback
import android.os.Bundle
import android.view.View
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity

/**
 * 功能作用：快捷方式工具页面
 * 初始注释时间： 2021/9/18 17:44
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
class DesktopShortcutActivity : BaseActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_custom_tools_android_desktop_shortcut)
    }

    override fun initListener(savedInstanceState: Bundle?) {
        super.initListener(savedInstanceState)
        AtlwDesktopShortcutUtil.getInstance().setDesktopShortcutOptionsCallback(object : DesktopShortcutOptionsCallback {
            override fun addSuccess() {
                AtlwToastHintUtil.getInstance().toastMsg("添加快捷方式成功")
            }

            override fun fail() {
                AtlwToastHintUtil.getInstance().toastMsg("添加快捷方式失败")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        AtlwDesktopShortcutUtil.getInstance().setDesktopShortcutOptionsCallback(null)
    }

    fun mainClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btnAdd -> {
                    AtlwDesktopShortcutUtil.getInstance().addDesktopShortcut(this, this.javaClass, "ssss", "你猜", "http://www.baidu.com",
                        BitmapFactory.decodeResource(resources, R.mipmap.avlw_icon_arrow_left))
                }
                else -> {

                }
            }
        }
    }
}
