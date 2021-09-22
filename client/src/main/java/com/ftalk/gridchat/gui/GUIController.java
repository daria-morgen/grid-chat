package com.ftalk.gridchat.gui;

import com.ftalk.gridchat.service.GUIService;
import com.ftalk.gridchat.service.HazelcastChatService;
import org.springframework.stereotype.Controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Queue;

@Controller
public class GUIController {

    private final HazelcastChatService hazelcastChatService;

    private final MainFrame mainFrame;

    private final GUIService guiService;

    public GUIController(HazelcastChatService hazelcastChatService, MainFrame mainFrame, GUIService guiService) {
        this.hazelcastChatService = hazelcastChatService;
        this.mainFrame = mainFrame;
        this.guiService = guiService;

        mainFrame.updateChatList(hazelcastChatService.getChats());
//        mainFrame.updateChatList(hazelcastChatService.getRemoteChats());

        initCreateNewChatListener();
        initActivateChatListener();
        initMessageSenderListener();

    }

    private void initMessageSenderListener() {
        mainFrame.getJbSendMessage().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!mainFrame.getJlChatValue().getText().isEmpty())
                    hazelcastChatService.sendMessage(
                            mainFrame.getJlChatValue().getText(),
                            mainFrame.getJTextField().getText());
            }
        });
    }

    private void initActivateChatListener() {
        mainFrame.getList().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String chatPoint = (String) mainFrame.getList().getModel().getElementAt(mainFrame.getList().locationToIndex(e.getPoint()));

                    //todo create client count on chat
                    mainFrame.updateChatName(chatPoint,
                            hazelcastChatService.getClientsCount());

                    Queue<String> messages = hazelcastChatService.getChatMessages(chatPoint);

                    messages.forEach(m -> guiService.postMessage(m));

                }
            }
        });
    }

    private void initCreateNewChatListener() {
        mainFrame.getJbNewChat().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String newChatName = mainFrame.getJNewChatField().getText();
//                mainFrame.getJNewChatField().setText("");
                hazelcastChatService.createNewChat(newChatName);
            }
        });
    }

}
