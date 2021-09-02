package com.ftalk.gridchat.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

@Component
public class MainFrame {

    private static final Logger log = LoggerFactory.getLogger(MainFrame.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final JFrame f;

    private final GUIElements guiElements;

    public MainFrame(GUIElements guiElements) {
        this.guiElements = guiElements;

        this.f = new JFrame("The Twilight Zone");

        f.setBounds(100, 100, 600, 500);
        f.setTitle("GridChat");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.add(this.guiElements.getJsp(), BorderLayout.CENTER);
        // label, который будет отражать количество клиентов в чате
        f.add(this.guiElements.getJlNumberOfClients(), BorderLayout.NORTH);
        f.add(this.guiElements.getBottomPanel(), BorderLayout.SOUTH);

        // отображаем форму
        f.setVisible(true);
    }
}
