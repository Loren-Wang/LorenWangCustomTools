package com.example.testapp.activity.video

import android.os.Bundle
import com.example.testapp.R
import com.example.testapp.base.BaseFragment

/**
 * 功能作用：视频播放页面
 * 创建时间：2021-01-21 12:10 下午
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
class VideoPlayFragment : BaseFragment() {
    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.page_common_video_play)
    }
}
