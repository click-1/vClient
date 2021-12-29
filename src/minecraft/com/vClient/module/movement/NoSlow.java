package com.vClient.module.movement;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventPostMotionUpdate;
import com.vClient.event.events.EventReceivePacket;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.MovementUtil;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import org.lwjgl.input.Keyboard;

public class NoSlow extends Module {
    public NoSlow() {
        super("NoSlow", Keyboard.CHAR_NONE, Category.MOVEMENT, "Remove movement slowing effects by items.");
    }
    @EventTarget
    public void onPacket(EventReceivePacket event) {
        if (event.getPacket() instanceof C07PacketPlayerDigging) {
            if (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && MovementUtil.isMoving())
                event.setCancelled(true);
        }
    }
    @EventTarget
    public void onPost(EventPostMotionUpdate event) {
        if (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && MovementUtil.isMoving()) {
            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
        }
    }
}
