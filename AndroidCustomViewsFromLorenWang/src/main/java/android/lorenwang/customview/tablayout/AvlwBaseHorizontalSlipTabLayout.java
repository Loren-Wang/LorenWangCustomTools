package android.lorenwang.customview.tablayout;


import java.util.List;

import androidx.annotation.Nullable;

/**
 * 创建时间：2019-05-05 下午 19:38:27
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

interface AvlwBaseHorizontalSlipTabLayout {
    /**
     * 设置tab列表
     */
    void setTabList(@Nullable List<String> tabList, @Nullable Integer selectPosi);

    /**
     * 跳转到指定位置
     */
    void skipToPosi(int posi);

    /**
     * 滑动到指定位置，带百分比滑动
     */
    void slipToPosi(int slipToPosi, float percent);

    /**
     * 滑动跳转到指定位置
     */
    void slipSkipToPosi(int slipToPosi);
}
