package com.ftalk.gridchat.service;

import com.ftalk.gridchat.dto.Chat;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.collection.IQueue;
import com.hazelcast.collection.ISet;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Getter
public class HazelcastClientService {

    private final HazelcastInstance hzclient;
    private final HazelcastInstance hzRemoteClient;

    private final RestTemplateService restTemplate;

    public HazelcastClientService(RestTemplateService restTemplate) {

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setInstanceName(UUID.randomUUID().toString().substring(0, 10));
        this.hzclient = HazelcastClient.newHazelcastClient(clientConfig);

        ClientConfig clientRemoteConfig = new ClientConfig();
        clientRemoteConfig.setInstanceName(UUID.randomUUID().toString().substring(0, 10));

        clientRemoteConfig.getNetworkConfig().addAddress("194.87.248.33:5701");
        clientRemoteConfig.setClusterName("remote_chat");
        this.hzRemoteClient = HazelcastClient.newHazelcastClient(clientRemoteConfig);

        this.restTemplate = restTemplate;
    }

    public IMap<Object, Object> getMap(String messages) {
        return getHzclient().getMap(messages);
    }

    public Integer getClientsCount() {
        return restTemplate.getClientsCount();
    }

    public ISet<Chat> createNewChat(String text) {
        hzRemoteClient.getSet(text);
        return hzclient.getSet(text);
    }

    public void sendMessage(String chatName, String text) {
        IQueue<Object> remoteClientQueue = hzRemoteClient.getQueue(chatName);
        remoteClientQueue.add(LocalDateTime.now() + "-" + hzclient.getName() + ": " + text);

        IQueue<Object> queue = hzclient.getQueue(chatName);
        queue.add(LocalDateTime.now() + "-" + hzclient.getName() + ": " + text);
    }

    public IQueue<String> getQueue(String elementAt) {
        hzRemoteClient.getQueue(elementAt);
        return hzclient.getQueue(elementAt);
    }

    public IQueue<String> getRemoteQueue(String elementAt) {
        return hzRemoteClient.getQueue(elementAt);
    }

    public ISet<Chat> getSet(String chats) {
        return hzclient.getSet(chats);
    }

    public ISet<Chat> getRemoteSet(String chats) {
        return hzRemoteClient.getSet(chats);
    }
}
