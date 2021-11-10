package android.lorenwang.commonbaseframe.network

import android.lorenwang.commonbaseframe.AcbflwBaseCommonKey.KEY_BASE_API_URL
import android.lorenwang.commonbaseframe.AcbflwBaseCommonKey.KEY_BASE_H5_URL
import android.lorenwang.commonbaseframe.AcbflwBaseCommonKey.KEY_BASE_SHARE_URL
import android.lorenwang.commonbaseframe.AcbflwBaseCommonKey.KEY_BASE_URL_RECORDS
import android.lorenwang.commonbaseframe.AcbflwBaseConfig
import android.lorenwang.commonbaseframe.network.manage.AcbflwInterceptor
import android.lorenwang.commonbaseframe.network.manage.AcbflwResponseGsonConverterFactory
import android.lorenwang.tools.app.AtlwSharedPrefUtil
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javabase.lorenwang.dataparse.JdplwJsonUtil
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData
import kotlinbase.lorenwang.tools.extend.kttlwIsEmpty
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
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
open class AcbflwNetworkManager private constructor() {
    /**
     * 正常网络请求retrofit
     */
    private var lwRetrofit: Retrofit? = null

    /**
     * 下载请求实例不使用Gson解析器
     */
    private var lwDownloadRetrofit: Retrofit? = null

    /**
     * 记录的拦截器列表
     */
    private var interceptors: Array<Interceptor>? = null

    /**
     * 记录的json数据解析器
     */
    private var converterFactory: Converter.Factory? = null

    /**
     * 分享基础Url
     */
    var shareBaseUrl = ""
        private set

    /**
     * h5基础url
     */
    var h5BaseUrl = ""
        private set

