package com.cool.biz.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cool.biz.system.entity.User;
import com.cool.biz.system.entity.UserRole;
import com.cool.biz.system.mapper.UserMapper;
import com.cool.biz.system.service.UserRoleService;
import com.cool.biz.system.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *<p>
 *用户信息表 服务实现类
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/16
 */
@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserRoleService userRoleService;

    @Override
    public int saveUser(User user) {
        if (StrUtil.isEmptyIfStr(user.getId())) {
            // 新增用户信息
            int rows = baseMapper.insert(user);
            // 新增用户与角色管理
            addUserRole(user);
            return rows;
        } else {
            // 删除用户与角色关联
            userRoleService.remove(new QueryWrapper<UserRole>().eq("user_id", user.getId()));
            // 新增用户与角色管理
            addUserRole(user);
            user.setPassword(null);
            return baseMapper.updateById(user);
        }
    }


    @Override
    public int dsAdd() throws Exception {
        User user=new User();
        user.setId(88);
        user.setUserName("123");
        user.setPassword("123");
        user.setPhone("12345678911");
        baseMapper.insert(user);
        if(true){
            throw new Exception("错误");
        }
        return 0;
    }


    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void addUserRole(User user) {
        Integer[] roles = user.getRoles();
        if (roles != null) {
            // 新增用户与角色管理
            for (Integer roleId : roles) {
                UserRole ur = new UserRole();
                ur.setUserId(user.getId());
                ur.setRoleId(roleId);
                userRoleService.save(ur);
            }
        }
    }


}
