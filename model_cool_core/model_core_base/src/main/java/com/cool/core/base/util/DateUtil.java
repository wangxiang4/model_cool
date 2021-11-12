package com.cool.core.base.util;

import java.lang.management.ManagementFactory;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *<p>
 * DateUtil 扩展hutool
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/19
 */
public class DateUtil extends cn.hutool.core.date.DateUtil {

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    public static String formatDate(long dateTime, String pattern) {
        return format(new Date(dateTime), pattern);
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        return parse(str.toString());
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static Timestamp dateToStr(String str){
        if(StrUtil.isNotBlank(str)){
            if(str.trim().length()<=10){
                String toStr=DateUtil.parseDateToStr("yyyy-MM-dd hh:mm:ss",DateUtil.parseDate(str));
                return Timestamp.valueOf(toStr);
            }else{
                return Timestamp.valueOf(str);
            }
        }
       return null;
    }

}
