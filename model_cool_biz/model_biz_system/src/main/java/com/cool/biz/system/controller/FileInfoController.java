package com.cool.biz.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cool.biz.system.entity.FileInfo;
import com.cool.biz.system.service.FileInfoService;
import com.cool.core.base.api.R;
import com.cool.core.base.config.GlobalConfig;
import com.cool.core.base.util.DateUtil;
import com.cool.core.base.util.FileUtil;
import com.cool.core.base.util.UploadUtil;
import com.cool.core.log.annotation.OperLog;
import com.cool.core.security.entity.SecurityUser;
import com.cool.core.security.util.SecurityUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 *<p>
 * 文件Controller
 *</p>
 *
 * @Author: 菜王
 * @Date: 2020/11/20
 */
@RestController
@AllArgsConstructor
@RequestMapping("/system/fileInfo")
public class FileInfoController {

    private final FileInfoService fileInfoService;

    private QueryWrapper<FileInfo> getQueryWrapper(FileInfo fileInfo) {
        return new QueryWrapper<FileInfo>().like(StrUtil.isNotBlank(fileInfo.getName()), "name", fileInfo.getName()).eq(StrUtil.isNotBlank(fileInfo.getType()), "type", fileInfo.getType())
                .between(StrUtil.isNotBlank(fileInfo.getBeginTime()) && StrUtil.isNotBlank(fileInfo.getEndTime()), "create_time",
                        DateUtil.dateToStr(fileInfo.getBeginTime()),
                        DateUtil.dateToStr(fileInfo.getEndTime()))
                .orderByDesc("create_time");
    }

    @PreAuthorize("@ps.hasPerm('fileInfo_view')")
    @GetMapping("/list")
    public R list(Page page, FileInfo fileInfo) {
        IPage<FileInfo> fileInfoPage = fileInfoService.page(page, getQueryWrapper(fileInfo));
        return R.ok(fileInfoPage.getRecords(), fileInfoPage.getTotal());
    }

    @OperLog("文件上传")
    @PreAuthorize("@ps.hasPerm('fileInfo_add')")
    @PostMapping("/upload")
    public R upload(MultipartFile file, HttpServletRequest request) {
        FileInfo fileInfo = new FileInfo();
        String oFileName = file.getOriginalFilename();
        // 获取文件的文件名
        String fileName = oFileName.substring(0, oFileName.lastIndexOf("."));
        // 获取文件的后缀名
        String fileFormat = oFileName.substring(oFileName.lastIndexOf(".") + 1);
        // 获取文件的后缀名
        String type = UploadUtil.getType(fileFormat);
        // 新文件名
        String newFileName = type + new Date().getTime();
        // 获取大小
        String fileSize = FileUtil.fileSize(file.getSize());
        String path = "/profile/upload/" + type + "/" + UploadUtil.fileUp(file,GlobalConfig.getUploadPath() + type + "/", newFileName);
        fileInfo.setName(fileName);
        fileInfo.setType(type);
        fileInfo.setPath(path);
        fileInfo.setFormat(fileFormat);
        fileInfo.setSize(fileSize);
        SecurityUser user = SecurityUtil.getUser();
        fileInfo.setCreateBy(user.getUsername());
        fileInfo.setCreateTime(new Date());
        fileInfoService.save(fileInfo);
        return R.ok("文件上传成功！");
    }

    @OperLog("文件删除")
    @PreAuthorize("@ps.hasPerm('fileInfo_del')")
    @DeleteMapping("/remove/{id}")
    public R remove(@PathVariable("id") Integer[] id) {
        return R.ok(fileInfoService.removeByIds(Arrays.asList(id)));
    }


}
