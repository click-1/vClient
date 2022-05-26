package com.vClient.module.movement;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import org.lwjgl.input.Keyboard;

public class GuiMove extends Module {
    private final KeyBinding[] moveKeys = {mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump};

    public GuiMove() {
        super("GuiMove", Keyboard.CHAR_NONE, Category.MOVEMENT, "Allow player to move while accessing inventory, chests, etc.");
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (!(mc.currentScreen instanceof GuiChat || mc.currentScreen instanceof GuiIngameMenu)) {
            for (KeyBinding key : moveKeys) {
                if (GameSettings.isKeyDown(key) && mc.currentScreen instanceof GuiContainer)
                    mc.getNetHandler().addToSendQueue(new C0DPacketCloseWindow(mc.thePlayer.openContainer.windowId));
                key.pressed = GameSettings.isKeyDown(key);
            }
        }
    }

    @Override
    public void onDisable() {
        if (mc.currentScreen != null) {
            for (KeyBinding key : moveKeys)
                    key.pressed = false;
        }
        super.onDisable();
    }
}
