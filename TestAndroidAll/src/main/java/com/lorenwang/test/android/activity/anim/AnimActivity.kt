package com.lorenwang.test.android.activity.anim

import android.lorenwang.anims.AalwAnimUtil
import android.os.Bundle
import android.view.View
import com.lorenwang.test.android.base.BaseActivity
import com.lorenwang.test.android.R
import kotlinx.android.synthetic.main.activity_anims.*

/**
 * 功能作用：动画相关Activity
 * 初始注释时间： 2020/4/5 3:00 下午
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 *@author LorenWang（王亮）
 */
class AnimActivity : BaseActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_anims)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    /**
     * 动画相关点击
     */
    fun animClick(v: View) {
        when (v.id) {
            R.id.btnTranslateY -> {
                AalwAnimUtil.getInstance().startTranslateYAnim(btnTranslateY, 0F, -300F, 1000L)
            }
            R.id.btnTranslateX -> {
                AalwAnimUtil.getInstance().startTranslateXAnim(btnTranslateX, 0F, 300F, 1000L)
            }
            R.id.btnTranslateXY -> {
                AalwAnimUtil.getInstance().startTranslateAnimation(btnTranslateXY, 0F, 0F, 300F, -300F, 1000L, null)
            }
            R.id.btnScaleY -> {
                AalwAnimUtil.getInstance().startScaleYAnim(btnScaleY, 1F, 0.2F, 1000L)
            }
            R.id.btnScaleX -> {
                AalwAnimUtil.getInstance().startScaleXAnim(btnScaleX, 1F, 0.2F, 1000L)
            }
            R.id.btnScaleXY -> {
                AalwAnimUtil.getInstance().startScaleAnimation(btnScaleXY, 1F, 1F, 0.2F, 0.2F, 1000L, null)
            }
            R.id.btnRotateFrom -> {
                AalwAnimUtil.getInstance().startRotateAnimation(btnRotateFrom, 0F, 90F, 1000L)
            }
            R.id.btnRotate -> {
                AalwAnimUtil.getInstance().startRotateAnimation(btnRotate, 0F, -90F, 400F, 200F, 1000L, null)
            }
            R.id.btnAlpha -> {
                AalwAnimUtil.getInstance().startAlphaAnimation(btnAlpha, 1F, 0.2F, 2000L)
            }
            R.id.btnTranslateYObj -> {
                AalwAnimUtil.getInstance().getTranslateYAnimator(btnTranslateYObj, 0F, -300F, 1000L, null).start()
            }
            R.id.btnTranslateXObj -> {
                AalwAnimUtil.getInstance().getTranslateXAnimator(btnTranslateXObj, 0F, 300F, 1000L, null).start()
            }
            R.id.btnTranslateXYObj -> {
                btnTranslateXYObj.startAnimation(
                    AalwAnimUtil.getInstance().getTranslateAnimator(0F, 300F, 0F, -300F, 1000L, null))
            }
            R.id.btnScaleYObj -> {
                AalwAnimUtil.getInstance().getScaleYAnimator(btnScaleYObj, 1F, 0.2F, 1000L, null).start()
            }
            R.id.btnScaleXObj -> {
                AalwAnimUtil.getInstance().getScaleXAnimator(btnScaleXObj, 1F, 0.2F, 1000L, null).start()
            }
            R.id.btnScaleXYObj -> {
                btnScaleXYObj.startAnimation(
                    AalwAnimUtil.getInstance().getScaleAnimator(1F, 0.2F, 1F, 0.2F, 200F, 0F, 1000L, null))
            }
            R.id.btnRotateXObj -> {
                AalwAnimUtil.getInstance().getRotateXAnimator(btnRotateXObj, 0F, 20F, 1000L, null).start()
            }
            R.id.btnRotateYObj -> {
                AalwAnimUtil.getInstance().getRotateYAnimator(btnRotateYObj, 0F, 20F, 1000L, null).start()
            }
            R.id.btnRotateXYObj -> {
                AalwAnimUtil.getInstance().getRotateAnimator(btnRotateXYObj, 0F, 20F, 1000L, null).start()
            }
            R.id.btnAlphaObj -> {
                AalwAnimUtil.getInstance().getAlphaAnimator(btnAlphaObj, 1F, 0.2F, 1000L, null).start()
            }
            else -> {

            }
        }
    }
}
