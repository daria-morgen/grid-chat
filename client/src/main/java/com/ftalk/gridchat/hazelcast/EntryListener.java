package com.ftalk.gridchat.hazelcast;

import com.ftalk.gridchat.service.GUIService;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.MapEvent;
import com.hazelcast.map.listener.*;

public class EntryListener implements
        EntryAddedListener<String, String>,
        EntryRemovedListener<String, String>,
        EntryUpdatedListener<String, String>,
        EntryEvictedListener<String, String>,
        EntryLoadedListener<String, String>,
        MapEvictedListener,
        MapClearedListener {

    private final GUIService guiService;

    public EntryListener(GUIService guiService) {
        this.guiService = guiService;
    }

    @Override
    public void entryAdded(EntryEvent<String, String> event) {
        System.out.println("Entry Added:" + event.getValue());
        guiService.postMessage(event.getValue());
    }

    @Override
    public void entryRemoved(EntryEvent<String, String> event) {
        System.out.println("Entry Removed:" + event);
    }

    @Override
    public void entryUpdated(EntryEvent<String, String> event) {
        System.out.println("Entry Updated:" + event);
    }

    @Override
    public void entryEvicted(EntryEvent<String, String> event) {
        System.out.println("Entry Evicted:" + event);
    }

    @Override
    public void entryLoaded(EntryEvent<String, String> event) {
        System.out.println("Entry Loaded:" + event);
    }

    @Override
    public void mapEvicted(MapEvent event) {
        System.out.println("Map Evicted:" + event);
    }

    @Override
    public void mapCleared(MapEvent event) {
        System.out.println("Map Cleared:" + event);
    }
}

