package com.cool.biz.monotor.service;

import java.util.Map;


/**
 *<p>
 * redis 数据
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/20
 */
public interface RedisService {
	/**
	 * 获取内存信息
	 *
	 * @return
	 */
	Map<String, Object> getInfo();
}
