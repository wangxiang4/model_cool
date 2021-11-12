package com.cool.core.data.config;

import com.baomidou.mybatisplus.extension.incrementer.PostgreKeyGenerator;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
* @Author: 菜王
* @Date: 2020/11/3
 * mybatisPlus配置
*/
@Configuration
@MapperScan("com.cool.biz.*.mapper")
public class MybatisPlusConfig {

	/**
	* @Param: []
	* @return: com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor
	* @Author: 菜王
	* @Date: 2020/9/24 22:37
	* @description:  分页插件配置(必须有条件)
	*/
	@Bean
	@ConditionalOnMissingBean
	public PaginationInterceptor paginationInterceptor() {
		return new PaginationInterceptor();
	}


	/**
	* @Param: []
	* @return: com.baomidou.mybatisplus.extension.incrementer.PostgreKeyGenerator
	* @Author: 菜王
	* @Date: 2020/9/28 14:27
	* @description: postgres序列生成器
	*/
	@Bean
	public PostgreKeyGenerator postgreKeyGenerator(){
		return new PostgreKeyGenerator();
	}

}
