package com.vClient.event.events;

import com.vClient.event.Event;

public class EventChat extends Event {
    private String message;

    public EventChat(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
