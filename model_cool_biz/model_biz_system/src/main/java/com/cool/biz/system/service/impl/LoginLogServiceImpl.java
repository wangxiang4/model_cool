package com.cool.biz.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cool.biz.system.entity.LoginLog;
import com.cool.biz.system.mapper.LoginLogMapper;
import com.cool.biz.system.service.LoginLogService;
import org.springframework.stereotype.Service;


/**
 *<p>
 * 系统访问记录 服务实现类
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/21
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

}
