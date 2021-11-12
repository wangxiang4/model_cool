package com.cool.core.base.util;


import cn.hutool.core.util.StrUtil;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
/** 
* @Param:
* @return:  
* @Author: 菜鸟小王子
* @Date: 2020/9/29 17:50 
* @description:
*/
/**
* @Author: 菜王
* @Date: 2020/11/4
* yml配置处理工具类
*/
public class YamlUtil {

    public static Map<?, ?> loadYaml(String fileName) throws FileNotFoundException {
        InputStream in = null;
        try {
            in = YamlUtil.class.getClassLoader().getResourceAsStream(fileName);
        } catch (Exception e) {
            throw new FileNotFoundException();
        }
        return StrUtil.isNotEmpty(fileName) ? (LinkedHashMap<?, ?>) new Yaml().load(in) : null;
    }


    //向YamL文件中写数据
    public static void dumpYaml(String fileName,Map<?, ?> map) throws IOException {
        if (StrUtil.isNotEmpty(fileName)) {
            FileWriter fileWriter = new FileWriter(YamlUtil.class.getResource(fileName).getFile());
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);
            yaml.dump(map, fileWriter);
        }
    }

    public static Object getProperty(Map<?, ?> map, Object qualifiedKey) {
        if (map != null && !map.isEmpty() && qualifiedKey != null) {
            String input = String.valueOf(qualifiedKey);
            if (!input.equals("")) {
                //判断你是不是properties配置文件|如果是递归拿到最右一个Key直接获取
                if (input.contains(".")) {
                    int index = input.indexOf(".");
                    String left = input.substring(0, index);
                    String right = input.substring(index + 1);
                    return getProperty((Map<?, ?>) map.get(left), right);
                } else if (map.containsKey(input)) {
                    return map.get(input);
                } else {
                    return null;
                }
            }
        }
        return null;
    }


    public static void setProperty(Map<?, ?> map, Object qualifiedKey, Object value) {
        if (map != null && !map.isEmpty() && qualifiedKey != null) {
            String input = String.valueOf(qualifiedKey);
            if (!input.equals("")) {
                //判断你是不是properties配置文件|如果是递归拿到最右一个Key直接存
                if (input.contains(".")) {
                    int index = input.indexOf(".");
                    String left = input.substring(0, index);
                    String right = input.substring(index + 1, input.length());
                    setProperty((Map<?, ?>) map.get(left), right, value);
                } else {
                    ((Map<Object, Object>) map).put(qualifiedKey, value);
                }
            }
        }
    }


}
