package com.cool.biz.system.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
/**
 *<p>
 * 返回结果
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/16
 */
@Data
@Builder
public class ResultVo<T> implements Serializable {

    /**
     * 查询结果
     */
    private T result;
    /**
     * 扩展信息
     */
    private T extend;

}
