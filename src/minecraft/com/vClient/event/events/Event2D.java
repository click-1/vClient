package com.vClient.event.events;

import com.vClient.event.Event;

public class Event2D extends Event {
    private int width, height;
    public Event2D(int width, int height) {
        this.width = width;
        this.height = height;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
}
