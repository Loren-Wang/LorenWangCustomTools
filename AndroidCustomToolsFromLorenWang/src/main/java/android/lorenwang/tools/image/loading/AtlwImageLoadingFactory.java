package android.lorenwang.tools.image.loading;


import static android.lorenwang.tools.AtlwConfig.IMAGE_LOAD_LIBRARY_TYPE_FRESCO;
import static android.lorenwang.tools.AtlwConfig.IMAGE_LOAD_LIBRARY_TYPE_GLIDE;
import static android.lorenwang.tools.AtlwConfig.IMAGE_LOAD_LIBRARY_TYPE_IMAGE_LOAD;

/**
 * 功能作用：图片加载工厂
 * 初始注释时间： 2019/12/19 15:09
 * 注释创建人：Loren（王亮）
 * 方法介绍：
 * 1、获取图片加载实例---getImageLoading（imageLoadLibraryType）
 * 2、获取工具类实例---getInstance（）
 * 3、清除内存缓存,如果参数为空则清除所有相关实例---clearMemoryCache(imageLoadLibraryType)
 * 4、清除本地缓存,如果参数为空则清除所有相关实例---clearDiskCache(imageLoadLibraryType)
 * 5、暂停加载图片---pauseLoading()
 * 6、恢复加载图片---resumeLoading()
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author wangliang
 */
public class AtlwImageLoadingFactory {
    private final String TAG = getClass().getName();
    private static AtlwFrescoImageLoading acbflwFrescoImageLoading;
    private static AtlwGlideImageLoading acbflwGlideImageLoading;
    private static AtlwImageLoadImageLoading acbflwImageLoadImageLoading;
    private static AtlwImageLoadingFactory optionsFactory;

    private AtlwImageLoadingFactory() {
    }

    /**
     * 获取图片加载实例
     *
     * @param imageLoadLibraryType 实例类型
     * @return 相应的实例
     */
    public static AtlwBaseImageLoading getImageLoading(int imageLoadLibraryType) {
        switch (imageLoadLibraryType) {
            case IMAGE_LOAD_LIBRARY_TYPE_FRESCO:
                if (acbflwFrescoImageLoading == null) {
                    synchronized (AtlwFrescoImageLoading.class) {
                        if (acbflwFrescoImageLoading == null) {
                            acbflwFrescoImageLoading = new AtlwFrescoImageLoading();
                        }
                    }
                }
                return acbflwFrescoImageLoading;
            case IMAGE_LOAD_LIBRARY_TYPE_GLIDE:
                if (acbflwGlideImageLoading == null) {
                    synchronized (AtlwGlideImageLoading.class) {
                        if (acbflwGlideImageLoading == null) {
                            acbflwGlideImageLoading = new AtlwGlideImageLoading();
                        }
                    }
                }
                return acbflwGlideImageLoading;
            case IMAGE_LOAD_LIBRARY_TYPE_IMAGE_LOAD:
                if (acbflwImageLoadImageLoading == null) {
                    synchronized (AtlwImageLoadImageLoading.class) {
                        if (acbflwImageLoadImageLoading == null) {
                            acbflwImageLoadImageLoading = new AtlwImageLoadImageLoading();
                        }
                    }
                }
                return acbflwImageLoadImageLoading;
            default:
                return null;
        }
    }

    /**
     * 获取工具类实例
     *
     * @return 工具类实例
     */
    public static AtlwImageLoadingFactory getInstance() {
        if (optionsFactory == null) {
            synchronized (AtlwImageLoadingFactory.class) {
                if (optionsFactory == null) {
                    optionsFactory = new AtlwImageLoadingFactory();
                }
            }
        }
        return optionsFactory;
    }

    /**
     * 清除内存缓存,如果参数为空则清除所有相关实例
     */
    public void clearMemoryCache(Integer imageLoadLibraryType) {
        if (imageLoadLibraryType == null) {
            if (acbflwFrescoImageLoading != null) {
                acbflwFrescoImageLoading.clearMemoryCache();
            }
            if (acbflwGlideImageLoading != null) {
                acbflwGlideImageLoading.clearMemoryCache();
            }
        } else {
            switch (imageLoadLibraryType) {
                case IMAGE_LOAD_LIBRARY_TYPE_FRESCO:
                    if (acbflwFrescoImageLoading != null) {
                        acbflwFrescoImageLoading.clearMemoryCache();
                    }
                    break;
                case IMAGE_LOAD_LIBRARY_TYPE_GLIDE:
                    if (acbflwGlideImageLoading != null) {
                        acbflwGlideImageLoading.clearMemoryCache();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 清除本地缓存,如果参数为空则清除所有相关实例
     */
    public void clearDiskCache(Integer imageLoadLibraryType) {
        if (imageLoadLibraryType == null) {
            if (acbflwFrescoImageLoading != null) {
                acbflwFrescoImageLoading.clearDiskCache();
            }
            if (acbflwGlideImageLoading != null) {
                acbflwGlideImageLoading.clearDiskCache();
            }
        } else {
            switch (imageLoadLibraryType) {
                case IMAGE_LOAD_LIBRARY_TYPE_FRESCO:
                    if (acbflwFrescoImageLoading != null) {
                        acbflwFrescoImageLoading.clearDiskCache();
                    }
                    break;
                case IMAGE_LOAD_LIBRARY_TYPE_GLIDE:
                    if (acbflwGlideImageLoading != null) {
                        acbflwGlideImageLoading.clearDiskCache();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 暂停加载图片
     */
    public void pauseLoading() {
        if (acbflwFrescoImageLoading != null) {
            acbflwFrescoImageLoading.pauseLoading();
        }
        if (acbflwGlideImageLoading != null) {
            acbflwGlideImageLoading.pauseLoading();
        }
    }

    /**
     * 恢复加载图片
     */
    public void resumeLoading() {
        if (acbflwFrescoImageLoading != null) {
            acbflwFrescoImageLoading.resumeLoading();
        }
        if (acbflwGlideImageLoading != null) {
            acbflwGlideImageLoading.resumeLoading();
        }
    }
}
