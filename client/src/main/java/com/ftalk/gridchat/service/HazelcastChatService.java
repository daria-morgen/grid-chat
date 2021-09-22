package com.ftalk.gridchat.service;

import com.ftalk.gridchat.dto.Chat;
import com.ftalk.gridchat.hazelcast.HZCollectionsUtils;
import com.ftalk.gridchat.hazelcast.listeners.ChatsListener;
import com.hazelcast.map.IMap;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Queue;

@Service
@Getter
public class HazelcastChatService {

    private final HazelcastClientService hazelcastClientService;

    private final GUIService guiService;

    public HazelcastChatService(HazelcastClientService hazelcastClientService, GUIService guiService) {
        this.hazelcastClientService = hazelcastClientService;
        this.guiService = guiService;

        addMembershipListeners();
        addRemoteChatListeners();
    }


    private void addMembershipListeners() {
        hazelcastClientService.addMembershipListeners(guiService);
    }

    private void addRemoteChatListeners(){
        IMap<String, Chat> chatsSet = hazelcastClientService.getIChats();
        chatsSet.addEntryListener(new ChatsListener(guiService), true);

        hazelcastClientService.getHzRemoteClient().forEach(e -> {
            IMap<String, Chat> chatMap = HZCollectionsUtils.getIChats(e);
            hazelcastClientService.updateLocalChatsList(chatMap);
            chatMap.forEach((k,v)->{
                chatMap.addEntryListener(new ChatsListener(guiService), true);
            });
        });
    }

    public Integer getClientsCount() {
        return hazelcastClientService.getClientsCount();
    }

    public void createNewChat(String newChatName) {
        hazelcastClientService.createNewChat(newChatName);
    }

    public Queue<String> getChatMessages(String chatPoint) {
        return hazelcastClientService.getChatMessages(chatPoint, guiService);
    }

    public void sendMessage(String chatName, String message) {
        hazelcastClientService.sendMessage(chatName, message);
    }

    public IMap<String, Chat> getChats() {
        return hazelcastClientService.getIChats();
    }

    public List<IMap<String, Chat>> getRemoteChats() {
        return hazelcastClientService.getRemoteIChats();
    }
}
