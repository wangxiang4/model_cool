package com.cool.biz.reptile.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 
 * @Author --Geek--
 * @Date 2020-09-28 
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)//set方法返回链式调用
@TableName("error_reptile_history")
@KeySequence(value = "mould_reptile_history_history_id_seq")
public class ErrorReptileHistory implements Serializable {

	private static final long serialVersionUID =  3030852457791878206L;

	@TableId(type= IdType.INPUT)
	private Long historyId;

	private Date historyTime;

	private String reptileUrl;

	private String reptileContent;

	private Boolean exception;

}
