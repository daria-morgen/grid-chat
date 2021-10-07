package com.ftalk.gridchat.config;

import com.ftalk.gridchat.dto.Chat;
import com.ftalk.gridchat.dto.GridChatConstants;
import com.ftalk.gridchat.dto.Server;
import com.ftalk.gridchat.hazelcast.PublicIPLoader;
import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.ftalk.gridchat.dto.GridChatConstants.MAP_CHATS;

@Configuration
public class GridChatConfig {

    @Value("${isSuperCluster}")
    private boolean isSuperCluster;

    @Value("${publicIP}")
    private String publicIP;

    @Bean
    public HazelcastInstance hazelcastInstance(PublicIPLoader publicIPLoader) {
        //Следующий код запускает члена Hazelcast, который хранит список доступных чатов
        Config cfg = new Config();

        if (isSuperCluster) {
            //супер кластер - сервер с публичным IP, хранит в себе список доступных чатов со всех доступных узлов,
            //Также хранит все чаты, которые проходят через него.
            cfg.getNetworkConfig().setPublicAddress(publicIP);

            NetworkConfig network = cfg.getNetworkConfig();
            network.getRestApiConfig().setEnabled(true);

            JoinConfig join = network.getJoin(); //todo добавить загрузчик public IP
            if (publicIPLoader.getPublicIPServers().size() > 0) {
                publicIPLoader.getPublicIPServers().forEach(e -> {
                            if (!e.getURL().equals(this.publicIP))
                                join.getTcpIpConfig().addMember(e.getURL());
                        }
                );
            }

            join.getTcpIpConfig().setRequiredMember(null).setEnabled(true);
            cfg.setClusterName(GridChatConstants.CLUSTER_SUPER_CLUSTER);
        } else {
            //локальный кластер, созданный по умолчанию хранит только список чатов
            cfg.setClusterName(GridChatConstants.CLUSTER_CHAT_LIST);
        }

        return Hazelcast.newHazelcastInstance(cfg);
    }

    @Bean
    IMap<String, Chat> iMapTestChats(@Qualifier("hazelcastInstance") HazelcastInstance instance) {
        //для удаленного тестового сервера создаем тестовый чат
        if (isSuperCluster) {
            IMap<String, Chat> chats = instance.getMap(MAP_CHATS);
            chats.put("remote_cool_chat",
                    new Chat("remote_cool_chat",
                            new Server("194.87.248.33", "5701"),
                            true,
                            "default")
            );
            return chats;
        }
        return null;
    }
}
