package com.test.springboot

import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import java.nio.charset.StandardCharsets

/**
 * 功能作用：解决中文乱码问题
 * 初始注释时间： 2022/4/19 10:32
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
@Configuration
open class MyConfiguration : WebMvcConfigurationSupport() {
    override fun extendMessageConverters(converters: List<HttpMessageConverter<*>?>) {
        // 解决controller返回字符串中文乱码问题
        for (converter in converters) {
            if (converter is StringHttpMessageConverter) {
                converter.defaultCharset = StandardCharsets.UTF_8
            }
        }
    }

    /**
     * 发现如果继承了WebMvcConfigurationSupport，则在yml中配置的相关内容会失效。 需要重新指定静态资源
     *
     * @param registry
     */
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/")
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/")
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/")
        super.addResourceHandlers(registry)
    }

    /**
     * 配置servlet处理
     */
    override fun configureDefaultServletHandling(configurer: DefaultServletHandlerConfigurer) {
        configurer.enable()
    }
}