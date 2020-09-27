package com.example.testapp.activity.anim

import android.lorenwang.anims.AalwAnimUtils
import android.os.Bundle
import android.view.View
import com.example.testapp.base.BaseActivity
import com.example.testapp.R
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
                AalwAnimUtils.getInstance().startTranslateYAnim(btnTranslateY, 0F, -300F, 1000L)
            }
            R.id.btnTranslateX -> {
                AalwAnimUtils.getInstance().startTranslateXAnim(btnTranslateX, 0F, 300F, 1000L)
            }
            R.id.btnTranslateXY -> {
                AalwAnimUtils.getInstance().startTranslateAnimation(btnTranslateXY, 0F, 0F, 300F, -300F, 1000L, null)
            }
            R.id.btnScaleY -> {
                AalwAnimUtils.getInstance().startScaleYAnim(btnScaleY, 1F, 0.2F, 1000L)
            }
            R.id.btnScaleX -> {
                AalwAnimUtils.getInstance().startScaleXAnim(btnScaleX, 1F, 0.2F, 1000L)
            }
            R.id.btnScaleXY -> {
                AalwAnimUtils.getInstance().startScaleAnimation(btnScaleXY, 1F, 1F, 0.2F, 0.2F, 1000L, null)
            }
            R.id.btnRotateFrom -> {
                AalwAnimUtils.getInstance().startRotateAnimation(btnRotateFrom, 0F, 90F, 1000L)
            }
            R.id.btnRotate -> {
                AalwAnimUtils.getInstance().startRotateAnimation(btnRotate, 0F, -90F, 400F, 200F, 1000L, null)
            }
            R.id.btnAlpha -> {
                AalwAnimUtils.getInstance().startAlphaAnimation(btnAlpha, 1F, 0.2F, 2000L)
            }
            R.id.btnTranslateYObj -> {
                AalwAnimUtils.getInstance().getTranslateYAnimator(btnTranslateYObj, 0F, -300F, 1000L, null).start()
            }
            R.id.btnTranslateXObj -> {
                AalwAnimUtils.getInstance().getTranslateXAnimator(btnTranslateXObj, 0F, 300F, 1000L, null).start()
            }
            R.id.btnTranslateXYObj -> {
                btnTranslateXYObj.startAnimation(AalwAnimUtils.getInstance().getTranslateAnimator(0F, 300F, 0F, -300F, 1000L, null))
            }
            R.id.btnScaleYObj -> {
                AalwAnimUtils.getInstance().getScaleYAnimator(btnScaleYObj, 1F, 0.2F, 1000L, null).start()
            }
            R.id.btnScaleXObj -> {
                AalwAnimUtils.getInstance().getScaleXAnimator(btnScaleXObj, 1F, 0.2F, 1000L, null).start()
            }
            R.id.btnScaleXYObj -> {
                btnScaleXYObj.startAnimation(AalwAnimUtils.getInstance().getScaleAnimator(1F, 0.2F, 1F, 0.2F, 200F, 0F, 1000L, null))
            }
            R.id.btnRotateXObj -> {
                AalwAnimUtils.getInstance().getRotateXAnimator(btnRotateXObj, 0F, 20F, 1000L, null).start()
            }
            R.id.btnRotateYObj -> {
                AalwAnimUtils.getInstance().getRotateYAnimator(btnRotateYObj, 0F, 20F, 1000L, null).start()
            }
            R.id.btnRotateXYObj -> {
                AalwAnimUtils.getInstance().getRotateAnimator(btnRotateXYObj, 0F, 20F, 1000L, null).start()
            }
            R.id.btnAlphaObj -> {
                AalwAnimUtils.getInstance().getAlphaAnimator(btnAlphaObj, 1F, 0.2F, 1000L, null).start()
            }
            else -> {

            }
        }
    }
}
