package com.ftalk.gridchat;

import com.ftalk.gridchat.dto.Chat;
import com.ftalk.gridchat.service.HazelcastService;
import com.hazelcast.collection.ItemEvent;
import com.hazelcast.collection.ItemListener;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.map.IMap;
import com.hazelcast.map.MapEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;
import java.util.Queue;


@Component
public class SimpleUIController {

    @FXML
    public TextField loginTextField;

    @FXML
    public Button loginButton;

    @FXML
    public TextField nChatTextField;

    @FXML
    public Button nChatButton;

    @FXML
    public ListView chatListView;

    @FXML
    public Label chatNameLabel;

    @FXML
    public TextArea chatTextArea;

    @FXML
    public TextField sendMessageTextField;

    @FXML
    public Button sendMessageButton;

    private final HazelcastService hazelcastService;

    public SimpleUIController(HazelcastService hazelcastService) {
        this.hazelcastService = hazelcastService;
    }

    @FXML
    public void initialize() {
        this.loginButton.setOnAction(actionEvent -> {
                    hazelcastService.registerClient(loginTextField.getText());
                    this.loginTextField.setDisable(true);
                    this.loginButton.setDisable(true);

                    activateChatWindow();
                }
        );
    }

    private void activateChatWindow() {
        this.nChatTextField.setDisable(false);
        this.nChatButton.setDisable(false);
        this.chatListView.setDisable(false);
        this.chatNameLabel.setDisable(false);
        this.chatTextArea.setDisable(false);
        this.sendMessageTextField.setDisable(false);
        this.sendMessageButton.setDisable(false);

        //формируем список чатов из опубликованных в локальной сети
        //для 1 клиента он будет пуст. Подписываемся на обновление этого списка.
        //в локальном кластере(chats_cluster) хранится только список чатов.
        for (Map.Entry<String, Chat> chat : getIChats()) {
            this.chatListView.getItems().add(chat.getValue().getName());
        }

        this.chatListView.setOnMouseClicked(event -> {
            if (event.getClickCount() > 1) {
                chatTextArea.clear();

                String chatName = (String) this.chatListView.getFocusModel().getFocusedItem();
                Queue<String> chat = hazelcastService.getChat(chatName, new ItemListener<String>() {
                    @Override
                    public void itemAdded(ItemEvent<String> itemEvent) {
                        chatTextArea.appendText(itemEvent.getItem());
                        chatTextArea.appendText("\n");
                        sendMessageTextField.setText("");
                    }

                    @Override
                    public void itemRemoved(ItemEvent<String> itemEvent) {
                    }
                });

                chatNameLabel.setText(chatName);

                for (String chatMessage : chat) {
                    chatTextArea.appendText(chatMessage);
                    chatTextArea.appendText("\n");
                }
            }
        });

        this.nChatButton.setOnAction(actionEvent -> {
            hazelcastService.createNewChat(nChatTextField.getText());
        });

        this.sendMessageButton.setOnAction(actionEvent -> {
            hazelcastService.sendMessage(sendMessageTextField.getText());
        });

    }

    private IMap<String, Chat> getIChats() {
        return hazelcastService.getChatList(new EntryListener<String, Chat>() {
            @Override
            public void mapEvicted(MapEvent mapEvent) {

            }

            @Override
            public void mapCleared(MapEvent mapEvent) {

            }

            @Override
            public void entryUpdated(EntryEvent<String, Chat> entryEvent) {
                chatListView.getItems().add(entryEvent.getValue().getName());
            }

            @Override
            public void entryRemoved(EntryEvent<String, Chat> entryEvent) {

            }

            @Override
            public void entryExpired(EntryEvent<String, Chat> entryEvent) {

            }

            @Override
            public void entryEvicted(EntryEvent<String, Chat> entryEvent) {

            }

            @Override
            public void entryAdded(EntryEvent<String, Chat> entryEvent) {

            }
        });
    }
}
