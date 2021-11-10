package com.lorenwang.test.android.activity.customView

import android.lorenwang.customview.radiogroup.AvlwTextListSelectRadioGroup
import android.os.Bundle
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import com.lorenwang.test.android.databinding.ActivityCustomViewRadioGroupBinding

class RadioGroupActivity : BaseActivity() {

    private lateinit var mBinding: ActivityCustomViewRadioGroupBinding

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_custom_view_radio_group)
        mBinding = ActivityCustomViewRadioGroupBinding.bind(findViewById(R.id.root))
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        mBinding.rgSelect.setList(1, arrayListOf(AvlwTextListSelectRadioGroup.SelectItemBean().also { it.text = Math.random().toString() },
            AvlwTextListSelectRadioGroup.SelectItemBean().also { it.text = Math.random().toString() },
            AvlwTextListSelectRadioGroup.SelectItemBean().also { it.text = Math.random().toString() },
            AvlwTextListSelectRadioGroup.SelectItemBean().also { it.text = Math.random().toString() }), null, R.style.RadioButtonStyle)
    }
}
