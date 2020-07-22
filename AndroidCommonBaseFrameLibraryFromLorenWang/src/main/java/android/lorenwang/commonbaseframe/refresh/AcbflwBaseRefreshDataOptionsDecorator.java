package android.lorenwang.commonbaseframe.refresh;

/**
 * 功能作用：基础数据列表操作接口
 * 创建时间：2020-01-06 18:30
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public interface AcbflwBaseRefreshDataOptionsDecorator {
    /**
     * 开始刷新
     */
    void startRefreshing();

    /**
     * 开始加载更多
     */
    void startLoadingMore();

}
