package com.vClient.event.events;

import com.vClient.event.Event;

public class EventMessage extends Event {
    private String message;

    public EventMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
