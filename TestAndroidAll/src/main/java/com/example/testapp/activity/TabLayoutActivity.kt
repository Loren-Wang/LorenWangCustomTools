package com.example.testapp.activity

import android.os.Bundle
import com.example.testapp.R
import com.example.testapp.base.BaseActivity

class TabLayoutActivity : BaseActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_tab_layout)
    }
}
