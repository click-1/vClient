package com.vClient.module.player;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventReceivePacket;
import com.vClient.module.Category;
import com.vClient.module.Module;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.lwjgl.input.Keyboard;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity", Keyboard.CHAR_NONE, Category.PLAYER, "Remove player knockback effects.");
    }
    @EventTarget
    public void onEventReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof S12PacketEntityVelocity) {
            if ((mc.theWorld.getEntityByID(((S12PacketEntityVelocity) event.getPacket()).getEntityID())) != mc.thePlayer)
                return;
            event.setCancelled(true);
            ((S12PacketEntityVelocity) event.getPacket()).motionX = 0;
            ((S12PacketEntityVelocity) event.getPacket()).motionY = 0;
            ((S12PacketEntityVelocity) event.getPacket()).motionZ = 0;
        }
        if (event.getPacket() instanceof S27PacketExplosion)
            event.setCancelled(true);
    }
}
