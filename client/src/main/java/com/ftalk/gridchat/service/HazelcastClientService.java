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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import static com.ftalk.gridchat.dto.GridChatConstants.SET_CHAT_TYPE;

@Service
public class HazelcastClientService {

    @Value("${clusterName}")
    private String clusterName;

    private final HazelcastInstance hzclient;

    private final List<HazelcastInstance> hzRemoteClient; //todo

    private final RestTemplateService restTemplate;

    public HazelcastClientService(HazelcastInstance hzclient, RemoteChatsLoader remoteChatsLoader, RestTemplateService restTemplate) {
        this.hzclient = hzclient;

        this.hzRemoteClient = remoteChatsLoader.getHazelcastInstances();

        this.restTemplate = restTemplate;
    }

    public Integer getClientsCount() {
        return restTemplate.getClientsCount();
    }

    public Map<Object, Object> createNewChat(String newChat) {
        hzclient.getMap(SET_CHAT_TYPE).put(newChat, new Chat(newChat));
        return hzclient.getMap(SET_CHAT_TYPE);
    }

    public void sendMessage(String chatName, String text) {
        Map<String, Chat> chatSet = HZCollectionsUtils.getChats(hzclient);
        Chat chat = chatSet.get(chatName);
        if (chat.isRemote()) {
            hzRemoteClient.get(0).getQueue(chatName).add(text);
        } else {
            hzclient.getQueue(chatName).add(LocalDateTime.now() + "-" + hzclient.getName() + ": " + text);
        }
    }

    public void addMembershipListeners(GUIService guiService) {
        hzclient.getCluster().addMembershipListener(new ClientListener(guiService));
        hzRemoteClient.forEach(e -> e.getCluster().addMembershipListener(new ClientListener(guiService)));
    }

    public Queue<String> getChatMessages(String chatPoint, GUIService guiService) {
        IQueue<String> queue = hzclient.getQueue(chatPoint);
        queue.addItemListener(new MessageListener(guiService), true);

        return hzclient.getQueue(chatPoint);
    }

    public IMap<String, Chat> getIChats() {
        return HZCollectionsUtils.getIChats(hzclient);
    }

    public void updateLocalChatList(String chatName, Chat chat) {
        hzclient.getMap(SET_CHAT_TYPE).put(chatName, chat);
    }

    public void updateLocalChatsList(IMap<String, Chat> chats) {
        hzclient.getMap(SET_CHAT_TYPE).putAll(chats);
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
