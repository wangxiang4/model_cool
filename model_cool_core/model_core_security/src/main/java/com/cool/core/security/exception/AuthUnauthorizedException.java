package com.cool.core.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;

/**
 * @author 菜王
 * @create 2020-11-03
 */
@JsonSerialize(using = Auth2ExceptionSerializer.class)
public class AuthUnauthorizedException extends Auth2Exception {

	public AuthUnauthorizedException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "unauthorized";
	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.UNAUTHORIZED.value();
	}

}
