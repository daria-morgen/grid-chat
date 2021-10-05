package com.ftalk.gridchat.config;

import com.ftalk.gridchat.dto.GridChatConstants;
import com.ftalk.gridchat.dto.Server;
import com.ftalk.gridchat.hazelcast.RemoteChatsLoader;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

import static com.ftalk.gridchat.dto.GridChatConstants.CLUSTER_SUPER_CLUSTER;

@Configuration
public class GridChatClientConfig {
    private final RemoteChatsLoader remoteChatsLoader;

    public GridChatClientConfig(RemoteChatsLoader remoteChatsLoader) {
        this.remoteChatsLoader = remoteChatsLoader;
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
