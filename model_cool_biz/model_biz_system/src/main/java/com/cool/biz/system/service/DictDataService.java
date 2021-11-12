package com.cool.biz.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cool.biz.system.entity.DictData;

import java.util.List;

/**
 *<p>
 * 字典数据表 服务类
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/18
 */
public interface DictDataService extends IService<DictData> {

    List<DictData> getDictDataList(String dictType);
}
