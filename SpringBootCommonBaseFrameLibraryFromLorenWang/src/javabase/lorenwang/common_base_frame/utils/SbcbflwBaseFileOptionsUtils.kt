package javabase.lorenwang.common_base_frame.utils

import javabase.lorenwang.common_base_frame.SbcbflwCommonUtils
import javabase.lorenwang.common_base_frame.bean.SbcbflwBaseDataDisposeStatusBean
import javabase.lorenwang.tools.common.JtlwCommonUtils
import javabase.lorenwang.tools.enums.JtlwFileTypeEnum
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

/**
 * 功能作用：文件操作基础单例类
 * 创建时间：2020-02-03 下午 20:01:19
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：子类必须将构造函数私有化
 */
abstract class SbcbflwBaseFileOptionsUtils {
    companion object {
        lateinit var baseInstance: SbcbflwBaseFileOptionsUtils
    }



    /**
     * 检测文件状态
     * @param file 要被检测的文件
     * @param receiveFileTypes 要接收的文件类型
     * @param finishFun 检测结束的操作函数
     * @return 结果返回
     */
    fun checkFileStatus(file: MultipartFile, receiveFileTypes: Array<JtlwFileTypeEnum>, finishFun: () -> SbcbflwBaseDataDisposeStatusBean): SbcbflwBaseDataDisposeStatusBean {
        val fileHeader = getFileHeader(file.inputStream)
        //上传文件空判断
        if (fileHeader.isNullOrEmpty()) {
            SbcbflwLogUtils.baseInstance.logE(this.javaClass, "文件${file
                    .originalFilename}---无法解析文件类型，无法执行上传流程！",true)
            return getUnKnowFileTypeStatusBean()
        }
        //上传文件空判断
        if (file.isEmpty) {
            SbcbflwLogUtils.baseInstance.logE(this.javaClass, "文件${file.originalFilename}---上传文件为空，无法执行上传流程！",true)
            return getFileEmptyStatusBean()
        }
        //判断文件上传大小
        if (file.size > SbcbflwCommonUtils.instance.propertiesConfig.ossTypeFileMaxSize) {
            SbcbflwLogUtils.baseInstance.logE(this.javaClass, "文件${file.originalFilename}---上传文件过大，大于${SbcbflwCommonUtils.instance.propertiesConfig.ossTypeFileMaxSize}！",true)
            return getFileTooLargeStatusBean()
        }
        //文件接收类型判断
        if (receiveFileTypes.isNotEmpty() && !receiveFileTypes.contains(getFileType(fileHeader))) {
            SbcbflwLogUtils.baseInstance.logE(this.javaClass, "文件${file.originalFilename}---不是接收类型数据！",true)
            return getNotReceiveFileTypeStatusBean()
        }
        return try {
            finishFun()
        } catch (e: Exception) {
            SbcbflwBaseDataDisposeStatusBean(false)
        }
    }

    /**
     * @param filePath 文件路径
     * @return 文件头信息
     * @author wlx
     * <p>
     * 方法描述：根据文件路径获取文件头信息
     */
    private fun getFileHeader(inputStream: InputStream?): String? {
        return try {
            if (inputStream == null) {
                null
            } else {
                val byte = ByteArray(28);
                inputStream.read(byte, 0, byte.size);
                JtlwCommonUtils.getInstance().bytesToHexString(byte)
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 获取文件类型
     */
    private fun getFileType(fileHeader: String): JtlwFileTypeEnum {
        JtlwFileTypeEnum.values().forEach {
            if (fileHeader.startsWith(it.start, true)) {
                return it
            }
        }
        return JtlwFileTypeEnum.OTHER
    }

    /**
     * 获取位置文件类型状态实例
     */
    abstract fun getUnKnowFileTypeStatusBean(): SbcbflwBaseDataDisposeStatusBean

    /**
     * 获取未见内容是空情况下状态实例
     */
    abstract fun getFileEmptyStatusBean(): SbcbflwBaseDataDisposeStatusBean

    /**
     * 获取非接收文件类型情况下状态实例
     */
    abstract fun getNotReceiveFileTypeStatusBean(): SbcbflwBaseDataDisposeStatusBean

    /**
     * 获取文件过大情况下状态实例
     */
    abstract fun getFileTooLargeStatusBean(): SbcbflwBaseDataDisposeStatusBean
}
