package com.lorenwang.test.android.activity.customView.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity

class FragmentAndBannerActivity : BaseActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_custom_view_fragment_and_banner)
        findViewById<ViewPager>(R.id.vpgList).adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
            /**
             * Return the Fragment associated with a specified position.
             */
            override fun getItem(position: Int): Fragment {
                return BannerFragment()
            }

            /**
             * Return the number of views available.
             */
            override fun getCount(): Int {
                return 4
            }

        }
    }
}
