package com.example.testapp.activity.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.testapp.base.BaseActivity
import com.example.testapp.R
import kotlinx.android.synthetic.main.activity_fragment_and_banner.*

class FragmentAndBannerActivity : BaseActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        addContentView(R.layout.activity_fragment_and_banner)
        vpgList.adapter = object : FragmentStatePagerAdapter(supportFragmentManager) {
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
