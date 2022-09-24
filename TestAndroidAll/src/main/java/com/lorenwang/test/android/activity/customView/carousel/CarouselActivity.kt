package com.lorenwang.test.android.activity.customView.carousel

import android.app.Activity
import android.lorenwang.commonbaseframe.adapter.AcbflwBaseBindingRecyclerViewHolder
import android.lorenwang.commonbaseframe.adapter.AcbflwBaseRecyclerAdapter
import android.lorenwang.commonbaseframe.adapter.AcbflwBaseRecyclerViewHolder
import android.lorenwang.commonbaseframe.extension.acbflwLoadNetImageConfig
import android.os.Bundle
import android.view.View
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseListActivity
import com.lorenwang.test.android.databinding.ItemListOnlyImageviewTwoBinding
import kotlinx.android.synthetic.main.activity_carousel.*


/**
 * 功能作用：层叠画廊显示
 * 初始注释时间： 2021/3/18 14:52
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
class CarouselActivity : BaseListActivity<String>() {
    private var adapter = object : AcbflwBaseRecyclerAdapter<String>(this) {
        override fun getListViewHolder(viewType: Int, itemView: View): AcbflwBaseRecyclerViewHolder<String> {
            return this@CarouselActivity.getListViewHolder(viewType, itemView)
        }
    }

    override fun setContentViewConfig(resId: Int?)  {
        super.setContentViewConfig(R.layout.activity_carousel)
        recyList.adapter = adapter
        recyList.setLayoutManagerConfig(7, 0.5F, 0.9F, 0.2F)
        recyList2.adapter = adapter
        recyList2.setLayoutManagerCircleConfig(9, 0, 280)
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)

        val list = arrayListOf<String>()
        list.add(
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fmedia-cdn.tripadvisor.com%2Fmedia%2Fphoto-s%2F01%2F3e%2F05%2F40%2Fthe-sandbar-that-links.jpg&refer=http%3A%2F%2Fmedia-cdn.tripadvisor.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1613641155&t=f497b1475fe549412973182b5f966e79")
        list.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3849299870,82529265&fm=26&gp=0.jpg")
        list.add(
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fac-q.static.booking.cn%2Fimages%2Fhotel%2Fmax1024x768%2F111%2F111145520.jpg&refer=http%3A%2F%2Fac-q.static.booking.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1613641155&t=81e44be872c785f8b3f03bd412424a36")
        list.add(
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201511%2F12%2F20151112144901_etMf5.thumb.700_0.jpeg&refer=http%3A%2F%2Fcdn.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1616165582&t=200566ad259e61316d5fee689dc33c4c")
        list.add(
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fmedia-cdn.tripadvisor.com%2Fmedia%2Fphoto-s%2F01%2F3e%2F05%2F40%2Fthe-sandbar-that-links.jpg&refer=http%3A%2F%2Fmedia-cdn.tripadvisor.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1613641155&t=f497b1475fe549412973182b5f966e79")
        list.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3849299870,82529265&fm=26&gp=0.jpg")
        list.add(
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fac-q.static.booking.cn%2Fimages%2Fhotel%2Fmax1024x768%2F111%2F111145520.jpg&refer=http%3A%2F%2Fac-q.static.booking.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1613641155&t=81e44be872c785f8b3f03bd412424a36")
        list.add(
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201511%2F12%2F20151112144901_etMf5.thumb.700_0.jpeg&refer=http%3A%2F%2Fcdn.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1616165582&t=200566ad259e61316d5fee689dc33c4c")
        list.add(
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fmedia-cdn.tripadvisor.com%2Fmedia%2Fphoto-s%2F01%2F3e%2F05%2F40%2Fthe-sandbar-that-links.jpg&refer=http%3A%2F%2Fmedia-cdn.tripadvisor.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1613641155&t=f497b1475fe549412973182b5f966e79")
        list.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3849299870,82529265&fm=26&gp=0.jpg")
        list.add(
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fac-q.static.booking.cn%2Fimages%2Fhotel%2Fmax1024x768%2F111%2F111145520.jpg&refer=http%3A%2F%2Fac-q.static.booking.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1613641155&t=81e44be872c785f8b3f03bd412424a36")
        list.add(
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201511%2F12%2F20151112144901_etMf5.thumb.700_0.jpeg&refer=http%3A%2F%2Fcdn.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1616165582&t=200566ad259e61316d5fee689dc33c4c")
        list.add(
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fmedia-cdn.tripadvisor.com%2Fmedia%2Fphoto-s%2F01%2F3e%2F05%2F40%2Fthe-sandbar-that-links.jpg&refer=http%3A%2F%2Fmedia-cdn.tripadvisor.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1613641155&t=f497b1475fe549412973182b5f966e79")
        list.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3849299870,82529265&fm=26&gp=0.jpg")
        list.add(
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fac-q.static.booking.cn%2Fimages%2Fhotel%2Fmax1024x768%2F111%2F111145520.jpg&refer=http%3A%2F%2Fac-q.static.booking.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1613641155&t=81e44be872c785f8b3f03bd412424a36")
        list.add(
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201511%2F12%2F20151112144901_etMf5.thumb.700_0.jpeg&refer=http%3A%2F%2Fcdn.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1616165582&t=200566ad259e61316d5fee689dc33c4c")

        adapter.singleTypeRefresh(list, R.layout.item_list_only_imageview_two, false,false)

    }


    fun getListViewHolder(viewType: Int, itemView: View): AcbflwBaseBindingRecyclerViewHolder<String, ItemListOnlyImageviewTwoBinding> {
        return object : AcbflwBaseBindingRecyclerViewHolder<String, ItemListOnlyImageviewTwoBinding>(itemView) {
            override fun setViewData(activity: Activity, model: String?, position: Int) {
                binding?.imgPic?.acbflwLoadNetImageConfig(model)
            }
        }
    }
}
