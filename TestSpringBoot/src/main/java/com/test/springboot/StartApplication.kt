package com.test.springboot

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import springbase.lorenwang.common_base_frame.SbcbflwBaseApplication
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
@EnableJpaAuditing
@EntityScan(basePackages = ["springbase.lorenwang.user.database.table", "com.test.springboot.database.table"])
open class StartApplication : SbcbflwBaseApplication() {
    override fun getConfigProperties(): Array<String> {
        return arrayOf("application.properties", "application-dev.properties")
    }

    override fun outSideTomcatConfigureFinish(application: SpringApplication) {}

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            configOptions
            //初始化单例工具类
            val springApplication = SpringApplication(StartApplication::class.java)
            initBase(springApplication, arrayOf("application.properties", "application-dev.properties"))
            springApplication.run(*args)
        }
    }
}
