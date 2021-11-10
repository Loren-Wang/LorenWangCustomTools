package com.lorenwang.test.android.activity.customToolsAndroid.image

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.lorenwang.commonbaseframe.adapter.AcbflwBaseRecyclerViewHolder
import android.lorenwang.commonbaseframe.image.AcbflwImageSelectUtil
import android.lorenwang.tools.AtlwConfig
import android.lorenwang.tools.app.AtlwViewUtil
import android.lorenwang.tools.image.AtlwImageCommonUtil
import android.lorenwang.tools.image.loading.AtlwImageLoadCallback
import android.lorenwang.tools.image.loading.AtlwImageLoadConfig
import android.lorenwang.tools.image.loading.AtlwImageLoadingFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.lorenwang.test.android.R
import kotlinbase.lorenwang.tools.extend.kttlwIsNotNullOrEmpty
import kotlinbase.lorenwang.tools.extend.kttlwThrottleClick

/**
 * 功能作用：图片列表位图加载页面
 * 初始注释时间： 2021/1/19 5:34 下午
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
class ImageListBitmapActivity : com.lorenwang.test.android.base.BaseListActivity<Bitmap?>() {
    override fun initView(savedInstanceState: Bundle?) {
        addDefaultContentView(null)
        initBaseList(false, false, false)


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

        AtlwImageLoadingFactory.getImageLoading(AtlwConfig.IMAGE_LOAD_LIBRARY_TYPE_FRESCO)
            .getNetImageBitmap(list, AtlwImageLoadConfig.Build().setLoadCallback(object : AtlwImageLoadCallback() {
                override fun onFailure() {

                }

                override fun onSuccess(bitmap: Bitmap?, width: Int, height: Int) {
                }

                override fun onGetListBitmapFinish(map: MutableMap<String, Bitmap?>?) {
                    val list = arrayListOf<Bitmap>()
                    map?.values?.forEach {
                        if (it != null && !it.isRecycled) {
                            list.add(AtlwImageCommonUtil.getInstance()
                                .addWatermarkBitmap(it, 12, Color.argb((255 * 0.6F).toInt(), 0, 255, 0), "仅供身份认证使用", it.width, it.height, 45))
                        }
                    }
                    singleTypeLoad(list, R.layout.item_list_only_imageview, false)


//                    singleTypeRefresh(map?.values?.toList(), R.layout.item_list_only_imageview, false)
                }
            }).build())

        val path = "/storage/emulated/0/DCIM/Screenshots/Screenshot_2021-02-17-10-14-26-087_com.tencent.mm.jpg"
        if (path.kttlwIsNotNullOrEmpty()) {
            val bitmap = BitmapFactory.decodeFile(path)
            if (bitmap != null) {
                val watermarkBitmap = AtlwImageCommonUtil.getInstance()
                    .addWatermarkBitmap(bitmap.copy(bitmap.config, true), 12, Color.argb((255 * 0.35).toInt(), 0, 0, 255), "剩的饭发撒剪短发", bitmap.width,
                        bitmap.height, 45)
                singleTypeLoad(arrayListOf(watermarkBitmap), R.layout.item_list_only_imageview, false)
            }
        }
    }

    override fun getListViewHolder(viewType: Int, itemView: View): AcbflwBaseRecyclerViewHolder<Bitmap?> {
        return object : AcbflwBaseRecyclerViewHolder<Bitmap?>(itemView) {
            override fun setViewData(activity: Activity, model: Bitmap?, position: Int) {
                model?.let { bottom ->
                    itemView.findViewById<AppCompatImageView>(R.id.imgPic).apply {
                        setImageBitmap(bottom)
                        AtlwViewUtil.getInstance().setViewWidthHeight(this, bottom.width, bottom.height)
                    }
                    itemView.kttlwThrottleClick {
                        AcbflwImageSelectUtil.getInstance().openSelectImage(this@ImageListBitmapActivity, 1, null, 1)
                    }

//                    BitmapFactory.decodeResource(resources,R.drawable.icon_empty_add)?.let { top->
//                        AtlwImageCommonUtil.getInstance().getOverlapBitmap(bottom,top,200,null)?.let {
//                            itemView.findViewById<AppCompatImageView>(R.id.imgPic).apply {
//                                setImageBitmap(it)
//                                AtlwViewUtil.getInstance().setViewWidthHeight(this, it.width, it.height)
//                            }
//                        }
//                    }
                }
            }
        }
    }


}
