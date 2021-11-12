package com.cool.biz.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cool.biz.system.entity.FileInfo;
import com.cool.biz.system.mapper.FileInfoMapper;
import com.cool.biz.system.service.FileInfoService;
import org.springframework.stereotype.Service;

/**
 *<p>
 * 文件Service业务层
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/16
 */
@Service
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo> implements FileInfoService {

}
