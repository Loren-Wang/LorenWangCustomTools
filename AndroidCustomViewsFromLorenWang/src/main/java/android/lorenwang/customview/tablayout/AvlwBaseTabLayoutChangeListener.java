package android.lorenwang.customview.tablayout;

import androidx.annotation.FloatRange;

/**
 * 创建时间：2019-04-14 上午 00:00:26
 * 创建人：王亮（Loren wang）
 * 功能作用：滑动监听
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

/**
 * 功能作用：滑动监听
 * 初始注释时间： 2020/9/22 1:19 下午
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
public interface AvlwBaseTabLayoutChangeListener {
    /**
     * 位置切换
     *
     * @param isOnTouchChange 是否是触摸切换的位置
     * @param text            显示文本
     * @param position        切换后的位置
     */
    void onChangePosition(boolean isOnTouchChange, String text, int position);

    /**
     * 切换进度
     *
     * @param isOnTouchChange 是否是触摸切换的位置
     * @param percent         进度
     */
    void onChangePercent(boolean isOnTouchChange, @FloatRange(from = 0, to = 1) float percent);
}
