package com.example.testapp.viewpager

import android.app.Activity
import android.lorenwang.common_base_frame.adapter.AcbflwBaseRecyclerAdapter
import android.lorenwang.common_base_frame.adapter.AcbflwBaseRecyclerViewHolder
import android.lorenwang.customview.viewpager.banner.AvlwBannerView
import android.lorenwang.tools.AtlwSetting.IMAGE_LOAD_LIBRARY_TYPE_GLIDE
import android.lorenwang.tools.image.loading.AtlwImageLoadingFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.example.testapp.BaseActivity
import com.example.testapp.R
import java.util.*

class BannerActivity : BaseActivity() {
    override fun onChildCreate(savedInstanceState: Bundle?) {
        addChildView(R.layout.activity_banner)

        val list: MutableList<String> = ArrayList()
        list.add("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3429149743,132351892&fm=26&gp=0.jpg")
        list.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3944680232,2054173354&fm=26&gp=0.jpg")
        list.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1578992998636&di=99ac1ca07c0d1020664db8b32f12ea6d&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201708%2F28%2F20170828114331_3S4Ut.thumb.700_0.jpeg")

        showBanner(findViewById(R.id.vpgList0), list, list.size)
        showBanner(findViewById(R.id.vpgList1), list, list.size)
        showBanner(findViewById(R.id.vpgList2), list, list.size)
        showBanner(findViewById(R.id.vpgList3), list, list.size)
        showBanner(findViewById(R.id.vpgList4), list, Int.MAX_VALUE)
        showBanner(findViewById(R.id.vpgList5), list, Int.MAX_VALUE)
    }

    /**
     * 显示轮播图
     */
    private fun showBanner(vpgList: AvlwBannerView, list: List<String>, itemCount: Int) {
        val adapter: AcbflwBaseRecyclerAdapter<String?> = object : AcbflwBaseRecyclerAdapter<String?>(this) {
            override fun getListViewHolder(viewType: Int, itemView: View): AcbflwBaseRecyclerViewHolder<String?>? {
                return object : AcbflwBaseRecyclerViewHolder<String?>(itemView) {
                    override fun setViewData(activity: Activity?, model: String?, position: Int) {
                        AtlwImageLoadingFactory.getImageLoading(IMAGE_LOAD_LIBRARY_TYPE_GLIDE)
                                .loadingNetImage(model, itemView as AppCompatImageView, itemView.getWidth(), itemView.getHeight())
                    }
                }
            }

            override fun getItemCount(): Int {
                return itemCount
            }
        }
        adapter.singleTypeRefresh(list, R.layout.item_imageview, false)
        vpgList.setViewData(list.size, adapter)
    }
}
