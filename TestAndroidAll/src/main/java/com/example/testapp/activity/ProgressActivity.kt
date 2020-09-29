package com.example.testapp.activity

import android.os.Bundle
import com.example.testapp.R
import com.example.testapp.base.BaseActivity

class ProgressActivity : BaseActivity() {
    /**
     * 初始化view
     */
    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_progress)

    }
}
