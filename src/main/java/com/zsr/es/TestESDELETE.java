package com.zsr.es;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TestESDELETE {
    /**
     * 测试 delete
     */
    @Test
    public void testDelete() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", "my-es-cluster").build();
        Client client = TransportClient.builder()
                .settings(settings)
                .build()
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("bdnode1"), 9300));
        DeleteResponse response = client.prepareDelete("blog", "article", "1")
                .get();
        String index = response.getIndex();
        String type = response.getType();
        String id = response.getId();
        long version = response.getVersion();
        System.out.println(index + " : " + type + ": " + id + ": " + version);
        client.close();
    }
}
