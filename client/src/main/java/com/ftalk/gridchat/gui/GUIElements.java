package com.ftalk.gridchat.gui;

import com.ftalk.gridchat.service.MessageService;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

@Component
@Getter
public class GUIElements {

    private final MessageService messageService;

    private JTextArea jTextArea;
    private JTextField jTextField;
    private JButton jbSendMessage;
    private JScrollPane jsp;
    private JLabel jlNumberOfClients;
    private JPanel bottomPanel;

    public GUIElements(MessageService messageService) {
        this.messageService = messageService;

        jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        jTextArea.setLineWrap(true);

        jsp = new JScrollPane(jTextArea);

        jlNumberOfClients = new JLabel("Количество клиентов в чате: ");
        bottomPanel = new JPanel(new BorderLayout());

        jbSendMessage = new JButton("Отправить");
        bottomPanel.add(jbSendMessage, BorderLayout.EAST);

        jTextField = new JTextField("Введите ваше сообщение: ");
        bottomPanel.add(jTextField, BorderLayout.CENTER);
        bottomPanel.add(jTextField, BorderLayout.WEST);

        jbSendMessage.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                messageService.sendMessage(jTextField.getText());
                jTextField.setText("");
            }
        });

        // при фокусе поле имя очищается
        jTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jTextField.setText("");
            }
        });

    }

}
