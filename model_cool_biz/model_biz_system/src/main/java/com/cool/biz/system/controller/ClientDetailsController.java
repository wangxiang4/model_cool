package com.cool.biz.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cool.biz.system.entity.ClientDetails;
import com.cool.biz.system.service.ClientDetailsService;
import com.cool.core.base.api.R;
import com.cool.core.base.util.StrUtil;
import com.cool.core.log.annotation.OperLog;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


/**
 *<p>
 * 授权客户端Controller
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/20
 */
@RestController
@AllArgsConstructor
@RequestMapping("/system/clientDetails")
public class ClientDetailsController {

    private final ClientDetailsService clientDetailsService;
    private final PasswordEncoder passwordEncoder;

    private QueryWrapper<ClientDetails> getQueryWrapper(ClientDetails clientDetails) {
        return new QueryWrapper<ClientDetails>().like(StrUtil.isNotBlank(clientDetails.getClientId()), "client_id", clientDetails.getClientId());
    }

    @PreAuthorize("@ps.hasPerm('clientDetails_view')")
    @GetMapping("/list")
    @ResponseBody
    public R list(Page page, ClientDetails clientDetails) {
        IPage<ClientDetails> clientDetailsPage = clientDetailsService.page(page, getQueryWrapper(clientDetails));
        return R.ok(clientDetailsPage.getRecords(), clientDetailsPage.getTotal());
    }

    @GetMapping("/{clientId}")
    public R getById(@PathVariable("clientId") String clientId) {
        return R.ok(clientDetailsService.getById(clientId));
    }

    @OperLog("终端新增")
    @PreAuthorize("@ps.hasPerm('clientDetails_add')")
    @PostMapping("/save")
    @ResponseBody
    public R save(@RequestBody ClientDetails clientDetails) {
        clientDetails.setClientSecret(passwordEncoder.encode(clientDetails.getClientSecret()));
        clientDetailsService.save(clientDetails);
        return R.ok();
    }

    @OperLog("终端修改")
    @PreAuthorize("@ps.hasPerm('clientDetails_edit')")
    @PutMapping("/update")
    @ResponseBody
    public R update(@RequestBody ClientDetails clientDetails) {
        clientDetails.setClientSecret(passwordEncoder.encode(clientDetails.getClientSecret()));
        clientDetailsService.updateById(clientDetails);
        return R.ok();
    }

    @OperLog("终端删除")
    @PreAuthorize("@ps.hasPerm('clientDetails_del')")
    @DeleteMapping("/remove/{clientId}")
    @ResponseBody
    public R remove(@PathVariable("clientId") String clientId) {
        return R.ok(clientDetailsService.removeById(clientId));
    }
}
