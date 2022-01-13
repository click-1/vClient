package com.vClient.event.events;

import com.vClient.event.Event;

public class EventChat extends Event {
    private String incomingChat;
    public EventChat(String incomingChat) {
        this.incomingChat = incomingChat;
    }
    public String getIncomingChat() {
        return incomingChat;
    }
    public void setIncomingChat(String incomingChat) {
        this.incomingChat = incomingChat;
    }
}
