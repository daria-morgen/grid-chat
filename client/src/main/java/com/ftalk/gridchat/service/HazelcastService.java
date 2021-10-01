package com.ftalk.gridchat.service;

import com.ftalk.gridchat.dto.Chat;
import com.ftalk.gridchat.hazelcast.HZCollectionsUtils;
import com.ftalk.gridchat.hazelcast.RemoteChatsLoader;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.collection.IQueue;
import com.hazelcast.collection.ItemListener;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static com.ftalk.gridchat.dto.GridChatConstants.SET_CHAT_TYPE;

@Service
public class HazelcastService {

    @Value("${clusterName}")
    private String clusterName;

    private HazelcastInstance hzclient;

    private final List<HazelcastInstance> hzRemoteClient; //todo

    private final RestTemplateService restTemplate;

    private Map<String, Chat> allLocalClientChats = new HashMap<>();

    public HazelcastService(RemoteChatsLoader remoteChatsLoader, RestTemplateService restTemplate) {

        this.hzRemoteClient = remoteChatsLoader.getHazelcastInstances();
        this.restTemplate = restTemplate;
    }

    public Chat createNewChat(String newChat) {
        Chat chat = new Chat(newChat);
        hzclient.getMap(SET_CHAT_TYPE).put(newChat, chat);
        allLocalClientChats.put(newChat, chat);
        return chat;
    }

    public void sendMessage(String chatName, String text) {
        Chat chat = allLocalClientChats.get(chatName);
        if (chat.isRemote()) {
            hzRemoteClient.get(0).getQueue(chatName).add(LocalDate.now() + ":" + hzRemoteClient.get(0).getName() + ": " + text);
        } else {
            hzclient.getQueue(chatName).add(LocalDate.now() + ":" + hzclient.getName() + ": " + text);
        }
    }


    public Queue<String> getChatMessages(String chatName, ItemListener<String> listener) {
        Chat chat = allLocalClientChats.get(chatName);

        if (chat.isRemote()) {
            IQueue<String> queue = hzRemoteClient.get(0).getQueue(chatName);
            queue.addItemListener(listener, true);
            return queue;
        }

        IQueue<String> queue = hzclient.getQueue(chatName);
        queue.addItemListener(listener, true);
        return queue;
    }

    public IMap<String, Chat> getIChats(EntryListener<String, Chat> entryListener) {
        IMap<String, Chat> iChats = HZCollectionsUtils.getIChats(hzclient);
        iChats.addEntryListener(entryListener, true);
        return iChats;
    }

    public void updateLocalChatList(String chatName, Chat chat) {
        hzclient.getMap(SET_CHAT_TYPE).put(chatName, chat);
    }

    public List<IMap<String, Chat>> getRemoteIChats() {
        ArrayList<IMap<String, Chat>> remoteChats = new ArrayList<>();
        hzRemoteClient.forEach(e -> {
            remoteChats.add(e.getMap(SET_CHAT_TYPE));
        });
        return remoteChats;
    }

    public void registerClient(String userName) {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setInstanceName(userName);
        clientConfig.setClusterName(clusterName);

        hzclient = HazelcastClient.newHazelcastClient(clientConfig);

        allLocalClientChats.putAll(hzclient.getMap(SET_CHAT_TYPE));
        hzRemoteClient.forEach(e -> allLocalClientChats.putAll(e.getMap(SET_CHAT_TYPE)));
    }
}
