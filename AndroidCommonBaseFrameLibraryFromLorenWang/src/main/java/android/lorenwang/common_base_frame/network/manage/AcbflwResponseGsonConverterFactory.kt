package android.lorenwang.common_base_frame.network.manage

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * 创建时间：2019-08-07 15:13
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class AcbflwResponseGsonConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {
    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>,
                                       retrofit: Retrofit): Converter<ResponseBody, *> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return AcbflwResponseGsonConverter<Any>(gson, adapter = adapter as TypeAdapter<Any>)
    }

    override fun requestBodyConverter(type: Type, parameterAnnotations: Array<Annotation>, methodAnnotations: Array<Annotation>, retrofit: Retrofit): Converter<*, RequestBody> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return AcbflwRequestGsonConverter<Any>(gson, adapter as TypeAdapter<Any>)
    }

    companion object {
        @JvmOverloads  // Guarding public API nullability.
        fun create(gson: Gson? = Gson()): AcbflwResponseGsonConverterFactory {
            if (gson == null) throw NullPointerException("gson == null")
            return AcbflwResponseGsonConverterFactory(gson)
        }
    }

}
