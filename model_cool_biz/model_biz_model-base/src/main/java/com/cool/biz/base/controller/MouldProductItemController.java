package com.cool.biz.base.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cool.biz.base.entity.MouldProductItem;
import com.cool.biz.base.service.MouldProductItemService;
import com.cool.core.base.api.R;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 菜王
 * @create 2020-11-22
 */
@RestController
@AllArgsConstructor
@RequestMapping("/model/productItem")
public class MouldProductItemController {

    private final MouldProductItemService mouldProductItemService;

    @GetMapping("/list")
    public R list(Page page,String id,String type) {
        IPage<MouldProductItem> userIPage = mouldProductItemService.page(page, Wrappers.<MouldProductItem>query().lambda().eq(MouldProductItem::getAttributeId,id).orderByAsc(MouldProductItem::getProductWeights));
        return R.ok(userIPage.getRecords(),userIPage.getTotal());
    }

}
