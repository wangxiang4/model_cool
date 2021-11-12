package com.cool.core.security.service;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
/**
 * @author 菜王
 * @create 2020-11-03
 * 接口权限判断工具
 */
@Slf4j
@Component("ps")
public class PermissionService {

    /**
    * @Param: [permission]
    * @return: boolean
    * @Author: 菜王
    * @Date: 2020/11/3 21:41
    * @description: 判断接口是否有xxx_xxx权限
    */
    public boolean hasPerm(String permission) {
        if (StrUtil.isBlank(permission)) {
            return false;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        //匹配是否有该权限
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(StringUtils::hasText)
                .anyMatch(x -> PatternMatchUtils.simpleMatch(permission, x));
    }


    /**
    * @Param: [permissions]
    * @return: boolean
    * @Author: 菜王
    * @Date: 2020/11/3 21:42
    * @description: 验证用户是否具有以下任意一个权限
    */
    public boolean hasAnyPerm(String permissions) {
        if (StringUtils.isEmpty(permissions)) {
            return false;
        }
        for (String permission : permissions.split(",")) {
            if (permission != null && hasPerm(permission)) {
                return true;
            }
        }
        return false;
    }
}
