package com.ftalk.gridchat.gui;

import com.ftalk.gridchat.dto.Chat;
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
import java.util.Set;

import static com.ftalk.gridchat.dto.GridChatConstants.SET_CHAT_TYPE;

@Controller
public class GUIController {

    private final HazelcastClientService hazelcastClientService;

    private final MainFrame mainFrame;

    private final GUIService guiService;

    public GUIController(HazelcastClientService hazelcastClientService, MainFrame mainFrame, GUIService guiService) {
        this.hazelcastClientService = hazelcastClientService;
        this.mainFrame = mainFrame;
        this.guiService = guiService;

        ISet<Chat> chats = hazelcastClientService.getSet(SET_CHAT_TYPE);
        chats.addItemListener(new ChatsListener(guiService), true);
        mainFrame.updateChatList(chats);

        ISet<Chat> remoteChats = hazelcastClientService.getRemoteSet(SET_CHAT_TYPE);
        remoteChats.addItemListener(new ChatsListener(guiService), true);
        mainFrame.updateChatList(remoteChats);

        initCreateNewChatListener();
        initActivateChatListener();
        initMessageSenderListener();

        hazelcastClientService.getHzclient().getCluster().addMembershipListener(new ClientListener(guiService));
        hazelcastClientService.getHzRemoteClient().getCluster().addMembershipListener(new ClientListener(guiService));

    }

    private void initMessageSenderListener() {
        mainFrame.getJbSendMessage().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!mainFrame.getJlChatValue().getText().isEmpty())
                    hazelcastClientService.sendMessage(
                            mainFrame.getJlChatValue().getText(),
                            mainFrame.getJTextField().getText());
            }
        });
    }

    private void initActivateChatListener() {
        mainFrame.getList().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String elementAt = (String) mainFrame.getList().getModel().getElementAt(mainFrame.getList().locationToIndex(e.getPoint()));
                    mainFrame.updateChatName(elementAt,
                            hazelcastClientService.getClientsCount());

                    Set<Chat> remoteSet = hazelcastClientService.getRemoteSet(SET_CHAT_TYPE);
                    remoteSet.forEach(r->{
                        if(r.getName().equals(mainFrame.getJlChatValue().getText())){
                            if (r.isRemote()){
                                IQueue<String> queue = hazelcastClientService.getRemoteQueue(elementAt);
                                queue.addItemListener(new MessageListener(guiService), true);
                                queue.forEach(k ->
                                        guiService.postMessage(k)
                                );
                            }else{
                                IQueue<String> queue = hazelcastClientService.getQueue(elementAt);
                                queue.addItemListener(new MessageListener(guiService), true);
                                queue.forEach(k ->
                                        guiService.postMessage(k)
                                );
                            }
                        }
                    });


                }
            }
        });
    }

    private void initCreateNewChatListener() {
        mainFrame.getJbNewChat().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String newChatName = mainFrame.getJNewChatField().getText();
                ISet<Chat> newChat = hazelcastClientService.createNewChat(newChatName);

                newChat.addItemListener(new ChatsListener(guiService), true);

                ISet<Chat> chats = hazelcastClientService.getSet(SET_CHAT_TYPE);
                chats.add(new Chat(newChatName));
            }
        });
    }


}
