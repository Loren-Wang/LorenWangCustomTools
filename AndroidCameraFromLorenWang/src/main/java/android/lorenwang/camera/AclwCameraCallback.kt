package android.lorenwang.camera

import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.media.ImageReader

/**
 * 功能作用：相机回调
 * 初始注释时间： 2021/12/23 14:11
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
abstract class AclwCameraCallback {
    lateinit var mCurrentCameraCaptureSession: CameraCaptureSession
    lateinit var mCurrentCameraDevice: CameraDevice

    /**
     * 相机状态回调
     */
    open fun onImageAvailable(reader: ImageReader) {}

    /**
     * 相机开启失败
     */
    open fun openCameraFail() {}

    /**
     * 摄像头关闭
     */
    open fun cameraClose(camera: CameraDevice) {
        this.mCurrentCameraDevice = camera
    }

    /**
     * 摄像头出现异常
     */
    open fun cameraError(camera: CameraDevice, error: Int) {
        this.mCurrentCameraDevice = camera
    }

    /**
     * 摄像头断开连接
     */
    open fun cameraDisconnected() {}

    /**
     * 摄像头已打开s
     */
    open fun cameraOpen(camera: CameraDevice) {
        this.mCurrentCameraDevice = camera
    }

    /**
     * 相机控制器处理成功
     */
    open fun onConfiguredCameraCaptureSessionSuccess(session: CameraCaptureSession) {
        this.mCurrentCameraCaptureSession = session
    }

    /**
     * 相机控制器处理失败
     */
    open fun onConfiguredCameraCaptureSessionFail(session: CameraCaptureSession) {
        this.mCurrentCameraCaptureSession = session
    }

    /**
     * 权限获取失败
     */
    fun permissionRequestFail() {

    }

    /**
     * 权限获取成功
     */
    fun permissionRequestSuccess() {

    }

}