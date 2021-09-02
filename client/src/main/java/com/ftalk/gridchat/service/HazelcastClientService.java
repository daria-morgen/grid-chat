package com.ftalk.gridchat.service;

import com.ftalk.gridchat.hazelcast.EntryListener;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.stereotype.Service;

@Service
public class HazelcastClientService {

    private final HazelcastInstance hzclient;

    private final MessageService messageService;

    private final GUIService guiService;

    public HazelcastClientService(HazelcastInstance hzclient, MessageService messageService, GUIService guiService) {
        this.hzclient = hzclient;
        this.messageService = messageService;
        this.guiService = guiService;
        IMap<Object, Object> messages = hzclient.getMap("messages");
        messages.addEntryListener(new EntryListener(this.guiService), true);

    }
}
