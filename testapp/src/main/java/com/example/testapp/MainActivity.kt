package com.example.testapp

import android.app.Activity
import android.lorenwang.customview.CustomProgressTypeView
import android.lorenwang.customview.dialog.BottomListOptionsDialogType1
import android.lorenwang.customview.dialog.BottomListOptionsDialogType2
import android.lorenwang.customview.dialog.ConfirmCancelDialog1
import android.lorenwang.customview.dialog.LoadingDialogType1
import android.os.Bundle
import java.util.*

class MainActivity : Activity() {
    var confirmCancelDialog1:ConfirmCancelDialog1? = null
    var loadingDialogType1:LoadingDialogType1? = null
    var bottomListOptionsDialogType1:BottomListOptionsDialogType1? = null
    var bottomListOptionsDialogType2: BottomListOptionsDialogType2? = null
    private var test: CustomProgressTypeView? = null;
    private var precent = 0f
    private lateinit var tabList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        test = findViewById(R.id.test)
        test?.setProgress(0.1f)
    }

    override fun onBackPressed() {
        test?.setAutoProgress(2000)
//        precent += 0.1f
//        if(precent.toInt() <= 1){
//            test?.slipToPosi(0,precent)
//        }
//        bottomListOptionsDialogType2!!.show()
    }
}
