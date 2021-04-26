package com.example.testapp.activity.image

import android.graphics.Bitmap
import android.lorenwang.commonbaseframe.image.AcbflwFileSelectCallback
import android.lorenwang.commonbaseframe.image.AcbflwImageSelectUtil
import android.lorenwang.commonbaseframe.image.AcbflwLocalImageSelectBean
import android.lorenwang.customview.dialog.AvlwZoomablePreviewDialog
import android.lorenwang.tools.AtlwConfig
import android.lorenwang.tools.image.loading.AtlwImageLoadCallback
import android.lorenwang.tools.image.loading.AtlwImageLoadConfig
import android.lorenwang.tools.image.loading.AtlwImageLoadingFactory
import android.os.Bundle
import com.example.testapp.R
import com.example.testapp.base.BaseActivity
import kotlinbase.lorenwang.tools.extend.kttlwThrottleClick
import kotlinx.android.synthetic.main.activity_zoomable_image_view.*

class ZoomableImageViewActivity : BaseActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_zoomable_image_view)
    }

    override fun initListener(savedInstanceState: Bundle?) {
        super.initListener(savedInstanceState)
        btnDialog.setOnClickListener {
            val dialog = AvlwZoomablePreviewDialog(this)
            dialog.setImagePath(R.drawable.ic_launcher_background, R.drawable.icon_empty_add_unable,
                "https://qcampfile.oss-cn-shanghai.aliyuncs.com/qcamp/answerConfig/1910/28/1572252438644_1204x8193.png")
            dialog.show()
        }
        btnConfirmCrop.kttlwThrottleClick {
            val fl1 = imgZoom.width * 0.6F
            val fl = (1 - fl1 / imgZoom.height) / 2
            val cropBitmap = imgZoom.getCenterBitmap(0.6F,true)
            imgZoom.setBgImageViewBitmap(cropBitmap)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        AcbflwImageSelectUtil.getInstance().openSelectImage(this, 1, 10, arrayListOf(), true, object : AcbflwFileSelectCallback {
            override fun onResult(result: MutableList<AcbflwLocalImageSelectBean>?) {
                AtlwImageLoadingFactory.getImageLoading(AtlwConfig.IMAGE_LOAD_LIBRARY_TYPE_FRESCO).loadingLocalImage(result!![0].path, null,
                    AtlwImageLoadConfig.Build().setLoadGetBitmap(true).setLoadCallback(object : AtlwImageLoadCallback() {
                        override fun onFailure() {

                        }

                        override fun onSuccess(bitmap: Bitmap?, width: Int, height: Int) {
                            imgZoom.setBgImageViewBitmap(bitmap)
                        }
                    }).build())
            }

            override fun onCancel() {
            }
        })

    }
}
