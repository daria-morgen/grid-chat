package com.ftalk.gridchat;

import com.ftalk.gridchat.dto.Events;
import com.ftalk.gridchat.dto.UIComponentNames;
import com.ftalk.gridchat.mediator.Mediator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;


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

    private final Mediator mediator;

    public SimpleUIController(Mediator mediator) {
        this.mediator = mediator;
    }

    @FXML
    public void initialize() {

        mediator.initialUiObject(this.loginTextField, UIComponentNames.LOGIN_TEXT_FIELD);
        mediator.initialUiObject(this.loginButton, UIComponentNames.LOGIN_BUTTON);
        mediator.initialUiObject(this.nChatTextField, UIComponentNames.N_CHAT_TEXT_FIELD);
        mediator.initialUiObject(this.nChatButton, UIComponentNames.N_CHAT_BUTTON);
        mediator.initialUiObject(this.chatListView, UIComponentNames.CHAT_LIST_VIEW);
        mediator.initialUiObject(this.chatNameLabel, UIComponentNames.CHAT_NAME_LABEL);
        mediator.initialUiObject(this.chatTextArea, UIComponentNames.CHAT_TEXT_AREA);
        mediator.initialUiObject(this.sendMessageTextField, UIComponentNames.SEND_MESSAGE_TEXT_FIELD);
        mediator.initialUiObject(this.sendMessageButton, UIComponentNames.SEND_MESSAGE_BUTTON);

        mediator.event(Events.REGISTER_USER);
        mediator.event(Events.REGISTER_LISTENERS);
    }
}
