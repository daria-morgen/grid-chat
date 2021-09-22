package com.ftalk.gridchat.service;

import com.ftalk.gridchat.dto.Chat;
import com.ftalk.gridchat.hazelcast.HZCollectionsUtils;
import com.ftalk.gridchat.hazelcast.RemoteChatsLoader;
import com.ftalk.gridchat.hazelcast.listeners.ClientListener;
import com.ftalk.gridchat.hazelcast.listeners.MessageListener;
import com.hazelcast.collection.IQueue;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static com.ftalk.gridchat.dto.GridChatConstants.SET_CHAT_TYPE;

@Service
public class HazelcastClientService {

    private final HazelcastInstance hzclient;

    private final List<HazelcastInstance> hzRemoteClient; //todo

    private final RestTemplateService restTemplate;

    private Map<String, Chat> allLocalClientChats = new HashMap<>();

    public HazelcastClientService(HazelcastInstance hzclient, RemoteChatsLoader remoteChatsLoader, RestTemplateService restTemplate) {
        this.hzclient = hzclient;

        this.hzRemoteClient = remoteChatsLoader.getHazelcastInstances();

        allLocalClientChats.putAll(hzclient.getMap(SET_CHAT_TYPE));
        hzRemoteClient.forEach(e -> allLocalClientChats.putAll(e.getMap(SET_CHAT_TYPE)));

        this.restTemplate = restTemplate;
    }

    public Integer getClientsCount() {
        return restTemplate.getClientsCount();
    }

    public Map<Object, Object> createNewChat(String newChat) {
        Chat chat = new Chat(newChat);
        hzclient.getMap(SET_CHAT_TYPE).put(newChat, chat);
        allLocalClientChats.put(newChat, chat);
        return hzclient.getMap(SET_CHAT_TYPE);
    }

    public void sendMessage(String chatName, String text) {
        Chat chat = allLocalClientChats.get(chatName);
        if (chat.isRemote()) {
            hzRemoteClient.get(0).getQueue(chatName).add(LocalDate.now() + ":" + LocalTime.now() + "-" + hzRemoteClient.get(0).getName() + ": " + text);
        } else {
            hzclient.getQueue(chatName).add(LocalDate.now() + ":" + LocalTime.now() + "-" + hzclient.getName() + ": " + text);
        }
    }

    public void addMembershipListeners(GUIService guiService) {
        hzclient.getCluster().addMembershipListener(new ClientListener(guiService));
        hzRemoteClient.forEach(e -> e.getCluster().addMembershipListener(new ClientListener(guiService)));
    }

    public Queue<String> getChatMessages(String chatPoint, GUIService guiService) {
        Chat chat = allLocalClientChats.get(chatPoint);
        if (chat.isRemote()) {
            IQueue<String> queue = hzRemoteClient.get(0).getQueue(chatPoint);
            queue.addItemListener(new MessageListener(guiService), true);
            return queue;
        }

        IQueue<String> queue = hzclient.getQueue(chatPoint);
        queue.addItemListener(new MessageListener(guiService), true);
        return queue;
    }

    public IMap<String, Chat> getIChats() {
        return HZCollectionsUtils.getIChats(hzclient);
    }

    public void updateLocalChatList(String chatName, Chat chat) {
        hzclient.getMap(SET_CHAT_TYPE).put(chatName, chat);
    }

    public List<HazelcastInstance> getHzRemoteClient() {
        return hzRemoteClient;
    }

    public List<IMap<String, Chat>> getRemoteIChats() {
        ArrayList<IMap<String, Chat>> remoteChats = new ArrayList<>();
        hzRemoteClient.forEach(e -> {
            remoteChats.add(e.getMap(SET_CHAT_TYPE));
        });
        return remoteChats;
    }
}
