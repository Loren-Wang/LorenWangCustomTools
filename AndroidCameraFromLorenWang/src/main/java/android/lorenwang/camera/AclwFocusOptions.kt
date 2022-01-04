package android.lorenwang.camera

import android.graphics.Rect
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.params.MeteringRectangle
import android.util.Log
import androidx.core.math.MathUtils.clamp


/**
 * 功能作用：对焦处理工具类
 * 初始注释时间： 2021/12/24 10:27
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
internal class AclwFocusOptions {
    /**
     * 是否支持对焦
     */
    fun checkAllowFocus(cameraManager: CameraManager?, type: Int, options: AclwCameraOptions): Boolean {
        if (cameraManager != null) {
            val cameraId = options.getCameraId(cameraManager, type)
            if (cameraId != null) {
                val characteristics = cameraManager.getCameraCharacteristics(cameraId)
                if (characteristics.get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE) != null) {
                    when (characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)) {
                        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED, CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_3, CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL -> return true
                    }
                }
            }
        }
        return false
    }

    fun getTouchFocusRect(cameraManager: CameraManager?, type: Int,options: AclwCameraOptions,eventX:Double,eventY:Double){
//        if (cameraManager != null) {
//            val cameraId = options.getCameraId(cameraManager, type)
//            if (cameraId != null) {
//                val characteristics = cameraManager.getCameraCharacteristics(cameraId)
//                //获取相机预览区域
//                val cameraRect = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE)
//                val newRect: Rect
//                val leftPos: Int
//                val topPos: Int
//                // 坐标转换
//                val newX: Float = currentY
//                val newY: Float = previewWidth - currentX
//                // 大小转换
//                leftPos = (newX / previewHeight * rect.right)
//                topPos = (newY / previewWidth * rect.bottom)
//                // 以坐标点为中心生成一个矩形, 需要防止上下左右的值溢出
//                val left: Int = clamp(leftPos - areaSize, 0, rect.right)
//                val top: Int = clamp(topPos - areaSize, 0, rect.bottom)
//                val right: Int = clamp(leftPos + areaSize, leftPos, rect.right)
//                val bottom: Int = clamp(topPos + areaSize, topPos, rect.bottom)
//                newRect = Rect(left, top, right, bottom)
//                Log.d(TAG, newRect.toString())
//                // 构造MeteringRectangle
//                return MeteringRectangle(newRect, weight)
//            }
//        }
    }
}