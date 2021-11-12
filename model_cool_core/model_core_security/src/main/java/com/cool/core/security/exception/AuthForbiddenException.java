package com.cool.core.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.http.HttpStatus;

/**
 * @author 菜王
 * @create 2020-11-03
 * 授权403禁止访问异常类
 */
@JsonSerialize(using = Auth2ExceptionSerializer.class)
public class AuthForbiddenException extends Auth2Exception {

    //处理异常不抛出-后期可自由处理
    public AuthForbiddenException(String msg, Throwable t) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "access_denied";
    }

    @Override
    public int getHttpErrorCode() {
        return HttpStatus.FORBIDDEN.value();
    }

}
