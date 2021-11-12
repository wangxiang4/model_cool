package com.cool.biz.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cool.biz.system.entity.OperLog;
import com.cool.biz.system.mapper.OperLogMapper;
import com.cool.biz.system.service.OperLogService;
import org.springframework.stereotype.Service;


/**
 *<p>
 * 操作日志记录 服务实现类
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/21
 */
@Service
public class OperLogServiceImpl extends ServiceImpl<OperLogMapper, OperLog> implements OperLogService {

}
