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
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Queue;

import static com.ftalk.gridchat.dto.GridChatConstants.SET_CHAT_TYPE;

@Service
public class HazelcastService {

    private String userName;

    private Chat chat;

    private final HazelcastInstance hzChatListClient;

    private HazelcastInstance hzLocalClient;

    private HazelcastInstance hzRemoteClient; //todo

    private final RestTemplateService restTemplate;

    public HazelcastService(HazelcastInstance hzChatListClient, RemoteChatsLoader remoteChatsLoader, RestTemplateService restTemplate) {
        this.hzChatListClient = hzChatListClient;
//        this.hzRemoteClient = remoteChatsLoader.getHazelcastInstances();
        this.restTemplate = restTemplate;
    }

    public void registerClient(String userName) {
        this.userName = userName;
    }

    public IMap<String, Chat> getChatList(EntryListener<String, Chat> entryListener) {
        IMap<String, Chat> iChats = HZCollectionsUtils.getIChats(hzChatListClient);
        iChats.addEntryListener(entryListener, true);
        return iChats;
    }

    public void createNewChat(String newChat) {
        Chat chat = new Chat(newChat);
        hzChatListClient.getMap(SET_CHAT_TYPE).put(newChat, chat);
    }

    //При подключении к чату создаем клиента и сервер.
    //Текущий пользователь больше не хранит данные о прошлом чате.
    public Queue<String> getChat(@NonNull String chatName, ItemListener<String> listener) {
        this.chat = HZCollectionsUtils.getChats(hzChatListClient).get(chatName);

        restTemplate.createNewChat(this.chat.getCode());

        ClientConfig clientRemoteConfig = new ClientConfig();
        clientRemoteConfig.setInstanceName(userName);
        clientRemoteConfig.setClusterName(chat.getCode());


        hzLocalClient = HazelcastClient.newHazelcastClient(clientRemoteConfig);

        IQueue<String> queue = hzLocalClient.getQueue(chat.getCode());
        queue.addItemListener(listener, true);

        return queue;
    }

    public void sendMessage(String text) {
        hzLocalClient.getQueue(this.chat.getCode()).add(LocalDate.now() + ":" + this.userName + ": " + text);
    }

}
