package com.ftalk.gridchat.hazelcast;

import com.ftalk.gridchat.service.GUIService;
import com.hazelcast.cluster.MembershipEvent;
import com.hazelcast.cluster.MembershipListener;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryAddedListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener implements
        EntryAddedListener<String, String>{

    private final GUIService guiService;

    public MessageListener(GUIService guiService) {
        this.guiService = guiService;
    }

    @Override
    public void entryAdded(EntryEvent<String, String> event) {
        guiService.postMessage(event.getMember().getAddress().getHost() + ":" + event.getMember().getAddress().getPort() + ": " + event.getValue());
    }
}

