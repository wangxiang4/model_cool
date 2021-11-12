package com.cool.core.log.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpUtil;
import com.cool.core.base.util.AddressUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 *<p>
 * 操作日志util
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/18
 */
@Slf4j
@UtilityClass
public class OperLogUtil {

    public PreparedStatementSetter setOperLog(String title, long time, String userName, String clientId, String errorMsg) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = ServletUtil.getClientIP(request);
        return (ps)->{
                ps.setString(1, "1");
                ps.setString(2, title);
                ps.setString(3, request.getMethod());
                ps.setString(4, request.getHeader("user-agent"));
                ps.setString(5, userName);
                ps.setString(6, clientId);
                ps.setString(7, URLUtil.getPath(request.getRequestURI()));
                ps.setString(8, ip);
                ps.setString(9, AddressUtil.getCityInfo(ip));
                ps.setString(10, HttpUtil.toParams(request.getParameterMap()));
                if (StrUtil.isNotBlank(errorMsg)) {
                    ps.setString(11, "1");
                    ps.setString(12, errorMsg);
                } else {
                    ps.setString(11, "0");
                    ps.setString(12, "");
                }
                ps.setString(13, time + "");
                ps.setString(14, DateUtil.now());
            };
    };


    /**
     * 获取客户端
     *
     * @return clientId
     */
    public String getClientId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication auth2Authentication = (OAuth2Authentication) authentication;
            return auth2Authentication.getOAuth2Request().getClientId();
        }
        return null;
    }

    /**
     * 获取用户名称
     *
     * @return username
     */
    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getName();
    }
}
