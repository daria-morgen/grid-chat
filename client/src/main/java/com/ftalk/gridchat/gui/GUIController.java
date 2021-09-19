package com.ftalk.gridchat.gui;

import com.ftalk.gridchat.hazelcast.ClientListener;
import com.ftalk.gridchat.hazelcast.EntryListener;
import com.ftalk.gridchat.service.GUIService;
import com.ftalk.gridchat.service.HazelcastClientService;
import com.ftalk.gridchat.service.MessageService;
import org.springframework.stereotype.Controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Controller
public class GUIController {
    private final MessageService messageService;

    private final HazelcastClientService clientService;

    private final MainFrame mainFrame;

    private final GUIService guiService;

    public GUIController(MessageService messageService, HazelcastClientService clientService, MainFrame mainFrame, GUIService guiService) {
        this.messageService = messageService;
        this.clientService = clientService;
        this.mainFrame = mainFrame;
        this.guiService = new GUIService(mainFrame);

        mainFrame.getJbSendMessage().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                messageService.sendMessage(mainFrame.getJTextField().getText());
                mainFrame.getJTextField().setText("");
            }
        });

        this.mainFrame.getJlNumberOfClients().setText("Количество клиентов в чате: " + clientService.getHzclient().getCluster().getMembers().size());

        clientService.getMessageMap("messages").addEntryListener(new EntryListener(guiService), true);
        clientService.getHzclient().getCluster().addMembershipListener(new ClientListener(guiService));
    }


}
