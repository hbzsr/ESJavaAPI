package com.zsr.es.bean;

import com.alibaba.fastjson.JSON;

public class Info {
    public static void main(String[] args) {
        Info info = new Info();
        info.setId("1");
        info.setTitle("基于Lucene的搜索服务器");
        info.setContent("它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口");
        String json = JSON.toJSONString(info);
        System.out.println(json);
    }


    /**
     * id : 1
     * title : 基于Lucene的搜索服务器
     * content : 它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口
     */

    private String id;
    private String title;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
