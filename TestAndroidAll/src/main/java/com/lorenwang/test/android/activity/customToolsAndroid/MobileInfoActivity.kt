package com.lorenwang.test.android.activity.customToolsAndroid

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.lorenwang.commonbaseframe.network.callback.AcbflwFileDownLoadCallback
import android.lorenwang.commonbaseframe.network.file.AcbflwFileDownLoadBean
import android.lorenwang.tools.AtlwConfig
import android.lorenwang.tools.app.AtlwActivityUtil
import android.lorenwang.tools.app.AtlwPermissionRequestCallback
import android.lorenwang.tools.app.AtlwScreenUtil
import android.lorenwang.tools.app.AtlwToastHintUtil
import android.lorenwang.tools.base.AtlwAPKPackageNameList
import android.lorenwang.tools.file.AtlwFileOptionUtil
import android.lorenwang.tools.image.loading.AtlwImageLoadConfig
import android.lorenwang.tools.image.loading.AtlwImageLoadingFactory
import android.lorenwang.tools.mobile.AtlwMobileContentUtil
import android.lorenwang.tools.mobile.AtlwMobileOptionsUtil
import android.os.Bundle
import android.view.View
import com.lorenwang.test.android.BuildConfig
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import com.lorenwang.test.android.base.BasePresenter
import com.lorenwang.test.android.databinding.ActivityCustomToolsAndroidMobileInfoBinding
import javabase.lorenwang.tools.common.JtlwDateTimeUtil
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData
import java.io.File

