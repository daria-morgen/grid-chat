package com.ftalk.gridchat.gui;

import com.ftalk.gridchat.hazelcast.ChatsListener;
import com.ftalk.gridchat.hazelcast.ClientListener;
import com.ftalk.gridchat.hazelcast.MessageListener;
import com.ftalk.gridchat.service.GUIService;
import com.ftalk.gridchat.service.HazelcastClientService;
import com.hazelcast.collection.IQueue;
import com.hazelcast.collection.ISet;
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
                String newChatName = mainFrame.getJNewChatField().getText();
                IQueue<String> newChat = hazelcastClientService.createNewChat(newChatName);

                newChat.addItemListener(new ChatsListener(guiService), true);

                ISet<String> chats = hazelcastClientService.getSet("chats");
                chats.add(newChatName);
            }
        });


        mainFrame.getList().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String elementAt = (String) mainFrame.getList().getModel().getElementAt(mainFrame.getList().locationToIndex(e.getPoint()));

                    mainFrame.updateChatName(elementAt,
                            hazelcastClientService.getClientsCount());

                    IQueue<String> queue = hazelcastClientService.getQueue(elementAt);
                    queue.addItemListener(new MessageListener(guiService), true);
                    queue.forEach(k ->
                            guiService.postMessage(k)
                    );

                }
            }
        });


        mainFrame.getJbSendMessage().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!mainFrame.getJlChatValue().getText().isEmpty())
                    hazelcastClientService.sendMessage(
                            mainFrame.getJlChatValue().getText(),
                            mainFrame.getJTextField().getText());

            }
        });

        ISet<String> chats = hazelcastClientService.getSet("chats");
        mainFrame.updateChatList(chats);
        chats.addItemListener(new ChatsListener(guiService), true);
        hazelcastClientService.getHzclient().getCluster().addMembershipListener(new ClientListener(guiService));

    }


}
