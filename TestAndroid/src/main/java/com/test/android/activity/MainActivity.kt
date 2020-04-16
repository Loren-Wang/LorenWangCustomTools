package com.test.android.activity

import android.lorenwang.tools.app.AtlwActivityJumpUtils
import android.os.Bundle
import android.view.View
import com.test.android.R
import com.test.android.activity.anim.AnimActivity
import com.test.android.activity.bluetooth.BluetoothActivity
import com.test.android.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_main)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    /**
     * 主页相关点击事件
     */
    fun mainClick(v: View) {
        when (v.id) {
            R.id.btnAnim -> {
                //动画界面
                AtlwActivityJumpUtils.getInstance().jump(this, AnimActivity::class.java)
            }
            R.id.btnBlue -> {
                //蓝牙
                AtlwActivityJumpUtils.getInstance().jump(this, BluetoothActivity::class.java)
            }
            else -> {

            }
        }
    }
}
