package springbase.lorenwang.base

import kotlinbase.lorenwang.tools.extend.kttlwToJsonData
import org.springframework.boot.Banner
import org.springframework.boot.SpringApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.ApplicationListener
import org.springframework.core.env.MapPropertySource
import org.springframework.core.env.StandardEnvironment

/**
 * 功能作用：基础application类
 * 创建时间：2020-01-14 10:25
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
abstract class SpbflwBaseApplication : SpringBootServletInitializer() {
    companion object {
        /**
         * 初始化基础
         * @param application 系统进程实例
         * @param properties 配置文件列表,按照优先级从上到下进行数据配置
         */
        fun <T : SpbflwBaseApplication> initBase(application: SpringApplication, startApplication: T) {
            //初始化消息map集合
            startApplication.getMessagePropertiesList().let {
                for (item in it) {
                    spblwConfig.getPropertiesDataMap(item, spblwConfig.messageMap)
                }
            }
            //初始化启动
            application.addListeners(object : ApplicationListener<ApplicationReadyEvent> {
                /**
                 * Handle an application event.
                 * @param event the event to respond to
                 */
                override fun onApplicationEvent(event: ApplicationReadyEvent) {
                    spblwConfig.applicationContext = event.applicationContext
                    startApplication.applicationReadyStart()
                }
            })
            spblwConfig.getLogUtil().logI(Companion::class.java, "系统基础初始化获取到实例，开始初始化上下文", true)
            //设置application配置
            setApplicationConfig(application, startApplication.getConfigProperties())
            spblwConfig.getLogUtil().logI(Companion::class.java, "系统基础初始化完成", true)
        }

        /**
         * 设置application配置
         * @param properties 配置文件列表,按照优先级从上到下进行数据配置
         */
        private fun setApplicationConfig(application: SpringApplication, properties: Array<String>) {
            spblwConfig.getLogUtil().logI(Companion::class.java, "开始初始化application配置", true)
            val environment = getStandardEnvironment(properties)
            spblwConfig.getLogUtil().logI(Companion::class.java, "设置环境配置", true)
            application.setEnvironment(environment)
            application.setBannerMode(Banner.Mode.OFF)
        }

        /**
         * 获取环境变量配置实例
         * @param properties 配置文件列表,按照优先级从上到下进行数据配置
         */
        private fun getStandardEnvironment(properties: Array<String>): StandardEnvironment {
            spblwConfig.getLogUtil().logI(Companion::class.java, "读取各个properties配置文件，按照先后顺序更新配置信息Map", true)
            var map = hashMapOf<String, Any?>()
            properties.forEach {
                map = spblwConfig.getPropertiesDataMap(it, map)
            }
            spblwConfig.getLogUtil().logI(Companion::class.java, "各个properties配置文件配置读取覆盖完成：${map.kttlwToJsonData()}", true)

            spblwConfig.getLogUtil().logI(Companion::class.java, "初始化环境配置实体，并将配置设置更新到环境当中", true)
            val environment = StandardEnvironment()
            val propertySources = environment.propertySources
            propertySources.addFirst(MapPropertySource("MY_MAP", map))
            spblwConfig.getLogUtil().logI(Companion::class.java, "环境配置实体配置完成", true)
            return environment
        }
    }

    override fun configure(builder: SpringApplicationBuilder): SpringApplicationBuilder {
        spblwConfig.getLogUtil().logI(Companion::class.java, "系统在外置tomcat中开始初始化上下文", true)
        val sources = builder.sources(Companion::class.java)
        val application = sources.application()
        //设置配置文件
        setApplicationConfig(application, getConfigProperties())
        //配置初始化完成处理
        outSideTomcatConfigureFinish(application)
        spblwConfig.getLogUtil().logI(Companion::class.java, "系统在外置tomcat中配置完成", true)
        return sources
    }

    /**
     * 获取配置文件列表,按照优先级从上到下进行数据配置
     */
    abstract fun getConfigProperties(): Array<String>

    /**
     * 配置初始化完成处理
     */
    abstract fun outSideTomcatConfigureFinish(application: SpringApplication)

    /**
     * application初始化准备完成，即咋listeners中监听了回调时间的再次调用
     */
    open fun applicationReadyStart() {}

    /**
     * 获取消息文本文件列表
     */
    abstract fun getMessagePropertiesList(): ArrayList<String>
}
