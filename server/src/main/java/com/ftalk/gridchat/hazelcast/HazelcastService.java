package com.ftalk.gridchat.hazelcast;


import com.ftalk.gridchat.dto.Request;
import com.ftalk.gridchat.dto.Response;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

@Service
public class HazelcastService {

    private final HazelcastInstance instance;

    private final Map<String, Map<Integer, String>> mapChats;

    private final Map<Integer, String> mapMessages;

    public HazelcastService(HazelcastInstance instance) {
        this.instance = instance;
        this.mapChats = instance.getMap("chats");
        this.mapMessages = instance.getMap("messages");
    }

    public Response sendMessage(Request message) {
        this.mapMessages.put(mapMessages.size() + 1, message.getMessage());
        return null;
    }

    public int getCountOfClients() {
        return instance.getClientService().getConnectedClients().size();
    }

    public Response createNewChat(String request) {
        mapChats.put(request, new HashMap<>());
        return null;
    }
}
