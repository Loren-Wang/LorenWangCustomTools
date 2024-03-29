package com.lorenwang.test.android.activity.customToolsAndroid.image

import android.lorenwang.commonbaseframe.image.AcbflwFileSelectCallback
import android.lorenwang.commonbaseframe.image.AcbflwImageSelectUtil
import android.lorenwang.commonbaseframe.image.AcbflwLocalImageSelectBean
import android.os.Bundle
import android.widget.Toast
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData
import kotlinbase.lorenwang.tools.extend.kttlwThrottleClick
import kotlinx.android.synthetic.main.activity_image_select.*

/**
 * 功能作用：图片选择相关页面
 * 初始注释时间： 2021/4/14 17:39
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
class ImageSelectActivity : BaseActivity() {
    override fun setContentViewConfig(resId: Int?)  {
        super.setContentViewConfig(R.layout.activity_image_select)
    }

    override fun initListener(savedInstanceState: Bundle?) {
        super.initListener(savedInstanceState)
        btnSelectVideoOnly.kttlwThrottleClick {
            AcbflwImageSelectUtil.getInstance().openSelectVideo(this, 10, 0, 90, null, object : AcbflwFileSelectCallback {
                override fun onResult(result: MutableList<AcbflwLocalImageSelectBean>?) {
                    Toast.makeText(this@ImageSelectActivity, result.kttlwGetNotEmptyData{arrayListOf()}.size.toString(), Toast.LENGTH_LONG).show()
                }

                override fun onCancel() {

                }
            })

        }
    }
}
