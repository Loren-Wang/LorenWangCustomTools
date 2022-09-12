package com.lorenwang.test.android.activity.customView.viewpager

import android.app.Activity
import android.lorenwang.commonbaseframe.adapter.AcbflwBaseBindingRecyclerViewHolder
import android.lorenwang.commonbaseframe.adapter.AcbflwBaseRecyclerAdapter
import android.lorenwang.commonbaseframe.adapter.AcbflwBaseRecyclerViewHolder
import android.lorenwang.commonbaseframe.extension.acbflwLoadNetImageConfig
import android.lorenwang.customview.viewpager.banner.AvlwBannerView
import android.lorenwang.tools.image.loading.AtlwImageLoadConfig
import android.os.Bundle
import android.view.View
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import com.lorenwang.test.android.databinding.ItemListOnlyImageviewTwoBinding

class BannerActivity : BaseActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_custom_view_banner)

        val list: MutableList<String> = ArrayList()
        list.add("http://img3.imgtn.bdimg.com/it/u=2464547320,3316604757&fm=26&gp=0.jpg")
        list.add("http://tu1.whhost.net/uploads/20181229/10/1546051661-fQRszAGlmj.jpg")
        list.add("http://youimg1.c-ctrip.com/target/tg/706/427/971/f30f03169a5a4152bf009722b3387f2f.jpg")
        list.add("http://img.xshuma.com/201210/21452012100576182.jpg")
        list.add("http://l.paipaitxt.com/118851/10/06/30/104_10897237_4ee2ef93d633782.jpg")

        showBanner(findViewById(R.id.vpgList0), list, list.size)
        showBanner(findViewById(R.id.vpgList1), list, list.size)
        showBanner(findViewById(R.id.vpgList2), list, list.size)
        showBanner(findViewById(R.id.vpgList3), list, list.size)
        showBanner(findViewById(R.id.vpgList4), list, Int.MAX_VALUE)
        showBanner(findViewById(R.id.vpgList5), list, Int.MAX_VALUE)
        showBanner(findViewById(R.id.vpgList6), list.subList(0, 1), Int.MAX_VALUE)
    }

    /**
     * 显示轮播图
     */
    private fun showBanner(vpgList: AvlwBannerView, list: List<String>, itemCount: Int) {
        val adapter: AcbflwBaseRecyclerAdapter<String?> = object : AcbflwBaseRecyclerAdapter<String?>(this) {
            override fun getListViewHolder(viewType: Int, itemView: View): AcbflwBaseRecyclerViewHolder<String?> {
                return object : AcbflwBaseBindingRecyclerViewHolder<String?, ItemListOnlyImageviewTwoBinding>(itemView) {
                    override fun setViewData(activity: Activity, model: String?, position: Int) {
                        binding?.imgPic.acbflwLoadNetImageConfig(model, false, AtlwImageLoadConfig.Build())
//                        AtlwImageLoadingFactory.getImageLoading(IMAGE_LOAD_LIBRARY_TYPE_GLIDE).loadingNetImage(model, itemView as AppCompatImageView,
//                                AtlwImageLoadConfig.Build().setShowViewHeight(itemView.height).setShowViewWidth(itemView.width).build())
                    }
                }
            }

            override fun getItemCount(): Int {
                return itemCount
            }
        }
        adapter.singleTypeRefresh(list, R.layout.item_list_only_imageview_two, false,false)
        vpgList.setViewData(list.size, adapter)
    }
}
