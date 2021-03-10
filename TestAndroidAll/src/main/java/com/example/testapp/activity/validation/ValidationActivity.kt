package com.example.testapp.activity.validation

import android.lorenwang.customview.validation.AvlwSliderValidationView
import android.lorenwang.tools.app.AtlwToastHintUtil
import android.os.Bundle
import com.example.testapp.R
import com.example.testapp.base.BaseActivity
import kotlinx.android.synthetic.main.activity_validation.*

/**
 * 功能作用：验证界面
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

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_validation)
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
