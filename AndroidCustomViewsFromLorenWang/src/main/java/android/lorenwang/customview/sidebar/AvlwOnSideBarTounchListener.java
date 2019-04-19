package android.lorenwang.customview.sidebar;

/**
 * 创建时间：2019-04-19 下午 13:52:46
 * 创建人：王亮（Loren wang）
 * 功能作用：sidebar触摸监听
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public interface AvlwOnSideBarTounchListener {
    /**
     * 根据列表返回触摸位置
     * @param posi 触摸位置
     * @param str 触摸位置所在字符串
     * @param isDowm 是否是按下触发
     * @param isMove 是否是移动触发
     * @param isUp 是否是手指抬起触发
     */
    void onTouchPosiForList(int posi,String str,boolean isDowm,boolean isMove,boolean isUp);
}
