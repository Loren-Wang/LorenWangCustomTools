package com.example.testapp.base;

import android.app.Activity;
import android.lorenwang.commonbaseframe.adapter.AcbflwBaseRecyclerViewHolder;
import android.lorenwang.commonbaseframe.adapter.AcbflwBaseType;
import android.lorenwang.commonbaseframe.bean.AcbflwPageShowViewDataBean;
import android.lorenwang.commonbaseframe.list.AcbflwBaseListDataOptions;
import android.lorenwang.commonbaseframe.list.AcbflwBaseListDataOptionsDecorator;
import android.lorenwang.commonbaseframe.refresh.AcbflwBaseRefreshDataOptions;
import android.lorenwang.commonbaseframe.refresh.AcbflwBaseRefreshDataOptionsDecorator;
import android.lorenwang.commonbaseframe.refresh.AcbflwRefreshView;
import android.view.View;

import com.example.testapp.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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

public abstract class BaseListActivity<T> extends BaseActivity implements AcbflwBaseRefreshDataOptionsDecorator,
        AcbflwBaseListDataOptionsDecorator<T> {

    /**
     * 列表数据操作
     */
    private AcbflwBaseListDataOptions<T> listDataOptions;

    /**
     * 刷新数据操作
     */
    private AcbflwBaseRefreshDataOptions refreshDataOptions;

    /**
     * 当前页码
     */
    protected Integer currentPageIndex;

    @Override
    public void setListShowData(@Nullable AcbflwPageShowViewDataBean<T> data, int itemLayoutRes, boolean showScrollEnd) {
        listDataOptions.setListShowData(data, itemLayoutRes, showScrollEnd);
        loadingAllFinish();
    }

    @Override
    public void setListShowData(@Nullable AcbflwPageShowViewDataBean<AcbflwBaseType<T>> data, boolean showScrollEnd) {
        listDataOptions.setListShowData(data, showScrollEnd);
        loadingAllFinish();
    }

    @Override
    public void showContentData() {
        listDataOptions.showContentData();
        loadingAllFinish();
    }

    /**
     * 添加默认视图控件
     *
     * @param headViewLayout 头部布局id
     */
    protected void addDefaultContentView(@LayoutRes Integer headViewLayout) {
        addContentView(R.layout.activity_common_refresh_list, headViewLayout);
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
        refreshDataOptions = new AcbflwBaseRefreshDataOptions(this, this, getRefreshView(), this);
        listDataOptions = new AcbflwBaseListDataOptions<T>(this, this, refreshDataOptions, getRecycleView()) {
            @Override
            public AcbflwBaseRecyclerViewHolder<T> getListViewHolder(int viewType, @NotNull final View itemView) {
                if (viewType == R.layout.empty_data_default) {
                    return new AcbflwBaseRecyclerViewHolder<T>(itemView) {
                        @Override
                        public void setViewData(@Nullable Activity activity, @Nullable T model, int position) {
                            initEmptyView(itemView, R.layout.empty_data_default, null);
                        }
                    };
                }
                return BaseListActivity.this.getListViewHolder(viewType, itemView);
            }
        };
        //如果使用swipe刷新，则禁用qtrefresh刷新，是否开启刷新使用enableRefresh 判断
        if (useSwipeRefresh) {
            refreshDataOptions.setAllowRefresh(false);
            if (getSwipeAcbflwRefresh() != null) {
                getSwipeAcbflwRefresh().setEnabled(enableRefresh);
                getSwipeAcbflwRefresh().setOnRefreshListener(() -> refreshDataOptions.startRefreshing());
            }
        } else {
            if (getSwipeAcbflwRefresh() != null) {
                getSwipeAcbflwRefresh().setEnabled(false);
            }
            refreshDataOptions.setAllowLoadMore(enableRefresh);
        }
        //设置是否加载更多
        refreshDataOptions.setAllowLoadMore(enableLoad);
    }

    /**
     * 获取列表组件
     */
    private RecyclerView getRecycleView() {
        return findViewById(R.id.recyList);
    }

    /**
     * 获取刷新组件
     */
    private AcbflwRefreshView getRefreshView() {
        return findViewById(R.id.rfRefresh);
    }

    @Override
    public void clear() {
        listDataOptions.clear();
        loadingAllFinish();
    }

    @NotNull
    @Override
    public ArrayList<AcbflwBaseType<T>> getAdapterDataList() {
        return listDataOptions.getAdapterDataList();
    }

    @Override
    public void multiTypeLoad(@Nullable List<? extends AcbflwBaseType<T>> list, boolean haveMoreData) {
        listDataOptions.multiTypeLoad(list, haveMoreData);
        loadingAllFinish();
    }

    @Override
    public void multiTypeRefresh(List<? extends AcbflwBaseType<T>> list, boolean haveMoreData) {
        if (list == null || list.isEmpty()) {
            showListDefaultEmptyView(haveMoreData);
        } else {
            listDataOptions.multiTypeRefresh(list, haveMoreData);
        }
        loadingAllFinish();
    }

    @Override
    public void showEmptyView(int layoutId, @Nullable T desc, boolean haveMoreData) {
        listDataOptions.showEmptyView(layoutId, desc, haveMoreData);
        loadingAllFinish();
    }

    @Override
    public void singleTypeLoad(@Nullable List<? extends T> list, int layoutId, boolean haveMoreData) {
        listDataOptions.singleTypeLoad(list, layoutId, haveMoreData);
        loadingAllFinish();
    }

    @Override
    public void singleTypeRefresh(@Nullable List<? extends T> list, int layoutId, boolean haveMoreData) {
        if (list == null || list.isEmpty()) {
            showListDefaultEmptyView(haveMoreData);
        } else {
            listDataOptions.singleTypeRefresh(list, layoutId, haveMoreData);
        }
        loadingAllFinish();
    }

    /**
     * 显示列表中默认的无数据显示
     */
    protected void showListDefaultEmptyView(boolean haveMoreData) {
        this.showEmptyView(R.layout.empty_data_default, null, haveMoreData);
    }

    /**
     * 开始刷新
     */
    @Override
    public void startRefreshing() {

    }

    /**
     * 开始加载更多
     */
    @Override
    public void startLoadingMore() {

    }

    @Override
    public <D> void netReqSuccess(int netOptionReqCode, D data) {
        super.netReqSuccess(netOptionReqCode, data);
        //结束刷新
        loadingAllFinish();
    }

    @Override
    public void netReqFail(int netOptionReqCode, @Nullable String message) {
        super.netReqFail(netOptionReqCode, message);
        //结束刷新
        loadingAllFinish();
    }

    /**
     * 结束所有加载中
     */
    public void loadingAllFinish() {
        //结束刷新
        if (getSwipeAcbflwRefresh() != null) {
            getSwipeAcbflwRefresh().setRefreshing(false);
        }
        refreshDataOptions.finishAll();
    }
}
