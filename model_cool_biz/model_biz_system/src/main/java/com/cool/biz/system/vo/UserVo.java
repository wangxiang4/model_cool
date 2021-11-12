package com.cool.biz.system.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author entfrm
 * @date 2020/5/24
 * @description
 */
/**
 *<p>
 * 用户vo
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/16
 */
@Data
public class UserVo implements Serializable {

    /**
     * 用户ID
     */
    private Integer id;

    /**
     * 昵称
     */
    private String nickName;
}

