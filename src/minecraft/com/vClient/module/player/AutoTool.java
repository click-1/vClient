package com.vClient.module.player;

import com.vClient.event.EventTarget;
import com.vClient.event.events.EventClickBlock;
import com.vClient.module.Category;
import com.vClient.module.Module;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

public class AutoTool extends Module {
    public AutoTool() {
        super("AutoTool", Keyboard.CHAR_NONE, Category.PLAYER, "Choose optimal item in hotbar for mining.");
    }

    @EventTarget
    public void onClickBlock(EventClickBlock event) {
        float best = 1F;
        int bestSlot = -1;
        Block block = mc.theWorld.getBlockState(event.getBlockPos()).getBlock();

        for (int i = 0; i < 9; i++) {
            ItemStack item = mc.thePlayer.inventory.getStackInSlot(i);
            if (item == null)
                continue;
            float curr = item.getStrVsBlock(block);

            if (curr > best) {
                best = curr;
                bestSlot = i;
            }
        }

        if (bestSlot != -1)
            mc.thePlayer.inventory.currentItem = bestSlot;
    }
}
