package com.cool.biz.reptile.enumerate;

/**
 * @author 菜鸟小王子
 * @create 2020-09-28
 */
public interface reptile {

    //基本Url
    enum basisUrl implements reptile{
        //官方网站
        fastenCloudUrl("https://fastencloud.industbox.com"),
        //中文首页
        fastenCloudChineseUrl("https://fastencloud.industbox.com/zh/");
        public String url;
        basisUrl(String url) {
            this.url = url;
        }
    }

    //数据存储Key
    enum dataStorageKey implements reptile{
        //标准bom数据存储标识
        bomKey("bom");
        public String key;
        dataStorageKey(String key) {
            this.key = key;
        }
    }

    //蜘蛛配置
    enum spiderConfig implements reptile{
        //布隆最大去重数量
        urlFilter(100000),
        //启动线程数
        threadCount(3);
        public Integer count;
        spiderConfig(Integer count) {
            this.count = count;
        }
    }

    //ajax配置
    enum ajaxUrl implements reptile{
        //模型文件Url
        modelFileUrl("https://api.cn.industbox.com/Standard/StandardImageM"),
        //模型表格Url
        modelTableUrl("https://api.cn.industbox.com/Standard/GetStandardParamTable");
        public String url;
        ajaxUrl(String url) {
            this.url = url;
        }
    }


}
