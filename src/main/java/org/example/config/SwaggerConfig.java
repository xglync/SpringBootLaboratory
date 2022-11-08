package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger配置类
 */
@Configuration   //注解下面的类为一个配置类，加载到spring容器
@EnableSwagger2   //注解类支持Swagger2
public class SwaggerConfig {

    @Bean  //注解的作用是，把方法的返回值对象，注入spring容器
    public Docket docket(){
        return  new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径,控制器类包
                .apis(RequestHandlerSelectors.basePackage("org.example.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    //设置Swaggle接口信息
    public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                //页面标题
                .title("医院挂号平台API接口文档")
                //创建人
                .contact(new Contact("王佳宝", "http://www.javachat.cc",
                        "3049352171@qq.com"))
                //版本号
                .version("1.0")
                //描述
                .description("系统API描述")
                .build();
    }
}