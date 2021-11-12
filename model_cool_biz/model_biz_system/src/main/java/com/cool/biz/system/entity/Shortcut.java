package com.cool.biz.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cool.core.data.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 *<p>
 * 快捷方式对象 Shortcut
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_shortcut")
public class Shortcut extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 编号 */
    @TableId
    private Integer id;

    /** 名称 */
    private String name;

    /** 图标 */
    private String icon;

    /** 背景颜色 */
    private String bgColor;

    /** 路径 */
    private String path;

    /** 顺序 */
    private Integer sort;

}
