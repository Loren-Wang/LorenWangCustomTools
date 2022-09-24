package com.lorenwang.test.android.base;

import com.lorenwang.test.android.R;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 功能作用：基础列表Activity
 * 创建时间：2020-07-23 11:37 上午
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

public abstract class BaseListActivity<T> extends BaseActivity {
    /**
     * 当前页码
     */
    protected Integer currentPageIndex;

    /**
     * 添加默认视图控件
     *
     * @param headViewLayout 头部布局id
     */
    protected void addDefaultContentView(@LayoutRes Integer headViewLayout) {
        //        addContentView(R.layout.activity_common_refresh_list, headViewLayout);
    }

    /**
     * 初始化列表基础组件
     *
     * @param useSwipeRefresh 是否使用swipe的刷新样式,如果布局中存在该布局同时id为swipeRefresh的才生效
     * @param enableRefresh   是否开启刷新
     * @param enableLoad      是否开启加载更多
     */
    protected void initBaseList(Boolean useSwipeRefresh, Boolean enableRefresh, Boolean enableLoad) {
        if (useSwipeRefresh == null) {
            useSwipeRefresh = true;
        }
        if (enableRefresh == null) {
            enableRefresh = true;
        }
        if (enableLoad == null) {
            enableLoad = false;
        }
    }

    /**
     * 获取列表组件
     */
    private RecyclerView getRecycleView() {
        return findViewById(R.id.recyList);
    }

}
