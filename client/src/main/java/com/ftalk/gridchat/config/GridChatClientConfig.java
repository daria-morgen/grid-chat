package com.ftalk.gridchat.config;

import com.ftalk.gridchat.balancer.RemoteChatBalancer;
import com.ftalk.gridchat.balancer.impl.RemoteChatBalancerImpl;
import com.ftalk.gridchat.dto.GridChatConstants;
import com.ftalk.gridchat.hazelcast.PublicIPLoader;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Configuration
public class GridChatClientConfig {

    private final PublicIPLoader publicIPLoader;

    public GridChatClientConfig(PublicIPLoader publicIPLoader) {
        this.publicIPLoader = publicIPLoader;
    }

    @Bean
    RemoteChatBalancer remoteChatBalancer() {
        return new RemoteChatBalancerImpl(publicIPLoader);
    }

    @Bean
    public HazelcastInstance hazelcastLocalChatsInstance() {
        //Следующий код запускает клиента Hazelcast,
        // который читает список доступных чатов в кластере с чатами.
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName(GridChatConstants.CLUSTER_CHAT_LIST);
        clientConfig.setInstanceName("client_chats_" + UUID.randomUUID().toString().substring(0, 5));
        return HazelcastClient.newHazelcastClient(clientConfig);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
