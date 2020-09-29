package android.lorenwang.customview.progress;

import androidx.annotation.FloatRange;

/**
 * 功能作用：进度条操作的函数接口
 * 创建时间：2020-09-29 4:16 下午
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
interface AvlwProgressBarOptions {
    /**
     * 设置进度
     *
     * @param progress 进度
     */
    void setProgress(@FloatRange(from = 0, to = 0) float progress);
}
