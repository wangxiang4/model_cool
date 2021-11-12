package com.cool.core.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;

/**
 * @author 菜王
 * @create 2020-11-03
 */
@JsonSerialize(using = Auth2ExceptionSerializer.class)
public class AuthServerErrorException extends Auth2Exception {

	public AuthServerErrorException(String msg, Throwable t) {
		super(msg);
	}

	@Override
	public String getOAuth2ErrorCode() {
		return "server_error";
	}

	@Override
	public int getHttpErrorCode() {
		return HttpStatus.INTERNAL_SERVER_ERROR.value();
	}

}
