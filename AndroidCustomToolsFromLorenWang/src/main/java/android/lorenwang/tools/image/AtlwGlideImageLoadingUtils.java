package android.lorenwang.tools.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.lorenwang.tools.app.AtlwThreadUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import net.qiujuer.genius.graphics.Blur;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import javabase.lorenwang.tools.JtlwMatchesRegularCommon;
import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;

/**
 * 创建时间：2019-04-11 下午 14:29:15
 * 创建人：王亮（Loren wang）
 * 功能作用：glide图片加载工具，仅暴露出去方法，其gradle引入使用仅library使用的方式，其他框架使用需要单独引入gradle
 * 当前工具使用的glide的版本为4.8.0
 * 思路：使用一个map结合记录存储请求的RequestOptions，里面包含请求的错误图片以及加载中图片，此map的key为所有
 * 生成RequestOptions参数的集合字符串，值为RequestOptions，
 * 方法：1、获取请求的RequestOptions
 * 2、加载普通图片
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AtlwGlideImageLoadingUtils extends AtlwBaseImageLoading {
    private final String TAG = getClass().getName();
    private static volatile AtlwGlideImageLoadingUtils optionsInstance;

    private AtlwGlideImageLoadingUtils() {
    }

    public static AtlwGlideImageLoadingUtils getInstance() {
        if (optionsInstance == null) {
            synchronized (AtlwGlideImageLoadingUtils.class) {
                if (optionsInstance == null) {
                    optionsInstance = new AtlwGlideImageLoadingUtils();
                }
            }
        }
        return optionsInstance;
    }

    /**
     * 存储RequestOptions的键值对，为线程安全键值对，key为所有请求参数字符串集合
     */
    private Map<String, RequestOptions> requestOptionsMap = new ConcurrentHashMap<>();

    /**
     * 加载网址图片
     *
     * @param context        上下文
     * @param path           图片地址
     * @param requestOptions 请求操作配置类
     * @param imageView      图片控件
     */
    public void loadNetImage(Context context, String path, RequestOptions requestOptions, ImageView imageView) {
        //空判定
        if (JtlwCheckVariateUtils.getInstance().isHaveEmpty(context, path, requestOptions, imageView)
                && !path.matches(JtlwMatchesRegularCommon.EXP_URL_STR)) {
            return;
        }
        Glide.with(context).load(path).apply(requestOptions).into(imageView);
    }

    /**
     * 加载网址图片获取位图
     *
     * @param context        上下文
     * @param path           图片地址
     * @param requestOptions 请求操作配置类
     * @param callback       图片加载回调
     */
    public void loadNetImageGetBitmap(final Context context, final String path, final RequestOptions requestOptions, final OnImageLoadCallback callback) {
        //空判定
        if (JtlwCheckVariateUtils.getInstance().isHaveEmpty(context, path, requestOptions, callback)
                && !path.matches(JtlwMatchesRegularCommon.EXP_URL_STR)) {
            return;
        }
        AtlwThreadUtils.getInstance().postOnChildThread(new Runnable() {
            @Override
            public void run() {
                Bitmap bmp = null;
                try {
                    bmp = Glide.with(context).asBitmap().load(path).apply(requestOptions).submit().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                callback.onBitmap(bmp);
            }
        });
    }

    /**
     * 高斯模糊图片
     *
     * @param context          上下文
     * @param path             图片地址
     * @param requestOptions   请求操作配置类
     * @param imageView        图片加载控件
     * @param radius           模糊精度0--25,值越大，模糊度越高
     * @param canReuseInBitmap 是否在原始位图中重用
     */
    public void loadNetImageBlur(final Context context, final String path, final RequestOptions requestOptions, final ImageView imageView, final int radius, final boolean canReuseInBitmap) {
        //空判定
        if (JtlwCheckVariateUtils.getInstance().isHaveEmpty(context, path, requestOptions, imageView)
                && !path.matches(JtlwMatchesRegularCommon.EXP_URL_STR)) {
            return;
        }
        AtlwThreadUtils.getInstance().postOnChildThread(new Runnable() {
            Bitmap bmp = null;

            @Override
            public void run() {
                try {
                    //高斯模糊图片不能影响原图的使用，但是因为又要用到glide的缓存机制，所以自动给所有的图片加个后缀，保证呼和原图地址的缓存区分开
                    bmp = Glide.with(context).asBitmap().load(path + "?blur").apply(requestOptions).submit().get();
                    if (bmp != null) {
                        bmp = Blur.onStackBlurClip(bmp, radius);
                        if (bmp != null) {
                            AtlwThreadUtils.getInstance().postOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    imageView.setImageBitmap(bmp);
                                }
                            });
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 获取请求的RequestOptions
     *
     * @param errorResId      错误资源id
     * @param placeholderId   占位的资源id
     * @param imageScaleTypes 图片缩放数据
     * @return RequestOptions
     */
    public RequestOptions getRequestOptions(@DrawableRes int errorResId
            , @DrawableRes int placeholderId, ImageView.ScaleType... imageScaleTypes) {
        StringBuffer key = new StringBuffer(String.valueOf(errorResId)).append(String.valueOf(placeholderId));
        int length = 0;
        if (imageScaleTypes != null && (length = imageScaleTypes.length) > 0) {
            for (int i = 0; i < length; i++) {
                key.append(imageScaleTypes[i]);
            }
        }
        RequestOptions requestOptions = requestOptionsMap.get(key.toString());
        if (requestOptions == null) {
            requestOptions = new RequestOptions();
            //错误资源图片以及占位资源图片
            requestOptions = requestOptions.error(errorResId).placeholder(placeholderId);
            //缩放数据
            for (int i = 0; i < length; i++) {
                switch (imageScaleTypes[i]) {
                    case CENTER_CROP:
                        requestOptions = requestOptions.centerCrop();
                        break;
                    case CENTER_INSIDE:
                        requestOptions = requestOptions.centerInside();
                        break;
                    case FIT_CENTER:
                        requestOptions = requestOptions.fitCenter();
                        break;
                    default:
                        break;
                }
            }
            requestOptionsMap.put(key.toString(), requestOptions);
        }
        key.setLength(0);
        key = null;
        return requestOptions;
    }

    /**
     * 获取请求的RequestOptions
     *
     * @param errorDrawable       错误资源图片
     * @param placeholderDrawable 占位的资源图片
     * @param imageScaleTypes     图片缩放数据
     * @return RequestOptions
     */
    public RequestOptions getRequestOptions(@Nullable Drawable errorDrawable
            , @Nullable Drawable placeholderDrawable, ImageView.ScaleType... imageScaleTypes) {
        StringBuffer key = new StringBuffer().append(errorDrawable.hashCode()).append(placeholderDrawable.hashCode());
        int length = 0;
        if (imageScaleTypes != null && (length = imageScaleTypes.length) > 0) {
            for (int i = 0; i < length; i++) {
                key.append(imageScaleTypes[i]);
            }
        }
        RequestOptions requestOptions = requestOptionsMap.get(key.toString());
        if (requestOptions == null) {
            requestOptions = new RequestOptions();
            //错误资源图片以及占位资源图片
            requestOptions = requestOptions.error(errorDrawable).placeholder(placeholderDrawable);
            //缩放数据
            for (int i = 0; i < length; i++) {
                switch (imageScaleTypes[i]) {
                    case CENTER_CROP:
                        requestOptions = requestOptions.centerCrop();
                        break;
                    case CENTER_INSIDE:
                        requestOptions = requestOptions.centerInside();
                        break;
                    case FIT_CENTER:
                        requestOptions = requestOptions.fitCenter();
                        break;
                    default:
                        break;
                }
            }
            requestOptionsMap.put(key.toString(), requestOptions);
        }
        key.setLength(0);
        key = null;
        return requestOptions;
    }
}
