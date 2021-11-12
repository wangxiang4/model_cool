package com.cool.biz.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cool.biz.system.entity.Role;

import java.util.List;

/**
 *<p>
 *角色信息表 Mapper 接口
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/16
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
     List<Role> selectRolesByUserId(Integer userId);

}
