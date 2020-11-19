package com.example.testapp.activity

import android.content.Intent
import android.lorenwang.tools.app.AtlwActivityJumpUtils
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import com.example.testapp.R
import com.example.testapp.activity.androidTools.MobileContactsActivity
import com.example.testapp.activity.androidTools.MobileSmsActivity
import com.example.testapp.activity.anim.AnimActivity
import com.example.testapp.activity.bluetooth.BluetoothActivity
import com.example.testapp.activity.dialog.DialogsActivity
import com.example.testapp.activity.graphicCodeScan.CodeGenerateActivity
import com.example.testapp.activity.graphicCodeScan.ScanCodeActivity
import com.example.testapp.activity.image.ImageViewActivity
import com.example.testapp.activity.image.ZoomableImageViewActivity
import com.example.testapp.activity.textview.CustomDrawableButtonActivity
import com.example.testapp.activity.textview.ShowPriceTextViewActivity
import com.example.testapp.activity.textview.ShowQuantityOfCommodityActivity
import com.example.testapp.activity.titlebar.TitleBarHeadViewActivity
import com.example.testapp.activity.viewpager.BannerActivity
import com.example.testapp.activity.viewpager.FragmentAndBannerActivity
import com.example.testapp.activity.viewpager.ViewPager2Activity
import com.example.testapp.base.BaseActivity
import com.facebook.drawee.backends.pipeline.Fresco

class MainActivity : BaseActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_main)
        Fresco.initialize(applicationContext)
    }

    override fun initListener(savedInstanceState: Bundle?) {
        super.initListener(savedInstanceState)
        findViewById<ScrollView>(R.id.scrollView)?.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if(scrollY > 0){
                swipeRefresh?.isEnabled = false
            }else{
                swipeRefresh?.isEnabled = true
            }
        }
    }

    override fun onRefreshData() {
        super.onRefreshData()
        swipeRefresh?.isRefreshing = false
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
                R.id.btnAnim -> {
                    //动画界面
                    AtlwActivityJumpUtils.getInstance().jump(this, AnimActivity::class.java)
                }
                R.id.btnBlue -> {
                    //蓝牙
                    AtlwActivityJumpUtils.getInstance().jump(this, BluetoothActivity::class.java)
                }
                R.id.btnProgress -> {
                    //进度条
                    AtlwActivityJumpUtils.getInstance().jump(this, ProgressActivity::class.java)
                }
                else -> {

                }
            }
        }
    }

}