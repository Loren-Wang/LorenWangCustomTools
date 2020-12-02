package javabase.lorenwang.network;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;
import javabase.lorenwang.tools.net.JtlwNetUtils;

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
public class JnlwHttpClientReq extends JnlwBaseReq {
    /**
     * 请求客户端
     */
    private CloseableHttpClient requestClient;

    /**
     * 设置请求客户端
     *
     * @param httpclient 请求客户端
     */
    void setRequestClient(CloseableHttpClient httpclient) {
        this.requestClient = httpclient;
    }

    /**
     * 设置Get请求
     *
     * @param config 请求配置
     */
    @Override
    void sendGetRequest(@NotNull JnlwNetworkReqConfig config) {
        //创建请求
        HttpGet httpGet = getHttpRequestBase(new HttpGet(), config);
        //发起请求
        sendRequest(config, httpGet);
    }

    /**
     * 设置Put请求
     *
     * @param config 请求配置
     */
    @Override
    void sendPutRequest(@NotNull JnlwNetworkReqConfig config) {
        //创建请求
        HttpPut request = getHttpRequestBase(new HttpPut(), config);
        //json数据处理
        if (JtlwCheckVariateUtils.getInstance().isNotEmpty(config.getRequestDataJson())) {
            request.setEntity(new StringEntity(config.getRequestDataJson(), "UTF-8"));
            request.setHeader("Content-Type", "application/json;charset=utf8");
        }
        //发起请求
        sendRequest(config, request);
    }

    /**
     * 设置Post请求
     *
     * @param config 请求配置
     */
    @Override
    void sendPostRequest(@NotNull JnlwNetworkReqConfig config) {
        //创建请求
        HttpPost request = getHttpRequestBase(new HttpPost(), config);
        //json数据处理
        if (JtlwCheckVariateUtils.getInstance().isNotEmpty(config.getRequestDataJson())) {
            request.setEntity(new StringEntity(config.getRequestDataJson(), "UTF-8"));
            request.setHeader("Content-Type", "application/json;charset=utf8");
        }
        //发起请求
        sendRequest(config, request);
    }

    /**
     * 设置Delete请求
     *
     * @param config 请求配置
     */
    @Override
    void sendDeleteRequest(@NotNull JnlwNetworkReqConfig config) {
        //创建请求
        HttpDelete request = getHttpRequestBase(new HttpDelete(), config);
        //发起请求
        sendRequest(config, request);
    }

    /**
     * 设置Options请求
     *
     * @param config 请求配置
     */
    @Override
    void sendOptionsRequest(@NotNull JnlwNetworkReqConfig config) {
        //创建请求
        HttpOptions request = getHttpRequestBase(new HttpOptions(), config);
        //发起请求
        sendRequest(config, request);
    }

    /**
     * 获取基础请求
     *
     * @param request 请求实例
     * @param config  配置信息
     * @param <T>     初始化过的请求实例
     * @return 处理后的请求实例
     */
    private <T extends HttpRequestBase> T getHttpRequestBase(T request, @NotNull JnlwNetworkReqConfig config) {
        request.setURI(URI.create(generateRequestUrl(config)));

        //header处理
        for (Map.Entry<String, String> entry : config.getRequestHeaderParams().entrySet()) {
            request.addHeader(entry.getKey(), entry.getValue());
        }

        //requestConfig处理
        RequestConfig requestConfig = RequestConfig.custom()
                // 设置连接超时时间(单位毫秒)
                .setConnectTimeout(config.getConnectTimeout())
                // 设置请求超时时间(单位毫秒)
                .setConnectionRequestTimeout(config.getConnectRequestTimeout())
                // socket读写超时时间(单位毫秒)
                .setSocketTimeout(config.getSocketTimeout())
                // 设置是否允许重定向(默认为true)
                .setRedirectsEnabled(true).build();

        // 将上面的配置信息 运用到这个Get请求里
        request.setConfig(requestConfig);

        return request;
    }

    /**
     * 发起请求
     *
     * @param request 请求实例
     * @param config  配置信息
     * @param <T>     初始化过的请求实例
     */
    private <T extends HttpRequestBase> void sendRequest(@NotNull JnlwNetworkReqConfig config, T request) {
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = requestClient.execute(request);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null && response.getStatusLine().getStatusCode() == 200) {
                if (config.getNetworkCallback() != null) {
                    config.getNetworkCallback().success(response.getStatusLine().getProtocolVersion().getProtocol(),
                            response.getStatusLine().getProtocolVersion().getMajor(),
                            response.getStatusLine().getProtocolVersion().getMinor(),
                            EntityUtils.toString(responseEntity));
                }
            } else {
                if (retryConnect(config)) {
                    if (config.getNetworkCallback() != null) {
                        config.getNetworkCallback().fail(response.getStatusLine().getProtocolVersion().getProtocol(),
                                response.getStatusLine().getProtocolVersion().getMajor(),
                                response.getStatusLine().getProtocolVersion().getMinor(),
                                response.getStatusLine().getStatusCode());
                    }
                }
            }
        } catch (Exception e) {
            if (retryConnect(config)) {
                if (config.getNetworkCallback() != null) {
                    config.getNetworkCallback().error(e);
                }
            }
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
