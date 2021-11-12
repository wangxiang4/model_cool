package com.cool.core.swagger.config;

import com.cool.core.base.config.GlobalConfig;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;

/**
 *<p>
 * swagger 配置
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/20
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/dev")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)) //对所有ApiOperation注解的api进行监控
                .paths(PathSelectors.any()) //对所有路径进行监控
                .build()
                .securitySchemes(Collections.singletonList(securitySchemes()))
                .securityContexts(Collections.singletonList(securityContexts()));
    }


    /**
     * 认证方式使用密码模式
     */
    private SecurityScheme securitySchemes() {
        GrantType grantType = new ResourceOwnerPasswordCredentialsGrant("/dev/oauth/token?wx=wx");

        return new OAuthBuilder()
                .name("Authorization")
                .grantTypes(Collections.singletonList(grantType))
                .scopes(Arrays.asList(scopes()))
                .build();
    }

    /**
     * 设置 swagger2 认证的安全上下文
     */
    private SecurityContext securityContexts() {
        return SecurityContext.builder()
                .securityReferences(Collections.singletonList(new SecurityReference("Authorization", scopes())))
                //正则式过滤,此处是所有非login开头的接口都需要认证
                .forPaths(PathSelectors.regex("^(?!login).*$"))
                .build();
    }

    /**
     * 允许认证的scope
     */
    private AuthorizationScope[] scopes() {
        return new AuthorizationScope[]{
                new AuthorizationScope("server", "服务器权限")
        };
    }

    /**
     * 添加摘要信息
     */
    private ApiInfo apiInfo() {
        // 用ApiInfoBuilder进行定制
        return new ApiInfoBuilder()
                // 设置标题
                .title(GlobalConfig.getName())
                // 描述
                .description(GlobalConfig.getName() + "接口文档")
                // 版本
                .version("版本号:" + GlobalConfig.getVersion())
                .build();
    }
}
