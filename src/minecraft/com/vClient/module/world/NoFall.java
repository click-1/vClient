package com.vClient.module.world;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventPreMotionUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;

public class NoFall extends Module {
    private float yaw, pitch;
    private boolean fell;

    public NoFall() {
        super("NoFall", Keyboard.CHAR_NONE, Category.WORLD, "Negate fall damage.");
    }

    @EventTarget
    public void onPre(EventPreMotionUpdate event) {
        if (mc.thePlayer.fallDistance < 3f) {
            fell = false;
        }

        if (mc.thePlayer.fallDistance >= 3f) {
            if (!fell) {
                yaw = event.getYaw();
                pitch = event.getPitch();
                fell = true;
            }
            event.setYaw(yaw);
            event.setPitch(pitch);
            event.setGround(false);
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, true));
        }
    }
}
