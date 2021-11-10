package com.lorenwang.test.android.activity.customView

import android.os.Bundle
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import kotlinbase.lorenwang.tools.extend.kttlwThrottleClick
import kotlinx.android.synthetic.main.activity_wheel_view.*

class WheelViewActivity : BaseActivity() {
    val list = arrayListOf<Any>("测试1", "测试2", "测试3", "测试4", "测试5", "测试6", "测试7", "测试8", "测试9")
    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_wheel_view)
    }

    override fun initListener(savedInstanceState: Bundle?) {
        super.initListener(savedInstanceState)
        btnChange?.kttlwThrottleClick {
            rvList?.setSelect(edtCount.text.toString().toInt())
        }
        btnRemove?.kttlwThrottleClick {
            list.removeAt(edtCount.text.toString().toInt())
            rvList?.setData(list)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        rvList?.setData(list)
        rvList?.setSelect(5)
    }
}
