package com.ftalk.gridchat.config;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ftalk.gridchat.service.GUIService;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Configuration
public class GridChatClientConfig {

    @Bean
    public HazelcastInstance hazelcastInstance() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setInstanceName(UUID.randomUUID().toString().substring(0,10));
        return HazelcastClient.newHazelcastClient(clientConfig);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
