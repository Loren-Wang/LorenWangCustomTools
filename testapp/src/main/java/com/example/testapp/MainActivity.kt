package com.example.testapp

import android.app.Activity
import android.lorenwang.customview.dialog.BottomListOptionsDialogType1
import android.lorenwang.customview.dialog.BottomListOptionsDialogType2
import android.lorenwang.customview.dialog.ConfirmCancelDialog1
import android.lorenwang.customview.dialog.LoadingDialogType1
import android.lorenwang.customview.tablayout.HorizontalSlipTabLayout
import android.os.Bundle
import java.util.*

class MainActivity : Activity() {
    var confirmCancelDialog1:ConfirmCancelDialog1? = null
    var loadingDialogType1:LoadingDialogType1? = null
    var bottomListOptionsDialogType1:BottomListOptionsDialogType1? = null
    var bottomListOptionsDialogType2: BottomListOptionsDialogType2? = null
    private var test: HorizontalSlipTabLayout? = null;
    private var posi = 0
    private lateinit var tabList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        test = findViewById(R.id.test)
        tabList = ArrayList<String>()
        tabList.add("好借")
        tabList.add("好借好借")
        tabList.add("好借")
        tabList.add("好借好")
        tabList.add("好借")
        test?.setTabList(tabList,0)

//        confirmCancelDialog1 = ConfirmCancelDialog1(this)
//        confirmCancelDialog1!!.setContent("地方哈好舒服空间啊双卡双待发就发稍等会房间卡接收到后方可闪电发货事发后撒大口径",20,null)
//        confirmCancelDialog1!!.setBtnLeft("取消",20,null,object : View.OnClickListener{
//            override fun onClick(v: View?) {
//
//                confirmCancelDialog1!!.dismiss()
//            }
//        })
//        confirmCancelDialog1!!.setBtnRight("确定",null,null,object : View.OnClickListener{
//            override fun onClick(v: View?) {
//            }
//        })
//        confirmCancelDialog1!!.setOptionsState(true,false,30)
//
//        loadingDialogType1 = LoadingDialogType1(this)
//        loadingDialogType1!!.setWindowBackground(Color.parseColor("#88000000"))

//        bottomListOptionsDialogType1 = object : BottomListOptionsDialogType1(this){
//            override fun onOptionsItemClick(posi: Int, text: String?) {
//            }
//        }
//        bottomListOptionsDialogType1!!.setOptionsList(Array(1){"1"},null,null,null,null,null,null)

//        bottomListOptionsDialogType2 = BottomListOptionsDialogType2(this)
//        var list = ArrayList<OptionsItemAttribute>()
//        list.add(OptionsItemAttribute("1",10,Color.BLACK,100,null,null,null,null,Gravity.LEFT))
//        list.add(OptionsItemAttribute("2",10,Color.BLACK,100,null,null,null,null,Gravity.CENTER))
//        bottomListOptionsDialogType2!!.setOptionsList(list)
    }

    override fun onBackPressed() {

        test?.skipToPosi(++posi % tabList.size)
//        bottomListOptionsDialogType2!!.show()
    }
}
