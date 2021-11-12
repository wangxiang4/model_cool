package com.cool.auth.config;

import com.cool.auth.filter.CaptchaFilter;
import com.cool.core.base.config.GlobalConfig;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 菜王
 * @create 2020-11-06
 */
@Configuration
@EnableResourceServer
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter implements WebMvcConfigurer {
    private final RedisTemplate redisTemplate;

    @Override
    @SneakyThrows
    public void configure(HttpSecurity http) {
        http
        // CRSF禁用，因为不使用session
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/system/user/**","/oauth/**","/common/**","/captcha/**").permitAll()
        .anyRequest().authenticated()
        .and().addFilterBefore(new CaptchaFilter(redisTemplate), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /** 本地文件上传路径 */
        registry.addResourceHandler("/profile/**").addResourceLocations("file:" + GlobalConfig.getProfile());
    }
    
}
