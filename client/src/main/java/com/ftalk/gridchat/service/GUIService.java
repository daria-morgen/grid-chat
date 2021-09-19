package com.ftalk.gridchat.service;

import com.ftalk.gridchat.gui.MainFrame;
import com.ftalk.gridchat.hazelcast.EntryListener;
import com.hazelcast.map.IMap;
import org.springframework.stereotype.Service;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Service
public class GUIService {

    private final MainFrame mainFrame;

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
}
