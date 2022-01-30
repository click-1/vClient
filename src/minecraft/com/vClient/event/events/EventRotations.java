package com.vClient.event.events;

import com.vClient.event.Event;

public class EventRotations extends Event {
    public float yawHead, pitchHead;
    public boolean active = false;
    public EventRotations(float yawHead, float pitchHead) {
        this.yawHead = yawHead;
        this.pitchHead = pitchHead;
    }
}
