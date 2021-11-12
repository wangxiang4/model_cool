package com.cool.biz.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cool.biz.system.entity.Role;
import com.cool.biz.system.entity.RoleMenu;
import com.cool.biz.system.mapper.RoleMapper;
import com.cool.biz.system.service.RoleMenuService;
import com.cool.biz.system.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 *<p>
 * 角色信息表 服务实现类
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/16
 */
@Service
@AllArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final RoleMenuService roleMenuService;

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRoleKeys(Integer userId) {
        List<Role> perms = baseMapper.selectRolesByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (Role perm : perms) {
            if (perm != null) {
                permsSet.addAll(Arrays.asList(perm.getCode().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<Role> selectMyRolesByUserId(Integer userId) {
        return baseMapper.selectRolesByUserId(userId);
    }

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @Override
    public List<Role> selectRolesByUserId(Integer userId) {
        List<Role> roles = baseMapper.selectList(new QueryWrapper<>());
        if (StrUtil.isEmptyIfStr(userId)) {
            return roles;
        } else {
            List<Role> userRoles = baseMapper.selectRolesByUserId(userId);
            for (Role role : roles) {
                for (Role userRole : userRoles) {
                    if (role.getId() == userRole.getId()) {
                        role.setFlag(true);
                        break;
                    }
                }
            }
            return roles;
        }
    }

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public boolean insertRole(Role role) {
        // 新增角色信息
        baseMapper.insert(role);
        return insertRoleMenu(role);
    }

    /**
     * 修改数据权限信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean updatePerms(Role role) {
        // 修改角色信息
        baseMapper.updateById(role);
        return true;
    }



    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public boolean updateRoleMenu(Role role) {
        // 修改角色信息
        baseMapper.updateById(role);
        // 删除角色与菜单关联
        roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("role_id", role.getId()));
        return insertRoleMenu(role);
    }

    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    public boolean insertRoleMenu(Role role) {
        boolean rows = true;
        // 新增用户与角色管理
        List<RoleMenu> list = new ArrayList<RoleMenu>();
        for (Integer menuId : role.getMenuIds()) {
            RoleMenu rm = new RoleMenu();
            rm.setRoleId(role.getId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0) {
            rows = roleMenuService.saveBatch(list);
        }
        return rows;
    }
}
