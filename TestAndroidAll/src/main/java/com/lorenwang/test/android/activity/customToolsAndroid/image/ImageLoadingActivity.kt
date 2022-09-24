package com.lorenwang.test.android.activity.customToolsAndroid.image

import android.graphics.Bitmap
import android.lorenwang.tools.AtlwConfig
import android.lorenwang.tools.app.AtlwScreenUtil
import android.lorenwang.tools.app.AtlwToastHintUtil
import android.lorenwang.tools.file.AtlwFileOptionUtil
import android.lorenwang.tools.image.loading.AtlwImageLoadCallback
import android.lorenwang.tools.image.loading.AtlwImageLoadConfig
import android.lorenwang.tools.image.loading.AtlwImageLoadingFactory
import android.view.View
import android.view.ViewGroup
import com.lorenwang.test.android.BuildConfig
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import com.lorenwang.test.android.databinding.ActivityCustomToolsAndroidImageLoadingBinding
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData

/**
 * 功能作用：图片加载显示页面
 * 初始注释时间： 2021/10/8 13:43
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
class ImageLoadingActivity : BaseActivity() {

    private var binding: ActivityCustomToolsAndroidImageLoadingBinding? = null
        get() {
            field = field.kttlwGetNotEmptyData { ActivityCustomToolsAndroidImageLoadingBinding.inflate(layoutInflater) }
            return field
        }

    private var type: Int? = null

    override fun setContentViewConfig(resId: Int?)  {
        addShowContentView(true, binding)
    }

    fun mainClick(view: View?) {
        if (view != null) {
            binding?.ivSimple?.setImageBitmap(null)
            //app文件夹地址
            val appSystemStorageDirPath = AtlwFileOptionUtil.getInstance().getAppSystemStorageDirPath(BuildConfig.APPLICATION_ID)
            //地址
            val path: String? = binding?.etContent?.text.toString()
            when (view.id) {
                R.id.btn_fresco -> {
                    type = AtlwConfig.IMAGE_LOAD_LIBRARY_TYPE_FRESCO
                }
                R.id.btn_glide -> {
                    type = AtlwConfig.IMAGE_LOAD_LIBRARY_TYPE_GLIDE
                }
                R.id.btn_image_loading -> {
                    type = AtlwConfig.IMAGE_LOAD_LIBRARY_TYPE_IMAGE_LOAD
                }
                //加载图片
                R.id.btn_loading -> {
                    if (path.isNullOrEmpty()) {
                        AtlwToastHintUtil.getInstance().toastMsg("请输入网址")
                        return
                    }
                    if (type == null) {
                        AtlwToastHintUtil.getInstance().toastMsg("请选择加载类型")
                        return
                    }
                    val config = AtlwImageLoadConfig.Build().setShowViewWidth(AtlwScreenUtil.getInstance().screenWidth)
                        .setShowViewHeight(ViewGroup.LayoutParams.WRAP_CONTENT).build()
                    if (type == AtlwConfig.IMAGE_LOAD_LIBRARY_TYPE_FRESCO) {
                        AtlwImageLoadingFactory.getImageLoading(type!!).loadingImage(path, binding?.ivFresco, config)
                    } else {
                        AtlwImageLoadingFactory.getImageLoading(type!!).loadingImage(path, binding?.ivSimple, config)
                    }
                }
                //获取位图
                R.id.btn_get_bitmap -> {
                    if (path.isNullOrEmpty()) {
                        AtlwToastHintUtil.getInstance().toastMsg("请输入网址")
                        return
                    }
                    if (type == null) {
                        AtlwToastHintUtil.getInstance().toastMsg("请选择加载类型")
                        return
                    }
                    val config = AtlwImageLoadConfig.Build().setLoadGetBitmap(true).setLoadCallback(object : AtlwImageLoadCallback() {
                        override fun onFailure() {
                        }

                        override fun onSuccess(bitmap: Bitmap?, width: Int, height: Int) {
                            if (bitmap != null) {
                                binding?.ivSimple?.setImageBitmap(bitmap)
                            }
                        }

                    }).build()
                    if (type == AtlwConfig.IMAGE_LOAD_LIBRARY_TYPE_FRESCO) {
                        AtlwImageLoadingFactory.getImageLoading(type!!).loadingImage(path, binding?.ivFresco, config)
                    } else {
                        AtlwImageLoadingFactory.getImageLoading(type!!).loadingImage(path, binding?.ivSimple, config)
                    }
                }
                //高斯模糊加载图片
                R.id.btn_blur -> {
                    if (path.isNullOrEmpty()) {
                        AtlwToastHintUtil.getInstance().toastMsg("请输入网址")
                        return
                    }
                    if (type == null) {
                        AtlwToastHintUtil.getInstance().toastMsg("请选择加载类型")
                        return
                    }
                    val config = AtlwImageLoadConfig.Build().setLoadGetBitmap(true).setBlurIterations(1).setBlurRadius(3000).build()
                    if (type == AtlwConfig.IMAGE_LOAD_LIBRARY_TYPE_FRESCO) {
                        AtlwImageLoadingFactory.getImageLoading(type!!).loadingImage(path, binding?.ivFresco, config)
                    } else {
                        AtlwImageLoadingFactory.getImageLoading(type!!).loadingImage(path, binding?.ivSimple, config)
                    }
                }
            }
        }
    }
}
