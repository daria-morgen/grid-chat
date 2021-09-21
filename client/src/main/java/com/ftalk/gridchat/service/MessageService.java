package com.ftalk.gridchat.service;

import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final RestTemplateService restTemplate;

    public MessageService(RestTemplateService restTemplate) {
        this.restTemplate = restTemplate;
    }


}
