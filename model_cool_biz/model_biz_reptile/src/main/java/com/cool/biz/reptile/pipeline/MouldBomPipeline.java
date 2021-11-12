package com.cool.biz.reptile.pipeline;

import com.cool.biz.reptile.enumerate.reptile;
import com.cool.biz.reptile.entity.MouldBomStandard;
import com.cool.biz.reptile.service.impl.reptileServiceImpl;
import com.cool.biz.reptile.service.reptileService;
import com.cool.core.base.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.ArrayList;
import java.util.List;

/**
* @Param:
* @return:
* @Author: 菜鸟小王子
* @Date: 2020/9/28 18:52
* @description: 管道后续页面数据存储处理
*/
@Slf4j
//实现页面四大组件之一(管道处理组件)-等前面三个组件执行完才初始化
public class MouldBomPipeline implements Pipeline {

    //SpringIOC容器中获取对象
    private reptileService reptileService=SpringUtils.getBean(reptileServiceImpl.class);
    //声明后续处理List
    private List<MouldBomStandard> targetBomList = new ArrayList();

    @Override
    public void process(ResultItems resultItems, Task task) {
        targetBomList = resultItems.get(reptile.dataStorageKey.bomKey.key);
        targetBomList.forEach(item->{
            //处理数据库异常无限重试--怕数据库这边有问题插不进报错(那我这次数据就白爬了)
            while (true){
                try {
                    reptileService.addMouldBomStandard(item);
                    log.info(String.format("模型标准数据保存成功：id = %s, name = %s", item.getBomId(), item.getBomName()));
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info(String.format("数据存储异常信息位置：id = %s, name = %s", item.getBomId(), item.getBomName()));
                    continue;
                }
                break;
            }
        });
    }

    public List getTargetBomList(){
        return targetBomList;
    }

}
