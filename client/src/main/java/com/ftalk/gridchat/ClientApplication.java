package com.ftalk.gridchat;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {
    public static void main(String[] args) {
        Application.launch(ClientFxApplication.class, args);
    }
}
