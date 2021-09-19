package com.ftalk.gridchat.controller;

import com.ftalk.gridchat.dto.Request;
import com.ftalk.gridchat.dto.Response;
import com.ftalk.gridchat.hazelcast.HazelcastService;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    private final HazelcastService hazelcastService;

    public MessageController(HazelcastService hazelcastService) {
        this.hazelcastService = hazelcastService;
    }

    @PostMapping("/sendMessage")
    public Response newEmployee(@RequestBody Request request) {
        return hazelcastService.sendMessage(request);
    }

    @GetMapping("/getMessagesSize")
    public int getMessagesSize() {
        return hazelcastService.getMessagesSize();
    }

    @GetMapping("/countOfClients")
    public int getCountOfClients() {
        return hazelcastService.getCountOfClients();
    }
}
