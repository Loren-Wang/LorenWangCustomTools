package android.lorenwang.tools.image.loading;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.lorenwang.tools.AtlwConfig;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 功能作用：glide图片加载类
 * 创建时间：2019-12-19 16:36
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

class AtlwGlideImageLoading extends AtlwBaseImageLoading {

    /**
     * 加载图片
     *
     * @param pathOrRes 地址或者资源
     * @param imageView 图片控件
     * @param config    配置
     */
    private void loadGildeImage(Object pathOrRes, final ImageView imageView, @NotNull final AtlwImageLoadConfig config) {
        RequestManager requestManager = Glide.with(AtlwConfig.nowApplication);
        if (!config.isLoadGetBitmap() && imageView != null) {
            RequestBuilder<Drawable> builder = requestManager.load(pathOrRes);
            getBuild(imageView, builder, config).into(imageView);
        } else {
            getBuild(imageView, requestManager.asBitmap().load(pathOrRes), config).into(new Target<Bitmap>() {
                private Request request;

                @Override
                public void onStart() {

                }

                @Override
                public void onStop() {

                }

                @Override
                public void onDestroy() {

                }

                @Override
                public void onLoadStarted(@Nullable Drawable placeholder) {

                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    if (config.getLoadCallback() != null) {
                        config.getLoadCallback().onFailure();
                    }
                }

                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    resultBitmap(resource, config);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }

                @Override
                public void getSize(@NonNull SizeReadyCallback cb) {

                }

                @Override
                public void removeCallback(@NonNull SizeReadyCallback cb) {

                }

                @Override
                public void setRequest(@Nullable Request request) {
                    this.request = request;
                }

                @Nullable
                @Override
                public Request getRequest() {
                    return request;
                }
            });
        }
    }

    /**
     * 获取构造器
     *
     * @param builder 构造器
     * @param config  配置文件
     * @return 构造器
     */
    private <T> RequestBuilder<T> getBuild(ImageView imageView, RequestBuilder<T> builder, @NotNull AtlwImageLoadConfig config) {
        if (config.isScaleTypeFitCenter()) {
            builder = builder.fitCenter();
        }
        if (config.isScaleTypeCenterGroup()) {
            builder = builder.centerCrop();
        }
        if (config.isScaleTypeCenterInside()) {
            builder = builder.centerInside();
        }
        if (config.getBitmapTransformations() != null && config.getBitmapTransformations().length > 0) {
            builder = builder.transform(config.getBitmapTransformations());
        }
        //是否显示缩略图
        if (isShowThumbnail(imageView, config.getShowViewWidth() > 0 ? config.getShowViewWidth() : null,
                config.getShowViewHeight() > 0 ? config.getShowViewHeight() : null)) {
            builder = builder.thumbnail(0.2f);
        }
        if (config.getShowViewWidth() > 0 && config.getShowViewHeight() > 0) {
            //宽高设置
            builder = builder.override(config.getShowViewWidth(), config.getShowViewHeight());
        }
        //占位图设置
        builder = builder.placeholder(AtlwConfig.imageLoadingLoadResId).error(AtlwConfig.imageLoadingFailResId);

        //高斯模糊处理
        if (config.getBlurRadius() != null && config.getBlurRadius() > 0) {
            int size = config.getBlurIterations() != null ? config.getBlurIterations() : 1;
            for (int i = 0; i < size; i++) {
                builder = builder.transform(new GlideBlurTransformation(config.getBlurRadius()));
            }
        }

        return builder;
    }

    @Override
    public void loadingNetImage(String path, ImageView imageView, @NotNull AtlwImageLoadConfig config) {
        loadGildeImage(path, imageView, config);
    }

    @Override
    public void loadingLocalImage(String path, ImageView imageView, @NotNull AtlwImageLoadConfig config) {
        loadGildeImage(path, imageView, config);

    }

    @Override
    public void loadingResImage(int resId, ImageView imageView, @NotNull AtlwImageLoadConfig config) {
        loadGildeImage(resId, imageView, config);
    }

    @Override
    public void loadingBitmapImage(Bitmap bitmap, ImageView imageView, @NotNull AtlwImageLoadConfig config) {

    }

    @Override
    public void getNetImageBitmap(String path, AtlwImageLoadConfig config) {
        loadGildeImage(path, null, config);
    }

    @Override
    public void clearMemoryCache() {
        Glide.get(AtlwConfig.nowApplication).clearMemory();
    }

    @Override
    public void clearDiskCache() {
        Glide.get(AtlwConfig.nowApplication).clearDiskCache();
    }

    @Override
    public void pauseLoading() {
        Glide.with(AtlwConfig.nowApplication).pauseRequests();
    }

    @Override
    public void resumeLoading() {
        Glide.with(AtlwConfig.nowApplication).resumeRequests();
    }
}
