package com.ftalk.gridchat.hazelcast;

import com.ftalk.gridchat.service.GUIService;
import com.hazelcast.cluster.MembershipEvent;
import com.hazelcast.cluster.MembershipListener;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryAddedListener;
import org.springframework.stereotype.Component;

@Component
public class EntryListener implements
        EntryAddedListener<String, String>{

    private final GUIService guiService;

    public EntryListener(GUIService guiService) {
        this.guiService = guiService;
    }

    @Override
    public void entryAdded(EntryEvent<String, String> event) {
        System.out.println("Entry Added:" + event.getValue());
        guiService.postMessage(event.getMember().getAddress().getHost() + ":" + event.getMember().getAddress().getPort() + ": " + event.getValue());
    }


}

