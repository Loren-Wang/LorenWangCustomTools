package com.lorenwang.test.android.activity

import android.os.Bundle
import android.widget.EditText
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import javabase.lorenwang.dataparse.JdplwJsonUtil

class DataParseActivity : BaseActivity() {

    private var edtData: EditText? = null

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_data_parse)
        edtData = findViewById(R.id.edtData)
    }

    fun onClickBtnJsonParseStr() {
        edtData?.let {
            JdplwJsonUtil.fromJson(edtData!!.text.toString(), String::class.java)
        }
    }

    fun onClickBtnJsonParseArray() {
        JdplwJsonUtil.fromJsonArray("[{\"context\":\"订单已提交，开始处理你的订单\",\"createTime\":1576569348000,\"state\":\"订单提交成功\"}]", com.lorenwang.test.android.bean.ExpressLogBean::class.java)
    }
}
