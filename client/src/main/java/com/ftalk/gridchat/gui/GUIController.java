package com.ftalk.gridchat.gui;

import com.ftalk.gridchat.hazelcast.ChatsListener;
import com.ftalk.gridchat.hazelcast.ClientListener;
import com.ftalk.gridchat.hazelcast.MessageListener;
import com.ftalk.gridchat.service.GUIService;
import com.ftalk.gridchat.service.HazelcastClientService;
import com.ftalk.gridchat.service.MessageService;
import org.springframework.stereotype.Controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Controller
public class GUIController {

    private final HazelcastClientService hazelcastClientService;

    private final MainFrame mainFrame;

    private final GUIService guiService;

    public GUIController(HazelcastClientService hazelcastClientService, MainFrame mainFrame, GUIService guiService) {
        this.hazelcastClientService = hazelcastClientService;
        this.mainFrame = mainFrame;
        this.guiService = guiService;

        mainFrame.getJbNewChat().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hazelcastClientService.createNewChat(mainFrame.getJNewChatField().getText());
//                mainFrame.getJNewChatField().setText("");
            }
        });


        mainFrame.getList().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    mainFrame.updateChatName((String) mainFrame.getList().getModel().getElementAt(mainFrame.getList().locationToIndex(e.getPoint())),
                            hazelcastClientService.getClientsCount());
                }
            }
        });


        mainFrame.getJbSendMessage().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hazelcastClientService.sendMessage(
                        mainFrame.getJNewChatField().getText(),
                        mainFrame.getJTextField().getText());
                mainFrame.getJlChatName().setText(mainFrame.getJNewChatField().getText());
            }
        });

        mainFrame.updateChatList(hazelcastClientService.getMap("chats"));
        hazelcastClientService.getMap("chats").addEntryListener(new ChatsListener(guiService), true);
        hazelcastClientService.getMap("messages").addEntryListener(new MessageListener(guiService), true);
        hazelcastClientService.getHzclient().getCluster().addMembershipListener(new ClientListener(guiService));

    }


}
