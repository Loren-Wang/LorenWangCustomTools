package android.lorenwang.netoptions.frame;

import android.lorenwang.netoptions.BuildConfig;
import android.lorenwang.netoptions.NetworkOptionsCallback;
import android.lorenwang.netoptions.NetworkOptionsRecordDto;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.lorenwang.netoptions.NetworkOptionsConstant.NETWORK_DATA_REQUEST_ERROR;
import static android.lorenwang.netoptions.NetworkOptionsConstant.NETWORK_DATA_REQUEST_ERROR_TYPE_DOWN_ERROR;
import static android.lorenwang.netoptions.NetworkOptionsConstant.NETWORK_DATA_REQUEST_FAIL_CASE_REQUEST_FAIL;
import static android.lorenwang.netoptions.NetworkOptionsConstant.NETWORK_REQUEST_FOR_DELETE;
import static android.lorenwang.netoptions.NetworkOptionsConstant.NETWORK_REQUEST_FOR_DOWN_LOAD_FILE;
import static android.lorenwang.netoptions.NetworkOptionsConstant.NETWORK_REQUEST_FOR_GET;
import static android.lorenwang.netoptions.NetworkOptionsConstant.NETWORK_REQUEST_FOR_POST;
import static android.lorenwang.netoptions.NetworkOptionsConstant.NETWORK_REQUEST_FOR_POST_JSON;
import static android.lorenwang.netoptions.NetworkOptionsConstant.NETWORK_REQUEST_FOR_PUT;


