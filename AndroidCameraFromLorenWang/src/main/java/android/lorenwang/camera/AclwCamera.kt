package android.lorenwang.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.params.MeteringRectangle
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.lorenwang.tools.app.AtlwActivityUtil
import android.lorenwang.tools.app.AtlwPermissionRequestCallback
import android.lorenwang.tools.base.AtlwLogUtil
import android.media.ImageReader
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Size
import android.view.Display
import android.view.Surface
import android.view.TextureView
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat


/**
 * 功能作用：相机工具类
 * 初始注释时间： 2021/12/23 13:19
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
class AclwCamera {
    private val mShowTag = javaClass.name

    /**
     * 相机操作相关
     */
    private val mCameraOptions = AclwCameraOptions()

    /**
     * 相机管理器
     */
    private var mCameraManager: CameraManager? = null

    /**
     * 默认的handler
     */
    private val mDefaultHandler by lazy {
        val looper = Looper.myLooper() ?: throw IllegalArgumentException("No handler given, and current thread has no looper!")
        Handler(looper)
    }

    /**
     * 最佳预览大小
     */
    private var mBestPreviewSize: Size? = null

    /**
     * 相机权限相关
     */
    private val mCameraPermissions =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    /**
     * 打开相机
     * @param context 上下文
     * @param surface 预览图像承载体
     * @param display 预览控件window信息
     * @param callback 回调
     * @param handler 在哪个线程
     * @param minPreviewPixels 最小预览像素
     * @param maxAspectDistortion 最大预览比例
     */
    fun openCamera(context: Context, surface: TextureView, display: Display, callback: AclwCameraCallback, cameraType: Int,
        handler: Handler = mDefaultHandler, minPreviewPixels: Int = mCameraOptions.mMinPreviewPixels,
        maxAspectDistortion: Double = mCameraOptions.mMaxAspectDistortion) {
        //请求权限
        AtlwActivityUtil.getInstance().goToRequestPermissions(context, mCameraPermissions, object : AtlwPermissionRequestCallback {
            override fun permissionRequestFailCallback(permissionList: MutableList<String>?) {
                AtlwLogUtil.logUtils.logI(mShowTag, "权限获取失败")
                callback.permissionRequestFail()
            }

            @SuppressLint("MissingPermission", "ClickableViewAccessibility")
            override fun permissionRequestSuccessCallback(permissionList: MutableList<String>?) {
                AtlwLogUtil.logUtils.logI(mShowTag, "权限获取成功")
                callback.permissionRequestSuccess()
                //权限检测
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Log.e("openCamera", "没有相关权限，无法开启")
                    return
                }
                //获取相机管理器
                mCameraManager = mCameraOptions.getCameraManager()
                //等待控件加载完成,或者已加载直接设置预览
                if (surface.width <= 0 || surface.height <= 0) {
                    surface.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            initPreviewConfigAndOpen(surface, cameraType, display, callback, handler, Size(surface.width, surface.height),
                                minPreviewPixels, maxAspectDistortion)

                            surface.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        }
                    })
                } else {
                    initPreviewConfigAndOpen(surface, cameraType, display, callback, handler, Size(surface.width, surface.height), minPreviewPixels,
                        maxAspectDistortion)
                }
            }
        })
    }

    /**
     * 开通预览
     */
    fun openPreview(cameraDevice: CameraDevice, session: CameraCaptureSession, surface: Surface, callback: CameraCaptureSession.CaptureCallback,
        handler: Handler = mDefaultHandler) {
        session.setRepeatingRequest(getCaptureRequestBuilder(cameraDevice, CameraDevice.TEMPLATE_PREVIEW, surface,
            listOf(CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH,
                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)).build(), callback, handler)
    }


    /**
     * 获取请求的builer
     */
    private fun getCaptureRequestBuilder(cameraDevice: CameraDevice, templateType: Int, target: Surface, controlAfModes: List<Int> = listOf(),
        controlAfTrigger: List<Int> = listOf(), controlAePrecaptureTrigger: List<Int> = listOf(),
        controlAfRegions: List<Array<MeteringRectangle>> = listOf(), controlAeRegions: List<Array<MeteringRectangle>> = listOf(),
        modes: List<Int> = listOf()): CaptureRequest.Builder {
        return cameraDevice.createCaptureRequest(templateType).also {
            it.addTarget(target)
            for (mode in modes) {
                it.set(CaptureRequest.CONTROL_MODE, mode)
            }
            for (mode in controlAfModes) {
                it.set(CaptureRequest.CONTROL_AF_MODE, mode)
            }
            for (trigger in controlAfTrigger) {
                it.set(CaptureRequest.CONTROL_AF_TRIGGER, trigger)
            }
            for (precaptureTrigger in controlAePrecaptureTrigger) {
                it.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, precaptureTrigger)
            }
            for (item in controlAfRegions) {
                it.set(CaptureRequest.CONTROL_AF_REGIONS, item)
            }
            for (item in controlAeRegions) {
                it.set(CaptureRequest.CONTROL_AE_REGIONS, item)
            }
        }
    }

    /**
     * 初始化预览相关
     * @param surface 预览图像承载体
     * @param display 预览控件window信息
     * @param callback 回调
     * @param handler 在哪个线程
     * @param showSize 显示区域
     * @param minPreviewPixels 最小预览像素
     * @param maxAspectDistortion 最大预览比例
     */
    @SuppressLint("MissingPermission")
    private fun initPreviewConfigAndOpen(surface: TextureView, cameraType: Int, display: Display, callback: AclwCameraCallback,
        handler: Handler = mDefaultHandler, showSize: Size, minPreviewPixels: Int = mCameraOptions.mMinPreviewPixels,
        maxAspectDistortion: Double = mCameraOptions.mMaxAspectDistortion) {
        //获取最佳预览大小
        val bestPreviewSize = getBestPreviewSize(cameraType, display, showSize, minPreviewPixels, maxAspectDistortion)
        //获取拍照实时状态
        val imageReader = ImageReader.newInstance(bestPreviewSize.width, bestPreviewSize.height, ImageFormat.JPEG, 1).also {
            it.setOnImageAvailableListener({ reader -> callback.onImageAvailable(reader) }, handler)
        }
        //开启相机
        mCameraManager?.let {
            val cameraId = mCameraOptions.getCameraId(it, cameraType)
            if (cameraId.isNullOrEmpty()) {
                AtlwLogUtil.logUtils.logI(mShowTag, "相机开启失败")
                callback.openCameraFail()
            } else {
                try {
                    it.openCamera(cameraId, object : CameraDevice.StateCallback() {
                        @SuppressLint("Recycle")
                        override fun onOpened(camera: CameraDevice) {
                            AtlwLogUtil.logUtils.logI(mShowTag, "摄像头已打开")
                            callback.cameraOpen(camera)
                            //设置预览最佳大小
                            surface.surfaceTexture?.setDefaultBufferSize(bestPreviewSize.width, bestPreviewSize.height)
                            //回调
                            val stateCallback = object : CameraCaptureSession.StateCallback() {
                                override fun onConfigured(session: CameraCaptureSession) {
                                    AtlwLogUtil.logUtils.logI(mShowTag, "相机控制器处理成功")
                                    callback.onConfiguredCameraCaptureSessionSuccess(session)
                                }

                                override fun onConfigureFailed(session: CameraCaptureSession) {
                                    AtlwLogUtil.logUtils.logI(mShowTag, "相机控制器处理失败")
                                    callback.onConfiguredCameraCaptureSessionFail(session)
                                }
                            }
                            //生成相机的控制器
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                val list = mutableListOf(OutputConfiguration(OutputConfiguration.SURFACE_GROUP_ID_NONE, imageReader.surface))
                                list.add(OutputConfiguration(OutputConfiguration.SURFACE_GROUP_ID_NONE, Surface(surface.surfaceTexture)))
                                camera.createCaptureSession(
                                    SessionConfiguration(SessionConfiguration.SESSION_REGULAR, list, { run -> handler.post(run) }, stateCallback))
                            } else {
                                val list = arrayListOf(imageReader.surface)
                                list.add(Surface(surface.surfaceTexture))
                                camera.createCaptureSession(list, stateCallback, handler)
                            }
                        }

                        override fun onDisconnected(camera: CameraDevice) {
                            AtlwLogUtil.logUtils.logI(mShowTag, "摄像头断开连接")
                            callback.cameraDisconnected()
                        }

                        override fun onError(camera: CameraDevice, error: Int) {
                            AtlwLogUtil.logUtils.logI(mShowTag, "摄像头状态异常$error")
                            callback.cameraError(camera, error)
                        }

                        override fun onClosed(camera: CameraDevice) {
                            super.onClosed(camera)
                            AtlwLogUtil.logUtils.logI(mShowTag, "摄像头关闭")
                            callback.cameraClose(camera)
                        }
                    }, handler)
                } catch (e: java.lang.Exception) {
                    AtlwLogUtil.logUtils.logI(mShowTag, "相机开启失败$e")
                    callback.openCameraFail()
                }
            }
        }
    }

    /**
     * 获取最佳预览大小
     */
    private fun getBestPreviewSize(cameraType: Int, display: Display, showSize: Size, minPreviewPixels: Int = mCameraOptions.mMinPreviewPixels,
        maxAspectDistortion: Double = mCameraOptions.mMaxAspectDistortion): Size {
        mBestPreviewSize = showSize
        mCameraManager?.let {
            //相机信息
            val characteristics = mCameraOptions.getCameraCharacteristics(it, cameraType)
            //获取预览列表
            val allowPreviewSizeList = mCameraOptions.getCameraPreviewSize(characteristics, SurfaceTexture::class.java)
            //是否需要旋转方向
            val exchange = exchangeWidthHeight(cameraType, display)
            mBestPreviewSize = mCameraOptions.getBestPreviewSizeValue(allowPreviewSizeList, if (exchange) {
                Size(showSize.height, showSize.width)
            } else {
                showSize
            }, minPreviewPixels, maxAspectDistortion)
        }
        return mBestPreviewSize!!
    }

    /**
     * 是否需要宽高转换
     */
    private fun exchangeWidthHeight(cameraType: Int, display: Display): Boolean {
        mCameraManager?.let {
            //相机信息
            val characteristics = mCameraOptions.getCameraCharacteristics(it, cameraType)
            //是否需要旋转方向
            return mCameraOptions.exchangeWidthHeight(display.rotation, mCameraOptions.getCameraSensorOrientation(characteristics))
        }
        return false
    }
}