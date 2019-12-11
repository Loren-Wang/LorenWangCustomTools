package android.lorenwang.common_base_frame.network

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import android.lorenwang.common_base_frame.network.manage.AcbflwResponseGsonConverterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * 功能作用：基础api管理器
 * 创建时间：2019-12-11 14:33
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
class AcbflwNetworkManager private constructor() {
    private var lwRetrofit: Retrofit? = null

    companion object {
        private var optionsInstance: AcbflwNetworkManager? = null

        val instance: AcbflwNetworkManager
            get() {
                if (optionsInstance == null) {
                    synchronized(AcbflwNetworkManager::class.java) {
                        if (optionsInstance == null) {
                            optionsInstance = AcbflwNetworkManager()
                        }
                    }
                }
                return optionsInstance as AcbflwNetworkManager
            }
    }

    /**
     * 初始化网络请求器
     * @param baseUrl 基础url链接
     * @param interceptor 网络拦截器
     */
    fun initRetrofit(baseUrl: String, interceptor: Interceptor) {
        val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
        lwRetrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(AcbflwResponseGsonConverterFactory.create()) //使用自定义的gson数据解析器，部分代码取自源码当中的gson解析器
                .build()
    }

    /**
     * 返回自定义的请求实体
     *
     * @param cls 请求类
     * @param <T> 泛型
     * @return 发起的请求实例
    </T> */
    fun <T> create(cls: Class<T>?): T? {
        return lwRetrofit?.create(cls)
    }
}
