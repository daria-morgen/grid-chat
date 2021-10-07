package com.ftalk.gridchat.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Server implements Serializable {
    private String host;
    private String port;

    public String getURL(){
        return this.host+":"+this.port;
    }
}
