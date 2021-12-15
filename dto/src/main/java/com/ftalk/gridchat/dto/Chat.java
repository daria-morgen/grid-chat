package com.ftalk.gridchat.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
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

}
