package com.example.testapp.viewpager

import android.lorenwang.customview.viewpager.AvlwViewPagerConstraintLayout
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.testapp.BaseActivity
import com.example.testapp.R

class ViewPager2Activity : BaseActivity() {

    override fun onChildCreate(savedInstanceState: Bundle?) {
        addChildView(R.layout.activity_view_pager2)

        val viewPager: AvlwViewPagerConstraintLayout = findViewById(R.id.vpgConstraint)
        viewPager.setFirstViewPage(findViewById(R.id.vpgList))
        findViewById<ViewPager2>(R.id.vpgList).adapter = object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
            override fun getItemCount(): Int {
                return 4
            }

            override fun createFragment(p0: Int): Fragment {
                return ViewPager2Fragment(viewPager, p0)
            }
        }
    }
}