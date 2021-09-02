package com.ftalk.gridchat.service;

import com.ftalk.gridchat.dto.Request;
import com.ftalk.gridchat.dto.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MessageService {

    @Value("${messageServiceHost}")
    private String host;

    private final RestTemplate restTemplate;

    public MessageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendMessage(String message) {
        restTemplate.postForObject(
                host+"/sendMessage", new Request(message), Response.class);
    }

    public int getMessagesSize(){
        return restTemplate.getForObject(
                "host/getMessagesSize", Integer.class);
    }


}
