package com.example.testapp.activity

import android.lorenwang.customview.sidebar.AvlwSideBar1
import android.os.Bundle
import com.example.testapp.R
import com.example.testapp.base.BaseActivity
import javabase.lorenwang.tools.common.JtlwVariateDataParamUtils

/**
 * 创建时间：2019-04-17 上午 11:13:32
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class SideBarActivity : BaseActivity() {

    private lateinit var sidebar1Avlw: AvlwSideBar1

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_sidebar)
        sidebar1Avlw = findViewById(R.id.sidebar1)

        initSideBar1()
    }

    private fun initSideBar1() {
        sidebar1Avlw.setTextList(JtlwVariateDataParamUtils.getInstance().paramesArrayToList(
                arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L")
        ))
    }
}
