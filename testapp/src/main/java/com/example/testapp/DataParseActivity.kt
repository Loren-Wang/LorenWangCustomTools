package com.example.testapp

import android.os.Bundle
import android.view.View
import android.widget.EditText
import javabase.lorenwang.dataparse.JdplwJsonUtils

class DataParseActivity : BaseActivity() {

    private var edtData: EditText? = null

    override fun onChildCreate(savedInstanceState: Bundle?) {
        addChildView(R.layout.activity_data_parse)
        edtData = findViewById(R.id.edtData)
    }

    fun onClickBtnJsonParseStr(view: View) {
        edtData?.let {
            JdplwJsonUtils.fromJson(edtData!!.text.toString(), String::class.java)
        }
    }

    fun onClickBtnJsonParseArray(view: View) {
//        JdplwJsonUtils.fromJsonArray<String>(edtData!!.text.toString())
    }
}
