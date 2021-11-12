package com.cool.biz.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cool.biz.base.entity.MouldTwoLevelMenu;
import com.cool.biz.base.mapper.MouldTwoLevelMenuMapper;
import com.cool.biz.base.service.MouldTwoLevelMenuService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author 菜王
 * @create 2020-11-22
 */
@Service
@AllArgsConstructor
public class MouldTwoLevelMenuServiceImpl extends ServiceImpl<MouldTwoLevelMenuMapper, MouldTwoLevelMenu> implements MouldTwoLevelMenuService{
}
