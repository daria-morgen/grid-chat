package com.ftalk.gridchat.gui;

import com.ftalk.gridchat.dto.Chat;
import com.hazelcast.map.IMap;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
@Getter
public class MainFrame {

    private static final Logger log = LoggerFactory.getLogger(MainFrame.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final JFrame f;

    private JPanel chatPanel;
    private JPanel chatLabelsPanel;
    private JTextArea jTextArea;
    private JTextField jTextField;
    private JButton jbSendMessage;
    private JScrollPane jsp;
    private JLabel jlChatName;
    private JLabel jlChatValue;
    private JLabel jlNumberOfClients;
    private JPanel bottomPanel;

    private JPanel chatChatLabelButtonPanel;
    private JPanel chatListPanel;
    private JPanel bottomNewChatPanel;
    private JButton jbNewChat;
    private JTextField jNewChatField;
    private JLabel jlChatList;

    DefaultListModel model;
    private JList list;
    private JScrollPane jScrollPane;


    public MainFrame() {

        this.f = new JFrame("The Twilight Zone");

        f.setBounds(100, 100, 300, 500);
        f.setTitle("GridChat");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initChatListBlock();
        f.add(chatListPanel, BorderLayout.NORTH);

        initChatBlock();
        f.add(chatPanel, BorderLayout.CENTER);

        // отображаем форму
        f.setVisible(true);
    }

    private void initChatBlock() {
        jlChatName = new JLabel("Имя чата: ");
        jlChatValue = new JLabel();
        jlNumberOfClients = new JLabel("Количество клиентов в чате: " + 0);
        chatLabelsPanel = new JPanel(new BorderLayout());
        chatLabelsPanel.add(jlChatName, BorderLayout.WEST);
        chatLabelsPanel.add(jlChatValue, BorderLayout.CENTER);
        chatLabelsPanel.add(jlNumberOfClients, BorderLayout.SOUTH);


        jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        jTextArea.setLineWrap(true);

        jsp = new JScrollPane(jTextArea);


        bottomPanel = new JPanel(new BorderLayout());

        jbSendMessage = new JButton("Отправить");
        bottomPanel.add(jbSendMessage, BorderLayout.EAST);

        jTextField = new JTextField("Введите ваше сообщение: ");
        bottomPanel.add(jTextField, BorderLayout.CENTER);

        chatPanel = new JPanel(new BorderLayout());
        chatPanel.add(chatLabelsPanel, BorderLayout.NORTH);
        chatPanel.add(jsp, BorderLayout.CENTER);
        chatPanel.add(bottomPanel, BorderLayout.SOUTH);

        // при фокусе поле имя очищается
        jTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jTextField.setText("");
            }
        });
    }

    void initChatListBlock() {
        jlChatList = new JLabel("Список чатов: ");
        model = new DefaultListModel();
        list = new JList(model);
        jScrollPane = new JScrollPane(list);

        jNewChatField = new JTextField("Создать новый чат: ");
        jbNewChat = new JButton("+");

        bottomNewChatPanel = new JPanel(new BorderLayout());
        bottomNewChatPanel.add(jNewChatField, BorderLayout.WEST);
        bottomNewChatPanel.add(jbNewChat, BorderLayout.EAST);

        chatChatLabelButtonPanel = new JPanel(new BorderLayout());
        chatChatLabelButtonPanel.add(jlChatList, BorderLayout.WEST);
        chatChatLabelButtonPanel.add(bottomNewChatPanel, BorderLayout.EAST);

        chatListPanel = new JPanel(new BorderLayout());
        chatListPanel.add(chatChatLabelButtonPanel, BorderLayout.NORTH);
        chatListPanel.add(jScrollPane, BorderLayout.CENTER);
    }

    public void updateChatList(IMap<String, Chat> chats) {
        log.info("updateChatList: {}", chats.size());
        chats.forEach((k, v) -> {
            model.addElement(v.getName());
        });
    }

    public void updateChatList(String chatName) {

        model.addElement(chatName);
    }

    public void updateChatName(String chatName, Integer clientsCount) {
        jlChatValue.setText(chatName);
        jlNumberOfClients.setText("Количество клиентов в чате: " + clientsCount);

    }

    public void updateChatList(List<IMap<String, Chat>> remoteChats) {
        remoteChats.forEach(e -> e.forEach(c -> updateChatList(c.getValue().getName())));
    }
}
