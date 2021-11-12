package com.cool.core.base.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @author 菜王
 * 国际化配置
 */
@Configuration
public class MessageSourceConfig {


	/**
	* @Param: []
	* @return: org.springframework.context.MessageSource
	* @Author: 菜王
	* @Date: 2020-09-28 22:20
	* @description: 创建国际化消息对象并设置资源
	*/
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource= new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:i18n/messages");
		return messageSource;
	}

}
