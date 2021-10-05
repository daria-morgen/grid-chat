package com.ftalk.gridchat.hazelcast;

import com.ftalk.gridchat.dto.Chat;
import com.hazelcast.collection.IQueue;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import java.util.Map;
import java.util.Queue;

import static com.ftalk.gridchat.dto.GridChatConstants.MAP_CHATS;


public class HZCollectionsUtils {

    public static Map<String, Chat> getChats(HazelcastInstance hzclient) {
        return hzclient.getMap(MAP_CHATS);
    }

    public static Queue<Object> getMessages(HazelcastInstance hzclient, String chatName) {
        return hzclient.getQueue(chatName);
    }

    public static IMap<String, Chat> getIChats(HazelcastInstance hzclient) {
        return hzclient.getMap(MAP_CHATS);
    }

    public static IQueue<Object> getIMessages(HazelcastInstance hzclient, String chatName) {
        return hzclient.getQueue(chatName);
    }

}
