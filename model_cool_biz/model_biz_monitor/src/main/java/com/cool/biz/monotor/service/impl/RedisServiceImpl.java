package com.cool.biz.monotor.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.cool.biz.monotor.service.RedisService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate redisTemplate;

    /**
     * 获取内存信息
     *
     * @return
     */
    @Override
    public Map<String, Object> getInfo() {
        //获取Redis信息
        Properties info = (Properties) redisTemplate.execute((RedisCallback) redisConnection -> redisConnection.info());
        //获取redis统计命令信息
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback) redisConnection -> redisConnection.info("commandstats"));
        //获取当前数据库的 key 的数量
        Object dbSize = redisTemplate.execute((RedisCallback) redisConnection -> redisConnection.dbSize());

        Map<String, Object> result = new HashMap<>(4);
        result.put("info", info);
        result.put("dbSize", dbSize);
        result.put("time", DateUtil.format(new Date(), DatePattern.NORM_TIME_PATTERN));

        //处理拆分(命令类型的统计信息,包括调用次数,这些命令消耗的总CPU时间以及每个命令执行消耗的平均CPU时间)
        List<Map<String, String>> pieList = new ArrayList<>();
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(4);
            String property = commandStats.getProperty(key);
            data.put("name", StrUtil.removePrefix(key, "cmdstat_"));
            data.put("value", StrUtil.subBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        result.put("commandStats", pieList);
        return result;
    }
}
