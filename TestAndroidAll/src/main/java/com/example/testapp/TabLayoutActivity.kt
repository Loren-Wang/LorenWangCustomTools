package com.example.testapp

import android.os.Bundle

class TabLayoutActivity : BaseActivity() {


    override fun onChildCreate(savedInstanceState: Bundle?) {
        addChildView(R.layout.activity_tab_layout)
    }
}
