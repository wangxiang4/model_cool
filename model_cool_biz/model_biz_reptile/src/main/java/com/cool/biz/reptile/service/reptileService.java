package com.cool.biz.reptile.service;

import com.cool.biz.reptile.entity.*;

/**
 * @author 菜鸟小王子
 * @create 2020-09-28
 */
public interface reptileService{


    /**
    * @Param: [mouldBomStandard]
    * @return: void
    * @Author: 菜鸟小王子
    * @Date: 2020/9/28 13:57
    * @description: 添加模块Bom标准菜单
    */
    void addMouldBomStandard(MouldBomStandard mouldBomStandard);


    /**
     * @Param: [mouldFirstLevelMenu]
     * @return: void
     * @Author: 菜鸟小王子
     * @Date: 2020/9/28 13:57
     * @description: 添加模具一级分类菜单
     */
    void addMouldFirstLevelMenu(MouldFirstLevelMenu mouldFirstLevelMenu);


    /**
    * @Param: [mouldTwoLevelMenu]
    * @return: void
    * @Author: 菜鸟小王子
    * @Date: 2020/9/28 14:01
    * @description: 添加模具二级分类菜单
    */
    void addMouldTwoLevelMenu(MouldTwoLevelMenu mouldTwoLevelMenu);


    /**
     * @Param: [mouldFirstLevelMenu]
     * @return: void
     * @Author: 菜鸟小王子
     * @Date: 2020/9/28 13:57
     * @description: 添加模具三级分类属性菜单
     */
    void addMouldFourAttribute(MouldFourAttribute mouldFourAttribute);

    
    /** 
    * @Param: [mouldReptileHistory]
    * @return: void 
    * @Author: 菜鸟小王子
    * @Date: 2020/9/28 13:58 
    * @description: 存爬虫操作历史记录
    */ 
    void addMouldReptileHistory(ErrorReptileHistory errorReptileHistory);
    
    /** 
    * @Param: [mouldProductItem]
    * @return: void 
    * @Author: 菜鸟小王子
    * @Date: 2020/10/2 13:57 
    * @description: 产品元素数据存储
    */ 
    void addMouldProductItemMapper(MouldProductItem mouldProductItem);

}
