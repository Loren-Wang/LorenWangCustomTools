package android.lorenwang.commonbaseframe.image;

import java.util.List;

/**
 * 功能作用：
 * 创建时间：2021-01-05 12:27 下午
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
public interface AcbflwFileSelectCallback {
    void onResult(List<AcbflwLocalImageSelectBean> result);

    void onCancel();
}
