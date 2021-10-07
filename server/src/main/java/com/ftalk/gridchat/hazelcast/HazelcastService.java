package com.ftalk.gridchat.hazelcast;

import com.ftalk.gridchat.dto.Chat;
import com.hazelcast.config.Config;
import com.hazelcast.config.OnJoinPermissionOperationName;
import com.hazelcast.config.PermissionConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.stereotype.Service;

@Service
public class HazelcastService {

    private final HazelcastInstance instanceOfChatList;

    private HazelcastInstance instanceOfChat;

    public HazelcastService(HazelcastInstance instanceOfChatList) {
        this.instanceOfChatList = instanceOfChatList;
    }

    public int getCountOfClients() {
        return instanceOfChat.getClientService().getConnectedClients().size();
    }

    public HazelcastInstance createInstance(Chat chat) {
        if (instanceOfChat != null)
            instanceOfChat.shutdown();

        Config cfg = new Config();
        cfg.setClusterName(chat.getCode());
//        if (chat.isPrivate()) {
//            cfg.setLiteMember(true);
//            PermissionConfig allPermission = new PermissionConfig(
//                    PermissionConfig.PermissionType.QUEUE,
//                    chat.getCode(),
//                    chat.getCode()
//            );
//
//            cfg.getSecurityConfig()
//                    .setEnabled(true)
//                    .setOnJoinPermissionOperation(OnJoinPermissionOperationName.NONE)
//                    .addClientPermissionConfig(allPermission);
//        }
        return this.instanceOfChat = Hazelcast.newHazelcastInstance(cfg);

    }
}
