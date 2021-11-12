package com.cool.biz.reptile.browser.PhantomJs;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;

/**
 * @author 菜鸟小王子
 * @create 2020-10-10
 * 真实内核PhantomJs浏览器
 * 原生操作内部采用模块化导出对象API给你使用,不过要写js文件操作,算了懒得些
 * 直接用的Selenium开源Web自动化测试包(就是一个提供操作浏览器APi的JAR包)进行操作
 * todo 内核版本:V1.0.0
 */
public class PhantomJsDriver {

    //线程共享,保证线程在内存的可见性,但不能保证变量的原子性
    private volatile static PhantomJsDriver PhantomJsDriver;

    //创建PhantomJs浏览器操作DOM驱动线程集合
    private ThreadLocal<WebDriver> PhantomJsThreadLocal;

    //创建独立线程集合(内部只会获取当前线程的变量)独立内存
    public PhantomJsDriver() {
        PhantomJsThreadLocal=new ThreadLocal<>();
    }

    //对外提供单例对象
    public static PhantomJsDriver getInstance() {
        //减少进入同步次数,消耗性能
        if (PhantomJsDriver == null) {
            //开启线程同步
            synchronized (PhantomJsDriver.class) {
                //防止大批堵在同步队列线程重复创建对象
                if (PhantomJsDriver == null) {
                    PhantomJsDriver = new PhantomJsDriver();
                }
            }
        }
        return PhantomJsDriver;
    }


    //返回操作PhantomJs—DOM驱动
    public WebDriver getPhantomJsSelenium(){
        WebDriver webDriver;
        if((webDriver=PhantomJsThreadLocal.get())==null){
            System.setProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,"D:\\coolProfile\\phantomjs.exe");
            webDriver=new PhantomJSDriver();
        }
        return webDriver;
    }


    //返回操作Google-DOM驱动
    public WebDriver getGoogleSelenium(){
        WebDriver webDriver;
        if((webDriver=PhantomJsThreadLocal.get())==null){
            System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY,"D:\\coolProfile\\chromedriver.exe");
            webDriver=new ChromeDriver();
        }
        return webDriver;
    }


    public static void main(String[] args) {
    }

}
