package com.cool.core.security.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author 菜王
 * @create 2020-11-03
 */
public class SecurityUser extends User{
    
    //用户ID
    @Getter
    private Integer id;

    public SecurityUser(Integer id,String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id=id;
    }

    /**
    * @Param: []
    * @return: boolean 
    * @Author: 菜王
    * @Date: 2020/11/3 20:06 
    * @description: 管理员校验
    */ 
    public boolean isAdmin() {
        return isAdmin(this.id);
    }

    public static boolean isAdmin(Integer id) {
        return id != null && 1 == id;
    }


}
