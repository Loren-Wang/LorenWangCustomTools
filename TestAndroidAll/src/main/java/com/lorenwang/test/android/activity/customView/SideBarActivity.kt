package com.lorenwang.test.android.activity.customView

import android.lorenwang.customview.sidebar.AvlwSideBar1
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import javabase.lorenwang.tools.common.JtlwVariateDataParamUtil

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

    override fun setContentViewConfig(resId: Int?)  {
        super.setContentViewConfig(R.layout.activity_custom_view_sidebar)
        sidebar1Avlw = findViewById(R.id.sidebar1)

        initSideBar1()
    }

    private fun initSideBar1() {
        sidebar1Avlw.setTextList(JtlwVariateDataParamUtil.getInstance().paramesArrayToList(
                arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L")
        ))
    }
}
