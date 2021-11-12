package com.cool.core.security.handler;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 菜王
 * @create 2020-11-03
 * 认证失败事件处理器
 */
public abstract class AbstractAuthenticationFailureEvenHandler implements ApplicationListener<AbstractAuthenticationFailureEvent> {

	/**
	 * Handle an application event.
	 * @param event the event to respond to
	 * 登录失败会进这个事件,可以做一些处理(登录和登录失败去限制登录次数)
	 */
	@Override
	public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		HttpServletResponse response = requestAttributes.getResponse();
		AuthenticationException authenticationException = event.getException();
		Authentication authentication = (Authentication) event.getSource();
		handle(authenticationException, authentication, request, response);
	}

	/**
	 * 提供处理登录失败方法
	 * <p>
	 *
	 * @param authenticationException 登录的authentication 对象
	 * @param authentication          登录的authenticationException 对象
	 * @param request                 请求
	 * @param response                响应
	 */
	public abstract void handle(AuthenticationException authenticationException, Authentication authentication
			,HttpServletRequest request, HttpServletResponse response);
}
