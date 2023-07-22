package com.vClient.module.minigames;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventChat;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.vClient;
import net.minecraft.item.Item;
import org.lwjgl.input.Keyboard;

public class Thrower extends Module {
    public Thrower() {
        super("Thrower", Keyboard.CHAR_NONE, Category.MINIGAMES, "bot");
    }

    @EventTarget
    public void onChat(EventChat event) {
        if (event.getIncomingChat().contains("bozo")) {
            removeTrash(1);
        }
    }

    private void removeTrash(int i) {
        if (mc.thePlayer.inventory.getStackInSlot(i) == null || mc.thePlayer.inventory.getStackInSlot(i).stackSize == 0)
            return;
        Item item = mc.thePlayer.inventory.getStackInSlot(i).getItem();
        if (item == null)
            return;
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i < 9 ? i + 36 : i, 4, 4, mc.thePlayer);
    }

}
