package android.lorenwang.commonbaseframe.network.manage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 功能作用：
 * 创建时间：2019-12-13 下午 22:16:25
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class AcbflwResponseGsonConverterFactory extends Converter.Factory {
    private Gson gson;

    private AcbflwResponseGsonConverterFactory(Gson gson) {
        this.gson = gson;
    }

    public static AcbflwResponseGsonConverterFactory create() {
        return new AcbflwResponseGsonConverterFactory(new Gson());
    }

    @Override
    public AcbflwResponseGsonConverter responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new AcbflwResponseGsonConverter(gson.getAdapter(TypeToken.get(type)));
    }

    @Override
    public AcbflwRequestGsonConverter requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new AcbflwRequestGsonConverter(gson, gson.getAdapter(TypeToken.get(type)));
    }
}
