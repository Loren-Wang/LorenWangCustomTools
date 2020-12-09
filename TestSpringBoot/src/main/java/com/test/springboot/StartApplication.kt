package com.test.springboot

import com.qtoolsbaby.servicemmxs.config.PropertiesConfig
import com.test.springboot.applicationListener.ApplicationListenerForStart
import com.test.springboot.utils.FileOptionsUtils
import com.test.springboot.utils.LogUtils
import javabase.lorenwang.common_base_frame.SbcbflwBaseApplication
import javabase.lorenwang.common_base_frame.SbcbflwCommon
import javabase.lorenwang.common_base_frame.utils.SbcbfBaseAllUtils
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import springfox.documentation.swagger2.annotations.EnableSwagger2

/**
 * 功能作用：测试SpringBoot入口
 * 初始注释时间： 2020/6/15 12:27 下午
 * 注释创建人：LorenWang（王亮）
 * 方法介绍：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author LorenWang（王亮）
 */
@ServletComponentScan
@SpringBootApplication
@EnableSwagger2
open class StartApplication : SbcbflwBaseApplication() {
    override fun getConfigProperties() : Array<String> {
        return arrayOf("application.properties")
    }

    override fun outSideTomcatConfigureFinish(application : SpringApplication) {}

    companion object {
        @JvmStatic
        fun main(args : Array<String>) {
            SbcbfBaseAllUtils.logUtils = LogUtils.instance
            SbcbfBaseAllUtils.fileOptionsUtils = FileOptionsUtils.instance
            val springApplication = SpringApplication(StartApplication::class.java)
            LogUtils.instance.logI(this::class.java, "添加启动监听")
            springApplication.addListeners(ApplicationListenerForStart())
            initBase(springApplication, arrayOf("application.properties"))
            springApplication.run(*args)
        }
    }
}
