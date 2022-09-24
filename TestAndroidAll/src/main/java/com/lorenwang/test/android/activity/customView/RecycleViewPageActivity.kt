package com.lorenwang.test.android.activity.customView

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import kotlinx.android.synthetic.main.activity_recycle_view_page.*

class RecycleViewPageActivity : BaseActivity() {

    override fun setContentViewConfig(resId: Int?)  {
        super.setContentViewConfig(R.layout.activity_recycle_view_page)
        vpgList.adapter = RecycleViewPagerAdapter(this)
    }
    class RecycleViewPagerAdapter(var activity: RecycleViewPageActivity) : RecyclerView.Adapter<RecycleViewPagerHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewPagerHolder {
            return RecycleViewPagerHolder(activity, activity.layoutInflater.inflate(R.layout.item_recycle_viewpager, null))
        }

        override fun getItemCount(): Int {
            return 3
        }

        override fun onBindViewHolder(holder: RecycleViewPagerHolder, position: Int) {
//            val transaction = activity.supportFragmentManager.beginTransaction()
////            transaction.replace(holder.itemView.findViewById<View>(R.id.test).id, TestFragment())
//            transaction.add(R.id.test, TestFragment())
//            transaction.commit()
        }

    }

    class RecycleViewPagerHolder(var activity: RecycleViewPageActivity, itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.layoutParams = ViewGroup.LayoutParams(1080, 500)
//            val transaction = activity.supportFragmentManager.beginTransaction()
//            transaction.replace(itemView.findViewById<View>(R.id.test).id, TestFragment())
//            transaction.commit()
        }
    }
}
