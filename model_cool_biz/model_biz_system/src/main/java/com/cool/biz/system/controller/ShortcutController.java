package com.cool.biz.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cool.biz.system.entity.Shortcut;
import com.cool.biz.system.service.ShortcutService;
import com.cool.core.base.api.R;
import com.cool.core.base.util.DateUtil;
import com.cool.core.log.annotation.OperLog;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


/** 
 *<p>
 * 快捷方式Controller
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/17
 */ 
@RestController
@AllArgsConstructor
@RequestMapping("/system/shortcut")
public class ShortcutController {

    private final ShortcutService shortcutService;

    private QueryWrapper<Shortcut> getQueryWrapper(Shortcut shortcut) {
        return new QueryWrapper<Shortcut>().like(StrUtil.isNotBlank(shortcut.getName()), "name", shortcut.getName())
                .between(StrUtil.isNotBlank(shortcut.getBeginTime()) && StrUtil.isNotBlank(shortcut.getEndTime()), "create_time",
                        DateUtil.dateToStr(shortcut.getBeginTime()),
                        DateUtil.dateToStr(shortcut.getEndTime())).orderByAsc("sort");
    }

    @PreAuthorize("@ps.hasPerm('shortcut_view')")
    @GetMapping("/list")
    public R list(Page page, Shortcut shortcut) {
        IPage<Shortcut> shortcutPage = shortcutService.page(page, getQueryWrapper(shortcut));
        return R.ok(shortcutPage.getRecords(), shortcutPage.getTotal());
    }

    @PreAuthorize("@ps.hasPerm('shortcut_view')")
    @GetMapping("/shortcutList")
    public R shortcutList(Shortcut shortcut) {
        List<Shortcut> shortcutList = shortcutService.list(getQueryWrapper(shortcut));
        return R.ok(shortcutList);
    }

    @GetMapping("/{id}")
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(shortcutService.getById(id));
    }

    @OperLog("快捷方式新增")
    @PreAuthorize("@ps.hasPerm('shortcut_add')")
    @PostMapping("/save")
    public R save(@RequestBody Shortcut shortcut) {
        shortcutService.saveOrUpdate(shortcut);
        return R.ok();
    }

    @OperLog("快捷方式修改")
    @PreAuthorize("@ps.hasPerm('shortcut_edit')")
    @PutMapping("/update")
    public R update(@RequestBody Shortcut shortcut) {
        shortcutService.updateById(shortcut);
        return R.ok();
    }

    @OperLog("快捷方式删除")
    @PreAuthorize("@ps.hasPerm('shortcut_del')")
    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable("id") Integer[] id) {
        return R.ok(shortcutService.removeByIds(Arrays.asList(id)));
    }


}
