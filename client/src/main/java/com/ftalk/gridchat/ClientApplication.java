package com.ftalk.gridchat;

import com.ftalk.gridchat.fx.ClientFxApplication;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {
    public static void main(String[] args) {
        Application.launch(ClientFxApplication.class, args);
    }
}
