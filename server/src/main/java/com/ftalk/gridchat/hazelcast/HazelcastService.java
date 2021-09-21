package com.ftalk.gridchat.hazelcast;


import com.ftalk.gridchat.dto.Request;
import com.ftalk.gridchat.dto.Response;
import com.hazelcast.collection.ISet;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

@Service
public class HazelcastService {

    private final HazelcastInstance instance;

    public HazelcastService(HazelcastInstance instance) {
        this.instance = instance;
    }

    public int getCountOfClients() {
        return instance.getClientService().getConnectedClients().size();
    }

}
