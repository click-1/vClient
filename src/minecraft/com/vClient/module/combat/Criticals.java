package com.vClient.module.combat;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventReceivePacket;
import com.vClient.module.Category;
import com.vClient.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

public class Criticals extends Module {
    public Criticals() {
        super("Criticals", Keyboard.CHAR_NONE, Category.COMBAT, "Apply critical damage.");
    }

    @Override
    public void onEnable() {
        mc.thePlayer.jump();
        super.onEnable();
    }

    @EventTarget
    public void onPacket(EventReceivePacket event) {
        if (event.getPacket() instanceof C03PacketPlayer)
            ((C03PacketPlayer) event.getPacket()).onGround = false;
    }
}
