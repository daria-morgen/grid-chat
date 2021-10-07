package com.ftalk.gridchat.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Chat implements Serializable {
    private String name;
    private String code;
    private Server server;
    private String creatorName;
    private boolean isTransfer;
    private boolean isPrivate;
    private boolean isLocalPublic;
    private List<String> userNames;

    public Chat() {
    }

    public Chat(String name, String creatorName) {
        this.name = name;
        this.code = UUID.randomUUID().toString().substring(0, 10);
        this.creatorName= creatorName;
    }
    public Chat(String name, boolean isPrivate, List<String> userNames, String creatorName) {
        this.name = name;
        this.code = UUID.randomUUID().toString().substring(0, 10);
        this.isPrivate = isPrivate;
        this.userNames = userNames;
        this.creatorName= creatorName;
    }

    public Chat(String name, Server server, boolean isTransfer,String creatorName) {
        this.name = name;
        this.server = server;
        this.isTransfer = isTransfer;
        this.code = UUID.randomUUID().toString().substring(0, 10);
        this.creatorName= creatorName;
    }

}
