package com.example.testapp

import android.R.layout.simple_list_item_2
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.adapter.BaseRecyclerAdapter
import com.example.testapp.adapter.holder.SmartViewHolder
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import java.util.*


class SmartRefreshLayoutActivity : BaseActivity() {

    private var mAdapter: BaseRecyclerAdapter<String>? = null

    override fun onChildCreate(savedInstanceState: Bundle?) {
        addChildView(R.layout.activity_smart_refresh_layout)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener(View.OnClickListener { finish() })

        val listView = findViewById<ListView>(R.id.listView)
        mAdapter = object : BaseRecyclerAdapter<String>(simple_list_item_2) {
            override fun onBindViewHolder(holder: SmartViewHolder, model: String, position: Int) {
                holder.text(android.R.id.text1, "sdfasdfsadf")
                holder.text(android.R.id.text2, "sadfsadfasd")
            }
        }
        listView.setAdapter(mAdapter)

        //todo SCROLL_STATE_IDLE
        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
            internal var SCROLL_STATE_IDLE = 0
            internal var SCROLL_STATE_TOUCH_SCROLL = 1
            internal var SCROLL_STATE_FLING = 2
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    println("SCROLL_STATE_IDLE")
                } else if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    println("SCROLL_STATE_TOUCH_SCROLL")
                } else if (scrollState == SCROLL_STATE_FLING) {
                    println("SCROLL_STATE_FLING")
                }
            }

            override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {

            }
        })

        val refreshLayout = findViewById<SmartRefreshLayout>(R.id.refreshLayout)
        refreshLayout.setEnableAutoLoadMore(true)//开启自动加载功能（非必须）
        refreshLayout.setOnRefreshListener(OnRefreshListener { refreshLayout ->
            //                refreshLayout.getLayout().postDelayed(new Runnable() {
            //                    @Override
            //                    public void run() {
            mAdapter?.refresh(initData())
            refreshLayout.finishRefresh()
            refreshLayout.resetNoMoreData()//setNoMoreData(false);
            //                    }
            //                }, 2000);
        })
        refreshLayout.setOnLoadMoreListener(OnLoadMoreListener { refreshLayout ->
            refreshLayout.layout.postDelayed({
                if (mAdapter!!.getItemCount() > 30) {
                    Toast.makeText(application, "数据全部加载完毕", Toast.LENGTH_SHORT).show()
                    refreshLayout.finishLoadMoreWithNoMoreData()//将不会再次触发加载更多事件
                } else {
                    mAdapter?.loadMore(initData())
                    refreshLayout.finishLoadMore()
                }
            }, 2000)
        })

        //触发自动刷新
        refreshLayout.autoRefresh()
        //item 点击测试
        mAdapter?.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            val dialog = BottomSheetDialog(this@SmartRefreshLayoutActivity)
            val dialogView = View.inflate(baseContext, R.layout.activity_smart_refresh_layout, null)
            val refreshLayout = dialogView.findViewById<SmartRefreshLayout>(R.id.refreshLayout)
            val recyclerView = RecyclerView(baseContext)
            recyclerView.layoutManager = LinearLayoutManager(baseContext)
            recyclerView.adapter = mAdapter
            refreshLayout.setEnableRefresh(false)
            refreshLayout.setEnableNestedScroll(false)
            refreshLayout.setRefreshContent(recyclerView)
            dialog.setContentView(dialogView)
            dialog.show()
        })

        //点击测试
        val footer = refreshLayout.getRefreshFooter()
        if (footer is ClassicsFooter) {
            refreshLayout.getRefreshFooter()!!.getView().findViewById<View>(ClassicsFooter.ID_TEXT_TITLE).setOnClickListener(View.OnClickListener { Toast.makeText(baseContext, "点击测试", Toast.LENGTH_SHORT).show() })
        }
    }

    private fun initData(): Collection<String> {
        return Arrays.asList("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")
    }
}
