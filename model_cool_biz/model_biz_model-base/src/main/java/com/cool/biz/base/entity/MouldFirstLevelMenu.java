package com.cool.biz.base.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description 
 * @Author --Geek--
 * @Date 2020-09-28 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)//set方法返回链式调用
@TableName("mould_first_level_menu")
public class MouldFirstLevelMenu  implements Serializable {

	private static final long serialVersionUID =  147865159054822474L;

	/**
	 * 一级菜单Id
	 */
	@TableId
	private String menuFirstId;

	/**
	 * 菜单名称
	 */
	private String menuFirstName;

	/**
	 * 菜单图片
	 */
	private String menuImgId;

	/**
	 * 权重
	 */
	private Long menuFirstWeights;

	/**
	 * 关联Bom标准模块
	 */
	private String bomId;

	/***
	 * 一级菜单code
	 */
	private String menuFirstCode;


}
