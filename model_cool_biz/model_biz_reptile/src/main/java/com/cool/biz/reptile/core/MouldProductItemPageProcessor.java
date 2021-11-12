package com.cool.biz.reptile.core;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.cool.biz.reptile.entity.*;
import com.cool.biz.reptile.enumerate.reptile;
import com.cool.biz.reptile.mapper.MouldFirstLevelMenuMapper;
import com.cool.biz.reptile.mapper.MouldFourAttributeMapper;
import com.cool.biz.reptile.mapper.MouldTwoLevelMenuMapper;
import com.cool.biz.reptile.reptileupload.SpiderUploadService;
import com.cool.biz.reptile.service.reptileService;
import com.cool.core.base.common.cooluuid.UUIDTool;
import com.cool.core.base.util.SpringUtils;
import com.cool.core.base.util.StrUtil;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 菜鸟小王子
 * @create 2020-10-02
 */
//模块产品分页爬取
//@RequiredArgsConstructor(staticName = "of")
public class MouldProductItemPageProcessor implements PageProcessor{

    private reptileService reptileService= SpringUtils.getBean(reptileService.class);
    private SpiderUploadService uploadService=SpringUtils.getBean(SpiderUploadService.class);

    private MouldFirstLevelMenuMapper mouldFirstLevelMenuMapper=SpringUtils.getBean(MouldFirstLevelMenuMapper.class);
    private MouldTwoLevelMenuMapper mouldTwoLevelMenuMapper=SpringUtils.getBean(MouldTwoLevelMenuMapper.class);
    private MouldFourAttributeMapper mouldFourAttributeMapper=SpringUtils.getBean(MouldFourAttributeMapper.class);

    //爬取-配置
    private Site site = Site.me()
            .setCharset("utf-8") //设置编码格式-跟爬取网站一致(不然会出现乱码)
            .setTimeOut(1000*60) //设置连接超时时间1分钟
            .setCycleRetryTimes(1000) //设置循环重试次数(防崩)
            .setSleepTime(1000*3); //低调一点不要这么放肆,100毫秒请求一次吧

    @Override
    public Site getSite() {
        return site;
    }


