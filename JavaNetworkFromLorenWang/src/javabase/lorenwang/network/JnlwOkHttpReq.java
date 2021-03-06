package javabase.lorenwang.network;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;
import javabase.lorenwang.tools.net.JtlwNetUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 功能作用：httpClient请求
 * 创建时间：2020-12-01 11:59 上午
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
public class JnlwOkHttpReq extends JnlwBaseReq {
    /**
     * 请求客户端
     */
    private OkHttpClient requestClient;

    /**
     * 设置客户端
     * @param requestClient 客户端
     */
    void setRequestClient(OkHttpClient requestClient) {
        this.requestClient = requestClient;
    }

    /**
     * 设置Get请求
     *
     * @param config 请求配置
     */
    @Override
    void sendGetRequest(@NotNull JnlwNetworkReqConfig config) {
        Request.Builder requestBuilder = getHttpRequestBase(new Request.Builder().get(), config);
        Call call = requestClient.newCall(requestBuilder.build());
        call.enqueue(getRequestCallback(config));
    }

    /**
     * 设置Put请求
     *
     * @param config 请求配置
     */
    @Override
    void sendPutRequest(@NotNull JnlwNetworkReqConfig config) {
        Request.Builder requestBuilder = getHttpRequestBase(new Request.Builder(), config);
        RequestBody requestBody;
        //json数据处理
        if (JtlwCheckVariateUtils.getInstance().isNotEmpty(config.getRequestDataJson())) {
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(config.getRequestDataJson()));
        } else {
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "");
        }
        Call call = requestClient.newCall(requestBuilder.put(requestBody).build());
        call.enqueue(getRequestCallback(config));
    }

    /**
     * 设置Post请求
     *
     * @param config 请求配置
     */
    @Override
    void sendPostRequest(@NotNull JnlwNetworkReqConfig config) {
        Request.Builder requestBuilder = getHttpRequestBase(new Request.Builder(), config);
        RequestBody requestBody;
        //json数据处理
        if (JtlwCheckVariateUtils.getInstance().isNotEmpty(config.getRequestDataJson())) {
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(config.getRequestDataJson()));
        } else {
            requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "");
        }
        Call call = requestClient.newCall(requestBuilder.post(requestBody).build());
        call.enqueue(getRequestCallback(config));
    }

    /**
     * 设置Delete请求
     *
     * @param config 请求配置
     */
    @Override
    void sendDeleteRequest(@NotNull JnlwNetworkReqConfig config) {
        Request.Builder requestBuilder = getHttpRequestBase(new Request.Builder().delete(), config);
        Call call = requestClient.newCall(requestBuilder.build());
        call.enqueue(getRequestCallback(config));
    }

    /**
     * 设置Options请求,okhttp没有该方法
     *
     * @param config 请求配置
     */
    @Deprecated
    @Override
    void sendOptionsRequest(@NotNull JnlwNetworkReqConfig config) {

    }

    /**
     * 获取基础请求
     *
     * @param config 配置信息
     * @return 处理后的请求实例
     */
    private Request.Builder getHttpRequestBase(Request.Builder requestBuilder, @NotNull JnlwNetworkReqConfig config) {
        requestBuilder.url(generateRequestUrl(config));

        //header处理
        for (Map.Entry<String, String> entry : config.getRequestHeaderParams().entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }
        return requestBuilder;
    }

    /**
     * 获取请求回调
     *
     * @param config 配置信息
     * @return 请求回调
     */
    private Callback getRequestCallback(@NotNull JnlwNetworkReqConfig config) {
        return new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (retryConnect(config)) {
                    if (config.getNetworkCallback() != null) {
                        config.getNetworkCallback().error(e);
                    }
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    if (config.getNetworkCallback() != null) {
                        String data = response.body().string();
                        switch (response.protocol()) {
                            case QUIC:
                            case HTTP_2:
                            case HTTP_1_1:
                                config.getNetworkCallback().success("http", 1, 1, data);
                                break;
                            case HTTP_1_0:
                                config.getNetworkCallback().success("http", 1, 0, data);
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    if (retryConnect(config)) {
                        if (config.getNetworkCallback() != null) {
                            switch (response.protocol()) {
                                case QUIC:
                                case HTTP_2:
                                case HTTP_1_1:
                                    config.getNetworkCallback().fail("http", 1, 1, response.code());
                                    break;
                                case HTTP_1_0:
                                    config.getNetworkCallback().fail("http", 1, 0, response.code());
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        };
    }

}
