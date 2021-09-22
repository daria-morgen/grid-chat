package com.ftalk.gridchat.hazelcast;


import com.hazelcast.core.HazelcastInstance;
import org.springframework.stereotype.Service;

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
