package com.example.testapp.textview

import android.os.Bundle
import com.example.testapp.BaseActivity
import com.example.testapp.R

class CustomDrawableButtonActivity :BaseActivity() {

    /**
     * 子类create
     */
    override fun onChildCreate(savedInstanceState: Bundle?) {
       addChildView(R.layout.activity_custom_drawable_button)
    }
}
