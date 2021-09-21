package com.ftalk.gridchat.hazelcast;

import com.ftalk.gridchat.dto.Chat;
import com.ftalk.gridchat.service.GUIService;
import com.hazelcast.collection.ItemEvent;
import com.hazelcast.collection.ItemListener;
import org.springframework.stereotype.Component;

@Component
public class ChatsListener implements
        ItemListener<Chat> {

    private final GUIService guiService;

    public ChatsListener(GUIService guiService) {
        this.guiService = guiService;
    }

    @Override
    public void itemAdded(ItemEvent<Chat> itemEvent) {
        guiService.updateChatList(itemEvent.getItem().getName());
    }

    @Override
    public void itemRemoved(ItemEvent<Chat> itemEvent) {

    }
}

