package springbase.lorenwang.base

import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import springbase.lorenwang.base.controller.SpblwBaseHttpServletRequestWrapper
import springbase.lorenwang.base.utils.SpblwLog
import java.io.IOException
import java.io.InputStream
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

/**
 * 功能作用：SpringBoot基础库接口
 * 初始注释时间： 2022/2/28 10:05
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
abstract class SpblwConfig {
    /**
     * 项目运行实例
     */
    lateinit var applicationContext: ConfigurableApplicationContext

    /**
     * 消息源
     */
    val messageSource: MessageSource by lazy { applicationContext.getBean(MessageSource::class.java) }

    /**
     * 日志工具类
     */
    abstract fun getLogUtil(): SpblwLog

    /**
     * 当前是否是debug模式
     */
    open fun currentIsDebug(): Boolean = false

    /**
     * 初始化接口拦截处理
     */
    open fun initFilterDoFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Boolean {
        return true
    }

    /**
     * 格式化所有的网络请求
     */
    open fun paramsAllRequest(request: ServletRequest): SpblwBaseHttpServletRequestWrapper? {
        return if (request is HttpServletRequest) {
            SpblwBaseHttpServletRequestWrapper(request)
        } else {
            null
        }
    }

    /**
     * 获取请求头部分用户token的key,获取header中accessToken使用的关键字
     */
    open fun getAccessControlAllowHeadersUserTokenKey(): String {
        return "userToken"
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
            getLogUtil().logE(this::class.java, "${propertiesName}配置文件加载异常")
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                getLogUtil().logE(this::class.java, "${propertiesName}文件流关闭出现异常")
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
     * 获取文字字符
     * @param code ：对应messages配置的key.
     * @return
     */
    fun getMessage(code: String?): String {
        //这里使用比较方便的方法，不依赖request.
        return getMessage(code, null)
    }

    /**
     * 获取文字字符
     * @param code ：对应messages配置的key.
     * @param defaultMessage : 没有设置key的时候的默认值.
     * @return
     */
    fun getMessage(code: String?, defaultMessage: String?): String {
        //这里使用比较方便的方法，不依赖request.
        return code?.let {
            messageSource.getMessage(it, null, defaultMessage ?: "", LocaleContextHolder.getLocale())
        } ?: ""
    }

    /**
     * 参数异常响应
     */
    abstract fun <R : SpblwBaseHttpServletRequestWrapper> responseErrorForParams(request: R?): String

    /**
     * 数据响应成功
     */
    abstract fun <R : SpblwBaseHttpServletRequestWrapper> responseSuccess(request: R?, data: Any?): String

    /**
     * 数据删除失败
     */
    abstract fun <R : SpblwBaseHttpServletRequestWrapper> responseDeleteFail(request: R?): String

    /**
     * 未知错误失败
     */
    abstract fun <R : SpblwBaseHttpServletRequestWrapper> responseFailForUnKnow(request: R?): String

    /**
     * 登录验证失败,用户未登录或者token失效
     */
    abstract fun <R : SpblwBaseHttpServletRequestWrapper> responseErrorUserLoginEmptyOrTokenNoneffective(request: R?): String

    /**
     * 无权限异常
     */
    abstract fun <R : SpblwBaseHttpServletRequestWrapper> responseErrorNotPermission(request: R?): String

}

/**
 * 基础库config
 */
lateinit var spblwConfig: SpblwConfig