package com.ftalk.gridchat.mediator;

import com.ftalk.gridchat.dto.Chat;
import com.ftalk.gridchat.dto.Events;
import com.ftalk.gridchat.dto.UIComponentNames;
import com.hazelcast.core.EntryListener;
import com.hazelcast.map.IMap;

// Общий интерфейс посредников.
public interface Mediator {
    void event(Events event);

    void initialUiObject(Object uiSender, UIComponentNames fieldName);

}
