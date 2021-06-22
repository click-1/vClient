package com.vClient.module.player;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventReceivePacket;
import com.vClient.module.Category;
import com.vClient.module.Module;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import org.lwjgl.input.Keyboard;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity", Keyboard.CHAR_NONE, Category.PLAYER);
    }
    @EventTarget
    public void onEventReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof S12PacketEntityVelocity) {
            event.setCancelled(true);
        }
    }
}
