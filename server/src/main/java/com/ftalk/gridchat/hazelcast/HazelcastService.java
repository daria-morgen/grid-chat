package com.ftalk.gridchat.hazelcast;


import com.ftalk.gridchat.dto.Request;
import com.ftalk.gridchat.dto.Response;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Queue;

@Service
public class HazelcastService {
    private final HazelcastInstance instance;

    private final Map<Integer, String> mapMessages;
    private final Queue<String> queueMessages;

    public HazelcastService(HazelcastInstance instance) {
        this.instance = instance;
        mapMessages = instance.getMap("messages");
        queueMessages = instance.getQueue("messages");
    }

    public Response sendMessage(Request message) {
        this.queueMessages.offer(instance.getName() + ": " + message);
        this.mapMessages.put(mapMessages.size() + 1, message.getMessage());
        return null;
    }

    public int getMessagesSize() {
        return queueMessages.size();
    }

}
