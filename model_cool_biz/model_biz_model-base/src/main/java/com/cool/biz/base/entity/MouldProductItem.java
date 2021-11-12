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
@TableName("mould_product_item")
public class MouldProductItem  implements Serializable {

	private static final long serialVersionUID =  4739808147710680911L;

	@TableId
	private String productId;

	private String productName;

	private String productTitle;

	private String titleImgId;

	private String drawingImgId;

	private String mouldDrawingImgId;

	private String mouldParamPackage;

	private String attributeId;

	private Long productWeights;
}
