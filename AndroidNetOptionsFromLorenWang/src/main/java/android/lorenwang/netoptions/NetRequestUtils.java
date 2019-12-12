package android.lorenwang.netoptions;

import android.lorenwang.netoptions.common.AndJavaCommonUtils;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by LorenWang on 2018/7/23.
 * 创建时间：2018/7/23 下午 03:30
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public class NetRequestUtils {
    private static String TAG;
    private static NetRequestUtils netRequestUtils;
    public static NetRequestUtils getInstance(){
        if(netRequestUtils == null){
            netRequestUtils = new NetRequestUtils();
        }
        return netRequestUtils;
    }

    private static final int TIMEOUT;  // 超时时间
    private static final long SAME_REQUEST_URL_PATH_INTERVEL;//相同网址请求间隔
    private static final String RESPONSE_RESULE_CHARACTER_ENCODING_DEFAULT;//默认的返回数据的解析格式
    private static final String NETWORK_REQUEST_TYPE_GET = "GET";//网络请求类型之get请求
    private static final String NETWORK_REQUEST_TYPE_POST = "POST";//网络请求类型之post请求
    private static final String NETWORK_REQUEST_TYPE_POST_JSON = "POST_JSON";//网络请求类型之post请求
    private static final String NETWORK_REQUEST_TYPE_UP_LOAD_FILE = "upLoadFile";//网络请求类型之上传文件
    private static final String NETWORK_REQUEST_TYPE_DOWN_LOAD_FILE = "downLoadFile";//网络请求类型之下载文件
    private OkHttpClient okHttpClient = new OkHttpClient();//网络请求实体




    //数据
    /**同一个网址以及参数在同一时间只能请求一次，期中的key值是网址以及参数tostring的集合体，记录着所有的网络请求，在
     *网络请求完成的时候会移除相应的请求，同时在下一次的请求的时候会再次添加的*/
    private Map<String,NetworkRequestRecordDto> requestRecordDtoMap = new HashMap<>();
    /**请求分类集合，该集合中的key值代表着requestActName所代表的界面或服务，而list集合中存储的是相应界面的所有网络请求的网址
     * 和参数的集合体，也就是每一个value的每一个参数都是requestRecordDtoMap的key值*/
    private Map<String,List<String>> requestClassifyMap = new HashMap<>();


    //主线程子线程问题
    private Handler handlerMainThread = new Handler(Looper.getMainLooper());//主线程
    private Handler handlerChildThread;//子线程


    private Runnable onNetworkDataRequestFailRunnable;//网络数据请求失败runnable
    private Runnable onNetworkDataRequestSucessRunnable;//网络数据请求成功runnable
    private Runnable onNetworkDataRequestErrorRunnable;//网络数据请求异常runnable
    private Runnable onNetworkFileRequestFailRunnable;//网络文件上传下载请求失败runnable
    private Runnable onNetworkFileRequestSucessRunnable;//网络文件上传下载请求成功runnable
    private Runnable onNetworkFileRequestErrorRunnable;//网络文件上传下载请求异常runnable
    private Runnable onNetworkFileRequestProgressRunnable;//网络文件上传下载请求进度runnable

    public static final int NETWORK_DATA_SUCCESS = 0;//网络请求
    //网络请求异常
    public static final int NETWORK_DATA_REQUEST_ERROR = -1;//网络请求发起异常
    public static final int NETWORK_DATA_REQUEST_ERROR_TYPE_DOWN_ERROR = -2;//网络请求文件下载异常
    //网络请求失败原因
    public static final int NETWORK_DATA_REQUEST_FAIL_CASE_requestActName_IS_NULL = -1;//上下文为空
    public static final int NETWORK_DATA_REQUEST_FAIL_CASE_URL_NOT_EFFECTIVE = -2;//url为空或无效
    public static final int NETWORK_DATA_REQUEST_FAIL_CASE_USER_NOT_EFFECTIVE = -3;//不是当前用户请求的
    public static final int NETWORK_DATA_REQUEST_FAIL_CASE_TIME_INTERVALIN = -4;//在限定的时间请求间隔内再次请求了
    public static final int NETWORK_DATA_REQUEST_FAIL_CASE_LAST_REQUESNOT_FINISH = -5;//上一次相同网址key的请求还未完成
    public static final int NETWORK_DATA_REQUEST_FAIL_CASE_RESULT_DATA_PARAMS_FAIL = -6;//返回数据格式化失败
    public static final int NETWORK_DATA_REQUEST_FAIL_CASE_REQUEST_FAIL = -7;//网络请求发起失败，可能出现了404,500等错误
    public static final int NETWORK_DATA_REQUEST_NOT_ONESELF_RESULT_DATA = -8;//网络请求发起成功，但返回的数据不是自己的服务器返回的
    public static final int NETWORK_DATA_REQUEST_NOT_CONNECTION_NET = -9;//没有网络连接
    public static final int NETWORK_DATA_REQUEST_ERROR_URL_PARSE_FAIL = -10;//网址格式化失败



    static {
        TAG = "http net options";
        TIMEOUT = 1000 * 30;  // 超时时间
        SAME_REQUEST_URL_PATH_INTERVEL = 2000;//相同网址请求间隔
        RESPONSE_RESULE_CHARACTER_ENCODING_DEFAULT = "UTF-8";//默认的返回数据的解析格式
    }




    public NetRequestUtils() {
        try {
            X509TrustManager trustManager = null;
            SSLSocketFactory sslSocketFactory = null;
            final InputStream inputStream;
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
//                    .addInterceptor(new Interceptor() {
//                        @Override
//                        public Response intercept(Chain chain) throws IOException {
//                            Request request = chain.request()
//                                    .newBuilder()
//                                    .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
////                                    .addHeader("Accept-Encoding", "gzip, deflate")
////                                    .addHeader("Connection", "keep-alive")
//                                    .addHeader("fromeType", "android")
//                                    .addHeader("appVersionCode", String.valueOf(BuildConfig.VERSION_CODE))
////                                    .addHeader("Accept", "*/*")
////                                    .addHeader("Cookie", "add cookies here")
//                                    .build();
//                            return chain.proceed(request);
//                        }
//                    })
                    .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
//                    .connectionSpecs(Collections.singletonList(spec))
                    ;

            if(trustManager == null || sslSocketFactory == null) {
                //设置请求头
                okHttpClient = builder.build();
            }else {
                //设置请求头
                okHttpClient = builder
                        .sslSocketFactory(sslSocketFactory,trustManager)
                        .build();
            }
        }catch (Exception e){
            Log.d(TAG,"超时时间及请求头设置失败");
            okHttpClient = new OkHttpClient();
        }
    }

    /**
     * 以流的方式添加信任证书
     */
    /**
     * Returns a trust manager that trusts {@code certificates} and none other. HTTPS services whose
     * certificates have not been signed by these certificates will fail with a {@code
     * SSLHandshakeException}.
     * <p>
     * <p>This can be used to replace the host platform's built-in trusted certificates with a custom
     * set. This is useful in development where certificate authority-trusted certificates aren't
     * available. Or in production, to avoid reliance on third-party certificate authorities.
     * <p>
     * <p>
     * <h3>Warning: Customizing Trusted Certificates is Dangerous!</h3>
     * <p>
     * <p>Relying on your own trusted certificates limits your server team's ability to update their
     * TLS certificates. By installing a specific set of trusted certificates, you take on additional
     * operational complexity and limit your ability to migrate between certificate authorities. Do
     * not use custom trusted certificates in production without the blessing of your server's TLS
     * administrator.
     */
    private X509TrustManager trustManagerForCertificates(InputStream in)
            throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        // Put the certificates a key store.
        char[] password = "password".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }
    /**
     * 添加password
     * @param password
     * @return
     * @throws GeneralSecurityException
     */
    private KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType()); // 这里添加自定义的密码，默认
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }




    /**
     * 获取对于缓存记录的key
     *
     * @param requestActName
     * @param requestPath
     * @param paramsMap
     * @return
     */
    private String getRecordKey(String requestActName, String requestPath, Map<String, Object> paramsMap){
        return requestActName + requestPath + (paramsMap != null ? paramsMap.toString() : "");
    }

    /**
     * 检测url请求网址的合法性
     * @param requestPath 被检测的网址
     * @return true : 有效 false：无效
     */
    private boolean isUrlEffective(String requestPath){
        if(requestPath == null || "".equals(requestPath) || requestPath.indexOf("http") != 0){
            Log.i(TAG,"网址无效，被检测网址（" + requestPath + ")");
            return false;
        }else {
            Log.i(TAG,"网址有效，被检测网址（" + requestPath + ")");
            return true;
        }
    }

    /**
     * 检测是否是当前用户发起的请求，如果未登录则直接通过请求
     * @param requestPath 被检测的网址
     * @param paramsMap 被检测post请求的集合
     * @return true：通过 false：不通过
     */
    private boolean checkRequest(String requestPath, Map<String, Object> paramsMap){
//        if(UserInfoUtils.checkUserWheatherLogined()){
//            String accessToken = UserInfoUtils.getInstance(requestActName).getUserDetailInfoDto().getAccessToken();
//            Collection list = paramsMap != null ? paramsMap.values() : new ArrayList();
//            //检测网址中是否包含token
//            if(requestPath.contains(accessToken)){
//                Log.e(TAG,"网址token有效，被检测网址（" + requestPath + "),被检测参数（" + (paramsMap != null ? paramsMap.toString() : "") + ")");
//                return true;
//            }else if(list.contains(accessToken)){
//                Log.e(TAG,"网址token有效，被检测网址（" + requestPath + "),被检测参数（" + (paramsMap != null ? paramsMap.toString() : "") + ")");
//                return true;
//            }else {
//                Log.e(TAG,"网址token无效，被检测网址（" + requestPath + "),被检测参数（" + (paramsMap != null ? paramsMap.toString() : "") + ")");
//                return false;
//            }
//        }else {
//            Log.e(TAG,"网址token无效，用户未登录，通过检测，被检测网址（" + requestPath + "),被检测参数（" + (paramsMap != null ? paramsMap.toString() : "") + ")");
//            return true;
//        }
        return true;
    }

    /**
     * 检测是否在网络请求间隔时间内
     * @param requestRecordDtoKey 网络请求记录集合key
     * @return true ： 在事件内，false ： 在事件外
     */
    private boolean isTimeIntervalIn(String requestRecordDtoKey){
        NetworkRequestRecordDto networkRequestRecordDto = requestRecordDtoMap.get(requestRecordDtoKey);
        if(networkRequestRecordDto != null){
            if(AndJavaCommonUtils.getInstance().getMillisecond() - networkRequestRecordDto.requestTime <= SAME_REQUEST_URL_PATH_INTERVEL){
                Log.i(TAG,"不在相同网址请求时间间隔外，通过检测");
                return true;
            }else {
                Log.i(TAG,"在相同网址请求时间间隔外，不通过检测");
                return false;
            }
        }else {
            Log.i(TAG,"未进行过相同网址请求，不通过检测");
            return false;
        }
    }

    /**
     * 检测在之前是否有相同的网络请求未完成
     * @param requestRecordDtoKey 网络请求记录集合key
     * @return
     */
    private void isRecordSaveAlikeRequest(String requestRecordDtoKey){
        NetworkRequestRecordDto networkRequestRecordDto = requestRecordDtoMap.get(requestRecordDtoKey);
        if(networkRequestRecordDto != null){
            if(networkRequestRecordDto.isRequestFinish){
                Log.i(TAG,"存在了相同的网址请求，但该网址请求已完成，通过检测");
            }else {
                if(networkRequestRecordDto.call.isCanceled()) {
                    networkRequestRecordDto.call.cancel();
                }
                Log.i(TAG,"存在了相同的网址请求，但该网址请求未完成，取消之前的请求信息");
            }
        }else {
            Log.i(TAG,"未进行过相同网址请求，通过检测");
        }
    }

    /**
     * 记录网络请求
     * @param requestActName 上下文
     * @param requestPath 请求网址
     * @param requestType 请求类型
     * @param paramsMap post请求参数
     * @param networkRequestCallback 请求返回值
     * @param isFrontRequest 是否是前台的请求
     * @param call 网络请求请求体
     */
    private void recordNetworkRequest(String requestActName, String requestRecordDtoKey, String requestPath, String requestType, Map<String, Object> paramsMap, Object networkRequestCallback, boolean isFrontRequest, Call call) {
        NetworkRequestRecordDto networkRequestRecordDto = new NetworkRequestRecordDto(requestType, requestPath, paramsMap, call, requestActName
                , AndJavaCommonUtils.getInstance().getMillisecond(), isFrontRequest, networkRequestCallback);
        //存储记录请求内容
        requestRecordDtoMap.put(requestRecordDtoKey,networkRequestRecordDto);
        //记录页面请求
        List<String> list = requestClassifyMap.get(requestActName);
        if(list == null){
            list = new ArrayList<>();
        }
        list.add(requestRecordDtoKey);
        requestClassifyMap.put(requestActName,list);
        Log.d(TAG,"requestInfo :::" + networkRequestRecordDto.toString());
    }

    /**
     * 取消某一个请求
     * @param requestActName
     * @param url
     * @param paramsMap
     */
    public void remNetReq(String requestActName,String url,Map<String,Object> paramsMap){
        String recordKey = getRecordKey(requestActName, url, paramsMap);
        NetworkRequestRecordDto networkRequestRecordDto = requestRecordDtoMap.get(recordKey);
        if(networkRequestRecordDto != null && networkRequestRecordDto.call != null){
            networkRequestRecordDto.call.cancel();
        }
    }

    /**
     * 移除指定页面的所有请求存储
     * @param requestActName
     */
    public void remActReq(@NonNull String requestActName){
        List<String> list = requestClassifyMap.get(requestActName);
        if(list != null) {
            Iterator<String> iterator = list.iterator();
            NetworkRequestRecordDto networkRequestRecordDto;
            while (iterator.hasNext()) {
                networkRequestRecordDto = requestRecordDtoMap.get(iterator.next());
                networkRequestRecordDto.call.cancel();
                requestRecordDtoMap.remove(networkRequestRecordDto);
                networkRequestRecordDto = null;
            }
            iterator = null;

            list.clear();
            list = null;
        }
    }






    /**************************************数据请求****************************************/

    /**
     * 字符串get请求
     * @param requestActName 请求的上下文请求名称
     * @param requestPath 请求网址
     * @param object
     * @param networkRequestCallback  请求回调
     * @param isCheckInterval 是否检查时间间隔
     * @param isFrontRequest 是否是前台的请求
     */
    public void stringRequestForGet(String requestActName, String requestPath
            , Object object, NetworkOptionsCallback networkRequestCallback, boolean isCheckInterval, boolean isFrontRequest){
        networkDataRequest(requestActName,requestPath,NETWORK_REQUEST_TYPE_GET,null,object,networkRequestCallback,isCheckInterval,isFrontRequest);
    }

    /**
     * 网络请求post集合
     * @param requestActName 请求上下文
     * @param requestPath 请求网址
     * @param map post请求参数集合
     * @param networkRequestCallback 请求回调
     * @param isCheckInterval 是否检查时间间隔
     * @param isFrontRequest 是否是前台的请求
     */
    public void stringRequestForPost(String requestActName, String requestPath, Map<String, Object> map
            , Object object, NetworkOptionsCallback networkRequestCallback, boolean isCheckInterval, boolean isFrontRequest) {
        networkDataRequest(requestActName, requestPath, NETWORK_REQUEST_TYPE_POST, map, object, networkRequestCallback, isCheckInterval, isFrontRequest);
    }
    /**
     * 网络请求post_json集合
     * @param requestActName 请求上下文
     * @param requestPath 请求网址
     * @param map post请求参数集合
     * @param networkRequestCallback 请求回调
     * @param isCheckInterval 是否检查时间间隔
     * @param isFrontRequest 是否是前台的请求
     */
    public void stringRequestForPostJson(String requestActName, String requestPath, Map<String, Object> map
            , Object object, NetworkOptionsCallback networkRequestCallback, boolean isCheckInterval, boolean isFrontRequest){
        networkDataRequest(requestActName,requestPath,NETWORK_REQUEST_TYPE_POST_JSON,map, object, networkRequestCallback,isCheckInterval,isFrontRequest);
    }

    /**
     * 网路请求综合
     * @param requestActName 请求上下文
     * @param requestPath 请求网址
     * @param requestType 请求类型，get or post
     * @param paramsMap post请求集合
     * @param object
     * @param networkRequestCallback 请求回调
     * @param isCheckInterval 是否检查忽略时间，检查忽略时间的前提是要已经有请求过的数据
     * @param isFrontRequest 是否是前台的请求
     */
    private void networkDataRequest(String requestActName, final String requestPath, String requestType, final Map<String, Object> paramsMap
            , final Object object, final NetworkOptionsCallback networkRequestCallback, boolean isCheckInterval, boolean isFrontRequest){
        final String requestRecordDtoKey = getRecordKey(requestActName, requestPath,paramsMap);
        if(requestActName == null){
            requestActName = BuildConfig.APPLICATION_ID;
        }
//        //检查网络是否连接
//        if(!AppCommon.netIsConnection){
//            onNetworkRequestFail(null,NETWORK_DATA_REQUEST_NOT_CONNECTION_NET,networkRequestCallback);
//            return;
//        }
        //检测网址是否有效
        if(!isUrlEffective(requestPath)){
            onNetworkRequestFail(null,NETWORK_DATA_REQUEST_FAIL_CASE_URL_NOT_EFFECTIVE,networkRequestCallback);
            return;
        }
        //如果登录的话是否是当前用户进行的网络请求
        if(!checkRequest(requestPath,paramsMap)){
            onNetworkRequestFail(null,NETWORK_DATA_REQUEST_FAIL_CASE_USER_NOT_EFFECTIVE,networkRequestCallback);
            return;
        }
        //时间间隔检测
        if(isCheckInterval && isTimeIntervalIn(requestRecordDtoKey)){
            onNetworkRequestFail(null,NETWORK_DATA_REQUEST_FAIL_CASE_TIME_INTERVALIN,networkRequestCallback);
            return;
        }
        //检测之前是否有相同的网络请求未完成，如果有的话则取消请求
        isRecordSaveAlikeRequest(requestRecordDtoKey);

        //构造网络请求
        Request request = null;
        switch (requestType){
            case NETWORK_REQUEST_TYPE_GET:
                request = new Request.Builder().url(requestPath).build();
                break;
            case NETWORK_REQUEST_TYPE_POST:
                if(paramsMap != null){
                    //创建请求的参数body
                    FormBody.Builder builder = new FormBody.Builder();
                    for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                        if(entry.getKey() != null && entry.getValue() != null
                                && !"".equals(entry.getKey()) && !"".equals(entry.getValue())){
                            builder.add(entry.getKey(), String.valueOf(entry.getValue()));
                        }
                    }
                    request = new Request.Builder().url(requestPath).post(builder.build()).build();
                }
                break;
            case NETWORK_REQUEST_TYPE_POST_JSON:
                if(paramsMap != null){
//                    request = new Request.Builder().url(requestPath)
//                            .post(FormBody.create(MediaType.parse("application/json; charset=utf-8"), JsonUtils.toJson(paramsMap))).build();
                }
                break;
            default:
                break;
        }



        if(request != null){
            Call call = okHttpClient.newCall(request);
            //记录网络请求
            recordNetworkRequest(requestActName,requestRecordDtoKey, requestPath, requestType, paramsMap, networkRequestCallback, isFrontRequest, call);
            final String finalRequestActName = requestActName;
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    finishRequestAfterRecord(call);//完成网络请求后对于记录的修改
                    if(!call.isCanceled()) {
                        if (e != null && e.getMessage() != null) {
                            Log.d(TAG, e.getMessage());
                            e.printStackTrace();
                        }
                        onNetworkRequestFail(object,NETWORK_DATA_REQUEST_FAIL_CASE_REQUEST_FAIL, networkRequestCallback);
                    }
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    finishRequestAfterRecord(call);//完成网络请求后对于记录的修改
                    if(!call.isCanceled()) {
                        try {
                            Log.d(TAG, response.toString());
                            if (response.isSuccessful()) {
                                onNetworkRequestSuccess(finalRequestActName,object,requestRecordDtoKey, new String(response.body().string().getBytes(), RESPONSE_RESULE_CHARACTER_ENCODING_DEFAULT), networkRequestCallback);
                            } else {
                                onNetworkRequestError(object,NETWORK_DATA_REQUEST_ERROR,"",networkRequestCallback);
                            }
                        } catch (Exception e) {
                            onNetworkRequestError(object,NETWORK_DATA_REQUEST_ERROR,"",networkRequestCallback);
                        }
                    }
                }

                /**
                 * 完成网络请求后对于记录的修改
                 * @param call
                 */
                private void finishRequestAfterRecord(Call call) {
                    NetworkRequestRecordDto networkRequestRecordDto = requestRecordDtoMap.get(requestRecordDtoKey);
                    networkRequestRecordDto.isRequestFinish = true;
                    networkRequestRecordDto.call = call;
                    requestRecordDtoMap.put(requestRecordDtoKey, networkRequestRecordDto);
                }
            });
        }
    }




    /***********************************文件上传请求****************************************/

    public void upLoadFileRequest(final String requestActName, final String requestPath, final Map<String, Object> paramsMap
            , final Object object, boolean isUpDataOriginalPicture, boolean isFrontRequest, final NetworkOptionsCallback networkRequestCallback){
        final String requestRecordDtoKey = getRecordKey(requestActName,requestPath,paramsMap);
//        //检查网络是否连接
//        if(!AppCommon.netIsConnection){
//            onNetworkRequestFail(object,NETWORK_DATA_REQUEST_NOT_CONNECTION_NET,networkRequestCallback);
//            return;
//        }
        //检测上下文是否为空
        if(requestActName == null){
            Log.e(TAG,"上下文为空");
            onNetworkRequestFail(object,NETWORK_DATA_REQUEST_FAIL_CASE_requestActName_IS_NULL,networkRequestCallback);
            return;
        }
        //检测网址是否有效
        if(!isUrlEffective(requestPath)){
            onNetworkRequestFail(object,NETWORK_DATA_REQUEST_FAIL_CASE_URL_NOT_EFFECTIVE,networkRequestCallback);
            return;
        }
        //如果登录的话是否是当前用户进行的网络请求
        if(!checkRequest(requestPath,paramsMap)){
            onNetworkRequestFail(object,NETWORK_DATA_REQUEST_FAIL_CASE_USER_NOT_EFFECTIVE,networkRequestCallback);
            return;
        }
        try {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            //设置类型
            builder.setType(MultipartBody.FORM);
            Iterator<Map.Entry<String, Object>> iterator = paramsMap.entrySet().iterator();
            Map.Entry<String, Object> next;
            String key;
            Object value;
            while (iterator.hasNext()){
                next = iterator.next();
                key = next.getKey();
                value = next.getValue();
                if(!(value instanceof File)){
                    builder.addFormDataPart(key, value.toString());
                }else {
                    File nowFile = (File) value;
                    File upDataFile = null;//将要上传的file
                    //逻辑判定要上传的文件
//                    if(CheckUtils.checkIsImage(nowFile.getAbsolutePath())){
//                        if(isUpDataOriginalPicture){
//                            upDataFile = nowFile;
//                        }else {
////                            String compressPath = AppCommon.PROJECT_FILE_DIR_IMAGE + AppUtils.generateUuid() + ".jpg";
////                            try {
////                                boolean compressState =  NativeUtil.compressFile(nowFile.getAbsolutePath(),compressPath,300,true, Bitmap.CompressFormat.JPEG);
////                                if(compressState) {
////                                    try {
////                                        upDataFile = new File(compressPath);
////                                    } catch (Exception e) {
////                                        Log.e(TAG,"file is not find");
////                                        upDataFile = new File(nowFile.getAbsolutePath());
////                                    }
////                                }
////                            }catch (Exception e){
////                                Log.e(TAG,"file is not find");
////                                upDataFile = new File(nowFile.getAbsolutePath());
////                            }
//                            upDataFile = new File(nowFile.getAbsolutePath());
//                        }
//                    }else {
//                        upDataFile = nowFile;
//                    }
                    //准备上传文件
                    if(upDataFile != null && upDataFile.exists()){
                        final File finalUpDataFile = upDataFile;
                        builder.addFormDataPart(key, upDataFile.getName(), new RequestBody() {
                            @Override
                            public MediaType contentType() {
                                return MediaType.parse("application/octet-stream");//mdiatype 这个需要和服务端保持一致 你需要看下你们服务器设置的ContentType 是不是这个，他们设置的是哪个 我们要和他们保持一致
                            }

                            @Override
                            public synchronized void writeTo(BufferedSink sink) throws IOException {
                                Source source;
                                try {
                                    source = Okio.source(finalUpDataFile);
                                    Buffer buf = new Buffer();
                                    long current = 0;
                                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                                        sink.write(buf, readCount);
                                        current += readCount;
                                        int progress = (int) (current * 1.0f / finalUpDataFile.length() * 100);
                                        onNetworkFileRequestProgress(finalUpDataFile.getAbsolutePath(),object,progress, networkRequestCallback);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
            //创建RequestBody
            RequestBody body = builder.build();
            //创建Request
            final Request request = new Request.Builder().url(requestPath).post(body).build();
            if(request != null){
                Call call = okHttpClient.newCall(request);
                //记录网络请求
                recordNetworkRequest(requestActName,requestRecordDtoKey, requestPath, NETWORK_REQUEST_TYPE_UP_LOAD_FILE, paramsMap, networkRequestCallback, isFrontRequest, call);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        finishRequestAfterRecord(call);//完成网络请求后对于记录的修改
                        if(!call.isCanceled()) {
                            if (e != null && e.getMessage() != null) {
                                Log.d(TAG, e.getMessage());
                                e.printStackTrace();
                            }
                            onNetworkRequestFail(object,NETWORK_DATA_REQUEST_FAIL_CASE_REQUEST_FAIL, networkRequestCallback);
                        }
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        finishRequestAfterRecord(call);//完成网络请求后对于记录的修改
                        if(!call.isCanceled()) {
                            try {
                                Log.d(TAG, response.toString());
                                if (response.isSuccessful()) {
                                    onNetworkRequestSuccess(requestActName,object,requestRecordDtoKey, new String(response.body().string().getBytes(), RESPONSE_RESULE_CHARACTER_ENCODING_DEFAULT), networkRequestCallback);
                                } else {
                                    onNetworkRequestError(object,NETWORK_DATA_REQUEST_ERROR,"",networkRequestCallback);
                                }
                            } catch (Exception e) {
                                onNetworkRequestError(object,NETWORK_DATA_REQUEST_ERROR,"",networkRequestCallback);
                            }
                        }
                    }

                    /**
                     * 完成网络请求后对于记录的修改
                     * @param call
                     */
                    private void finishRequestAfterRecord(Call call) {
                        NetworkRequestRecordDto networkRequestRecordDto = requestRecordDtoMap.get(requestRecordDtoKey);
                        networkRequestRecordDto.isRequestFinish = true;
                        networkRequestRecordDto.call = call;
                        requestRecordDtoMap.put(requestRecordDtoKey, networkRequestRecordDto);
                    }
                });
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

    }


    /***********************************文件下载请求****************************************/
    public void downLoadFileRequest(final String requestActName, final String requestPath, final String savePath
            , final Object object, boolean isUpDataOriginalPicture, boolean isFrontRequest, final NetworkOptionsCallback networkRequestCallback){
//        //检查网络是否连接
//        if(!AppCommon.netIsConnection){
//            onNetworkRequestFail(object,NETWORK_DATA_REQUEST_NOT_CONNECTION_NET,networkRequestCallback);
//            return;
//        }
        final String requestRecordDtoKey = getRecordKey(requestActName, requestPath,null);
        //检测上下文是否为空
        if(requestActName == null){
            Log.e(TAG,"上下文为空");
            onNetworkRequestFail(object,NETWORK_DATA_REQUEST_FAIL_CASE_requestActName_IS_NULL,networkRequestCallback);
            return;
        }
        //检测网址是否有效
        if(!isUrlEffective(requestPath)){
            onNetworkRequestFail(object,NETWORK_DATA_REQUEST_FAIL_CASE_URL_NOT_EFFECTIVE,networkRequestCallback);
            return;
        }
        //如果登录的话是否是当前用户进行的网络请求
        if(!checkRequest(requestPath,null)){
            onNetworkRequestFail(object,NETWORK_DATA_REQUEST_FAIL_CASE_USER_NOT_EFFECTIVE,networkRequestCallback);
            return;
        }
        //开启下载
        Request request = new Request.Builder().url(requestPath).build();
        Call call = okHttpClient.newCall(request);
        //记录网络请求
        recordNetworkRequest(requestActName,requestRecordDtoKey, requestPath, NETWORK_REQUEST_TYPE_DOWN_LOAD_FILE, null, networkRequestCallback, isFrontRequest, call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                finishRequestAfterRecord(call);//完成网络请求后对于记录的修改
                if(!call.isCanceled()) {
                    if (e != null && e.getMessage() != null) {
                        Log.d(TAG,savePath + "文件下载失败  :" + e.getMessage());
                        e.printStackTrace();
                    }
                    onNetworkRequestFail(null,NETWORK_DATA_REQUEST_FAIL_CASE_REQUEST_FAIL, networkRequestCallback);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                finishRequestAfterRecord(call);//完成网络请求后对于记录的修改
                if(!call.isCanceled()) {
                    try {
                        Log.d(TAG, response.toString());
                        if (response.isSuccessful()) {
                            saveFile(response);
                        } else {
                            onNetworkRequestError(object,NETWORK_DATA_REQUEST_ERROR,"",networkRequestCallback);
                        }
                    } catch (Exception e) {
                        onNetworkRequestError(object,NETWORK_DATA_REQUEST_ERROR,"",networkRequestCallback);
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
                    if(!file.getParentFile().exists()){
                        file.getParentFile().mkdirs();
                    }
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        onNetworkFileRequestProgress(requestPath,object,progress,networkRequestCallback);
                        fos.flush();
                    }
                    Log.d(TAG,savePath + "文件下载成功");
                    if(file.exists()){
                        file = null;
                        onNetworkRequestSuccess(requestActName,object,requestRecordDtoKey,"",networkRequestCallback);
                    }else {
                        onNetworkRequestError(object,NETWORK_DATA_REQUEST_ERROR_TYPE_DOWN_ERROR,"",networkRequestCallback);
                    }
                } catch (Exception e) {
                    Log.d(TAG,savePath + "文件下载失败  :" + e.getMessage());
                    onNetworkRequestError(object,NETWORK_DATA_REQUEST_ERROR_TYPE_DOWN_ERROR,"",networkRequestCallback);
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }

            /**
             * 完成网络请求后对于记录的修改
             * @param call
             */
            private void finishRequestAfterRecord(Call call) {
                NetworkRequestRecordDto networkRequestRecordDto = requestRecordDtoMap.get(requestRecordDtoKey);
                networkRequestRecordDto.isRequestFinish = true;
                networkRequestRecordDto.call = call;
                requestRecordDtoMap.put(requestRecordDtoKey, networkRequestRecordDto);
            }
        });

    }


    /**
     * 主页宫格文件下载
     * @param requestActName
     * @param requestPath
     * @param saveKey 保存到共享存储当中的key
     * @param object
     * @param networkRequestCallback
     */
    public void downLoadHomeIcon(final String requestActName, final String requestPath, final String saveKey
            , final Object object, final NetworkOptionsCallback networkRequestCallback){
//        //检查网络是否连接
//        if(!AppCommon.netIsConnection){
//            onNetworkRequestFail(object,NETWORK_DATA_REQUEST_NOT_CONNECTION_NET,networkRequestCallback);
//            return;
//        }
            final String requestRecordDtoKey = getRecordKey(requestActName, requestPath,null);
            //检测上下文是否为空
            if(requestActName == null){
                Log.e(TAG,"上下文为空");
                onNetworkRequestFail(object,NETWORK_DATA_REQUEST_FAIL_CASE_requestActName_IS_NULL,networkRequestCallback);
                return;
            }
            //检测网址是否有效
            if(!isUrlEffective(requestPath)){
                onNetworkRequestFail(object,NETWORK_DATA_REQUEST_FAIL_CASE_URL_NOT_EFFECTIVE,networkRequestCallback);
                return;
            }
            //如果登录的话是否是当前用户进行的网络请求
            if(!checkRequest(requestPath,null)){
                onNetworkRequestFail(object,NETWORK_DATA_REQUEST_FAIL_CASE_USER_NOT_EFFECTIVE,networkRequestCallback);
                return;
            }
            //开启下载
            Request request = new Request.Builder().url(requestPath).build();
            Call call = okHttpClient.newCall(request);
            //记录网络请求
            recordNetworkRequest(requestActName,requestRecordDtoKey, requestPath, NETWORK_REQUEST_TYPE_DOWN_LOAD_FILE, null
                    , networkRequestCallback, true, call);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    finishRequestAfterRecord(call);//完成网络请求后对于记录的修改
                    if(!call.isCanceled()) {
                        if (e != null && e.getMessage() != null) {
                            Log.d(TAG,saveKey + "文件下载失败  :" + e.getMessage());
                            e.printStackTrace();
                        }
                        onNetworkRequestFail(null,NETWORK_DATA_REQUEST_FAIL_CASE_REQUEST_FAIL, networkRequestCallback);
                    }
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    finishRequestAfterRecord(call);//完成网络请求后对于记录的修改
                    if(!call.isCanceled()) {
                        try {
                            Log.d(TAG, response.toString());
                            if (response.isSuccessful()) {
                                saveFile(response);
                            } else {
                                onNetworkRequestError(object,NETWORK_DATA_REQUEST_ERROR,"",networkRequestCallback);
                            }
                        } catch (Exception e) {
                            onNetworkRequestError(object,NETWORK_DATA_REQUEST_ERROR,"",networkRequestCallback);
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
                    ByteArrayOutputStream outputStream = null;
                    try {
                        is = response.body().byteStream();
                        long total = response.body().contentLength();
                        outputStream = new ByteArrayOutputStream();
                        long sum = 0;
                        while ((len = is.read(buf)) != -1) {
                            outputStream.write(buf, 0, len);
                            sum += len;
                            int progress = (int) (sum * 1.0f / total * 100);
                            onNetworkFileRequestProgress(requestPath,object,progress,networkRequestCallback);
                            outputStream.flush();
                        }
                        Log.d(TAG,saveKey + "文件下载成功");
                        String data = android.util.Base64.encodeToString(outputStream.toByteArray(), android.util.Base64.DEFAULT);
                        if(!TextUtils.isEmpty(data)){
//                            //存储数据
//                            SharedPrefUtils.getInstance(ContextUtils.getApplicationContext()).putString(saveKey,data);
                            onNetworkRequestSuccess(requestActName,object,requestRecordDtoKey,data,networkRequestCallback);
                        }else {
                            onNetworkRequestError(object,NETWORK_DATA_REQUEST_ERROR_TYPE_DOWN_ERROR,"",networkRequestCallback);
                        }
                    } catch (Exception e) {
                        Log.d(TAG,saveKey + "文件下载失败  :" + e.getMessage());
                        onNetworkRequestError(object,NETWORK_DATA_REQUEST_ERROR_TYPE_DOWN_ERROR,"",networkRequestCallback);
                    } finally {
                        try {
                            if (is != null)
                                is.close();
                        } catch (IOException e) {
                        }
                        try {
                            if (outputStream != null)
                                outputStream.close();
                        } catch (IOException e) {
                        }
                    }
                }

                /**
                 * 完成网络请求后对于记录的修改
                 * @param call
                 */
                private void finishRequestAfterRecord(Call call) {
                    NetworkRequestRecordDto networkRequestRecordDto = requestRecordDtoMap.get(requestRecordDtoKey);
                    networkRequestRecordDto.isRequestFinish = true;
                    networkRequestRecordDto.call = call;
                    requestRecordDtoMap.put(requestRecordDtoKey, networkRequestRecordDto);
                }
            });
    }

















    /**************************************请求回调****************************************/

    /**
     * 网络请求成功
     * @param requestActName 上下文
     * @param requestRecordDtoKey 请求数据记录key
     * @param data 网络请求返回的数据
     * @param callback 请求回调
     */
    private void onNetworkRequestSuccess(final String requestActName, final Object object, final String requestRecordDtoKey
            , final String data, final NetworkOptionsCallback callback){
        final NetworkRequestRecordDto networkRequestRecordDto = requestRecordDtoMap.get(requestRecordDtoKey);
        if(callback != null){
            onNetworkDataRequestSucessRunnable = new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG,"network resultData  :" + data);
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        int code = NETWORK_DATA_REQUEST_FAIL_CASE_RESULT_DATA_PARAMS_FAIL;
                        String msg = "";
                        boolean success = false;
                        String content = "";
                        if(jsonObject.has("code")){
                            code = jsonObject.getInt("code");
                        }
                        if(jsonObject.has("message")){
                            msg = jsonObject.getString("message");
                            msg = msg == null ? "" : msg;
                        }
                        if(jsonObject.has("success")){
                            success = jsonObject.getBoolean("success");
                        }
                        if(jsonObject.has("data")){
                            content = jsonObject.getString("data");
                        }
//                        callback.successForJsonData(code,msg,success,content,object);
                    }catch (Exception e){
//                        callback.successForData(NETWORK_DATA_SUCCESS,"",true,data,object);
                    }
                }
            };
            postMainRunnable(onNetworkDataRequestSucessRunnable,null);
        }else {
            //更新记录数据,只能在最后使用完成更新
            networkRequestRecordDto.resultData = data;
            requestRecordDtoMap.put(requestRecordDtoKey, networkRequestRecordDto);
        }
    }

    /**网络请求之数据交互请求出错
     * @param object
     * @param statusCode 请求状态码
     * @param status 请求状态值
     * @param networkRequestCallback 请求回调
     */
    private void onNetworkRequestError(final Object object, final Integer statusCode, final String status, final NetworkOptionsCallback networkRequestCallback){
        if (networkRequestCallback != null) {
            onNetworkDataRequestErrorRunnable = new Runnable() {
                public void run() {
//                    networkRequestCallback.error(statusCode,status,object);
                }
            };
            postMainRunnable(onNetworkDataRequestErrorRunnable,null);
        }
    }

    /**
     * 网络在请求时失败
     * @param failCase 失败原因
     * @param networkRequestCallback 请求回调
     */
    private void onNetworkRequestFail(final Object object, final int failCase, final NetworkOptionsCallback networkRequestCallback){
        if (null != networkRequestCallback) {
            onNetworkDataRequestFailRunnable = new Runnable() {
                public void run() {
//                    networkRequestCallback.fail(failCase,"",object);
                }
            };
            postMainRunnable(onNetworkDataRequestFailRunnable,null);
        }
    }

    /**
     * 文件上传下载进度回调
     * @param object
     * @param progress 0-100
     * @param networkRequestCallback
     */
    private void onNetworkFileRequestProgress(final String filePath, final Object object, final int progress
            , final NetworkOptionsCallback networkRequestCallback){
        if (null != networkRequestCallback) {
            onNetworkFileRequestProgressRunnable = new Runnable() {
                public void run() {
                    Log.d(TAG, "当前文件传输进度:::" + progress);
                    networkRequestCallback.progress(progress);
                }
            };
            postMainRunnable(onNetworkFileRequestProgressRunnable,null);
        }
    }

    /**
     * 向主线程发送runnable
     * @param runnable
     * @param delayed 为空时不做延时
     */
    private void postMainRunnable(Runnable runnable,Long delayed){
        if(delayed != null){
            try {
                handlerMainThread.postDelayed(runnable,delayed);
            }catch (Exception e){
                handlerMainThread = new Handler(Looper.myLooper());
                handlerMainThread.postDelayed(runnable,delayed);
            }
        }else {
            try {
                handlerMainThread.post(runnable);
            }catch (Exception e){
                handlerMainThread = new Handler(Looper.myLooper());
                handlerMainThread.post(runnable);
            }

        }
    }
}
