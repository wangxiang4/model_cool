package com.cool.biz.reptile.entity;

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
@TableName("mould_two_level_menu")
public class MouldTwoLevelMenu  implements Serializable {

	private static final long serialVersionUID =  2483148696201584056L;

	@TableId
	private String menuTwoId;

	private String menuTwoCode;

	private String menuTwoName;

	private String menuImgId;

	private Long menuTwoWeights;

	private String menuFirstId;

}
