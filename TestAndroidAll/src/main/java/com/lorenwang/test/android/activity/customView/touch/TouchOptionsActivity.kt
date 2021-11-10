package com.lorenwang.test.android.activity.customView.touch

import android.lorenwang.customview.touchs.AvlwTouchOptionsView
import android.lorenwang.tools.app.AtlwToastHintUtil
import android.os.Bundle
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import kotlinbase.lorenwang.tools.extend.kttlwGetScreenHeight
import kotlinbase.lorenwang.tools.extend.kttlwGetScreenWidth
import kotlinx.android.synthetic.main.activity_custom_view_touch_options.*

/**
 * 功能作用：触摸操作界面
 * 初始注释时间： 2021/3/11 18:47
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
class TouchOptionsActivity : BaseActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_custom_view_touch_options)
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        val list = arrayListOf<AvlwTouchOptionsView.TouchRangBean>()
        list.add(AvlwTouchOptionsView.TouchRangBean(200F, 200F, 300F, 500F, "1"))
        toTouch?.setOriginUserData(kttlwGetScreenWidth(), kttlwGetScreenHeight(), list)
        toTouch?.setTouchListener {
            AtlwToastHintUtil.getInstance().toastMsg(it.tag.toString())
        }
        toTouch?.setDrawTouch(true, null)
    }
}
