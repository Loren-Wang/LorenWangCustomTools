package android.lorenwang.commonbaseframe.image.loading;

import android.graphics.Bitmap;
import android.lorenwang.tools.AtlwConfig;
import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.view.DraweeView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import org.jetbrains.annotations.NotNull;

/**
 * 功能作用：ImageLoad框架加载
 * 创建时间：2020-10-21 7:06 下午
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
class AcbflwImageLoadImageLoading extends AcbflwBaseImageLoading {

    /**
     * 加载图片
     *
     * @param path      地址信息
     * @param imageView 图片控件
     * @param config    配置信息
     */
    private void loadImage(String path, ImageView imageView, @NotNull AcbflwImageLoadConfig config) {
        if (path != null && imageView != null) {
            ImageSize mImageSize = null;
            if (config.getResizeLoadWidth() > 0 && config.getResizeLoadHeight() > 0) {
                mImageSize = new ImageSize(config.getResizeLoadWidth(),
                        config.getResizeLoadHeight());
            }
            ImageLoader.getInstance().loadImage(path, mImageSize,
                    getDisplayImageOptions(config), getImageLoadingListener(config, imageView));
        }
    }

    /**
     * 获取加载监听
     *
     * @param config    配置信息
     * @param imageView 图片控件
     * @return 加载监听
     */
    private ImageLoadingListener getImageLoadingListener(@NotNull final AcbflwImageLoadConfig config,
                                                         final ImageView imageView) {
        return new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (config.getLoadCallback() != null) {
                    config.getLoadCallback().onFailure();
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (loadedImage != null && !loadedImage.isRecycled()) {
                    Bitmap resultBitmap = null;
                    if (config.isLoadGetBitmap()) {
                        resultBitmap = loadedImage.copy(loadedImage.getConfig(),
                                loadedImage.isMutable());
                    }
                    imageView.setImageBitmap(loadedImage);

                    //回调
                    if (config.getLoadCallback() != null) {
                        config.getLoadCallback().onSuccess(resultBitmap, loadedImage.getWidth(),
                                loadedImage.getHeight());
                    }

                    //设置控件样式
                    setImageViewLayoutParams(config.isUseActualAspectRatio() && ((DraweeView) imageView).getAspectRatio() == 0,
                            imageView, config.getShowViewWidth(), config.getShowViewHeight(),
                            loadedImage.getWidth(), loadedImage.getHeight());
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if (config.getLoadCallback() != null) {
                    config.getLoadCallback().onFailure();
                }
            }
        };
    }

    /**
     * 或加载的配置
     *
     * @param config 配置信息
     * @return 配置
     */
    private DisplayImageOptions getDisplayImageOptions(@NotNull AcbflwImageLoadConfig config) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.showImageOnLoading(AtlwConfig.imageLoadingLoadResId)
                .showImageForEmptyUri(AtlwConfig.imageLoadingFailResId)
                .showImageOnFail(AtlwConfig.imageLoadingFailResId)
                //在加载前是否重置 view ,默认为false
                .resetViewBeforeLoading(true)
                // 是否启用内存缓存，默认为false
                .cacheInMemory(!config.isDisableMemoryCache())
                // 是否启用磁盘缓存，默认为false
                .cacheOnDisk(!config.isDisableDiskCache())
                // 图片的色彩格式
                .bitmapConfig(Bitmap.Config.ARGB_8888);
        //是否加载原图
        if (config.isUseOriginImage()) {
            builder.imageScaleType(ImageScaleType.NONE);
        }

        return builder.build();
    }

    @Override public void loadingNetImage(String path, ImageView imageView,
            @NotNull AcbflwImageLoadConfig config) {
        loadImage(path, imageView, config);
    }

    @Override public void loadingLocalImage(String path, ImageView imageView,
            @NotNull AcbflwImageLoadConfig config) {

    }

    @Override
    public void loadingResImage(int resId, ImageView imageView, @NotNull AcbflwImageLoadConfig config) {
        loadImage("drawable://" + resId, imageView, config);
    }

    @Override public void loadingBitmapImage(Bitmap bitmap, ImageView imageView,
            @NotNull AcbflwImageLoadConfig config) {

    }

    /**
     * 清除内存缓存
     */
    @Override
    public void clearMemoryCache() {
        ImageLoader.getInstance().clearMemoryCache();
    }

    /**
     * 清除本地缓存
     */
    @Override
    public void clearDiskCache() {
        ImageLoader.getInstance().clearDiskCache();
    }

    /**
     * 暂停加载图片
     */
    @Override
    public void pauseLoading() {
        ImageLoader.getInstance().pause();
    }

    /**
     * 恢复加载图片
     */
    @Override
    public void resumeLoading() {
        ImageLoader.getInstance().resume();
    }
}
