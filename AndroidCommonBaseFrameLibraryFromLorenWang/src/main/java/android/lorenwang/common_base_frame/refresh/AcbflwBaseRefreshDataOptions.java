package android.lorenwang.common_base_frame.refresh;

import android.content.Context;
import android.lorenwang.common_base_frame.mvp.AcbflwBaseView;

import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import androidx.annotation.NonNull;

/**
 * 功能作用：基础列表数据类
 * 创建时间：2020-01-06 18:31
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AcbflwBaseRefreshDataOptions implements AcbflwBaseRefreshDataOptionsDecorator {
    private final String TAG = getClass().getName();
    private AcbflwRefreshView refreshLayout;
    /**
     * 刷新头
     */
    RefreshHeader refreshHeader;
    /**
     * 刷新底
     */
    RefreshFooter refreshFooter;

    private AcbflwBaseRefreshDataOptionsDecorator qtBaseRefreshDataOptionsDecorator;

    public AcbflwBaseRefreshDataOptions(Context context, AcbflwBaseView baseView, AcbflwRefreshView refreshLayout,
                                        AcbflwBaseRefreshDataOptionsDecorator qtBaseRefreshDataOptionsDecorator) {
        this.qtBaseRefreshDataOptionsDecorator = qtBaseRefreshDataOptionsDecorator;
        if (refreshHeader != null) {
            refreshLayout.setRefreshHeader(refreshHeader);
        }
        if (refreshFooter != null) {
            refreshLayout.setRefreshFooter(refreshFooter);
        }
        //头部高度
        refreshLayout.setHeaderHeight(60);
        //底部高度
        refreshLayout.setFooterHeight(40);
        //设置允许刷新
        refreshLayout.setEnableRefresh(true);
        //设置允许加载更多
        refreshLayout.setEnableLoadMore(true);
        //设置底部越界时是否自动加载，在项目中关闭自动加载更多
        refreshLayout.setEnableAutoLoadMore(false);
        //刷新监听
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                startRefreshing();
            }
        });
        //加载更多监听
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                startLoadingMore();
            }
        });
    }

    /**
     * 设置刷新顶部
     *
     * @param refreshHeader 刷新顶部
     */
    public void setRefreshHeader(RefreshHeader refreshHeader) {
        if (refreshHeader != null) {
            this.refreshHeader = refreshHeader;
            refreshLayout.setRefreshHeader(refreshHeader);
        }
    }

    /**
     * 设置刷新底部
     *
     * @param refreshFooter 刷新底部
     */
    public void setRefreshFooter(RefreshFooter refreshFooter) {
        if (refreshFooter != null) {
            this.refreshFooter = refreshFooter;
            refreshLayout.setRefreshFooter(refreshFooter);
        }
    }


    /**
     * 结束刷新或者结束加载更多
     */
    public void finishAll() {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }
    }

    /**
     * 设置是否允许加载更多
     *
     * @param allow 允许为true
     */
    public void setAllowLoadMore(boolean allow) {
        if (refreshLayout != null) {
            refreshLayout.setEnableLoadMore(allow);
        }
    }

    /**
     * 设置是否允许加载更多
     *
     * @param allow 允许为true
     */
    public void setAllowRefresh(boolean allow) {
        if (refreshLayout != null) {
            refreshLayout.setEnableRefresh(allow);
        }
    }

    @Override
    public void startRefreshing() {
        this.qtBaseRefreshDataOptionsDecorator.startRefreshing();
    }

    @Override
    public void startLoadingMore() {
        this.qtBaseRefreshDataOptionsDecorator.startLoadingMore();
    }
}
