package com.example.testapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun mainClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btnImageView -> {
                    startActivity(Intent(this, ImageViewActivity::class.java))
                }
                R.id.btnImageLoadForGlide -> {
                    startActivity(Intent(this, GlideImageLoadingActivity::class.java))
                }
                R.id.btnBrightness -> {
                    startActivity(Intent(this, BrightnessActivity::class.java))
                }
                R.id.btnFileOptionsForScan -> {
                    startActivity(Intent(this, FileOptionsForScanFileActivity::class.java))
                }
                R.id.btnSideBar -> {
                    startActivity(Intent(this, SideBarActivity::class.java))
                }
                R.id.btnHorizontalSlipTabLayout -> {
                    startActivity(Intent(this, HorizontalSlipTabLayoutActivity::class.java))
                }
                R.id.btnSwitchButton -> {
                    startActivity(Intent(this, SwitchButtonActivity::class.java))
                }
                R.id.btnSmartRefreshLayout -> {
                    startActivity(Intent(this, SmartRefreshLayoutActivity::class.java))
                }
                R.id.btnFileOptions -> {
                    startActivity(Intent(this, FileOptionsActivity::class.java))
                }
                else -> {

                }
            }
        }
    }

}
