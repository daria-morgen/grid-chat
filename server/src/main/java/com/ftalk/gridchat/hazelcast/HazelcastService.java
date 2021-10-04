package com.ftalk.gridchat.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.stereotype.Service;

@Service
public class HazelcastService {

    private final HazelcastInstance instanceOfChatList;

    private HazelcastInstance instanceOfChat;

    public HazelcastService(HazelcastInstance instanceOfChatList) {
        this.instanceOfChatList = instanceOfChatList;
    }

    public int getCountOfClients() {
        return instanceOfChat.getClientService().getConnectedClients().size();
    }

    public HazelcastInstance createInstance(String clusterName) {
        if (instanceOfChat != null)
            instanceOfChat.shutdown();

        Config cfg = new Config();
        cfg.setClusterName(clusterName);
        return this.instanceOfChat = Hazelcast.newHazelcastInstance(cfg);
    }
}
