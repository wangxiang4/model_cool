package com.cool.biz.reptile.core;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import com.cool.biz.reptile.browser.PhantomJs.PhantomJsDriver;
import com.cool.biz.reptile.enumerate.reptile;
import com.cool.biz.reptile.entity.ErrorReptileHistory;
import com.cool.biz.reptile.entity.MouldFirstLevelMenu;
import com.cool.biz.reptile.entity.MouldFourAttribute;
import com.cool.biz.reptile.entity.MouldTwoLevelMenu;
import com.cool.biz.reptile.reptileupload.SpiderUploadService;
import com.cool.biz.reptile.service.reptileService;
import com.cool.core.base.common.cooluuid.UUIDTool;
import com.cool.core.base.util.SpringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Param:
 * @return:
 * @Author: 菜鸟小王子
 * @Date: 2020-09-30
 * @description: 爬取模具标准[独立页面处理器]
 */
//实现页面四大组件之一(页面处理组件)
public class CoreCodePageProcessor implements PageProcessor {

    private SpiderUploadService uploadService= SpringUtils.getBean(SpiderUploadService.class);
    private reptileService reptileService=SpringUtils.getBean(reptileService.class);
    Set<String> pages=new HashSet<>();

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

            Html html = page.getHtml();
            if (page.getUrl().get().indexOf("?") != -1) {
                Map<String, String> params = HttpUtil.decodeParamMap(page.getUrl().get().split("\\?")[1], "utf-8");
                //一级分类处理区域
                if (params.containsKey("bomId")) {
                    try {
                        List<String> one_html = html.xpath("/html/body/div[@id='__nuxt']/div[@id='__layout']/div[@class='static-html']/div[@class='static-html-inner']/div[@class='page-container']/div/div[@class='page-list']/div[@class='mid-content']/div[@class='filter-bl']/div[@class='select-panel'][2]/div[@class='panel-content']/ul[@class='cate-list']/a").all();
                        if (one_html.size() == 0) {
                            page.addTargetRequest(page.getUrl().get() + "&cool=reptile");
                            return;
                        }
                        for (int i = 0, j = one_html.size(); i < j; i++) {
                            Html Item = Html.create(one_html.get(i));
                            //这网站中文有问题--有时候显示不了中文
                            if (Item.xpath("//a/@href").get().indexOf("zh") == -1) {
                                //重新加载
                                page.addTargetRequest(page.getUrl().get() + "&cool=reptile");
                                break;
                            }
                            MouldFirstLevelMenu mouldFirstLevelMenu = new MouldFirstLevelMenu();
                            mouldFirstLevelMenu.setMenuFirstId(UUIDTool.uuid());
                            mouldFirstLevelMenu.setMenuFirstCode(Item.xpath("//a/@href").get().split("/")[3]);
                            mouldFirstLevelMenu.setMenuFirstName(Item.xpath("//a/li/div[@class='list-item-name']/text()").get());
                            mouldFirstLevelMenu.setMenuFirstWeights(Long.valueOf(Integer.sum(i, 1)));
                            //标准Id关联
                            mouldFirstLevelMenu.setBomId(params.get("bomId"));
                            String fileUrl = Item.xpath("//a/li/div[@class='list-item-img']/img/@src").get();
                            mouldFirstLevelMenu.setMenuImgId(uploadService.SpiderUploader(fileUrl, "oneClass"));
                            reptileService.addMouldFirstLevelMenu(mouldFirstLevelMenu);
                            //抓取二级菜单请求放到队列中
                            page.addTargetRequest(reptile.basisUrl.fastenCloudUrl.url + Item.xpath("//a/@href").get() + "?FirstLevelId=" + mouldFirstLevelMenu.getMenuFirstId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        StringBuffer errorBuffer=new StringBuffer();
                        errorBuffer.append(e+"\r\n");
                        StackTraceElement[] ete=e.getStackTrace();
                        for (int i = 0; i < ete.length; i++) {
                            errorBuffer.append(ete[i]+"\r\n");
                        }
                        ErrorReptileHistory errorReptileHistory =new ErrorReptileHistory();
                        errorReptileHistory.setReptileUrl("一级分类错误===>>"+page.getUrl().get());
                        errorReptileHistory.setHistoryTime(new Date());
                        errorReptileHistory.setException(true);
                        errorReptileHistory.setReptileContent(errorBuffer.toString());
                        reptileService.addMouldReptileHistory(errorReptileHistory);
                    }
                }

                //二级分类处理区域
                if (params.containsKey("FirstLevelId")) {
                    try {
                        List<String> two_html = html.xpath("/html/body/div[@id='__nuxt']/div[@id='__layout']/div[@class='static-html']/div[@class='static-html-inner']/div[@class='page-container']/div/div[@class='page-list']/div[@class='mid-content']/div[@class='filter-bl']/div[@class='select-panel'][3]/div[@class='panel-content']/ul[@class='cate-list cate-type-list']/a").all();
                        if (two_html.size() == 0) {
                            page.addTargetRequest(page.getUrl().get() + "&cool=reptile");
                        }
                        for (int i = 0, j = two_html.size(); i < j; i++) {
                            Html Item = Html.create(two_html.get(i));
                            //这网站中文有问题--有时候显示不了中文
                            if (Item.xpath("//a/@href").get().indexOf("zh") == -1) {
                                //重新加载
                                page.addTargetRequest(page.getUrl().get() + "&cool=reptile");
                                break;
                            }
                            MouldTwoLevelMenu mouldTwoLevelMenu = new MouldTwoLevelMenu();
                            //一级分类Id关联
                            mouldTwoLevelMenu.setMenuFirstId(params.get("FirstLevelId"));
                            mouldTwoLevelMenu.setMenuTwoId(UUIDTool.uuid());
                            mouldTwoLevelMenu.setMenuTwoName(Item.xpath("//a/li/div[@class='list-item-name']/text()").get());
                            mouldTwoLevelMenu.setMenuTwoWeights(Long.valueOf(Integer.sum(i, 1)));
                            //爬的网站图片服务器崩了先不管图片吧 mouldTwoLevelMenu.setMenuImgId();
                            mouldTwoLevelMenu.setMenuTwoCode(Item.xpath("//a/@href").get().split("/")[3]);
                            reptileService.addMouldTwoLevelMenu(mouldTwoLevelMenu);
                            //抓取三级属性分类请求放到队列中
                            page.addTargetRequest(reptile.basisUrl.fastenCloudUrl.url + Item.xpath("//a/@href").get() + "?twoId=" + mouldTwoLevelMenu.getMenuTwoId());
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                        StringBuffer errorBuffer=new StringBuffer();
                        errorBuffer.append(e+"\r\n");
                        StackTraceElement[] ete=e.getStackTrace();
                        for (int i = 0; i < ete.length; i++) {
                            errorBuffer.append(ete[i]+"\r\n");
                        }
                        ErrorReptileHistory errorReptileHistory =new ErrorReptileHistory();
                        errorReptileHistory.setReptileUrl("二级分类错误===>>"+page.getUrl().get());
                        errorReptileHistory.setHistoryTime(new Date());
                        errorReptileHistory.setException(true);
                        errorReptileHistory.setReptileContent(errorBuffer.toString());
                        reptileService.addMouldReptileHistory(errorReptileHistory);
                    }
                }

                //三级分类属性处理区域
                if (params.containsKey("twoId")) {
                    WebDriver driver=null;
                    try {
                        //采用PhantomJs[JS-API浏览器]内用命令窗口浏览器
                        //WebDriver driver = PhantomJsDriver.getInstance().getPhantomJsSelenium();
                        driver = PhantomJsDriver.getInstance().getGoogleSelenium();
                        List<String> four_html = html.xpath("/html/body/div[@id='__nuxt']/div[@id='__layout']/div[@class='static-html']/div[@class='static-html-inner']/div[@class='page-container']/div/div[@class='page-list']/div[@class='mid-content']/div[@class='filter-bl']/div[@class='select-panel'][4]/div[@class='panel-content']/ul[@class='cate-list cate-property-list']/li").all();
                        for (int i=0,j=four_html.size(); i<j; i++) {
                            Html Item = Html.create(four_html.get(i));
                            MouldFourAttribute mouldFourAttribute = new MouldFourAttribute();
                            mouldFourAttribute.setAttributeId(UUIDTool.uuid());
                            mouldFourAttribute.setAttributeParentId("root");
                            mouldFourAttribute.setAttributeName(Item.xpath("//li/div[1]/text()").get());
                            mouldFourAttribute.setAttributeWeights(Long.valueOf(Integer.sum(i, 1)));
                            mouldFourAttribute.setMenuTwoId(params.get("twoId"));
                            reptileService.addMouldFourAttribute(mouldFourAttribute);
                            List<String> child_html = Item.xpath("//li/div[@class='property-sub-list']/a").all();
                            for (int c = 0, v = child_html.size(); c < v; c++) {

                                Html childItem = Html.create(child_html.get(c));
                                MouldFourAttribute mouldFourChildAttribute = new MouldFourAttribute();
                                mouldFourChildAttribute.setAttributeId(UUIDTool.uuid());
                                mouldFourChildAttribute.setAttributeParentId(mouldFourAttribute.getAttributeId());
                                String fileUrlChild = childItem.xpath("//a/div/div[1]/img/@src").get();
                                mouldFourChildAttribute.setAttributeImgId(uploadService.SpiderUploader(fileUrlChild, "/fourChildLevel"));
                                mouldFourChildAttribute.setAttributeName(childItem.xpath("//a/div/div[2]/text()").get());
                                mouldFourChildAttribute.setAttributeWeights(Long.valueOf(Integer.sum(c, 1)));
                                mouldFourChildAttribute.setMenuTwoId(params.get("twoId"));
                                reptileService.addMouldFourAttribute(mouldFourChildAttribute);

                                WebDriverWait webDriverWait = new WebDriverWait(driver, 60 * 10, 1000);
                                //操作真实浏览器有页面检测,点击后旧数据不能再次使用,重新拉取
                                driver.get(page.getUrl().get());
                                WebDriver finalDriver = driver;
                                List<WebElement> fourLevel = webDriverWait.until((ExpectedCondition<List<WebElement>>) webDriver -> finalDriver.findElements(By.xpath("/html/body/div[@id='__nuxt']/div[@id='__layout']/div[@class='static-html']/div[@class='static-html-inner']/div[@class='page-container']/div/div[@class='page-list']/div[@class='mid-content']/div[@class='filter-bl']/div[@class='select-panel'][4]/div[@class='panel-content']/ul[@class='cate-list cate-property-list']/li")));
                                //修复渲染乱跳错误|修复连接过长500错误
                                while (true){
                                    List<WebElement> activaElement=webDriverWait.until((ExpectedCondition<List<WebElement>>) webDriver ->finalDriver.findElements(By.className("active")));
                                    if(activaElement.stream().filter(item->
                                    ObjectUtil.isEmpty(item.findElement(By.xpath("./..")).getAttribute("href"))?
                                    false:
                                    page.getUrl().get().contains(item.findElement(By.xpath("./..")).getAttribute("href")))
                                    .collect(Collectors.toList()).size()>0){
                                        break;
                                    }
                                    driver.get(page.getUrl().get());
                                    fourLevel=webDriverWait.until((ExpectedCondition<List<WebElement>>) webDriver -> finalDriver.findElements(By.xpath("/html/body/div[@id='__nuxt']/div[@id='__layout']/div[@class='static-html']/div[@class='static-html-inner']/div[@class='page-container']/div/div[@class='page-list']/div[@class='mid-content']/div[@class='filter-bl']/div[@class='select-panel'][4]/div[@class='panel-content']/ul[@class='cate-list cate-property-list']/li")));
                                }
                                WebElement htmlElement = fourLevel.get(i);
                                List<WebElement> fourChildLevel = htmlElement.findElements(By.xpath(".//div[@class='property-sub-list']/a"));
                                WebElement htmlChild = fourChildLevel.get(c);
                                System.out.println(htmlChild.getAttribute("title"));
                                Actions action = new Actions(driver);
                                action.moveToElement(htmlElement).perform();
                                //修复元素展开渲染慢,元素展不开
                                while (true){
                                    if(htmlChild.isDisplayed()){
                                        try {
                                            htmlChild.click();
                                            break;
                                        } catch (ElementNotVisibleException e) {
                                            continue;
                                        }
                                    }
                                    action.moveToElement(htmlElement).perform();
                                }
                                System.out.println("===>>>" + driver.getCurrentUrl());
                                pages.add(String.format("%s%s", driver.getCurrentUrl(),"&attributeId="+mouldFourChildAttribute.getAttributeId()));
                            }
                        }
                        driver.quit();
                    } catch (Throwable e) {
                        if(!driver.toString().contains("(null)")){
                            driver.quit();
                        }
                        e.printStackTrace();
                        StringBuffer errorBuffer=new StringBuffer();
                        errorBuffer.append(e+"\r\n");
                        StackTraceElement[] ete=e.getStackTrace();
                        for (int i = 0; i < ete.length; i++) {
                            errorBuffer.append(ete[i]+"\r\n");
                        }
                        ErrorReptileHistory errorReptileHistory =new ErrorReptileHistory();
                        errorReptileHistory.setReptileUrl("属性错误||父元素=====>>>>"+page.getUrl().get()+"||子元素====>>>"+driver.getCurrentUrl());
                        errorReptileHistory.setHistoryTime(new Date());
                        errorReptileHistory.setException(true);
                        errorReptileHistory.setReptileContent(errorBuffer.toString());
                        reptileService.addMouldReptileHistory(errorReptileHistory);
                    }
                }
            }
        }

    public Set<String> getTargetPages(){
        return pages;
    }

}
