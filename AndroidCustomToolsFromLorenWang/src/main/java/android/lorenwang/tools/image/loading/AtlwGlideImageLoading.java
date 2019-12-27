package android.lorenwang.tools.image.loading;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.lorenwang.tools.AtlwSetting;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

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

    @Override
    public void loadingNetImage(String path, ImageView imageView, int width, int height, AtlwImageLoadConfig config) {
        loadGildeImage(path, imageView, width, height, config);
    }

    @Override
    public void loadingLocalImage(String path, ImageView imageView, int width, int height, AtlwImageLoadConfig config) {
        loadGildeImage(path, imageView, width, height, config);
    }

    @Override
    public void loadingResImage(int resId, ImageView imageView, int width, int height, AtlwImageLoadConfig config) {
        loadGildeImage(resId, imageView, width, height, config);
    }

    @Override
    public void loadingBitmapImage(Bitmap bitmap, ImageView imageView, int width, int height) {

    }

    /**
     * 加载图片
     *
     * @param pathOrRes 地址或者资源
     * @param imageView 图片控件
     * @param width     宽度
     * @param height    高度
     * @param config    配置
     */
    private void loadGildeImage(Object pathOrRes, final ImageView imageView, int width, int height, final AtlwImageLoadConfig config) {
        RequestManager requestManager = Glide.with(AtlwSetting.nowApplication.getApplicationContext());
        if (config == null || config.getCallback() == null) {
            RequestBuilder<Drawable> builder = requestManager.load(pathOrRes);
            getBuild(builder, width, height, config).into(imageView);
//            getBuild(builder, width, height, config).into(new CustomTarget<Drawable>(width, height) {
//                @Override
//                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                    imageView.setImageDrawable(resource);
//                }
//
//                @Override
//                public void onLoadCleared(@Nullable Drawable placeholder) {
//                    imageView.setImageDrawable(placeholder);
//                }
//            });
        } else {
            getBuild(requestManager.asBitmap().load(pathOrRes), width, height, config)
                    .into(new Target<Bitmap>() {
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
                            if (config.getCallback() != null) {
                                config.getCallback().onFailure(imageView, new Throwable("error"));
                            }
                        }

                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            if (config.getCallback() != null) {
                                config.getCallback().onSuccess(imageView, resource);
                            }
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
    private <T> RequestBuilder<T> getBuild(RequestBuilder<T> builder, int width, int height, AtlwImageLoadConfig config) {
        if (config != null) {
            if (config.isScaleTypeFitCenter()) {
                builder = builder.fitCenter();
            }
            if (config.isScaleTypeCenterGroup()) {
                builder = builder.centerCrop();
            }
            if (config.isScaleTypeCenterInside()) {
                builder = builder.centerInside();
            }
            if (config.getBitmapTransformations() != null
                    && config.getBitmapTransformations().length > 0) {
                builder = builder.transform(config.getBitmapTransformations());
            }
        }
        //是否显示缩略图
        if (isShowThumbnail(width, height)) {
            builder = builder.thumbnail(0.2f);
        }
        //宽高设置
        builder = builder.override(width, height);
        //占位图设置
        builder = builder.placeholder(AtlwSetting.imageLoadingLoadResId)
                .error(AtlwSetting.imageLoadingFailResId);
        return builder;
    }

    @Override
    public void clearMemoryCache() {
        Glide.get(AtlwSetting.nowApplication.getApplicationContext()).clearMemory();
    }

    @Override
    public void clearDiskCache() {
        Glide.get(AtlwSetting.nowApplication.getApplicationContext()).clearDiskCache();
    }

    @Override
    public void pauseLoading() {
        Glide.with(AtlwSetting.nowApplication.getApplicationContext()).pauseRequests();
    }

    @Override
    public void resumeLoading() {
        Glide.with(AtlwSetting.nowApplication.getApplicationContext()).resumeRequests();
    }
}
