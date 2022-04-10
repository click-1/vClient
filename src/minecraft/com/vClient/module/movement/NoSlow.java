package com.vClient.module.movement;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventReceivePacket;
import com.vClient.module.Category;
import com.vClient.module.Module;
import net.minecraft.item.*;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S30PacketWindowItems;
import org.lwjgl.input.Keyboard;

public class NoSlow extends Module {
    public NoSlow() {
        super("NoSlow", Keyboard.CHAR_NONE, Category.MOVEMENT, "Remove movement-slowing effects by items.");
    }

    @EventTarget
    public void onPacket(EventReceivePacket event) {
        if ((mc.thePlayer.isUsingItem() || mc.thePlayer.isBlocking()) && (event.getPacket() instanceof S30PacketWindowItems || event.getPacket() instanceof C07PacketPlayerDigging || event.getPacket() instanceof C08PacketPlayerBlockPlacement))
            event.setCancelled(true);
    }

    private boolean usableItem(Item item) {
        return item instanceof ItemSword || item instanceof ItemBow ||
                item instanceof ItemPotion || item instanceof ItemFood;
    }
}
