package com.lorenwang.test.android.activity.customView

import android.os.Bundle
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity

class TabLayoutActivity : BaseActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_custom_view_tab_layout)
    }
}
