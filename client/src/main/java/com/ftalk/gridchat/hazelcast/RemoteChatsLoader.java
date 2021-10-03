package com.ftalk.gridchat.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RemoteChatsLoader {

    private List<HazelcastInstance> hazelcastInstances = new ArrayList<>();


    //todo added remote loader
    public List<HazelcastInstance> getHazelcastInstances() {
        ClientConfig clientRemoteConfig = new ClientConfig();
        clientRemoteConfig.setInstanceName(UUID.randomUUID().toString().substring(0, 10));

        clientRemoteConfig.getNetworkConfig().addAddress("194.87.248.33:5701");
        clientRemoteConfig.setClusterName("remote_cluster");
        this.hazelcastInstances.add(HazelcastClient.newHazelcastClient(clientRemoteConfig));
        return this.hazelcastInstances;
    }
}
