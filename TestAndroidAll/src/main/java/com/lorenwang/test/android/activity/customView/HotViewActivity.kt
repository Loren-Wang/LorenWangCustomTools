package com.lorenwang.test.android.activity.customView

import android.lorenwang.customview.imageview.AvlwImageHotSpotsView
import android.lorenwang.tools.app.AtlwToastHintUtil
import android.os.Bundle
import android.widget.ImageView
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData

/**
 * 功能作用：热区控件
 * 初始注释时间： 2021/11/8 09:36
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
class HotViewActivity : BaseActivity() {

    /**
     * 热区控件
     */
    private var ivHot: AvlwImageHotSpotsView? = null
        get() {
            field = field.kttlwGetNotEmptyData { findViewById(R.id.ih_hot) }
            return field
        }

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_csustom_view_hot_view)
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        ivHot?.setImageResource(R.drawable.image_default)
        ivHot?.scaleType = ImageView.ScaleType.FIT_XY
        ivHot?.setHotSpotsData(arrayListOf(AvlwImageHotSpotsView.ImageHotSpotsInfo(0F, 0.0F, 0.1F, 0.1F, "第一个区域，左上角百分之10"),
            AvlwImageHotSpotsView.ImageHotSpotsInfo(0.9F, 0F, 0.1F, 0.1F, "第二个区域，右上角百分之10"),
            AvlwImageHotSpotsView.ImageHotSpotsInfo(0F, 0.9F, 0.1F, 0.1F, "第三个区域，左下角百分之10"),
            AvlwImageHotSpotsView.ImageHotSpotsInfo(0.9F, 0.9F, 0.1F, 0.1F, "第四个区域，右下角百分之10"))) { _, data ->
            AtlwToastHintUtil.getInstance().toastMsg(data.data)
        }
    }
}
