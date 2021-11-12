package com.cool.auth.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.cool.core.base.util.AddressUtil;
import lombok.experimental.UtilityClass;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author 菜王
 * @create 2020-11-06
 */
@UtilityClass
public class LoginLogUtil {

    public PreparedStatementSetter setLoginLog(HttpServletRequest request, String loginType, String userName, String errorMsg){
        String ip = ServletUtil.getClientIP(request);
        return (ps)->{
                ps.setString(1, userName);
                ps.setString(2, loginType);
                ps.setString(3, ip);
                ps.setString(4, AddressUtil.getCityInfo(ip));
                ps.setString(5, request.getHeader("user-agent"));
                if (StrUtil.isNotBlank(errorMsg)) {
                    ps.setString(6, "1");
                    ps.setString(7, errorMsg);
                } else {
                    ps.setString(6, "0");
                    ps.setString(7, "");
                }
                ps.setString(8,DateUtil.now());
            };
    }

    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }


}
