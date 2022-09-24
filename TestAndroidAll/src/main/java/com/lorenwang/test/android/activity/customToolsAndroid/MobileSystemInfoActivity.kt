package com.lorenwang.test.android.activity.customToolsAndroid

import android.annotation.SuppressLint
import android.lorenwang.tools.app.AtlwActivityUtil
import android.lorenwang.tools.app.AtlwPermissionRequestCallback
import android.lorenwang.tools.mobile.AtlwMobileSystemInfoUtil
import android.os.Bundle
import android.widget.TextView
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import kotlinbase.lorenwang.tools.extend.kttlwToJsonData

/**
 * 功能作用：手机系统信息
 * 初始注释时间： 2021/11/5 15:35
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
class MobileSystemInfoActivity : BaseActivity() {
    override fun setContentViewConfig(resId: Int?)  {
        super.setContentViewConfig(R.layout.activity_custom_tools_android_mobile_system_info)
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        AtlwActivityUtil.getInstance()
            .goToRequestPermissions(this, arrayOf(android.Manifest.permission.READ_PHONE_STATE,), 1231,object : AtlwPermissionRequestCallback {
                @SuppressLint("MissingPermission")
                override fun permissionRequestSuccessCallback(permissionList: MutableList<String>?) {
                    findViewById<TextView>(R.id.tv_show).apply {
                        append("当前手机系统版本号：${AtlwMobileSystemInfoUtil.getInstance().systemVersion}\n")
                        append("当前手机型号：${AtlwMobileSystemInfoUtil.getInstance().systemModel}\n")
                        append("当前手机厂商：${AtlwMobileSystemInfoUtil.getInstance().deviceBrand}\n")
                        append("当前手机系统sdk版本号：${AtlwMobileSystemInfoUtil.getInstance().systemSdkVersion}\n")
                        append("当前手机品牌：${AtlwMobileSystemInfoUtil.getInstance().mobileBrand}\n")
                        append("当前手机IMEI：${AtlwMobileSystemInfoUtil.getInstance().imeiInfo}\n")
                        append("当前手机网络：${
                            AtlwMobileSystemInfoUtil.getInstance().networkType.let {
                                when (it) {
                                    0 -> "无网络"
                                    1 -> "WIFi"
                                    2 -> "WAP"
                                    3 -> "NET"
                                    else -> ""
                                }
                            }
                        }\n")
                        append("当前手机mac地址：${AtlwMobileSystemInfoUtil.getInstance().mac}\n")
                        append("当前手机ip地址：${AtlwMobileSystemInfoUtil.getInstance().ipAddress.kttlwToJsonData()}\n")
                        append("当前手机系统语言：${AtlwMobileSystemInfoUtil.getInstance().systemLanguage}\n")
                        append("当前手机系统语言列表：${AtlwMobileSystemInfoUtil.getInstance().systemLanguageList.kttlwToJsonData()}\n")
                    }
                }

                override fun permissionRequestFailCallback(permissionList: MutableList<String>?) {

                }
            })
    }
}
