package com.cool.auth.filter;

import cn.hutool.core.util.StrUtil;
import com.cool.core.base.config.GlobalConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author 菜王
 * @create 2020-11-06
 * 验证码过滤器
 */
@Component
@Data
@AllArgsConstructor
public class CaptchaFilter extends OncePerRequestFilter{

    /**
     * redis验证码目录
     **/
    private final String REDIS_DIR = "captcha:";

    private RedisTemplate redisTemplate;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isLoginReq(request)) {
            if (validateCaptcha(request)) {
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(428);
                return;
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }


    /**
     * 验证验证码合法性
     *
     * @param request
     * @return
     */
    private boolean validateCaptcha(HttpServletRequest request) {
        if (!GlobalConfig.isRedisSwitch()) {
            return true;
        }
        String code = request.getParameter("code");
        String realKey = request.getParameter("realKey");
        if (StrUtil.isBlank(code) || StrUtil.isBlank(realKey)) {
            return false;
        }
        String redisKey = REDIS_DIR + realKey;
        Object obj = redisTemplate.opsForValue().get(redisKey);
        if (!Objects.isNull(obj)) {
            redisTemplate.delete(redisKey);
            return obj.toString().toLowerCase().equals(code.toLowerCase());
        }
        return false;

    }


    /**
     * 是否 login的POST请求
     *
     * @param request
     * @return
     */
    private boolean isLoginReq(HttpServletRequest request) {
        if(StrUtil.isNotBlank(request.getParameter("wx"))&&request.getParameter("wx").equals("wx")){
            return false;
        }else{
            return "POST".equals(request.getMethod()) && "/oauth/token".equals(request.getServletPath());
        }
    }



}
