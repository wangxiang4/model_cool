package com.cool.core.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author 菜王
 * @create 2020-11-03
 * 授权无效异常类
 */
@JsonSerialize(using = Auth2ExceptionSerializer.class)
public class AuthInvalidException extends Auth2Exception {

	public AuthInvalidException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "invalid_exception";
	}

	@Override
	public int getHttpErrorCode() {
		return 426;
	}

}
