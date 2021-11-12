package com.cool.biz.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cool.core.data.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *<p>
 * 字典数据表
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_data")
public class DictData extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 数据编号
     */
    @TableId
    private Integer id;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 字典标签
     */
    private String label;

    /**
     * 字典键值
     */
    private String value;

    /**
     * 字典排序
     */
    private Integer sort;

}
