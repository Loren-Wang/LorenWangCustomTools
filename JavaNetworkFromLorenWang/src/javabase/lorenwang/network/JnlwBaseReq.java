package javabase.lorenwang.network;

import java.util.Map;


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
     * @return 响应实体
     */
    public JnlwHttpRes sendRequest(JnlwNetworkReqConfig config) {
        switch (config.getNetworkTypeEnum()) {
            case GET:
                return sendGetRequest(config);
            case PUT:
                return sendPutRequest(config);
            case POST:
                return sendPostRequest(config);
            case DELETE:
                return sendDeleteRequest(config);
            case OPTIONS:
                return sendOptionsRequest(config);
            case NONE:
            default:
                return null;
        }
    }

    /**
     * 重试连接
     *
     * @param config 配置信息
     * @return true代表着已经不需要重试
     */
    protected boolean retryConnect(JnlwNetworkReqConfig config) {
        if (config.getReconnectionCount() > 0) {
            sendRequest(new JnlwNetworkReqConfig.Build().copy(config).setReconnectionCount(config.getReconnectionCount() - 1).build());
            return false;
        } else {
            return true;
        }
    }

    /**
     * 生成请求的url地址
     *
     * @param config 配置信息
     * @return url地址
     */
    protected String generateRequestUrl(JnlwNetworkReqConfig config) {
        //url处理
        StringBuilder url = new StringBuilder();
        if (JnlwUtils.getInstance().isNotEmpty(config.getBaseUrl())) {
            url.append(config.getBaseUrl());
        } else {
            url.append("http://127.0.0.1");
        }
        if (JnlwUtils.getInstance().isNotEmpty(config.getRequestUrl())) {
            //如果是本地的话判断是否第一位是斜杠，无斜杠要添加
            String first = config.getRequestUrl().substring(0, 1);
            String end = url.substring(url.length() - 1, url.length());
            if (first.matches("[^/?]") && end.matches("[^/?]")) {
                url.append("/");
            }
            url.append(config.getRequestUrl());
        }
        String requestUrl = url.toString();
        url.setLength(0);
        //参数拼接处理
        for (Map.Entry<String, String> entry : config.getRequestDataParams().entrySet()) {
            requestUrl = JnlwUtils.getInstance().addUrlParams(requestUrl, entry.getKey(), entry.getValue());
        }
        return requestUrl;
    }

    /**
     * 设置Get请求
     *
     * @param config 请求配置
     * @return 请求结果
     */
    abstract JnlwHttpRes sendGetRequest(JnlwNetworkReqConfig config);

    /**
     * 设置Put请求
     *
     * @param config 请求配置
     * @return 请求结果
     */
    abstract JnlwHttpRes sendPutRequest(JnlwNetworkReqConfig config);

    /**
     * 设置Post请求
     *
     * @param config 请求配置
     * @return 请求结果
     */
    abstract JnlwHttpRes sendPostRequest(JnlwNetworkReqConfig config);

    /**
     * 设置Delete请求
     *
     * @param config 请求配置
     * @return 请求结果
     */
    abstract JnlwHttpRes sendDeleteRequest(JnlwNetworkReqConfig config);

    /**
     * 设置Options请求
     *
     * @param config 请求配置
     * @return 请求结果
     */
    abstract JnlwHttpRes sendOptionsRequest(JnlwNetworkReqConfig config);
}
