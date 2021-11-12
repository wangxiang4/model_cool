package com.cool.core.base.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.cool.core.base.config.GlobalConfig;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;

/**
 * @author 菜鸟小王子
 * 文件工具类(一些IO常用操作)
 */

public class FileUtil extends cn.hutool.core.io.FileUtil {

    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String fileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }


    //复制流数据--到文件中
    public static void copyInputStreamToFile(InputStream source, File destination) throws IOException {
        try {
            copyToFile(source, destination);
        } finally {
            IoUtil.close(source);
        }
    }

    //数据复制方法
    public static void copyToFile(InputStream source, File destination) throws IOException {
        //创建当前输出文件流,覆盖【不追加】
        FileOutputStream output = openOutputStream(destination);
        try {
            IoUtil.copy(source,output);
            output.close();
        } finally {
            IoUtil.close(output);
        }

    }

    public static FileOutputStream openOutputStream(File file) throws IOException {
        return openOutputStream(file, false);
    }


    public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
        //判断当前文件是否存在|不存在看看他是不是在上一级,如果都没有不好意思报错去吧
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }
            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && !parent.mkdirs() && !parent.isDirectory()) {
                throw new IOException("Directory '" + parent + "' could not be created");
            }
        }
        return new FileOutputStream(file, append);
    }




    /** 
    * @Param: [request, fileName] 请求对象,文件名
    * @return: java.lang.String 编码后的文件名
    * @Author: 菜鸟小王子
    * @Date: 2020/9/29 22:38 
    * @description: 下载文件名重新编码|防止前端中文乱码
    */ 
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName)throws UnsupportedEncodingException {

        //通过UA获取浏览器信息
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE")) {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        } else if (agent.contains("Firefox")) {
            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        } else if (agent.contains("Chrome")) {
            // google浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        } else {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }




    /** 
    * @Param: [urlConnection, inFilePath]
    * @return: java.lang.String
    * @Author: 菜鸟小王子
    * @Date: 2020/9/30 1:44 
    * @description: 下载文件时候指定目录传输(主备模式开启)
    */ 
    public  static  String  downloadFile(URLConnection urlConnection, String inFilePath){
        try {
            urlConnection.connect();
            int fileLength = urlConnection.getContentLength();
            BufferedInputStream bin = new BufferedInputStream(urlConnection.getInputStream());
            //上传地址
            String uploadPath = PathJoin(GlobalConfig.getUploadPath(),inFilePath);
            //备份地址
            String backPath = PathJoin(GlobalConfig.getUploadBackPath(),inFilePath);

            File uploadFile = new File(uploadPath);
            File backFile = new File(backPath);
            // 校验文件夹目录是否存在，不存在就创建一个目录
            if(!uploadFile.getParentFile().exists()) {
                uploadFile.getParentFile().mkdirs();
            }
            if(!backFile.getParentFile().exists()) {
                backFile.getParentFile().mkdirs();
            }
            OutputStream outUpload = new FileOutputStream(uploadFile);
            OutputStream outBack = new FileOutputStream(backFile);
            int size = 0;
            int len = 0;
            byte[] buf = new byte[2048];
            while ((size = bin.read(buf)) != -1) {
                len += size;
                outUpload.write(buf, 0, size);
                outBack.write(buf, 0, size);
                System.out.println("下载了-------> " + len * 100 / fileLength + "%\n");
            }
            bin.close();
            outUpload.close();
            outBack.close();
            System.out.println("文件下载成功！");
        } catch (Exception e) {
            System.err.println("文件下载失败！");
            e.printStackTrace();
        }finally {
            return inFilePath;
        }

    }


    /**
    * @Param: [args]
    * @return: java.lang.String
    * @Author: 菜鸟小王子
    * @Date: 2020/9/30 13:54
    * @description: 路径拼接方法
    */
    public static String PathJoin(String... args) {
        StringBuilder sb = new StringBuilder();
        String temp = null;
        for (int i = 0, max = args.length; i < max; i += 1) {
            if (!StrUtil.isNullOrUndefined(args[i])) {
                temp = args[i].replaceAll(("/".equals(File.separator) ? "\\\\" : "/"),
                        ("/".equals(File.separator) ? "/" : "\\\\"));
                // 处理拼接中间的路径分隔符
                if (sb.length() > 0) {
                    if (temp.startsWith(File.separator) && sb.lastIndexOf(File.separator) == (sb.length() - 1)) {
                        temp = temp.substring(1);
                    } else if (!temp.startsWith(File.separator)
                            && sb.lastIndexOf(File.separator) != (sb.length() - 1)) {
                        temp = File.separator + temp;
                    }
                }
                sb.append(temp);
                temp = null;
            }
        }
        return sb.toString();
    }



}
