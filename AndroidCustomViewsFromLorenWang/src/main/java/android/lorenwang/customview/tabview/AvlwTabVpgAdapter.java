package android.lorenwang.customview.tabview;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 功能作用：tab列表中的viewpager适配器
 * 创建时间：2020-01-17 17:36
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public abstract class AvlwTabVpgAdapter<T> {
    private AppCompatActivity activity;

    public AvlwTabVpgAdapter(AppCompatActivity activity) {
        this.activity = activity;
    }

    /**
     * 获取item数量
     *
     * @return item数量
     */
    public abstract int getItemCount();

    /**
     * 获取item实例
     *
     * @param position 位置
     * @return 实例
     */
    public abstract T getItem(int position);

    public AppCompatActivity getActivity() {
        return activity;
    }
}
