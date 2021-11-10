package com.lorenwang.test.android.activity.customView.textview

import android.lorenwang.customview.texiview.AvlwFixedWidthHeightDrawableButton
import android.os.Bundle
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import kotlinbase.lorenwang.tools.extend.kttlwThrottleClick

class CustomDrawableButtonActivity : BaseActivity() {
    var change = false

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_custom_view_custom_drawable_button)
    }

    override fun initListener(savedInstanceState: Bundle?) {
        super.initListener(savedInstanceState)
        findViewById<AvlwFixedWidthHeightDrawableButton>(R.id.btnChange)?.let {
            it.kttlwThrottleClick { _ ->
                if (change) {
                    it.setDrawable(1, null, null, null, R.drawable.image_default)
                } else {
                    it.setDrawable(0, null, null, null, null)
                }
                change = !change
            }
        }
    }
}
