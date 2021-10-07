package com.ftalk.gridchat.mediator.impl;

import com.ftalk.gridchat.dto.Chat;
import com.ftalk.gridchat.dto.Events;
import com.ftalk.gridchat.dto.UIComponentNames;
import com.ftalk.gridchat.mediator.Mediator;
import com.ftalk.gridchat.service.HazelcastService;
import com.hazelcast.collection.ItemEvent;
import com.hazelcast.collection.ItemListener;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.map.MapEvent;
import javafx.application.Platform;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Queue;

@Component
public class HzUIDialog implements Mediator {

    private TextField loginTextField;

    private Button loginButton;

    private TextField nChatTextField;

    private Button nChatButton;

    private CheckBox nChatPrivateCheckBox;

    private TextField nChatPrivateTextField;

    private CheckBox nChatRemoteCheckBox;

    private ListView chatListView;

    private Label chatNameLabel;

    private TextArea chatTextArea;

    private TextField sendMessageTextField;

    private Button sendMessageButton;

    private final HazelcastService hazelcastService;

    public HzUIDialog(HazelcastService hazelcastService) {
        this.hazelcastService = hazelcastService;
    }

    @Override
    public void event(Events event) {
        switch (event) {
            case REGISTER_USER:
                this.loginButton.setOnAction(actionEvent -> {
                            hazelcastService.registerClient(loginTextField.getText());
                            this.loginTextField.setDisable(true);
                            this.loginButton.setDisable(true);

                            //формируем список чатов из опубликованных в локальной сети
                            //для 1 клиента он будет пуст. Подписываемся на обновление этого списка.
                            //в локальном кластере(chats_cluster) хранится только список чатов.
//                            for (Map<String, Chat> chat : ) {
                            getChatList().forEach(e -> this.chatListView.getItems().add(e.getName()));

                            this.nChatTextField.setDisable(false);
                            this.nChatPrivateCheckBox.setDisable(false);
                            this.nChatRemoteCheckBox.setDisable(false);
                            this.chatListView.setDisable(false);
                            this.nChatButton.setDisable(false);

                        }
                );
                break;
            case REGISTER_LISTENERS:
                this.nChatPrivateCheckBox.setOnMouseClicked(e -> {
                    this.nChatPrivateTextField.setDisable(!nChatPrivateTextField.isDisable());
                });
                this.chatListView.setOnMouseClicked(e -> {
                    if (e.getClickCount() > 1) {
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

                        this.chatNameLabel.setDisable(false);
                        this.chatTextArea.setDisable(false);
                        this.sendMessageTextField.setDisable(false);
                        this.sendMessageButton.setDisable(false);
                    }
                });

                this.nChatButton.setOnMouseClicked(actionEvent -> {
                    if (nChatTextField.getText().isEmpty())
                        return;

                    if (nChatPrivateCheckBox.isSelected() && !nChatRemoteCheckBox.isSelected())
                        hazelcastService.createNewPrivateChat(nChatTextField.getText(), nChatPrivateTextField.getText());

                    if (!nChatPrivateCheckBox.isSelected() && !nChatRemoteCheckBox.isSelected())
                        hazelcastService.createNewLocalChat(nChatTextField.getText());

                    if (!nChatPrivateCheckBox.isSelected() && nChatRemoteCheckBox.isSelected())
                        hazelcastService.createNewRemoteChat(nChatTextField.getText());

                    if (nChatPrivateCheckBox.isSelected() && nChatRemoteCheckBox.isSelected())
                        hazelcastService.createNewPrivateRemoteChat(nChatTextField.getText(), nChatPrivateTextField.getText());


//                    nChatTextField.setText(Strings.EMPTY);
                });

                this.sendMessageButton.setOnAction(actionEvent -> {
                    hazelcastService.sendMessage(sendMessageTextField.getText());
                });
                break;
        }

    }

    @Override
    public void initialUiObject(Object uiSender, UIComponentNames fieldName) {
        switch (fieldName) {
            case LOGIN_TEXT_FIELD:
                this.loginTextField = (TextField) uiSender;
                break;
            case LOGIN_BUTTON:
                this.loginButton = (Button) uiSender;
                break;
            case N_CHAT_TEXT_FIELD:
                this.nChatTextField = (TextField) uiSender;
                break;
            case N_CHAT_BUTTON:
                this.nChatButton = (Button) uiSender;
                break;
            case N_CHAT_CHECK_BOX:
                this.nChatPrivateCheckBox = (CheckBox) uiSender;
                break;
            case N_CHAT_LOCAL_CHECK_BOX:
                this.nChatRemoteCheckBox = (CheckBox) uiSender;
                break;
            case N_CHAT_PRIVATE_TEXT_FIELD:
                this.nChatPrivateTextField = (TextField) uiSender;
                break;
            case CHAT_LIST_VIEW:
                this.chatListView = (ListView) uiSender;
                break;
            case CHAT_NAME_LABEL:
                this.chatNameLabel = (Label) uiSender;
                break;
            case CHAT_TEXT_AREA:
                this.chatTextArea = (TextArea) uiSender;
                break;
            case SEND_MESSAGE_TEXT_FIELD:
                this.sendMessageTextField = (TextField) uiSender;
                break;
            case SEND_MESSAGE_BUTTON:
                this.sendMessageButton = (Button) uiSender;
                break;

        }
    }

    private List<Chat> getChatList() {
        return hazelcastService.getChatList(new EntryListener<String, Chat>() {
            @Override
            public void mapEvicted(MapEvent mapEvent) {

            }

            @Override
            public void mapCleared(MapEvent mapEvent) {

            }

            @Override
            public void entryUpdated(EntryEvent<String, Chat> entryEvent) {
                Platform.runLater(() -> {
                    if (hazelcastService.isChatAvailable(entryEvent))
                        chatListView.getItems().add(entryEvent.getValue().getName());
                });
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
