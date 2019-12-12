package android.lorenwang.netoptions;

import android.Manifest;
import android.lorenwang.netoptions.frame.BaseNetworkOptions;
import android.lorenwang.netoptions.frame.OkHttpOptionsUtils;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import android.text.TextUtils;

import java.util.Map;

/**
 * 创建时间：2018-12-17 下午 12:08:44
 * 创建人：王亮（Loren wang）
 * 功能作用：${DESCRIPTION}
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class NetworkOptionsUtils extends BaseNetworkOptions {
    private final String TAG = getClass().getName();
    private static NetworkOptionsUtils networkOptionsUtils;
    private BaseNetworkOptions nowNetworkOptions;

    private NetworkOptionsUtils() {
    }

    public static NetworkOptionsUtils getInstance() {
        if (networkOptionsUtils == null) {
            networkOptionsUtils = new NetworkOptionsUtils();
        }
        return networkOptionsUtils;
    }

    /**
     * 基类初始化
     *
     * @param optionsFrame               操作的框架名称
     * @param timeOut                    超时时间
     * @param sameRequestUrlPathIntervel 相同网址时间请求间隔
     * @param dataEncoding               响应数据解析格式
     * @param reqHeads                   请求头集合
     * @param isDebug                    是否开启debug
     */
    public void init(String optionsFrame, Long timeOut, Long sameRequestUrlPathIntervel
            , String dataEncoding, Map<String, String> reqHeads, boolean isDebug) {
        //如果请求框架为空则默认使用okhttp框架
        if (TextUtils.isEmpty(optionsFrame)) {
            optionsFrame = "okhttp";
        }
        //判断请求框架
        switch (optionsFrame) {
            case "okhttp":
            default:
                nowNetworkOptions = OkHttpOptionsUtils.getInstance();
                break;
        }
        ((OkHttpOptionsUtils) nowNetworkOptions).init(timeOut, sameRequestUrlPathIntervel, dataEncoding, reqHeads);
    }

    /**
     * 取消某一个请求
     *
     * @param requestActName
     * @param url
     * @param paramsMap
     */
    @Override
    public void remNetReq(String requestActName, String url, Map<String, Object> paramsMap) {
        nowNetworkOptions.remNetReq(requestActName, url, paramsMap);
    }

    /**
     * 移除指定页面的所有请求存储
     *
     * @param requestActName
     */
    @Override
    public void remActReq(@NonNull String requestActName) {
        nowNetworkOptions.remActReq(requestActName);
    }

    @Override
    protected void isRecordSaveAlikeRequest(Object requestUtil) {
    }

    @Override
    public void stringRequestForGet(String requestActName, String requestPath, Map<String, Object> paramsMapHeader, Object object, NetworkOptionsCallback networkRequestCallback, boolean isCheckInterval, boolean isFrontRequest) {
        nowNetworkOptions.stringRequestForGet(requestActName, requestPath, paramsMapHeader, object, networkRequestCallback
                , isCheckInterval, isFrontRequest);
    }

    @Override
    public void stringRequestForPost(String requestActName, String requestPath, Map<String, Object> paramsMapHeader, Map<String, Object> paramsMap, Object object, NetworkOptionsCallback networkRequestCallback, boolean isCheckInterval, boolean isFrontRequest) {
        nowNetworkOptions.stringRequestForPost(requestActName, requestPath, paramsMapHeader, paramsMap, object,
                networkRequestCallback, isCheckInterval, isFrontRequest);
    }

    @Override
    public void jsonRequestForPost(String requestActName, String requestPath, Map<String, Object> paramsMapHeader, String jsonStr, Object object, NetworkOptionsCallback networkRequestCallback, boolean isCheckInterval, boolean isFrontRequest) {
        nowNetworkOptions.jsonRequestForPost(requestActName, requestPath, paramsMapHeader, jsonStr, object,
                networkRequestCallback, isCheckInterval, isFrontRequest);
    }

    @Override
    public void requestForDelete(String requestActName, String requestPath, Map<String, Object> paramsMapHeader, Map<String, Object> paramsMap, Object object, NetworkOptionsCallback networkRequestCallback, boolean isCheckInterval, boolean isFrontRequest) {
        nowNetworkOptions.requestForDelete(requestActName, requestPath, paramsMapHeader, paramsMap, object,
                networkRequestCallback, isCheckInterval, isFrontRequest);
    }

    @Override
    public void requestForPut(String requestActName, String requestPath, Map<String, Object> paramsMapHeader, Map<String, Object> paramsMap, Object object, NetworkOptionsCallback networkRequestCallback, boolean isCheckInterval, boolean isFrontRequest) {
        nowNetworkOptions.requestForPut(requestActName, requestPath, paramsMapHeader, paramsMap, object,
                networkRequestCallback, isCheckInterval, isFrontRequest);
    }

    @Override
    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void downLoadFileRequest(String requestActName, String requestPath, Map<String, Object> paramsMapHeader, String savePath, Object object, boolean isFrontRequest, NetworkOptionsCallback networkRequestCallback) {
        nowNetworkOptions.downLoadFileRequest(requestActName, requestPath, paramsMapHeader, savePath, object, isFrontRequest, networkRequestCallback);
    }

}
