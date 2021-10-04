package com.ftalk.gridchat.hazelcast.listeners;

import com.ftalk.gridchat.dto.Chat;
import com.ftalk.gridchat.service.HazelcastService;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.map.MapEvent;
import org.springframework.stereotype.Component;

@Component
public class RemoteChatsListener implements
        EntryListener<String,Chat> {

    private final HazelcastService hazelcastService;

    public RemoteChatsListener(HazelcastService hazelcastService) {
        this.hazelcastService = hazelcastService;
    }


    @Override
    public void entryAdded(EntryEvent<String, Chat> entryEvent) {
//        hazelcastService.updateLocalClusterChatList(entryEvent.getKey(),entryEvent.getValue());
    }

    @Override
    public void entryEvicted(EntryEvent<String, Chat> entryEvent) {

    }

    @Override
    public void entryExpired(EntryEvent<String, Chat> entryEvent) {

    }

    @Override
    public void entryRemoved(EntryEvent<String, Chat> entryEvent) {

    }

    @Override
    public void entryUpdated(EntryEvent<String, Chat> entryEvent) {

    }

    @Override
    public void mapCleared(MapEvent mapEvent) {

    }

    @Override
    public void mapEvicted(MapEvent mapEvent) {

    }
}

