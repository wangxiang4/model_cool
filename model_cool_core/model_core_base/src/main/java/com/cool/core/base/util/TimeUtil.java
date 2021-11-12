package com.cool.core.base.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author 菜鸟小王子
 * @create 2020-09-30
 */
public class TimeUtil {


    /**
    * @Param: []
    * @return: java.lang.String
    * @Author: 菜鸟小王子
    * @Date: 2020/9/30 13:42
    * @description: 生成随机文件名：当前年月日时分秒+五位随机数
    */
    public static String getRandomFileName() {

        SimpleDateFormat simpleDateFormat;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");

        Date date = new Date();

        String str = sdf.format(date);

        Random random = new Random();

        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数

        return rannum + str;// 当前时间
    }


}
