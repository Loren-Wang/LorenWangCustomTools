package android.lorenwang.netoptions.frame;

import android.Manifest;
import android.lorenwang.netoptions.NetworkOptionsCallback;
import android.lorenwang.netoptions.NetworkOptionsRecordDto;
import android.lorenwang.netoptions.common.AndJavaCommonUtils;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static android.lorenwang.netoptions.NetworkOptionsConstant.NETWORK_DATA_REQUEST_FAIL_CASE_TIME_INTERVALIN;
import static android.lorenwang.netoptions.NetworkOptionsConstant.NETWORK_DATA_REQUEST_FAIL_CASE_URL_NOT_EFFECTIVE;


/**
 * 创建时间：2018-12-17 下午 12:18:17
 * 创建人：王亮（Loren wang）
 * 功能作用：基础网络操作
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public abstract class BaseNetworkOptions {
    protected final String TAG = getClass().getName();
    protected Long timeOut = 15000l;//超时时间
    protected Long sameRequestUrlPathIntervel = 0l;//相同网址时间请求间隔
    protected String dataEncoding = "urf-8";//响应数据解析格式
    protected Map<String, String> reqHeads;//请求头集合
    //数据
    /**
     * 同一个网址以及参数在同一时间只能请求一次，期中的key值是网址以及参数tostring的集合体，记录着所有的网络请求，在
     * 网络请求完成的时候会移除相应的请求，同时在下一次的请求的时候会再次添加的
     */
    protected Map<String, NetworkOptionsRecordDto> requestRecordDtoMap = new HashMap<>();
    /**
     * 请求分类集合，该集合中的key值代表着requestActName所代表的界面或服务，而list集合中存储的是相应界面的所有网络请求的网址
     * 和参数的集合体，也就是每一个value的每一个参数都是requestRecordDtoMap的key值
     */
    protected Map<String, List<String>> requestClassifyMap = new HashMap<>();
    //主线程
    private Handler handlerMainThread = new Handler(Looper.getMainLooper());//主线程

    private Runnable onNetworkDataRequestFailRunnable;//网络数据请求失败runnable
    private Runnable onNetworkDataRequestSucessRunnable;//网络数据请求成功runnable
    private Runnable onNetworkDataRequestErrorRunnable;//网络数据请求异常runnable
    private Runnable onNetworkFileRequestFailRunnable;//网络文件上传下载请求失败runnable
    private Runnable onNetworkFileRequestSucessRunnable;//网络文件上传下载请求成功runnable
    private Runnable onNetworkFileRequestErrorRunnable;//网络文件上传下载请求异常runnable
    private Runnable onNetworkFileRequestProgressRunnable;//网络文件上传下载请求进度runnable


    /**
     * 基类初始化
     *
     * @param timeOut                    超时时间
     * @param sameRequestUrlPathIntervel 相同网址时间请求间隔
     * @param dataEncoding               响应数据解析格式
     * @param reqHeads                   请求头集合
     */
    protected void init(Long timeOut, Long sameRequestUrlPathIntervel
            , String dataEncoding, Map<String, String> reqHeads) {
        this.timeOut = timeOut;
        this.dataEncoding = dataEncoding;
        this.reqHeads = reqHeads;
    }

    /**
     * 获取对于缓存记录的key
     *
     * @param requestActName
     * @param requestPath
     * @param paramsMap
     * @return
     */
    protected String getRecordKey(String requestActName, String requestPath, Map<String, Object> paramsMap) {
        return requestActName + requestPath + (paramsMap != null ? paramsMap.toString() : "");
    }

    /**
     * 检测url请求网址的合法性
     *
     * @param requestPath 被检测的网址
     * @return true : 有效 false：无效
     */
    protected boolean isUrlEffective(String requestPath) {
        if (requestPath == null || "".equals(requestPath) || requestPath.indexOf("http") != 0) {
            Log.i(TAG, "网址无效，被检测网址（" + requestPath + ")");
            return false;
        } else {
            Log.i(TAG, "网址有效，被检测网址（" + requestPath + ")");
            return true;
        }
    }

    /**
     * 检测是否在网络请求间隔时间内
     *
     * @param requestRecordDtoKey 网络请求记录集合key
     * @return true ： 在事件内，false ： 在事件外
     */
    protected boolean isTimeIntervalIn(String requestRecordDtoKey) {
        NetworkOptionsRecordDto networkRequestRecordDto = requestRecordDtoMap.get(requestRecordDtoKey);
        if (networkRequestRecordDto != null) {
            if (AndJavaCommonUtils.getInstance().getMillisecond() - networkRequestRecordDto.requestTime <= sameRequestUrlPathIntervel) {
                Log.i(TAG, "不在相同网址请求时间间隔外，通过检测");
                return true;
            } else {
                Log.i(TAG, "在相同网址请求时间间隔外，不通过检测");
                return false;
            }
        } else {
            Log.i(TAG, "未进行过相同网址请求，不通过检测");
            return false;
        }
    }

    /**
     * 检测在之前是否有相同的网络请求未完成
     *
     * @param requestRecordDtoKey 网络请求记录集合key
     * @return
     */
    protected void isRecordSaveAlikeRequest(String requestRecordDtoKey) {
        NetworkOptionsRecordDto networkRequestRecordDto = requestRecordDtoMap.get(requestRecordDtoKey);
        if (networkRequestRecordDto != null) {
            if (networkRequestRecordDto.isRequestFinish) {
                Log.i(TAG, "存在了相同的网址请求，但该网址请求已完成，通过检测");
            } else {
                isRecordSaveAlikeRequest(networkRequestRecordDto.requestUtil);
                Log.i(TAG, "请求检测交给子类");
            }
        } else {
            Log.i(TAG, "未进行过相同网址请求，通过检测");
        }
    }

    /**
     * 记录网络请求
     *
     * @param requestActName         上下文
     * @param requestPath            请求网址
     * @param requestType            请求类型
     * @param paramsMapHeader
     * @param paramsMap              post请求参数
     * @param networkRequestCallback 请求返回值
     * @param isFrontRequest         是否是前台的请求
     * @param call                   网络请求请求体
     */
    protected void recordNetworkRequest(String requestActName, String requestRecordDtoKey,
                                        String requestPath, String requestType, Map<String, Object> paramsMapHeader, Map<String, Object> paramsMap,
                                        String jsonStr, Object networkRequestCallback, boolean isFrontRequest, Call call) {
        NetworkOptionsRecordDto networkRequestRecordDto = new NetworkOptionsRecordDto(requestType, requestPath, paramsMapHeader, paramsMap, jsonStr, call, requestActName
                , AndJavaCommonUtils.getInstance().getMillisecond(), isFrontRequest, networkRequestCallback);
        //存储记录请求内容
        requestRecordDtoMap.put(requestRecordDtoKey, networkRequestRecordDto);
        //记录页面请求
        List<String> list = requestClassifyMap.get(requestActName);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(requestRecordDtoKey);
        requestClassifyMap.put(requestActName, list);
        Log.d(TAG, "requestInfo :::" + networkRequestRecordDto.toString());
    }

    /**
     * 检测所有信息
     *
     * @param requestPath            请求地址
     * @param requestRecordDtoKey    请求记录key
     * @param isCheckInterval        是否检测时间间隔
     * @param networkRequestCallback 请求回调
     * @return
     */
    protected boolean checkAllInfo(final String requestPath, String requestRecordDtoKey, boolean isCheckInterval
            , final NetworkOptionsCallback networkRequestCallback) {
//        if(!AppCommon.netIsConnection){
//            onNetworkRequestFail(null,NETWORK_DATA_REQUEST_NOT_CONNECTION_NET,networkRequestCallback);
//            return;
//        }
        //检测网址是否有效
        if (!isUrlEffective(requestPath)) {
            onNetworkRequestFail(null, NETWORK_DATA_REQUEST_FAIL_CASE_URL_NOT_EFFECTIVE, networkRequestCallback);
            return false;
        }
        //时间间隔检测
        if (isCheckInterval && isTimeIntervalIn(requestRecordDtoKey)) {
            onNetworkRequestFail(null, NETWORK_DATA_REQUEST_FAIL_CASE_TIME_INTERVALIN, networkRequestCallback);
            return false;
        }
        //检测之前是否有相同的网络请求未完成，如果有的话则取消请求
        isRecordSaveAlikeRequest(requestRecordDtoKey);
        return true;
    }

    /**************************************请求回调****************************************/

    /**
     * 网络请求成功
     *
     * @param requestActName      上下文
     * @param requestRecordDtoKey 请求数据记录key
     * @param data                网络请求返回的数据
     * @param callback            请求回调
     */
    protected void onNetworkRequestSuccess(final String requestActName, final Object object, final String requestRecordDtoKey
            , final String data, final NetworkOptionsCallback callback) {
        final NetworkOptionsRecordDto networkRequestRecordDto = requestRecordDtoMap.get(requestRecordDtoKey);
        if (callback != null) {
            onNetworkDataRequestSucessRunnable = new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "network resultData  :" + data);
                    callback.successForData(data, object);
                }
            };
            postMainRunnable(onNetworkDataRequestSucessRunnable, null);
        } else {
            //更新记录数据,只能在最后使用完成更新
            networkRequestRecordDto.resultData = data;
            requestRecordDtoMap.put(requestRecordDtoKey, networkRequestRecordDto);
        }
    }

    /**
     * 网络请求之数据交互请求出错
     *
     * @param object
     * @param statusCode             请求状态码
     * @param networkRequestCallback 请求回调
     */
    protected void onNetworkRequestError(final Object object, final Integer statusCode, final NetworkOptionsCallback networkRequestCallback) {
        if (networkRequestCallback != null) {
            onNetworkDataRequestErrorRunnable = new Runnable() {
                public void run() {
                    networkRequestCallback.error(statusCode, object);
                }
            };
            postMainRunnable(onNetworkDataRequestErrorRunnable, null);
        }
    }

    /**
     * 网络在请求时失败
     *
     * @param failCase               失败原因
     * @param networkRequestCallback 请求回调
     */
    protected void onNetworkRequestFail(final Object object, final int failCase, final NetworkOptionsCallback networkRequestCallback) {
        if (null != networkRequestCallback) {
            onNetworkDataRequestFailRunnable = new Runnable() {
                public void run() {
                    networkRequestCallback.fail(failCase, object);
                }
            };
            postMainRunnable(onNetworkDataRequestFailRunnable, null);
        }
    }

    /**
     * 文件上传下载进度回调
     *
     * @param object
     * @param progress               0-100
     * @param networkRequestCallback
     */
    protected void onNetworkFileRequestProgress(final String filePath, final Object object, final int progress
            , final NetworkOptionsCallback networkRequestCallback) {
        if (null != networkRequestCallback) {
            onNetworkFileRequestProgressRunnable = new Runnable() {
                public void run() {
                    Log.d(TAG, "当前文件传输进度:::" + progress);
                    networkRequestCallback.progress(progress);
                }
            };
            postMainRunnable(onNetworkFileRequestProgressRunnable, null);
        }
    }

    /**
     * 向主线程发送runnable
     *
     * @param runnable
     * @param delayed  为空时不做延时
     */
    private void postMainRunnable(Runnable runnable, Long delayed) {
        if (delayed != null) {
            try {
                handlerMainThread.postDelayed(runnable, delayed);
            } catch (Exception e) {
                handlerMainThread = new Handler(Looper.myLooper());
                handlerMainThread.postDelayed(runnable, delayed);
            }
        } else {
            try {
                handlerMainThread.post(runnable);
            } catch (Exception e) {
                handlerMainThread = new Handler(Looper.myLooper());
                handlerMainThread.post(runnable);
            }

        }
    }


    /*******************************************请求处理********************************************/


    /**
     * 取消某一个请求
     *
     * @param requestActName
     * @param url
     * @param paramsMap
     */
    public abstract void remNetReq(String requestActName, String url, Map<String, Object> paramsMap);

    /**
     * 移除指定页面的所有请求存储
     *
     * @param requestActName
     */
    public abstract void remActReq(@NonNull String requestActName);

    /**
     * 检测在之前是否有相同的网络请求未完成,如果有则取消上一次请求
     *
     * @param requestUtil
     */
    protected abstract void isRecordSaveAlikeRequest(Object requestUtil);


    /**********************************************发起请求*****************************************/

    /**
     * 字符串get请求
     *
     * @param requestActName         请求的上下文请求名称
     * @param requestPath            请求网址
     * @param object                 传递的实体参数
     * @param networkRequestCallback 请求回调
     * @param isCheckInterval        是否检查时间间隔
     * @param isFrontRequest         是否是前台的请求
     */
    public abstract void stringRequestForGet(String requestActName, String requestPath, final Map<String, Object> paramsMapHeader
            , Object object, NetworkOptionsCallback networkRequestCallback, boolean isCheckInterval, boolean isFrontRequest);

    /**
     * 字符串post请求
     *
     * @param requestActName         请求的上下文请求名称
     * @param requestPath            请求网址
     * @param paramsMap              map请求参数
     * @param object                 传递的实体参数
     * @param networkRequestCallback 请求回调
     * @param isCheckInterval        是否检查时间间隔
     * @param isFrontRequest         是否是前台的请求
     */
    public abstract void stringRequestForPost(String requestActName, String requestPath, final Map<String, Object> paramsMapHeader
            , Map<String, Object> paramsMap, Object object, NetworkOptionsCallback networkRequestCallback, boolean isCheckInterval, boolean isFrontRequest);

    /**
     * 字符串post请求
     *
     * @param requestActName         请求的上下文请求名称
     * @param requestPath            请求网址
     * @param jsonStr                请求的json数据
     * @param object                 传递的实体参数
     * @param networkRequestCallback 请求回调
     * @param isCheckInterval        是否检查时间间隔
     * @param isFrontRequest         是否是前台的请求
     */
    public abstract void jsonRequestForPost(String requestActName, String requestPath, final Map<String, Object> paramsMapHeader
            , String jsonStr, Object object, NetworkOptionsCallback networkRequestCallback, boolean isCheckInterval, boolean isFrontRequest);

    /**
     * delete请求
     *
     * @param requestActName         请求的上下文请求名称
     * @param requestPath            请求网址
     * @param paramsMap              请求的map数据
     * @param object                 传递的实体参数
     * @param networkRequestCallback 请求回调
     * @param isCheckInterval        是否检查时间间隔
     * @param isFrontRequest         是否是前台的请求
     */
    public abstract void requestForDelete(String requestActName, String requestPath, final Map<String, Object> paramsMapHeader
            , Map<String, Object> paramsMap, Object object, NetworkOptionsCallback networkRequestCallback, boolean isCheckInterval, boolean isFrontRequest);

    /**
     * put请求
     *
     * @param requestActName         请求的上下文请求名称
     * @param requestPath            请求网址
     * @param paramsMap              请求的map数据
     * @param object                 传递的实体参数
     * @param networkRequestCallback 请求回调
     * @param isCheckInterval        是否检查时间间隔
     * @param isFrontRequest         是否是前台的请求
     */
    public abstract void requestForPut(String requestActName, String requestPath, final Map<String, Object> paramsMapHeader
            , Map<String, Object> paramsMap, Object object, NetworkOptionsCallback networkRequestCallback, boolean isCheckInterval, boolean isFrontRequest);


    /***********************************文件下载请求****************************************/
    /**
     * 下载文件
     *
     * @param requestActName         请求的页面名成
     * @param requestPath            请求地址
     * @param savePath               保存地址路径
     * @param object                 传递的实体参数
     * @param isFrontRequest         是否是前台请求
     * @param networkRequestCallback 请求回调
     */

    @RequiresPermission(allOf = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public abstract void downLoadFileRequest(final String requestActName, final String requestPath, final Map<String, Object> paramsMapHeader
            , final String savePath, final Object object, boolean isFrontRequest, final NetworkOptionsCallback networkRequestCallback);
}
