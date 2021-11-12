package com.cool.biz.reptile.reptileupload;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cool.biz.reptile.entity.MouldFile;

/**
 * @author 菜鸟小王子
 * @create 2020-09-30
 */
public interface SpiderUploadService extends IService<MouldFile> {
    String SpiderUploader(String originUrl,String inFilePath);
}
