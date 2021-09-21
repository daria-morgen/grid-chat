package com.ftalk.gridchat.config;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GridChatConfig {
    @Bean
    public HazelcastInstance hazelcastInstance() {
        //Следующий код запускает первого члена Hazelcast, создает и использует карту клиентов и очередь
        Config cfg = new Config();
        return Hazelcast.newHazelcastInstance(cfg);
    }
}
