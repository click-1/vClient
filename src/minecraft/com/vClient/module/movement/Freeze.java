package com.vClient.module.movement;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import org.lwjgl.input.Keyboard;

public class Freeze extends Module {
    public Freeze() {
        super("Freeze", Keyboard.CHAR_NONE, Category.MOVEMENT, "Freeze player at current location.");
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
        mc.thePlayer.isDead = true;
        mc.thePlayer.rotationYaw = mc.thePlayer.cameraYaw;
        mc.thePlayer.rotationPitch = mc.thePlayer.cameraPitch;
    }
    @Override
    public void onDisable() {
        mc.thePlayer.isDead = false;
    }
}
