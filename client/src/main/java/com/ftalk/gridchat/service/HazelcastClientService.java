package com.ftalk.gridchat.service;

import com.ftalk.gridchat.dto.Chat;
import com.hazelcast.collection.IQueue;
import com.hazelcast.collection.ISet;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Getter
public class HazelcastClientService {

    private final HazelcastInstance hzclient;

    private final RestTemplateService restTemplate;

    public HazelcastClientService(HazelcastInstance hzclient, RestTemplateService restTemplate) {
        this.hzclient = hzclient;
        this.restTemplate = restTemplate;
    }

    public IMap<Object, Object> getMap(String messages) {
        return getHzclient().getMap(messages);
    }

    public Integer getClientsCount() {
        return restTemplate.getClientsCount();
    }

    public ISet<Chat> createNewChat(String text) {
        return getHzclient().getSet(text);
    }

    public void sendMessage(String chatName, String text) {
        IQueue<Object> queue = getHzclient().getQueue(chatName);
        queue.add(LocalDateTime.now()+"-"+hzclient.getName() + ": " + text);
    }

    public IQueue<String> getQueue(String elementAt) {
        return getHzclient().getQueue(elementAt);

    }

    public ISet<Chat> getSet(String chats) {
        return getHzclient().getSet(chats);
    }
}
