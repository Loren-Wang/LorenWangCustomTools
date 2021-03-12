package com.example.testapp.activity

import android.content.Intent
import android.lorenwang.tools.app.AtlwActivityJumpUtil
import android.os.Bundle
import android.view.View
import com.example.testapp.R
import com.example.testapp.activity.androidTools.MobileContactsActivity
import com.example.testapp.activity.androidTools.MobileSmsActivity
import com.example.testapp.activity.anim.AnimActivity
import com.example.testapp.activity.bluetooth.BluetoothActivity
import com.example.testapp.activity.calendar.CalendarActivity
import com.example.testapp.activity.dialog.DialogsActivity
import com.example.testapp.activity.graphicCodeScan.CodeGenerateActivity
import com.example.testapp.activity.graphicCodeScan.ScanCodeActivity
import com.example.testapp.activity.image.ImageListBitmapActivity
import com.example.testapp.activity.image.ImageViewActivity
import com.example.testapp.activity.image.ZoomableImageViewActivity
import com.example.testapp.activity.location.LocationActivity
import com.example.testapp.activity.recycleView.WheelRecycleViewActivity
import com.example.testapp.activity.textview.CustomDrawableButtonActivity
import com.example.testapp.activity.textview.ShowPriceTextViewActivity
import com.example.testapp.activity.textview.ShowQuantityOfCommodityActivity
import com.example.testapp.activity.titlebar.TitleBarHeadViewActivity
import com.example.testapp.activity.touch.TouchOptionsActivity
import com.example.testapp.activity.validation.ValidationActivity
import com.example.testapp.activity.video.VideoPlayActivity
import com.example.testapp.activity.video.VideoPlayListActivity
import com.example.testapp.activity.viewpager.BannerActivity
import com.example.testapp.activity.viewpager.FragmentAndBannerActivity
import com.example.testapp.activity.viewpager.ViewPager2Activity
import com.example.testapp.base.BaseActivity
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.empty_data_default.*

class MainActivity : BaseActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_main)
        Fresco.initialize(applicationContext)
    }

    override fun onRefreshData() {
        super.onRefreshData()
        swipeRefresh?.isRefreshing = false
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        btnTouchOptions?.performClick()
    }

    fun mainClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btnTouchOptions -> {
                    startActivity(Intent(this, TouchOptionsActivity::class.java))
                }
                R.id.btnValidation -> {
                    startActivity(Intent(this, ValidationActivity::class.java))
                }
                R.id.btnCalendar -> {
                    startActivity(Intent(this, CalendarActivity::class.java))
                }
                R.id.btnWheelRecycleView -> {
                    startActivity(Intent(this, WheelRecycleViewActivity::class.java))
                }
                R.id.btnVideoPlay -> {
                    startActivity(Intent(this, VideoPlayActivity::class.java))
                }
                R.id.btnVideoPlayList -> {
                    startActivity(Intent(this, VideoPlayListActivity::class.java))
                }
                R.id.btnImageListBitmap -> {
                    startActivity(Intent(this, ImageListBitmapActivity::class.java))
                }
                R.id.btnLocation -> {
                    startActivity(Intent(this, LocationActivity::class.java))
                }
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
                    AtlwActivityJumpUtil.getInstance().jump(this, AnimActivity::class.java)
                }
                R.id.btnBlue -> {
                    //蓝牙
                    AtlwActivityJumpUtil.getInstance().jump(this, BluetoothActivity::class.java)
                }
                R.id.btnProgress -> {
                    //进度条
                    AtlwActivityJumpUtil.getInstance().jump(this, ProgressActivity::class.java)
                }
                else -> {

                }
            }
        }
    }

}
