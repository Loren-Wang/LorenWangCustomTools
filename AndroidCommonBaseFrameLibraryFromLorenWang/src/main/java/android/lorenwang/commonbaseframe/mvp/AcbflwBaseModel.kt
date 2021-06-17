package android.lorenwang.commonbaseframe.mvp

import android.app.Activity
import android.lorenwang.commonbaseframe.AcbflwBaseApplication
import android.lorenwang.commonbaseframe.R
import android.lorenwang.commonbaseframe.network.AcbflwNetworkManager
import android.lorenwang.commonbaseframe.network.callback.AcbflwFileDownLoadCallback
import android.lorenwang.commonbaseframe.network.callback.AcbflwNetOptionsByModelCallback
import android.lorenwang.commonbaseframe.network.file.AcbflwFileDownLoadBean
import android.lorenwang.tools.file.AtlwFileOptionUtil
import android.lorenwang.tools.mobile.AtlwMobileSystemInfoUtil
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javabase.lorenwang.dataparse.JdplwJsonUtils
import javabase.lorenwang.tools.JtlwLogUtils
import kotlinbase.lorenwang.tools.common.bean.KttlwBaseNetResponseBean
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException


/**
 * 功能作用：基础model
 * 初始注释时间： 2021/1/17 8:55 下午
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
open class AcbflwBaseModel {
    protected val tag = javaClass.name

    /**
     *
     * 请求的记录器
     */
    private var compositeDisposable = CompositeDisposable()

    /**
     * 释放
     */
    fun releaseModel() {
        compositeDisposable.clear()
        JtlwLogUtils.logUtils.logI(tag, "释放了当前model所有网络请求！")
    }

    /**
     * 获取响应处理Observer
     * @param activity 页面实例
     * @param pageIndex 页码
     * @param pageCount 页面数量
     * @param netOptionsCallback 请求结果回调
     */
    open fun <D, T : KttlwBaseNetResponseBean<D>, CB : AcbflwNetOptionsByModelCallback<D, T>> getBaseObserver(activity: Activity?, pageIndex: Int?,
        pageCount: Int?, netOptionsCallback: CB): Observer<Response<T>> {
        //添加model实例
        AcbflwBaseApplication.application?.addModel(activity, this)
        return object : Observer<Response<T>> {
            override fun onComplete() {
                setPageInfo()
                netOptionsCallback.onCompleteFinish()
            }

            override fun onSubscribe(d: Disposable) {
                setPageInfo()
                compositeDisposable.add(d)
                netOptionsCallback.onSubscribeStart()
            }

            override fun onNext(t: Response<T>) {
                setPageInfo()
                if (t.code() == 200) {
                    val repCode = t.body()?.stateCode
                    if (repCode == AcbflwNetRepCode.repCodeSuccess) {
                        //网络请求成功
                        netOptionsCallback.success(t.body()!!)
                    } else {
                        AcbflwNetRepCode.repCodeLoginStatusError.forEach {
                            if (it == repCode) {
                                //用户登陆状态异常，需要跳转到登陆页面
                                netOptionsCallback.userLoginStatusError(it, t.body()?.stateMessage)
                                return
                            }
                        }
                        JtlwLogUtils.logUtils.logE(tag, t.code().toString())
                        netOptionsCallback.error(Exception(JdplwJsonUtils.toJson(t.body())))
                    }
                } else {
                    JtlwLogUtils.logUtils.logE(tag, t.code().toString())
                    netOptionsCallback.error(Exception(JdplwJsonUtils.toJson(t.body())))
                }
            }

            override fun onError(e: Throwable) {
                when (e) {
                    is HttpException, is UnknownHostException -> {
                        //判断是无网络还是其他问题
                        try {
                            if (AtlwMobileSystemInfoUtil.getInstance().networkType == 0) {
                                JtlwLogUtils.logUtils.logE(tag, AcbflwBaseApplication.appContext?.getString(R.string.net_error_net))
                            } else {
                                JtlwLogUtils.logUtils.logE(tag, AcbflwBaseApplication.appContext?.getString(R.string.net_error_server))
                            }
                        } catch (e: Exception) {
                            JtlwLogUtils.logUtils.logE(tag, AcbflwBaseApplication.appContext?.getString(R.string.net_error_net))
                        }
                    }
                    is SocketTimeoutException -> {
                        //判断是无网络还是其他问题
                        try {
                            if (AtlwMobileSystemInfoUtil.getInstance().networkType == 0) {
                                JtlwLogUtils.logUtils.logE(tag, AcbflwBaseApplication.appContext?.getString(R.string.net_error_net))
                            } else {
                                JtlwLogUtils.logUtils.logE(tag, AcbflwBaseApplication.appContext?.getString(R.string.net_error_timeout))
                            }
                        } catch (e: Exception) {
                            JtlwLogUtils.logUtils.logE(tag, AcbflwBaseApplication.appContext?.getString(R.string.net_error_timeout))
                        }
                    }
                    is SSLException -> {
                        //判断是无网络还是其他问题
                        try {
                            if (AtlwMobileSystemInfoUtil.getInstance().networkType == 0) {
                                JtlwLogUtils.logUtils.logE(tag, AcbflwBaseApplication.appContext?.getString(R.string.net_error_net))
                            } else {
                                JtlwLogUtils.logUtils.logE(tag, AcbflwBaseApplication.appContext?.getString(R.string.net_error_sll))
                            }
                        } catch (e: Exception) {
                            JtlwLogUtils.logUtils.logE(tag, AcbflwBaseApplication.appContext?.getString(R.string.net_error_sll))
                        }
                    }
                    else -> {

                    }
                }
                setPageInfo()
                netOptionsCallback.error(e)
            }

            /**
             * 设置分页信息
             */
            fun setPageInfo() {
                netOptionsCallback.pageIndex = pageIndex
                netOptionsCallback.pageCount = pageCount
            }
        }
    }

    /**
     * 文件下载
     */
    fun downloadFile(bean: AcbflwFileDownLoadBean, callback: AcbflwFileDownLoadCallback) {
        AcbflwNetworkManager.instance.createDownload(AcbflwBaseApi::class.java)?.downloadFile(bean.fileUrlPath)?.subscribeOn(Schedulers.io())
            ?.subscribe(object : Observer<Response<ResponseBody>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(body: Response<ResponseBody>) {
                    val inputStream = body.body()?.byteStream()
                    val contentLength = body.body()?.contentLength()
                    if (inputStream == null || contentLength == null) {
                        callback.downloadFail(bean)
                        return
                    }

                    val localFile = File("${bean.fileDirPath}/${bean.saveFileName}")
                    AtlwFileOptionUtil.getInstance().createDirectory(true, localFile.absolutePath, true)
                    val fileOutputStream = FileOutputStream(localFile)
                    try {
                        val bytes = ByteArray(1024)
                        var rendLength = 0
                        var currLength: Long = 0
                        while (rendLength.let { rendLength = inputStream.read(bytes);rendLength } != -1) {
                            fileOutputStream.write(bytes, 0, rendLength)
                            fileOutputStream.flush()
                            currLength += rendLength
                            callback.updateProgress((currLength * 100.0 / contentLength).toInt())
                        }
                        callback.downloadSuccess(bean, localFile.absolutePath)
                    } catch (ignore: Exception) {
                        callback.downloadFail(bean)
                    } finally {
                        fileOutputStream.close()
                        inputStream.close()
                    }
                }

                override fun onError(e: Throwable) {
                    callback.downloadFail(bean)
                }

                override fun onComplete() {
                }
            })
    }
}
