package com.lorenwang.test.android.activity.customToolsJava

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity

class CustomToolsActivity : BaseActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_custom_tools_java)
    }

    fun mainClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btn_common -> {
                    startActivity(Intent(this, CommonActivity::class.java))
                }
            }
        }
    }
}
