package android.lorenwang.commonbaseframe.pulgins.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * 功能作用：插件相关的api
 * 创建时间：2019-12-27 17:04
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public interface AcbflwPluginApi {
    @GET
    Observable<AcbflwWeChatResponse> getWeiXinToken(@Url String url);
}
