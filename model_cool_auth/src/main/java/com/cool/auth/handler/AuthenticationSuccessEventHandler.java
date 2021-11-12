package com.cool.auth.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.cool.auth.util.LoginLogUtil;
import com.cool.core.base.constant.SecurityConstants;
import com.cool.core.base.constant.SqlConstants;
import com.cool.core.security.entity.SecurityUser;
import com.cool.core.security.handler.AbstractAuthenticationSuccessEventHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;

/**
 * @author 菜王
 * @create 2020-11-06
 */
@Slf4j
@Component
@AllArgsConstructor
public class AuthenticationSuccessEventHandler extends AbstractAuthenticationSuccessEventHandler {

    private final TaskExecutor taskExecutor;
    private final JdbcTemplate jdbcTemplate;

    /**
     * 处理登录成功方法
     * <p>
     * 获取到登录的authentication 对象
     *
     * @param authentication 登录对象
     * @param request        请求
     * @param response       返回
     */
    @Override
    public void handle(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        String url = URLUtil.getPath(request.getRequestURI());
        log.info("用户：{} 授权成功，url：{}",securityUser, url);
        String loginType = "0"; // 0 登录 1退出
        if (StrUtil.containsAny(url,SecurityConstants.AUTH_TOKEN,SecurityConstants.TOKEN_LOGOUT)) {
            if(SecurityConstants.TOKEN_LOGOUT.equals(url)){
                loginType = "1";
            }
            PreparedStatementSetter pss = LoginLogUtil.setLoginLog(request, loginType, securityUser.getUsername(), "");
            CompletableFuture.runAsync(() -> {
                log.info("执行结果：" + jdbcTemplate.update(SqlConstants.LOGIN_LOG, pss));
            }, taskExecutor);
        }
    }
}
