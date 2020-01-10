package android.lorenwang.common_base_frame.image.loading;

import android.graphics.Bitmap;
import android.lorenwang.tools.app.AtlwScreenUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;

import androidx.annotation.DrawableRes;
import javabase.lorenwang.tools.JtlwMatchesRegularCommon;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

/**
 * 功能作用：基础图片加载接口
 * 创建时间：2019-12-19 13:48
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 1、未知图片加载---loadingImage（pathOrRes,imageView,width,height）---对于的是不确定是网络图片还是本地图片的情况下使用
 * 2、未知图片加载---loadingImage（pathOrRes,imageView,width,height，config）---对于的是不确定是网络图片还是本地图片的情况下使用
 * 3、未知图片宽高加载---loadingImageAndSetWidthHeight（pathOrRes,imageView,width,height）---对于的是不确定是网络图片还是本地图片以及未初始设置图片宽高的情况下使用
 * 4、未知图片宽高加载---loadingImageAndSetWidthHeight（pathOrRes,imageView,width,height，config）---对于的是不确定是网络图片还是本地图片以及未初始设置图片宽高的情况下使用
 * 5、加载网络图片---loadingNetImage（path,imageView,width,height）
 * 6、加载网络图片---loadingNetImage（path,imageView,width,height，config）
 * 7、加载本地图片---loadingLocalImage（path,imageView,width,height）
 * 8、加载本地图片---loadingLocalImage（path,imageView,width,height，config）
 * 9、加载资源图片---loadingResImage（resId,imageView,width,height）
 * 10、加载资源图片---loadingResImage（resId,imageView,width,height，config）
 * 11、加载bitmap位图---loadingBitmapImage（bitmap,imageView,width,height，config）
 * 12、清除内存缓存---clearMemoryCache()
 * 13、清除本地缓存---clearDiskCache()
 * 14、暂停加载图片---pauseLoading()
 * 15、恢复加载图片---resumeLoading()
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public abstract class AcbflwBaseImageLoading {
    protected final String TAG = getClass().getName();
    private final int LOADING_TYPE_FOR_UN_KNOW = -1;//未知图片
    private final int LOADING_TYPE_FOR_NET = 0;//网络图片加载
    private final int LOADING_TYPE_FOR_LOCAL = 1;//本地图片加载
    private final int LOADING_TYPE_FOR_RES = 2;//资源图片加载
    protected int showThumbnailMaxWidth;
    protected int showThumbnailMaxHeight;

    public AcbflwBaseImageLoading() {
        showThumbnailMaxWidth = (int) (AtlwScreenUtils.getInstance().getScreenWidth() * 0.05);
        showThumbnailMaxHeight = (int) (AtlwScreenUtils.getInstance().getScreenHeight() * 0.05);
    }

    /**
     * 图片加载，对于的是不确定是网络图片还是本地图片的情况下使用
     *
     * @param pathOrRes 图片或资源地址
     * @param imageView 图片控件
     * @param width     图片加载宽度
     * @param height    图片加载高度
     */
    public void loadingImage(Object pathOrRes, ImageView imageView, int width, int height) {
        this.loadingImage(pathOrRes, imageView, width, height, null);
    }

    /**
     * 图片加载，对于的是不确定是网络图片还是本地图片的情况下使用
     *
     * @param pathOrRes 图片或资源地址
     * @param imageView 图片控件
     * @param width     图片加载宽度
     * @param height    图片加载高度
     */
    public void loadingImage(Object pathOrRes, ImageView imageView, int width, int height, AcbflwImageLoadConfig config) {
        switch (getPathOrResType(pathOrRes)) {
            case LOADING_TYPE_FOR_NET:
                loadingNetImage((String) pathOrRes, imageView, width, height, config);
                break;
            case LOADING_TYPE_FOR_LOCAL:
                loadingLocalImage((String) pathOrRes, imageView, width, height, config);
                break;
            case LOADING_TYPE_FOR_RES:
                loadingResImage((Integer) pathOrRes, imageView, width, height, config);
                break;
            default:
                break;
        }
    }

    /**
     * 加载网络图片，为了适应预先不知道图片宽高的情况，当不知道图片宽高的时候使用该方法会设置控件的宽高属性
     *
     * @param pathOrRes 图片或资源地址
     * @param imageView 图片控件
     * @param width     图片加载宽度
     * @param height    图片加载高度
     */
    public void loadingImageAndSetWidthHeight(Object pathOrRes, ImageView imageView, int width, int height) {
        this.loadingImageAndSetWidthHeight(pathOrRes, imageView, width, height, null);
    }

    /**
     * 加载网络图片，为了适应预先不知道图片宽高的情况，当不知道图片宽高的时候使用该方法会设置控件的宽高属性
     *
     * @param pathOrRes 图片或资源地址
     * @param imageView 图片控件
     * @param width     图片加载宽度
     * @param height    图片加载高度
     */
    public void loadingImageAndSetWidthHeight(Object pathOrRes, ImageView imageView, int width, int height, AcbflwImageLoadConfig config) {
        if (JtlwCheckVariateUtils.getInstance().isEmpty(pathOrRes) && imageView != null) {

            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(width, height);
            } else {
                layoutParams.width = width;
                layoutParams.height = height;
            }
            imageView.setLayoutParams(layoutParams);
            loadingImage(pathOrRes, imageView, width, height, config);
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
                if (((String) pathOrRes).matches(JtlwMatchesRegularCommon.EXP_URL_STR) || ((String) pathOrRes).matches(JtlwMatchesRegularCommon.EXP_URL_IP)) {
                    return LOADING_TYPE_FOR_NET;
                } else {
                    File file = new File((String) pathOrRes);
                    if (file.exists()) {
                        file = null;
                        return LOADING_TYPE_FOR_LOCAL;
                    } else {
                        file = null;
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
     * @param width     图片宽度
     * @param height    图片高度
     */
    public void loadingNetImage(String path, ImageView imageView, int width, int height) {
        this.loadingNetImage(path, imageView, width, height, null);
    }

    /**
     * 加载本地图片
     *
     * @param path      本地图片地址
     * @param imageView 图片控件
     * @param width     图片宽度
     * @param height    图片高度
     */
    public void loadingLocalImage(String path, ImageView imageView, int width, int height) {
        this.loadingLocalImage(path, imageView, width, height, null);
    }

    /**
     * 加载资源图片
     *
     * @param resId     资源图片id
     * @param imageView 图片控件
     * @param width     图片宽度
     * @param height    图片高度
     */
    public void loadingResImage(@DrawableRes int resId, ImageView imageView, int width, int height) {
        this.loadingResImage(resId, imageView, width, height, null);
    }

    /**
     * 加载网络图片
     *
     * @param path      网络图片地址
     * @param imageView 图片控件
     * @param width     图片宽度
     * @param height    图片高度
     * @param config  回调
     */
    public abstract void loadingNetImage(String path, ImageView imageView, int width, int height, AcbflwImageLoadConfig config);

    /**
     * 加载本地图片
     *
     * @param path      本地图片地址
     * @param imageView 图片控件
     * @param width     图片宽度
     * @param height    图片高度
     * @param config  回调
     */
    public abstract void loadingLocalImage(String path, ImageView imageView, int width, int height, AcbflwImageLoadConfig config);

    /**
     * 加载资源图片
     *
     * @param resId     资源图片id
     * @param imageView 图片控件
     * @param width     图片宽度
     * @param height    图片高度
     * @param config  回调
     */
    public abstract void loadingResImage(@DrawableRes int resId, ImageView imageView, int width, int height, AcbflwImageLoadConfig config);

    /**
     * 加载bitmap位图
     *
     * @param bitmap    位图
     * @param imageView 图片控件
     * @param width     宽度
     * @param height    高度
     */
    public abstract void loadingBitmapImage(Bitmap bitmap, ImageView imageView, int width, int height);

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
     * @param width  显示图片宽度
     * @param height 显示图片高度
     * @return 显示缩略图true
     */
    protected boolean isShowThumbnail(int width, int height) {
        //判断是否需要显示缩略图
        return width < showThumbnailMaxWidth && height < showThumbnailMaxHeight;
    }
}
