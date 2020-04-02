package com.example.testapp.viewpager

import android.app.Activity
import android.lorenwang.common_base_frame.adapter.AcbflwBaseRecyclerAdapter
import android.lorenwang.common_base_frame.adapter.AcbflwBaseRecyclerViewHolder
import android.lorenwang.customview.viewpager.AvlwCustomViewPager
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

        val vpgList0: AvlwCustomViewPager = findViewById(R.id.vpgList0)
        val adapter0: AcbflwBaseRecyclerAdapter<String?> = object : AcbflwBaseRecyclerAdapter<String?>(this) {
            override fun getListViewHolder(viewType: Int, itemView: View): AcbflwBaseRecyclerViewHolder<String?>? {
                return object : AcbflwBaseRecyclerViewHolder<String?>(itemView) {
                    override fun setViewData(activity: Activity?, model: String?, position: Int) {
                        AtlwImageLoadingFactory.getImageLoading(IMAGE_LOAD_LIBRARY_TYPE_GLIDE)
                                .loadingNetImage(model, itemView as AppCompatImageView?, itemView.width, itemView.height)
                    }
                }
            }
        }
        adapter0.singleTypeRefresh(list, R.layout.item_imageview, false)
        vpgList0.setDataListSize(list.size)
        vpgList0.setListDataAdapter(adapter0)

        val vpgList1: AvlwCustomViewPager = findViewById(R.id.vpgList1)
        val adapter1: AcbflwBaseRecyclerAdapter<String?> = object : AcbflwBaseRecyclerAdapter<String?>(this, true) {
            override fun getListViewHolder(viewType: Int, itemView: View): AcbflwBaseRecyclerViewHolder<String?>? {
                return object : AcbflwBaseRecyclerViewHolder<String?>(itemView) {
                    override fun setViewData(activity: Activity?, model: String?, position: Int) {
                        AtlwImageLoadingFactory.getImageLoading(IMAGE_LOAD_LIBRARY_TYPE_GLIDE)
                                .loadingNetImage(model, itemView as AppCompatImageView, itemView.getWidth(), itemView.getHeight())
                    }
                }
            }
        }
        adapter1.singleTypeRefresh(list, R.layout.item_imageview, false)
        vpgList1.setAutoplayTime(5000L)
        vpgList1.setDataListSize(list.size)
        vpgList1.setListDataAdapter(adapter1)

        val vpgList2: AvlwCustomViewPager = findViewById(R.id.vpgList2)
        val adapter2: AcbflwBaseRecyclerAdapter<String?> = object : AcbflwBaseRecyclerAdapter<String?>(this, true) {
            override fun getListViewHolder(viewType: Int, itemView: View): AcbflwBaseRecyclerViewHolder<String?>? {
                return object : AcbflwBaseRecyclerViewHolder<String?>(itemView) {
                    override fun setViewData(activity: Activity?, model: String?, position: Int) {
                        AtlwImageLoadingFactory.getImageLoading(IMAGE_LOAD_LIBRARY_TYPE_GLIDE)
                                .loadingNetImage(model, itemView as AppCompatImageView, itemView.getWidth(), itemView.getHeight())
                    }
                }
            }
        }
        adapter2.singleTypeRefresh(list, R.layout.item_imageview, false)
        vpgList2.setListDataAdapter(adapter2)
        vpgList2.setDataListSize(list.size)
        vpgList2.setAutoplayTime(1000L)

        val vpgList3: AvlwCustomViewPager = findViewById(R.id.vpgList3)
        val adapter3: AcbflwBaseRecyclerAdapter<String?> = object : AcbflwBaseRecyclerAdapter<String?>(this) {
            override fun getListViewHolder(viewType: Int, itemView: View): AcbflwBaseRecyclerViewHolder<String?>? {
                return object : AcbflwBaseRecyclerViewHolder<String?>(itemView) {
                    override fun setViewData(activity: Activity?, model: String?, position: Int) {
                        AtlwImageLoadingFactory.getImageLoading(IMAGE_LOAD_LIBRARY_TYPE_GLIDE)
                                .loadingNetImage(model, itemView as AppCompatImageView, itemView.getWidth(), itemView.getHeight())
                    }
                }
            }
        }
        adapter3.singleTypeRefresh(list, R.layout.item_imageview, false)
        vpgList3.setListDataAdapter(adapter3)
        vpgList3.setDataListSize(list.size)
    }
}
