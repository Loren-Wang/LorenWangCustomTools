package com.test.springboot;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import javabase.lorenwang.common_base_frame.SbcbflwBaseApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
public class SpringbootApplication extends SbcbflwBaseApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(SpringbootApplication.class);
        SpringbootApplication.Companion.initBase(springApplication,new String[]{"application.properties"});
        springApplication.run(args);
    }

    @NotNull
    @Override
    public String[] getConfigProperties() {
        return new String[]{"application.properties"};
    }

    @Override
    public void outSideTomcatConfigureFinish(@NotNull SpringApplication springApplication) {

    }
}
