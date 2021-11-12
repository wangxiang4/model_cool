package com.cool.biz.reptile.reptileupload.impl;

import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cool.biz.reptile.mapper.MouldFileMapper;
import com.cool.biz.reptile.entity.MouldFile;
import com.cool.biz.reptile.reptileupload.SpiderUploadService;
import com.cool.core.base.common.cooluuid.UUIDTool;
import com.cool.core.base.util.FileUtil;
import com.cool.core.base.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
* @Param:
* @return:
* @Author: 菜鸟小王子
* @Date: 2020/9/30 11:49
* @description: 蜘蛛专属上传-类-不公用
*/
@Slf4j
@Service
public class SpiderUploadServiceImpl extends ServiceImpl<MouldFileMapper,MouldFile> implements SpiderUploadService {

    @Autowired
    private  MouldFileMapper mouldFileMapper;

    @Override
    public  String SpiderUploader(String originUrl,String inFilePath){
        //抓取originUrl文件名称
        String originFileName = ReUtil.get("[^/]+(?!.*/)",originUrl,0).split("\\?")[0];
        URLConnection urlConnection = null;
        try {
            // 连接类的父类，抽象类---------------------------------------------------URL请求连接配置
            urlConnection = new URL(originUrl).openConnection();
            //设置证书信任管理器不验证
            TrustManager[] trustManagers = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    }
                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    }
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }
            };
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null,trustManagers,null);
            // http的连接类
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) urlConnection;
            //设置跳过SSL验证
            httpsURLConnection.setSSLSocketFactory(ctx.getSocketFactory());
            //设置超时1分钟
            httpsURLConnection.setConnectTimeout(1000*60);
            //设置请求方式，默认是GET
            httpsURLConnection.setRequestMethod("GET");
            // 设置字符编码
            httpsURLConnection.setRequestProperty("Charset", "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        //---------------------------------------------------------------------
        MouldFile mouldFile=new MouldFile();
        mouldFile.setFileId(UUIDTool.uuid());
        mouldFile.setLinkFileId(UUIDTool.uuid());
        mouldFile.setFileName(originFileName);
        mouldFile.setUploadDate(new Date());
        if(originFileName.lastIndexOf(".")!=-1){
           mouldFile.setFileSufixx(originFileName.substring(originFileName.lastIndexOf(".")));
        }else{
           mouldFile.setFileSufixx(".stl");
        }
        String path=FileUtil.PathJoin(inFilePath,String.format("%s%s",TimeUtil.getRandomFileName(),mouldFile.getFileSufixx()));
        mouldFile.setFilePath(path);
        mouldFile.setFileBackPath(path);
        FileUtil.downloadFile(urlConnection,path);
        mouldFileMapper.insert(mouldFile);
        return mouldFile.getLinkFileId();
    }


}
