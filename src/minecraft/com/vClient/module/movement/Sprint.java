package com.vClient.module.movement;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventTick;
import com.vClient.module.Category;
import com.vClient.module.Module;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class Sprint extends Module {
    private boolean sprinting = true;
    private int ticksleft = 590;
    private final int key = mc.gameSettings.keyBindSprint.getKeyCode();

    public Sprint() {
        super("Sprint", Keyboard.CHAR_NONE, Category.MOVEMENT, "Keepsprint.");
    }

    @EventTarget
    public void onTick(EventTick event) {
        ticksleft--;
        sprinting = !sprinting;
        if (!sprinting)
            KeyBinding.setKeyBindState(key, Keyboard.isKeyDown(key));
        else
            KeyBinding.setKeyBindState(key, true);
        if (ticksleft <= 0) {
            KeyBinding.setKeyBindState(key, false);
            mc.thePlayer.setSprinting(false);
            ticksleft = 590;
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.keyBindSprint.pressed = false;
    }
}
