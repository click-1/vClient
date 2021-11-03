package com.vClient.module.misc;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import org.lwjgl.input.Keyboard;

public class GameSpeed extends Module {
    public GameSpeed() {
        super("GameSpeed", Keyboard.CHAR_NONE, Category.MISC, "Change player game speed.");
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
        mc.timer.timerSpeed = 2F;
    }
    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1F;
        super.onDisable();
    }
}
