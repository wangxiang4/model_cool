package com.cool.biz.monotor.controller;

import com.cool.biz.monotor.server.Server;
import com.cool.core.base.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *<p>
 * 服务器监控
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/20
 */
@RestController
@RequestMapping("/monitor/server")
public class ServerController {

    @GetMapping
    public R getInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        return R.ok(server);
    }
}
