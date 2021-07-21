package android.lorenwang.tools.image.loading;

import android.graphics.Bitmap;
import android.lorenwang.tools.AtlwConfig;

import com.bumptech.glide.load.Transformation;

/**
 * 功能作用：图片加载配置类
 * 初始注释时间： 2020/10/20 11:27 上午
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
public class AtlwImageLoadConfig {

    private AtlwImageLoadConfig() {
    }

    /**
     * 图片加载回调
     */
    private AtlwImageLoadCallback loadCallback;
    /**
     * 缩放类型
     */
    private boolean scaleTypeFitCenter = false;
    /**
     * 缩放类型
     */
    private boolean scaleTypeCenterGroup = false;
    /**
     * 缩放类型
     */
    private boolean scaleTypeCenterInside = false;
    /**
     * 缩放类型
     */
    private boolean scaleTypeFitStart = false;
    /**
     * 缩放类型
     */
    private boolean scaleTypeFitEnd = false;
    /**
     * 缩放类型
     */
    private boolean scaleTypeFocusCrop = false;
    /**
     * 缩放类型
     */
    private boolean scaleTypeCenter = false;
    /**
     * 缩放类型
     */
    private boolean scaleTypeFitXy = false;
    /**
     * 缩放类型
     */
    private boolean scaleTypeFitBottomStart = false;
    /**
     * 是否关闭内存缓存
     */
    private boolean disableMemoryCache = false;
    /**
     * 是否关闭存储卡缓存
     */
    private boolean disableDiskCache = false;
    /**
     * 是否使用渐进式加载
     */
    private boolean allowProgressiveRendering = true;
    /**
     * 是否使用缓存加载图片
     */
    private boolean useCacheLoadImage = true;
    /**
     * 使用实际的宽高比,默认在没有传递宽高时以及图片本身没有设置宽高时使用
     * 和 showViewWidth、showViewHeight结合使用
     */
    private boolean useActualAspectRatio = true;
    /**
     * 如果是gif图片的话是否播放动图
     */
    private boolean gifPlay = true;
    /**
     * 是否是获取位图
     */
    private boolean loadGetBitmap = false;
    /**
     * 是否使用原图
     */
    private boolean useOriginImage = false;
    /**
     * 是否使用默认的加载中以及失败的图片
     */
    private boolean useDefaultLoadAndFailImage = true;
    /**
     * 获取bitmap使用的
     */
    private Transformation<Bitmap>[] bitmapTransformations;
    /**
     * 显示宽度，和useActualAspectRatio结合使用
     */
    private int showViewWidth = 0;
    /**
     * 显示高度，和useActualAspectRatio结合使用
     */
    private int showViewHeight = 0;
    /**
     * 加载的图片大小处理宽度
     */
    private int resizeLoadWidth = 0;
    /**
     * 加载的图片大小处理高度
     */
    private int resizeLoadHeight = 0;
    /**
     * 加载失败图片
     */
    private Integer imageLoadingFailResId;
    /**
     * 加载中图片
     */
    private Integer imageLoadingLoadResId;
    /**
     * 高斯模糊迭代次数
     */
    private Integer blurIterations = null;
    /**
     * 高斯模糊半径
     */
    private Integer blurRadius = null;
    /**
     * 缩略图缓存地址
     */
    private String thumbSavePath = null;

    public String getThumbSavePath() {
        return thumbSavePath;
    }

    public Integer getBlurIterations() {
        return blurIterations;
    }

    public Integer getBlurRadius() {
        return blurRadius;
    }

    public int getShowViewHeight() {
        return showViewHeight;
    }

    public int getShowViewWidth() {
        return showViewWidth;
    }

    public int getResizeLoadHeight() {
        return resizeLoadHeight;
    }

    public int getResizeLoadWidth() {
        return resizeLoadWidth;
    }

    public boolean isAllowProgressiveRendering() {
        return allowProgressiveRendering;
    }

    public boolean isUseCacheLoadImage() {
        return useCacheLoadImage;
    }

    public boolean isUseOriginImage() {
        return useOriginImage;
    }

    public boolean isUseDefaultLoadAndFailImage() {
        return useDefaultLoadAndFailImage;
    }

    public AtlwImageLoadCallback getLoadCallback() {
        return loadCallback;
    }

    public Transformation<Bitmap>[] getBitmapTransformations() {
        return bitmapTransformations;
    }

    public boolean isLoadGetBitmap() {
        return loadGetBitmap;
    }

    public boolean isScaleTypeFitCenter() {
        return scaleTypeFitCenter;
    }

    public boolean isScaleTypeCenterGroup() {
        return scaleTypeCenterGroup;
    }

    public boolean isScaleTypeCenterInside() {
        return scaleTypeCenterInside;
    }

    public boolean isScaleTypeFitStart() {
        return scaleTypeFitStart;
    }

    public boolean isScaleTypeFitEnd() {
        return scaleTypeFitEnd;
    }

    public boolean isScaleTypeFocusCrop() {
        return scaleTypeFocusCrop;
    }

    public boolean isScaleTypeCenter() {
        return scaleTypeCenter;
    }

    public boolean isScaleTypeFitXy() {
        return scaleTypeFitXy;
    }

    public boolean isScaleTypeFitBottomStart() {
        return scaleTypeFitBottomStart;
    }

    public boolean isDisableDiskCache() {
        return disableDiskCache;
    }

    public boolean isDisableMemoryCache() {
        return disableMemoryCache;
    }

    public boolean isUseActualAspectRatio() {
        return useActualAspectRatio;
    }

    public boolean isGifPlay() {
        return gifPlay;
    }

    public Integer getImageLoadingFailResId() {
        return imageLoadingFailResId;
    }

    public Integer getImageLoadingLoadResId() {
        return imageLoadingLoadResId;
    }

    public static class Build {
        /**
         * 图片加载回调
         */
        private AtlwImageLoadCallback loadCallback;
        private boolean scaleTypeFitCenter = false;
        private boolean scaleTypeCenterGroup = false;
        private boolean scaleTypeCenterInside = false;
        private boolean scaleTypeFitStart = false;
        private boolean scaleTypeFitEnd = false;
        private boolean scaleTypeFocusCrop = false;
        private boolean scaleTypeCenter = false;
        private boolean scaleTypeFitXy = false;
        private boolean scaleTypeFitBottomStart = false;
        /**
         * 是否关闭内存缓存
         */
        private boolean disableMemoryCache = false;
        /**
         * 是否关闭存储卡缓存
         */
        private boolean disableDiskCache = false;
        /**
         * 是否使用渐进式加载
         */
        private boolean allowProgressiveRendering = true;
        /**
         * 是否使用缓存加载图片
         */
        private boolean useCacheLoadImage = true;
        /**
         * 使用实际的宽高比,默认在没有传递宽高时以及图片本身没有设置宽高时使用
         */
        private boolean useActualAspectRatio = true;
        /**
         * 如果是gif图片的话是否播放动图
         */
        private boolean gifPlay = true;
        /**
         * 是否是获取位图
         */
        private boolean loadGetBitmap = false;
        /**
         * 是否使用原图
         */
        private boolean useOriginImage = false;
        /**
         * 是否使用默认的加载中以及失败的图片
         */
        private boolean useDefaultLoadAndFailImage = true;
        private Transformation<Bitmap>[] bitmapTransformations;
        /**
         * 显示宽度，和useActualAspectRatio结合使用
         */
        private int showViewWidth = 0;
        /**
         * 显示高度，和useActualAspectRatio结合使用
         */
        private int showViewHeight = 0;
        /**
         * 加载的图片大小处理宽度
         */
        private int resizeLoadWidth = 0;
        /**
         * 加载的图片大小处理高度
         */
        private int resizeLoadHeight = 0;
        /**
         * 加载失败图片
         */
        private Integer imageLoadingFailResId;
        /**
         * 加载中图片
         */
        private Integer imageLoadingLoadResId;
        /**
         * 高斯模糊迭代次数
         */
        private Integer blurIterations = null;
        /**
         * 高斯模糊半径
         */
        private Integer blurRadius = null;
        /**
         * 缩略图缓存地址
         */
        private String thumbSavePath = null;

        public Build setThumbSavePath(String thumbSavePath) {
            this.thumbSavePath = thumbSavePath;
            return this;
        }

        public Build setBlurIterations(Integer blurIterations) {
            this.blurIterations = blurIterations;
            return this;
        }

        public Build setBlurRadius(Integer blurRadius) {
            this.blurRadius = blurRadius;
            return this;
        }

        public Build setResizeLoadHeight(int resizeLoadHeight) {
            this.resizeLoadHeight = resizeLoadHeight;
            return this;
        }

        public Build setResizeLoadWidth(int resizeLoadWidth) {
            this.resizeLoadWidth = resizeLoadWidth;
            return this;
        }

        public Build setShowViewHeight(int showViewHeight) {
            this.showViewHeight = showViewHeight;
            return this;
        }

        public Build setShowViewWidth(int showViewWidth) {
            this.showViewWidth = showViewWidth;
            return this;
        }

        public Build setUseOriginImage(boolean useOriginImage) {
            this.useOriginImage = useOriginImage;
            return this;
        }

        public Build setLoadCallback(AtlwImageLoadCallback loadCallback) {
            this.loadCallback = loadCallback;
            return this;
        }

        public Build setAllowProgressiveRendering(boolean allowProgressiveRendering) {
            this.allowProgressiveRendering = allowProgressiveRendering;
            return this;
        }

        public Build setUseCacheLoadImage(boolean useCacheLoadImage) {
            this.useCacheLoadImage = useCacheLoadImage;
            return this;
        }

        public Build setScaleTypeFitCenter(boolean scaleTypeFitCenter) {
            this.scaleTypeFitCenter = scaleTypeFitCenter;
            return this;
        }

        public Build setLoadGetBitmap(boolean loadGetBitmap) {
            this.loadGetBitmap = loadGetBitmap;
            return this;
        }

        public Build setScaleTypeCenterGroup(boolean scaleTypeCenterGroup) {
            this.scaleTypeCenterGroup = scaleTypeCenterGroup;
            return this;
        }

        public Build setScaleTypeCenterInside(boolean scaleTypeCenterInside) {
            this.scaleTypeCenterInside = scaleTypeCenterInside;
            return this;
        }

        public Build setScaleTypeFitStart(boolean scaleTypeFitStart) {
            this.scaleTypeFitStart = scaleTypeFitStart;
            return this;
        }

        public Build setScaleTypeFitEnd(boolean scaleTypeFitEnd) {
            this.scaleTypeFitEnd = scaleTypeFitEnd;
            return this;
        }

        public Build setScaleTypeFocusCrop(boolean scaleTypeFocusCrop) {
            this.scaleTypeFocusCrop = scaleTypeFocusCrop;
            return this;
        }

        public Build setScaleTypeCenter(boolean scaleTypeCenter) {
            this.scaleTypeCenter = scaleTypeCenter;
            return this;
        }

        public Build setScaleTypeFitXy(boolean scaleTypeFitXy) {
            this.scaleTypeFitXy = scaleTypeFitXy;
            return this;
        }

        public Build setScaleTypeFitBottomStart(boolean scaleTypeFitBottomStart) {
            this.scaleTypeFitBottomStart = scaleTypeFitBottomStart;
            return this;
        }

        public Build setBitmapTransformations(Transformation<Bitmap>[] bitmapTransformations) {
            this.bitmapTransformations = bitmapTransformations;
            return this;
        }

        public Build setDisableDiskCache(boolean disableDiskCache) {
            this.disableDiskCache = disableDiskCache;
            return this;
        }

        public Build setDisableMemoryCache(boolean disableMemoryCache) {
            this.disableMemoryCache = disableMemoryCache;
            return this;
        }

        public Build setUseActualAspectRatio(boolean useActualAspectRatio) {
            this.useActualAspectRatio = useActualAspectRatio;
            return this;
        }

        public Build setUseDefaultLoadAndFailImage(boolean useDefaultLoadAndFailImage) {
            this.useDefaultLoadAndFailImage = useDefaultLoadAndFailImage;
            return this;
        }

        public Build setGifPlay(boolean gifPlay) {
            this.gifPlay = gifPlay;
            return this;
        }

        public Build setImageLoadingFailResId(Integer imageLoadingFailResId) {
            this.imageLoadingFailResId = imageLoadingFailResId == null ? AtlwConfig.imageLoadingFailResId : imageLoadingFailResId;
            return this;
        }

        public Build setImageLoadingLoadResId(Integer imageLoadingLoadResId) {
            this.imageLoadingLoadResId = imageLoadingLoadResId == null ? AtlwConfig.imageLoadingLoadResId : imageLoadingLoadResId;
            return this;
        }

        public AtlwImageLoadConfig build() {
            AtlwImageLoadConfig config = new AtlwImageLoadConfig();
            config.scaleTypeFitCenter = this.scaleTypeFitCenter;
            config.scaleTypeCenterGroup = this.scaleTypeCenterGroup;
            config.scaleTypeCenterInside = this.scaleTypeCenterInside;
            config.loadGetBitmap = this.loadGetBitmap;
            config.scaleTypeFitStart = this.scaleTypeFitStart;
            config.scaleTypeFitEnd = this.scaleTypeFitEnd;
            config.useOriginImage = this.useOriginImage;
            config.loadCallback = this.loadCallback;
            config.scaleTypeFocusCrop = this.scaleTypeFocusCrop;
            config.bitmapTransformations = this.bitmapTransformations;
            config.scaleTypeCenter = this.scaleTypeCenter;
            config.scaleTypeFitXy = this.scaleTypeFitXy;
            config.scaleTypeFitBottomStart = this.scaleTypeFitBottomStart;
            config.disableDiskCache = this.disableDiskCache;
            config.disableMemoryCache = this.disableMemoryCache;
            config.useActualAspectRatio = this.useActualAspectRatio;
            config.useCacheLoadImage = this.useCacheLoadImage;
            config.useDefaultLoadAndFailImage = this.useDefaultLoadAndFailImage;
            config.allowProgressiveRendering = this.allowProgressiveRendering;
            config.gifPlay = this.gifPlay;
            config.showViewHeight = this.showViewHeight;
            config.showViewWidth = this.showViewWidth;
            config.resizeLoadHeight = this.resizeLoadHeight;
            config.resizeLoadWidth = this.resizeLoadWidth;
            config.imageLoadingFailResId = this.imageLoadingFailResId;
            config.imageLoadingLoadResId = this.imageLoadingLoadResId;
            config.blurIterations = this.blurIterations;
            config.blurRadius = this.blurRadius;
            config.thumbSavePath = this.thumbSavePath;
            return config;
        }
    }

    public Build copyBuild() {
        Build build = new Build();
        build.scaleTypeFitCenter = this.scaleTypeFitCenter;
        build.scaleTypeCenterGroup = this.scaleTypeCenterGroup;
        build.scaleTypeCenterInside = this.scaleTypeCenterInside;
        build.loadGetBitmap = this.loadGetBitmap;
        build.scaleTypeFitStart = this.scaleTypeFitStart;
        build.scaleTypeFitEnd = this.scaleTypeFitEnd;
        build.useOriginImage = this.useOriginImage;
        build.scaleTypeFocusCrop = this.scaleTypeFocusCrop;
        build.bitmapTransformations = this.bitmapTransformations;
        build.scaleTypeCenter = this.scaleTypeCenter;
        build.scaleTypeFitXy = this.scaleTypeFitXy;
        build.scaleTypeFitBottomStart = this.scaleTypeFitBottomStart;
        build.disableDiskCache = this.disableDiskCache;
        build.disableMemoryCache = this.disableMemoryCache;
        build.useActualAspectRatio = this.useActualAspectRatio;
        build.useCacheLoadImage = this.useCacheLoadImage;
        build.useDefaultLoadAndFailImage = this.useDefaultLoadAndFailImage;
        build.allowProgressiveRendering = this.allowProgressiveRendering;
        build.gifPlay = this.gifPlay;
        build.showViewHeight = this.showViewHeight;
        build.showViewWidth = this.showViewWidth;
        build.resizeLoadHeight = this.resizeLoadHeight;
        build.resizeLoadWidth = this.resizeLoadWidth;
        build.imageLoadingFailResId = this.imageLoadingFailResId;
        build.imageLoadingLoadResId = this.imageLoadingLoadResId;
        build.blurIterations = this.blurIterations;
        build.blurRadius = this.blurRadius;
        build.thumbSavePath = this.thumbSavePath;
        return build;
    }
}
