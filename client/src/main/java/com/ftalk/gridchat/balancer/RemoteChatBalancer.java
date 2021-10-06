package com.ftalk.gridchat.balancer;

import com.ftalk.gridchat.dto.Chat;
import com.hazelcast.core.HazelcastInstance;

public interface RemoteChatBalancer {

    Chat createNewRemoteChat(String chatName);

    int getPublicIPSize();

    String getPublicIPServerURL();

    String getAnotherPublicIPServerURL(String currentURL);
}
