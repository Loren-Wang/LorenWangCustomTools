package com.example.testapp.activity

import android.os.Bundle
import android.widget.EditText
import com.example.testapp.R
import com.example.testapp.base.BaseActivity
import com.example.testapp.bean.ExpressLogBean
import javabase.lorenwang.dataparse.JdplwJsonUtils

class DataParseActivity : BaseActivity() {

    private var edtData: EditText? = null

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_data_parse)
        edtData = findViewById(R.id.edtData)
    }

    fun onClickBtnJsonParseStr() {
        edtData?.let {
            JdplwJsonUtils.fromJson(edtData!!.text.toString(), String::class.java)
        }
    }

    fun onClickBtnJsonParseArray() {
        JdplwJsonUtils.fromJsonArray("[{\"context\":\"订单已提交，开始处理你的订单\",\"createTime\":1576569348000,\"state\":\"订单提交成功\"}]", ExpressLogBean::class.java)
    }
}
