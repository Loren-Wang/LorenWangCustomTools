package android.lorenwang.camera

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.lorenwang.tools.AtlwConfig
import android.lorenwang.tools.base.AtlwLogUtil
import android.util.Size
import android.view.Surface
import java.util.*
import kotlin.math.abs


/**
 * 功能作用：相机操作相关
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
internal class AclwCameraOptions {
    /**
     * 标签
     */
    private val showTag = javaClass.name

    /**
     * 最小预览大小
     */
    val mMinPreviewPixels = 480 * 320

    /**
     * 最大宽高比
     */
    val mMaxAspectDistortion = 0.15

    /**
     * 获取摄像机管理器
     *
     * @return 管理器，可能为空
     */
    fun getCameraManager(): CameraManager? {
        val mCameraManager = AtlwConfig.nowApplication.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        return try {
            val cameraIdList: Array<String> = mCameraManager.cameraIdList
            if (cameraIdList.isEmpty()) {
                AtlwLogUtil.logUtils.logI(showTag, "没有可用相机")
                return null
            }
            mCameraManager
        } catch (e: CameraAccessException) {
            AtlwLogUtil.logUtils.logE(e)
            null
        }
    }

    /**
     * 获取要使用的相机id
     *
     * @param cameraManager 相机管理器
     * @param type          使用的方向类型
     * @return 要使用的相机id
     */
    fun getCameraId(cameraManager: CameraManager?, type: Int): String? {
        if (cameraManager != null) {
            try {
                var cameraCharacteristics: CameraCharacteristics
                for (cameraId in cameraManager.cameraIdList) {
                    cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId!!)
                    if (type == cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)) {
                        return cameraId
                    }
                }
            } catch (ignore: Exception) {
            }
        }
        AtlwLogUtil.logUtils.logI(showTag, "相机id获取失败")
        return null
    }

    /**
     * 获取要使用的相机参数
     *
     * @param cameraManager 相机管理器
     * @param type          使用的方向类型
     * @return 要使用的相机参数
     */
    fun getCameraCharacteristics(cameraManager: CameraManager, type: Int): CameraCharacteristics? {
        try {
            var cameraCharacteristics: CameraCharacteristics? = null
            for (cameraId in cameraManager.cameraIdList) {
                cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId!!)
                if (type == cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)) {
                    break
                }
            }
            if (cameraCharacteristics != null) {
                val supportLevel = cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)
                if (supportLevel != CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
                    return cameraCharacteristics
                } else {
                    AtlwLogUtil.logUtils.logI(showTag, "相机硬件不支持新特性")
                }
            }
        } catch (ignore: java.lang.Exception) {
        }
        AtlwLogUtil.logUtils.logI(showTag, "相机配置参数获取失败")
        return null
    }

    /**
     * 获取摄像头方向
     *
     * @param characteristics 摄像头信息
     * @return 方向角度
     */
    fun getCameraSensorOrientation(characteristics: CameraCharacteristics?): Int? {
        return characteristics?.get(CameraCharacteristics.SENSOR_ORIENTATION)
    }

    /**
     * 获取相机的预览尺寸
     *
     * @param characteristics 摄像头信息
     * @return 预览尺寸
     */
    fun <T> getCameraPreviewSize(characteristics: CameraCharacteristics?, cls: Class<T>?): ArrayList<Size>? {
        val map = characteristics?.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        return map?.getOutputSizes(cls)?.toCollection(arrayListOf())
    }

    /**
     * 根据提供的屏幕方向 [displayRotation] 和相机方向 [sensorOrientation] 返回是否需要交换宽高
     *
     * @param displayRotation   屏幕方向
     * @param sensorOrientation 相机方向
     * @return 是否交换宽高
     */
    fun exchangeWidthHeight(displayRotation: Int, sensorOrientation: Int?): Boolean {
        var exchange = false
        if (sensorOrientation != null) {
            when (displayRotation) {
                Surface.ROTATION_0, Surface.ROTATION_180 -> if (sensorOrientation == 90 || sensorOrientation == 270) {
                    exchange = true
                }
                Surface.ROTATION_90, Surface.ROTATION_270 -> if (sensorOrientation == 0 || sensorOrientation == 180) {
                    exchange = true
                }
                else -> {}
            }
        }
        AtlwLogUtil.logUtils.logI(showTag, "屏幕方向 $displayRotation")
        AtlwLogUtil.logUtils.logI(showTag, "相机方向 $sensorOrientation")
        return exchange
    }

    /**
     * 从相机支持的分辨率中计算出最适合的预览界面尺
     * @param allowUseList 允许的预览列表
     * @param showSize 显示区域大小
     * @param minPreviewPixels 最小预览像素
     * @param maxAspectDistortion 最大预览比例
     */
    fun getBestPreviewSizeValue(allowUseList: ArrayList<Size>?, showSize: Size, minPreviewPixels: Int = mMinPreviewPixels,
        maxAspectDistortion: Double = mMaxAspectDistortion): Size {     //没有找到合适的返回支持列表第一个
        if (allowUseList.isNullOrEmpty()) {
            return showSize.also {
                AtlwLogUtil.logUtils.logI(showTag, "No suitable preview sizes, using default: $it")
            }
        }
        //排序处理
        Collections.sort(allowUseList, Comparator { a, b ->
            val aPixels = a.height * a.width
            val bPixels = b.height * b.width
            if (bPixels < aPixels) {
                return@Comparator -1
            }
            if (bPixels > aPixels) {
                1
            } else 0
        })
        //打印日志
        if (AtlwLogUtil.logUtils.isShowLog) {
            val previewSizesString = StringBuilder()
            for (size in allowUseList) {
                previewSizesString.append(size.width).append('x').append(size.height).append(' ')
            }
            AtlwLogUtil.logUtils.logI(showTag, "Supported preview sizes: $previewSizesString")
        }
        //显示的比例
        val showAspectRatio = showSize.width.toDouble() / (if (showSize.height > 0) showSize.height.toDouble() else 1.0)
        //遍历清除多余的，如果有合适的直接返回
        val iterator = allowUseList.iterator()
        while (iterator.hasNext()) {
            val supportedPreviewSize = iterator.next()
            val realWidth = supportedPreviewSize.width
            val realHeight = supportedPreviewSize.height
            //移除过于小的
            if (realWidth * realHeight < minPreviewPixels) {
                iterator.remove()
                continue
            }
            val isCandidatePortrait = realWidth < realHeight
            val maybeFlippedWidth = if (isCandidatePortrait) realHeight else realWidth
            val maybeFlippedHeight = if (isCandidatePortrait) realWidth else realHeight
            val aspectRatio = maybeFlippedWidth.toDouble() / maybeFlippedHeight.toDouble()
            val distortion = abs(aspectRatio - showAspectRatio)
            //移除宽高比过大的
            if (distortion > maxAspectDistortion) {
                iterator.remove()
                continue
            }
            //正合适的话直接返回
            if (maybeFlippedWidth == showSize.width && maybeFlippedHeight == showSize.width) {
                return Size(realWidth, realHeight).also {
                    AtlwLogUtil.logUtils.logI(showTag, "Found preview size exactly matching screen size: $it")
                }
            }
        }

        //没有找到合适的返回支持列表第一个
        if (allowUseList.isNotEmpty()) {
            return allowUseList[0].also {
                AtlwLogUtil.logUtils.logI(showTag, "Using largest suitable preview size: $it")
            }
        }
        //都没有返回当前的
        return showSize.also {
            AtlwLogUtil.logUtils.logI(showTag, "No suitable preview sizes, using default: $it")
        }
    }
}