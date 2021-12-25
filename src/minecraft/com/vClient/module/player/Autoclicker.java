package com.vClient.module.player;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import org.lwjgl.input.Keyboard;

public class AutoClicker extends Module {
    public AutoClicker() {
        super("AutoClicker", Keyboard.CHAR_NONE, Category.PLAYER, "Repeated swinging while holding left mouse button.");
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.gameSettings.keyBindAttack.isKeyDown() && !mc.gameSettings.keyBindUseItem.isKeyDown())
            mc.clickMouse();
    }
}
