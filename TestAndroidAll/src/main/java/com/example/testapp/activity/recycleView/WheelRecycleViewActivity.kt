package com.example.testapp.activity.recycleView

import android.os.Bundle
import com.example.testapp.R
import com.example.testapp.base.BaseActivity
import kotlinbase.lorenwang.tools.extend.kttlwThrottleClick
import kotlinx.android.synthetic.main.activity_wheel_recycle_view.*

/**
 * 功能作用：滚筒控件显示页面
 * 初始注释时间： 2021/1/31 4:48 下午
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
class WheelRecycleViewActivity : BaseActivity() {
    val list = arrayListOf<Any>("测试1", "测试2", "测试3", "测试4", "测试5", "测试6", "测试7", "测试8", "测试9")
    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_wheel_recycle_view)
    }

    override fun initListener(savedInstanceState: Bundle?) {
        super.initListener(savedInstanceState)
        btnChange?.kttlwThrottleClick {
            rvList?.setSelect(edtCount.text.toString().toInt())
        }
        btnRemove?.kttlwThrottleClick {
            list.removeAt(edtCount.text.toString().toInt())
            rvList?.setData(list)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        rvList?.setData(list)
        rvList?.setSelect(5)
    }
}
