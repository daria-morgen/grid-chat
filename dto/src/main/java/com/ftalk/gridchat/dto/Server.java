package com.ftalk.gridchat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class Server implements Serializable {
    private String host;
    private String port;

    public String getURL(){
        return this.host+":"+this.port;
    }
}
