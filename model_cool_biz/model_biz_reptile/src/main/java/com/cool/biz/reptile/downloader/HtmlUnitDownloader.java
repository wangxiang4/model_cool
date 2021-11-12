package com.cool.biz.reptile.downloader;

import com.cool.biz.reptile.browser.htmlunit.WebClientFactory;
import com.cool.biz.reptile.entity.ErrorReptileHistory;
import com.cool.biz.reptile.service.reptileService;
import com.cool.core.base.util.SpringUtils;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.AbstractDownloader;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.WMCollections;

import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
* @Param:
* @return:
* @Author: 菜鸟小王子
* @Date: 2020/9/28 18:21
* @description: 使用HtmlUnit动态加载页面，运行JavaScript
*/

/***
 * 下载页面处理核心逻辑:实现四大组件第一个:下载组件
 *
 * 注意这个里面可以做的事情就多了
 * 可以处理模拟浏览器点击js事件等等的一些JS操作
 *
 * 以及可以利用Page保存点击过后的Page页面使用putField()转存
 *
 * ---------------------------------------------------------------------------------
 *
 * 内核版本 V1.0.0(采用纯净的虚拟内核工具:HtmlUnit:不需要引入其他依赖文件:性能最好)
 *
 *
 */
@Slf4j
public class HtmlUnitDownloader extends AbstractDownloader implements Downloader {

    private static WebClientFactory webClientFactory = WebClientFactory.getInstance();
    private reptileService reptileService= SpringUtils.getBean(reptileService.class);

    @Override
    public Page download(Request request, Task task) {
        // 设置接收的响应码
        Site site = null;
        if (task != null) {
            site = task.getSite();
        }

        //多线程启动累计统计返回状态码
        Set<Integer> acceptStatCode;
        if (site != null) {
            acceptStatCode = site.getAcceptStatCode();
        } else {
            acceptStatCode = WMCollections.newHashSet(200);
        }

        int statusCode;
        int failure = 0;
        Document doc;
        WebClient webClient = null;
        // 通过url获取页面，成功则返回，失败则重试3次
        while (true) {
            try {
                webClient = webClientFactory.getWebClient();
                HtmlPage rootPage = webClient.getPage(request.getUrl());
                doc = Jsoup.parse(rootPage.asXml());
                statusCode = rootPage.getWebResponse().getStatusCode();
                if (statusAccept(acceptStatCode, statusCode)) {
                    log.info("downloading page success {}", request.getUrl());
                    Page page = new Page();
                    page.setRawText(doc.html());
                    page.setUrl(new PlainText(request.getUrl()));
                    page.setRequest(request);
                    page.setStatusCode(statusCode);
                    onSuccess(request);
                    return page;
                    //把url放到任务队列中
                    //page.addTargetRequest("");
                } else {
                    log.warn("get page {} error, status code {} ", request.getUrl(), statusCode);
                    return null;
                }
            } catch (Exception e) {
                onError(request);
                failure++;
                if (failure == 3) {
                    ErrorReptileHistory errorReptileHistory =new ErrorReptileHistory();
                    errorReptileHistory.setReptileUrl(request.getUrl());
                    errorReptileHistory.setHistoryTime(new Date());
                    errorReptileHistory.setException(true);
                    errorReptileHistory.setReptileContent(e.getMessage());
                    reptileService.addMouldReptileHistory(errorReptileHistory);
                    log.warn("get page {} error, 3 times, com.cool.core.base.exception {} ", request.getUrl(), e.getMessage());
                    return null;
                }
                //睡眠随机范围5
                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                } catch (InterruptedException e1) { }
                log.warn("download page {} error", request.getUrl(), e);
                continue;
            } finally {
                WebClientFactory.WebClientClose(webClient);
            }
        }
    }

    protected boolean statusAccept(Set<Integer> acceptStatCode, int statusCode) {
        return acceptStatCode.contains(statusCode);
    }

    @Override
    public void setThread(int arg0) {
        // TODO Auto-generated method stub

    }
}