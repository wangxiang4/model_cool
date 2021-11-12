package com.cool.biz.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/** 
 *<p>
 * 文件对象 FileInfo
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/16
 */ 
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_file_info")
public class FileInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Integer id;

    /** 文件名称 */
    private String name;

    /** 文件类型 */
    private String type;

    /** 文件格式 */
    private String format;

    /** 文件大小 */
    private String size;

    /** 文件保存路径 */
    private String path;

    /** 缩略图路径 */
    private String thumbnail;

    /** 状态 */
    private String status;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 备注 */
    private String remarks;

    @TableLogic
    private String delFlag;

    /** 开始时间 */
    @TableField(exist = false)
    @JsonIgnore
    private String beginTime;

    /** 结束时间 */
    @TableField(exist = false)
    @JsonIgnore
    private String endTime;

}
