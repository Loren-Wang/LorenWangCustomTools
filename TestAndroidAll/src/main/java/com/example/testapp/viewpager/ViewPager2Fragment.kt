package com.example.testapp.viewpager

import android.annotation.SuppressLint
import android.graphics.Color
import android.lorenwang.customview.viewpager.AvlwViewPagerConstraintLayout
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.testapp.R

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
class ViewPager2Fragment(var viewPager: AvlwViewPagerConstraintLayout, var posi: Int) : Fragment() {

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val inflate: View = inflater.inflate(R.layout.fragment_view_pager2, container, false)
        viewPager.setSecondViewPageMap(posi, inflate.findViewById(R.id.vpgList))
        inflate.findViewById<View>(R.id.vpgList).setBackgroundColor(Color.GRAY)
        (inflate.findViewById<View>(R.id.vpgList) as ViewPager2).adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(i: Int): Fragment {
                return ViewPager2ShowFragment(i)
            }

            override fun getItemCount(): Int {
                return 5
            }
        }
        val frameLayout = inflate.findViewById<View>(R.id.test) as FrameLayout
        frameLayout.setPadding(90, 90, 90, 300)
        return inflate
    }
}
