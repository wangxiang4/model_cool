package com.cool.biz.reptile.startup;

import com.cool.biz.reptile.core.CoreCodePageProcessor;
import com.cool.biz.reptile.core.MouldBomPageProcessor;
import com.cool.biz.reptile.core.MouldProductItemPageProcessor;
import com.cool.biz.reptile.enumerate.reptile;
import com.cool.biz.reptile.entity.MouldBomStandard;
import com.cool.biz.reptile.pipeline.MouldBomPipeline;
import com.cool.core.base.config.GlobalConfig;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 菜鸟小王子
 * @create 2020-09-28
 * 采用可拔插式蜘蛛编写(尽量降低代码耦合-减少复杂判断囊肿代码出现-提高可读性)
 */
@Component
public class SpiderStartUp implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args){
        this.Main();
    }
    
    //采用断点续爬-防止数据过多崩溃(还原复杂)-爬一区域保存一区域
    public void Main(){
       if(Boolean.valueOf(GlobalConfig.getConfig("cool.spider"))){
            //爬取所有--------------------------------------
            /*List<MouldBomStandard> mouldBomStandards=this.crawlingBomStandard();
            List<String> pages=this.crawlLevel(mouldBomStandards);
            this.crawlingMouldItem(pages);*/

            //爬取单标准-------------------------------------
            Set<String> pages=this.crawlLevel("https://fastencloud.industbox.com/zh/list/GB/?bomId=4066ad4eba8444d194380867b34483b9");
            this.crawlingMouldItem(pages);
        }
    }

    //提供区域爬取标准
    public Set<String> crawlLevel(String url){
        CoreCodePageProcessor coreCodePageProcessor=new CoreCodePageProcessor();
        Spider.create(coreCodePageProcessor)
                .addUrl(url)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(reptile.spiderConfig.urlFilter.count)))//设置布隆过滤定时队列中重复的url(最大10万条)
                .thread(reptile.spiderConfig.threadCount.count)
                .run();
        return coreCodePageProcessor.getTargetPages();
    }


    /**
    * @Param: []
    * @return: java.util.List<com.cool.biz.reptile.model.MouldBomStandard>
    * @Author: 菜鸟小王子
    * @description: 爬取模具Bom标准数据
    */
    private List<MouldBomStandard> crawlingBomStandard(){
        MouldBomPipeline mouldBomPipeline=new MouldBomPipeline();
        Spider.create(new MouldBomPageProcessor())
        .addUrl(reptile.basisUrl.fastenCloudChineseUrl.url)
        .setScheduler(
            new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(reptile.spiderConfig.urlFilter.count))//设置布隆过滤定时队列中重复的url(最大10万条)
        )
        .addPipeline(mouldBomPipeline)
        .thread(reptile.spiderConfig.threadCount.count)
        .run();
        return mouldBomPipeline.getTargetBomList();
    }


    /**
    * @Param: [mouldBomStandards]
    * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    * @Author: 菜鸟小王子
    * @description: 爬取[所有菜单层级一共四级]
     * 重点:这里返回所有{模拟内核点击}后的page页面内附带二级分类Id
    */
    public Set<String> crawlLevel(List<MouldBomStandard> mouldBomStandards){
        //基本操作==拼接
        List<String> bomUrl=mouldBomStandards.stream()
                .map(item-> String.format("%s%s%s",reptile.basisUrl.fastenCloudUrl.url,item.getMouldReptileHistory().getReptileUrl(),"?bomId="+item.getBomId()))
                .collect(Collectors.toList());
        String[] sourceUrls = new String[bomUrl.size()];
        CoreCodePageProcessor coreCodePageProcessor=new CoreCodePageProcessor();
        Spider.create(coreCodePageProcessor)
        .addUrl(bomUrl.toArray(sourceUrls))
        .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(reptile.spiderConfig.urlFilter.count)))//设置布隆过滤定时队列中重复的url(最大10万条)
        .thread(reptile.spiderConfig.threadCount.count)
        .run();
        return coreCodePageProcessor.getTargetPages();
    }


    /** 
    * @Param: [list]
    * @return: void 
    * @Author: 菜鸟小王子
    * @description: 爬取核心模具数据
    */ 
    public void crawlingMouldItem(Set<String> list){
        String[] sourceUrls = new String[list.size()];
        Spider.create(new MouldProductItemPageProcessor())
        .addUrl(list.toArray(sourceUrls))
        .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(reptile.spiderConfig.urlFilter.count)))//设置布隆过滤定时队列中重复的url(最大10万条)
        .thread(reptile.spiderConfig.threadCount.count)
        .run();
    }



}
