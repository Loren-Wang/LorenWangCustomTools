package com.lorenwang.test.android.activity.customView

import android.os.Bundle
import com.lorenwang.test.android.base.BaseActivity
import com.lorenwang.test.android.R

class ShowQuantityOfCommodityActivity : BaseActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_custom_view_show_quantity_of_commodity)
    }
}
