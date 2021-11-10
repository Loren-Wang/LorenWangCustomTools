package com.lorenwang.test.android.activity.customView.video

import android.app.Activity
import android.lorenwang.commonbaseframe.adapter.AcbflwBaseRecyclerViewHolder
import android.lorenwang.customview.video.AvlwVideoPlayView
import android.lorenwang.tools.app.AtlwActivityUtil
import android.lorenwang.tools.app.AtlwToastHintUtil
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.lorenwang.test.android.R

/**
 * 功能作用：
 * 创建时间：2021-01-21 12:15 下午
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
class VideoPlayViewHolder(view: View) : AcbflwBaseRecyclerViewHolder<String>(view) {
    private var vpPlay = itemView.findViewById<AvlwVideoPlayView>(R.id.vpPlay)
    override fun setViewData(activity: Activity, model: String?, position: Int) {
        model?.let {
            vpPlay?.setPlayCallback(object : AvlwVideoPlayView.PlayCallback() {
                override fun playStart(isLoopPlay: Boolean, isAutoPlay: Boolean) {
                    AtlwToastHintUtil.getInstance().toastMsg("开始播放----${it}")
                }

                override fun playFinish() {
                    AtlwToastHintUtil.getInstance().toastMsg("播放完成----${it}")
                }

                override fun progressChange(currentTime: Long, progresss: Float) {
                    AtlwToastHintUtil.getInstance().toastMsg("播放进度----${progresss}")
                }
            })
            vpPlay?.setReadyPlay(it, model.hashCode().toString(), false, true, false)
            itemView.findViewById<AppCompatButton>(R.id.btnStart).setOnClickListener { videoPlayOnClick(activity, it) }
            itemView.findViewById<AppCompatButton>(R.id.btnPause).setOnClickListener { videoPlayOnClick(activity, it) }
            itemView.findViewById<AppCompatButton>(R.id.btnFull).setOnClickListener { videoPlayOnClick(activity, it) }
        }
    }

    fun videoPlayOnClick(activity: Activity, view: View) {
        when (view.id) {
            R.id.btnStart -> {
                vpPlay?.startPlay()
            }
            R.id.btnPause -> {
                vpPlay?.pausePlay()
            }
            R.id.btnFull -> {
                AtlwActivityUtil.getInstance().changeActivityScreenOrientation(activity)
            }
            else -> {

            }
        }
    }
}
