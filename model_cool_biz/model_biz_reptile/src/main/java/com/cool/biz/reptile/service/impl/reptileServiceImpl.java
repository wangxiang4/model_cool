package com.cool.biz.reptile.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cool.biz.reptile.mapper.*;
import com.cool.biz.reptile.entity.*;
import com.cool.biz.reptile.service.reptileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 菜鸟小王子
 * @create 2020-09-28
 * 爬虫公共业务代码
 */
@Service
@AllArgsConstructor
public class reptileServiceImpl implements reptileService {

    private final MouldBomStandardMapper mouldBomStandardMapper;
    private final MouldFirstLevelMenuMapper mouldFirstLevelMenuMapper;
    private final MouldTwoLevelMenuMapper mouldTwoLevelMenuMapper;
    private final MouldFourAttributeMapper mouldFourAttributeMapper;
    private final MouldReptileHistoryMapper mouldReptileHistoryMapper;
    private final MouldProductItemMapper mouldProductItemMapper;

    @Override
    public void addMouldBomStandard(MouldBomStandard mouldBomStandard) {
        //先斩后奏--
        if(mouldBomStandardMapper.selectList(
                new QueryWrapper<MouldBomStandard>().eq("bom_code",mouldBomStandard.getBomCode())
        ).size()>0){
            Map<String,Object> param=new HashMap();
            param.put("bom_code",mouldBomStandard.getBomCode());
            mouldBomStandardMapper.deleteByMap(param);
        }
        mouldBomStandardMapper.insert(mouldBomStandard);
    }

    @Override
    public void addMouldFirstLevelMenu(MouldFirstLevelMenu mouldFirstLevelMenu) {
        //先斩后奏--
        if(mouldFirstLevelMenuMapper.selectList(
                new QueryWrapper<MouldFirstLevelMenu>().eq("menu_first_code",mouldFirstLevelMenu.getMenuFirstCode())
        ).size()>0){
            Map<String,Object> param=new HashMap();
            param.put("menu_first_code",mouldFirstLevelMenu.getMenuFirstCode());
            mouldFirstLevelMenuMapper.deleteByMap(param);
        }
        mouldFirstLevelMenuMapper.insert(mouldFirstLevelMenu);
    }

    @Override
    public void addMouldTwoLevelMenu(MouldTwoLevelMenu mouldTwoLevelMenu) {
        //先斩后奏--
        if(mouldTwoLevelMenuMapper.selectList(
                new QueryWrapper<MouldTwoLevelMenu>().eq("menu_two_code",mouldTwoLevelMenu.getMenuTwoCode())
        ).size()>0){
            Map<String,Object> param=new HashMap();
            param.put("menu_two_code",mouldTwoLevelMenu.getMenuTwoCode());
            mouldTwoLevelMenuMapper.deleteByMap(param);
        }
        mouldTwoLevelMenuMapper.insert(mouldTwoLevelMenu);
    }

    @Override
    public void addMouldFourAttribute(MouldFourAttribute mouldFourAttribute) {
        mouldFourAttributeMapper.insert(mouldFourAttribute);
    }

    @Override
    public void addMouldReptileHistory(ErrorReptileHistory errorReptileHistory) {
        mouldReptileHistoryMapper.insert(errorReptileHistory);
    }

    @Override
    public void addMouldProductItemMapper(MouldProductItem mouldProductItem) {
        mouldProductItemMapper.insert(mouldProductItem);
    }
}
