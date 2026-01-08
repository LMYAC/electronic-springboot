package com.qf.electronic.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2 //启用Swagger
@EnableWebMvc
public class SwaggerConfig {

    @Bean //@Bean注解的作用就是将该方法创建出来的对象纳入Spring IOC容器管理
    public Docket api() {
        //Docket类就是Swagger提供的一个与Spring MVC集成的配置类
        return new Docket(DocumentationType.SWAGGER_2) //文档类型设置为SWAGGER2
                .select() //选择当前文档类型进行构建
                .apis(RequestHandlerSelectors.basePackage("com.qf.electronic.controller")) //请求控制器包
                .paths(PathSelectors.any())//为任意请求构建API文档
                .build() //构建API
                .apiInfo(apiInfo()); //设置AIP文档的信息
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("电力巡检项目接口文档")
                .description("电力巡检项目接口测试")
                .version("1.0.0")
                .termsOfServiceUrl("") //服务条款地址
                .license("") //许可证
                .licenseUrl("") //许可证URL
                .build();
    }
}
