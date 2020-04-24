package javabase.lorenwang.tools.thread;

/**
 * 功能作用：倒计时回调
 * 创建时间：2019-02-22 下午 19:46:34
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public interface CountDownCallback {
    /**
     * 倒计时每次回调时间
     * @param sumTime 总时间
     * @param nowTime 当前倒计时时间
     */
    void countDownTime(long sumTime,long nowTime);
    /**
     * 倒计时结束
     */
    void finish();
}
