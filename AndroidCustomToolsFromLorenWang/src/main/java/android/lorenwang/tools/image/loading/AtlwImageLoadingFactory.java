package android.lorenwang.tools.image.loading;

import static android.lorenwang.tools.AtlwSetting.IMAGE_LOAD_LIBRARY_TYPE_FRESCO;
import static android.lorenwang.tools.AtlwSetting.IMAGE_LOAD_LIBRARY_TYPE_GLIDE;


/**
 * 功能作用：图片加载工厂
 * 初始注释时间： 2019/12/19 15:09
 * 注释创建人：LorenWang（王亮）
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
 */
public class AtlwImageLoadingFactory {
    private final String TAG = getClass().getName();
    private static AtlwFrescoImageLoading qtFrescoImageLoading;
    private static AtlwGlideImageLoading qtGlideImageLoading;
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
                if (qtFrescoImageLoading == null) {
                    synchronized (AtlwFrescoImageLoading.class) {
                        if (qtFrescoImageLoading == null) {
                            qtFrescoImageLoading = new AtlwFrescoImageLoading();
                        }
                    }
                }
                return qtFrescoImageLoading;
            case IMAGE_LOAD_LIBRARY_TYPE_GLIDE:
                if (qtGlideImageLoading == null) {
                    synchronized (AtlwGlideImageLoading.class) {
                        if (qtGlideImageLoading == null) {
                            qtGlideImageLoading = new AtlwGlideImageLoading();
                        }
                    }
                }
                return qtGlideImageLoading;
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
            if (qtFrescoImageLoading != null) {
                qtFrescoImageLoading.clearMemoryCache();
            }
            if (qtGlideImageLoading != null) {
                qtGlideImageLoading.clearMemoryCache();
            }
        } else {
            switch (imageLoadLibraryType) {
                case IMAGE_LOAD_LIBRARY_TYPE_FRESCO:
                    if (qtFrescoImageLoading != null) {
                        qtFrescoImageLoading.clearMemoryCache();
                    }
                    break;
                case IMAGE_LOAD_LIBRARY_TYPE_GLIDE:
                    if (qtGlideImageLoading != null) {
                        qtGlideImageLoading.clearMemoryCache();
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
            if (qtFrescoImageLoading != null) {
                qtFrescoImageLoading.clearDiskCache();
            }
            if (qtGlideImageLoading != null) {
                qtGlideImageLoading.clearDiskCache();
            }
        } else {
            switch (imageLoadLibraryType) {
                case IMAGE_LOAD_LIBRARY_TYPE_FRESCO:
                    if (qtFrescoImageLoading != null) {
                        qtFrescoImageLoading.clearDiskCache();
                    }
                    break;
                case IMAGE_LOAD_LIBRARY_TYPE_GLIDE:
                    if (qtGlideImageLoading != null) {
                        qtGlideImageLoading.clearDiskCache();
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
        if (qtFrescoImageLoading != null) {
            qtFrescoImageLoading.pauseLoading();
        }
        if (qtGlideImageLoading != null) {
            qtGlideImageLoading.pauseLoading();
        }
    }

    /**
     * 恢复加载图片
     */
    public void resumeLoading() {
        if (qtFrescoImageLoading != null) {
            qtFrescoImageLoading.resumeLoading();
        }
        if (qtGlideImageLoading != null) {
            qtGlideImageLoading.resumeLoading();
        }
    }
}
