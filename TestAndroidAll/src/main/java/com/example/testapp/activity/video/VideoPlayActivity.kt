package com.example.testapp.activity.video

import android.os.Bundle
import android.os.PersistableBundle
import com.example.testapp.R
import com.example.testapp.base.BaseActivity

/**
 * 功能作用：视频播放页面
 * 创建时间：2021-01-20 4:14 下午
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
class VideoPlayActivity : BaseActivity() {

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.item_list_video_play)
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        VideoPlayViewHolder(showContentView!!).setViewData(this,"https://vd4.bdstatic.com/mda-kk0nypaib3ih5tvz/sc/cae_h264_clips/1604218626/mda-kk0nypaib3ih5tvz.mp4?auth_key=1611204555-0-0-3a28571b930310122f39c1b033cd38f1&bcevod_channel=searchbox_feed&pd=1&pt=3&abtest=4_1-6_1",0)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }


}
