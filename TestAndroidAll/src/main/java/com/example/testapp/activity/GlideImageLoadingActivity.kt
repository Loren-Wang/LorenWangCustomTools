package com.example.testapp.activity

import android.lorenwang.tools.image.AtlwGlideImageLoadingUtils
import android.os.Bundle
import android.widget.ImageView
import com.example.testapp.R
import com.example.testapp.base.BaseActivity

/**
 * 创建时间：2019-04-11 下午 15:07:23
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class GlideImageLoadingActivity : BaseActivity() {

    private lateinit var imgLoadOrdinary: ImageView
    private lateinit var imgLoadBitmap: ImageView
    private lateinit var imgLoadblue: ImageView
    private val IMAGE_PATH = "http://pic75.nipic.com/file/20150821/9448607_145742365000_2.jpg"

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_image_loading_glide)
        imgLoadOrdinary = findViewById(R.id.imgLoadOrdinary)
        imgLoadBitmap = findViewById(R.id.imgLoadBitmap)
        imgLoadblue = findViewById(R.id.imgLoadblue)

        //加载普通图片
        AtlwGlideImageLoadingUtils.getInstance().loadNetImage(this, IMAGE_PATH, AtlwGlideImageLoadingUtils.getInstance().getRequestOptions(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background), imgLoadOrdinary)
        //加载位图图片
        AtlwGlideImageLoadingUtils.getInstance().loadNetImageGetBitmap(this, IMAGE_PATH, AtlwGlideImageLoadingUtils.getInstance().getRequestOptions(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background)) { bitmap ->
            runOnUiThread {
                imgLoadBitmap.setImageBitmap(bitmap)
            }
        }
        //高斯模糊图片
        AtlwGlideImageLoadingUtils.getInstance().loadNetImageBlur(this, IMAGE_PATH, AtlwGlideImageLoadingUtils.getInstance().getRequestOptions(R.drawable.ic_launcher_background, R.drawable.ic_launcher_background), imgLoadblue, 25, true)
    }
}
