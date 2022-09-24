package android.lorenwang.commonbaseframe.network;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 功能作用：基础Api
 * 初始注释时间： 2021/6/2 10:27
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
public interface AcbflwBaseApi {

    /**
     * 文件下载
     *
     * @param fileUrl 文件下载地址
     * @return 响应数据
     */
    @Streaming
    @GET()
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    /**
     * 发起get请求
     *
     * @return 响应数据
     */
    @GET()
    Call<Object> sendGetUrlRequest(@Url String url);

    /**
     * 发起post请求
     *
     * @return 响应数据
     */
    @POST()
    Call<Object> sendPostUrlRequest(@Url String url);

    /**
     * 发起get请求
     *
     * @return 响应数据
     */
    @GET()
    Call<Object> sendGetUrlRequest(@Url String url, @Body RequestBody data);

    /**
     * 发起post请求
     *
     * @return 响应数据
     */
    @POST()
    Call<Object> sendPostUrlRequest(@Url String url, @Body RequestBody data);
}
