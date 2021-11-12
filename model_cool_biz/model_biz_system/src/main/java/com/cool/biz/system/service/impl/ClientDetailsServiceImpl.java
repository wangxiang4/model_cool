package com.cool.biz.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cool.biz.system.entity.ClientDetails;
import com.cool.biz.system.mapper.ClientDetailsMapper;
import com.cool.biz.system.service.ClientDetailsService;
import org.springframework.stereotype.Service;

/**
 *<p>
 * 授权客户端 服务实现类
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/20
 */
@Service
public class ClientDetailsServiceImpl extends ServiceImpl<ClientDetailsMapper, ClientDetails> implements ClientDetailsService {

}
