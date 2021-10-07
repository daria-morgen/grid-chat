package com.ftalk.gridchat.service;

import com.ftalk.gridchat.balancer.RemoteChatBalancer;
import com.ftalk.gridchat.dto.Chat;
import com.ftalk.gridchat.hazelcast.HZCollectionsUtils;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.collection.IQueue;
import com.hazelcast.collection.ItemListener;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.ftalk.gridchat.dto.GridChatConstants.*;

@Service
public class HazelcastService {

    private String userName;

    private String hzRemoteUserName;

    private Chat chat;

    private final HazelcastInstance hzChatListClient;

    private HazelcastInstance hzLocalClient;
    private HazelcastInstance hzRemoteClient;

    private HazelcastInstance hzRemoteChatListClient;

    private final RestTemplateService restTemplate;

    private final RemoteChatBalancer remoteChatBalancer;


    public HazelcastService(@Qualifier("hazelcastLocalChatsInstance") HazelcastInstance hzChatListClient,
                            RestTemplateService restTemplate,
                            RemoteChatBalancer remoteChatBalancer) {
        this.hzChatListClient = hzChatListClient;
        this.restTemplate = restTemplate;
        this.remoteChatBalancer = remoteChatBalancer;

        if (remoteChatBalancer.getPublicIPSize() > 0) {
            ClientConfig clientRemoteConfig = new ClientConfig();
            clientRemoteConfig.setInstanceName("client_sc_" + UUID.randomUUID().toString().substring(0, 5));
            clientRemoteConfig.setClusterName(CLUSTER_SUPER_CLUSTER);
            remoteChatBalancer.getPublicIPServers().forEach(e -> clientRemoteConfig.getNetworkConfig().addAddress(e.getURL()));

            hzRemoteChatListClient = HazelcastClient.newHazelcastClient(clientRemoteConfig);

        }
    }

    public void registerClient(String userName) {
        this.userName = userName;
        this.hzRemoteUserName = "r_" + userName;
    }

    public List<Chat> getChatList(EntryListener<String, Chat> entryListener) {
        Map<String, Chat> resultMap = new HashMap<>();
        IMap<String, Chat> iChats = HZCollectionsUtils.getIChats(hzChatListClient);
        iChats.addEntryListener(entryListener, true);
        resultMap.putAll(iChats);

        if (hzRemoteChatListClient != null) {
            IMap<String, Chat> iRemoteChats;
            iRemoteChats = HZCollectionsUtils.getIChats(hzRemoteChatListClient);
            iRemoteChats.addEntryListener(entryListener, true);
            resultMap.putAll(iRemoteChats);
        }

        return resultMap.values().stream().
                filter(this::isChatAvailable
                ).collect(Collectors.toList());
    }

    public void createNewRemoteChat(String newChat) {
        if (hzRemoteChatListClient != null) {
            Chat chat = remoteChatBalancer.createNewRemoteChat(newChat, this.userName);
            hzRemoteChatListClient.getMap(MAP_CHATS).put(newChat, chat);
        }
    }

    public void createNewPrivateRemoteChat(String newChat, String toUserName) {
        if (hzRemoteChatListClient != null) {
            Chat chat = new Chat(newChat, true, Collections.singletonList(toUserName), this.userName);
            hzRemoteChatListClient.getMap(MAP_CHATS).put(newChat, chat);
        }
    }

    public void createNewPrivateChat(String newChat, String toUserName) {
        Chat chat = new Chat(newChat, true, Collections.singletonList(toUserName), this.userName);
        hzChatListClient.getMap(MAP_CHATS).put(newChat, chat);
    }

    public void createNewLocalChat(String newChat) {
        Chat chat = new Chat(newChat, this.userName);
        hzChatListClient.getMap(MAP_CHATS).put(newChat, chat);
    }

    //При подключении к чату создаем клиента и сервер.
    //Текущий пользователь больше не хранит данные о прошлом чате.
    public Queue<String> getChat(@NonNull String chatName, ItemListener<String> listener) {
        if (HZCollectionsUtils.getChats(hzChatListClient).containsKey(chatName)) {
            this.chat = HZCollectionsUtils.getChats(hzChatListClient).get(chatName);
        } else {
            this.chat = HZCollectionsUtils.getChats(hzRemoteChatListClient).get(chatName);
        }

        if (this.chat.isTransfer()) {

            if (hzRemoteClient != null) {
                hzRemoteClient.shutdown();
            }
            if (hzLocalClient != null) {
                hzLocalClient.shutdown();
            }

            //при подключении к удаленному чату, кладем его в локальный кластер.
            hzChatListClient.getMap(REMOTE_MAP_CHATS).put(chatName, chat);

            // создаем клиента, слушающего  удаленный чат на сервере.
            ClientConfig clientRemoteConfig = new ClientConfig();
            clientRemoteConfig.setInstanceName(hzRemoteUserName);
            clientRemoteConfig.setClusterName(CLUSTER_SUPER_CLUSTER);
            clientRemoteConfig.getNetworkConfig().addAddress(chat.getServer().getURL());
            remoteChatBalancer.getPublicIPServers().forEach(e -> clientRemoteConfig.getNetworkConfig().addAddress(e.getURL()));

            hzRemoteClient = HazelcastClient.newHazelcastClient(clientRemoteConfig);

            //получаем очередь сообщений с удаленного сервера.
            IQueue<String> queue = hzRemoteClient.getQueue(chat.getCode());
            queue.addItemListener(listener, true);
            return queue;

        } else {

            if (hzRemoteClient != null) {
                hzRemoteClient.shutdown();
            }
            if (hzLocalClient != null) {
                hzLocalClient.shutdown();
            }

            //Для локального чата создаем отдельный инстанс server для конкретного чата
            restTemplate.createNewChat(this.chat);

            //Для локального чата создаем клиента
            ClientConfig clientRemoteConfig = new ClientConfig();
            clientRemoteConfig.setInstanceName(userName);
            clientRemoteConfig.setClusterName(chat.getCode());
            ClientNetworkConfig networkConfig = clientRemoteConfig.getNetworkConfig();
            networkConfig.addAddress("127.0.0.1", "127.0.0.1:5704");
            hzLocalClient = HazelcastClient.newHazelcastClient(clientRemoteConfig);

            IQueue<String> queue = hzLocalClient.getQueue(chat.getCode());
            queue.addItemListener(listener, true);

            return queue;
        }
    }

    //Предполагаем, что
    public void sendMessage(String text) {
        if (chat.isTransfer()) {
            hzRemoteClient.getQueue(this.chat.getCode()).add(LocalDate.now() + ":" + this.userName + ": " + text);
        } else {
            hzLocalClient.getQueue(this.chat.getCode()).add(LocalDate.now() + ":" + this.userName + ": " + text);
        }
    }

    private boolean isChatAvailable(Chat e) {
        if (e.isPrivate() &&
                (e.getUserNames().contains(this.userName) || e.getCreatorName().equals(this.userName))) {
            return true;
        } else if (e.isTransfer())
            return true;

        return !e.isPrivate();
    }

    public boolean isChatAvailable(EntryEvent<String, Chat> entryEvent) {
        Chat chat = entryEvent.getValue();
        return isChatAvailable(chat);
    }
}