    /**
     * api基础Url
     */
    var apiBaseUrl = ""
        private set

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
     * 初始化网络请求
     * @param appCompileType 当前编译类型
     * @param baseUrl 基础网络请求地址
     * @param shareBaseUrl 基础的分享地址
     * @param h5BaseUrl 基础的网页数据加载地址
     * @param timeout 网络请求超时时间，毫秒值，默认为30秒,即30000毫秒
     * @param converterFactory json数据解析器
     * @param interceptor 拦截器列表
     */
    fun initRetrofit(appCompileType: Int, baseUrl: String, shareBaseUrl: String?, h5BaseUrl: String?, timeout: Long?,
        converterFactory: Converter.Factory?, interceptor: Array<Interceptor>?) {
        this.converterFactory = converterFactory
        this.interceptors = interceptor
        //正式情况下设置什么地址就是什么地址，不允许读取缓存数据
        if (AcbflwBaseConfig.appCompileTypeIsRelease(appCompileType)) {
            this.apiBaseUrl = baseUrl.kttlwGetNotEmptyData()
            this.shareBaseUrl = shareBaseUrl.kttlwGetNotEmptyData()
            this.h5BaseUrl = h5BaseUrl.kttlwGetNotEmptyData()
        } else { //非正式环境读取缓存数据
            this.apiBaseUrl = AtlwSharedPrefUtil.getInstance().getString(KEY_BASE_API_URL, baseUrl.kttlwGetNotEmptyData())
            this.shareBaseUrl = AtlwSharedPrefUtil.getInstance().getString(KEY_BASE_SHARE_URL, shareBaseUrl.kttlwGetNotEmptyData())
            this.h5BaseUrl = AtlwSharedPrefUtil.getInstance().getString(KEY_BASE_H5_URL, h5BaseUrl.kttlwGetNotEmptyData())
        }

        //构造okhttp
        var builder = OkHttpClient.Builder()
        //防止无法进行http请求
        builder = builder.connectionSpecs(listOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))
        //设置超时时间
        builder = builder.readTimeout(timeout ?: 30000, TimeUnit.MILLISECONDS)
        builder = builder.writeTimeout(timeout ?: 30000, TimeUnit.MILLISECONDS)
        builder = builder.connectTimeout(timeout ?: 30000, TimeUnit.MILLISECONDS)
        //添加拦截器
        interceptor?.forEach {
            builder = builder.addInterceptor(it)
        }
        //添加默认日志拦截器,后加
        builder = builder.addInterceptor(AcbflwInterceptor(appCompileType))
        lwRetrofit = Retrofit.Builder().baseUrl(this.apiBaseUrl).client(builder.build()).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(converterFactory ?: AcbflwResponseGsonConverterFactory.create()) //使用自定义的gson数据解析器，部分代码取自源码当中的gson解析器
            .build()
        //下载请求实例不使用Gson解析器，同时不需要拦截器
        builder = OkHttpClient.Builder()
        //防止无法进行http请求
        builder = builder.connectionSpecs(listOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))
        //设置超时时间
        builder = builder.readTimeout(timeout ?: 30000, TimeUnit.MILLISECONDS)
        builder = builder.writeTimeout(timeout ?: 30000, TimeUnit.MILLISECONDS)
        builder = builder.connectTimeout(timeout ?: 30000, TimeUnit.MILLISECONDS)
        lwDownloadRetrofit =
            Retrofit.Builder().baseUrl(this.apiBaseUrl).client(builder.build()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    }

    /**
     * 修改url配置信息
     * @param appCompileType 当前编译类型
     * @param baseUrl 基础网络请求地址
     * @param shareBaseUrl 基础的分享地址
     * @param h5BaseUrl 基础的网页数据加载地址
     * @param timeout 网络请求超时时间，毫秒值，默认为30秒,即30000毫秒
     */
    fun changeUrlConfig(appCompileType: Int, baseUrl: String, shareBaseUrl: String?, h5BaseUrl: String?, timeout: Long?) {
        if (!AcbflwBaseConfig.appCompileTypeIsRelease(appCompileType) && baseUrl.isNotEmpty()) {
            var changeShareUrl = shareBaseUrl
            var changeH5Url = h5BaseUrl
            val recodeUrls: Array<Array<String>> = getRecordUrls() //更新空数据处理
            if (recodeUrls.isNotEmpty()) {
                changeH5Url.kttlwIsEmpty().let {
                    if (recodeUrls[0].isNotEmpty()) {
                        changeH5Url = recodeUrls[0][1]
                    }
                }
                changeShareUrl.kttlwIsEmpty().let {
                    if (recodeUrls[0].isNotEmpty()) {
                        changeShareUrl = recodeUrls[0][2]
                    }
                }
            } //重新初始化配置
            initRetrofit(appCompileType, baseUrl, shareBaseUrl, h5BaseUrl, timeout, this.converterFactory, this.interceptors) //记录数据
            AtlwSharedPrefUtil.getInstance().putString(KEY_BASE_API_URL, baseUrl)
            AtlwSharedPrefUtil.getInstance().putString(KEY_BASE_H5_URL, changeH5Url)
            AtlwSharedPrefUtil.getInstance().putString(KEY_BASE_SHARE_URL, changeShareUrl) //记录数据,首先判断列表中是否有该数据

            //记录数据,首先判断列表中是否有该数据
            var haveApiUrl = false
            for (showUrl in recodeUrls) {
                if (baseUrl == showUrl[0]) {
                    haveApiUrl = true
                    break
                }
            }
            if (!haveApiUrl) {
                val urls = arrayOfNulls<Array<String>>(recodeUrls.size + 1)
                urls[recodeUrls.size] = arrayOf(baseUrl, h5BaseUrl.kttlwGetNotEmptyData(), shareBaseUrl.kttlwGetNotEmptyData())
                System.arraycopy(recodeUrls, 0, urls, 0, recodeUrls.size)
                AtlwSharedPrefUtil.getInstance().putString(KEY_BASE_URL_RECORDS, JdplwJsonUtil.toJson(urls))
            }
        }
    }

    /**
     * 返回自定义的请求实体
     *
     * @param cls 请求类
     * @param <T> 泛型
     * @return 发起的请求实例
    </T> */
    fun <T> create(cls: Class<T>?): T? {
        return cls?.let { lwRetrofit?.create(cls) }
    }

    /**
     * 返回自定义的下载请求实体
     * @param cls 请求api类
     * @param <T> api泛型类
     * @return 发起的下载请求实例
     */
    fun <T> createDownload(cls: Class<T>?): T? {
        return cls?.let { lwDownloadRetrofit?.create(cls) }
    }

    /**
     * 获取显示的url数组
     *
     * @return 二维数组，一级数组中的每一个元素内的数据从头至尾分别代表着基础URl，h5基础Url，分享基础Url
     */
    fun getRecordUrls(): Array<Array<String>> {
        return JdplwJsonUtil.fromJson(AtlwSharedPrefUtil.getInstance().getString(KEY_BASE_URL_RECORDS, ""), Array<Array<String>>::class.java)
            ?: arrayOf()
    }
}
