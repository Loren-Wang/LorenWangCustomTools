package com.test.springboot

import com.google.common.base.Predicates
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.schema.ModelRef
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Parameter
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


/**
 * Swagger2配置类
 * 在与spring boot集成时，放在与Application.java同级的目录下。
 * 通过@Configuration注解，让Spring来加载该类配置。
 * 再通过@EnableSwagger2注解来启用Swagger2。
 */
@Configuration
@EnableSwagger2
open class SwaggerConfig {
    /**
     * 创建API应用
     * apiInfo() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
     * 本例采用指定扫描的包路径来定义指定要建立API的目录。
     *
     * @return
     */
    @Bean
    open fun createRestApi(): Docket {
        //添加所有的默认请求餐
        val tokenPar = ParameterBuilder()
        val pars: MutableList<Parameter> = ArrayList()
        configOptions.getAccessControlAllowHeadersAddKey().split(",").forEach {
            tokenPar.name(it).description("用户登录后获取到的请求信息").modelRef(ModelRef("string")).parameterType("header").required(false).build()
        }
        pars.add(tokenPar.build())
        return Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).pathMapping("/").select() // 选择那些路径和api会生成document
            .apis(RequestHandlerSelectors.any())// 对所有api进行监控
            //不显示错误的接口地址
            .paths(Predicates.not(PathSelectors.regex("/error.*")))//错误路径不监控
            .paths(PathSelectors.regex("/.*"))// 对根下所有路径进行监控
            .build().globalOperationParameters(pars)
    }

    /**
     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
     * 访问地址：http://项目实际地址/swagger-ui.html
     * @return
     */
    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder().title("").description("更多请关注http://www.baidu.com").termsOfServiceUrl("http://www.baidu.com").version("1.0").build()
    }
}
