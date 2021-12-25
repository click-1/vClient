package com.vClient.module.movement;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventPreMotionUpdate;
import com.vClient.module.Category;
import com.vClient.module.Module;
import com.vClient.util.MovementUtil;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import org.lwjgl.input.Keyboard;

public class NoSlow extends Module {
    public NoSlow() {
        super("NoSlow", Keyboard.CHAR_NONE, Category.MOVEMENT, "Remove movement slowing effects by items.");
    }
    @EventTarget
    public void onPre(EventPreMotionUpdate event) {
        if (mc.thePlayer.onGround && MovementUtil.isMoving() && mc.thePlayer.getItemInUse().getItem() instanceof ItemSword) {
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
        }
    }
}
