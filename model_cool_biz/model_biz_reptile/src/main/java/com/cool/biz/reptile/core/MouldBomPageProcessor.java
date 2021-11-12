package com.cool.biz.reptile.core;

import com.cool.biz.reptile.enumerate.reptile;
import com.cool.biz.reptile.entity.ErrorReptileHistory;
import com.cool.biz.reptile.entity.MouldBomStandard;
import com.cool.biz.reptile.service.reptileService;
import com.cool.core.base.common.cooluuid.UUIDTool;
import com.cool.core.base.util.SpringUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @Param:
* @return:
* @Author: 菜鸟小王子
* @Date: 2020/9/28 17:41
* @description: 爬取模具标准[独立页面处理器]
*/
//实现页面四大组件之一(页面处理组件)
public class MouldBomPageProcessor implements PageProcessor{

    private reptileService reptileService=SpringUtils.getBean(reptileService.class);

    //爬取-配置
    private Site site = Site.me()
            .setCharset("utf-8") //设置编码格式-跟爬取网站一致(不然会出现乱码)
            .setTimeOut(1000*60) //设置连接超时时间1分钟
            .setCycleRetryTimes(1000) //设置循环重试次数(防崩)
            .setSleepTime(1000*3); //低调一点不要这么放肆,100毫秒请求一次吧
    @Override
    public Site getSite() {
        //实现返回自己设置的配置
        return site;
    }


    //重写内置页面抓取处理方法
    @Override
    public void process(Page page) {

        try {
            //总体首页
            Html html=page.getHtml();
            //获取标准Bom区域|怕断开分片爬取
            List<String> bomHead=html.xpath("//div[@class=index-bottom]/div/div[1]/div[2]/ul/a").all();
            List<MouldBomStandard> mouldBomStandards=new ArrayList<>();
            //处理结果集
            for (int i = 0,j=bomHead.size();i<j;i++) {
                Html itemHtml=Html.create(bomHead.get(i));
                MouldBomStandard mouldBomStandard=new MouldBomStandard();
                mouldBomStandard.setBomId(UUIDTool.uuid());
                mouldBomStandard.setBomName(itemHtml.xpath("//a/li/div[@class='list-item-name']/text()").get());
                mouldBomStandard.setBomCode(itemHtml.xpath("//a/li/div[@class='list-item-code']/text()").get());
                mouldBomStandard.setBomWeights(Long.valueOf(Integer.sum(i,1)));
                ErrorReptileHistory errorReptileHistory =new ErrorReptileHistory();
                errorReptileHistory.setReptileUrl(itemHtml.xpath("//a/@href").get());
                mouldBomStandard.setMouldReptileHistory(errorReptileHistory);
                mouldBomStandards.add(mouldBomStandard);
            }
            //保存结果集丢到处理组件去操作
            page.putField(reptile.dataStorageKey.bomKey.key,mouldBomStandards);
        } catch (Exception e) {
            e.printStackTrace();
            StringBuffer errorBuffer=new StringBuffer();
            errorBuffer.append(e+"\r\n");
            StackTraceElement[] ete=e.getStackTrace();
            for (int i = 0; i < ete.length; i++) {
                errorBuffer.append(ete[i]+"\r\n");
            }
            ErrorReptileHistory errorReptileHistory =new ErrorReptileHistory();
            errorReptileHistory.setReptileUrl("BOM标准错误===>>"+page.getUrl().get());
            errorReptileHistory.setHistoryTime(new Date());
            errorReptileHistory.setException(true);
            errorReptileHistory.setReptileContent(errorBuffer.toString());
            reptileService.addMouldReptileHistory(errorReptileHistory);
        }


    }


}
