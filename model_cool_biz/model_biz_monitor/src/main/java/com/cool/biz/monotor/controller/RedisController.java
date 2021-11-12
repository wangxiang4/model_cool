package com.cool.biz.monotor.controller;

import com.cool.biz.monotor.service.RedisService;
import com.cool.core.base.api.R;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *<p>
 * redis数据
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/20
 */
@RestController
@AllArgsConstructor
@RequestMapping("/monitor/redis")
public class RedisController {

    private final RedisService redisService;

    @GetMapping()
    public R getInfo() {
        return R.ok(redisService.getInfo());
    }
}