    @Override
    public void process(Page page) {

        //抓取页面数据
        if(page.getUrl().get().indexOf("?")!=-1){
            Map<String,String> params= HttpUtil.decodeParamMap(page.getUrl().get().split("\\?")[1],"utf-8");
            if(params.containsKey("attributeId")){
                try {
                    Html html=page.getHtml();
                    String attributeId=params.get("attributeId");
                    productSave(attributeId,html);
                    Element element=html.getDocument().body();
                    //分页爬取
                    if(element.getElementsByClass("page-item active").next().size()!=0){
                        Node node=element.getElementsByClass("page-item active").next().get(0);
                        Node link=node.childNode(0);
                        String bkUrl=link.attr("href");
                        String param=bkUrl.indexOf("?")!=-1?"&attributeId=":"?attributeId=";
                        page.addTargetRequest(String.format("%s%s%s%s",reptile.basisUrl.fastenCloudUrl.url,bkUrl,param,attributeId));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    StringBuffer errorBuffer=new StringBuffer();
                    errorBuffer.append(e+"\r\n");
                    StackTraceElement[] ete=e.getStackTrace();
                    for (int i = 0; i < ete.length; i++) {
                        errorBuffer.append(ete[i]+"\r\n");
                    }
                    ErrorReptileHistory errorReptileHistory =new ErrorReptileHistory();
                    errorReptileHistory.setReptileUrl("获取模具产品错误===>>"+page.getUrl().get());
                    errorReptileHistory.setHistoryTime(new Date());
                    errorReptileHistory.setException(true);
                    errorReptileHistory.setReptileContent(errorBuffer.toString());
                    reptileService.addMouldReptileHistory(errorReptileHistory);
                }
            }
        }

    }


    /** 
    * @Param: [attributeId, html] 属性菜单关联Id,当前Page页
    * @return: void 
    * @Author: 菜鸟小王子
    * @Date: 2020/10/2 15:57 
    * @description: 产品保存
    */ 
    private void productSave(String attributeId,Html html){
        //获取所有元素集合
        List<String> HomeItem=html.xpath("//*[@id=\"__layout\"]/div/div/div[2]/div/div/div/div[3]/div/div[2]/ul/li[@class!=\"flex-item-hide\"]").all();
        for (int i = 0; i <HomeItem.size();i++){
            Html htmlElement=Html.create(HomeItem.get(i));
            String idHtml=htmlElement.xpath("//li/a/@href").get().split("/")[3];
            String Id=idHtml.substring(0,idHtml.indexOf("."));
            String code=htmlElement.xpath("//li/a/div[2]/text()").get();
            String name=htmlElement.xpath("//li/a/div[3]/text()").get();
            MouldProductItem mouldProductItem=new MouldProductItem();
            mouldProductItem.setProductId(UUIDTool.uuid());
            mouldProductItem.setAttributeId(attributeId);
            mouldProductItem.setProductTitle(code);
            mouldProductItem.setProductName(name);
            //获取当前模型所有文件地址
            Map<String,Object> paramMap=new HashMap<>();
            paramMap.put("standardID",Id);
            paramMap.put("axisXId",0);
            paramMap.put("standardCode",code);
            paramMap.put("standardName",name);
            paramMap.put("lValue",0);
            paramMap.put("language_x","zh");
            String json= HttpUtil.get(reptile.ajaxUrl.modelFileUrl.url,paramMap);
            Map<String,Object> remap= (Map<String, Object>) JSONObject.parse(json);
            Map<String,Object> data= (Map<String, Object>) remap.get("data");
            mouldProductItem.setTitleImgId(uploadService.SpiderUploader( data.get("specImgUrl").toString(),"modelItem/tileImg"));
            mouldProductItem.setDrawingImgId(uploadService.SpiderUploader(data.get("bigPhyImgUrl").toString(),"modelItem/drawingImg"));
            mouldProductItem.setMouldDrawingImgId(uploadService.SpiderUploader(data.get("stlImgUrl").toString(),"modelItem/mouldDrawingImg"));
            //获取当前模型表格参数
            Map<String,Object> paramTableMap=new HashMap<>();
            paramTableMap.put("standardID",Id);
            paramTableMap.put("language_x","zh");
            String jsonTable=HttpUtil.get(reptile.ajaxUrl.modelTableUrl.url,paramTableMap);
            JSONObject jsonObj=JSONObject.parseObject(jsonTable);
            mouldProductItem.setMouldParamPackage(JSONObject.toJSONString(jsonObj.get("data")));
            mouldProductItem.setProductWeights(Long.valueOf(Integer.sum(i,1)));

            //叠层向上级查询
            if(StrUtil.isNotBlank(attributeId)){
                MouldFourAttribute mouldFourAttribute=mouldFourAttributeMapper.selectById(attributeId);
                if(StrUtil.isNotBlank(mouldFourAttribute.getMenuTwoId())){
                    mouldProductItem.setMenuTwoId(mouldFourAttribute.getMenuTwoId());
                    MouldTwoLevelMenu mouldTwoLevelMenu=mouldTwoLevelMenuMapper.selectById(mouldFourAttribute.getMenuTwoId());
                    if(StrUtil.isNotBlank(mouldTwoLevelMenu.getMenuFirstId())){
                        mouldProductItem.setMenuFirstId(mouldTwoLevelMenu.getMenuFirstId());
                        MouldFirstLevelMenu mouldFirstLevelMenu=mouldFirstLevelMenuMapper.selectById(mouldTwoLevelMenu.getMenuFirstId());
                        if(StrUtil.isNotBlank(mouldFirstLevelMenu.getBomId())){
                            mouldProductItem.setMenuFirstId(mouldFirstLevelMenu.getBomId());
                        }
                    }
                }
            }

            reptileService.addMouldProductItemMapper(mouldProductItem);
        }
    }


}
