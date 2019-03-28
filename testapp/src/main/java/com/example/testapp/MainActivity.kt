package com.example.testapp

import android.app.Activity
import android.lorenwang.customview.tablayout.HorizontalSlipTabLayout
import android.lorenwang.customview.tablayout.HorizontalSlipTabLayout3
import android.os.Bundle

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showHorizontalSlipTabLayout3()
    }


    fun showHorizontalSlipTabLayout(){
        setContentView(R.layout.activity_main_horizontal_slip_tab_layout)
        var test = findViewById<HorizontalSlipTabLayout>(R.id.test)
        test.setTabList(arrayListOf("热门","最新"),0)
    }
    fun showHorizontalSlipTabLayout3(){
        setContentView(R.layout.activity_main_horizontal_slip_tab_layout_3)
        var test = findViewById<HorizontalSlipTabLayout3>(R.id.test)
        test.setTabList(arrayListOf("热门","最新"),0)
    }

}
