package com.ftalk.gridchat.service;

import com.ftalk.gridchat.dto.Chat;
import com.ftalk.gridchat.gui.MainFrame;
import com.hazelcast.core.EntryEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GUIService {

    private static final Logger log = LoggerFactory.getLogger(GUIService.class);

    private final MainFrame mainFrame;

    private List<String> chatList = new ArrayList<>();

    public GUIService(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void postMessage(String message) {
        mainFrame.getJTextArea().append(String.valueOf(message));
        mainFrame.getJTextArea().append("\n");
        mainFrame.getJTextField().setText("");
    }

    public void updateCountOfClients(int countOfClients) {
        mainFrame.getJlNumberOfClients().setText("Количество клиентов в чате: " + countOfClients);
    }

    public void updateChatList(String chatName) {
        mainFrame.updateChatList(chatName);
    }
}
