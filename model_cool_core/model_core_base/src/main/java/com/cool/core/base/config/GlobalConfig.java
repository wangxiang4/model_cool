package com.cool.core.base.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.system.OsInfo;
import com.cool.core.base.util.FileUtil;
import com.cool.core.base.util.YamlUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
/**
* @Author: 菜王
* @Date: 2020/11/3
* 全局配置类
*/
@Slf4j
@NoArgsConstructor
public class GlobalConfig {

    private static OsInfo osInfo = new OsInfo();

    private static String NAME = "application.yml";

    //相对路径-数据库存储-匹配环境地址使用
    private static final String UPLOAD_PROFILE_DATABASE_PATH=String.format("%s%s","upload",File.separator);

    //头像子目录
    private static final String UPLOAD_AVATAR_PATH=String.format("%s%s","avatar",File.separator);

    //下载子目录
    private static final String UPLOAD_DOWNLOAD_PATH=String.format("%s%s","download",File.separator);

    /**
     * 当前对象实例
     */
    private static GlobalConfig globalConfig = null;

    /**
     * 保存全局属性值
     */
    private static Map<String, String> map = new HashMap();


    /**
     * 静态工厂方法 获取当前对象实例 多线程安全单例模式(使用双重同步锁)
     */
    public static synchronized GlobalConfig getInstance() {
        if (globalConfig == null) {
            synchronized (GlobalConfig.class) {
                if (globalConfig == null){
                    globalConfig = new GlobalConfig();
                }
            }
        }
        return globalConfig;
    }


    /**
     * 获取配置
     */
    public static String getConfig(String key) {
        String value = map.get(key);
        if (value == null) {
            Map<?, ?> yamlMap = null;
            try {
                yamlMap = YamlUtil.loadYaml(NAME);
                value = String.valueOf(YamlUtil.getProperty(yamlMap, key));
                map.put(key, value != null ? value : StrUtil.EMPTY);
            } catch (FileNotFoundException e) {
                log.error("获取全局配置异常 {}", key);
            }
        }
        return value;
    }


    /**
     * 获取项目名称
     */
    public static String getName() {
        return StrUtil.nullToDefault(getConfig("cool.name"), "模酷酷");
    }

    /**
     * 获取项目版本
     */
    public static String getVersion() {
        return StrUtil.nullToDefault(getConfig("cool.version"), "1.1.0");
    }


    /**
     * 获取文件上传根路径
     */
    public static String getProfile() {
        //判断系统环境给出对应的上传路径
        if(osInfo.isLinux()){
            return StrUtil.nullToDefault(getConfig("cool.linux-profile"),
                    FileUtil.getWebRoot().toString().split(String.format("%s%s%s",File.separator,"model_cool",File.separator))[0]+File.separator);
        }else{
            return StrUtil.nullToDefault(getConfig("cool.win-profile"),
                    FileUtil.getWebRoot().toString().split(String.format("%s%s%s%s%s",File.separator,File.separator,"model_cool",File.separator,File.separator))[0]+File.separator);
        }
    }


    /**
     * 获取系统语言
     */
    public static String getLang() {
        return StrUtil.nullToDefault(getConfig("cool.lang"), "zh_CN");
    }

    /**
     * 获取redis开关
     */
    public static Boolean isRedisSwitch() {
        return Boolean.valueOf(getConfig("cool.redisSwitch"));
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath() {
        //判断系统环境给出对应的上传路径
        if(osInfo.isLinux()){
            return  String.format("%s%s",
                    StrUtil.nullToDefault(getConfig("cool.linux-profile"),
                    FileUtil.getWebRoot().toString().split(String.format("%s%s%s%s%s",File.separator,File.separator,"model_cool",File.separator,File.separator))[0]+File.separator),
                    UPLOAD_AVATAR_PATH);
        }else{
            return String.format("%s%s",
                    StrUtil.nullToDefault(getConfig("cool.win-profile"),
                    FileUtil.getWebRoot().toString().split(String.format("%s%s%s%s%s",File.separator,File.separator,"model_cool",File.separator,File.separator))[0]+File.separator),
                    UPLOAD_AVATAR_PATH);
        }
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        //判断系统环境给出对应的上传路径
        if(osInfo.isLinux()){
            return String.format("%s%s",
                    StrUtil.nullToDefault(getConfig("cool.linux-profile"),
                    FileUtil.getWebRoot().toString().split(String.format("%s%s%s%s%s",File.separator,File.separator,"model_cool",File.separator,File.separator))[0]+File.separator),
                    UPLOAD_DOWNLOAD_PATH);
        }else{
            return String.format("%s%s",
                    StrUtil.nullToDefault(getConfig("cool.win-profile"),
                    FileUtil.getWebRoot().toString().split(String.format("%s%s%s%s%s",File.separator,File.separator,"model_cool",File.separator,File.separator))[0]+File.separator),
                    UPLOAD_DOWNLOAD_PATH);
        }
    }

    /** 
    * @Param: []
    * @return: java.lang.String 
    * @Author: 菜鸟小王子
    * @Date: 2020/9/30 1:04 
    * @description: 返回上传地址自己没配置返回当前项目同级地址
    */ 
    public static String getUploadPath() {
        //判断系统环境给出对应的上传路径
        if(osInfo.isLinux()){
            return String.format("%s%s",
                    StrUtil.nullToDefault(getConfig("cool.linux-profile"),
                            FileUtil.getWebRoot().toString().split(String.format("%s%s%s%s%s",File.separator,File.separator,"model_cool",File.separator,File.separator))[0]+File.separator),
                            UPLOAD_PROFILE_DATABASE_PATH);
        }else{
            return String.format("%s%s",
                    StrUtil.nullToDefault(getConfig("cool.win-profile"),
                            FileUtil.getWebRoot().toString().split(String.format("%s%s%s%s%s",File.separator,File.separator,"model_cool",File.separator,File.separator))[0]+File.separator),
                            UPLOAD_PROFILE_DATABASE_PATH);
        }
    }

    /**Back
     * 获取文件备份路径
     */
    public static String getUploadBackPath() {
        //备份地址-绝对路径(根据系统环境来)
        return String.format("%s%s%s",osInfo.isLinux()? FileUtil.getUserHomePath():"c:",File.separator,UPLOAD_PROFILE_DATABASE_PATH);
    }


}