/**
 * 创建时间：2018-12-17 下午 12:15:2
 * 创建人：王亮（Loren wang）
 * 功能作用：OkHttp操作工具类
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class OkHttpOptionsUtils extends BaseNetworkOptions {
    private static OkHttpOptionsUtils okHttpOptionsUtils;
    private OkHttpClient okHttpClient;

    private OkHttpOptionsUtils() {
    }

    public static OkHttpOptionsUtils getInstance() {
        if (okHttpOptionsUtils == null) {
            okHttpOptionsUtils = new OkHttpOptionsUtils();
        }
        return okHttpOptionsUtils;
    }

    /**
     * 基类初始化
     *
     * @param timeOut                    超时时间
     * @param sameRequestUrlPathIntervel 相同网址时间请求间隔
     * @param dataEncoding               响应数据解析格式
     * @param reqHeads                   请求头集合
     */
    public void init(Long timeOut, Long sameRequestUrlPathIntervel
            , String dataEncoding, final Map<String, String> reqHeads) {
        super.init(timeOut, sameRequestUrlPathIntervel, dataEncoding, reqHeads);
        try {
            X509TrustManager trustManager = null;
            SSLSocketFactory sslSocketFactory = null;
            try {
                trustManager = new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                };
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, new TrustManager[]{trustManager}, new java.security.SecureRandom());
                sslSocketFactory = sc.getSocketFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }


            //创建构建器
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            if (reqHeads != null) {
                                Request.Builder builder = null;
                                Iterator<Map.Entry<String, String>> iterator = reqHeads.entrySet().iterator();
                                Map.Entry<String, String> item;
                                while (iterator.hasNext()) {
                                    item = iterator.next();
                                    if (builder == null) {
                                        builder = request.newBuilder();
                                    }
                                    builder.addHeader(item.getKey(), item.getValue());
                                }
                                request = builder.build();
                            }
                            return chain.proceed(request);
                        }
                    })
                    .connectTimeout(timeOut, TimeUnit.MILLISECONDS)
                    .writeTimeout(timeOut, TimeUnit.MILLISECONDS)
                    .readTimeout(timeOut, TimeUnit.MILLISECONDS);

            if (trustManager == null || sslSocketFactory == null) {
                //设置请求头
                okHttpClient = builder.build();
            } else {
                //设置请求头
                okHttpClient = builder
                        .sslSocketFactory(sslSocketFactory, trustManager)
                        .build();
            }

        } catch (Exception e) {
            Log.d(TAG, "超时时间及请求头设置失败");
            okHttpClient = new OkHttpClient();
        }
    }

    @Override
    public void remNetReq(String requestActName, String url, Map<String, Object> paramsMap) {
        String recordKey = getRecordKey(requestActName, url, paramsMap);
        NetworkOptionsRecordDto networkRequestRecordDto = requestRecordDtoMap.get(recordKey);
        if (networkRequestRecordDto != null
                && networkRequestRecordDto.requestUtil != null
                && networkRequestRecordDto.requestUtil instanceof Call) {
            ((Call) networkRequestRecordDto.requestUtil).cancel();
        }
    }

    @Override
    public void remActReq(@NonNull String requestActName) {
        List<String> list = requestClassifyMap.get(requestActName);
        if (list != null) {
            Iterator<String> iterator = list.iterator();
            NetworkOptionsRecordDto networkRequestRecordDto;
            while (iterator.hasNext()) {
                networkRequestRecordDto = requestRecordDtoMap.get(iterator.next());
                if (networkRequestRecordDto != null
                        && networkRequestRecordDto.requestUtil != null
                        && networkRequestRecordDto.requestUtil instanceof Call) {
                    ((Call) networkRequestRecordDto.requestUtil).cancel();
                }
                requestRecordDtoMap.remove(networkRequestRecordDto);
                networkRequestRecordDto = null;
            }
            iterator = null;

            list.clear();
            list = null;
        }
    }

    @Override
    public void isRecordSaveAlikeRequest(Object requestUtil) {
        if (requestUtil != null && requestUtil instanceof Call) {
            ((Call) requestUtil).cancel();
        }
        Log.i(TAG, "存在了相同的网址请求，但该网址请求未完成，取消之前的请求信息");
    }

    @Override
    public void stringRequestForGet(String requestActName, String requestPath, final Map<String, Object> paramsMapHeader, Object object
            , NetworkOptionsCallback networkRequestCallback, boolean isCheckInterval, boolean isFrontRequest) {
        networkDataRequest(requestActName, requestPath, NETWORK_REQUEST_FOR_GET, paramsMapHeader, null,
                null, object, networkRequestCallback, isCheckInterval, isFrontRequest);
    }

    @Override
    public void stringRequestForPost(String requestActName, String requestPath, Map<String, Object> paramsMapHeader, Map<String, Object> paramsMap, Object object, NetworkOptionsCallback networkRequestCallback, boolean isCheckInterval, boolean isFrontRequest) {
        networkDataRequest(requestActName, requestPath, NETWORK_REQUEST_FOR_POST, paramsMapHeader, paramsMap, null,
                object, networkRequestCallback, isCheckInterval, isFrontRequest);
    }

    @Override
    public void jsonRequestForPost(String requestActName, String requestPath, Map<String, Object> paramsMapHeader
            , String jsonStr, Object object, NetworkOptionsCallback networkRequestCallback
            , boolean isCheckInterval, boolean isFrontRequest) {
        networkDataRequest(requestActName, requestPath, NETWORK_REQUEST_FOR_POST_JSON, paramsMapHeader, null,
                jsonStr, object, networkRequestCallback, isCheckInterval, isFrontRequest);
    }

    @Override
    public void requestForDelete(String requestActName, String requestPath, Map<String, Object> paramsMapHeader
            , Map<String, Object> paramsMap, Object object, NetworkOptionsCallback networkRequestCallback
            , boolean isCheckInterval, boolean isFrontRequest) {
        networkDataRequest(requestActName, requestPath, NETWORK_REQUEST_FOR_DELETE, paramsMapHeader, paramsMap,
                null, object, networkRequestCallback, isCheckInterval, isFrontRequest);
    }

    @Override
    public void requestForPut(String requestActName, String requestPath, Map<String, Object> paramsMapHeader, Map<String, Object> paramsMap, Object object, NetworkOptionsCallback networkRequestCallback, boolean isCheckInterval, boolean isFrontRequest) {
        networkDataRequest(requestActName, requestPath, NETWORK_REQUEST_FOR_PUT, paramsMapHeader, paramsMap,
                null, object, networkRequestCallback, isCheckInterval, isFrontRequest);
    }


    /**
     * 网路请求综合
     *
     * @param requestActName         请求上下文
     * @param requestPath            请求网址
     * @param requestType            请求类型，get or post
     * @param paramsMap              post请求集合
     * @param jsonStr                如果是postjson请求时就传递json字符串
     * @param object
     * @param networkRequestCallback 请求回调
     * @param isCheckInterval        是否检查忽略时间，检查忽略时间的前提是要已经有请求过的数据
     * @param isFrontRequest         是否是前台的请求
     */
    private void networkDataRequest(String requestActName, final String requestPath, String requestType
            , final Map<String, Object> paramsMapHeader, final Map<String, Object> paramsMap, String jsonStr
            , final Object object, final NetworkOptionsCallback networkRequestCallback, boolean isCheckInterval, boolean isFrontRequest) {
        final String requestRecordDtoKey = getRecordKey(requestActName, requestPath, paramsMap);
        if (requestActName == null) {
            requestActName = BuildConfig.APPLICATION_ID;
        }
        if (!checkAllInfo(requestPath, requestRecordDtoKey, isCheckInterval, networkRequestCallback)) {
            return;
        }

        //构造网络请求
        Request.Builder build = null;
        switch (requestType) {
            case NETWORK_REQUEST_FOR_GET:
                build = new Request.Builder().url(requestPath);
                break;
            case NETWORK_REQUEST_FOR_POST_JSON:
                if (jsonStr != null) {
                    build = new Request.Builder().url(requestPath)
                            .post(FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr));
                }
                break;
            case NETWORK_REQUEST_FOR_POST:
            case NETWORK_REQUEST_FOR_DELETE:
            case NETWORK_REQUEST_FOR_PUT:
                if (paramsMap != null) {
                    //创建请求的参数body
                    FormBody.Builder formBuilder = new FormBody.Builder();
                    for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                        if (entry.getKey() != null && entry.getValue() != null
                                && !"".equals(entry.getKey()) && !"".equals(entry.getValue())) {
                            formBuilder.add(entry.getKey(), String.valueOf(entry.getValue()));
                        }
                    }
                    switch (requestType) {
                        case NETWORK_REQUEST_FOR_POST:
                            build = new Request.Builder().url(requestPath).post(formBuilder.build());
                            break;
                        case NETWORK_REQUEST_FOR_DELETE:
                            build = new Request.Builder().url(requestPath).delete(formBuilder.build());
                            break;
                        case NETWORK_REQUEST_FOR_PUT:
                            build = new Request.Builder().url(requestPath).put(formBuilder.build());
                            break;
                        default:
                            break;
                    }
                }
                break;
            default:
                break;
        }
        if (paramsMapHeader != null) {
            for (Map.Entry<String, Object> entry : paramsMapHeader.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null
                        && !"".equals(entry.getKey()) && !"".equals(entry.getValue())) {
                    build.addHeader(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
        }


        if (build != null) {
            Call call = okHttpClient.newCall(build.build());
            //记录网络请求
            recordNetworkRequest(requestActName, requestRecordDtoKey, requestPath, requestType, paramsMapHeader, paramsMap, jsonStr, networkRequestCallback, isFrontRequest, call);
            final String finalRequestActName = requestActName;
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    finishRequestAfterRecord(call);//完成网络请求后对于记录的修改
                    if (!call.isCanceled()) {
                        if (e != null && e.getMessage() != null) {
                            Log.d(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        onNetworkRequestFail(object, NETWORK_DATA_REQUEST_FAIL_CASE_REQUEST_FAIL, networkRequestCallback);
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    finishRequestAfterRecord(call);//完成网络请求后对于记录的修改
                    if (!call.isCanceled()) {
                        try {
                            Log.d(TAG, response.toString());
                            if (response.isSuccessful()) {
                                onNetworkRequestSuccess(finalRequestActName, object, requestRecordDtoKey, new String(response.body().string().getBytes(), dataEncoding), networkRequestCallback);
                            } else {
                                onNetworkRequestError(object, NETWORK_DATA_REQUEST_ERROR, networkRequestCallback);
                            }
                        } catch (Exception e) {
                            onNetworkRequestError(object, NETWORK_DATA_REQUEST_ERROR, networkRequestCallback);
                        }
                    }
                }

                /**
                 * 完成网络请求后对于记录的修改
                 * @param call
                 */
                private void finishRequestAfterRecord(Call call) {
                    NetworkOptionsRecordDto networkRequestRecordDto = requestRecordDtoMap.get(requestRecordDtoKey);
                    networkRequestRecordDto.isRequestFinish = true;
                    networkRequestRecordDto.requestUtil = call;
                    requestRecordDtoMap.put(requestRecordDtoKey, networkRequestRecordDto);
                }
            });
        }
    }

    @Override
    public void downLoadFileRequest(String requestActName, final String requestPath, Map<String, Object> paramsMapHeader, final String savePath, final Object object, boolean isFrontRequest, final NetworkOptionsCallback networkRequestCallback) {
        final String requestRecordDtoKey = getRecordKey(requestActName, requestPath, null);
        if (requestActName == null) {
            requestActName = BuildConfig.APPLICATION_ID;
        }
        //判断保存地址是否为空
        if (TextUtils.isEmpty(savePath)) {
            return;
        }
        if (!checkAllInfo(requestPath, requestRecordDtoKey, false, networkRequestCallback)) {
            return;
        }
        //开启下载
        Request.Builder build = new Request.Builder().url(requestPath);
        if (paramsMapHeader != null) {
            for (Map.Entry<String, Object> entry : paramsMapHeader.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null
                        && !"".equals(entry.getKey()) && !"".equals(entry.getValue())) {
                    build.addHeader(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
        }
        Call call = okHttpClient.newCall(build.build());
        //记录网络请求
        recordNetworkRequest(requestActName, requestRecordDtoKey, requestPath, NETWORK_REQUEST_FOR_DOWN_LOAD_FILE, paramsMapHeader, null, null, networkRequestCallback, isFrontRequest, call);
        final String finalRequestActName = requestActName;
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                finishRequestAfterRecord(call);//完成网络请求后对于记录的修改
                if (!call.isCanceled()) {
                    if (e != null && e.getMessage() != null) {
                        Log.d(TAG, savePath + "文件下载失败  :" + e.getMessage());
                        e.printStackTrace();
                    }
                    onNetworkRequestFail(null, NETWORK_DATA_REQUEST_FAIL_CASE_REQUEST_FAIL, networkRequestCallback);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                finishRequestAfterRecord(call);//完成网络请求后对于记录的修改
                if (!call.isCanceled()) {
                    try {
                        Log.d(TAG, response.toString());
                        if (response.isSuccessful()) {
                            saveFile(response);
                        } else {
                            onNetworkRequestError(object, NETWORK_DATA_REQUEST_ERROR, networkRequestCallback);
                        }
                    } catch (Exception e) {
                        onNetworkRequestError(object, NETWORK_DATA_REQUEST_ERROR, networkRequestCallback);
                    }
                }

            }

            /**
             * 保存文件
             * @param response
             */
            private void saveFile(Response response) {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath);
                    //如果不存在同时没有创建成功的话
                    if (!file.getParentFile().exists()
                            && !file.getParentFile().mkdirs()) {
                        return;
                    }
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        onNetworkFileRequestProgress(requestPath, object, progress, networkRequestCallback);
                        fos.flush();
                    }
                    Log.d(TAG, savePath + "文件下载成功");
                    if (file.exists()) {
                        file = null;
                        onNetworkRequestSuccess(finalRequestActName, object, requestRecordDtoKey, "", networkRequestCallback);
                    } else {
                        onNetworkRequestError(object, NETWORK_DATA_REQUEST_ERROR_TYPE_DOWN_ERROR, networkRequestCallback);
                    }
                } catch (Exception e) {
                    Log.d(TAG, savePath + "文件下载失败  :" + e.getMessage());
                    onNetworkRequestError(object, NETWORK_DATA_REQUEST_ERROR_TYPE_DOWN_ERROR, networkRequestCallback);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                            is = null;
                        }
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) {
                            fos.close();
                            fos = null;
                        }
                    } catch (IOException e) {
                    }
                }
            }

            /**
             * 完成网络请求后对于记录的修改
             * @param call
             */
            private void finishRequestAfterRecord(Call call) {
                NetworkOptionsRecordDto networkRequestRecordDto = requestRecordDtoMap.get(requestRecordDtoKey);
                networkRequestRecordDto.isRequestFinish = true;
                networkRequestRecordDto.requestUtil = call;
                requestRecordDtoMap.put(requestRecordDtoKey, networkRequestRecordDto);
            }
        });
    }

}
