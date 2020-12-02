package javabase.lorenwang.network;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import javabase.lorenwang.tools.common.JtlwCheckVariateUtils;
import javabase.lorenwang.tools.net.JtlwNetUtils;

/**
 * 功能作用：网络基础请求
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
abstract class JnlwBaseReq {
    /**
     * 发起网络请求
     *
     * @param config 发起网络请求的配置信息
     */
    public void sendRequest(@NotNull JnlwNetworkReqConfig config) {
        switch (config.getNetworkTypeEnum()) {
            case GET:
                sendGetRequest(config);
                break;
            case PUT:
                sendPutRequest(config);
                break;
            case POST:
                sendPostRequest(config);
                break;
            case DELETE:
                sendDeleteRequest(config);
                break;
            case OPTIONS:
                sendOptionsRequest(config);
                break;
            case NONE:
            default:
                break;
        }
    }

    /**
     * 重试连接
     *
     * @param config 配置信息
     * @return true代表着已经不需要重试
     */
    protected boolean retryConnect(@NotNull JnlwNetworkReqConfig config) {
        if (config.getReconnectionCount() > 0) {
            sendRequest(new JnlwNetworkReqConfig.Build().copy(config).setReconnectionCount(config.getReconnectionCount() - 1).build());
            return false;
        } else {
            return true;
        }
    }

    /**
     * 生成请求的url地址
     * @param config 配置信息
     * @return url地址
     */
    protected String generateRequestUrl(@NotNull JnlwNetworkReqConfig config) {
        //url处理
        StringBuilder url = new StringBuilder();
        if (JtlwCheckVariateUtils.getInstance().isNotEmpty(config.getBaseUrl())) {
            url.append(config.getBaseUrl());
        } else {
            url.append("http://127.0.0.1");
        }
        if (JtlwCheckVariateUtils.getInstance().isNotEmpty(config.getRequestUrl())) {
            //如果是本地的话判断是否第一位是斜杠，无斜杠要添加
            String first = config.getRequestUrl().substring(0, 1);
            String end = url.substring(url.length() - 1, url.length());
            if(first.matches("[^/?]") && end.matches("[^/?]")){
                url.append("/");
            }
            url.append(config.getRequestUrl());
        }
        String requestUrl = url.toString();
        url.setLength(0);
        //参数拼接处理
        for (Map.Entry<String, String> entry : config.getRequestDataParams().entrySet()) {
            requestUrl = JtlwNetUtils.getInstance().addUrlParams(requestUrl, entry.getKey(), entry.getValue());
        }
        return requestUrl;
    }

    /**
     * 设置Get请求
     *
     * @param config 请求配置
     */
    abstract void sendGetRequest(@NotNull JnlwNetworkReqConfig config);

    /**
     * 设置Put请求
     *
     * @param config 请求配置
     */
    abstract void sendPutRequest(@NotNull JnlwNetworkReqConfig config);

    /**
     * 设置Post请求
     *
     * @param config 请求配置
     */
    abstract void sendPostRequest(@NotNull JnlwNetworkReqConfig config);

    /**
     * 设置Delete请求
     *
     * @param config 请求配置
     */
    abstract void sendDeleteRequest(@NotNull JnlwNetworkReqConfig config);

    /**
     * 设置Options请求
     *
     * @param config 请求配置
     */
    abstract void sendOptionsRequest(@NotNull JnlwNetworkReqConfig config);
}
