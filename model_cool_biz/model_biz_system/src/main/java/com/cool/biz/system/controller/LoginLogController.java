package com.cool.biz.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cool.biz.system.entity.LoginLog;
import com.cool.biz.system.service.LoginLogService;
import com.cool.core.base.api.R;
import com.cool.core.base.util.DateUtil;
import com.cool.core.log.annotation.OperLog;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 *<p>
 * 系统登录记录
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/21
 */
@RestController
@AllArgsConstructor
@RequestMapping("/monitor/loginLog")
public class LoginLogController {

    private final LoginLogService loginLogService;

    private QueryWrapper<LoginLog> getQueryWrapper(LoginLog loginLog) {
        return new QueryWrapper<LoginLog>().like(StrUtil.isNotBlank(loginLog.getLoginName()), "login_name", loginLog.getLoginName()).like(StrUtil.isNotBlank(loginLog.getLoginIp()), "login_ip", loginLog.getLoginIp())
                .eq(StrUtil.isNotBlank(loginLog.getStatus()), "status", loginLog.getStatus()).between(StrUtil.isNotBlank(loginLog.getBeginTime()) && StrUtil.isNotBlank(loginLog.getEndTime()), "login_time",
                        DateUtil.dateToStr(loginLog.getBeginTime()),DateUtil.dateToStr(loginLog.getEndTime())).orderByDesc("id");
    }

    @PreAuthorize("@ps.hasPerm('loginLog_view')")
    @GetMapping("/list")
    public R list(Page page, LoginLog loginLog) {
        IPage<LoginLog> loginLogPage = loginLogService.page(page, getQueryWrapper(loginLog));
        return R.ok(loginLogPage.getRecords(), loginLogPage.getTotal());
    }

    @OperLog("登录日志删除")
    @PreAuthorize("@ps.hasPerm('loginLog_del')")
    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable Integer[] id) {
        try {
            loginLogService.removeByIds(Arrays.asList(id));
            return R.ok();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @OperLog("登录日志清空")
    @PreAuthorize("@ps.hasPerm('loginLog_del')")
    @DeleteMapping("/clean")
    public R clean() {
        loginLogService.remove(new QueryWrapper<>());
        return R.ok();
    }

}
