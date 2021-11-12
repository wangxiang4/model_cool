package com.cool.biz.reptile.Javajsengine;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/** 
* @Param:
* @return:  
* @Author: 菜鸟小王子
* @Date: 2020/10/9 21:10 
* @description:
 *  线程安全的JavaScriptEngine工厂类
 *  用于获得线程本地变量JavaScriptEngine
*/ 
public class JSEngineFactory {
    private volatile static JSEngineFactory jsEngineFactory;
    private ThreadLocal<ScriptEngine> scriptEngineThreadLocal;
    //JDK1.8新特性js执行引擎
    private final static String JS_ENGINE_NAME = "nashorn";
    //创建独立线程对象
    private JSEngineFactory() {
        scriptEngineThreadLocal = new ThreadLocal<>();
    }

    //对外提供单例对象
    public static JSEngineFactory getInstance() {
        if (jsEngineFactory == null) {
            synchronized (JSEngineFactory.class) {
                if (jsEngineFactory == null) {
                    jsEngineFactory = new JSEngineFactory();
                }
            }
        }
        return jsEngineFactory;
    }

    //对外提供js解析引擎
    public ScriptEngine getScriptEngine() {
        ScriptEngine scriptEngine;
        if ((scriptEngine = scriptEngineThreadLocal.get()) == null) {
            scriptEngine = new ScriptEngineManager().getEngineByName(JS_ENGINE_NAME);
            scriptEngineThreadLocal.set(scriptEngine);
        }
        return scriptEngine;
    }



}
