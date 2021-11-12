package com.cool.biz.reptile.core;

import com.cool.biz.reptile.browser.PhantomJs.PhantomJsDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

public class TestPageProcessor implements PageProcessor{

    private Site site = Site.me()
            .setCharset("utf-8") //设置编码格式-跟爬取网站一致(不然会出现乱码)
            .setTimeOut(1000*60) //设置连接超时时间1分钟
            .setCycleRetryTimes(1000) //设置循环重试次数(防崩)
            .setSleepTime(1000*3); //低调一点不要这么放肆,100毫秒请求一次吧
    @Override
    public Site getSite() {
        return site;
    }


    @Override
    public void process(Page page) {
        WebDriver driver = PhantomJsDriver.getInstance().getPhantomJsSelenium();
        driver.get(page.getUrl().get());
        List<WebElement> webElement=driver.findElements(By.xpath("/html/body/div[@id='__nuxt']/div[@id='__layout']/div[@class='static-html']/div[@class='static-html-inner']/div[@class='page-container']/div/div[@class='page-list']/div[@class='mid-content']/div[@class='filter-bl']/div[@class='select-panel'][3]/div[@class='panel-content']/ul[@class='cate-list cate-type-list']/a"));
    }

    public static void main(String[] args) {
    }

}
