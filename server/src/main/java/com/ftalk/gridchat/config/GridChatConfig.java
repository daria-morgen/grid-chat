package com.ftalk.gridchat.config;

import com.ftalk.gridchat.dto.Chat;
import com.hazelcast.collection.ISet;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.ftalk.gridchat.dto.GridChatConstants.SET_CHAT_TYPE;

@Configuration
public class GridChatConfig {

    @Bean
    ISet<Chat> iSetChats(HazelcastInstance instance) {
        ISet<Chat> chats = instance.getSet(SET_CHAT_TYPE);

        //todo add loading remotes chatlist;
        return chats;
    }

    @Bean
    public HazelcastInstance hazelcastInstance() {
        //Следующий код запускает первого члена Hazelcast, создает и использует карту клиентов и очередь
        Config cfg = new Config();
//        cfg.setClusterName("remote_chat");
        return Hazelcast.newHazelcastInstance(cfg);
    }
}
