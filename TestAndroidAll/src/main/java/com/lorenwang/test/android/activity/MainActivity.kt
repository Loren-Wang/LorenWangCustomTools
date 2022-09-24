package com.lorenwang.test.android.activity

import android.content.Intent
import android.lorenwang.tools.app.AtlwActivityJumpUtil
import android.os.Bundle
import android.view.View
import com.lorenwang.test.android.R
import com.lorenwang.test.android.activity.anim.AnimActivity
import com.lorenwang.test.android.activity.bluetooth.BluetoothActivity
import com.lorenwang.test.android.activity.customToolsAndroid.CustomToolsActivity
import com.lorenwang.test.android.activity.customView.CustomViewActivity
import com.lorenwang.test.android.activity.graphicCodeScan.GraphicCodeActivity
import com.lorenwang.test.android.activity.net.NetActivity
import com.lorenwang.test.android.base.BaseActivity

class MainActivity : BaseActivity() {
    override fun initListener(savedInstanceState: Bundle?) {

    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun setContentViewConfig(resId: Int?) {
        super.setContentViewConfig(R.layout.activity_main)
    }

    override fun onRefreshData() {
        swipeRefresh?.isRefreshing = false
        android.R.drawable.arrow_down_float
    }

    fun mainClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btnGraphicCode -> {
                    startActivity(Intent(this, GraphicCodeActivity::class.java))
                }
                R.id.btnCustomView -> {
                    startActivity(Intent(this, CustomViewActivity::class.java))
                }
                R.id.btnCustomTools -> {
                    startActivity(Intent(this, CustomToolsActivity::class.java))
                }
                R.id.btnDataParse -> {
                    startActivity(Intent(this, DataParseActivity::class.java))
                }
                R.id.btnAnim -> {
                    //动画界面
                    AtlwActivityJumpUtil.getInstance().jump(this, AnimActivity::class.java)
                }
                R.id.btnBlue -> {
                    //蓝牙
                    AtlwActivityJumpUtil.getInstance().jump(this, BluetoothActivity::class.java)
                }
                R.id.btnNet -> {
                    //蓝牙
                    AtlwActivityJumpUtil.getInstance().jump(this, NetActivity::class.java)
                }
                else -> {

                }
            }
        }
    }
}
