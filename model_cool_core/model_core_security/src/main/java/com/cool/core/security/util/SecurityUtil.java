package com.cool.core.security.util;

import cn.hutool.core.util.StrUtil;
import com.cool.core.base.constant.CommonConstants;
import com.cool.core.security.entity.SecurityUser;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author 菜王
 * @create 2020-11-03
 * 安全工具类
 */
@UtilityClass//设置所有方法静态
public class SecurityUtil {

	/**
	 * @Param: []
	 * @return: org.springframework.security.core.Authentication
	 * @Author: 菜王
	 * @Date: 2020/11/3 21:50
	 * @description: 获取Authentication
	 */
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}


	/**
	* @Param: []
	* @return: com.cool.core.security.entity.SecurityUser
	* @Author: 菜王
	* @Date: 2020/11/3 21:51
	* @description: 获取当前用户对象
	*/
	public SecurityUser getUser() {
		Authentication authentication = getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof SecurityUser) {
			return (SecurityUser) principal;
		}
		return null;
	}


	/** 
	* @Param: []
	* @return: java.util.List<java.lang.Integer> 
	* @Author: 菜王
	* @Date: 2020/11/3 21:52 
	* @description: 获取用户角色信息
	*/ 
	public List<Integer> getRoles() {
		Authentication authentication = getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		List<Integer> roleIds = new ArrayList<>();
		authorities.stream()
				.filter(granted -> StrUtil.startWith(granted.getAuthority(),CommonConstants.ROLE))
				.forEach(granted -> {
					String id = StrUtil.removePrefix(granted.getAuthority(),CommonConstants.ROLE);
					roleIds.add(Integer.parseInt(id));
				});
		return roleIds;
	}

	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("cool"));
	}
}
