package com.ftalk.gridchat.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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

    public void createNewChat(String text) {
        getHzclient().getMap("chats").put(text, new HashMap<>());
    }

    public void sendMessage(String chatName, String text) {
        Map<String, Map<Integer, String>> chats = getHzclient().getMap("chats");
        Map<Integer, String> integerStringMap = chats.get(chatName);
        integerStringMap.put(integerStringMap.size() + 1,text);
    }
}
