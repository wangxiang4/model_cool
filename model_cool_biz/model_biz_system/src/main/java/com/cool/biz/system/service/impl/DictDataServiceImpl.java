package com.cool.biz.system.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cool.biz.system.entity.DictData;
import com.cool.biz.system.mapper.DictDataMapper;
import com.cool.biz.system.service.DictDataService;
import com.cool.core.base.config.GlobalConfig;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 *<p>
 * 字典数据表 服务实现类
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/18
 */
@Service
@AllArgsConstructor
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictData> implements DictDataService {

    private final String REDIS_DIR = "dictdata:";
    private final RedisTemplate redisTemplate;

    @Override
    public List<DictData> getDictDataList(String dictType) {
        List<DictData> dictDataList = new ArrayList<>();
        //redis缓存
        String cacheKey = REDIS_DIR + dictType;
        if (GlobalConfig.isRedisSwitch()) {
            Object dicts = redisTemplate.opsForValue().get(cacheKey);
            if (!StrUtil.isEmptyIfStr(dicts)) {
                dictDataList = JSONUtil.toList(JSONUtil.parseArray(dicts.toString()), DictData.class);
            } else {
                dictDataList = baseMapper.selectList(new QueryWrapper<DictData>().eq("dict_type", dictType).orderByAsc("sort"));
                redisTemplate.opsForValue().set(cacheKey, JSONUtil.toJsonStr(dictDataList));
            }
        } else {
            dictDataList = baseMapper.selectList(new QueryWrapper<DictData>().eq("dict_type", dictType).orderByAsc("sort"));
        }
        return dictDataList;
    }
}
