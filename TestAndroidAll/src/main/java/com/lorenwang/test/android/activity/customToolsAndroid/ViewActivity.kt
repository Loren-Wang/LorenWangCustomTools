package com.lorenwang.test.android.activity.customToolsAndroid

import android.content.res.ColorStateList
import android.graphics.Color
import android.lorenwang.tools.app.AtlwToastHintUtil
import android.lorenwang.tools.app.AtlwViewUtil
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import com.lorenwang.test.android.databinding.ActivityCustomToolsViewBinding
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData

/**
 * 功能作用：控件相关工具类
 * 初始注释时间： 2021/9/18 15:55
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
class ViewActivity : BaseActivity() {
    private var binding: ActivityCustomToolsViewBinding? = null
        get() {
            field = field.kttlwGetNotEmptyData { ActivityCustomToolsViewBinding.inflate(layoutInflater) }
            return field
        }

    override fun initView(savedInstanceState: Bundle?) {
        addShowContentView(true, binding)
    }


    fun mainClick(view: View?) {
        if (view != null) {
            val width = (Math.random() * 1000).toInt()
            val height = (Math.random() * 1000).toInt()
            val color =
                Color.argb((Math.random() * 255).toInt(), (Math.random() * 255).toInt(), (Math.random() * 255).toInt(), (Math.random() * 255).toInt())
            when (view.id) {
                R.id.btnGetParams -> {
                    AtlwToastHintUtil.getInstance().toastMsg(AtlwViewUtil.getInstance().getViewLayoutParams(view, null, null).toString())
                }
                R.id.btnSetWidthHeight -> {
                    AtlwViewUtil.getInstance().setViewWidthHeight(view, width, height)
                }
                R.id.btnSetParams -> {
                    AtlwViewUtil.getInstance().setViewWidthHeightMargin(view, width, height, width, height, height, width)
                }
                R.id.btnSetBgColor -> {
                    AtlwViewUtil.getInstance().setBackgroundTint(binding?.imgSrc, ColorStateList.valueOf(color))
                }
                R.id.btnSetSrcColor -> {
                    AtlwViewUtil.getInstance().setImageSrcTint(binding?.imgSrc, ColorStateList.valueOf(color))
                }
                R.id.btnSetDrawable -> {
                    AtlwViewUtil.getInstance().setTextViewDrawableLRTBTint(view as AppCompatButton, ColorStateList.valueOf(color))
                }
                R.id.btnGetTextWidth -> {
                    val button = view as AppCompatButton
                    AtlwToastHintUtil.getInstance()
                        .toastMsg("当前文字宽度：${AtlwViewUtil.getInstance().getStrTextWidth(button.paint, button.text.toString())}")
                }
                R.id.btnGetTextHeight -> {
                    val button = view as AppCompatButton
                    AtlwToastHintUtil.getInstance().toastMsg("当前文字高度：${AtlwViewUtil.getInstance().getStrTextHeight(button.paint)}")
                }
                R.id.btnGetViewBitmap -> {
                    AtlwViewUtil.getInstance().getViewBitmap(binding!!.root)?.let {
                        if (!it.isRecycled) {
                            binding?.imgSrc?.setImageBitmap(it)
                        }
                    }
                }
                R.id.btnGetViewBitmapWidthHeight -> {
                    AtlwViewUtil.getInstance().getViewBitmap(binding!!.root, (Math.random() * 500).toInt(), (Math.random() * 500).toInt(), null)
                        ?.let {
                            if (!it.isRecycled) {
                                binding?.imgSrc?.setImageBitmap(it)
                            }
                        }
                }
                R.id.btnGetViewBitmapIgnore -> {
                    AtlwViewUtil.getInstance().getViewBitmap(binding!!.root, arrayOf(R.id.btnGetViewBitmapIgnore))?.let {
                        if (!it.isRecycled) {
                            binding?.imgSrc?.setImageBitmap(it)
                        }
                    }
                }

                else -> {

                }
            }
        }
    }
}
