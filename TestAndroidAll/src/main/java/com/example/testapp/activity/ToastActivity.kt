package com.example.testapp.activity

import android.lorenwang.tools.app.AtlwToastHintUtil
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.testapp.R
import com.example.testapp.base.BaseActivity

/**
 * 创建时间： 2019/8/14 0014 上午 11:59:24
 * 创建人：LorenWang(王亮)
 * 功能作用：吐司提示Activity
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class ToastActivity : BaseActivity() {

    private var edtTime: EditText? = null
    private var btnToastCenter: Button? = null
    private var btnToastBottom: Button? = null
    private var btnToastTop: Button? = null
    private var btnCustomToastCenter: Button? = null

    private lateinit var customToastView: View

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_toast)


        edtTime = findViewById(R.id.edtTime)
        btnToastCenter = findViewById(R.id.btnToastCenter)
        btnToastBottom = findViewById(R.id.btnToastBottom)
        btnToastTop = findViewById(R.id.btnToastTop)
        btnCustomToastCenter = findViewById(R.id.btnCustomToastCenter)

        customToastView = layoutInflater.inflate(R.layout.custom_toast, null)


        btnToastCenter?.setOnClickListener {
            edtTime?.text?.toString()?.let {
                if (it.isNotEmpty()) {
                    AtlwToastHintUtil.getInstance().setParams(it.toLong(), Gravity.CENTER, 0, 0, 0f, 0f)
                    AtlwToastHintUtil.getInstance().toastMsg("重按退出")
                }
            }
        }
        btnToastBottom?.setOnClickListener {
            edtTime?.text?.toString()?.let {
                if (it.isNotEmpty()) {
                    AtlwToastHintUtil.getInstance().setParams(it.toLong(), Gravity.BOTTOM, 0, 0, 0f, 0f)
                    AtlwToastHintUtil.getInstance().toastMsg("重按退出")
                }
            }
        }
        btnToastTop?.setOnClickListener {
            edtTime?.text?.toString()?.let {
                if (it.isNotEmpty()) {
                    AtlwToastHintUtil.getInstance().setParams(it.toLong(), Gravity.TOP, 0, 0, 0f, 0f)
                    AtlwToastHintUtil.getInstance().toastMsg("重按退出")
                }
            }
        }
        btnCustomToastCenter?.setOnClickListener {
            edtTime?.text?.toString()?.let {
                if (it.isNotEmpty()) {
                    AtlwToastHintUtil.getInstance().setParams(it.toLong(), Gravity.CENTER, 0, 0, 0f, 0f)
                    AtlwToastHintUtil.getInstance().toastView(customToastView)
                }
            }
        }
    }
}
