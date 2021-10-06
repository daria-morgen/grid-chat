package com.ftalk.gridchat.balancer;

import com.ftalk.gridchat.dto.Chat;
import com.ftalk.gridchat.dto.Server;

import java.util.List;

public interface RemoteChatBalancer {

    Chat createNewRemoteChat(String chatName);

    int getPublicIPSize();

    String getPublicIPServerURL();

    String getAnotherPublicIPServerURL(String currentURL);

    List<Server> getPublicIPServers();
}
