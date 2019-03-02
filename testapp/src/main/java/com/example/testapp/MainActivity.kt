package com.example.testapp

import android.app.Activity
import android.lorenwang.customview.dialog.ConfirmCancelDialog1
import android.os.Bundle
import android.view.View

class MainActivity : Activity() {
    var confirmCancelDialog1:ConfirmCancelDialog1? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        confirmCancelDialog1 = ConfirmCancelDialog1(this)
        confirmCancelDialog1!!.setContent("地方哈好舒服空间啊双卡双待发就发稍等会房间卡接收到后方可闪电发货事发后撒大口径",20,null)
        confirmCancelDialog1!!.setBtnLeft("取消",20,null,object : View.OnClickListener{
            override fun onClick(v: View?) {

                confirmCancelDialog1!!.dismiss()
            }
        })
        confirmCancelDialog1!!.setBtnRight("确定",null,null,object : View.OnClickListener{
            override fun onClick(v: View?) {
            }
        })
        confirmCancelDialog1!!.setOptionsState(true,false,30)
    }

    override fun onBackPressed() {
        confirmCancelDialog1!!.show()
    }
}
