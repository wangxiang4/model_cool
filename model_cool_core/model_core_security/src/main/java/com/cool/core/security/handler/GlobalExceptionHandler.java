package com.cool.core.security.handler;

import com.cool.core.base.api.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
/**
 * @author 菜王
 * @create 2020-11-03
 * 全局异常处理器
 * control切面
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
    * @Param: [e]
    * @return: com.cool.core.base.api.R
    * @Author: 菜王
    * @Date: 2020/11/3 21:46
    * @description: 捕捉全局异常
    */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R handleGlobalException(Exception e) {
        log.error("全局异常信息 ex={}", e.getMessage(), e);
        return R.error(e.getLocalizedMessage());
    }


    /** 
    * @Param: [e]
    * @return: com.cool.core.base.api.R 
    * @Author: 菜王
    * @Date: 2020/11/3 21:45 
    * @description: 捕捉访问被拒绝的异常
    */ 
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R handleAccessDeniedException(AccessDeniedException e) {
        String msg = SpringSecurityMessageSource.getAccessor()
                .getMessage("AbstractAccessDecisionManager.accessDenied"
                        , e.getMessage());
        log.error("拒绝授权异常信息 ex={}", msg, e);
        return R.error(e.getLocalizedMessage());
    }



    /**
    * @Param: [exception]
    * @return: com.cool.core.base.api.R
    * @Author: 菜王
    * @Date: 2020/11/3 21:46
    * @description: 捕捉方法参数无效异常
    */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R handleBodyValidException(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        log.error("参数绑定异常信息 ex = {}", fieldErrors.get(0).getDefaultMessage());
        return R.error(fieldErrors.get(0).getDefaultMessage());
    }
}
