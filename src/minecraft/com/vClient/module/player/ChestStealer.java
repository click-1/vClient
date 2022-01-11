package com.vClient.module.player;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.ClockUtil;
import com.vClient.vClient;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import org.lwjgl.input.Keyboard;

public class ChestStealer extends Module {
    private ClockUtil clock = new ClockUtil();
    public ChestStealer() {
        super("ChestStealer", Keyboard.CHAR_NONE, Category.PLAYER, "Steal items from a chest.");
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
        clock.updateTime();
        final GuiScreen screen = mc.currentScreen;
        if (!(screen instanceof GuiChest) || clock.elapsedTime() <= vClient.instance.settingsManager.getSettingByName("Delay").getValDouble())
            return;
        final GuiChest chest = (GuiChest) mc.currentScreen;
        if (isEmpty(chest) || mc.thePlayer.inventory.getFirstEmptyStack() == -1)
            mc.thePlayer.closeScreen();
        for (int i = 0; i < chest.inventoryRows * 9; i++) {
            Slot slot = chest.inventorySlots.inventorySlots.get(i);
            if (slot.getStack() != null && !uselessItem(slot.getStack().getItem())) {
                mc.playerController.windowClick(chest.inventorySlots.windowId, slot.slotNumber, 0, 1, mc.thePlayer);
                clock.resetTime();
                return;
            }
        }
    }
    private boolean isEmpty(GuiChest chest) {
        for (int i = 0; i < chest.inventoryRows * 9; i++) {
            ItemStack itemStack = chest.inventorySlots.inventorySlots.get(i).getStack();
            if (itemStack != null && !uselessItem(itemStack.getItem()))
                return false;
        }
        return true;
    }
    private boolean uselessItem(Item item) {
        return item instanceof ItemTool || item instanceof ItemSnowball ||
                item instanceof ItemEgg;
    }
}
