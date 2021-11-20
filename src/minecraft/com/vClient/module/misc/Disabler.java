package com.vClient.module.misc;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventReceivePacket;
import com.vClient.module.Category;
import com.vClient.module.Module;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import org.lwjgl.input.Keyboard;

public class Disabler extends Module {
    public Disabler() {
        super("Disabler", Keyboard.CHAR_NONE, Category.MISC, "Disable Watchdog.");
    }
    @EventTarget
    public void onEventReceivePacket(EventReceivePacket event) {
        if (event.getPacket() instanceof C00PacketKeepAlive) {
            event.setCancelled(true);
        }
    }
}