package android.lorenwang.tools.image.loading;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.lorenwang.tools.AtlwConfig;
import android.lorenwang.tools.app.AtlwScreenUtil;
import android.lorenwang.tools.app.AtlwThreadUtil;
import android.lorenwang.tools.app.AtlwViewUtil;
import android.lorenwang.tools.image.AtlwImageCommonUtil;
import android.widget.ImageView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import androidx.annotation.DrawableRes;
import javabase.lorenwang.tools.JtlwMatchesRegularCommon;

/**
 * 功能作用：基础图片加载接口
 * 创建时间：2019-12-19 13:48
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 图片加载，对于的是不确定是网络图片还是本地图片的情况下使用--loadingImage(pathOrRes,imageView)
 * 图片加载，对于的是不确定是网络图片还是本地图片的情况下使用--loadingImage(pathOrRes,imageView,config)
 * 加载网络图片--loadingNetImage(path,imageView)
 * 加载本地图片--loadingLocalImage(path,imageView)
 * 加载资源图片--loadingResImage(resId,imageView)
 * 加载网络图片--loadingNetImage(path,imageView,config)
 * 加载本地图片--loadingLocalImage(path,imageView,config)
 * 加载资源图片--loadingResImage(resId,imageView,config)
 * 加载bitmap位图--loadingBitmapImage(bitmap,imageView,config)
 * 获取网络图片位图信息--getNetImageBitmap(path,config)
 * 获取指定列表的图片位图--getNetImageBitmap(list,config)
 * 清除内存缓存--clearMemoryCache()
 * 清除本地缓存--clearDiskCache()
 * 暂停加载图片--pauseLoading()
 * 恢复加载图片--resumeLoading()
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public abstract class AtlwBaseImageLoading {
    protected final String TAG = getClass().getName();
    /**
     * 未知图片
     */
    private final int LOADING_TYPE_FOR_UN_KNOW = -1;
    /**
     * 网络图片加载
     */
    private final int LOADING_TYPE_FOR_NET = 0;
    /**
     * 本地图片加载
     */
    private final int LOADING_TYPE_FOR_LOCAL = 1;
    /**
     * 资源图片加载
     */
    private final int LOADING_TYPE_FOR_RES = 2;
    protected int showThumbnailMaxWidth;
    protected int showThumbnailMaxHeight;

    /**
     * 默认加载配置
     */
    protected final AtlwImageLoadConfig defaultConfig = new AtlwImageLoadConfig.Build().build();

    public AtlwBaseImageLoading() {
        showThumbnailMaxWidth = (int) (AtlwScreenUtil.getInstance().getScreenWidth() * 0.05);
        showThumbnailMaxHeight = (int) (AtlwScreenUtil.getInstance().getScreenHeight() * 0.05);
    }

    /**
     * 图片加载，对于的是不确定是网络图片还是本地图片的情况下使用
     *
     * @param pathOrRes 图片或资源地址
     * @param imageView 图片控件
     */
    public void loadingImage(Object pathOrRes, ImageView imageView) {
        this.loadingImage(pathOrRes, imageView, defaultConfig);
    }

    /**
     * 图片加载，对于的是不确定是网络图片还是本地图片的情况下使用
     *
     * @param pathOrRes 图片或资源地址
     * @param imageView 图片控件
     */
    public void loadingImage(Object pathOrRes, ImageView imageView, @NotNull AtlwImageLoadConfig config) {
        switch (getPathOrResType(pathOrRes)) {
            case LOADING_TYPE_FOR_NET:
                loadingNetImage((String) pathOrRes, imageView, config);
                break;
            case LOADING_TYPE_FOR_LOCAL:
                loadingLocalImage((String) pathOrRes, imageView, config);
                break;
            case LOADING_TYPE_FOR_RES:
                loadingResImage((Integer) pathOrRes, imageView, config);
                break;
            default:
                break;
        }
    }

    /**
     * 获取地址或资源类型
     *
     * @param pathOrRes 地址或资源
     * @return 类型
     */
    private int getPathOrResType(Object pathOrRes) {
        if (pathOrRes != null) {
            if (pathOrRes instanceof String) {
                if (((String) pathOrRes).matches(JtlwMatchesRegularCommon.EXP_URL_STR) || ((String) pathOrRes).matches(
                        JtlwMatchesRegularCommon.EXP_URL_IP)) {
                    return LOADING_TYPE_FOR_NET;
                } else {
                    File file = new File((String) pathOrRes);
                    if (file.exists()) {
                        return LOADING_TYPE_FOR_LOCAL;
                    } else {
                        return LOADING_TYPE_FOR_UN_KNOW;
                    }
                }
            } else if (pathOrRes instanceof Integer) {
                return LOADING_TYPE_FOR_RES;
            } else {
                return LOADING_TYPE_FOR_UN_KNOW;
            }
        } else {
            return LOADING_TYPE_FOR_UN_KNOW;
        }
    }

    /**
     * 加载网络图片
     *
     * @param path      网络图片地址
     * @param imageView 图片控件
     */
    public void loadingNetImage(String path, ImageView imageView) {
        this.loadingNetImage(path, imageView, defaultConfig);
    }

    /**
     * 加载本地图片
     *
     * @param path      本地图片地址
     * @param imageView 图片控件
     */
    public void loadingLocalImage(String path, ImageView imageView) {
        this.loadingLocalImage(path, imageView, defaultConfig);
    }

    /**
     * 加载资源图片
     *
     * @param resId     资源图片id
     * @param imageView 图片控件
     */
    public void loadingResImage(@DrawableRes int resId, ImageView imageView) {
        this.loadingResImage(resId, imageView, defaultConfig);
    }

    /**
     * 加载网络图片
     *
     * @param path      网络图片地址
     * @param imageView 图片控件
     * @param config    回调
     */
    public abstract void loadingNetImage(String path, ImageView imageView, @NotNull AtlwImageLoadConfig config);

    /**
     * 加载本地图片
     *
     * @param path      本地图片地址
     * @param imageView 图片控件
     * @param config    回调
     */
    public abstract void loadingLocalImage(String path, ImageView imageView, @NotNull AtlwImageLoadConfig config);

    /**
     * 加载资源图片
     *
     * @param resId     资源图片id
     * @param imageView 图片控件
     * @param config    回调
     */
    public abstract void loadingResImage(@DrawableRes int resId, ImageView imageView, @NotNull AtlwImageLoadConfig config);

    /**
     * 加载bitmap位图
     *
     * @param bitmap    位图
     * @param imageView 图片控件
     */
    public abstract void loadingBitmapImage(Bitmap bitmap, ImageView imageView, @NotNull AtlwImageLoadConfig config);

    /**
     * 获取网络图片位图信息
     *
     * @param path   网络图片地址
     * @param config 位图信息
     */
    public abstract void getNetImageBitmap(String path, AtlwImageLoadConfig config);

    /**
     * 获取指定列表的图片位图
     *
     * @param list   图片地址列表
     * @param config 配置信息
     */
    public void getNetImageBitmap(ArrayList<String> list, final AtlwImageLoadConfig config) {
        if (config != null && list != null && list.size() > 0 && config.getLoadCallback() != null) {
            AtlwImageLoadConfig.Build build;
            final ConcurrentHashMap map = new ConcurrentHashMap();
            final int size = list.size();
            for (final String path : list) {
                build = config.copyBuild();
                build.setLoadCallback(new AtlwImageLoadCallback() {
                    @Override
                    public void onFailure() {
                        map.put(path, getNetImageBitmapResult(null, config));
                        if (map.size() == size) {
                            config.getLoadCallback().onGetListBitmapFinish(map);
                        }
                    }

                    @Override
                    public void onSuccess(Bitmap bitmap, int width, int height) {
                        map.put(path, getNetImageBitmapResult(bitmap, config));
                        if (map.size() == size) {
                            config.getLoadCallback().onGetListBitmapFinish(map);
                        }
                    }
                });
                getNetImageBitmap(path, build.build());
            }
        }
    }

    /**
     * 获取网络图片位图返回数据，当网络获取为空时使用fail图片
     *
     * @param bitmap 网络返回位图
     * @param config 配置信息
     */
    private Bitmap getNetImageBitmapResult(Bitmap bitmap, AtlwImageLoadConfig config) {
        if (bitmap != null) {
            return bitmap;
        } else {
            try {
                if (config == null || config.getImageLoadingFailResId() == null) {
                    return BitmapFactory.decodeStream(AtlwConfig.nowApplication.getResources().openRawResource(AtlwConfig.imageLoadingFailResId));
                } else {
                    return BitmapFactory.decodeStream(AtlwConfig.nowApplication.getResources().openRawResource(config.getImageLoadingFailResId()));
                }
            } catch (Exception ignore) {
                return null;
            }
        }
    }

    /**
     * 清除内存缓存
     */
    public abstract void clearMemoryCache();

    /**
     * 清除本地缓存
     */
    public abstract void clearDiskCache();

    /**
     * 暂停加载图片
     */
    public abstract void pauseLoading();

    /**
     * 恢复加载图片
     */
    public abstract void resumeLoading();

    /**
     * 是否显示缩略图
     *
     * @param imageView 被加载的图片控件
     * @param width     显示图片宽度
     * @param height    显示图片高度
     * @return 显示缩略图true
     */
    protected boolean isShowThumbnail(ImageView imageView, Integer width, Integer height) {
        if (imageView == null) {
            return false;
        }
        if (width == null || width <= 0) {
            width = imageView.getWidth();
        }
        if (height == null || height <= 0) {
            height = imageView.getHeight();
        }
        //判断是否需要显示缩略图
        return width < showThumbnailMaxWidth && height < showThumbnailMaxHeight;
    }

    /**
     * 设置图片样式
     *
     * @param useActualAspectRatio 是否使用自动宽高设置
     * @param imageView            图片控件
     * @param showViewWidth        显示的宽度
     * @param showViewHeight       显示的高度
     * @param imageWidth           位图宽度
     * @param imageHeight          位图高度
     */
    protected void setImageViewLayoutParams(boolean useActualAspectRatio, @NotNull ImageView imageView, int showViewWidth, int showViewHeight,
            int imageWidth, int imageHeight) {
        int setViewWidth = 0;
        int setViewHeight = 0;

        //判断可以设置数据的条件
        if (showViewWidth > 0 && showViewHeight > 0) {
            setViewWidth = showViewWidth;
            setViewHeight = showViewHeight;
        } else if (showViewWidth > 0) {
            if (useActualAspectRatio) {
                setViewWidth = showViewWidth;
                setViewHeight = Float.valueOf(imageHeight * 1.0f / imageWidth * setViewWidth).intValue();
            }
        } else if (showViewHeight > 0) {
            if (useActualAspectRatio) {
                setViewHeight = showViewHeight;
                setViewWidth = Float.valueOf(imageWidth * 1.0f / imageHeight * setViewHeight).intValue();
            }
        }
        if (setViewWidth > 0 && setViewHeight > 0 && (imageView.getWidth() != setViewWidth || imageView.getHeight() != setViewHeight)) {
            AtlwViewUtil.getInstance().setViewWidthHeight(imageView, setViewWidth, setViewHeight);
        }
    }

    /**
     * 返回位图信息
     *
     * @param bitmap 接口返回的位图
     * @param config 配置信息
     */
    protected void resultBitmap(Bitmap bitmap, final AtlwImageLoadConfig config) {
        if (bitmap != null && !bitmap.isRecycled()) {
            Bitmap resultBitmap = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
            //位图处理
            if (resultBitmap != null && !resultBitmap.isRecycled()) {
                //判断是否有显示宽高以及比例处理问题
                if (config.getShowViewWidth() > 0 && config.getShowViewHeight() > 0) {
                    resultBitmap = AtlwImageCommonUtil.getInstance().zoomImage(resultBitmap, config.getShowViewWidth(), config.getShowViewHeight());
                } else if (config.getShowViewWidth() > 0) {
                    if (config.isUseActualAspectRatio()) {
                        int zoomHeight = Float.valueOf(resultBitmap.getHeight() * 1.0f / resultBitmap.getWidth() * config.getShowViewWidth())
                                .intValue();
                        resultBitmap = AtlwImageCommonUtil.getInstance().zoomImage(resultBitmap, config.getShowViewWidth(), zoomHeight);
                    }
                } else if (config.getShowViewHeight() > 0) {
                    if (config.isUseActualAspectRatio()) {
                        int zoomWidth = Float.valueOf(resultBitmap.getWidth() * 1.0f / resultBitmap.getHeight() * config.getShowViewHeight())
                                .intValue();
                        resultBitmap = AtlwImageCommonUtil.getInstance().zoomImage(resultBitmap, zoomWidth, config.getShowViewHeight());
                    }
                }
                //返回位图
                if (resultBitmap != null && !resultBitmap.isRecycled() && config.getLoadCallback() != null) {
                    final Bitmap finalResultBitmap = resultBitmap;
                    AtlwThreadUtil.getInstance().postOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            config.getLoadCallback().onSuccess(finalResultBitmap, finalResultBitmap.getWidth(), finalResultBitmap.getHeight());
                        }
                    });
                }

            }
        }
    }
}
