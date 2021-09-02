package com.ftalk.gridchat.service;

import com.ftalk.gridchat.gui.GUIElements;
import org.springframework.stereotype.Service;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

@Service
public class GUIService {

    private final GUIElements guiElements;

    public GUIService(GUIElements guiElements) {
        this.guiElements = guiElements;
    }

    public void postMessage(String message) {
        guiElements.getJTextArea().append(String.valueOf(message));
        guiElements.getJTextArea().append("\n");
        guiElements.getJTextField().setText("");
    }
}
