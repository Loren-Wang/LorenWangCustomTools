package com.example.testapp.activity

import android.lorenwang.tools.app.AtlwBrightnessChangeContentObserver
import android.lorenwang.tools.app.AtlwBrightnessChangeUtils
import android.os.Bundle
import android.provider.Settings
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.SeekBar
import com.example.testapp.R
import com.example.testapp.base.BaseActivity


/**
 * 创建时间：2019-04-13 上午 11:37:59
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class BrightnessActivity : BaseActivity(), CompoundButton.OnCheckedChangeListener, SeekBar
.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser) {
            //随机设置个初始值
            AtlwBrightnessChangeUtils.getInstance().setBrightness(this, progress.toFloat(), false)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    //注册 Brightness 的 uri
    private val BRIGHTNESS_MODE_URI = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS_MODE)
    private val BRIGHTNESS_URI = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS)
    private val BRIGHTNESS_ADJ_URI = Settings.System.getUriFor("screen_auto_brightness_adj")
    private lateinit var seekBar: SeekBar
    private lateinit var checkBox: CheckBox

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_brightness)

        seekBar = findViewById(R.id.seekBar)
        checkBox = findViewById(R.id.checkbox)

        checkBox.setOnCheckedChangeListener(this)
        seekBar.setOnSeekBarChangeListener(this)
        //设置最大刻度
        seekBar.max = 255
        //设置初始的Progress
        seekBar.progress = 22

        //注册亮度观察者
        AtlwBrightnessChangeUtils.getInstance().registerBrightObserver(this, object : AtlwBrightnessChangeContentObserver(this) {
            override fun onBrightnessChange(brightness: Float?) {
                if (brightness != null) {
                    seekBar.progress = brightness.toInt()
                } else {
                    seekBar.progress = AtlwBrightnessChangeUtils.getInstance().getScreenBrightness(this@BrightnessActivity).toInt()
                }
            }
        })

        //随机设置个初始值
        AtlwBrightnessChangeUtils.getInstance().setBrightness(this, 22f, false)
    }


    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            seekBar.isEnabled = false
            AtlwBrightnessChangeUtils.getInstance().setBrightnessFollowMobileSystem()
        } else {
            seekBar.isEnabled = true
            AtlwBrightnessChangeUtils.getInstance().setBrightness(this, 22f, true)
        }
    }
}
