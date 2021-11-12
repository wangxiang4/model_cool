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
@TableName("mould_four_attribute")
public class MouldFourAttribute  implements Serializable {

	private static final long serialVersionUID =  3524700684358538643L;

	@TableId
	private String attributeId;

	private String attributeParentId;

	private String attributeName;

	private String attributeImgId;

	private Long attributeWeights;

	private String menuTwoId;


}
