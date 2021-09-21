package com.ftalk.gridchat.hazelcast;

import com.ftalk.gridchat.service.GUIService;
import com.hazelcast.collection.ItemEvent;
import com.hazelcast.collection.ItemListener;
import org.springframework.stereotype.Component;

@Component
public class ChatsListener implements
        ItemListener<String> {

    private final GUIService guiService;

    public ChatsListener(GUIService guiService) {
        this.guiService = guiService;
    }

    @Override
    public void itemAdded(ItemEvent<String> itemEvent) {
        guiService.updateChatList(itemEvent.getItem());

    }

    @Override
    public void itemRemoved(ItemEvent<String> itemEvent) {

    }
}

