package com.vClient.event.events;

import com.vClient.event.Event;

public class EventClairvoyance extends Event {
    private State state;
    public EventClairvoyance(State state) {
        this.state = state;
    }
    public void setState(State state) {
        this.state = state;
    }
    public State getState() {
        return state;
    }
    public enum State {
        PRE, POST
    }
}
