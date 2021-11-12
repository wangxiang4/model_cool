package com.cool.biz.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cool.biz.system.entity.User;

/**
 *<p>
 * 用户信息表 服务类
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/16
 */
public interface UserService extends IService<User> {

    /**
     * 保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int saveUser(User user);

    int dsAdd() throws Exception;


}
