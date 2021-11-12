package com.cool.biz.base.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cool.biz.base.entity.MouldProductItem;
import com.cool.biz.base.mapper.MouldProductItemMapper;
import com.cool.biz.base.service.MouldProductItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author 菜王
 * @create 2020-11-22
 */
@Service
@AllArgsConstructor
public class MouldProductItemServiceImpl extends ServiceImpl<MouldProductItemMapper, MouldProductItem> implements MouldProductItemService {
}
