package com.lorenwang.test.android.activity.customView.viewpager

import android.graphics.Color
import android.lorenwang.customview.viewpager.AvlwViewPagerConstraintLayout
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.lorenwang.test.android.base.BaseFragment
import com.lorenwang.test.android.R

/**
 * 功能作用：ViewPager2Fragment
 * 创建时间：2020-03-05 12:08
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class ViewPager2Fragment(var viewPager: AvlwViewPagerConstraintLayout, var posi: Int) : BaseFragment() {

    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.fragment_view_pager2)
//        viewPager.setSecondViewPageMap(posi, fragmentView!!.findViewById(R.id.vpgList))
//        fragmentView?.findViewById<View>(R.id.vpgList)?.setBackgroundColor(Color.GRAY)
//        (fragmentView?.findViewById<View>(R.id.vpgList) as ViewPager2).adapter = object : FragmentStateAdapter(this) {
//            override fun createFragment(i: Int): Fragment {
//                return com.lorenwang.test.android.activity.customView.viewpager.ViewPager2ShowFragment(i)
//            }
//
//            override fun getItemCount(): Int {
//                return 5
//            }
//        }
//        val frameLayout = fragmentView!!.findViewById<View>(R.id.test) as FrameLayout
//        frameLayout.setPadding(90, 90, 90, 300)
    }
}
