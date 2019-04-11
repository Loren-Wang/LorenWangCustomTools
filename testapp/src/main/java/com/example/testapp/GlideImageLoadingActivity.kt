package com.example.testapp

import android.app.Activity
import android.graphics.Bitmap
import android.lorenwang.tools.image.AtlwGlideImageLoadingUtils
import android.lorenwang.tools.image.OnImageLoadCallback
import android.os.Bundle
import android.widget.ImageView

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
class GlideImageLoadingActivity : Activity() {

    private lateinit var imgLoadOrdinary: ImageView
    private lateinit var imgLoadBitmap: ImageView
    private val IMAGE_PATH = "http://pic75.nipic.com/file/20150821/9448607_145742365000_2.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_loading_glide)
        imgLoadOrdinary = findViewById(R.id.imgLoadOrdinary)
        imgLoadBitmap = findViewById(R.id.imgLoadBitmap)

        //加载普通图片
        AtlwGlideImageLoadingUtils.getInstance().loadNetImage(this,IMAGE_PATH
                ,AtlwGlideImageLoadingUtils.getInstance().getRequestOptions(R.drawable.ic_launcher_background,R.drawable.ic_launcher_background)
                ,imgLoadOrdinary)
        //加载位图图片
        AtlwGlideImageLoadingUtils.getInstance().loadNetImageGetBitmap(this,IMAGE_PATH
                ,AtlwGlideImageLoadingUtils.getInstance().getRequestOptions(R.drawable.ic_launcher_background,R.drawable.ic_launcher_background)
                ,object :OnImageLoadCallback{
            override fun onBitmap(bitmap: Bitmap?) {
                runOnUiThread{
                    imgLoadBitmap.setImageBitmap(bitmap)
                }
            }

        })
    }
}
