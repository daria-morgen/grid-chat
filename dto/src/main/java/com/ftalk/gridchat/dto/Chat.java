package com.ftalk.gridchat.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Chat implements Serializable {
    private String name;
    private String code;
    private Server server;
    private boolean isTransfer;

    public Chat(String name) {
        this.name = name;
        this.code = UUID.randomUUID().toString().substring(0, 10);
    }

    public Chat(String name, Server server, boolean isTransfer) {
        this.name = name;
        this.server = server;
        this.isTransfer = isTransfer;
        this.code = UUID.randomUUID().toString().substring(0, 10);
    }

}
