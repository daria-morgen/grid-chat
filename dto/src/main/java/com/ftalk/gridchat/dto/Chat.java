package com.ftalk.gridchat.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Chat implements Serializable {
    private String name;
    private String host;
    private String port;
    private boolean isRemote;

    public Chat(String name) {
        this.name = name;
    }

    public Chat(String name, String host, String port, boolean isRemote) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.isRemote = isRemote;
    }
}
