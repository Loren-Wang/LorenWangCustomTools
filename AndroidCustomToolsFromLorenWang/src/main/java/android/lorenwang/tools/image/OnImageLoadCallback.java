package android.lorenwang.tools.image;

import android.graphics.Bitmap;

/**
 * 创建时间：2019-04-11 下午 20:56:38
 * 创建人：王亮（Loren wang）
 * 功能作用：图片加载回调
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public interface OnImageLoadCallback {
    /**
     * 回调位图
     * @param bitmap 位图数据
     */
    void onBitmap(Bitmap bitmap);
}
