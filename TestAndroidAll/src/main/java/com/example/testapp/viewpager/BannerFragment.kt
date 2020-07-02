package com.example.testapp.viewpager

import android.app.Activity
import android.lorenwang.common_base_frame.adapter.AcbflwBaseRecyclerAdapter
import android.lorenwang.common_base_frame.adapter.AcbflwBaseRecyclerViewHolder
import android.lorenwang.customview.viewpager.banner.AvlwBannerView
import android.lorenwang.tools.AtlwSetting
import android.lorenwang.tools.image.loading.AtlwImageLoadingFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import com.example.testapp.R
import kotlinx.android.synthetic.main.fragment_banner.*
import java.util.ArrayList

/**
 * 功能作用：轮播图fragment
 * 创建时间：2020-07-02 1:44 下午
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren wang）
 */
class BannerFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_banner, null)
        val bvBanner = view.findViewById<AvlwBannerView>(R.id.bvBanner)
        val list: MutableList<String> = ArrayList()
        list.add("http://img3.imgtn.bdimg.com/it/u=2464547320,3316604757&fm=26&gp=0.jpg")
        list.add("http://tu1.whhost.net/uploads/20181229/10/1546051661-fQRszAGlmj.jpg")
        list.add("http://youimg1.c-ctrip.com/target/tg/706/427/971/f30f03169a5a4152bf009722b3387f2f.jpg")
        list.add("http://img.xshuma.com/201210/21452012100576182.jpg")
        list.add("http://l.paipaitxt.com/118851/10/06/30/104_10897237_4ee2ef93d633782.jpg")

        val adapter: AcbflwBaseRecyclerAdapter<String?> = object :
                AcbflwBaseRecyclerAdapter<String?>(activity!!) {
            override fun getListViewHolder(viewType: Int, itemView: View): AcbflwBaseRecyclerViewHolder<String?>? {
                return object : AcbflwBaseRecyclerViewHolder<String?>(itemView) {
                    override fun setViewData(activity: Activity?, model: String?, position: Int) {
                        AtlwImageLoadingFactory.getImageLoading(AtlwSetting.IMAGE_LOAD_LIBRARY_TYPE_GLIDE)
                                .loadingNetImage(model, itemView as AppCompatImageView, itemView.getWidth(), itemView.getHeight())
                    }
                }
            }

            override fun getItemCount(): Int {
                return bvBanner.adapterItemCount
            }
        }
        adapter.singleTypeRefresh(list, R.layout.item_imageview, false)
        bvBanner.setViewData(list.size, adapter)

        return view
    }
}
