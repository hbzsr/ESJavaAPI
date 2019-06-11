package com.zsr.es;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

import java.net.InetAddress;

public class TestESUPDATE {
    /**
     * 测试更新 update API 使用 updateRequest 对象
     */
    @Test
    public void testUpdate() throws Exception {
        Settings settings = Settings.builder()
                .put("cluster.name", "my-es-cluster").build();
        Client client = TransportClient.builder()
                .settings(settings)
                .build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("bdnode1"), 9300));
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("blog");
        updateRequest.type("article");
        updateRequest.id("1");
        updateRequest.doc(XContentFactory.jsonBuilder()
                .startObject()
                // 对没有的字段添加, 对已有的字段替换
                .field("title", "ElasticSearch是一个基于Lucene的搜索服务器")
                .field("content",
                        "它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。Elasticsearch是用Java开发的，并作为Apache许可条款下的开放源码发布，是当前流行的企业级搜索引擎。设计用于云计算中，能够达到实时搜索，稳定，可靠，快速，安装使用方便。")
                .field("createDate", "2018-10-11")
                .endObject());
        UpdateResponse response = client.update(updateRequest).get();

        // 打印
        String index = response.getIndex();
        String type = response.getType();
        String id = response.getId();
        long version = response.getVersion();
        System.out.println(index + " : " + type + ": " + id + ": " + version);
        // 关闭连接
        client.close();
    }

    /**
     * 测试更新 update API 使用 updateRequest 对象
     */
    @Test
    public void testUpdate2() throws Exception {
        Settings settings = Settings.builder()
                .put("cluster.name", "my-es-cluster").build();
        Client client = TransportClient.builder()
                .settings(settings)
                .build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("bdnode1"), 9300));
        // 使用updateRequest对象及documents进行更新
        UpdateResponse response = client
                .update(new UpdateRequest("blog", "article", "1").doc(XContentFactory.jsonBuilder().startObject()
                        .field("title", "什么是Elasticsearch，ElasticSearch是一个基于Lucene的搜索服务器").endObject()))
                .get();
        // 打印
        String index = response.getIndex();
        String type = response.getType();
        String id = response.getId();
        long version = response.getVersion();
        System.out.println(index + " : " + type + ": " + id + ": " + version);
        // 关闭连接
        client.close();
    }

    /**
     * 测试upsert方法  ----->这个方法没有测试成功，下来再看
     */
    @Test
    public void testUpsert() throws Exception {
        Settings settings = Settings.builder()
                .put("cluster.name", "my-es-cluster").build();
        Client client = TransportClient.builder()
                .settings(settings)
                .build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("bdnode1"), 9300));
        // 设置查询条件, 查找不到则添加
        IndexRequest indexRequest = new IndexRequest("blog", "article", "1")
                .source(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("title", "基于Lucene的搜索服务器 map")
                        .field("content",
                                "它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口")
                        .endObject());
        // 设置更新, 查找到更新下面的设置
        UpdateRequest upsert = new UpdateRequest("blog", "article", "1")
                .doc(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("user", "李四")
                        .endObject())
                .upsert(indexRequest);

        client.update(upsert).get();
        client.close();
    }
}
