package javabase.lorenwang.network;


import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能作用：网络请求配置
 * 创建时间：2020-12-01 10:15 上午
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
public class JnlwNetworkReqConfig {
    private JnlwNetworkReqConfig() {
    }

    /**
     * 请求基础地址，不传递则请求localHost地址
     */
    private String baseUrl;
    /**
     * 请求地址，不能为空
     */
    private String requestUrl;
    /**
     * 网络请求类型
     */
    private JnlwNetworkTypeEnum networkTypeEnum;
    /**
     * 请求数据参数，requestDataParams和requestDataJson二选一
     */
    private Map<String, String> requestDataParams;
    /**
     * 请求的json数据，requestDataParams和requestDataJson二选一
     */
    private String requestDataJson;
    /**
     * 请求头参数
     */
    private Map<String, String> requestHeaderParams;
    /**
     * 连接超时时间
     */
    private int connectTimeout = 0;
    /**
     * 连接请求超时时间
     */
    private int connectRequestTimeout = 0;
    /**
     * socket超时时间
     */
    private int socketTimeout = 0;
    /**
     * 重连次数
     */
    private int reconnectionCount = 0;
    /**
     * 网络操作回调
     */
    private JnlwNetworkCallback networkCallback;

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public JnlwNetworkTypeEnum getNetworkTypeEnum() {
        return networkTypeEnum;
    }

    public Map<String, String> getRequestDataParams() {
        return requestDataParams;
    }

    public String getRequestDataJson() {
        return requestDataJson;
    }

    public Map<String, String> getRequestHeaderParams() {
        return requestHeaderParams;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getConnectRequestTimeout() {
        return connectRequestTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public JnlwNetworkCallback getNetworkCallback() {
        return networkCallback;
    }

    public int getReconnectionCount() {
        return reconnectionCount;
    }

    public static class Build {
        /**
         * 请求基础地址，不传递则请求localHost地址
         */
        private String baseUrl;
        /**
         * 请求地址，不能为空
         */
        private String requestUrl;
        /**
         * 网络请求类型
         */
        private JnlwNetworkTypeEnum networkTypeEnum = JnlwNetworkTypeEnum.NONE;
        /**
         * 请求数据参数，requestDataParams和requestDataJson二选一
         */
        private Map<String, String> requestDataParams = new HashMap<>();
        /**
         * 请求的json数据，requestDataParams和requestDataJson二选一
         */
        private String requestDataJson;
        /**
         * 请求参数
         */
        private Map<String, String> requestHeaderParams = new HashMap<>();
        /**
         * 连接超时时间
         */
        private int connectTimeout = 15000;
        /**
         * 连接请求超时时间
         */
        private int connectRequestTimeout = 15000;
        /**
         * socket超时时间
         */
        private int socketTimeout = 15000;
        /**
         * 重连次数
         */
        private int reconnectionCount = 0;
        /**
         * 网络操作回调
         */
        private JnlwNetworkCallback networkCallback;

        /**
         * 设置基础url，不传递则请求localHost地址
         *
         * @param baseUrl 基础url地址，不传递则请求localHost地址
         * @return 当前build
         */
        public Build setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        /**
         * 设置请求地址
         *
         * @param requestUrl 请求地址
         * @return 当前build
         */
        public Build setRequestUrl(@NotNull String requestUrl) {
            this.requestUrl = requestUrl;
            return this;
        }

        /**
         * 设置请求类型
         *
         * @param networkTypeEnum 请求类型枚举
         * @return 当前build
         */
        public Build setNetworkTypeEnum(@NotNull JnlwNetworkTypeEnum networkTypeEnum) {
            this.networkTypeEnum = networkTypeEnum;
            return this;
        }

        /**
         * 添加请求数据参数，requestDataParams和requestDataJson二选一
         *
         * @param key   请求key
         * @param value 请求value
         * @return 当前build
         */
        public Build addRequestDataParam(@NotNull String key, @NotNull String value) {
            try {
                requestDataParams.put(key, URLEncoder.encode(value, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                requestDataParams.put(key, value);
            }
            return this;
        }

        /**
         * 请求的json数据，requestDataParams和requestDataJson二选一
         *
         * @param requestDataJson json数据
         * @return 当前build
         */
        public Build setRequestDataJson(String requestDataJson) {
            this.requestDataJson = requestDataJson;
            return this;
        }

        /**
         * 添加请求头参数
         *
         * @param key   请求key
         * @param value 请求value
         * @return 当前build
         */
        public Build addRequestHeaderParam(@NotNull String key, @NotNull String value) {
            requestHeaderParams.put(key, value);
            return this;
        }

        /**
         * 连接超时时间
         *
         * @param connectTimeout 连接超时时间
         * @return 当前build
         */
        public Build setConnectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        /**
         * 设置连接请求超时时间
         *
         * @param connectRequestTimeout 连接请求超时时间
         * @return 当前build
         */
        public Build setConnectRequestTimeout(int connectRequestTimeout) {
            this.connectRequestTimeout = connectRequestTimeout;
            return this;
        }

        /**
         * 设置socket超时时间
         *
         * @param socketTimeout socket超时时间
         * @return 当前build
         */
        public Build setSocketTimeout(int socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        /**
         * 设置网络操作回调
         *
         * @param networkCallback 网络操作回调
         * @return 当前build
         */
        public Build setNetworkCallback(JnlwNetworkCallback networkCallback) {
            this.networkCallback = networkCallback;
            return this;
        }

        /**
         * 设置重连次数
         *
         * @param reconnectionCount 重连次数
         * @return 当前build
         */
        public Build setReconnectionCount(int reconnectionCount) {
            this.reconnectionCount = reconnectionCount;
            return this;
        }

        public JnlwNetworkReqConfig build() {
            JnlwNetworkReqConfig config = new JnlwNetworkReqConfig();
            config.baseUrl = baseUrl;
            config.requestUrl = requestUrl;
            config.networkTypeEnum = networkTypeEnum;
            config.requestDataParams = requestDataParams;
            config.requestDataJson = requestDataJson;
            config.requestHeaderParams = requestHeaderParams;
            config.connectTimeout = connectTimeout;
            config.connectRequestTimeout = connectRequestTimeout;
            config.socketTimeout = socketTimeout;
            config.networkCallback = networkCallback;
            config.reconnectionCount = reconnectionCount;
            return config;
        }

        /**
         * 复制配置信息
         *
         * @param config 配置信息
         * @return 复制的build实例
         */
        public Build copy(JnlwNetworkReqConfig config) {
            this.baseUrl = config.baseUrl;
            this.requestUrl = config.requestUrl;
            this.networkTypeEnum = config.networkTypeEnum;
            this.requestDataParams = config.requestDataParams;
            this.requestDataJson = config.requestDataJson;
            this.requestHeaderParams = config.requestHeaderParams;
            this.connectTimeout = config.connectTimeout;
            this.connectRequestTimeout = config.connectRequestTimeout;
            this.socketTimeout = config.socketTimeout;
            this.networkCallback = config.networkCallback;
            this.reconnectionCount = config.reconnectionCount;
            return this;
        }
    }
}
