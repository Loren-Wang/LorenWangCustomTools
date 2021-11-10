package com.lorenwang.test.android.activity.customToolsAndroid

import android.lorenwang.tools.app.AtlwToastHintUtil
import android.lorenwang.tools.base.AtlwCheckUtil
import android.lorenwang.tools.file.AtlwFileOptionUtil
import android.os.Bundle
import android.view.View
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import com.lorenwang.test.android.databinding.ActivityCustomToolsCheckBinding
import kotlinbase.lorenwang.tools.extend.kttlwClearFirstString
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData

/**
 * 功能作用：一些检查相关的工具类实现页面
 * 初始注释时间： 2021/9/18 16:23
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
class CheckActivity : BaseActivity() {

    private var binding: ActivityCustomToolsCheckBinding? = null
        get() {
            field = field.kttlwGetNotEmptyData { ActivityCustomToolsCheckBinding.inflate(layoutInflater) }
            return field
        }

    override fun initView(savedInstanceState: Bundle?) {
        addShowContentView(true, binding)
    }

    fun mainClick(view: View?) {
        if (view != null) {
            val path = AtlwFileOptionUtil.getInstance().baseStorageDirPath + binding?.edtContent?.text?.toString()?.kttlwClearFirstString("/", true)
            when (view.id) {
                R.id.btnIo -> {
                    if (AtlwCheckUtil.getInstance().checkFileOptionsPermission()) {
                        AtlwToastHintUtil.getInstance().toastMsg("当前有文件操作权限")
                    } else {
                        AtlwToastHintUtil.getInstance().toastMsg("当前没有文件操作权限")
                    }
                }
                R.id.btnFileExit -> {
                    if (AtlwCheckUtil.getInstance().checkFileIsExit(path)) {
                        AtlwToastHintUtil.getInstance().toastMsg("当前有文件存在")
                    } else {
                        AtlwToastHintUtil.getInstance().toastMsg("当前有文件不存在")
                    }
                }
                R.id.btnDirExit -> {
                    if (AtlwCheckUtil.getInstance().checkDirectoryIsExit(path)) {
                        AtlwToastHintUtil.getInstance().toastMsg("当前有文件夹存在")
                    } else {
                        AtlwToastHintUtil.getInstance().toastMsg("当前有文件夹不存在")
                    }
                }
                R.id.btnFileIsImage -> {
                    if (AtlwCheckUtil.getInstance().checkFileIsImage(path)) {
                        AtlwToastHintUtil.getInstance().toastMsg("当前有文件是图片")
                    } else {
                        AtlwToastHintUtil.getInstance().toastMsg("当前有文件不是图片")
                    }
                }
                R.id.btnCheckPermission -> {
                    if (AtlwCheckUtil.getInstance().checkAppPermission(binding?.edtContent?.toString())) {
                        AtlwToastHintUtil.getInstance().toastMsg("当前检测权限已有")
                    } else {
                        AtlwToastHintUtil.getInstance().toastMsg("当前检测权限未有")
                    }
                }
                R.id.btnCheckGps -> {
                    if (AtlwCheckUtil.getInstance().checkGpsIsOpen()) {
                        AtlwToastHintUtil.getInstance().toastMsg("当前已开启gps")
                    } else {
                        AtlwToastHintUtil.getInstance().toastMsg("当前未开启gps")
                    }
                }
                R.id.btnCheckAppInstall -> {
                    if (AtlwCheckUtil.getInstance().checkAppIsInstall(binding?.edtContent?.toString())) {
                        AtlwToastHintUtil.getInstance().toastMsg("当前app已安装")
                    } else {
                        AtlwToastHintUtil.getInstance().toastMsg("当前app未安装")
                    }
                }
                R.id.btnCheckAppRun -> {
                    if (AtlwCheckUtil.getInstance().checkAppIsRunning(binding?.edtContent?.toString())) {
                        AtlwToastHintUtil.getInstance().toastMsg("当前app已运行")
                    } else {
                        AtlwToastHintUtil.getInstance().toastMsg("当前app未运行")
                    }
                }
                R.id.btnCheckServiceRun -> {
                    binding?.edtContent?.toString()?.let {
                        try {
                            if (AtlwCheckUtil.getInstance().checkServiceIsRunning(Class.forName(it))) {
                                AtlwToastHintUtil.getInstance().toastMsg("当前服务已运行")
                            } else {
                                AtlwToastHintUtil.getInstance().toastMsg("当前服务未运行")
                            }
                        } catch (e: Exception) {

                        }
                    }
                }
                else -> {

                }
            }
        }
    }
}
