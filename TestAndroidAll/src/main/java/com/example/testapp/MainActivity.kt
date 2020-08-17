package com.example.testapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.testapp.androidTools.MobileContactsActivity
import com.example.testapp.androidTools.MobileSmsActivity
import com.example.testapp.dialog.DialogsActivity
import com.example.testapp.graphicCodeScan.CodeGenerateActivity
import com.example.testapp.graphicCodeScan.ScanCodeActivity
import com.example.testapp.image.ImageViewActivity
import com.example.testapp.image.ZoomableImageViewActivity
import com.example.testapp.textview.CustomDrawableButtonActivity
import com.example.testapp.textview.ShowPriceTextViewActivity
import com.example.testapp.textview.ShowQuantityOfCommodityActivity
import com.example.testapp.titlebar.TitleBarHeadViewActivity
import com.example.testapp.viewpager.BannerActivity
import com.example.testapp.viewpager.FragmentAndBannerActivity
import com.example.testapp.viewpager.ViewPager2Activity
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinbase.lorenwang.tools.extend.formatConversion

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Fresco.initialize(applicationContext)

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
                R.id.btnCodeGenerate -> {
                    startActivity(Intent(this, CodeGenerateActivity::class.java))
                }
                R.id.btnRecycleViewPager -> {
                    startActivity(Intent(this, RecycleViewPageActivity::class.java))
                }
                R.id.btnShowPriceText -> {
                    startActivity(Intent(this, ShowPriceTextViewActivity::class.java))
                }
                R.id.btnShowQuantityOfCommodity -> {
                    startActivity(Intent(this, ShowQuantityOfCommodityActivity::class.java))
                }
                R.id.btnBanner -> {
                    startActivity(Intent(this, BannerActivity::class.java))
                }
                R.id.btnBannerAndViewPager -> {
                    startActivity(Intent(this, FragmentAndBannerActivity::class.java))
                }
                R.id.btnViewPager2 -> {
                    startActivity(Intent(this, ViewPager2Activity::class.java))
                }
                R.id.btnDialogs -> {
                    startActivity(Intent(this, DialogsActivity::class.java))
                }
                R.id.btnZoomableImageView -> {
                    startActivity(Intent(this, ZoomableImageViewActivity::class.java))
                }
                R.id.btnTitleBarHeadView -> {
                    startActivity(Intent(this, TitleBarHeadViewActivity::class.java))
                }
                R.id.btnCustomDrawableButton -> {
                    startActivity(Intent(this, CustomDrawableButtonActivity::class.java))
                }
                else -> {

                }
            }
        }
    }

}
