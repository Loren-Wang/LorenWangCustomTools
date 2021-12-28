package com.lorenwang.test.android.activity.graphicCodeScan

import android.Manifest
import android.annotation.SuppressLint
import android.hardware.camera2.*
import android.lorenwang.camera.AclwCamera
import android.lorenwang.camera.AclwCameraCallback
import android.lorenwang.graphic_code_scan.AgcslwCamera
import android.lorenwang.graphic_code_scan.AgcslwCameraOptionsCallback
import android.lorenwang.tools.app.AtlwActivityUtil
import android.lorenwang.tools.app.AtlwPermissionRequestCallback
import android.lorenwang.tools.base.AtlwLogUtil
import android.os.Bundle
import android.view.Surface
import android.view.View
import com.lorenwang.test.android.R
import com.lorenwang.test.android.base.BaseActivity
import com.lorenwang.test.android.databinding.ActivityGraphicCodeCameraBinding
import javabase.lorenwang.tools.file.JtlwFileOptionUtil
import kotlinbase.lorenwang.tools.extend.kttlwGetNotEmptyData
import java.io.File

/**
 * 功能作用：图形码之相机页面
 * 初始注释时间： 2021/8/19 16:30
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
class CameraActivity : BaseActivity() {
    private val scan = AclwCamera()

    private var binding: ActivityGraphicCodeCameraBinding? = null
        get() {
            field = field.kttlwGetNotEmptyData { ActivityGraphicCodeCameraBinding.inflate(layoutInflater) }
            return field
        }
    private val callback = object : CameraCaptureSession.CaptureCallback() {
        override fun onCaptureProgressed(session: CameraCaptureSession, request: CaptureRequest, partialResult: CaptureResult) {
            super.onCaptureProgressed(session, request, partialResult)
        }
    }
    override fun initView(savedInstanceState: Bundle?) {
        addShowContentView(true, binding)
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        scan.openCamera(this@CameraActivity, binding!!.surfaceView, windowManager.defaultDisplay, object : AclwCameraCallback() {
            override fun onConfiguredCameraCaptureSessionSuccess(session: CameraCaptureSession) {
                super.onConfiguredCameraCaptureSessionSuccess(session)
                //获取构造体
                scan.openPreview(mCurrentCameraDevice, session, Surface(binding!!.surfaceView.surfaceTexture!!), callback)
            }
        }, CameraCharacteristics.LENS_FACING_BACK)
    }
}
