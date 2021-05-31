package android.lorenwang.commonbaseframe.network.manage;

import android.lorenwang.commonbaseframe.AcbflwBaseConfig;
import android.lorenwang.tools.base.AtlwLogUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URLDecoder;

import androidx.annotation.NonNull;
import javabase.lorenwang.dataparse.JdplwJsonUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

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
        Request originalRequest = chain.request();
        Request.Builder requestBuilder = originalRequest.newBuilder().header("Accept-Encoding", "UTF-8");
        Request request = requestBuilder.build();
        if (!AcbflwBaseConfig.appCompileTypeIsRelease(appCompileType)) {
            StringBuilder logBuilder = new StringBuilder("*\n\n****  Network_options_start  ****\n\n");
            //请求url
            logBuilder.append("***request_url:").append(originalRequest.url().toString()).append('\n');
            //请求方式
            logBuilder.append("***request_method:").append(originalRequest.method()).append("\n");
            //请求头
            logBuilder.append("***request_heads:\n").append(originalRequest.headers().toMultimap()).append("\n").append(
                    request.headers().toMultimap()).append("\n");
            //请求体
            RequestBody requestBody = originalRequest.body();
            if (requestBody != null) {
                logBuilder.append("***request_body:\n").append(bodyToString(requestBody)).append("\n");
            }
            //响应体
            Response response = chain.proceed(request);
            String body = response.body() != null ? response.body().string() : "";
            logBuilder.append("\n");
            //存储url
            logBuilder.append("***response_code:").append(response.code()).append('\n');
            logBuilder.append("***response_message:").append(response.message()).append('\n');
            //存储响应header
            logBuilder.append("***response_heads:\n").append(JdplwJsonUtils.toJson(response.headers().toMultimap())).append("\n\n");
            //存储响应体
            logBuilder.append("***response_body:\n").append(body.replaceAll(",\"", ",\n\"")).append("\n\n");

            logBuilder.append("****  Network_options_end  ****");
            AtlwLogUtil.logUtils.logI("Network_options", logBuilder.toString());
            System.out.println(logBuilder.toString());
            logBuilder.setLength(0);

            return response.newBuilder().body(ResponseBody.create(response.body().contentType(), body)).build();
        } else {
            return chain.proceed(request);
        }
    }

    /**
     * 将请求提转为String
     */
    @NonNull
    private static String bodyToString(final RequestBody request) {
        try {
            final Buffer buffer = new Buffer();
            if (request != null) {
                request.writeTo(buffer);
            } else {
                return "";
            }
            return URLDecoder.decode(buffer.readUtf8(), "UTF-8");
        } catch (final IOException e) {
            return "";
        }
    }

}
