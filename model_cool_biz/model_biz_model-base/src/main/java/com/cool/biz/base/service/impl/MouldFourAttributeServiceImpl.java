package com.cool.biz.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cool.biz.base.entity.MouldFourAttribute;
import com.cool.biz.base.mapper.MouldFourAttributeMapper;
import com.cool.biz.base.service.MouldFourAttributeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 菜王
 * @create 2020-11-22
 */
@Service
@AllArgsConstructor
public class MouldFourAttributeServiceImpl extends ServiceImpl<MouldFourAttributeMapper, MouldFourAttribute> implements MouldFourAttributeService{

    private final MouldFourAttributeMapper mouldFourAttributeMapper;

    @Override
    public List<MouldFourAttribute> findList(String id) {
        return mouldFourAttributeMapper.findList(id);
    }

}
