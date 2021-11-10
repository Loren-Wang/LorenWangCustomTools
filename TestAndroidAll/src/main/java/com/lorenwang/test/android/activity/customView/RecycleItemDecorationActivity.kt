package com.lorenwang.test.android.activity.customView

import android.app.Activity
import android.graphics.Color
import android.lorenwang.commonbaseframe.adapter.AcbflwBaseRecyclerAdapter
import android.lorenwang.commonbaseframe.adapter.AcbflwBaseRecyclerViewHolder
import android.lorenwang.customview.recycleview.AvlwCommonGridItemDecoration
import android.lorenwang.customview.recycleview.AvlwCommonHorizontalItemDecoration
import android.lorenwang.customview.recycleview.AvlwCommonVerticalItemDecoration
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity

/**
 * 功能作用：RecycleView分隔线相关
 * 初始注释时间： 2021/11/8 17:51
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
class RecycleItemDecorationActivity : BaseActivity() {
    private val rvList: RecyclerView
        get() = findViewById(R.id.rv_list)

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_custom_view_recycle_item_decoration)
        val adapter = object : AcbflwBaseRecyclerAdapter<String>(this) {
            override fun getListViewHolder(viewType: Int, itemView: View): AcbflwBaseRecyclerViewHolder<String> {
                return object : AcbflwBaseRecyclerViewHolder<String>(itemView) {
                    override fun setViewData(activity: Activity, model: String?, position: Int) {

                    }
                }
            }
        }
        val list = arrayListOf<String>()
        for (index in 0..100) {
            list.add("")
        }
        adapter.singleTypeLoad(list, R.layout.item_list_only_imageview, false)
        rvList.adapter = adapter

    }

    fun mainClick(view: View?) {
        if (view != null) {
            if (rvList.itemDecorationCount == 1) {
                rvList.removeItemDecorationAt(0)
            }
            when (view.id) {
                R.id.btn_grid -> {
                    var count = ((Math.random() * 10) % 5).toInt()
                    if (count <= 0) {
                        count = 3
                    }
                    rvList.layoutManager = GridLayoutManager(this, count)
                    rvList.addItemDecoration(
                        AvlwCommonGridItemDecoration(Color.RED, count, (Math.random() * 100F).toFloat(), (Math.random() * 100F).toFloat(),
                            (Math.random() * 100F).toFloat(), (Math.random() * 100F).toFloat(), (Math.random() * 100F).toFloat(),
                            (Math.random() * 100F).toFloat()))
                }
                R.id.btn_h -> {
                    rvList.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
                    rvList.addItemDecoration(
                        AvlwCommonHorizontalItemDecoration(Color.YELLOW, (Math.random() * 100F).toFloat(), (Math.random() * 100F).toFloat(),
                            (Math.random() * 100F).toFloat()))
                }
                R.id.btn_v -> {
                    rvList.layoutManager = LinearLayoutManager(this)
                    rvList.addItemDecoration(
                        AvlwCommonVerticalItemDecoration(Color.CYAN, (Math.random() * 100F).toFloat(), (Math.random() * 100F).toFloat(),
                            (Math.random() * 100F).toFloat(), (Math.random() * 100F).toFloat(), (Math.random() * 100F).toFloat(),
                            (Math.random() * 100F).toFloat(), (Math.random() * 100F).toFloat(), Color.BLUE, Color.GRAY))
                }
            }
        }
    }
}
