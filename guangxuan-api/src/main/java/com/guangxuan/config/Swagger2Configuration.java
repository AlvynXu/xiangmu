package com.guangxuan.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ZhuoLin
 */
@Configuration
@EnableSwagger2
//第三方swagger增强API注解
@EnableSwaggerBootstrapUI
@Profile({"dev", "test"})
public class Swagger2Configuration {

    private String token ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjMzMDgzNjA3MzE0LCJ1c2VybmFtZSI6IjE4MzI1NTY5MDczIn0.rHQ5YGW12nTdejzj8AzU2g3nmcXfZr2R2GaHdaIOmeg";

    //app1.0.2版本对外接口
    @Bean
    public Docket vApp102() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName(ApiVersionConstant.FAP_APP102)
                .select()
                .apis(input -> {
                    ApiVersion apiVersion = input.getHandlerMethod().getMethodAnnotation(ApiVersion.class);
                    if (apiVersion != null && Arrays.asList(apiVersion.group()).contains(ApiVersionConstant.FAP_APP102)) {
                        return true;
                    }
                    return false;
                })//controller路径
                .paths(PathSelectors.any())
                .build();
    }

    //app1.0.1版本对外接口
    @Bean
    public Docket vApp101() {
        //添加header参数
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        ticketPar.name("token").description("user token")
                .modelRef(new ModelRef("string")).parameterType("header")
                .defaultValue(token)
                //header中的ticket参数非必填，传空也可以
                .required(true).build();
        pars.add(ticketPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName(ApiVersionConstant.FAP_APP101)
                .select()
                .apis(input -> {
                    ApiVersion apiVersion = input.getHandlerMethod().getMethodAnnotation(ApiVersion.class);
                    if (apiVersion != null && Arrays.asList(apiVersion.group()).contains(ApiVersionConstant.FAP_APP101)) {
                        return true;
                    }
                    return false;
                })//controller路径
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    //app1.0.1版本对外接口
    @Bean
    public Docket vApp103() {
        //添加header参数
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        ticketPar.name("token").description("user token")
                .modelRef(new ModelRef("string")).parameterType("header")
                .defaultValue(token)
                //header中的ticket参数非必填，传空也可以
                .required(true).build();
        pars.add(ticketPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName(ApiVersionConstant.FAP_APP103)
                .select()
                .apis(input -> {
                    ApiVersion apiVersion = input.getHandlerMethod().getMethodAnnotation(ApiVersion.class);
                    if (apiVersion != null && Arrays.asList(apiVersion.group()).contains(ApiVersionConstant.FAP_APP103)) {
                        return true;
                    }
                    return false;
                })//controller路径
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }


    @Bean
    public Docket createRestApi() {

        //添加header参数
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        ticketPar.name("token").description("user token")
                .modelRef(new ModelRef("string")).parameterType("header")
                .defaultValue(token)
                //header中的ticket参数非必填，传空也可以
                .required(true).build();
        //根据每个方法名也知道当前方法在设置什么参数
        pars.add(ticketPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.guangxuan.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("象目api文档")
                .version("1.0")
                .build();
    }


}