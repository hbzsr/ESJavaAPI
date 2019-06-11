package com.zsr.es;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TestESGET {
    //使用GetResponse查询
    @Test
    public void testGetData() throws Exception {
        Settings settings = Settings.builder()
                .put("cluster.name", "my-es-cluster").build();
        Client client = TransportClient.builder()
                .settings(settings)
                .build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("bdnode1"), 9300));
        GetResponse response = client.prepareGet("blog", "article", "1")
                .setOperationThreaded(false)    // 线程安全
                .get();
        System.out.println(JSON.parse(response.getSourceAsString()));
        //关闭
        client.close();
    }

    /**
     * 测试multi get
     * 搜索
     * 从不同的index, type, 和id中获取
     */
    @Test
    public void testMultiGet() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", "my-es-cluster").build();
        Client client = TransportClient.builder()
                .settings(settings)
                .build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("bdnode1"), 9300));
        MultiGetResponse multiGetResponse = client.prepareMultiGet()
                .add("blog", "article", "1")
                .add("blog", "article", "2", "3", "4")
                .get();

        for (MultiGetItemResponse itemResponse : multiGetResponse) {
            GetResponse response = itemResponse.getResponse();
            if (response.isExists()) {
                String sourceAsString = response.getSourceAsString();
                System.out.println(sourceAsString);
            }
        }
        client.close();
    }
}
