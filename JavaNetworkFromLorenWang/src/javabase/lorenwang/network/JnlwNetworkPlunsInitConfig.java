package javabase.lorenwang.network;


/**
 * 功能作用：网络插件初始化配置
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
public class JnlwNetworkPlunsInitConfig {
    /**
     * 线程池最大连接数
     */
    private int connectMaxTotal = 0;
    /**
     * 线程池连接的最大主机数量
     */
    private int connectHostMaxTotal = 0;
    /**
     * 连接超时时间
     */
    private int defaultConnectTimeout = 0;
    /**
     * 连接请求超时时间
     */
    private int defaultConnectRequestTimeout = 0;
    /**
     * socket超时时间
     */
    private int defaultSocketTimeout = 0;

    public int getConnectMaxTotal() {
        return connectMaxTotal;
    }

    public int getConnectHostMaxTotal() {
        return connectHostMaxTotal;
    }

    public int getDefaultConnectTimeout() {
        return defaultConnectTimeout;
    }

    public int getDefaultConnectRequestTimeout() {
        return defaultConnectRequestTimeout;
    }

    public int getDefaultSocketTimeout() {
        return defaultSocketTimeout;
    }

    public static class Build {
        /**
         * 线程池最大连接数
         */
        private int connectMaxTotal = 1000;
        /**
         * 线程池连接的最大主机数量,也是最大并发数量
         */
        private int connectHostMaxTotal = 500;
        /**
         * 连接超时时间
         */
        private int defaultConnectTimeout = 15000;
        /**
         * 连接请求超时时间
         */
        private int defaultConnectRequestTimeout = 15000;
        /**
         * socket超时时间
         */
        private int defaultSocketTimeout = 15000;

        /**
         * 线程池最大连接数
         *
         * @param connectMaxTotal 线程池最大连接数
         * @return 当前build
         */
        public Build setConnectMaxTotal(int connectMaxTotal) {
            this.connectMaxTotal = connectMaxTotal;
            return this;
        }

        /**
         * 线程池连接的最大主机数量
         *
         * @param connectHostMaxTotal 线程池连接的最大主机数量
         * @return 当前build
         */
        public Build setConnectHostMaxTotal(int connectHostMaxTotal) {
            this.connectHostMaxTotal = connectHostMaxTotal;
            return this;
        }

        /**
         * 连接超时时间
         *
         * @param defaultConnectTimeout 连接超时时间
         * @return 当前build
         */
        public Build setDefaultConnectTimeout(int defaultConnectTimeout) {
            this.defaultConnectTimeout = defaultConnectTimeout;
            return this;
        }

        /**
         * 连接请求超时时间
         *
         * @param defaultConnectRequestTimeout 连接请求超时时间
         * @return 当前build
         */
        public Build setDefaultConnectRequestTimeout(int defaultConnectRequestTimeout) {
            this.defaultConnectRequestTimeout = defaultConnectRequestTimeout;
            return this;
        }

        /**
         * socket超时时间
         *
         * @param defaultSocketTimeout socket超时时间
         * @return 当前build
         */
        public Build setDefaultSocketTimeout(int defaultSocketTimeout) {
            this.defaultSocketTimeout = defaultSocketTimeout;
            return this;
        }

        public JnlwNetworkPlunsInitConfig build() {
            JnlwNetworkPlunsInitConfig config = new JnlwNetworkPlunsInitConfig();
            config.connectHostMaxTotal = this.connectHostMaxTotal;
            config.connectMaxTotal = this.connectMaxTotal;
            config.defaultConnectTimeout = this.defaultConnectTimeout;
            config.defaultConnectRequestTimeout = this.defaultConnectRequestTimeout;
            config.defaultSocketTimeout = this.defaultSocketTimeout;
            return config;
        }
    }
}
