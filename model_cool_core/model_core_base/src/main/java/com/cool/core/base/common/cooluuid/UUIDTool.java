package com.cool.core.base.common.cooluuid;

import java.util.UUID;

/**
 * @author 菜鸟小王子
 * @create 2020-09-28
 */
public class UUIDTool {

    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }

}
