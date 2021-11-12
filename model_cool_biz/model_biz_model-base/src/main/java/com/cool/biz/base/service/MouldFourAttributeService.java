package com.cool.biz.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cool.biz.base.entity.MouldFourAttribute;

import java.util.List;

/**
 * @author 菜王
 * @create 2020-11-22
 */
public interface MouldFourAttributeService extends IService<MouldFourAttribute> {
    List<MouldFourAttribute> findList(String id);
}
