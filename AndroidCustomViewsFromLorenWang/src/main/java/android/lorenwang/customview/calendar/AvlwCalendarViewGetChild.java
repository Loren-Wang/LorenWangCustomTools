package android.lorenwang.customview.calendar;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 功能作用：日历子布局内容获取
 * 创建时间：2021-02-08 8:29 下午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public interface AvlwCalendarViewGetChild {
    /**
     * 获取周标题视图
     *
     * @param weekDay 周几，0代表周日，1代表周一，依次类推
     * @return 标题视图
     */
    View getWeekTitleView(int weekDay);

    /**
     * 获取周标题视图viw的高度
     *
     * @return 高度
     */
    int getWeekTitleViewHeight();

    /**
     * 获取周中每天的view
     */
    View getWeekDayView();

    /**
     * 获取周中每天viw的高度
     *
     * @return 高度
     */
    int getWeekDayViewHeight();

    /**
     * 设置天view
     *
     * @param holder view载体
     * @param time   当前天零时
     * @param select 是否已选择
     */
    void setWeekDayView(RecyclerView.ViewHolder holder, Long time, boolean select);
}
