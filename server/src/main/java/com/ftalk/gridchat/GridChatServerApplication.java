package com.ftalk.gridchat;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GridChatServerApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(GridChatServerApplication.class).run(args);
    }
}
