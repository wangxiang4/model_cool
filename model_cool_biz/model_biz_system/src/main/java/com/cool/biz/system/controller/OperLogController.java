package com.cool.biz.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cool.biz.system.entity.OperLog;
import com.cool.biz.system.service.OperLogService;
import com.cool.core.base.api.R;
import com.cool.core.base.util.DateUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


/**
 *<p>
 * 系统操作记录
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/21
 */
@RestController
@AllArgsConstructor
@RequestMapping("/monitor/operLog")
public class OperLogController {

    private final OperLogService operLogService;

    private QueryWrapper<OperLog> getQueryWrapper(OperLog operLog) {
        return new QueryWrapper<OperLog>().like(StrUtil.isNotBlank(operLog.getOperName()), "oper_name", operLog.getOperName()).like(StrUtil.isNotBlank(operLog.getTitle()), "title", operLog.getTitle()).eq(!StrUtil.isEmptyIfStr(operLog.getStatus()), "status", operLog.getStatus())
                .between(StrUtil.isNotBlank(operLog.getBeginTime()) && StrUtil.isNotBlank(operLog.getEndTime()),
                        "oper_time", DateUtil.dateToStr(operLog.getBeginTime()),DateUtil.dateToStr(operLog.getEndTime())).orderByDesc("id");
    }

    @PreAuthorize("@ps.hasPerm('operLog_view')")
    @GetMapping("/list")
    public R list(Page page, OperLog operLog) {
        IPage<OperLog> operLogPage = operLogService.page(page, getQueryWrapper(operLog));
        return R.ok(operLogPage.getRecords(), operLogPage.getTotal());
    }

    @com.cool.core.log.annotation.OperLog("操作日志删除")
    @PreAuthorize("@ps.hasPerm('operLog_del')")
    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable String[] id) {
        try {
            operLogService.removeByIds(Arrays.asList(id));
            return R.ok();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @com.cool.core.log.annotation.OperLog("操作日志清空")
    @PreAuthorize("@ps.hasPerm('operLog_del')")
    @DeleteMapping("/clean")
    public R clean() {
        operLogService.remove(new QueryWrapper<>());
        return R.ok();
    }

}
