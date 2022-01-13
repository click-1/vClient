package com.vClient.module.world;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

public class NoFall extends Module {
    public NoFall() {
        super("NoFall", Keyboard.CHAR_NONE, Category.WORLD, "Negate fall damage.");
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.thePlayer.onGround)
            mc.thePlayer.fallDistance = 0;
        if (mc.thePlayer.fallDistance > .5F) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
            mc.thePlayer.fallDistance = 0f;
        }
    }

    @Override
    public void onDisable() {
        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
        mc.thePlayer.fallDistance = 0f;
        super.onDisable();
    }
}