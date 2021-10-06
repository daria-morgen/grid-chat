package com.ftalk.gridchat.hazelcast;

import com.ftalk.gridchat.dto.Server;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
public class RemoteServersLoader {

    private List<Server> publicIPServers = new ArrayList<>();

    //todo added remote loader
    public List<Server> getPublicIPServers() {
        publicIPServers.add(new Server("194.87.248.33", "5701"));
        return this.publicIPServers;
    }

    public Server getPublicServer() {
        return publicIPServers.get(0);
    }
}
