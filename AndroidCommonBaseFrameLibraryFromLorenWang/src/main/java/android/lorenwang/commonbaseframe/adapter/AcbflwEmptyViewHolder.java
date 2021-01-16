package android.lorenwang.commonbaseframe.adapter;

import android.app.Activity;
import android.view.View;

import org.jetbrains.annotations.NotNull;

/**
 * 功能作用：
 * 创建时间：2020-10-19 10:42 上午
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
public class AcbflwEmptyViewHolder<T> extends AcbflwBaseRecyclerViewHolder<T> {
    public AcbflwEmptyViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * 设置数据
     *
     * @param activity activity实例
     * @param model    model数据，泛型
     * @param position 位置
     */
    @Override
    public void setViewData(@NotNull Activity activity, T model, int position) {

    }
}
