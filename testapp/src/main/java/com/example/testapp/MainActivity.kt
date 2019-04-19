package com.example.testapp

import android.app.Activity
import android.content.Intent
import android.lorenwang.customview.tablayout.HorizontalSlipTabLayout
import android.os.Bundle
import android.view.View

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showHorizontalSlipTabLayout()
    }

    fun mainClick(view: View?) {
        if (view != null) {
            when (view.id) {
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
                else -> {

                }
            }
        }
    }

    fun showKerleyView() {
//        setContentView(R.layout.activity_main_kerley)
    }

    fun showCustomDrawableButton() {
//        setContentView(R.layout.activity_main_custom_drawable_button)
//        var test = findViewById<CustomDrawableButton>(R.id.test)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            test?.setBackgroundTintList(ColorStateList.valueOf(Color.RED))
//        }
    }


    fun showHorizontalSlipTabLayout() {
        setContentView(R.layout.activity_main_horizontal_slip_tab_layout)
        var test = findViewById<HorizontalSlipTabLayout>(R.id.test)
        test.setTabList(arrayListOf("热门", "最新"), 0)
    }

    fun showHorizontalSlipTabLayout3() {
//        setContentView(R.layout.activity_main_horizontal_slip_tab_layout_3)
//        var test = findViewById<HorizontalSlipTabLayout3>(R.id.test)
//        test.setTabList(arrayListOf("热门","最新"),0)
    }

}
