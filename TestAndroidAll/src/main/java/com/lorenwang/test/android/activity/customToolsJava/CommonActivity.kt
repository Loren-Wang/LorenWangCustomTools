package com.lorenwang.test.android.activity.customToolsJava

import android.view.View
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import com.lorenwang.test.android.databinding.ActivityCustomToolsJavaCommonBinding

class CommonActivity : BaseActivity() {

    private lateinit var mBinding: ActivityCustomToolsJavaCommonBinding

    override fun setContentViewConfig(resId: Int?)  {
        super.setContentViewConfig(R.layout.activity_custom_tools_java_common)
        mBinding = ActivityCustomToolsJavaCommonBinding.bind(findViewById(R.id.root))
    }

    fun mainClick(view: View?) {
        if (view != null) {
            when (view.id) {
            }
        }
    }
}
