package com.lorenwang.test.android.activity.customView

import android.lorenwang.customview.switchButton.AvlwSwitchButton1
import android.lorenwang.customview.switchButton.AvlwSwitchButtonChangeListener
import android.lorenwang.tools.app.AtlwToastHintUtil
import android.os.Bundle
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity

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
        addContentView(R.layout.activity_custom_view_switch_button)
        findViewById<AvlwSwitchButton1>(R.id.test1).setStateChangeListener(object : AvlwSwitchButtonChangeListener {
            override fun onChangeToOpen() {
                AtlwToastHintUtil.getInstance().toastMsg("open", 1)
            }

            override fun onChangeToClose() {
                AtlwToastHintUtil.getInstance().toastMsg("close", 1)
            }

        })
    }
}
