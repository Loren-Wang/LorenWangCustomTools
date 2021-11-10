package com.lorenwang.test.android.activity.customView

import android.lorenwang.tools.app.AtlwToastHintUtil
import android.os.Bundle
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import com.lorenwang.test.android.databinding.ActivityCustomViewSudokuSwipeGesturesBinding
import kotlinbase.lorenwang.tools.extend.kttlwThrottleClick
import kotlinbase.lorenwang.tools.extend.kttlwToJsonData

/**
 * 功能作用：九宫格手势控件
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
class SudokuSwipeGesturesActivity : BaseActivity() {

    private lateinit var mBinding: ActivityCustomViewSudokuSwipeGesturesBinding

    private var mType1Enable = true
    private var mType2Enable = true
    private var mType1Error = false
    private var mType2Error = false

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_custom_view_sudoku_swipe_gestures)
        mBinding = ActivityCustomViewSudokuSwipeGesturesBinding.bind(findViewById(R.id.root))
    }

    override fun initListener(savedInstanceState: Bundle?) {
        super.initListener(savedInstanceState)
        mBinding.ssgType1.setInputStateChangeCallback {
            AtlwToastHintUtil.getInstance().toastMsg(it.kttlwToJsonData())
            if (mType1Error) {
                mBinding.ssgType1.gestureError()
            }
        }
        mBinding.ssgType2.setInputStateChangeCallback {
            AtlwToastHintUtil.getInstance().toastMsg(it.kttlwToJsonData())
            if (mType2Error) {
                mBinding.ssgType2.gestureError()
            }
        }
        mBinding.btnType1Allow.kttlwThrottleClick {
            mType1Enable = !mType1Enable
            mBinding.ssgType1.setAllowDraw(mType1Enable)
            mBinding.btnType1Allow.text = if(mType1Enable) "禁用绘制" else "启用绘制"
        }
        mBinding.btnType1Change.kttlwThrottleClick {
            if (mBinding.ssgType1.circleShowType == 1) {
                mBinding.ssgType1.circleShowType = 2
            } else {
                mBinding.ssgType1.circleShowType = 1
            }
        }
        mBinding.btnType1Reset.kttlwThrottleClick {
            mBinding.ssgType1.resetAll()
        }
        mBinding.btnType1Error.kttlwThrottleClick {
            mType1Error = !mType1Error
            mBinding.btnType1Error.text = if(mType1Error) "禁用结束报错" else "开启结束报错"
        }
        mBinding.btnType2Allow.kttlwThrottleClick {
            mType2Enable = !mType2Enable
            mBinding.ssgType2.setAllowDraw(mType2Enable)
            mBinding.btnType2Allow.text = if(mType2Enable) "禁用绘制" else "启用绘制"
        }
        mBinding.btnType2Change.kttlwThrottleClick {
            if (mBinding.ssgType2.circleShowType == 1) {
                mBinding.ssgType2.circleShowType = 2
            } else {
                mBinding.ssgType2.circleShowType = 1
            }
        }
        mBinding.btnType2Reset.kttlwThrottleClick {
            mBinding.ssgType2.resetAll()
        }
        mBinding.btnType2Error.kttlwThrottleClick {
            mType2Error = !mType2Error
            mBinding.btnType2Error.text = if(mType2Error) "禁用结束报错" else "开启结束报错"
        }
    }
}
