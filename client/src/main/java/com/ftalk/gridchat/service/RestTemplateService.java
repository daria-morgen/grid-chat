package com.ftalk.gridchat.service;

import com.ftalk.gridchat.dto.Chat;
import com.ftalk.gridchat.dto.Request;
import com.ftalk.gridchat.dto.Response;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateService {
    @Value("${messageServiceHost}")
    private String host;

    private final RestTemplate restTemplate;

    public RestTemplateService(RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }

    public Integer getClientsCount() {
        return restTemplate.getForObject (
                host + "/countOfClients", Integer.class);
    }

    public void sendMessage(String message) {
        restTemplate.postForObject(
                host+"/sendMessage", new Request(message), Response.class);
    }

    public void createNewChat(Chat chat) {
        restTemplate.postForObject(
                host+"/createServerInstance", chat, Integer.class);
    }

    public IMap<String, Chat> getRemoteChatList(String url){
       return restTemplate.getForObject(
                host+"/hazelcast/rest/maps/mapName/chats", IMap.class);
    }
}
