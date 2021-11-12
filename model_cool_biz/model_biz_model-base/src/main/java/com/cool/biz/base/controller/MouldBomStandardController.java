package com.cool.biz.base.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cool.biz.base.entity.MouldBomStandard;
import com.cool.biz.base.entity.MouldFirstLevelMenu;
import com.cool.biz.base.entity.MouldFourAttribute;
import com.cool.biz.base.entity.MouldTwoLevelMenu;
import com.cool.biz.base.service.MouldBomStandardService;
import com.cool.biz.base.service.MouldFirstLevelMenuService;
import com.cool.biz.base.service.MouldFourAttributeService;
import com.cool.biz.base.service.MouldTwoLevelMenuService;
import com.cool.core.base.api.R;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 菜王
 * @create 2020-11-22
 * 标准Bom模型controller
 */
@RestController
@AllArgsConstructor
@RequestMapping("/model/base")
public class MouldBomStandardController {

    private final MouldBomStandardService mouldBomStandardService;

    private final MouldFirstLevelMenuService mouldFirstLevelMenuService;

    private final MouldTwoLevelMenuService mouldTwoLevelMenuService;

    private final MouldFourAttributeService mouldFourAttributeService;

    @GetMapping
    public R listAttribute(){
        Map<String,Object> map=new HashMap();
        List<MouldBomStandard> list1=mouldBomStandardService.list().stream().map(item->{
            MouldBomStandard mouldBomStandard=new MouldBomStandard();
            mouldBomStandard.setBomId(item.getBomId());
            mouldBomStandard.setBomCode(item.getBomCode());
            mouldBomStandard.setBomName(item.getBomName()+item.getBomCode());
            mouldBomStandard.setBomWeights(item.getBomWeights());
            return mouldBomStandard;
        }).collect(Collectors.toList());

        List<MouldFirstLevelMenu> list2= ObjectUtil.isNotEmpty(list1.get(0))?
                mouldFirstLevelMenuService.list(Wrappers.<MouldFirstLevelMenu>query()
                .lambda().eq(MouldFirstLevelMenu::getBomId,list1.get(0).getBomId())):
                new ArrayList<>();

        List<MouldTwoLevelMenu> list3= ObjectUtil.isNotEmpty(list2.get(0))?
                mouldTwoLevelMenuService.list(Wrappers.<MouldTwoLevelMenu>query()
               .lambda().eq(MouldTwoLevelMenu::getMenuFirstId,list2.get(0).getMenuFirstId())):
                new ArrayList<>();

        List<MouldFourAttribute> list4=ObjectUtil.isNotEmpty(list3.get(0))?
                mouldFourAttributeService.findList(list3.get(0).getMenuTwoId()):
                new ArrayList<>();

        map.put("bomOptions",list1);
        map.put("classOptions",list2);
        map.put("typeOptions",list3);
        map.put("attributeOptions",list4);

        return R.ok(map);
    }


    @GetMapping("/class/list")
    public R listClass(String bomId){
        return R.ok(
        mouldFirstLevelMenuService
        .list(
             Wrappers.<MouldFirstLevelMenu>query()
            .lambda().eq(MouldFirstLevelMenu::getBomId,bomId)
        ));
    }

    @GetMapping("/type/list")
    public R listType(String typeId){
        return R.ok(
        mouldTwoLevelMenuService
        .list(
            Wrappers.<MouldTwoLevelMenu>query()
            .lambda().eq(MouldTwoLevelMenu::getMenuFirstId,typeId)
        ));
    }

    @GetMapping("/attribute/list")
    public R listAttribute(String attributeId){
        return R.ok(
        mouldFourAttributeService
        .findList(attributeId));
    }



}
