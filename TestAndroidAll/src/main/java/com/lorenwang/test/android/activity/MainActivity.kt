package com.lorenwang.test.android.activity

import android.content.Intent
import android.lorenwang.tools.app.AtlwActivityJumpUtil
import android.os.Bundle
import android.view.View
import com.facebook.drawee.backends.pipeline.Fresco
import com.lorenwang.test.android.R
import com.lorenwang.test.android.activity.anim.AnimActivity
import com.lorenwang.test.android.activity.bluetooth.BluetoothActivity
import com.lorenwang.test.android.activity.customToolsAndroid.CustomToolsActivity
import com.lorenwang.test.android.activity.customView.CustomViewActivity
import com.lorenwang.test.android.activity.graphicCodeScan.GraphicCodeActivity
import com.lorenwang.test.android.base.BaseActivity
import kotlinx.android.synthetic.main.empty_data_default.*
import org.json.JSONException
import org.json.JSONObject

class MainActivity : BaseActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_main)
        Fresco.initialize(applicationContext)
        testData()
    }

    override fun onRefreshData() {
        super.onRefreshData()
        swipeRefresh?.isRefreshing = false
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
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
                else -> {

                }
            }
        }
    }

    fun testData() {
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject("{\"id\":100,  \"name\":\"yndfcd\",  \"phone\":null}")
            jsonObject["id"].javaClass
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}
