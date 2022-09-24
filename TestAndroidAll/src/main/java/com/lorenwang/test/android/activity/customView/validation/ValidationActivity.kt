package com.lorenwang.test.android.activity.customView.validation

import android.lorenwang.customview.validation.AvlwSliderValidationView
import android.lorenwang.tools.app.AtlwToastHintUtil
import android.os.Bundle
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import kotlinx.android.synthetic.main.activity_custom_view_validation.*

/**
 * 功能作用：滑块验证界面
 * 初始注释时间： 2021/3/10 11:27
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
class ValidationActivity : BaseActivity() {

    override fun setContentViewConfig(resId: Int?)  {
        super.setContentViewConfig(R.layout.activity_custom_view_validation)
    }

    override fun initListener(savedInstanceState: Bundle?) {
        super.initListener(savedInstanceState)
        svView?.setOnResultCallback(object : AvlwSliderValidationView.OnResultCallback {
            override fun success(view: AvlwSliderValidationView) {
                AtlwToastHintUtil.getInstance().toastMsg("成功")
                view.reset()
            }

            override fun fail(view: AvlwSliderValidationView) {
                AtlwToastHintUtil.getInstance().toastMsg("失败")
                view.reset()
            }
        })
    }
}
