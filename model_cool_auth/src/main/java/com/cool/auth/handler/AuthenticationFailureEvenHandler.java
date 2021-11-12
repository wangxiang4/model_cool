package com.cool.auth.handler;

import com.cool.auth.util.LoginLogUtil;
import com.cool.core.base.constant.SqlConstants;
import com.cool.core.security.handler.AbstractAuthenticationFailureEvenHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.CompletableFuture;

/**
 * @author 菜王
 * @create 2020-11-05
 */
@Slf4j
@Component
@AllArgsConstructor
public class AuthenticationFailureEvenHandler extends AbstractAuthenticationFailureEvenHandler {

    private final TaskExecutor taskExecutor;
    private final JdbcTemplate jdbcTemplate;

    /**
     * 处理登录失败方法
     * <p>
     *
     * @param authenticationException 登录的authentication 对象
     * @param authentication          登录的authenticationException 对象
     * @param request                 请求
     * @param response                响应
     */
    @Override
    public void handle(AuthenticationException authenticationException, Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        log.info("用户：{} 登录失败，异常：{}", authentication.getPrincipal(), authenticationException.getLocalizedMessage());
        PreparedStatementSetter pss = LoginLogUtil.setLoginLog(request, "0", authentication.getPrincipal().toString(),"登录失败，异常：" + authenticationException.getLocalizedMessage());
        CompletableFuture.runAsync(() -> {
            log.info("执行结果："+jdbcTemplate.update(SqlConstants.LOGIN_LOG, pss));
        }, taskExecutor);

    }
}
