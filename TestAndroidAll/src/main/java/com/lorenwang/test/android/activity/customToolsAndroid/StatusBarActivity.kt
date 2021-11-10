package com.lorenwang.test.android.activity.customToolsAndroid

import android.graphics.Color
import android.lorenwang.tools.app.AtlwScreenUtil
import android.lorenwang.tools.app.AtlwStatusBarUtil
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity

/**
 * 功能作用：状态栏操作页面
 * 初始注释时间： 2021/9/18 15:41
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
class StatusBarActivity : BaseActivity() {

    private var modeLight = true
    private var full = false

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_custom_tools_android_status_bar)
    }

    fun mainClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btnAlpha -> {
                    full = true
                    AtlwStatusBarUtil.getInstance().transparencyBar(this)
                    findViewById<LinearLayoutCompat>(R.id.lnContent)?.setPadding(0, AtlwScreenUtil.getInstance().statusBarHeight, 0, 0)
                }
                R.id.btnChangeColor -> {
                    AtlwStatusBarUtil.getInstance().setStatusBarColor(this,
                        Color.argb((Math.random() * 255).toInt(), (Math.random() * 255).toInt(), (Math.random() * 255).toInt(),
                            (Math.random() * 255).toInt()))
                }
                R.id.btnChangeMode -> {
                    if (modeLight) {
                        AtlwStatusBarUtil.getInstance().setStatusBarLightMode(this, full)
                    } else {
                        AtlwStatusBarUtil.getInstance().setStatusBarDarkMode(this, full)
                    }
                    modeLight = !modeLight
                }
                else -> {

                }
            }
        }
    }
}
