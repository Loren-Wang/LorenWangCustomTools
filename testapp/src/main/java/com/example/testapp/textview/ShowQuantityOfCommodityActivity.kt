package com.example.testapp.textview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testapp.BaseActivity
import com.example.testapp.R

class ShowQuantityOfCommodityActivity : BaseActivity() {

    override fun onChildCreate(savedInstanceState: Bundle?) {
        addChildView(R.layout.activity_show_quantity_of_commodity)
    }
}
