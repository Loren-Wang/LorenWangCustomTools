package com.lorenwang.test.android.activity.customView.textview

import android.os.Bundle
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import com.lorenwang.test.android.databinding.ActivityCustomViewTimeShowBinding
import java.util.*

/**
 * 功能作用：时间显示文本页面
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
class TimeShowActivity : BaseActivity() {

    private lateinit var mBinding: ActivityCustomViewTimeShowBinding

    override fun setContentViewConfig(resId: Int?)  {
        super.setContentViewConfig(R.layout.activity_custom_view_time_show)
        mBinding = ActivityCustomViewTimeShowBinding.bind(findViewById(R.id.root))
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        mBinding.tvTime1.setShowTime(System.currentTimeMillis() - 2314331434, System.currentTimeMillis())
        mBinding.tvTime2.setShowTime(Date())
    }
}
