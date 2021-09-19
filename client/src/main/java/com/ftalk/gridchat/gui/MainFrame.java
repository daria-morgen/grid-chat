package com.ftalk.gridchat.gui;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.SimpleDateFormat;

@Component
@Getter
public class MainFrame {

    private static final Logger log = LoggerFactory.getLogger(MainFrame.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final JFrame f;

    private JTextArea jTextArea;
    private JTextField jTextField;
    private JButton jbSendMessage;
    private JScrollPane jsp;
    private JLabel jlNumberOfClients;
    private JPanel bottomPanel;

    public MainFrame() {

        jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        jTextArea.setLineWrap(true);

        jsp = new JScrollPane(jTextArea);

        jlNumberOfClients = new JLabel("");
        bottomPanel = new JPanel(new BorderLayout());

        jbSendMessage = new JButton("Отправить");
        bottomPanel.add(jbSendMessage, BorderLayout.EAST);

        jTextField = new JTextField("Введите ваше сообщение: ");
        bottomPanel.add(jTextField, BorderLayout.CENTER);
        bottomPanel.add(jTextField, BorderLayout.WEST);

        // при фокусе поле имя очищается
        jTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jTextField.setText("");
            }
        });


        this.f = new JFrame("The Twilight Zone");

        f.setBounds(100, 100, 600, 500);
        f.setTitle("GridChat");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.add(jsp, BorderLayout.CENTER);
        // label, который будет отражать количество клиентов в чате
        f.add(jlNumberOfClients, BorderLayout.NORTH);
        f.add(bottomPanel, BorderLayout.SOUTH);

        // отображаем форму
        f.setVisible(true);
    }
}
