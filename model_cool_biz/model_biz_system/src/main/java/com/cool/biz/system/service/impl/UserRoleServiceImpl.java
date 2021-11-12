package com.cool.biz.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cool.biz.system.entity.UserRole;
import com.cool.biz.system.mapper.UserRoleMapper;
import com.cool.biz.system.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 *<p>
 *用户和角色关联表 服务实现类
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/16
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
