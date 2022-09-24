package com.lorenwang.test.android.base

import android.lorenwang.commonbaseframe.AcbflwBaseApplication
import android.lorenwang.commonbaseframe.AcbflwBaseConfig.appCompileTypeIsDebug
import android.lorenwang.commonbaseframe.AcbflwBaseConfig.mAppModuleNetDataDispose
import android.lorenwang.commonbaseframe.AcbflwViewInitInterface
import android.lorenwang.commonbaseframe.network.AcbflwRequestBean
import android.lorenwang.commonbaseframe.network.AppModuleNetDataDispose
import android.lorenwang.commonbaseframe.network.callback.AcbflwNetCallback
import android.lorenwang.tools.AtlwConfig
import android.lorenwang.tools.base.AtlwLogUtil
import com.lorenwang.test.android.BuildConfig
import retrofit2.Call

/**
 * 创建时间：2019-04-13 下午 14:30:36
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author wangliang
 */
class BaseApplication : AcbflwBaseApplication() {
    override fun isUsePictureSelectLibrary(): Boolean {
        return false
    }

    override fun isUseVideoPlayLibrary(): Boolean {
        return false
    }

    override fun onCreate() {
        super.onCreate()
        AtlwLogUtil.logUtils = AtlwLogUtil()
        AtlwConfig.initAndroidCustomTools(this, appCompileTypeIsDebug(BuildConfig.APP_COMPILE_TYPE), getExternalFilesDir("logInfo")?.absolutePath)
        mAppModuleNetDataDispose = object : AppModuleNetDataDispose {
            override fun onBefore(call: Call<*>?, retrofitKey: String?) {}
            override fun onAfter(call: Call<*>?, retrofitKey: String?, showLoading: Boolean) {
                if (currentShowActivity is AcbflwViewInitInterface) {
                    (currentShowActivity as AcbflwViewInitInterface).hideBaseLoading()
                }
            }

            override fun onLoading(call: Call<*>?, retrofitKey: String?, showLoading: Boolean) {
                if (currentShowActivity is AcbflwViewInitInterface && showLoading) {
                    (currentShowActivity as AcbflwViewInitInterface).showBaseLoading(false, null)
                }
            }

            override fun onError(call: Call<*>?, retrofitKey: String?, code: String, message: String?) {}
            override fun <T> onGatewaySuccess(call: Call<*>?, retrofitKey: String?, request: AcbflwRequestBean, result: String,
                callback: AcbflwNetCallback<T>) {
                callback.onSuccess(null, result)
//                try{
//                    val reqClass = JtlwClassUtil.getInstance().getSuperClassGenericType<T>(callback.javaClass, 0)
//                    callback.onSuccess(JdplwJsonUtil.fromJson(result, reqClass), result)
//                }catch(ignore:Exception){
//                    callback.onSuccess(null, result)
//                }

            }

            override fun gatewayResponseError(call: Call<Any>?, retrofitKey: String?, requestBean: AcbflwRequestBean, code: String?,
                message: String?): Boolean {
                return false
            }
        }
    }
}