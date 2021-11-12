package com.cool.biz.base.entity;

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
@TableName("mould_file")
public class MouldFile  implements Serializable {

	private static final long serialVersionUID =  5421686924817761165L;

	@TableId
	private String fileId;

	private Date uploadDate;

	private String fileName;

	private String filePath;

	private String fileSufixx;

	private String linkFileId;

	private String fileBackPath;

}
