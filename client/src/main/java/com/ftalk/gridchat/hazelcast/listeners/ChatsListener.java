package com.ftalk.gridchat.hazelcast.listeners;

import com.ftalk.gridchat.dto.Chat;
import com.ftalk.gridchat.service.GUIService;
import com.hazelcast.collection.ItemEvent;
import com.hazelcast.collection.ItemListener;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.map.MapEvent;
import org.springframework.stereotype.Component;

@Component
public class ChatsListener implements
        EntryListener<String,Chat> {

    private final GUIService guiService;

    public ChatsListener(GUIService guiService) {
        this.guiService = guiService;
    }

    @Override
    public void entryAdded(EntryEvent<String, Chat> entryEvent) {
        guiService.updateChatList(entryEvent.getValue().getName());
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

