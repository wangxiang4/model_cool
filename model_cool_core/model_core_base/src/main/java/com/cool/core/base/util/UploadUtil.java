package com.cool.core.base.util;

import cn.hutool.core.util.StrUtil;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
/**
 * @author 菜鸟小王子
 * 文件上传工具类
 */
@NoArgsConstructor
public class UploadUtil {

    // 最大文件大小
    private long maxSize = 10 * 1024 * 1024;

    // 文件保存目录url
    private String saveUrl;

    private static String image = "gif,jpg,jpeg,png,bmp";

    private static String media = "swf,flv,mp3,mp4,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb";

    private static String file = "doc,docx,xls,xlsx,ppt,pptx,htm,html,txt,zip,rar,gz,bz2";


    /**
    * @Param: [fileFormat]
    * @return: java.lang.String
    * @Author: 菜鸟小王子
    * @Date: 2020/9/29 22:17
    * @description: 获取文件对应的区块类型
    */
    public static String getType(String fileFormat) {
        if(StrUtil.contains(image, fileFormat)){
            return "image";
        }else if(StrUtil.contains(media, fileFormat)){
            return "media";
        }else if(StrUtil.contains(file, fileFormat)){
            return "file";
        }else {
            return "other";
        }
    }


    /** 
    * @Param: [file, filePath, fileName] 文件对象,上传路径,文件名
    * @return: java.lang.String 文件名
    * @Author: 菜鸟小王子
    * @Date: 2020/9/29 22:21 
    * @description: 
    */ 
    public static String fileUp(MultipartFile file, String filePath, String fileName) {
        String extName = ""; // 扩展名格式：
        try {
            //获取当前上传文件的后缀
            if (file.getOriginalFilename().lastIndexOf(".") >= 0) {
                extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            }
            //当前文件没有后缀(置为默认)
            if (StrUtil.isBlank(extName)) {
                extName = ".png";
            }
            copyFile(file.getInputStream(), filePath, fileName + extName).replaceAll("-", "");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return fileName + extName;
    }


    /** 
    * @Param: [in, dir, realName]
    * @return: java.lang.String 
    * @Author: 菜鸟小王子
    * @Date: 2020/9/29 22:28 
    * @description: 写文件到当前目录的upload目录中
    */ 
    public static String copyFile(InputStream in, String dir, String realName) throws IOException {
        //根据根路径和文件名称生成一个文件实例
        File file = new File(dir,realName);
        //文件存在删除
        if (file.exists()) {
            FileUtil.del(dir + realName);
        }
        //目录不存在创建
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        //创建文件
        file.createNewFile();

        FileUtil.copyInputStreamToFile(in,file);
        return realName;
    }

}
