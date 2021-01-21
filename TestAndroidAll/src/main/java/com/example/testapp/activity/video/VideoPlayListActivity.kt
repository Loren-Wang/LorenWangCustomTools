package com.example.testapp.activity.video

import android.lorenwang.customview.video.AvlwVideoPlayManager
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.example.testapp.R
import com.example.testapp.base.BaseActivity
import kotlinbase.lorenwang.tools.extend.*
import kotlinx.android.synthetic.main.activity_video_play_list.*
import kotlinx.android.synthetic.main.page_common_video_play.*

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
class VideoPlayListActivity : BaseActivity() {

    private var fragment: VideoPlayFragment? = null
        get() {
            if (field == null) {
                for (fragment in supportFragmentManager.fragments) {
                    if (fragment is VideoPlayFragment) {
                        field = fragment
                        break
                    }
                }
            }
            return field
        }
    private var adapter: VideoPlayAdapter? = null
        get() {
            field = field.kttlwGetNotEmptyData(VideoPlayAdapter(this))
            return field
        }

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_video_play_list)

        vpgList?.orientation = ViewPager2.ORIENTATION_VERTICAL
        vpgList?.adapter = adapter
    }

    override fun initListener(savedInstanceState: Bundle?) {
        super.initListener(savedInstanceState)
        btnChange?.kttlwThrottleClick {
            if (clContent.visibility == View.GONE) {
                fragment?.let { it1 -> supportFragmentManager.beginTransaction().hide(it1).commit() }
                clContent.kttlwToVisible()
            } else {
                fragment?.let { it1 -> supportFragmentManager.beginTransaction().show(it1).commit() }
                clContent.kttlwToGone()
            }
        }
        vpgList?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val uid = adapter?.adapterDataList?.get(position)?.bean.hashCode().toString()
                if(uid.kttlwIsNotNullOrEmpty()){
                    AvlwVideoPlayManager.getInstance().getUidVideoPlayView(this@VideoPlayListActivity,uid).startPlay()
                }
            }
        })
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        val list = arrayListOf<String>()
        list.add(
            "https://vd4.bdstatic.com/mda-kk0nypaib3ih5tvz/sc/cae_h264_clips/1604218626/mda-kk0nypaib3ih5tvz.mp4?auth_key=1611204555-0-0-3a28571b930310122f39c1b033cd38f1&bcevod_channel=searchbox_feed&pd=1&pt=3&abtest=4_1-6_1")
        list.add(
            "https://vd3.bdstatic.com/mda-km8k0uch7k59utyn/hd/cae_h264_clips/1607494739/mda-km8k0uch7k59utyn.mp4?auth_key=1611204600-0-0-a59fe1971e64344df5a2118d879e138f&bcevod_channel=searchbox_feed&pd=1&pt=3&abtest=all")
        list.add(
            "https://vd4.bdstatic.com/mda-maij357di5n93k1n/hd/mda-maij357di5n93k1n.mp4?auth_key=1611204649-0-0-6c408e73914cc5df8187388429b5b7f2&bcevod_channel=searchbox_feed&pd=1&pt=3&abtest=all")
        adapter?.singleTypeRefresh(list, R.layout.item_list_video_play, false)
    }
}
