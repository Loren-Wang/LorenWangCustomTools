package com.lorenwang.test.android.activity.customToolsAndroid

import android.lorenwang.tools.app.AtlwBrightnessChangeContentObserver
import android.lorenwang.tools.app.AtlwBrightnessChangeUtil
import android.lorenwang.tools.app.AtlwToastHintUtil
import android.os.Bundle
import android.view.View
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity

/**
 * 功能作用：亮度改变的页面
 * 初始注释时间： 2021/9/17 17:59
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
class BrightnessChangeActivity : BaseActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_custom_tools_android_brightness_change)
    }

    override fun initListener(savedInstanceState: Bundle?) {
        super.initListener(savedInstanceState)
        AtlwBrightnessChangeUtil.getInstance().registerBrightObserver(this, object : AtlwBrightnessChangeContentObserver(this) {
            override fun onBrightnessChange(brightness: Float?) {
                AtlwToastHintUtil.getInstance().toastMsg("当前屏幕亮度：${brightness}")
            }
        })
    }

    override fun onDestroy() {
        AtlwBrightnessChangeUtil.getInstance().unregisterBrightObserver(this)
        super.onDestroy()
    }

    fun mainClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btnCurrent -> {
                    AtlwToastHintUtil.getInstance().toastMsg(AtlwBrightnessChangeUtil.getInstance().getScreenBrightness(this).toString())
                }
                R.id.btnChange -> {
                    AtlwBrightnessChangeUtil.getInstance().updateMobileSystemBrightnessMode(this, true)
                }
                R.id.btnSet -> {
                    AtlwBrightnessChangeUtil.getInstance().setBrightness(this, Math.random().toFloat(), true)
                }
                R.id.btnIsAuto -> {
                    if (AtlwBrightnessChangeUtil.getInstance().isActivityAutoBrightness(this)) {
                        AtlwToastHintUtil.getInstance().toastMsg("当前亮度模式是自动")
                    } else {
                        AtlwToastHintUtil.getInstance().toastMsg("当前亮度模式是非自动")
                    }
                }
                R.id.btnSysIsAuto -> {
                    if (AtlwBrightnessChangeUtil.getInstance().isMobileSystemAutoBrightness(this)) {
                        AtlwToastHintUtil.getInstance().toastMsg("当前系统亮度模式是自动")
                    } else {
                        AtlwToastHintUtil.getInstance().toastMsg("当前系统亮度模式是非自动")
                    }
                }
                R.id.btnSave -> {
                    AtlwBrightnessChangeUtil.getInstance()
                        .saveBrightnessToMobileSystem(this, (AtlwBrightnessChangeUtil.getInstance().getScreenBrightness(this) * 255).toInt())
                }
                R.id.btnSystem -> {
                    AtlwBrightnessChangeUtil.getInstance().setBrightnessFollowMobileSystem()
                }
                else -> {

                }
            }
        }
    }
}
