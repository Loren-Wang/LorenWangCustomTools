package com.lorenwang.test.android.activity.customToolsAndroid

import android.graphics.Color
import android.lorenwang.tools.app.AtlwSpannableUtil
import android.lorenwang.tools.app.AtlwToastHintUtil
import android.lorenwang.tools.bean.AtlwSpannableClickBean
import android.lorenwang.tools.bean.AtlwSpannableTagBean
import android.os.Bundle
import com.lorenwang.test.android.base.BaseActivity
import com.lorenwang.test.android.databinding.ActivityCustomToolsSpannableBinding
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData

/**
 * 功能作用：字符串处理工具页面
 * 初始注释时间： 2021/9/18 14:25
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
class SpannableActivity : BaseActivity() {

    private var binding: ActivityCustomToolsSpannableBinding? = null
        get() {
            field = field.kttlwGetNotEmptyData { ActivityCustomToolsSpannableBinding.inflate(layoutInflater) }
            return field
        }

    override fun initView(savedInstanceState: Bundle?) {
        addShowContentView(true, binding)
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        binding?.tvClick?.let {
            AtlwSpannableUtil.getInstance()
                .setSpannableString(it, AtlwSpannableUtil.getInstance().paramsClickMessage("哈哈，123，来了", AtlwSpannableClickBean("123", Color.RED) {
                    AtlwToastHintUtil.getInstance().toastMsg("变色并点击")
                }))
        }
        binding?.tvChange?.let {
            AtlwSpannableUtil.getInstance().setSpannableString(it, AtlwSpannableUtil.getInstance().paramsTagMessage("哈哈，123，来来来来来了",
                    AtlwSpannableTagBean("来来来来来", Color.BLACK, Color.RED, Color.BLUE, 6, false, 36F, 10F, 10F, 20F, 20F, 10F, 10F)))
        }
    }
}
