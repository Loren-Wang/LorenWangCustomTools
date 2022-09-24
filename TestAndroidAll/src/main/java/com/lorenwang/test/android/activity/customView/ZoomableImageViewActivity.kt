package com.lorenwang.test.android.activity.customView

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
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import com.lorenwang.test.android.databinding.ActivityCustomViewZoomableImageViewBinding
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData
import kotlinbase.lorenwang.tools.extend.kttlwThrottleClick

/**
 * 功能作用：
 * 创建时间：2021/11/5 16:28
 * 创建者：王亮（Loren）
 * 备注：
 */
class ZoomableImageViewActivity : BaseActivity() {

    private var binding: ActivityCustomViewZoomableImageViewBinding? = null
        get() {
            field = field.kttlwGetNotEmptyData { ActivityCustomViewZoomableImageViewBinding.inflate(layoutInflater) }
            return field
        }

    override fun setContentViewConfig(resId: Int?)  {
        addShowContentView(true, binding!!)
    }

    override fun initListener(savedInstanceState: Bundle?) {
        super.initListener(savedInstanceState)
        binding?.btnDialog?.setOnClickListener {
            val dialog = AvlwZoomablePreviewDialog(this)
            dialog.setImagePath(R.drawable.ic_launcher_background, R.drawable.icon_empty_add_unable,
                "https://qcampfile.oss-cn-shanghai.aliyuncs.com/qcamp/answerConfig/1910/28/1572252438644_1204x8193.png")
            dialog.show()
        }
        binding?.btnConfirmCrop?.kttlwThrottleClick {
            val fl1 = binding?.imgZoom!!.width * 0.6F
            val fl = (1 - fl1 / binding?.imgZoom!!.height) / 2
            val cropBitmap = binding?.imgZoom!!.getCenterBitmap(0.6F, true)
            binding?.imgZoom?.setBgImageViewBitmap(cropBitmap)
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
                            binding?.imgZoom!!.setBgImageViewBitmap(bitmap)
                        }
                    }).build())
            }

            override fun onCancel() {
            }
        })

    }
}
