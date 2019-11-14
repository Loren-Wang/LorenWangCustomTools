package com.example.testapp

import android.app.Activity
import android.lorenwang.customview.tablayout.AvlwHorizontalSlipTabLayout
import android.lorenwang.customview.tablayout.AvlwHorizontalSlipTabLayout3
import android.os.Bundle
import android.widget.Button

/**
 * 创建时间：2019-05-06 上午 11:14:21
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class HorizontalSlipTabLayoutActivity : Activity() {
    private lateinit var test1: AvlwHorizontalSlipTabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizontal_slip_tab_layout)

        test1 = findViewById<AvlwHorizontalSlipTabLayout>(R.id.test1)
        test1.setTabList(arrayListOf("热门", "最新", "热门", "最新", "热门", "最新", "热门", "最新", "热门", "最新", "热门", "最新"), 0)

        var test3 = findViewById<AvlwHorizontalSlipTabLayout3>(R.id.test3)
        test3.setTabList(arrayListOf("热门", "最新", "热门", "最新", "热门", "最新", "热门", "最新", "热门", "最新", "热门", "最新"), 0)

        findViewById<Button>(R.id.btnChangeList).setOnClickListener {
            test1.setTabList(arrayListOf("测试", "测试", "测试", "测试"), 0)
        }
    }
}
