package com.vClient.event.events;

import com.vClient.event.Event;
import net.minecraft.entity.Entity;

public class EventAttack extends Event {
    private Entity targetEntity;

    public EventAttack(Entity targetEntity) {
        this.targetEntity = targetEntity;
    }

    public Entity getTargetEntity() {
        return targetEntity;
    }
}
