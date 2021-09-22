package com.ftalk.gridchat.hazelcast.listeners;

import com.ftalk.gridchat.service.GUIService;
import com.hazelcast.client.Client;
import com.hazelcast.cluster.MembershipEvent;
import com.hazelcast.cluster.MembershipListener;

public class ClientListener implements MembershipListener {

    private final GUIService guiService;

    public ClientListener(GUIService guiService) {
        this.guiService = guiService;
    }

    @Override
    public void memberAdded(MembershipEvent membershipEvent) {
        guiService.updateCountOfClients(membershipEvent.getMembers().size());
    }

    @Override
    public void memberRemoved(MembershipEvent membershipEvent) {
        guiService.updateCountOfClients(membershipEvent.getMembers().size());
    }
}

