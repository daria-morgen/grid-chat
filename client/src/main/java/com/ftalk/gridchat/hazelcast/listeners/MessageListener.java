package com.ftalk.gridchat.hazelcast.listeners;

import com.ftalk.gridchat.service.GUIService;
import com.hazelcast.collection.ItemEvent;
import com.hazelcast.collection.ItemListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener implements
        ItemListener<String> {

    private final GUIService guiService;

    public MessageListener(GUIService guiService) {
        this.guiService = guiService;
    }

    @Override
    public void itemAdded(ItemEvent<String> itemEvent) {
        guiService.postMessage(itemEvent.getItem());
    }

    @Override
    public void itemRemoved(ItemEvent<String> itemEvent) {

    }
}

