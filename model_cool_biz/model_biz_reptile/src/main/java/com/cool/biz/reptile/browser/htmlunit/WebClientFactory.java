package com.cool.biz.reptile.browser.htmlunit;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * 线程安全的WebClient工厂类
 * 单例模式和工厂模式
 * 只适用一些简单的js操作(不支持模块化加载js<js type=model>)
 */
public class WebClientFactory {

    private volatile static WebClientFactory webClientfactory;

    private WebClientFactory() {
    }

    public static WebClientFactory getInstance() {
        if (webClientfactory == null) {
            synchronized (WebClientFactory.class) {
                if (webClientfactory == null) {
                    webClientfactory = new WebClientFactory();
                }
            }
        }
        return webClientfactory;
    }

    public WebClient getWebClient() {
        WebClient webClient;
        webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setDoNotTrackEnabled(true);
        webClient.getOptions().setDownloadImages(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setTimeout(1000*60);//连接超时配置1分钟
        webClient.waitForBackgroundJavaScript(30000);//异步JS执行需要耗时,所以这里线程要阻塞30秒,等待异步JS执行结束
        return webClient;
    }
    
    /** 
    * @Param: [webClient]
    * @return: void 
    * @Author: 菜鸟小王子
    * @Date: 2020/10/1 20:11 
    * @description: 关闭客户端连接
    */ 
    public static void WebClientClose(WebClient webClient) {
        if (webClient.getCurrentWindow().getJobManager() != null) {
            webClient.getCurrentWindow().getJobManager().removeAllJobs();
            webClient.close();
        }
    }

}
