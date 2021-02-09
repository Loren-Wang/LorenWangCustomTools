package android.lorenwang.customview.calendar;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 功能作用：日历子布局内回调
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
public interface AvlwCalendarViewCallback {
    /**
     * 选择改变
     *
     * @param selectTimeOne 第一个时间
     * @param selectTimeTwo 第二个时间
     */
    void selectChange(Long selectTimeOne, Long selectTimeTwo);
}
