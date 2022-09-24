package com.lorenwang.test.android.activity.customToolsAndroid

import android.Manifest
import android.lorenwang.tools.app.AtlwActivityUtil
import android.lorenwang.tools.app.AtlwPermissionRequestCallback
import android.lorenwang.tools.app.AtlwToastHintUtil
import android.view.View
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import kotlinbase.lorenwang.tools.extend.kttlwToJsonData

/**
 * 功能作用：Activity相关工具类样例
 * 初始注释时间： 2021/9/17 17:40
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
class ActivityActivity : BaseActivity() {

    var inputMethod = false

    override fun setContentViewConfig(resId: Int?)  {
        super.setContentViewConfig(R.layout.activity_custom_tools_android_activity)
    }

    fun mainClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btnPermission -> {
                    AtlwActivityUtil.getInstance().goToRequestPermissions(this,
                        arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_NETWORK_STATE),1231231,
                        object : AtlwPermissionRequestCallback {
                            override fun permissionRequestSuccessCallback(permissionList: MutableList<String>?) {
                                AtlwToastHintUtil.getInstance().toastMsg("权限请求成功")
                            }

                            override fun permissionRequestFailCallback(permissionList: MutableList<String>?) {
                                AtlwToastHintUtil.getInstance().toastMsg("${permissionList?.kttlwToJsonData()}:权限请求失败")
                            }

                        })
                }
                R.id.btnKey -> {
                    AtlwActivityUtil.getInstance().setInputMethodVisibility(this, view, if (inputMethod) View.VISIBLE else View.GONE)
                    inputMethod = !inputMethod
                }
                R.id.btnExitCheck -> {
                    if (AtlwActivityUtil.getInstance().allowExitApp(3000)) {
                        AtlwToastHintUtil.getInstance().toastMsg("当前可以执行退出操作")
                    } else {
                        AtlwToastHintUtil.getInstance().toastMsg("是否允许退出app")
                    }
                }
                R.id.btnExit -> {
                    AtlwActivityUtil.getInstance().exitApp(this)
                }
                R.id.btnFront -> {
                    if (AtlwActivityUtil.getInstance().isOnForeground) {
                        AtlwToastHintUtil.getInstance().toastMsg("当前应用在前台")
                    } else {
                        AtlwToastHintUtil.getInstance().toastMsg("当前应用不在前台")
                    }
                }
                R.id.btnAppName -> {
                    AtlwToastHintUtil.getInstance().toastMsg(AtlwActivityUtil.getInstance().appName)
                }
                R.id.btnRotate -> {
                    AtlwActivityUtil.getInstance().changeActivityScreenOrientation(this)
                }
                R.id.btnLand -> {
                    if (AtlwActivityUtil.getInstance().isPageLandscape(this)) {
                        AtlwToastHintUtil.getInstance().toastMsg("当前状态为横屏")
                    } else {
                        AtlwToastHintUtil.getInstance().toastMsg("当前状态为竖屏")
                    }
                }
                else -> {

                }
            }
        }
    }
}
