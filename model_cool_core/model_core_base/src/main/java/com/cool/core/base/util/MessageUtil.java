package com.cool.core.base.util;

import cn.hutool.core.util.StrUtil;
import com.cool.core.base.config.GlobalConfig;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * @author 菜鸟小王子
 * 国际化消息处理
 */
public class MessageUtil {

    private static MessageSource messageSource = (MessageSource) SpringUtils.getBean("messageSource");

    /**
    * @Param: [code, params]
    * @return: java.lang.String
    * @Author: 菜王
    * @Date: 2020/11/3 23:08
    * @description: 返回国际化配置的消息
    */
    public static String getText(String code, String... params) {
        if (StrUtil.isBlank(code)) {
            return code;
        } else {
            Locale locale = Locale.CHINA;
            try {
                locale = new Locale(GlobalConfig.getLang());
            } catch (IllegalArgumentException iaEx) {}
            try {
                return messageSource.getMessage(code, params, locale);
            } catch (NoSuchMessageException var5) {
                return params != null && params.length > 0 ? (new MessageFormat(code != null ? code : "", LocaleContextHolder.getLocale())).format(params) : code;
            }
        }
    }
}
