package com.ftalk.gridchat.balancer.impl;

import com.ftalk.gridchat.balancer.RemoteChatBalancer;
import com.ftalk.gridchat.dto.Chat;
import com.ftalk.gridchat.dto.Server;
import com.ftalk.gridchat.hazelcast.PublicIPLoader;
import org.apache.logging.log4j.util.Strings;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RemoteChatBalancerImpl implements RemoteChatBalancer {
    private final PublicIPLoader publicIPLoader;

    public RemoteChatBalancerImpl(PublicIPLoader publicIPLoader) {
        this.publicIPLoader = publicIPLoader;
    }

    @Override
    public Chat createNewRemoteChat(String chatName, boolean isPrivate, String creatorName) {
        List<Server> publicIPServers =
                publicIPLoader.getPublicIPServers();
        //todo логика определение загруженности удаленных серверов.
        return Chat.builder()
                .name(chatName)
                .isPrivate(isPrivate)
                .server(publicIPServers.get(0))
                .isTransfer(true)
                .creatorName(creatorName)
                .build();
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

    @Override
    public List<Server> getPublicIPServers() {
        return publicIPLoader.getPublicIPServers();
    }

    @Override
    public Chat createNewRemoteChat(String newChat, boolean isPrivate, String toUserName, String creatorName) {
        List<Server> publicIPServers =
                publicIPLoader.getPublicIPServers();
//        Chat chat = new Chat(newChat, true, true, Collections.singletonList(toUserName), this.userName);

        //todo  определение загруженности удаленных серверов.
        Chat chat = Chat.builder()
                .name(newChat)
                .isPrivate(isPrivate)
                .server(publicIPServers.get(0))
                .isTransfer(true)
                .creatorName(creatorName)
                .build();
        chat.setUserNames(Collections.singletonList(toUserName));
        return chat;

    }

}
