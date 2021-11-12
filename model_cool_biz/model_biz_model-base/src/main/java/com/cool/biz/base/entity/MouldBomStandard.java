package com.cool.biz.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("mould_bom_standard")
public class MouldBomStandard  implements Serializable {

	private static final long serialVersionUID =  6593510727693523697L;

	/**
	 * 标准ID
	 */
	@TableId
	private String bomId;

	/**
	 * 标准名称
	 */
	private String bomName;

	/**
	 * 标准代码
	 */
	private String bomCode;

	/**
	 * 权重
	 */
	private Long bomWeights;

	//历史记录
	@TableField(exist = false)
	private ErrorReptileHistory mouldReptileHistory=new ErrorReptileHistory();


}
