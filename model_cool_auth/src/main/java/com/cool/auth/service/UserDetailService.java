package com.cool.auth.service;

import cn.hutool.core.util.StrUtil;
import com.cool.auth.entity.SysUser;
import com.cool.core.base.constant.CommonConstants;
import com.cool.core.base.constant.SqlConstants;
import com.cool.core.security.entity.SecurityUser;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 菜王
 * @create 2020-11-05
 * security用户详情实现-自定义Security用户数据
 */
@Configuration
@AllArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<SysUser> userList = jdbcTemplate.query(SqlConstants.QUERY_USER, new Object[]{username}, new BeanPropertyRowMapper(SysUser.class));
        if (userList != null && userList.size() > 0) {
            SysUser sysUser = userList.get(0);
            Set<String> permissions = new HashSet<>();
            //角色 菜单权限
            List<Integer> urList = jdbcTemplate.query(SqlConstants.QUERY_ROLES, new Object[]{sysUser.getId()},(rs, rowNum)->rs.getInt(1));
            if(urList != null && urList.size() > 0){
                permissions.addAll(urList.stream().map(roleId ->CommonConstants.ROLE+roleId).collect(Collectors.toList()));
                urList.forEach(roleId -> {
                    List<String> pList = jdbcTemplate.query(SqlConstants.QUERY_PREMS, new Object[]{roleId},(rs,i)->rs.getString(1));
                    if(pList != null && pList.size() > 0 && StrUtil.isNotBlank(pList.get(0))){
                        permissions.addAll(pList);
                    }
                });
            }
            boolean notLocked = false;
            if ("0".equals(sysUser.getStatus())) {
                notLocked = true;
            }
            SecurityUser authUser = new SecurityUser(sysUser.getId(),sysUser.getUserName(), sysUser.getPassword(),
            true, true, true, notLocked,
            AuthorityUtils.createAuthorityList(permissions.toArray(new String[0])));
            BeanUtils.copyProperties(sysUser,authUser);
            return authUser;
        }else {
            throw new UsernameNotFoundException("用户未找到!");
        }
    }
}
