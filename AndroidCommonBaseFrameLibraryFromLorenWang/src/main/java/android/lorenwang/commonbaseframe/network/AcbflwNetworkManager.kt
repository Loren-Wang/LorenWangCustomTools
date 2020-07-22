package android.lorenwang.commonbaseframe.network

import android.lorenwang.commonbaseframe.network.manage.AcbflwInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import android.lorenwang.commonbaseframe.network.manage.AcbflwResponseGsonConverterFactory
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.*
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
open class AcbflwNetworkManager private constructor() {
    private var lwRetrofit: Retrofit? = null
    private var lwDownloadRetrofit: Retrofit? = null

    companion object {
        private var optionsInstance: AcbflwNetworkManager? = null

        @JvmStatic
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
    fun initRetrofit(baseUrl: String, interceptor: Array<Interceptor>?,
                     converterFactory: Converter.Factory?) {
        var builder = OkHttpClient.Builder()
        //防止无法进行http请求
        builder = builder.connectionSpecs(listOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))
        //设置超时时间
        builder = builder.readTimeout(30, TimeUnit.SECONDS)
        //添加默认日志拦截器
        builder = builder.addInterceptor(AcbflwInterceptor())
        //添加拦截器
        interceptor?.forEach {
            builder = builder.addInterceptor(it)
        }
        lwRetrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(converterFactory
                        ?: AcbflwResponseGsonConverterFactory.create()) //使用自定义的gson数据解析器，部分代码取自源码当中的gson解析器
                .build()
        //下载请求实例不使用Gson解析器
        lwDownloadRetrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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

    /**
     * 返回自定义的下载请求实体
     * @param cls 请求api类
     * @param <T> api泛型类
     * @return 发起的下载请求实例
     */
    fun <T> createDownload(cls: Class<T>?): T? {
        return lwDownloadRetrofit?.create(cls)
    }
}