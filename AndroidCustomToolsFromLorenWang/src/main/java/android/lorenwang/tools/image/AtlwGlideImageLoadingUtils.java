package android.lorenwang.tools.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.lorenwang.tools.common.AtlwAndJavaCommonUtils;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javabase.lorenwang.tools.MatchesRegularCommon;

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

public class AtlwGlideImageLoadingUtils {
    private String TAG = "AtlwGlideImageLoadingUtils";
    private static AtlwGlideImageLoadingUtils utils;

    public static AtlwGlideImageLoadingUtils getInstance() {
        if (utils == null) {
            utils = new AtlwGlideImageLoadingUtils();
        }
        return utils;
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
        if (AtlwAndJavaCommonUtils.getInstance().isHaveEmpty(context, path, requestOptions, imageView)
                && !path.matches(MatchesRegularCommon.EXP_URL_STR)) {
            return;
        }
        Glide.with(context).load(path).apply(requestOptions).into(imageView);
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
