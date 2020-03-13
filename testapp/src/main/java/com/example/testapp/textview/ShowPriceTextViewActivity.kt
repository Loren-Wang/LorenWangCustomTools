package com.example.testapp.textview

import android.os.Bundle
import com.example.testapp.BaseActivity
import com.example.testapp.R

class ShowPriceTextViewActivity : BaseActivity() {

    override fun onChildCreate(savedInstanceState: Bundle?) {
        addChildView(R.layout.activity_show_price_text_view)
    }
}
