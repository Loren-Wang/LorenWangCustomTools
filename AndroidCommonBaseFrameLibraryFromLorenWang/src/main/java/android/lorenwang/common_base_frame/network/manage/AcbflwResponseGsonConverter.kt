package android.lorenwang.common_base_frame.network.manage

import android.lorenwang.tools.base.AtlwLogUtils
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException

/**
 * 创建时间：2019-08-07 15:09
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class AcbflwResponseGsonConverter<T : Any?>
internal constructor(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<ResponseBody, T> {
    private val TAG = javaClass.name
    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        return try {
            val originalBody = value.string()
            AtlwLogUtils.logD(TAG, "接收到接口返回数据：${originalBody}")
            value.use {
                adapter.fromJson(originalBody)
            }
        } catch (e: Exception) {
            throw RuntimeException(e.message)
        } finally {
            value.close()
        }
    }
}
