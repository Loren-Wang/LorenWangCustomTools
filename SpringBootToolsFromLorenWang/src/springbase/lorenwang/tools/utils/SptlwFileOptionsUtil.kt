package springbase.lorenwang.tools.utils

import javabase.lorenwang.tools.common.JtlwCommonUtil
import javabase.lorenwang.tools.enums.JtlwFileTypeEnum
import org.springframework.web.multipart.MultipartFile
import springbase.lorenwang.base.bean.SpblwBaseDataDisposeStatusBean
import springbase.lorenwang.tools.sptlwConfig
import java.io.IOException
import java.io.InputStream
import java.util.*

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
abstract class SptlwFileOptionsUtil {
    /**
     * 文件最大大小，单位是字节
     */
    private var ossTypeFileMaxSize = 5242880L

    /**
     * 检测文件状态
     * @param file 要被检测的文件
     * @param receiveFileTypes 要接收的文件类型
     * @param finishFun 检测结束的操作函数
     * @return 结果返回
     */
    fun checkFileStatus(file: MultipartFile, receiveFileTypes: Array<JtlwFileTypeEnum>,
        finishFun: () -> SpblwBaseDataDisposeStatusBean): SpblwBaseDataDisposeStatusBean {
        val fileHeader = getFileHeader(file.inputStream)
        //上传文件空判断
        if (fileHeader.isNullOrEmpty()) {
            sptlwConfig.getLogUtil().logE(this.javaClass, "文件${
                file.originalFilename
            }---无法解析文件类型，无法执行上传流程！", true)
            return getUnKnowFileTypeStatusBean()
        }
        //上传文件空判断
        if (file.isEmpty) {
            sptlwConfig.getLogUtil().logE(this.javaClass, "文件${file.originalFilename}---上传文件为空，无法执行上传流程！", true)
            return getFileEmptyStatusBean()
        }
        //判断文件上传大小
        if (file.size > ossTypeFileMaxSize) {
            sptlwConfig.getLogUtil().logE(this.javaClass, "文件${file.originalFilename}---上传文件过大，大于${ossTypeFileMaxSize}！", true)
            return getFileTooLargeStatusBean()
        }
        //文件接收类型判断
        if (receiveFileTypes.isNotEmpty() && !receiveFileTypes.contains(getFileType(fileHeader))) {
            sptlwConfig.getLogUtil().logE(this.javaClass, "文件${file.originalFilename}---不是接收类型数据！", true)
            return getNotReceiveFileTypeStatusBean()
        }
        return try {
            finishFun()
        } catch (e: Exception) {
            SpblwBaseDataDisposeStatusBean(false)
        }
    }

    /**
     * @param inputStream 文件流
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
                val byte = ByteArray(28)
                inputStream.read(byte, 0, byte.size)
                JtlwCommonUtil.getInstance().bytesToHexString(byte)
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
    abstract fun getUnKnowFileTypeStatusBean(): SpblwBaseDataDisposeStatusBean

    /**
     * 获取未见内容是空情况下状态实例
     */
    abstract fun getFileEmptyStatusBean(): SpblwBaseDataDisposeStatusBean

    /**
     * 获取非接收文件类型情况下状态实例
     */
    abstract fun getNotReceiveFileTypeStatusBean(): SpblwBaseDataDisposeStatusBean

    /**
     * 获取文件过大情况下状态实例
     */
    abstract fun getFileTooLargeStatusBean(): SpblwBaseDataDisposeStatusBean

    /**
     * 获取配置内容
     * @param propertiesName 配置文件全名称
     * @return 配置文件Properties
     */
    fun getProperties(propertiesName: String): Properties {
        val props = Properties()
        var inputStream: InputStream? = null
        try {
            inputStream = this::class.java.classLoader.getResourceAsStream(propertiesName)
            props.load(inputStream)
        } catch (e: Exception) {
            sptlwConfig.getLogUtil().logE(this::class.java, "${propertiesName}配置文件加载异常")
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                sptlwConfig.getLogUtil().logE(this::class.java, "${propertiesName}文件流关闭出现异常")
            }
        }
        return props
    }

    /**
     * 获取配置内容Map并更新map
     * @param propertiesName 全名称，例如：application-email.properties
     */
    fun getPropertiesDataMap(propertiesName: String, map: HashMap<String, Any>): HashMap<String, Any> {
        return getPropertiesDataMap(getProperties(propertiesName), map)
    }

    /**
     * 获取配置内容Map并更新map
     */
    fun getPropertiesDataMap(properties: Properties, map: HashMap<String, Any>): HashMap<String, Any> {
        val iterator = properties.entries.iterator()
        var entry: MutableMap.MutableEntry<Any, Any>
        while (iterator.hasNext()) {
            entry = iterator.next()
            map[entry.key as String] = entry.value
        }
        return map
    }

    /**
     * 初始化配置
     */
    open fun initConfig(ossTypeFileMaxSize: Long) {
        this.ossTypeFileMaxSize = ossTypeFileMaxSize
    }
}
