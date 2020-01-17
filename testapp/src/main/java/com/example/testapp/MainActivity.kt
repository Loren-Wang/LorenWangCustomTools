package com.example.testapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.testapp.androidTools.MobileContactsActivity
import com.example.testapp.androidTools.MobileSmsActivity

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btnRecycleViewPager).performClick()
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
                R.id.btnTabLayout -> {
                    startActivity(Intent(this, TabLayoutActivity::class.java))
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
                R.id.btnToast -> {
                    startActivity(Intent(this, ToastActivity::class.java))
                }
                R.id.btnDataParse -> {
                    startActivity(Intent(this, DataParseActivity::class.java))
                }
                R.id.btnMobileContacts -> {
                    startActivity(Intent(this, MobileContactsActivity::class.java))
                }
                R.id.btnMobileSms -> {
                    startActivity(Intent(this, MobileSmsActivity::class.java))
                }
                R.id.btnScanCode -> {
                    startActivity(Intent(this, ScanCodeActivity::class.java))
                }
                R.id.btnRecycleViewPager -> {
                    startActivity(Intent(this, RecycleViewPageActivity::class.java))
                }
                else -> {

                }
            }
        }
    }

}
