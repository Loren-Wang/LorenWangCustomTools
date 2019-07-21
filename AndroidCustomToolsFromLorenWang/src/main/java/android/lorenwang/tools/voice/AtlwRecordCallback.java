package android.lorenwang.tools.voice;

/**
 * 创建时间：2019-07-21 下午 20:42:48
 * 创建人：王亮（Loren wang）
 * 功能作用：录音状态改变回调
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public interface AtlwRecordCallback {
    /**
     * 录音启动状态
     *
     * @param isSuccess 是否启动成功
     */
    void recordStart(boolean isSuccess);

    /**
     * 录音停止
     *
     * @param isSuccess 是否停止成功
     */
    void recordStop(boolean isSuccess);

    /**
     * 录音取消状态
     *
     * @param isSuccess         是否取消成功
     * @param nowRecordSavePath
     */
    void recordCancel(boolean isSuccess, String nowRecordSavePath);
}
