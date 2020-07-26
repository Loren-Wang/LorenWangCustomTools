package android.lorenwang.tools.image.loading;

import android.graphics.Bitmap;
import android.lorenwang.tools.AtlwConfig;
import android.lorenwang.tools.app.AtlwThreadUtils;
import android.lorenwang.tools.base.AtlwLogUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.UriUtil;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;
import javabase.lorenwang.tools.thread.JtlwTimingTaskUtils;


/**
 * 功能作用：fresco图片加载类
 * 创建时间：2019-12-19 13:57
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

class AtlwFrescoImageLoading extends AtlwBaseImageLoading {

    @Override
    public void loadingNetImage(String path, ImageView imageView, Integer width, Integer height,
                                AtlwImageLoadConfig config) {
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(path)
                && !JtlwCheckVariateUtils.getInstance().isEmpty(imageView)
                && imageView instanceof SimpleDraweeView) {
            ImageRequest imageRequest =
                    getImageRequest(imageView,
                            ImageRequestBuilder.newBuilderWithSource(Uri.parse(path)),
                            width, height, true);
            loadFrecoImage((SimpleDraweeView) imageView, imageRequest, config);
        }
    }

    @Override
    public void loadingLocalImage(String path, ImageView imageView, Integer width, Integer height, AtlwImageLoadConfig config) {
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(path)
                && !JtlwCheckVariateUtils.getInstance().isEmpty(imageView)
                && imageView instanceof SimpleDraweeView) {
            ImageRequest imageRequest = getImageRequest(imageView, ImageRequestBuilder.newBuilderWithSource(new Uri.Builder().scheme(UriUtil.LOCAL_FILE_SCHEME)
                    .path(path).build()), width, height, true);
            loadFrecoImage((SimpleDraweeView) imageView, imageRequest, config);
        }
    }

    @Override
    public void loadingResImage(int resId, ImageView imageView, Integer width, Integer height, AtlwImageLoadConfig config) {
        ImageRequest imageRequest = getImageRequest(imageView, ImageRequestBuilder.newBuilderWithResourceId(resId), width, height, true);
        loadFrecoImage((SimpleDraweeView) imageView, imageRequest, config);
    }

    @Override
    public void loadingBitmapImage(Bitmap bitmap, ImageView imageView, Integer width, Integer height) {
        if (!JtlwCheckVariateUtils.getInstance().isEmpty(bitmap)
                && !JtlwCheckVariateUtils.getInstance().isEmpty(imageView)
                && imageView instanceof SimpleDraweeView) {
            try {
                if (bitmap != null) {
                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(AtlwConfig.nowApplication.getApplicationContext().getContentResolver(), bitmap, null, null));
                    if (uri != null) {
                        ImageRequest imageRequest = getImageRequest(imageView, ImageRequestBuilder.newBuilderWithSource(uri), width, height, true);
                        loadFrecoImage((SimpleDraweeView) imageView, imageRequest, null);
                    }
                }
            } catch (Exception e) {
                AtlwLogUtils.logE(TAG, "加载bitmap失败");
            }
        }
    }

    /**
     * 图片加载
     *
     * @param imageView    图片空缺
     * @param imageRequest 图片请求request
     * @param config       回调
     */
    private void loadFrecoImage(final SimpleDraweeView imageView, ImageRequest imageRequest, final AtlwImageLoadConfig config) {
        if (imageRequest != null) {
            if (config == null || config.getCallback() == null) {
                DraweeController draweeController = getDraweeController(imageRequest, imageView, true, null);
                if (draweeController != null) {

                    GenericDraweeHierarchy draweeHierarchy = getDraweeHierarchy(imageView, AtlwConfig.imageLoadingLoadResId, AtlwConfig.imageLoadingFailResId, config);
                    //设置占位图
                    if (draweeHierarchy != null) {
                        imageView.setHierarchy(draweeHierarchy);
                    }
                    //开始加载
                    imageView.setController(draweeController);
                }
            } else {
                DataSource<CloseableReference<CloseableImage>> dataSource = ImagePipelineFactory.getInstance().getImagePipeline().fetchDecodedImage(imageRequest, null);

                try {
                    dataSource.subscribe(new BaseBitmapDataSubscriber() {
                                             @Override
                                             public void onNewResultImpl(@Nullable final Bitmap bitmap) {
                                                 if (bitmap != null && !bitmap.isRecycled()) {
                                                     JtlwTimingTaskUtils.getInstance().schedule(
                                                             imageView.getId(), new Runnable() {
                                                                 @Override
                                                                 public void run() {
                                                                     final Bitmap resultBitmap =
                                                                             bitmap.copy(bitmap.getConfig(),
                                                                                     bitmap.isMutable());
                                                                     if (config.getCallback() != null && resultBitmap != null && !resultBitmap.isRecycled()) {
                                                                         AtlwThreadUtils.getInstance().postOnUiThread(new Runnable() {
                                                                             @Override
                                                                             public void run() {
                                                                                 config.getCallback().onSuccess(imageView, resultBitmap);
                                                                             }
                                                                         });
                                                                     }
                                                                 }
                                                             }, 0);
                                                 }
                                             }

                                             @Override
                                             public void onCancellation(DataSource<CloseableReference<CloseableImage>> dataSource) {
                                                 super.onCancellation(dataSource);
                                                 if (config.getCallback() != null) {
                                                     config.getCallback().onCancel(imageView);
                                                 }
                                             }

                                             @Override
                                             public void onFailureImpl(DataSource dataSource) {
                                                 Throwable throwable = null;
                                                 if (dataSource != null) {
                                                     throwable = dataSource.getFailureCause();
                                                 }
                                                 if (config.getCallback() != null) {
                                                     config.getCallback().onFailure(imageView, throwable);
                                                 }
                                             }
                                         },
                            UiThreadImmediateExecutorService.getInstance());
                } catch (Exception e) {
                    //oom风险.
                    e.printStackTrace();
                    if (config.getCallback() != null) {
                        config.getCallback().onFailure(imageView, e);
                    }
                }
            }
        }
    }

    /**
     * 清除内存缓存
     */
    @Override
    public void clearMemoryCache() {
        Fresco.getImagePipeline().clearMemoryCaches();
    }

    /**
     * 清除存储缓存
     */
    @Override
    public void clearDiskCache() {
        Fresco.getImagePipeline().clearDiskCaches();
    }

    /**
     * 暂停加载图片
     */
    @Override
    public void pauseLoading() {
        Fresco.getImagePipeline().pause();
        clearMemoryCache();
    }

    /**
     * 恢复加载图片
     */
    @Override
    public void resumeLoading() {
        Fresco.getImagePipeline().resume();
    }

    /**
     * 获取图片请求的request
     *
     *
     * @param imageView
     * @param imageRequestBuilder         图片请求builder
     * @param width                       宽
     * @param height                      高
     * @param isAllowProgressiveRendering 是否允许使用渐进式加载
     * @return 图片请求request
     */
    private ImageRequest getImageRequest(ImageView imageView, ImageRequestBuilder imageRequestBuilder, Integer width, Integer height, boolean isAllowProgressiveRendering) {
        //设置加载的宽高
        if (width != null && height != null) {
            try {
                ResizeOptions resizeOptions = new ResizeOptions(width, height);
                imageRequestBuilder.setResizeOptions(resizeOptions);
            } catch (Exception e) {
                AtlwLogUtils.logE(TAG, "resize失败");
            }
        }

        //判断是否需要显示缩略图
        if (isShowThumbnail(imageView,width, height)) {
            imageRequestBuilder.setLocalThumbnailPreviewsEnabled(true);
        }
        //设置是否渐进式加载
        imageRequestBuilder.setProgressiveRenderingEnabled(isAllowProgressiveRendering);

        return imageRequestBuilder.build();
    }

    /**
     * 获取图片加载控制器
     *
     * @param imageRequest     图片请求构造器
     * @param simpleDraweeView 图片加载控件
     * @param listener         图片处理监听
     * @return 图片加载控制器
     */
    private DraweeController getDraweeController(ImageRequest imageRequest, SimpleDraweeView simpleDraweeView, boolean isUserCache, ControllerListener<ImageInfo> listener) {
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setControllerListener(listener);
        if (isUserCache) {
            builder.setLowResImageRequest(ImageRequest.fromUri(imageRequest.getSourceUri()));
        }
        //加载上一个旧的
        try {
            builder.setOldController(simpleDraweeView.getController());
        } catch (Exception e) {
            AtlwLogUtils.logD(TAG, "图片加载异常");
        }
        return builder.build();
    }

    /**
     * 获取占位图控制器
     *
     * @param loadingResId 加载中资源id
     * @param failResId    失败资源id
     * @param config 配置
     * @return 占位图控制器
     */
    private GenericDraweeHierarchy getDraweeHierarchy(SimpleDraweeView simpleDraweeView, @DrawableRes Integer loadingResId, @DrawableRes Integer failResId, AtlwImageLoadConfig config) {
        if (loadingResId == null && failResId == null) {
            return null;
        } else if (simpleDraweeView != null) {
            GenericDraweeHierarchy hierarchy = simpleDraweeView.getHierarchy();
            if (config != null) {
                if (config.isScaleTypeCenterGroup()) {
                    hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
                }
                if (config.isScaleTypeCenterInside()) {
                    hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
                }
                if (config.isScaleTypeFitCenter()) {
                    hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
                }
                if (config.isScaleTypeFitEnd()) {
                    hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_END);
                }
                if (config.isScaleTypeFitStart()) {
                    hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_START);
                }
                if (config.isScaleTypeFocusCrop()) {
                    hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FOCUS_CROP);
                }
                if (config.isScaleTypeCenter()) {
                    hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER);
                }
                if (config.isScaleTypeFitXy()) {
                    hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
                }
                if (config.isScaleTypeFitBottomStart()) {
                    hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_BOTTOM_START);
                }
            }

            if (hierarchy == null) {
                GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(AtlwConfig.nowApplication.getApplicationContext().getResources());
                if (loadingResId != null) {
                    builder = builder.setPlaceholderImage(loadingResId);
                }
                if (failResId != null) {
                    builder = builder.setFailureImage(failResId);
                }
                return builder.build();
            } else {
                if (loadingResId != null) {
                    hierarchy.setPlaceholderImage(loadingResId);
                }
                if (failResId != null) {
                    hierarchy.setFailureImage(failResId);
                }
                return hierarchy;
            }
        } else {
            return null;
        }
    }

}
