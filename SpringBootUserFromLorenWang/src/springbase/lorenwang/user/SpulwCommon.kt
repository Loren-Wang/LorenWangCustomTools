package springbase.lorenwang.user

import org.springframework.context.ConfigurableApplicationContext
import springbase.lorenwang.base.SpblwConfig
import springbase.lorenwang.user.service.SpulwUserPermissionService
import springbase.lorenwang.user.service.SpulwUserService
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
open class SpulwCommon {

    /**
     * 项目运行实例
     */
    lateinit var applicationContext: ConfigurableApplicationContext

    /**
     * 用户服务
     */
    var userService: SpulwUserService? = null

    /**
     * 用户角色权限服务
     */
    var userRolePermission: SpulwUserPermissionService<*, *, *, *, *, *>? = null

    companion object {
        private var options: SpulwCommon? = null

        @JvmStatic
        val instance: SpulwCommon
            get() {
                if (options == null) {
                    synchronized(this::class.java) {
                        if (options == null) {
                            options = SpulwCommon()
                        }
                    }
                }
                return options!!
            }
    }

    /**
     * 初始化
     * @param applicationContext 运行实例
     */
    fun initBase(applicationContext: ConfigurableApplicationContext) {
        this.applicationContext = applicationContext
        try {
            userService = applicationContext.getBean(SpulwUserService::class.java)
            userRolePermission = applicationContext.getBean(SpulwUserPermissionService::class.java)
        } catch (ignore: java.lang.Exception) {
        }
    }

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
            SpblwConfig.logUtils.logE(this::class.java, "${propertiesName}配置文件加载异常")
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                SpblwConfig.logUtils.logE(this::class.java, "${propertiesName}文件流关闭出现异常")
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
}
