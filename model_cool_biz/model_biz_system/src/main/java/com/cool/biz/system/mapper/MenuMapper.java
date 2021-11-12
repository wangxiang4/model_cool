package com.cool.biz.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cool.biz.system.entity.Menu;

import java.util.List;

/**
 *<p>
 *菜单权限表 Mapper 接口
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/16
 */
public interface MenuMapper extends BaseMapper<Menu>{

    /**
     * 通过角色编号查询菜单
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<Menu> selectMenuListByRoleId(Integer roleId);
    /**
     * 通过角色编号查询菜单编号
     *
     * @param roleId 角色ID
     * @return 菜单编号列表
     */
    List<Integer> selectMenusByRoleId(Integer roleId);

}
