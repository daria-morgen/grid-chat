package com.ftalk.gridchat.controller;

import com.ftalk.gridchat.hazelcast.HazelcastService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerController {
    private final HazelcastService hazelcastService;

    public ServerController(HazelcastService hazelcastService) {
        this.hazelcastService = hazelcastService;
    }

    @GetMapping("/countOfClients")
    public int getCountOfClients() {
        return hazelcastService.getCountOfClients();
    }
}
