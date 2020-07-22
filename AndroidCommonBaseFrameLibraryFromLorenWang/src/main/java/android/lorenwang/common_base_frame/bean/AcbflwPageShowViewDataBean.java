package android.lorenwang.common_base_frame.bean;

import java.util.ArrayList;

/**
 * 功能作用：分页的view实体和数据显示的中间类
 * 创建时间：2020-07-20 8:20 下午
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

public class AcbflwPageShowViewDataBean<T> {
    /**
     * 是否是最后一页
     */
    private final boolean lastPageData;
    /**
     * 是否是第一页数据
     */
    private final boolean firstPageData;
    /**
     * 当前页码
     */
    private final int currentPageIndex;
    /**
     * 当前页面数量
     */
    private final int currentPageCount;
    /**
     * 总数
     */
    private final int total;
    /**
     * 总页面数
     */
    private final int totalPage;
    /**
     * 数据列表
     */
    private ArrayList<T> list;

    public AcbflwPageShowViewDataBean(boolean lastPageData, boolean firstPageData,
                                      int currentPageIndex, int currentPageCount, int total,
                                      int totalPage, ArrayList<T> list) {
        this.lastPageData = lastPageData;
        this.firstPageData = firstPageData;
        this.currentPageIndex = currentPageIndex;
        this.currentPageCount = currentPageCount;
        this.total = total;
        this.totalPage = totalPage;
        this.list = list;
    }

    public boolean isLastPageData() {
        return lastPageData;
    }

    public boolean isFirstPageData() {
        return firstPageData;
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public int getCurrentPageCount() {
        return currentPageCount;
    }

    public int getTotal() {
        return total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public ArrayList<T> getList() {
        return list;
    }
}
