package com.ftalk.gridchat.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
@Getter
public class HazelcastClientService {

    private final HazelcastInstance hzclient;

    private final RestTemplateService restTemplate;

    public HazelcastClientService(HazelcastInstance hzclient, RestTemplateService restTemplate) {
        this.hzclient = hzclient;
        this.restTemplate = restTemplate;
    }

    public IMap<Object, Object> getMessageMap(String messages) {
        return getHzclient().getMap(messages);
    }

    public Integer getClientsCount() {
        return restTemplate.getClientsCount();
    }
}
