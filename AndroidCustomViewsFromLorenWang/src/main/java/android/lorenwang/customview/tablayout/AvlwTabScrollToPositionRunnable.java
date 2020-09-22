package android.lorenwang.customview.tablayout;

/**
 * 功能作用：tab切换线程执行代码
 * 创建时间：2020-09-22 1:17 下午
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
abstract class AvlwTabScrollToPositionRunnable implements Runnable {
    /**
     * 是否是触摸切换
     */
    protected boolean onTouchChange = false;

    public void setOnTouchChange(boolean onTouchChange) {
        this.onTouchChange = onTouchChange;
    }

    public boolean isOnTouchChange() {
        return onTouchChange;
    }
}
