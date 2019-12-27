package android.lorenwang.tools.image.loading;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * 功能作用：图片加载位图回调
 * 创建时间：2019-12-19 14:49
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 1、位图获取成功---onSuccess（imageView，bitmap）
 * 2、位图获取失败---onFailure（imageView，throwable）
 * 3、取消获取位图---onCancel(imageView)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public interface AtlwImageLoadBitmapCallback {
    /**
     * 位图获取成功
     *
     * @param imageView 图片控件
     * @param bitmap    位图数据
     */
    void onSuccess(ImageView imageView, Bitmap bitmap);

    /**
     * 位图获取失败
     *
     * @param imageView 图片控件
     * @param throwable 异常信息
     */
    void onFailure(ImageView imageView, Throwable throwable);

    /**
     * 取消获取位图
     *
     * @param imageView 图片控件
     */
    void onCancel(ImageView imageView);
}
