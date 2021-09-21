package com.ftalk.gridchat.hazelcast;

import com.ftalk.gridchat.service.GUIService;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryAddedListener;
import org.springframework.stereotype.Component;

@Component
public class ChatsListener implements
        EntryAddedListener<String, String>{

    private final GUIService guiService;

    public ChatsListener(GUIService guiService) {
        this.guiService = guiService;
    }

    @Override
    public void entryAdded(EntryEvent<String, String> event) {
        guiService.updateChatList(event);
    }

}

