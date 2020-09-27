package com.example.testapp.activity

import android.lorenwang.customview.switchButton.AvlwSwitchButton1
import android.lorenwang.customview.switchButton.AvlwSwitchButtonChangeListener
import android.lorenwang.tools.app.AtlwToastHintUtils
import android.os.Bundle
import com.example.testapp.R
import com.example.testapp.base.BaseActivity

/**
 * 创建时间：2019-05-09 上午 10:24:43
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

class SwitchButtonActivity : BaseActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_switch_button)
        findViewById<AvlwSwitchButton1>(R.id.test1).setStateChangeListener(object : AvlwSwitchButtonChangeListener {
            override fun onChangeToOpen() {
                AtlwToastHintUtils.getInstance().toastMsg("open", 1)
            }

            override fun onChangeToClose() {
                AtlwToastHintUtils.getInstance().toastMsg("close", 1)
            }

        })
    }
}
