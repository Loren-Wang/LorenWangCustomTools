package android.lorenwang.common_base_frame.image;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.lorenwang.common_base_frame.AcbflwBaseApplication;
import android.lorenwang.tools.app.AtlwScreenUtils;
import android.lorenwang.tools.base.AtlwLogUtils;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.UriUtil;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.interfaces.DraweeHierarchy;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;


/**
 * Created by wangliang on 0010/2017/10/10.
 * 创建时间： 0010/2017/10/10 17:01
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */

/**
 * 功能作用：图片加载工具，主要是fresco图片加载
 * 初始注释时间： 2019/12/11 16:28
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class AcbflwImageLoadingUtils {
    private String TAG = getClass().getName();
    private static AcbflwImageLoadingUtils imageLoadingUtils;
    private ExecutorService executeBackgroundTask = Executors.newSingleThreadExecutor();

    private int showThumbnaiMaxWidth;
    private int showThumbnaiMaxHeight;

    private AcbflwImageLoadingUtils() {
        showThumbnaiMaxWidth = (int) (AtlwScreenUtils.getInstance().getScreenWidth(AcbflwBaseApplication.appContext) * 0.1);
        showThumbnaiMaxHeight = (int) (AtlwScreenUtils.getInstance().getScreenHeight(AcbflwBaseApplication.appContext) * 0.1);
    }

    public static AcbflwImageLoadingUtils getInstance() {
        if (imageLoadingUtils == null) {
            synchronized (AcbflwImageLoadingUtils.class) {
                if (imageLoadingUtils == null) {
                    imageLoadingUtils = new AcbflwImageLoadingUtils();
                }
            }
        }
        return imageLoadingUtils;
    }

    /**
     * 图片加载，对于的是不确定是网络图片还是本地图片的情况下使用
     *
     * @param path
     * @param simpleDraweeView
     * @param width
     * @param height
     */
    public void loadingImage(String path, @NonNull DraweeView simpleDraweeView, @NonNull int width, @NonNull int height) {
        if (!TextUtils.isEmpty(path)) {
            if (checkPathIsNetUrl(path)) {
                loadingImage(LOADING_TYPE_FOR_NET, path, simpleDraweeView, true, width, height, null, null, null);
                return;
            }

            if (chePathIsLocalPath(path)) {
                loadingImage(LOADING_TYPE_FOR_LOCAL, path, simpleDraweeView, true, width, height, null, null, null);
                return;
            }
        }
    }

    /**
     * 加载图片
     *
     * @param netUrlPath
     * @param simpleDraweeView
     * @param width
     * @param height
     */
    public void loadingNet(@NonNull String netUrlPath, @NonNull DraweeView simpleDraweeView, @NonNull int width, @NonNull int height) {
        if (checkPathIsNetUrl(netUrlPath))
            loadingImage(LOADING_TYPE_FOR_NET, netUrlPath, simpleDraweeView, true, width, height, null, null, null);
    }

    /**
     * 加载图片
     *
     * @param netUrlPath
     * @param simpleDraweeView
     * @param width
     * @param height
     */
    public void loadingNet(@NonNull String netUrlPath, @NonNull DraweeView simpleDraweeView, @NonNull int width, @NonNull int height, ControllerListener<ImageInfo> listener) {
        if (checkPathIsNetUrl(netUrlPath))
            loadingImage(LOADING_TYPE_FOR_NET, netUrlPath, simpleDraweeView, true, width, height, null, null, listener);
    }

    /**
     * 加载网络图片，为了适应预先不知道图片宽高的情况，当不知道图片宽高的时候使用该方法会设置控件的宽高属性
     *
     * @param simpleDraweeView
     * @param width
     * @param height
     */
    public void loadingImageAndSetWidthHeight(@NonNull String path, @NonNull DraweeView simpleDraweeView, @NonNull int width, @NonNull int height) {
        if (JtlwCheckVariateUtils.getInstance().isEmpty(path) && simpleDraweeView != null) {

            ViewGroup.LayoutParams layoutParams = simpleDraweeView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(width, height);
            } else {
                layoutParams.width = width;
                layoutParams.height = height;
            }
            simpleDraweeView.setLayoutParams(layoutParams);
            loadingImage(path, simpleDraweeView, width, height);
        }
    }


    /**
     * 加载图片
     *
     * @param netUrlPath
     * @param simpleDraweeView
     * @param width
     * @param height
     */
    public void loadingLocal(@NonNull String netUrlPath, @NonNull DraweeView simpleDraweeView, @NonNull int width, @NonNull int height) {
        loadingImage(LOADING_TYPE_FOR_LOCAL, netUrlPath, simpleDraweeView, true, width, height, null, null, null);
    }

    /**
     * 加载图片
     *
     * @param netUrlPath
     * @param simpleDraweeView
     * @param width
     * @param height
     */
    public void loadingLocal(@NonNull String netUrlPath, @NonNull DraweeView simpleDraweeView, @NonNull int width, @NonNull int height, ControllerListener<ImageInfo> listener) {
        loadingImage(LOADING_TYPE_FOR_LOCAL, netUrlPath, simpleDraweeView, true, width, height, null, null, listener);
    }

    /**
     * 加载本地资源图片
     *
     * @param resId            资源id
     * @param simpleDraweeView 图片控件
     * @param width            宽
     * @param height           高
     */
    public void loadingRes(@DrawableRes int resId, @NonNull DraweeView simpleDraweeView, @NonNull int width, @NonNull int height) {
        loadingImage(LOADING_TYPE_FOR_RES, resId, simpleDraweeView, true, width, height, null, null, null);
    }


    //加载直接返回Bitmap
    public void loadImageBitmap(final String url, @NonNull int width, @NonNull int height, final AcbflwFrescoBitmapCallback<Bitmap> callback) {
        //格式化网址
        final Uri uri;
        if (url.trim().indexOf("http") == 0) {
            uri = Uri.parse(url);
        } else {
            uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_FILE_SCHEME)
                    .path(url)
                    .build();
        }
        final ImageRequest imageRequest = getImageRequest(ImageRequestBuilder.newBuilderWithSource(uri)
                , width, height, true);
        DataSource<CloseableReference<CloseableImage>> dataSource = ImagePipelineFactory.getInstance().getImagePipeline().fetchDecodedImage(imageRequest, null);

        try {
            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                                     @Override
                                     public void onNewResultImpl(@Nullable final Bitmap bitmap) {
                                         if (callback != null && bitmap != null && !bitmap.isRecycled()) {
                                             executeBackgroundTask.submit(new Callable<Bitmap>() {
                                                 @Override
                                                 public Bitmap call() throws Exception {
                                                     final Bitmap resultBitmap = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
                                                     if (resultBitmap != null && !resultBitmap.isRecycled()) {
                                                         new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                             @Override
                                                             public void run() {
                                                                 callback.onSuccess(resultBitmap);
                                                             }
                                                         });
                                                     }
                                                     return resultBitmap;
                                                 }
                                             });
                                         }
                                     }

                                     @Override
                                     public void onCancellation(DataSource<CloseableReference<CloseableImage>> dataSource) {
                                         super.onCancellation(dataSource);
                                         if (callback != null) {
                                             callback.onCancel();
                                         }
                                     }

                                     @Override
                                     public void onFailureImpl(DataSource dataSource) {
                                         if (callback != null) {
                                             Throwable throwable = null;
                                             if (dataSource != null) {
                                                 throwable = dataSource.getFailureCause();
                                             }
                                             callback.onFailure(throwable);
                                         }
                                     }
                                 },
                    UiThreadImmediateExecutorService.getInstance());
        } catch (Exception e) {
            //oom风险.
            e.printStackTrace();
            callback.onFailure(e);
        }
    }

    //加载直接返回Bitmap
    public void loadImageBitmap(@DrawableRes final int resId, @NonNull int width, @NonNull int height, final AcbflwFrescoBitmapCallback<Bitmap> callback) {
        final ImageRequest imageRequest = getImageRequest(ImageRequestBuilder.newBuilderWithResourceId(resId)
                , width, height, true);
        DataSource<CloseableReference<CloseableImage>> dataSource = ImagePipelineFactory.getInstance().getImagePipeline().fetchDecodedImage(imageRequest, null);

        try {
            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                                     @Override
                                     public void onNewResultImpl(@Nullable final Bitmap bitmap) {
                                         if (callback != null && bitmap != null && !bitmap.isRecycled()) {
                                             executeBackgroundTask.submit(new Callable<Bitmap>() {
                                                 @Override
                                                 public Bitmap call() throws Exception {
                                                     final Bitmap resultBitmap = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
                                                     if (resultBitmap != null && !resultBitmap.isRecycled()) {
                                                         new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                             @Override
                                                             public void run() {
                                                                 callback.onSuccess(resultBitmap);
                                                             }
                                                         });
                                                     }
                                                     return resultBitmap;
                                                 }
                                             });
                                         }
                                     }

                                     @Override
                                     public void onCancellation(DataSource<CloseableReference<CloseableImage>> dataSource) {
                                         super.onCancellation(dataSource);
                                         if (callback != null) {
                                             callback.onCancel();
                                         }
                                     }

                                     @Override
                                     public void onFailureImpl(DataSource dataSource) {
                                         if (callback != null) {
                                             Throwable throwable = null;
                                             if (dataSource != null) {
                                                 throwable = dataSource.getFailureCause();
                                             }
                                             callback.onFailure(throwable);
                                         }
                                     }
                                 },
                    UiThreadImmediateExecutorService.getInstance());
        } catch (Exception e) {
            //oom风险.
            e.printStackTrace();
            callback.onFailure(e);
        }
    }

    /**
     * 加载bitmap
     *
     * @param bitmap
     * @param simpleDraweeView
     * @param width
     * @param height
     */
    public void lodingImageForBitmap(Bitmap bitmap, SimpleDraweeView simpleDraweeView, int width, int height) {
        try {
            if (bitmap != null) {
                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(AcbflwBaseApplication.appContext.getContentResolver(), bitmap, null, null));
                if (uri != null) {
                    ImageRequest imageRequest = getImageRequest(ImageRequestBuilder.newBuilderWithSource(uri), width, height, true);
                    DraweeController draweeController = getDraweeController(imageRequest, simpleDraweeView, true, null);
                    //开始加载
                    simpleDraweeView.setController(draweeController);
                }
            }
        } catch (Exception e) {
            AtlwLogUtils.logD(TAG, "加载bitmap失败");
        }

    }


    private final int LOADING_TYPE_FOR_NET = 0;//网络图片加载
    private final int LOADING_TYPE_FOR_LOCAL = 1;//本地图片加载
    private final int LOADING_TYPE_FOR_RES = 2;//资源图片加载

    /**
     * 加载图片
     *
     * @param type             加载图片的类型，是加载网络还是本地还是资源
     * @param pathOrRes        加载的网址或者本地地址或者资源id
     * @param simpleDraweeView 要加载到的图片位置
     * @param width            加载控件的宽度
     * @param height           加载控件的高度
     * @param loadingResId     加载中图片的资源id，可以为空，为空就是控件本身的加载中图片
     * @param failResId        加载失败图片的资源id，可以为空，为空就是控件本身的加载失败图片
     * @param listener         控制器加载状态监听
     */
    private void loadingImage(int type, @NonNull Object pathOrRes, @NonNull DraweeView simpleDraweeView, boolean isUserCache,
                              @NonNull int width, @NonNull int height, @DrawableRes Integer loadingResId, @DrawableRes Integer failResId, ControllerListener<ImageInfo> listener) {
        if (pathOrRes != null && simpleDraweeView != null) {
            ImageRequest imageRequest = null;
            DraweeController draweeController = null;
            DraweeHierarchy draweeHierarchy = null;
            switch (type) {
                case LOADING_TYPE_FOR_NET:
                    imageRequest = getImageRequest(ImageRequestBuilder.newBuilderWithSource(Uri.parse(String.valueOf(pathOrRes))), width, height, true);
                    break;
                case LOADING_TYPE_FOR_LOCAL:
                    imageRequest = getImageRequest(ImageRequestBuilder.newBuilderWithSource(new Uri.Builder().scheme(UriUtil.LOCAL_FILE_SCHEME)
                            .path(String.valueOf(pathOrRes)).build()), width, height, true);
                    break;
                case LOADING_TYPE_FOR_RES:
                    imageRequest = getImageRequest(ImageRequestBuilder.newBuilderWithResourceId((Integer) pathOrRes), width, height, true);
                    break;
            }
            if (imageRequest != null) {
                draweeController = getDraweeController(imageRequest, simpleDraweeView, isUserCache, listener);
                if (draweeController != null) {
                    draweeHierarchy = getDraweeHierarchy(simpleDraweeView, loadingResId, failResId);
                    //设置占位图
                    if (draweeHierarchy != null) {
                        simpleDraweeView.setHierarchy(draweeHierarchy);
                    }
                    //开始加载
                    simpleDraweeView.setController(draweeController);
                }
            }
        }
    }

    /**
     * 获取图片请求的request
     *
     * @param imageRequestBuilder         图片请求builder
     * @param width                       宽
     * @param height                      高
     * @param isAllowProgressiveRendering 是否允许使用渐进式加载
     * @return
     */
    private ImageRequest getImageRequest(ImageRequestBuilder imageRequestBuilder, Integer width, Integer height, boolean isAllowProgressiveRendering) {
        //设置加载的宽高
        if (width != null && height != null) {
            try {
                ResizeOptions resizeOptions = new ResizeOptions(width, height);
                imageRequestBuilder.setResizeOptions(resizeOptions);
                resizeOptions = null;
            } catch (Exception e) {
                AtlwLogUtils.logE(TAG, "resize失败");
            }

            //判断是否需要显示缩略图
            if (width < showThumbnaiMaxWidth && height < showThumbnaiMaxHeight) {
                imageRequestBuilder.setLocalThumbnailPreviewsEnabled(true);
            }

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
     * @param listener
     * @return
     */
    private DraweeController getDraweeController(ImageRequest imageRequest, DraweeView simpleDraweeView, boolean isUserCache, ControllerListener<ImageInfo> listener) {
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
        }
        return builder.build();
    }

    /**
     * 获取占位图控制器
     *
     * @param loadingResId
     * @param failResId
     * @return
     */
    private DraweeHierarchy getDraweeHierarchy(DraweeView simpleDraweeView, @DrawableRes Integer loadingResId, @DrawableRes Integer failResId) {
        if (loadingResId == null && failResId == null) {
            return null;
        } else if (simpleDraweeView != null) {
            GenericDraweeHierarchy hierarchy = (GenericDraweeHierarchy) simpleDraweeView.getHierarchy();
            if (hierarchy == null) {
                GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(AcbflwBaseApplication.appContext.getResources());
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


    /**
     * 检测地址是否是网络地址
     *
     * @param path
     * @return
     */
    public boolean checkPathIsNetUrl(@NonNull String path) {
        return path != null && !"".equals(path) && (path.contains("http://") || path.contains("https://"));
    }

    /**
     * 检测地址是否是本地地址
     *
     * @param path
     * @return
     */
    public boolean chePathIsLocalPath(@NonNull String path) {
        return new File(path).exists();
    }


    /**
     * 清除内存缓存
     */
    public void clearMemoryCache() {
        Fresco.getImagePipeline().clearMemoryCaches();
    }

    /**
     * 清除本地缓存
     */
    public void clearDiskCache() {
        Fresco.getImagePipeline().clearDiskCaches();
    }

    /**
     * 暂停加载图片
     */
    public void pauseLoading() {
        Fresco.getImagePipeline().pause();
        clearMemoryCache();
    }

    /**
     * 恢复加载图片
     */
    public void resumeLoading() {
        Fresco.getImagePipeline().resume();
    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    public Bitmap zoomImage(Bitmap bgimage, double newWidth,
                            double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap((int) newWidth, (int) newHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(bgimage, matrix, null);
        return bitmap;
    }

    /* * 把两个位图覆盖合成为一个位图，以底层位图的长宽为基准
     * @param backBitmap 在底部的位图
     * @param frontBitmap 盖在上面的位图
     * @return*/
    public Bitmap mergeBitmapForSearchCarMapPeople(Bitmap backBitmap, Bitmap frontBitmap, int maxWidth) {
        try {
            if (backBitmap == null || backBitmap.isRecycled()
                    || frontBitmap == null || frontBitmap.isRecycled()) {
                AtlwLogUtils.logE(TAG, "backBitmap=" + backBitmap + ";frontBitmap=" + frontBitmap);
                return null;
            }
            //底图转换宽高
            Bitmap bitmapBack = Bitmap.createBitmap((int) (maxWidth), (int) (maxWidth), Bitmap.Config.ARGB_8888);
            Canvas canvasBack = new Canvas(bitmapBack);
            Rect rectBack = new Rect(0, 0, bitmapBack.getWidth(), bitmapBack.getHeight());
            Rect rectFrontRectBack = new Rect(0, 0, backBitmap.getWidth(), backBitmap.getHeight());
            canvasBack.drawBitmap(backBitmap, rectFrontRectBack, rectBack, null);

            //整体图
            Bitmap bitmapAll = Bitmap.createBitmap(maxWidth, (int) (frontBitmap.getHeight() * (maxWidth * 1.0 / frontBitmap.getWidth())), Bitmap.Config.ARGB_8888);
            Canvas canvasAll = new Canvas(bitmapAll);
            Rect baseRect = new Rect(0, 0, bitmapAll.getWidth(), bitmapAll.getHeight());
            Rect frontRect = new Rect(0, 0, frontBitmap.getWidth(), frontBitmap.getHeight());
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(bitmapBack, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            canvasAll.drawBitmap(frontBitmap, frontRect, baseRect, null);
            canvasAll.drawCircle(Double.valueOf(maxWidth / 2.0).floatValue(), Double.valueOf(maxWidth / 2 - 5).floatValue()
                    , Double.valueOf((maxWidth * 0.8f) / 2).floatValue(), paint);

            return bitmapAll;
        } catch (Exception e) {
            return null;
        }
    }
}
