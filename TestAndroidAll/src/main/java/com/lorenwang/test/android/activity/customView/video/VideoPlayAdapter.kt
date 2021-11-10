package com.lorenwang.test.android.activity.customView.video

import android.app.Activity
import android.lorenwang.commonbaseframe.adapter.AcbflwBaseRecyclerAdapter
import android.lorenwang.commonbaseframe.adapter.AcbflwBaseRecyclerViewHolder
import android.view.View

/**
 * 功能作用：视频播放适配器
 * 创建时间：2021-01-21 12:14 下午
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
class VideoPlayAdapter(activity: Activity) : AcbflwBaseRecyclerAdapter<String>(activity) {
    override fun getListViewHolder(viewType: Int, itemView: View): AcbflwBaseRecyclerViewHolder<String> {
        return VideoPlayViewHolder(itemView)
    }
}
