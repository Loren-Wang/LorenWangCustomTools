package android.lorenwang.commonbaseframe.network

import android.lorenwang.commonbaseframe.network.callback.AcbflwFileDownLoadCallback
import android.lorenwang.commonbaseframe.network.callback.AcbflwNetCallback
import android.lorenwang.commonbaseframe.network.file.AcbflwFileDownLoadBean
import android.lorenwang.commonbaseframe.network.manage.AcbflwInterceptor
import android.lorenwang.commonbaseframe.network.manage.AcbflwResponseGsonConverterFactory
import android.lorenwang.tools.base.AtlwCheckUtil
import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class AcbflwNetManager(private val observable: Call<Any>?, val requestBean: AcbflwRequestBean) {

    companion object {
        /**
         * 客户端map集合
         */
        private val mRetrofitMap = hashMapOf<String, Retrofit>()

        /**
         * 重新设置网络请求
         * @param retrofitKey 记录客户端的key值
         * @param headerInterceptor 请求头拦截器
         * @param otherInterceptor 其他请求拦截器
         * @param baseUrl 请求基础地址，要以"/"结尾
         * @param timeout 请求超时时间，默认30秒，传值单位为毫秒值
         * @param converterFactory 数据解析相关工厂
         */
        @JvmStatic
        fun resetNetwork(appCompileType: Int, retrofitKey: String, baseUrl: String, timeout: Long? = null,
            converterFactory: Array<Converter.Factory>? = null, headerInterceptor: Interceptor? = null, otherInterceptor: Array<Interceptor>? = null,
            needLog: Boolean = true) {
            lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
                val lastRetrofit = mRetrofitMap[retrofitKey]
                //构造okhttp
                var builder = OkHttpClient.Builder()
                //防止无法进行http请求
                builder = builder.connectionSpecs(listOf(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))
                //设置超时时间
                builder = builder.readTimeout(timeout ?: 30000, TimeUnit.MILLISECONDS)
                builder = builder.writeTimeout(timeout ?: 30000, TimeUnit.MILLISECONDS)
                builder = builder.connectTimeout(timeout ?: 30000, TimeUnit.MILLISECONDS)
                //添加默认拦截器
                if (headerInterceptor != null) {
                    builder = builder.addInterceptor(headerInterceptor)
                }
                //添加拦截器
                otherInterceptor?.forEach {
                    builder = builder.addInterceptor(it)
                }
                //是否需要日志
                if (needLog) {
                    builder.addInterceptor(AcbflwInterceptor(appCompileType))
                }
                val factory = Retrofit.Builder().baseUrl(baseUrl).client(builder.build())
                if (converterFactory == null) {
                    var haveDefault = false
                    lastRetrofit?.converterFactories()?.forEach { item ->
                        factory.addConverterFactory(item)
                        if (item is AcbflwResponseGsonConverterFactory) {
                            haveDefault = true
                        }
                    }
                    if (!haveDefault) {
                        factory.addConverterFactory(AcbflwResponseGsonConverterFactory.create())
                    }
                } else {
                    converterFactory.forEach {
                        factory.addConverterFactory(it)
                    }
                }
                mRetrofitMap[retrofitKey] = factory.build()
                mRetrofitMap[retrofitKey]
            }.value
        }

        /**
         * 返回默认请求实体
         * @param cls 请求函数接口class
         */
        @JvmStatic
        fun <T> create(cls: Class<T>?, retrofitKey: String): T? {
            return cls?.let { mRetrofitMap[retrofitKey]?.create(cls) }
        }

        /**
         * 发起get请求
         * @param action 接口地址,使用"."分隔,开始不能是.
         * @param form 是否是form请求
         * @param data 请求数据字符串
         */
        @JvmStatic
        fun sendGetUrlRequest(retrofitKey: String, action: String, data: String?, form: Boolean = false): AcbflwNetManager? {
            if (mRetrofitMap.contains(retrofitKey)) {
                val retrofit = mRetrofitMap[retrofitKey]!!
                val call = create(AcbflwBaseApi::class.java, retrofitKey)
                if (data.isNullOrEmpty()) {
                    call?.sendGetUrlRequest(parmasRequestUrl(retrofit, action))
                } else {
                    call?.sendGetUrlRequest(parmasRequestUrl(retrofit, action), getRequestBody(data, form))
                }?.let {
                    return AcbflwNetManager(it, AcbflwRequestBean(action, data, form))
                }
            }
            return null
        }

        /**
         * 发起post请求
         * @param action 接口地址,使用"."分隔,开始不能是.
         * @param form 是否是form请求
         * @param data 请求数据字符串
         */
        @JvmStatic
        fun sendPostUrlRequest(retrofitKey: String, action: String, data: String?, form: Boolean = false): AcbflwNetManager? {
            if (mRetrofitMap.contains(retrofitKey)) {
                val retrofit = mRetrofitMap[retrofitKey]!!
                val call = create(AcbflwBaseApi::class.java, retrofitKey)
                if (data.isNullOrEmpty()) {
                    call?.sendPostUrlRequest(parmasRequestUrl(retrofit, action))
                } else {
                    call?.sendPostUrlRequest(parmasRequestUrl(retrofit, action), getRequestBody(data, form))
                }?.let {
                    return AcbflwNetManager(it, AcbflwRequestBean(action, data, form))
                }
            }
            return null
        }

        /**
         * 获取requestBody，根据是否是from请求返回不同的请求数据类型以及请求体内容
         * @param bean 请求数据实体
         * @param form 是否是form请求
         */
        @JvmStatic
        fun getRequestBody(bean: String, form: Boolean = false): RequestBody {
            return if (form) {
                RequestBody.create("application/x-www-form-urlencoded;charset=utf-8".toMediaTypeOrNull(), bean)
            } else {
                RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), bean)
            }
        }

        /**
         * 下载文件
         * @param callback 下载回调
         */
        @JvmStatic
        fun downloadFile(appCompileType: Int, retrofitKey: String, bean: AcbflwFileDownLoadBean, callback: AcbflwFileDownLoadCallback) {
            //判断有没有初始化过网络客户端，没有的话需要创建网络客户端
            val tag = "downloadFile"
            if (!mRetrofitMap.containsKey(retrofitKey)) {
                resetNetwork(appCompileType, "tag", "https://www.baidu.com/")
            }
            //权限检测处理
            if (AtlwCheckUtil.getInstance().checkIOUtilsOptionsPermissionAndObjects()) {
                val downloadFile = create(AcbflwBaseApi::class.java, tag)?.downloadFile(bean.fileUrlPath)
                if (downloadFile == null) {
                    Log.i(tag, "文件下载地址为空，无法下载")
                    callback.downloadFail(bean)
                } else {
                    Log.i(tag, "开始下载文件：文件地址：${bean.fileUrlPath}")
                    downloadFile.enqueue(object : retrofit2.Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            thread {
                                if (response.body() == null) {
                                    Log.i(tag, "待下载文件异常")
                                    callback.downloadFail(bean)
                                    return@thread
                                }
                                var inputStream: InputStream? = null//输入流
                                var outputStream: FileOutputStream? = null//输出流
                                val buffer = ByteArray(8192)//缓存
                                var bufferSize: Int//当前缓存的大小
                                var writeSize = 0L//已写入大小
                                val size: Long//总大小
                                var lastProgress = 0

                                //创建文件夹
                                val saveFile = File("${bean.fileDirPath}/${bean.saveFileName}")
                                if (saveFile.parentFile?.exists() != true) {
                                    saveFile.parentFile!!.mkdirs()
                                }
                                try {
                                    inputStream = response.body()!!.byteStream()
                                    outputStream = FileOutputStream(saveFile)
                                    size = response.body()!!.contentLength()
                                    while (inputStream.read(buffer).also { length -> bufferSize = length } != -1) {
                                        writeSize += bufferSize
                                        outputStream.write(buffer, 0, bufferSize)
                                        (writeSize.toFloat() / size * 100).toInt().let { current ->
                                            if (current != lastProgress) {
                                                lastProgress = current
                                                Log.i(tag, "文件下载进度：${current.toFloat() / 100}")
                                                callback.updateProgress(current)
                                            }
                                        }
                                    }
                                    Log.i(tag, "文件下载完成，存储地址：${saveFile.absoluteFile}")
                                    callback.downloadSuccess(bean, saveFile.absolutePath)
                                    outputStream.flush()
                                } catch (e: Exception) {
                                    Log.i(tag, "文件下载异常：${e.message}")
                                    callback.downloadFail(bean)
                                } finally {
                                    inputStream?.close()
                                    outputStream?.close()
                                }
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.i(tag, "文件下载异常：${t.message}")
                            callback.downloadFail(bean)
                        }
                    })
                }
            } else {
                Log.i(tag, "没有文件下载权限")
                callback.downloadFail(bean)
            }
        }


        /**
         * 除去末尾的0 字符操作
         *
         * @param str 要格式化的字符串
         * @return 去除后操作
         */
        @JvmStatic
        fun clearEndZeroAndParamsForDouble(str: String?): String? {
            var data = str
            if (!data.isNullOrEmpty() && data.indexOf(".") > 0) {
                data = data.replace("\\.0+$".toRegex(), "") //去掉多余的0
                if (data.contains("\\.\\d*0*$")) {
                    data = data.replace("0+$".toRegex(), "") //去掉多余的0
                }
            }
            return data
        }

        /**
         * 格式化请求地址
         */
        @JvmStatic
        fun parmasRequestUrl(retrofit: Retrofit, action: String): String {
            var baseUrl = retrofit.baseUrl().toString()
            if (baseUrl.lastIndexOf("/") == baseUrl.length - 1) {
                baseUrl = baseUrl.substring(0, baseUrl.length - 1)
            }
            val path = action.replace(".", "/")
            return if (path.indexOf(":") == 0 || path.indexOf("/") == 0) {
                "${baseUrl}${path}"
            } else {
                "${baseUrl}/${path}"
            }

        }
    }

    /**
     * 发起请求
     * @param callback 结果回调
     * @param showLoading 是否显示加载中
     */
    fun <T> execute(callback: AcbflwNetCallback<T>, showLoading: Boolean = true) {
        callback.onBefore()
        if (showLoading) {
            callback.onLoading(true)
        }
        if (observable != null) {
            observable.enqueue(object : retrofit2.Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    callback.onResponse(requestBean, response, showLoading)
                }

                override fun onFailure(call: Call<Any>, t: Throwable) {
                    callback.onResponseError(requestBean, t, showLoading)
                }
            })
        } else {
            callback.onError(HTTP_ERROR_SYSTEM, "")
            callback.onAfter(showLoading)
        }
    }
}