package android.lorenwang.commonbaseframe.network.manage;

import android.lorenwang.tools.base.AtlwLogUtils;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 功能作用：
 * 创建时间：2019-12-13 下午 22:24:18
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AcbflwResponseGsonConverter<T> implements Converter<ResponseBody, T> {
    private TypeAdapter<T> adapter;

    public AcbflwResponseGsonConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        try {
            String originalBody = value.string();
            String TAG = "AcbflwResponseGsonConverter";
            AtlwLogUtils.logUtils.logD(TAG, "接收到接口返回数据:" + originalBody);
            return adapter.fromJson(originalBody);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            value.close();
        }
    }
}
