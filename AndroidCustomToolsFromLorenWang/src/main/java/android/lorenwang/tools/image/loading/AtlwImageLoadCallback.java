package android.lorenwang.tools.image.loading;

import android.graphics.Bitmap;

import java.util.Map;

/**
 * 功能作用：图片加载回调
 * 初始注释时间： 2020/10/21 2:29 下午
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
public abstract class AtlwImageLoadCallback {
    /**
     * 图片加载失败
     */
    public abstract void onFailure();

    /**
     * 图片加载成功
     *
     * @param bitmap 位图，单独获取位图数据时使用，可能为空
     * @param width  加载宽度
     * @param height 加载高度
     */
    public abstract void onSuccess(Bitmap bitmap, int width, int height);

    /**
     * 获取列表位图加载结束
     *
     * @param map 存储列表位图的map
     */
    public void onGetListBitmapFinish(Map<String, Bitmap> map) {}
}
