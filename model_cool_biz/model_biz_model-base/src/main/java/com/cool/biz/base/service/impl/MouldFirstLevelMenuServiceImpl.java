package com.cool.biz.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cool.biz.base.entity.MouldFirstLevelMenu;
import com.cool.biz.base.mapper.MouldFirstLevelMenuMapper;
import com.cool.biz.base.service.MouldFirstLevelMenuService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author 菜王
 * @create 2020-11-22
 */
@Service
@AllArgsConstructor
public class MouldFirstLevelMenuServiceImpl extends ServiceImpl<MouldFirstLevelMenuMapper, MouldFirstLevelMenu> implements MouldFirstLevelMenuService {
}
