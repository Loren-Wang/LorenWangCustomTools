package android.lorenwang.commonbaseframe.network.manage;

import android.lorenwang.commonbaseframe.AcbflwBaseConfig;
import android.lorenwang.tools.base.AtlwLogUtils;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URLDecoder;

import javabase.lorenwang.dataparse.JdplwJsonUtils;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 功能作用：接口操作拦截
 * 创建时间：2020-04-18 4:33 下午
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren wang）
 */

public class AcbflwInterceptor implements Interceptor {
    /**
     * app编译类型
     */
    private final int appCompileType;

    public AcbflwInterceptor(int appCompileType) {
        this.appCompileType = appCompileType;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder()
                .header("Accept-Encoding", "UTF-8");
        Request request = requestBuilder.build();
        if (!AcbflwBaseConfig.appCompileTypeIsRelease(appCompileType)) {
            Response response = chain.proceed(request);
            String body = response.body() != null ? response.body().string() : "";

            StringBuilder logBuilder = new StringBuilder(
                    "*\n\n****  Network_options_start  ****\n\n");
            //存储url
            logBuilder.append("request_url:").append(response.request().url().toString()).append('\n');
            //存储method
            logBuilder.append("request_method:").append(response.request().method()).append("\n\n");
            //存储请求header
            logBuilder.append("**************  request_heads:\n")
                    .append(response.request().headers().toMultimap()).append("\n\n");
            //存储请求体
            logBuilder.append("**************  request_body:\n");
            RequestBody requestBody = response.request().body();
            if (requestBody instanceof FormBody) {
                long size = ((FormBody) requestBody).size();
                for (int i = 0; i < size; i++) {
                    logBuilder.append(((FormBody) requestBody).encodedName(i))
                            .append(":").append(URLDecoder.decode(((FormBody) requestBody).encodedValue(i)))
                            .append("\n");
                }
            }
            logBuilder.append("\n");
            //存储url
            logBuilder.append("response_code:").append(response.code()).append('\n');
            logBuilder.append("response_message:").append(response.message()).append('\n');
            //存储响应header
            logBuilder.append("**************  response_heads:\n")
                    .append(JdplwJsonUtils.toJson(response.headers().toMultimap())).append(
                            "\n\n");
            //存储响应体
            logBuilder.append("**************  response_body:\n")
                    .append(body.replaceAll(",\"",",\n\""))
                    .append("\n\n");

            logBuilder.append("****  Network_options_end  ****");
            AtlwLogUtils.logI("Network_options", logBuilder.toString());
            System.out.println(logBuilder.toString());
            logBuilder.setLength(0);

            return response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), body))
                    .build();
        } else {
            return chain.proceed(request);
        }
    }

    /**
     * 显示网络操作数据
     */
    private void showNetworkOptionsData(Response response, String responseData) {

    }
}
