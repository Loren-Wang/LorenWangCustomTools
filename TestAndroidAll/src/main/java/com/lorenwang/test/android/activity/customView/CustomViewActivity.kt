package com.lorenwang.test.android.activity.customView

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.lorenwang.test.android.R
import com.lorenwang.test.android.activity.customView.carousel.CarouselActivity
import com.lorenwang.test.android.activity.customView.textview.CustomDrawableButtonActivity
import com.lorenwang.test.android.activity.customView.textview.MarqueeActivity
import com.lorenwang.test.android.activity.customView.textview.ShowPriceTextViewActivity
import com.lorenwang.test.android.activity.customView.textview.TimeShowActivity
import com.lorenwang.test.android.activity.customView.titlebar.TitleBarHeadViewActivity
import com.lorenwang.test.android.activity.customView.validation.ValidationActivity
import com.lorenwang.test.android.activity.customView.video.VideoPlayActivity
import com.lorenwang.test.android.activity.customView.video.VideoPlayListActivity
import com.lorenwang.test.android.activity.customView.viewpager.BannerActivity
import com.lorenwang.test.android.activity.customView.viewpager.FragmentAndBannerActivity
import com.lorenwang.test.android.activity.customView.viewpager.ViewPager2Activity
import com.lorenwang.test.android.activity.customView.touch.TouchOptionsActivity
import com.lorenwang.test.android.base.BaseActivity

/**
 * 功能作用：自定义控件入口页面
 * 初始注释时间： 2021/9/1 19:15
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
class CustomViewActivity : BaseActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_custom_view)
    }

    fun mainClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btnWheelView -> {
                    startActivity(Intent(this, WheelViewActivity::class.java))
                }
                R.id.btnShadow -> {
                    startActivity(Intent(this, ShadowActivity::class.java))
                }
                R.id.btnCalendar -> {
                    startActivity(Intent(this, CalendarActivity::class.java))
                }
                R.id.btnCropView -> {
                    startActivity(Intent(this, ZoomableImageViewActivity::class.java))
                }
                R.id.btnDialog -> {
                    startActivity(Intent(this, DialogsActivity::class.java))
                }
                R.id.btnAllEdittext -> {
                    startActivity(Intent(this, EdittextActivity::class.java))
                }
                R.id.btnShowQuantityOfCommodity -> {
                    startActivity(Intent(this, ShowQuantityOfCommodityActivity::class.java))
                }
                R.id.btnImageView -> {
                    startActivity(Intent(this, ImageViewActivity::class.java))
                }
                R.id.btn_hot_view -> {
                    startActivity(Intent(this, HotViewActivity::class.java))
                }
                R.id.btn_progress -> {
                    startActivity(Intent(this, ProgressActivity::class.java))
                }
                R.id.btn_radio_group -> {
                    startActivity(Intent(this, RadioGroupActivity::class.java))
                }
                R.id.btnCarousel -> {
                    startActivity(Intent(this, CarouselActivity::class.java))
                }
                R.id.btnRecycleViewPager -> {
                    startActivity(Intent(this, RecycleViewPageActivity::class.java))
                }
                R.id.btnRecycleViewItemDecoration -> {
                    startActivity(Intent(this, RecycleItemDecorationActivity::class.java))
                }
                R.id.btnSideBar -> {
                    startActivity(Intent(this, SideBarActivity::class.java))
                }
                R.id.btn_sudoku_swipe_gestures -> {
                    startActivity(Intent(this, SudokuSwipeGesturesActivity::class.java))
                }
                R.id.btnSwitchButton -> {
                    startActivity(Intent(this, SwitchButtonActivity::class.java))
                }
                R.id.btnTabLayout -> {
                    startActivity(Intent(this, TabLayoutActivity::class.java))
                }
                R.id.btnShowPriceText -> {
                    startActivity(Intent(this, ShowPriceTextViewActivity::class.java))
                }
                R.id.btnCustomDrawableButton -> {
                    startActivity(Intent(this, CustomDrawableButtonActivity::class.java))
                }
                R.id.btn_marquee -> {
                    startActivity(Intent(this, MarqueeActivity::class.java))
                }
                R.id.btn_time_show -> {
                    startActivity(Intent(this, TimeShowActivity::class.java))
                }
                R.id.btnTitleBarHeadView -> {
                    startActivity(Intent(this, TitleBarHeadViewActivity::class.java))
                }
                R.id.btnTouchOptions -> {
                    startActivity(Intent(this, TouchOptionsActivity::class.java))
                }
                R.id.btnValidation -> {
                    startActivity(Intent(this, ValidationActivity::class.java))
                }
                R.id.btnVideoPlay -> {
                    startActivity(Intent(this, VideoPlayActivity::class.java))
                }
                R.id.btnVideoPlayList -> {
                    startActivity(Intent(this, VideoPlayListActivity::class.java))
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
                R.id.btn_kerley_or_dotted -> {
                    startActivity(Intent(this, KerleyOrDottedActivity::class.java))
                }
                else -> {

                }
            }
        }
    }
}
