package android.lorenwang.tools.image.loading;

import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.lorenwang.tools.AtlwConfig;
import android.lorenwang.tools.file.AtlwFileOptionUtil;
import android.net.Uri;
import android.widget.ImageView;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.UriUtil;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

import androidx.annotation.Nullable;
import javabase.lorenwang.tools.JtlwLogUtil;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtil;
import javabase.lorenwang.tools.thread.JtlwTimingTaskUtil;


/**
 * 功能作用：fresco图片加载类
 * 初始注释时间：2019-12-19 13:57
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

class AtlwFrescoImageLoading extends AtlwBaseImageLoading {


    /**
     * 加载图片
     *
     * @param imageView    图片控件
     * @param imageRequest 请求体
     * @param config       配置信息
     */
    private void loadImage(ImageView imageView, ImageRequest imageRequest, final AtlwImageLoadConfig config) {
        if (imageRequest != null && config != null) {
            if (imageView != null && !config.isLoadGetBitmap()) {
                //生成加载控制器
                DraweeController controller = getController(imageRequest, imageView, config);
                //获取占位图控制器
                GenericDraweeHierarchy hierarchy = getHierarchy(imageView, config);
                //设置占位图
                if (hierarchy != null) {
                    ((DraweeView) imageView).setHierarchy(hierarchy);
                }
                //开始加载
                ((DraweeView) imageView).setController(controller);
            } else {
                DataSource<CloseableReference<CloseableImage>> dataSource = ImagePipelineFactory.getInstance().getImagePipeline().fetchDecodedImage(
                        imageRequest, null);
                try {
                    dataSource.subscribe(new BaseBitmapDataSubscriber() {
                        @Override
                        public void onNewResultImpl(@Nullable final Bitmap bitmap) {
                            if (bitmap != null && !bitmap.isRecycled()) {
                                JtlwTimingTaskUtil.getInstance().schedule(Double.valueOf(Math.random() * 100000).intValue(), new Runnable() {
                                    @Override
                                    public void run() {
                                        resultBitmap(bitmap, config);
                                    }
                                }, 0);
                            }
                        }

                        @Override
                        public void onCancellation(DataSource<CloseableReference<CloseableImage>> dataSource) {
                            super.onCancellation(dataSource);
                        }

                        @Override
                        public void onFailureImpl(DataSource dataSource) {
                            if (config.getLoadCallback() != null) {
                                config.getLoadCallback().onFailure();
                            }
                        }
                    }, UiThreadImmediateExecutorService.getInstance());
                } catch (Exception e) {
                    //oom风险.
                    e.printStackTrace();
                    if (config.getLoadCallback() != null) {
                        config.getLoadCallback().onFailure();
                    }
                }
            }
        }
    }

    @Override
    public void loadingNetImage(String path, ImageView imageView, AtlwImageLoadConfig config) {
        if (config != null && !JtlwCheckVariateUtil.getInstance().isEmpty(path) && !JtlwCheckVariateUtil.getInstance().isEmpty(imageView) &&
                imageView instanceof DraweeView) {
            //生成请求
            ImageRequest imageRequest = getImageRequest(imageView, ImageRequestBuilder.newBuilderWithSource(Uri.parse(path)), config);
            if (imageRequest != null) {
                loadImage(imageView, imageRequest, config);
            }
        }
    }

    @Override
    public void loadingLocalImage(String path, ImageView imageView, AtlwImageLoadConfig config) {
        if (config != null) {
            if (JtlwCheckVariateUtil.getInstance().isEmpty(path)) {
                return;
            }
            Uri uri;
            //格式化请求uri
            if (path.contains("content://")) {
                uri = new Uri.Builder().scheme(UriUtil.LOCAL_CONTENT_SCHEME).query(path).authority(
                        AtlwConfig.nowApplication.getPackageName() + ".fileprovider").build();
            } else {
                uri = new Uri.Builder().scheme(UriUtil.LOCAL_FILE_SCHEME).path(path).authority(
                        AtlwConfig.nowApplication.getPackageName() + ".fileprovider").build();
            }
            ImageRequest request = getImageRequest(imageView, ImageRequestBuilder.newBuilderWithSource(uri), config);
            if (request != null) {
                loadImage(imageView, request, config);
            }
        }
    }

    @Override
    public void loadingResImage(int resId, ImageView imageView, AtlwImageLoadConfig config) {
        if (config != null) {
            ImageRequest request = getImageRequest(imageView, ImageRequestBuilder.newBuilderWithResourceId(resId), config);
            if (request != null) {
                loadImage(imageView, request, config);
            }
        }
    }

    @Override
    public void loadingBitmapImage(Bitmap bitmap, ImageView imageView, AtlwImageLoadConfig config) {
        if (config != null && !JtlwCheckVariateUtil.getInstance().isEmpty(bitmap) && !JtlwCheckVariateUtil.getInstance().isEmpty(imageView) &&
                imageView instanceof SimpleDraweeView) {
            try {
                if (bitmap != null && JtlwCheckVariateUtil.getInstance().isNotEmpty(config.getThumbSavePath())) {
                    if (AtlwFileOptionUtil.getInstance().writeToFile(false, new File(config.getThumbSavePath()), bitmap,
                            Bitmap.CompressFormat.JPEG)) {
                        loadingLocalImage(config.getThumbSavePath(), imageView, config);
                    } else {
                        JtlwLogUtil.logUtils.logE(TAG, "加载bitmap失败");
                    }
                    //
                    //                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(AtlwConfig.nowApplication.getContentResolver(), bitmap, null, null));
                    //                    if (uri != null) {
                    //                        getImageRequest(imageView, ImageRequestBuilder.newBuilderWithSource(
                    //                                Uri.parse("data:mime/type;base64," + AtlwImageCommonUtil.getInstance().imageToBase64String(bitmap))),
                    //                                loadImage(imageView, getImageRequest(imageView, ImageRequestBuilder.newBuilderWithSource(uri), config), config);
                    //                    }
                }
            } catch (Exception e) {
                JtlwLogUtil.logUtils.logE(TAG, "加载bitmap失败");
            }
        }
    }

    @Override
    public void getNetImageBitmap(String path, AtlwImageLoadConfig config) {
        if (JtlwCheckVariateUtil.getInstance().isNotEmpty(path)) {
            //生成请求
            ImageRequest imageRequest = getImageRequest(null, ImageRequestBuilder.newBuilderWithSource(Uri.parse(path)), config);
            if (imageRequest != null) {
                loadImage(null, imageRequest, config);
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
     * @param imageView           图片控件
     * @param imageRequestBuilder 图片请求builder
     * @param config              配置信息
     * @return 图片请求request
     */
    private ImageRequest getImageRequest(ImageView imageView, ImageRequestBuilder imageRequestBuilder, AtlwImageLoadConfig config) {
        if (config != null) {
            //设置加载的宽高
            if (config.getResizeLoadWidth() > 0 && config.getResizeLoadHeight() > 0) {
                try {
                    ResizeOptions resizeOptions = new ResizeOptions(config.getResizeLoadWidth(), config.getResizeLoadHeight());
                    imageRequestBuilder.setResizeOptions(resizeOptions);
                } catch (Exception e) {
                    JtlwLogUtil.logUtils.logE(TAG, "resize失败");
                }
            }
            //判断是否需要显示缩略图
            if (imageView != null && isShowThumbnail(imageView, config.getShowViewWidth(), config.getShowViewHeight())) {
                imageRequestBuilder.setLocalThumbnailPreviewsEnabled(true);
            }
            //是否关闭存储卡缓存
            if (config.isDisableDiskCache()) {
                imageRequestBuilder.disableDiskCache();
            }
            //是否关闭内存缓存
            if (config.isDisableMemoryCache()) {
                imageRequestBuilder.disableMemoryCache();
            }
            //设置是否渐进式加载
            imageRequestBuilder.setProgressiveRenderingEnabled(config.isAllowProgressiveRendering());
            //判断是否使用高斯模糊
            if (config.getBlurRadius() != null && config.getBlurRadius() > 0) {
                imageRequestBuilder.setPostprocessor(
                        new IterativeBoxBlurPostProcessor(config.getBlurIterations() != null ? config.getBlurIterations() : 1,
                                config.getBlurRadius()));
            }
        }
        return imageRequestBuilder.build();
    }

    /**
     * 获取图片加载控制器
     *
     * @param imageRequest 图片请求构造器
     * @param imageView    图片加载控件
     * @param config       配置文件
     * @return 图片加载控制器
     */
    private DraweeController getController(ImageRequest imageRequest, final ImageView imageView, final AtlwImageLoadConfig config) {
        if (imageRequest != null && imageView != null && config != null) {
            PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder().setImageRequest(imageRequest)
                    //设置是否加载动图，默认加载动态
                    .setAutoPlayAnimations(config.isGifPlay());
            if (config.isUseCacheLoadImage()) {
                builder.setLowResImageRequest(ImageRequest.fromUri(imageRequest.getSourceUri()));
            }
            if (imageView instanceof DraweeView) {
                //加载上一个旧的
                try {
                    builder.setOldController(((DraweeView) imageView).getController());
                } catch (Exception e) {
                    JtlwLogUtil.logUtils.logE(TAG, "图片加载异常");
                }

                builder.setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);

                        if (config.getLoadCallback() != null) {
                            config.getLoadCallback().onSuccess(null, imageInfo.getWidth(), imageInfo.getHeight());
                        }
                        //设置控件样式
                        setImageViewLayoutParams(config.isUseActualAspectRatio() && ((DraweeView) imageView).getAspectRatio() == 0, imageView,
                                config.getShowViewWidth(), config.getShowViewHeight(), imageInfo.getWidth(), imageInfo.getHeight());

                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {
                        super.onFailure(id, throwable);
                        if (config.getLoadCallback() != null) {
                            config.getLoadCallback().onFailure();
                        }
                    }
                });
            }
            return builder.build();
        } else {
            return null;
        }
    }

    /**
     * 获取占位图控制器
     *
     * @param config 配置信息
     * @return 占位图控制器
     */
    private GenericDraweeHierarchy getHierarchy(ImageView imageView, AtlwImageLoadConfig config) {
        if (config != null) {
            if (imageView instanceof SimpleDraweeView) {
                GenericDraweeHierarchy hierarchy = ((SimpleDraweeView) imageView).getHierarchy();
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
                if (hierarchy == null) {
                    GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(AtlwConfig.nowApplication.getResources());
                    if (config.getImageLoadingLoadResId() != null) {
                        builder = builder.setPlaceholderImage(config.getImageLoadingLoadResId());
                    }
                    if (config.getImageLoadingFailResId() != null) {
                        builder = builder.setFailureImage(config.getImageLoadingFailResId());
                    }
                    return builder.build();
                } else {
                    if (config.getImageLoadingFailResId() != null) {
                        hierarchy.setFailureImage(config.getImageLoadingFailResId());
                    }
                    if (config.getImageLoadingLoadResId() != null && !hierarchy.hasPlaceholderImage()) {
                        hierarchy.setPlaceholderImage(config.getImageLoadingLoadResId());
                    }
                    return hierarchy;
                }
            } else if (imageView instanceof DraweeView) {
                GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(AtlwConfig.nowApplication.getResources());
                if (config.getImageLoadingLoadResId() != null) {
                    builder = builder.setPlaceholderImage(config.getImageLoadingLoadResId());
                }
                if (config.getImageLoadingFailResId() != null) {
                    builder = builder.setFailureImage(config.getImageLoadingFailResId());
                }
                return builder.build();
            }
        }
        return null;
    }

}
