package android.lorenwang.tools.voice;

/**
 * 创建时间：2019-07-21 下午 20:47:56
 * 创建人：王亮（Loren wang）
 * 功能作用：音频播放改变回调
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public interface AtlwMediaPlayCallback {

    /**
     * 回调播放状态
     *
     * @param isSuccess 是否成功
     */
    void playStart(boolean isSuccess);

    /**
     * 播放结束
     */
    void playEnd();

    /**
     * 播放停止
     *
     * @param isSuccess   停止是否成功
     * @param nowPlayPath 当前被停止的播放地址
     */
    void playStop(boolean isSuccess, String nowPlayPath);
}
