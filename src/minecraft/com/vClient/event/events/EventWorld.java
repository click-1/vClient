package com.vClient.event.events;

import com.vClient.event.Event;
import net.minecraft.client.multiplayer.WorldClient;

public class EventWorld extends Event {
    private WorldClient worldClient;
    public EventWorld(WorldClient worldClient) {
        this.worldClient = worldClient;
    }
}
