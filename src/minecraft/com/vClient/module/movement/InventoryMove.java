package com.vClient.module.movement;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class InventoryMove extends Module {
    public InventoryMove() {
        super("InventoryMove", Keyboard.CHAR_NONE, Category.MOVEMENT, "Allow player to move while accessing inventory, chests, etc. ");
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        KeyBinding[] moveKeys = {mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump};
        if (!(mc.currentScreen instanceof GuiScreen) || mc.currentScreen instanceof GuiChat) {
            return;
        }
        for (KeyBinding key : moveKeys) {
            key.pressed = Keyboard.isKeyDown(key.getKeyCode());
        }
        for (KeyBinding bind : moveKeys) {
            if (Keyboard.isKeyDown(bind.getKeyCode()))
                continue;
            KeyBinding.setKeyBindState(bind.getKeyCode(), false);
        }
    }
}
