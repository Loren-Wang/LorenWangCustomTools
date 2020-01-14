package javabase.lorenwang.common_base_frame

import javabase.lorenwang.tools.JtlwLogUtils
import java.io.IOException
import java.io.InputStream
import java.util.*


/**
 * 功能作用：通用方法
 * 创建时间：2019-12-16 13:44
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 1、获取配置文件内容---getProperties(propertiesName)
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
open class SbcbflwCommonUtils {
    companion object {
        private var optionsUtils: SbcbflwCommonUtils? = null
        @JvmStatic
        val instance: SbcbflwCommonUtils
            get() {
                if (optionsUtils == null) {
                    synchronized(this::class.java) {
                        if (optionsUtils == null) {
                            optionsUtils = SbcbflwCommonUtils()
                        }
                    }
                }
                return optionsUtils!!
            }
    }

    /**
     * 获取配置内容
     * @param propertiesName 配置文件全名称
     * @return 配置文件Properties
     */
    fun getProperties(propertiesName: String): Properties {
        val props = Properties();
        var inputStream: InputStream? = null
        try {
            inputStream = this::class.java.classLoader.getResourceAsStream(propertiesName);
            props.load(inputStream);
        } catch (e: Exception) {
            JtlwLogUtils.logE(this::class.java, "${propertiesName}配置文件加载异常")
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                JtlwLogUtils.logE(this::class.java, "${propertiesName}文件流关闭出现异常")
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

    lateinit var propertiesConfig: SbcbflwPropertiesConfig
}
