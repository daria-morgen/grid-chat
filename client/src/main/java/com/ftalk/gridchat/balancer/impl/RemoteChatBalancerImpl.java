package com.ftalk.gridchat.balancer.impl;

import com.ftalk.gridchat.balancer.RemoteChatBalancer;
import com.ftalk.gridchat.dto.Chat;
import com.ftalk.gridchat.dto.Server;
import com.ftalk.gridchat.hazelcast.PublicIPLoader;
import jdk.internal.joptsimple.internal.Strings;

import java.util.List;
import java.util.stream.Collectors;

public class RemoteChatBalancerImpl implements RemoteChatBalancer {
    private final PublicIPLoader publicIPLoader;

    public RemoteChatBalancerImpl(PublicIPLoader publicIPLoader) {
        this.publicIPLoader = publicIPLoader;
    }

    @Override
    public Chat createNewRemoteChat(String chatName) {
        List<Server> publicIPServers =
                publicIPLoader.getPublicIPServers();

        //todo придумать логику определения загруженности удаленных серверов.
        return new Chat(chatName, publicIPServers.get(0), true);
    }

    @Override
    public int getPublicIPSize() {
        return publicIPLoader.getPublicIPServers().size();
    }

    @Override
    public String getPublicIPServerURL() {
        return publicIPLoader.getPublicIPServers().get(0).getURL();
    }

    @Override
    public String getAnotherPublicIPServerURL(String currentURL) {
        List<Server> collect = publicIPLoader.getPublicIPServers().stream().filter(e -> !e.getURL().equals(currentURL)).collect(Collectors.toList());
        if (collect.size() > 0)
            return collect.get(0).getURL();

        return Strings.EMPTY;
    }

}
