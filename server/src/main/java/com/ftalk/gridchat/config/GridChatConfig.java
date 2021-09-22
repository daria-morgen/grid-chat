package com.ftalk.gridchat.config;

import com.ftalk.gridchat.dto.Chat;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.ftalk.gridchat.dto.GridChatConstants.SET_CHAT_TYPE;

@Configuration
public class GridChatConfig {

    @Value("${clusterName}")
    private String clusterName;

    @Value("${isRemoteTestServer}")
    private boolean isRemoteTestServer;

    @Bean
    public HazelcastInstance hazelcastInstance() {
        //Следующий код запускает члена Hazelcast
        Config cfg = new Config();
        cfg.setClusterName(clusterName);
        return Hazelcast.newHazelcastInstance(cfg);
    }


    @Bean
    IMap<String, Chat> iMapTestChats(HazelcastInstance instance) {
        if (isRemoteTestServer) {
            IMap<String, Chat> chats = instance.getMap(SET_CHAT_TYPE);
            chats.put("remote_cool_chat", new Chat("remote_cool_chat", "", "", true));
            return chats;
        }
        return null;
    }
}
