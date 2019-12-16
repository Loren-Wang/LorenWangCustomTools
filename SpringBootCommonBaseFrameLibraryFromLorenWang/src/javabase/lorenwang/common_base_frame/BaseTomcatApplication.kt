package javabase.lorenwang.common_base_frame

/**
 * 功能作用：基础外置tomacat运行版本applica
 * 创建时间：2019-12-16 10:46
 * 创建人：王亮（Loren wang）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 */
//class BaseTomcatApplication : SpringBootServletInitializer() {
//    companion object {
//        @JvmStatic
//        fun main(args: Array<String>) {
//            LogUtils.instance.logI(this::class.java, "系统在内置tomcat中开始初始化上下文")
//            val application = SpringApplication(OfficialServiceApplication::class.java)
//            setApplicationConfig(application)
//            application.run(*args)
//            LogUtils.instance.logI(this::class.java, "系统内置tomcat中初始化完成")
//        }
//
//        /**
//         * 设置application配置
//         */
//        fun setApplicationConfig(application: SpringApplication) {
//            LogUtils.instance.logI(this::class.java, "开始初始化application配置")
//            val environment = getStandardEnvironment()
//            LogUtils.instance.logI(this::class.java, "设置环境配置")
//            application.setEnvironment(environment)
//            application.setBannerMode(Banner.Mode.OFF);
//            LogUtils.instance.logI(this::class.java, "添加启动监听")
//            application.addListeners(ApplicationListenerForStart())
//        }
//
//        /**
//         * 获取环境变量配置实例
//         */
//        private fun getStandardEnvironment(): StandardEnvironment {
//            LogUtils.instance.logI(this::class.java, "读取各个properties配置文件，按照先后顺序更新配置信息Map")
//            var map = hashMapOf<String, Any>()
//            map = getPropertiesDataMap("application-email.properties", map)
//            map = getPropertiesDataMap("application.properties", map)
//            map = getPropertiesDataMap("application-dev.properties", map)
//            map = getPropertiesDataMap("application-test.properties", map)
//            map = getPropertiesDataMap("application-pro.properties", map)
//            map = getPropertiesDataMap("qowWeb.pro.properties", map)
//            LogUtils.instance.logI(this::class.java, "各个properties配置文件配置读取覆盖完成：${JdplwJsonUtils.toJson(map)}")
//
//            LogUtils.instance.logI(this::class.java, "初始化环境配置实体，并将配置设置更新到环境当中")
//            val environment = StandardEnvironment()
//            val propertySources = environment.propertySources
//            propertySources.addFirst(MapPropertySource("MY_MAP", map));
//            LogUtils.instance.logI(this::class.java, "环境配置实体配置完成")
//            return environment
//        }
//
//        /**
//         * 获取配置内容Map并更新map
//         */
//        private fun getPropertiesDataMap(propertiesName: String, map: HashMap<String, Any>): HashMap<String, Any> {
//            val iterator = getProperties(propertiesName).entries.iterator()
//            var entry: MutableMap.MutableEntry<Any, Any>
//            while (iterator.hasNext()) {
//                entry = iterator.next()
//                map[entry.key as String] = entry.value
//            }
//            return map
//        }
//
//        /**
//         * 获取配置内容
//         */
//        fun getProperties(propertiesName: String): Properties {
//            val props = Properties();
//            var inputStream: InputStream? = null
//            try {
//                inputStream = this::class.java.classLoader.getResourceAsStream(propertiesName);
//                props.load(inputStream);
//            } catch (e: Exception) {
//                LogUtils.instance.logE(this::class.java, "${propertiesName}配置文件加载异常")
//            } finally {
//                try {
//                    inputStream?.close()
//                } catch (e: IOException) {
//                    LogUtils.instance.logE(this::class.java, "${propertiesName}文件流关闭出现异常")
//                }
//            }
//            return props
//        }
//    }
//
//
//    override fun configure(builder: SpringApplicationBuilder): SpringApplicationBuilder {
//        LogUtils.instance.logI(this::class.java, "系统在外置tomcat中开始初始化上下文")
//        val sources = builder.sources(this::class.java)
//        setApplicationConfig(sources.application())
//        LogUtils.instance.logI(this::class.java, "系统在外置tomcat中配置完成")
//        return sources
//    }
//}
