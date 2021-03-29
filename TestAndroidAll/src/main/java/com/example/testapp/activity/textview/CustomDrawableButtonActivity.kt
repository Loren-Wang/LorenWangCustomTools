package com.example.testapp.activity.textview

import android.os.Bundle
import com.example.testapp.base.BaseActivity
import com.example.testapp.R
import kotlinbase.lorenwang.tools.extend.kttlwThrottleClick
import kotlinx.android.synthetic.main.activity_custom_drawable_button.*

class CustomDrawableButtonActivity : BaseActivity() {
    var change = false

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_custom_drawable_button)
    }

    override fun initListener(savedInstanceState: Bundle?) {
        super.initListener(savedInstanceState)
        btnChange.kttlwThrottleClick {
            if(change){
                btnChange.setDrawable(1, null, null, null, R.drawable.image_default)
            }else{
                btnChange.setDrawable(0, null, null, null, null)
            }
            change = !change
        }
    }
}
