package android.lorenwang.customview.texiview.recordChat;

/**
 * 创建时间：2019-07-21 下午 22:17:20
 * 创建人：王亮（Loren wang）
 * 功能作用：录音按钮录音监听
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public interface AvlwRecordListener {
    /**
     * 取消录音
     */
    void cancelRecord();

    /**
     * 取消录音提示隐藏
     */
    void cancelRecordHintHide();

    /**
     * 取消录音的提示显示
     */
    void cancelRecordHintShow();

    /**
     * 当前录音时间
     *
     * @param nowRecordTime 录音详细时间
     */
    void nowRecordTime(long nowRecordTime);

    /**
     * 录音时间过短
     */
    void recordTimeShort();

    /**
     * 开始录音
     */
    void startRecord();

    /**
     * 停止录音
     */
    void stopRecord();

}
