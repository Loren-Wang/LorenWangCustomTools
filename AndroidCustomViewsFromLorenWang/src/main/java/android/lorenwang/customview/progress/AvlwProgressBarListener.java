package android.lorenwang.customview.progress;

import androidx.annotation.FloatRange;

/**
 * 功能作用：进度条监听
 * 创建时间：2020-09-30 10:13 上午
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
public interface AvlwProgressBarListener {
    /**
     * 进度改变
     *
     * @param progress      改变后进度
     * @param onTouchChange 是否是触摸改变的进度
     */
    void progressChanage(@FloatRange(from = 0, to = 1) float progress, boolean onTouchChange);
}