/**
 * 功能作用：手机信息相关页面
 * 初始注释时间： 2021/10/14 15:47
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
class MobileInfoActivity : BaseActivity() {

    private var binding: ActivityCustomToolsAndroidMobileInfoBinding? = null
        get() {
            field = field.kttlwGetNotEmptyData { ActivityCustomToolsAndroidMobileInfoBinding.inflate(layoutInflater) }
            return field
        }

    private var preseter: BasePresenter? = null
        get() {
            field = field.kttlwGetNotEmptyData { BasePresenter(this) }
            return field
        }

    private val cameraRequestCode = 1
    private val photosRequestCode = 2

    /**
     * 拍照存储地址
     */
    private var cameraPath: String? = null

    override fun initView(savedInstanceState: Bundle?) {
        addShowContentView(true, binding)
    }

    fun mainClick(view: View?) {
        if (view != null) {
            binding?.tvShow?.text = ""
            binding?.ivShow?.setImageBitmap(null)
            when (view.id) {
                R.id.btn_contact -> {
                    AtlwActivityUtil.getInstance()
                        .goToRequestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 0, object : AtlwPermissionRequestCallback {
                            @SuppressLint("MissingPermission")
                            override fun permissionRequestSuccessCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                                AtlwMobileContentUtil.getInstance().allContacts.let {
                                    for (bean in it) {
                                        appendText("${bean.contactName}-${bean.phoneNumber}\n")
                                    }
                                }
                            }

                            override fun permissionRequestFailCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                                AtlwToastHintUtil.getInstance().toastMsg("联系人权限请求失败")
                            }
                        })
                }
                R.id.btn_system_msg -> {
                    AtlwActivityUtil.getInstance()
                        .goToRequestPermissions(this, arrayOf(Manifest.permission.READ_SMS), 0, object : AtlwPermissionRequestCallback {
                            @SuppressLint("MissingPermission")
                            override fun permissionRequestSuccessCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                                AtlwMobileContentUtil.getInstance().systemSms.let {
                                    for (bean in it) {
                                        appendText("发送人：${bean.sendAddress}\n发送时间：${
                                            JtlwDateTimeUtil.getInstance().getFormatDateTime("yyyy-MM-dd hh:mm:ss", bean.sendDate)
                                        }\n接收时间：${
                                            JtlwDateTimeUtil.getInstance().getFormatDateTime("yyyy-MM-dd hh:mm:ss", bean.receiveDate)
                                        }\n内容：${bean.body}\n\n")
                                    }
                                }
                            }

                            override fun permissionRequestFailCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                                AtlwToastHintUtil.getInstance().toastMsg("系统短信权限请求失败")
                            }
                        })
                }
                R.id.btn_all_sms -> {
                    AtlwActivityUtil.getInstance()
                        .goToRequestPermissions(this, arrayOf(Manifest.permission.READ_SMS), 0, object : AtlwPermissionRequestCallback {
                            @SuppressLint("MissingPermission")
                            override fun permissionRequestSuccessCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                                AtlwMobileContentUtil.getInstance().allSms.let {
                                    for (bean in it) {
                                        appendText("发送人：${bean.sendAddress}\n发送时间：${
                                            JtlwDateTimeUtil.getInstance().getFormatDateTime("yyyy-MM-dd hh:mm:ss", bean.sendDate)
                                        }\n接收时间：${
                                            JtlwDateTimeUtil.getInstance().getFormatDateTime("yyyy-MM-dd hh:mm:ss", bean.receiveDate)
                                        }\n内容：${bean.body}\n\n")
                                    }
                                }
                            }

                            override fun permissionRequestFailCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                                AtlwToastHintUtil.getInstance().toastMsg("系统短信权限请求失败")
                            }
                        })
                }
                R.id.btn_install_apk -> {
                    preseter?.downLoadFile(1, this, AcbflwFileDownLoadBean("https://imtt.dd.qq.com/16891/apk/478D7DCCC946969FF6EC42FDD876800F.apk",
                        AtlwFileOptionUtil.getInstance().baseStorageDirPath, "aaa.apk"), object : AcbflwFileDownLoadCallback {
                        override fun updateProgress(progress: Int) {
                            appendText("当前下载进度：${progress}\n")
                        }

                        override fun downloadFail(bean: AcbflwFileDownLoadBean) {
                            appendText("文件下载失败")
                        }

                        override fun downloadSuccess(bean: AcbflwFileDownLoadBean, absolutePath: String) {
                            appendText("文件下载成功，开始执行安装程序")
                            AtlwMobileOptionsUtil.getInstance()
                                .installApp(this@MobileInfoActivity, "${BuildConfig.APPLICATION_ID}.fileprovider", absolutePath)
                        }

                    })
                }
                R.id.btn_jump_to_permission_setting -> {
                    AtlwMobileOptionsUtil.getInstance().jumpToAppPermissionSettingPage(this, packageName)
                }
                R.id.btn_jump_to_other_app -> {
                    AtlwMobileOptionsUtil.getInstance().launchApp(AtlwAPKPackageNameList.MAP_GAODE, null, this)
                }
                R.id.btn_copy -> {
                    val text = "asfds阿斯顿发阿斯顿发安抚"
                    appendText("复制的内容${text}")
                    AtlwMobileOptionsUtil.getInstance().copyContentToClipboard("asfa", text)
                }
                R.id.btn_vibrate -> {
                    AtlwMobileOptionsUtil.getInstance().vibrate(1000)
                }
                R.id.btn_open_camera -> {
                    appendText("开启相机前权限请求")
                    AtlwActivityUtil.getInstance().goToRequestPermissions(this,
                        arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        cameraRequestCode, object : AtlwPermissionRequestCallback {
                            @SuppressLint("MissingPermission")
                            override fun permissionRequestSuccessCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                                cameraPath = AtlwFileOptionUtil.getInstance().getAppSystemStorageDirPath(packageName) + Math.random() + ".jpg"
                                AtlwMobileOptionsUtil.getInstance().openCamera(this@MobileInfoActivity, cameraPath, cameraRequestCode)
                            }

                            override fun permissionRequestFailCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                                appendText("权限请求失败")
                            }
                        })
                }
                R.id.btn_open_photos -> {
                    appendText("开启相册前权限请求")
                    AtlwActivityUtil.getInstance()
                        .goToRequestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            photosRequestCode, object : AtlwPermissionRequestCallback {
                                @SuppressLint("MissingPermission")
                                override fun permissionRequestSuccessCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                                    AtlwMobileOptionsUtil.getInstance().openImagePhotoAlbum(this@MobileInfoActivity, photosRequestCode)
                                }

                                override fun permissionRequestFailCallback(permissionList: MutableList<String>?, permissionsRequestCode: Int) {
                                    appendText("权限请求失败")
                                }
                            })
                }
                R.id.btn_make_call -> {
                    AtlwMobileOptionsUtil.getInstance().makeCall(this, "18321634834")
                }
                else -> {

                }
            }
        }
    }

    /**
     * 添加文本
     */
    private fun appendText(text: String) {
        runOnUiThread { binding?.tvShow?.append(text) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                cameraRequestCode -> {
                    cameraPath?.let {
                        File(it).let { file ->
                            if (file.exists()) {
                                AtlwImageLoadingFactory.getImageLoading(AtlwConfig.IMAGE_LOAD_LIBRARY_TYPE_GLIDE)
                                    .loadingImage(file.absolutePath, binding?.ivShow,
                                        AtlwImageLoadConfig.Build().setShowViewWidth(AtlwScreenUtil.getInstance().screenWidth)
                                            .setShowViewHeight(AtlwScreenUtil.getInstance().screenWidth).build())
                            }
                        }
                    }
                }
                photosRequestCode -> {
                    AtlwFileOptionUtil.getInstance().getUriPath(data?.data)?.let {
                        File(it).let { file ->
                            if (file.exists()) {
                                AtlwImageLoadingFactory.getImageLoading(AtlwConfig.IMAGE_LOAD_LIBRARY_TYPE_FRESCO)
                                    .loadingImage(file.absolutePath, binding?.ivShow,
                                        AtlwImageLoadConfig.Build().setShowViewWidth(AtlwScreenUtil.getInstance().screenWidth)
                                            .setShowViewHeight(AtlwScreenUtil.getInstance().screenWidth).build())
                            }
                        }
                    }
                }
                else -> {

                }
            }
        } else {
            appendText("返回数据获取失败")
        }
    }
}
