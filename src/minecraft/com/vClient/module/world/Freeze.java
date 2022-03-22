package com.vClient.module.world;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import org.lwjgl.input.Keyboard;

public class Freeze extends Module {
    public Freeze() {
        super("Freeze", Keyboard.CHAR_NONE, Category.WORLD, "Freeze player at current location.");
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        mc.thePlayer.isDead = true;
    }

    @Override
    public void onDisable() {
        mc.thePlayer.isDead = false;
        super.onDisable();
    }
}
