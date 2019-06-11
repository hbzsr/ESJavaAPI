package com.zsr.es;

import com.alibaba.fastjson.JSON;
import com.zsr.es.bean.Info;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class TestESapi {
    private Client client;

    @Before
    public void getClient() throws Exception {
        System.out.println("开始~~~~~~~");
        Settings settings = Settings.builder()
                .put("cluster.name", "my-es-cluster").build();
        client = TransportClient.builder()
                .settings(settings)
                .build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("bdnode1"), 9300));

    }

    //直接拼接json字符串
    @Test
    public void createIndexNoMapping() {
        Info info = new Info();
        info.setId("1");
        info.setTitle("基于Lucene的搜索服务器");
        info.setContent("它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口");
        String json = JSON.toJSONString(info);
        IndexResponse indexResponse = this.client.prepareIndex("blog", "article", "2")
                .setSource(json).execute().actionGet();
        // 结果获取
        String index = indexResponse.getIndex();
        String type = indexResponse.getType();
        String id = indexResponse.getId();
        long version = indexResponse.getVersion();
        boolean created = indexResponse.isCreated();
        System.out.println(index + " : " + type + ": " + id + ": " + version + ": " + created);
    }

    //使用map
    @Test
    public void createIndexNoMapping1() {
        Map<String, Object> json = new HashMap<>();
        json.put("id", "2");
        json.put("title", "基于Lucene的搜索服务器 map");
        json.put("content", "它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口");
        IndexResponse indexResponse = this.client
                .prepareIndex("blog", "article", "2")
                .setSource(json).execute().actionGet();
        // 结果获取
        String index = indexResponse.getIndex();
        String type = indexResponse.getType();
        String id = indexResponse.getId();
        long version = indexResponse.getVersion();
        boolean created = indexResponse.isCreated();
        System.out.println(index + " : " + type + ": " + id + ": "
                + version + ": " + created);
    }

    //使用es的帮助类
    @Test
    public void createIndexNoMapping2() throws Exception {
        // 使用es的帮助类，创建一个json方式的对象
        XContentBuilder sourceBuilder = XContentFactory.jsonBuilder()
                .startObject()
                .field("id", 3)
                .field("title", "ElasticSearch是一个基于Lucene的搜索服务器")
                .field("content",
                        "它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。Elasticsearch是用Java开发的，并作为Apache许可条款下的开放源码发布，是当前流行的企业级搜索引擎。设计用于云计算中，能够达到实时搜索，稳定，可靠，快速，安装使用方便。")
                .endObject();
        // 创建索引
        IndexResponse indexResponse = client.prepareIndex("blog", "article", "3").setSource(sourceBuilder).get();
        // 结果获取
        String index = indexResponse.getIndex();
        String type = indexResponse.getType();
        String id = indexResponse.getId();
        long version = indexResponse.getVersion();
        boolean created = indexResponse.isCreated();
        System.out.println(index + " : " + type + ": " + id + ": " + version + ": " + created);
    }


    @After
    public void close() {
        System.out.println("关闭");
        if (client != null) {
            client.close();
        }
    }
}
