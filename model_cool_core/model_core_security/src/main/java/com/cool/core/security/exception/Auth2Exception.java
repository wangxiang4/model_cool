package com.cool.core.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author 菜王
 * @create 2020-11-03
 * 自定义OAuth2Exception
 */
@JsonSerialize(using = Auth2ExceptionSerializer.class)
public class Auth2Exception extends OAuth2Exception {

    @Getter
    private String errorCode;

    public Auth2Exception(String msg) {
        super(msg);
    }

    public Auth2Exception(String msg, String errorCode) {
        super(msg);
        this.errorCode=errorCode;
    }

}
