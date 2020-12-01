package javabase.lorenwang.network;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.concurrent.TimeUnit;

import javabase.lorenwang.tools.thread.JtlwTimingTaskUtils;
import okhttp3.OkHttpClient;

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
public class JnlwHttpClientReqFactory {
    /**
     * 网络插件初始化配置
     */
    private static JnlwNetworkPlunsInitConfig initConfig;
    /**
     * httpClient请求实例
     */
    private static volatile JnlwHttpClientReq httpClientReq;
    /**
     * okhttp请求实例
     */
    private static volatile JnlwOkHttpReq okHttpReq;

    /**
     * 设置初始配置信息
     *
     * @param initConfig 初始配置信息
     */
    public static void setInitConfig(JnlwNetworkPlunsInitConfig initConfig) {
        JnlwHttpClientReqFactory.initConfig = initConfig;
    }

    /**
     * 获取httpClient请求实例
     *
     * @return httpClient请求实例
     */
    public static JnlwBaseReq getHttpClientRequest() {
        initHttpClientReq();
        return httpClientReq;
    }

    /**
     * 获取OkHttp请求实例
     *
     * @return OkHttp请求实例
     */
    public static JnlwOkHttpReq getOkHttpRequest() {
        initHttpClientReq();
        return okHttpReq;
    }

    /**
     * 初始化HttpClient请求实例
     */
    static void initHttpClientReq() {
        if (httpClientReq == null) {
            synchronized (JnlwHttpClientReq.class) {
                if (httpClientReq == null) {
                    httpClientReq = new JnlwHttpClientReq();
                    //线程池处理
                    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
                    if (initConfig != null) {
                        //设置线程池最大连接数
                        cm.setMaxTotal(initConfig.getConnectMaxTotal());
                        cm.setDefaultMaxPerRoute(initConfig.getConnectHostMaxTotal());
                    }
                    //客户端初始化
                    CloseableHttpClient client = HttpClients.custom().setConnectionManager(cm).disableAutomaticRetries().build();
                    //设置客户端
                    httpClientReq.setRequestClient(client);
                    //启动定时清理线程
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            // 关闭过期的链接
                            cm.closeExpiredConnections();
                            // 选择关闭 空闲30秒的链接
                            cm.closeIdleConnections(30, TimeUnit.SECONDS);
                            //30s后再次清理
                            JtlwTimingTaskUtils.getInstance().schedule(httpClientReq.hashCode(), this, 30000L);
                        }
                    };
                    //30s后清理
                    JtlwTimingTaskUtils.getInstance().schedule(httpClientReq.hashCode(), runnable, 30000L);
                }
            }
        }
    }

    /**
     * 初始化OkHttp请求实例
     */
    static void initOkHttpReq() {
        if (okHttpReq == null) {
            synchronized (JnlwOkHttpReq.class) {
                if (okHttpReq == null) {
                    okHttpReq = new JnlwOkHttpReq();
                }
                OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder();
                OkHttpClient client;
                if (initConfig != null) {
                    clientBuilder.connectTimeout(initConfig.getDefaultConnectTimeout(), TimeUnit.MILLISECONDS);
                    clientBuilder.readTimeout(initConfig.getDefaultConnectRequestTimeout(), TimeUnit.MILLISECONDS);
                    clientBuilder.writeTimeout(initConfig.getDefaultConnectRequestTimeout(), TimeUnit.MILLISECONDS);
                    client = clientBuilder.build();
                    client.dispatcher().setMaxRequests(initConfig.getConnectMaxTotal());
                    client.dispatcher().setMaxRequestsPerHost(initConfig.getConnectHostMaxTotal());
                } else {
                    client = clientBuilder.build();
                }
                okHttpReq.setRequestClient(client);
            }
        }
    }
}
