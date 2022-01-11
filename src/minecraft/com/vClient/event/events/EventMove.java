package com.vClient.event.events;

import com.vClient.event.Event;

public class EventMove extends Event {
    private boolean safeWalk = false;
    private double x, y, z;
    public EventMove(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public boolean allowSafeWalk() {
        return safeWalk;
    }
    public void setSafeWalk(boolean safeWalk) {
        this.safeWalk = safeWalk;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void setZ(double z) {
        this.z = z;
    }
    public double getY() {
        return y;
    }
    public double getZ() {
        return z;
    }
}
