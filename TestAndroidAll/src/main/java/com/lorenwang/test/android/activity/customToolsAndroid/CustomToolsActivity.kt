package com.lorenwang.test.android.activity.customToolsAndroid

import android.content.Intent
import android.view.View
import com.lorenwang.test.android.R
import com.lorenwang.test.android.activity.customToolsAndroid.image.ImageCommonActivity
import com.lorenwang.test.android.activity.customToolsAndroid.image.ImageListBitmapActivity
import com.lorenwang.test.android.activity.customToolsAndroid.image.ImageLoadingActivity
import com.lorenwang.test.android.activity.customToolsAndroid.image.ImageSelectActivity
import com.lorenwang.test.android.base.BaseActivity

/**
 * 功能作用：自定义工具类样例入口
 * 初始注释时间： 2021/9/17 17:38
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
class CustomToolsActivity : BaseActivity() {
    override fun setContentViewConfig(resId: Int?)  {
        super.setContentViewConfig(R.layout.activity_custom_tools_android)
    }

    fun mainClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btnActivity -> {
                    startActivity(Intent(this, ActivityActivity::class.java))
                }
                R.id.btnBrightness -> {
                    startActivity(Intent(this, BrightnessChangeActivity::class.java))
                }
                R.id.btnNotify -> {
                    startActivity(Intent(this, MobileNotificationActivity::class.java))
                }
                R.id.btnScreen -> {
                    startActivity(Intent(this, ScreenActivity::class.java))
                }
                R.id.btnSpannable -> {
                    startActivity(Intent(this, SpannableActivity::class.java))
                }
                R.id.btnStatusBar -> {
                    startActivity(Intent(this, StatusBarActivity::class.java))
                }
                R.id.btnView -> {
                    startActivity(Intent(this, ViewActivity::class.java))
                }
                R.id.btnCheck -> {
                    startActivity(Intent(this, CheckActivity::class.java))
                }
                R.id.btnDesktopShortcut -> {
                    startActivity(Intent(this, DesktopShortcutActivity::class.java))
                }
                R.id.btnFile -> {
                    startActivity(Intent(this, FileActivity::class.java))
                }
                R.id.btn_image_common -> {
                    startActivity(Intent(this, ImageCommonActivity::class.java))
                }
                R.id.btn_image_loading -> {
                    startActivity(Intent(this, ImageLoadingActivity::class.java))
                }
                R.id.btn_location -> {
                    startActivity(Intent(this, LocationActivity::class.java))
                }
                R.id.btn_fly_msg -> {
                    startActivity(Intent(this, FlyMessageActivity::class.java))
                }
                R.id.btn_mobile_info -> {
                    startActivity(Intent(this, MobileInfoActivity::class.java))
                }
                R.id.btn_mobile_system_info -> {
                    startActivity(Intent(this, MobileSystemInfoActivity::class.java))
                }
                R.id.btnToast -> {
                    startActivity(Intent(this, ToastActivity::class.java))
                }
                R.id.btnImageListBitmap -> {
                    startActivity(Intent(this, ImageListBitmapActivity::class.java))
                }
                R.id.btnImageSelect -> {
                    startActivity(Intent(this, ImageSelectActivity::class.java))
                }
                else -> {

                }
            }
        }
    }
}
